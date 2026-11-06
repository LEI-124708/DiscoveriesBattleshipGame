# Métricas — estado real da Ficha 3

Análise suplementar do código atual com **Lizard 1.17.31**, não uma exportação
MetricsTree. `method-metrics.csv` contém CC, número de parâmetros, NLOC,
normalização min-max em [0,1] (intervalo nulo resulta em zero) e soma normalizada.
`complexity-ranking.svg` ordena todos os métodos por CC decrescente.
`provenance.json` fixa a árvore de fontes medida. Não comparar NLOC de Lizard
com LOC de MetricsTree como se fossem a mesma definição.

Reproduzir num ambiente Python isolado:

```sh
python -m pip install lizard==1.17.31
python scripts/code_metrics.py
```

Foram identificados 80 métodos. `Tasks::taskD` é o mais complexo, CC=12 e
39 NLOC. Concentra leitura de comandos, controlo da frota, combate e apresentação.
É candidato a Extract Method, mas também contém caminhos impedidos pelos bugs
de Game (#22), pelo que uma decomposição extensa fica para revisão posterior.

## MetricsTree (exportação real)

`metricstree-1e4f27a.xml` é a exportação **Export All Levels Metrics To XML File**
do plugin MetricsTree, feita no commit `1e4f27a` do ramo `Refactoring_129823`:
140 nós, 3 pacotes, 16 classes, 120 métodos e 2 959 métricas.

`reports/LEI-129823/LEI-129823-Metrics.xlsx` é gerado a partir desse XML por
`scripts/metrics_xml_to_excel.py` e tem as folhas Original, System, Package,
Class, Method e Gráficos (CC decrescente e áreas empilhadas normalizadas
CC/parâmetros/LOC).

```sh
python scripts/metrics_xml_to_excel.py CodeMetrics/metricstree-1e4f27a.xml \
    -o reports/LEI-129823/LEI-129823-Metrics.xlsx
```

## Evidência ainda necessária (individual)

- Qodana: executado em modo headless; resultados em `reports/LEI-129823/qodana/`.
  Falta apenas a captura da janela *Tools → Qodana* no IDE.
- Selecionar o último commit do requisito da Ficha 2 antes do respetivo PR.
  #14/#15 não têm implementação/PR encontrados. `5dfcbeb` é a ponta do PR #17
  de documentação Ship/Barge, não prova a implementação dos requisitos.
- Exportar o XML MetricsTree **histórico** em `5dfcbeb`; o XML versionado é do
  head atual do ramo.
- Confirmar limiares e fitness functions das figuras da ficha no plugin.

## Trabalho do grupo

A pasta CodeMetrics está proposta neste PR; só ficará em main após revisão e
merge por um membro autorizado. Cada membro deve entregar as suas métricas e
analisar as classes do seu requisito, criando issues de qualidade fundamentados.
A análise global e distribuição de oportunidades exigem acordo coletivo.
Nenhum passo de IDE, reunião, XML ou Excel é declarado concluído por estes CSVs.
