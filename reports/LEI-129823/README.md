# Cobertura validada — LEI-129823

Data: 2026-11-06. Java 21; JUnit 6.0.0; JaCoCo 0.8.14; Maven 3.9.16.

79 execuções, zero falhas/erros/ignorados. Baseline: 1 teste trivial, sem medição de cobertura.

Comando: `mvn --batch-mode clean verify` (local: Maven do IntelliJ, repositório Maven isolado).

Relatório HTML: [JaCoCo](jacoco/index.html). XML/CSV e resultados Surefire incluídos.

| Classe | Linhas cobertas/total | Ramos cobertos/total |
|---|---:|---:|
| App | 3/4 | n/a |
| Caravel | 13/14 | 8/9 |
| Frigate | 12/12 | 7/7 |
| Position | 23/23 | 12/12 |
| Compass | 18/18 | 5/5 |
| Game | 44/50 | 30/32 |
| Galleon | 35/36 | 16/17 |
| Carrack | 12/12 | 7/7 |
| Tasks | 86/93 | 47/51 |
| Fleet | 48/48 | 34/34 |
| IFleet | 2/2 | n/a |
| Ship | 71/72 | 51/52 |
| Barge | 5/5 | n/a |

Total: 372/389 linhas (95,63%); 217/226 ramos (96,02%). Sem excluir código produtivo.

Os testes KNOWN DEFECT reproduzem #22, não atestam correção. Os ramos de Game após countHits++ não são alcançáveis numa partida normal devido aos contadores nulos. Com assertions ativas, os checks null de Caravel/Galleon são precedidos pelo assert de Ship; não foram removidos nem excluídos para aumentar cobertura. O construtor implícito de App/Tasks e os restantes caminhos da consola explicam outras lacunas.

Âmbito individual: Ship/Barge têm antecedente no PR #17 de LEI-129823. Os testes das restantes classes são contribuição de apoio ao grupo; não existe evidência de rateio coletivo aprovado. Este relatório de toda a bateria não substitui o relatório IntelliJ filtrado pelas classes individuais após esse acordo.

Actions: confirmar execução remota correspondente ao commit do PR; o build local não a substitui. #14/#15 continuam pendentes.

Os caminhos locais dos logs/XML foram normalizados para `${PROJECT_ROOT}`; resultados, contagens e cobertura não foram alterados.
