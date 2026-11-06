# Validação das refabricações

Branch Refactoring_129823, dependente de UnitTests_129823. 79 testes passam em todas as verificações antes/depois, sem ignorados. Logs preservados nesta pasta. Métricas e relatório principal no branch de testes referem-se às fontes anteriores às refabricações; o HTML nesta pasta corresponde às fontes finais.

| Alteração | Commit |
|---|---|
| Rename Method: clarify Fleet collision predicate (#21) | c26f35c |
| Inline Variable: simplify remaining ship count (#21) | 0d4d00d |
| Extract Method: isolate empty board creation (#21) | 3a74a3e |
| Replace Conditional with Expression: simplify ship factory (#21) | 748c6f1 |
| Introduce Variable: explain row adjacency predicate (#21) | d7fd23b |

LINE: 368/385 (95.58%).

BRANCH: 219/228 (96.05%).

[JaCoCo final](jacoco/index.html). Os defeitos #22 foram intencionalmente preservados para separar correções funcionais de refabricações. A segurança está apoiada pela revisão do diff e pelos cenários cobertos; não é prova formal de equivalência para todas as entradas. A aceitação académica de cinco tipos diferentes e a ratificação do rateio ainda dependem do grupo/docente.
