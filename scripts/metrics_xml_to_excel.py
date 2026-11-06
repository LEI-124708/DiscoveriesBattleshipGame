#!/usr/bin/env python3
"""Converte a exportação XML do MetricsTree num livro Excel individual.

O XML produzido por "Export All Levels Metrics To XML File" tem o formato:

    <Project>
      <Metrics><Metric name=".." description=".." value=".."/></Metrics>
      <Package name="iscteiul">
        <Package name="ista">
          <Package name="battleship">
            <Class name="Game">
              <Method name="countHits()">
                <Metrics>...</Metrics>
              </Method>
            </Class>
          </Package>
        </Package>
      </Package>
    </Project>

Notas: os pacotes vêm aninhados por segmento (o nome completo é reconstruído por
".") e os valores usam vírgula decimal.

Folhas geradas:
  Original  — tabela plana com todas as métricas tal como vêm do XML
  System    — métricas do projeto + resumo
  Package   — uma linha por pacote
  Class     — uma linha por classe
  Method    — uma linha por método
  Gráficos  — CC por método (decrescente) e áreas empilhadas CC/parâmetros/LOC

Uso:
    python metrics_xml_to_excel.py metrics.xml -o LEI-129823-Metrics.xlsx
"""

from __future__ import annotations

import argparse
import sys
import xml.etree.ElementTree as ET
from dataclasses import dataclass, field
from pathlib import Path

from openpyxl import Workbook
from openpyxl.chart import AreaChart, BarChart, Reference
from openpyxl.styles import Alignment, Font, PatternFill
from openpyxl.utils import get_column_letter

LEVELS = ("Project", "Package", "Class", "Method")
HEADER_FILL = PatternFill("solid", fgColor="1F4E78")
HEADER_FONT = Font(bold=True, color="FFFFFF")
LOC_BY_LEVEL = {
    "Package": ("PLOC", "LOC"),
    "Class": ("CLOC", "CHL", "LOC"),
}


@dataclass
class Node:
    level: str
    name: str
    context: dict[str, str] = field(default_factory=dict)
    metrics: dict[str, str] = field(default_factory=dict)
    descriptions: dict[str, str] = field(default_factory=dict)


def _local(tag: str) -> str:
    return tag.rsplit("}", 1)[-1]


def _parse(elem: ET.Element) -> tuple[dict[str, str], dict[str, str]]:
    metrics: dict[str, str] = {}
    descriptions: dict[str, str] = {}
    for child in elem:
        if _local(child.tag) != "Metrics":
            continue
        for metric in child:
            if _local(metric.tag) != "Metric":
                continue
            name = metric.get("name")
            if not name:
                continue
            metrics[name] = metric.get("value", "")
            descriptions[name] = metric.get("description", "")
    return metrics, descriptions


def collect(xml_path: Path) -> list[Node]:
    root = ET.parse(xml_path).getroot()
    nodes: list[Node] = []

    def walk(elem: ET.Element, ctx: dict) -> None:
        tag = _local(elem.tag)
        if tag in LEVELS:
            name = elem.get("name") or ""
            current = dict(ctx)
            if tag == "Project":
                current["project"] = name or "Project"
            elif tag == "Package":
                segments = list(ctx.get("segments", [])) + [name]
                current["segments"] = segments
                current["package"] = ".".join(segments)
            else:
                current[tag.lower()] = name
            metrics, descriptions = _parse(elem)
            nodes.append(Node(tag, name, current, metrics, descriptions))
            ctx = current
        for child in elem:
            if _local(child.tag) != "Metrics":
                walk(child, ctx)

    walk(root, {})
    if not any(n.level == "Project" for n in nodes):
        raise SystemExit(f"{xml_path}: raiz <Project> não encontrada")
    return nodes


def _number(value: str):
    if value is None:
        return ""
    text = str(value).strip()
    if not text:
        return ""
    try:
        if "," in text:
            number = float(text.replace(".", "").replace(",", "."))
        else:
            number = float(text)
    except ValueError:
        return text
    return int(number) if number.is_integer() else number


def _names(nodes: list[Node]) -> list[str]:
    seen: list[str] = []
    for node in nodes:
        for name in node.metrics:
            if name not in seen:
                seen.append(name)
    return seen


def _style_header(ws, row: int = 1) -> None:
    for cell in ws[row]:
        cell.fill = HEADER_FILL
        cell.font = HEADER_FONT
        cell.alignment = Alignment(horizontal="center", vertical="center")


def _write_table(ws, headers: list[str], rows: list[list]) -> None:
    ws.append(headers)
    _style_header(ws, 1)
    for row in rows:
        ws.append(row)
    ws.freeze_panes = "A2"
    if rows:
        ws.auto_filter.ref = f"A1:{get_column_letter(len(headers))}{len(rows) + 1}"
    for idx, header in enumerate(headers, start=1):
        ws.column_dimensions[get_column_letter(idx)].width = max(
            12, min(40, len(str(header)) + 4)
        )


def _pick_loc(names: list[str], level: str) -> str | None:
    for candidate in LOC_BY_LEVEL.get(level, ()):
        if candidate in names:
            return candidate
    for name in names:
        if "LOC" in name:
            return name
    return names[0] if names else None


def _normalise(values: list[float]) -> list[float]:
    if not values:
        return []
    low, high = min(values), max(values)
    if high == low:
        return [0.0 for _ in values]
    return [round((v - low) / (high - low), 4) for v in values]


def _add_charts(wb: Workbook, methods: list[Node]) -> None:
    cc = []
    for node in methods:
        try:
            cc.append(float(node.metrics.get("CC", "0").replace(",", ".")))
        except ValueError:
            cc.append(0.0)
    order = sorted(range(len(methods)), key=lambda i: cc[i], reverse=True)[:15]
    chosen = [methods[i] for i in order]

    def val(node: Node, key: str) -> float:
        try:
            return float(node.metrics.get(key, "0").replace(",", "."))
        except ValueError:
            return 0.0

    nopm = [val(n, "NOPM") for n in chosen]
    loc = [val(n, "LOC") for n in chosen]
    cc_sel = [cc[i] for i in order]
    labels = [f"{n.context.get('class', '')}.{n.name}" for n in chosen]

    ws = wb.create_sheet("Gráficos")
    ws["A1"] = "Gráficos do nível Method"
    ws["A1"].font = Font(bold=True)
    headers = [
        "Método",
        "CC",
        "Parâmetros",
        "LOC",
        "CC (norm.)",
        "Parâmetros (norm.)",
        "LOC (norm.)",
    ]
    ws.append([])
    ws.append([])
    start_col = 8  # H
    for offset, header in enumerate(headers):
        cell = ws.cell(row=1, column=start_col + offset, value=header)
        cell.fill = HEADER_FILL
        cell.font = HEADER_FONT
    for i, label in enumerate(labels):
        row = 2 + i
        ws.cell(row=row, column=start_col, value=label)
        ws.cell(row=row, column=start_col + 1, value=int(cc_sel[i]) if cc_sel[i].is_integer() else cc_sel[i])
        ws.cell(row=row, column=start_col + 2, value=int(nopm[i]) if nopm[i].is_integer() else nopm[i])
        ws.cell(row=row, column=start_col + 3, value=int(loc[i]) if loc[i].is_integer() else loc[i])
        ws.cell(row=row, column=start_col + 4, value=_normalise(cc_sel)[i])
        ws.cell(row=row, column=start_col + 5, value=_normalise(nopm)[i])
        ws.cell(row=row, column=start_col + 6, value=_normalise(loc)[i])
    for offset, width in enumerate((34, 8, 12, 8, 12, 16, 12)):
        ws.column_dimensions[get_column_letter(start_col + offset)].width = width

    last = len(labels) + 1
    bar = BarChart()
    bar.type = "bar"
    bar.title = "Complexidade ciclomática por método (decrescente)"
    bar.x_axis.title = "CC"
    bar.y_axis.title = "Método"
    bar.add_data(
        Reference(ws, min_col=start_col + 1, min_row=1, max_row=last),
        titles_from_data=True,
    )
    bar.set_categories(Reference(ws, min_col=start_col, min_row=2, max_row=last))
    bar.height = 11
    bar.width = 20
    ws.add_chart(bar, "A3")

    area = AreaChart()
    area.grouping = "stacked"
    area.overlap = 100
    area.title = "CC / parâmetros / LOC normalizados (áreas empilhadas)"
    area.y_axis.title = "Valor normalizado"
    area.x_axis.title = "Método"
    area.add_data(
        Reference(ws, min_col=start_col + 4, max_col=start_col + 6, min_row=1, max_row=last),
        titles_from_data=True,
    )
    area.set_categories(Reference(ws, min_col=start_col, min_row=2, max_row=last))
    area.height = 11
    area.width = 20
    ws.add_chart(area, "A27")


def build_workbook(nodes: list[Node], destination: Path) -> None:
    by_level = {level: [n for n in nodes if n.level == level] for level in LEVELS}
    wb = Workbook()
    wb.remove(wb.active)

    # ---- Original ---------------------------------------------------------
    ws = wb.create_sheet("Original")
    rows = []
    for node in nodes:
        for metric, value in node.metrics.items():
            rows.append(
                [
                    node.level,
                    node.context.get("package", ""),
                    node.context.get("class", ""),
                    node.name if node.level == "Method" else "",
                    metric,
                    node.descriptions.get(metric, ""),
                    _number(value),
                ]
            )
    _write_table(
        ws,
        ["Nível", "Pacote", "Classe", "Método", "Métrica", "Descrição", "Valor"],
        rows,
    )

    project = by_level["Project"][0] if by_level["Project"] else None
    packages, classes, methods = (
        by_level["Package"],
        by_level["Class"],
        by_level["Method"],
    )

    # ---- System -----------------------------------------------------------
    ws = wb.create_sheet("System")
    ws["A1"] = "Sistema"
    ws["A1"].font = Font(bold=True, size=14)
    ws["A2"] = "Projeto"
    ws["B2"] = project.name if project else ""
    ws.append([])
    ws.append(["Métrica", "Descrição", "Valor"])
    _style_header(ws, 4)
    row = 5
    if project:
        for name, value in project.metrics.items():
            ws.cell(row=row, column=1, value=name)
            ws.cell(row=row, column=2, value=project.descriptions.get(name, ""))
            ws.cell(row=row, column=3, value=_number(value))
            row += 1
    row += 1
    ws.cell(row=row, column=1, value="Resumo").font = Font(bold=True)
    row += 1
    for label, total in (
        ("Pacotes", len(packages)),
        ("Classes", len(classes)),
        ("Métodos", len(methods)),
    ):
        ws.cell(row=row, column=1, value=label)
        ws.cell(row=row, column=2, value=total)
        row += 1
    for col, width in (("A", 26), ("B", 46), ("C", 16)):
        ws.column_dimensions[col].width = width

    # ---- Tabelas por nível ------------------------------------------------
    table_specs = {
        "Package": (["Package"], lambda n: [n.context.get("package", "")]),
        "Class": (
            ["Package", "File", "Class"],
            lambda n: [
                n.context.get("package", ""),
                n.context.get("file", ""),
                n.name,
            ],
        ),
        "Method": (
            ["Package", "File", "Class", "Method"],
            lambda n: [
                n.context.get("package", ""),
                n.context.get("file", ""),
                n.context.get("class", ""),
                n.name,
            ],
        ),
    }

    for level in ("Package", "Class", "Method"):
        level_nodes = by_level[level]
        if not level_nodes:
            continue
        prefix, extract = table_specs[level]
        metrics = _names(level_nodes)
        raw = [
            extract(n) + [_number(n.metrics.get(m, "")) for m in metrics]
            for n in level_nodes
        ]
        keep = [
            i for i in range(len(prefix)) if any(str(r[i]).strip() for r in raw)
        ]
        headers = [prefix[i] for i in keep] + metrics
        rows = [[r[i] for i in keep] + r[len(prefix):] for r in raw]
        ws = wb.create_sheet(level)
        _write_table(ws, headers, rows)

    _add_charts(wb, methods)

    destination.parent.mkdir(parents=True, exist_ok=True)
    wb.save(destination)


def main(argv: list[str]) -> int:
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument("xml", type=Path, help="exportação XML do MetricsTree")
    parser.add_argument("-o", "--output", type=Path, required=True)
    args = parser.parse_args(argv)

    if not args.xml.is_file():
        print(f"XML não encontrado: {args.xml}", file=sys.stderr)
        return 1
    nodes = collect(args.xml)
    build_workbook(nodes, args.output)
    print(f"{len(nodes)} nós convertidos -> {args.output}")
    return 0


if __name__ == "__main__":
    raise SystemExit(main(sys.argv[1:]))
