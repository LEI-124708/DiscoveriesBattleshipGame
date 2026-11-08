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

## Evidência ainda necessária (individual)

- Selecionar o último commit do requisito da Ficha 2 antes do respetivo PR.
  #14/#15 não têm implementação/PR encontrados. `5dfcbeb` é a ponta do PR #17
  de documentação Ship/Barge, não prova a implementação dos requisitos.
- Instalar/verificar Qodana e MetricsTree no IntelliJ; executar Inspect Code
  (Java/Security), Qodana, guardar configuração real `qodana.yaml`, relatório e comentários.
- Exportar XML **real** MetricsTree no commit histórico correto.
- Criar `LEI-129823-Metrics.xlsx`, com folha original + System/Package/Class/Method,
  gráfico CC decrescente e áreas empilhadas normalizadas CC/parâmetros/LOC.
- Confirmar limiares e fitness functions das figuras da ficha no plugin.

## Trabalho do grupo

A pasta CodeMetrics está proposta neste PR; só ficará em main após revisão e
merge por um membro autorizado. Cada membro deve entregar as suas métricas e
analisar as classes do seu requisito, criando issues de qualidade fundamentados.
A análise global e distribuição de oportunidades exigem acordo coletivo.
Nenhum passo de IDE, reunião, XML ou Excel é declarado concluído por estes CSVs.
