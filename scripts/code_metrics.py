"""Supplementary Lizard metrics; not a MetricsTree XML export."""
import csv
import json
import subprocess
from pathlib import Path
import lizard

root = Path(__file__).resolve().parents[1]
rows = []
for source in sorted((root / 'src/main/java').rglob('*.java')):
    analysis = lizard.analyze_file(str(source))
    for f in analysis.function_list:
        rows.append(dict(file=str(source.relative_to(root)), method=f.long_name,
                         line=f.start_line, CC=f.cyclomatic_complexity,
                         parameters=f.parameter_count, NLOC=f.nloc))
for key in ('CC', 'parameters', 'NLOC'):
    low, high = min(x[key] for x in rows), max(x[key] for x in rows)
    for row in rows:
        row[key + '_normalized'] = (row[key] - low) / (high - low) if high != low else 0
for row in rows:
    row['normalized_sum'] = sum(row[k + '_normalized'] for k in ('CC', 'parameters', 'NLOC'))
rows.sort(key=lambda r: (-r['CC'], r['file'], r['line']))
out = root / 'CodeMetrics'
out.mkdir(exist_ok=True)
with (out / 'method-metrics.csv').open('w', newline='') as stream:
    writer = csv.DictWriter(stream, fieldnames=list(rows[0]))
    writer.writeheader()
    writer.writerows(rows)
(out / 'provenance.json').write_text(json.dumps({
    'source_tree': subprocess.check_output(['git', 'rev-parse', 'HEAD:src/main'], cwd=root, text=True).strip(),
    'tool': 'lizard 1.17.31', 'scope': 'all src/main/java; supplementary current-source metrics',
    'normalization': '(value-min)/(max-min); zero when max=min',
    'not_metrics_tree': True, 'methods': len(rows)
}, indent=2) + '\n')
# Portable SVG ranking; escaped method labels and numerical values come from measured data.
from html import escape
svg=['<svg xmlns="http://www.w3.org/2000/svg" width="1250" height="%s">' % (60+len(rows)*23),
     '<rect width="100%" height="100%" fill="white"/>',
     '<text x="20" y="25" font-size="18">Lizard CC — current source — supplementary, not MetricsTree</text>']
for i,row in enumerate(rows):
    y=48+i*23
    label=Path(row['file']).stem+'::'+row['method'].split('(')[0]
    svg.extend([f'<text x="20" y="{y+12}" font-size="12">{escape(label)}</text>',
                f'<rect x="650" y="{y}" width="{row["CC"]*18}" height="16" fill="#2563eb"/>',
                f'<text x="{655+row["CC"]*18}" y="{y+12}" font-size="12">{row["CC"]}</text>'])
svg.append('</svg>')
(out/'complexity-ranking.svg').write_text('\n'.join(svg))
print('Measured',len(rows),'methods; most complex:',rows[0])
