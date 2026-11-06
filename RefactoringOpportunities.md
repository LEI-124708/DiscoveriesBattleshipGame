# Oportunidades de refabricação

Revisão direta do código em `19072f9`; não são resultados Qodana inventados.
Distribuição proposta, a ratificar pelo grupo. Alterações funcionais de #22
não são refabricações. #14/#15 continuam pendentes.

| Local | Nome do cheiro | Nome da refabricação | Número do aluno |
|---|---|---|---|
| Fleet::colisionRisk | Nome pouco claro/erro ortográfico | Rename Method → hasCollisionRisk | 129823 (proposta) |
| Game::getRemainingShips | Variável temporária redundante | Inline Variable | 129823 (proposta) |
| Game::printBoard | Long Method / preparação do mapa misturada com apresentação | Extract Method → createEmptyBoard | 129823 (proposta) |
| Ship::buildShip | Variável temporária e atribuições repetidas | Replace Conditional with Expression (switch expression) | 129823 (proposta) |
| App::main | Import sem uso e código comentado obsoleto | Safe Delete (import sem uso; sem alterar comandos) | 129823 (proposta) |
| Caravel/Carrack/Frigate::<init> | Duplicated Code | Pull Up Method para construção linear | Grupo — por atribuir |
| Tasks::taskD | Long Method (Lizard CC 12) | Extract Method / Extract Class | Grupo — por atribuir |
| Tasks::taskB/taskC/taskD | Duplicated Code | Extract Method para ciclo de comandos | Grupo — por atribuir |
| Ship::positions e getters de Fleet/Game | Exposed Mutable State | Encapsulate Collection | Grupo — requer análise de compatibilidade |
| Position::equals/hashCode | Contrato de igualdade inconsistente (bug) | Correção funcional separada, não contabilizar como refabricação | Grupo — #22 |
| Game::<init>/validShot | Contadores nulos e limite incorreto (bugs) | Correção funcional separada, não contabilizar como refabricação | Grupo — #22 |

O branch Refactoring_129823 deriva do branch de testes, pois a ficha exige
primeiro uma bateria enriquecida. Os PRs mantêm dependência explícita e não
serão fundidos automaticamente. A aprovação dos testes e das refabricações deve
ser feita por membros diferentes. Cinco entradas propostas não provam cinco
tipos aceites pelo docente; Safe Delete de import e switch expression devem ser
validados como elegíveis. Não se declara cumprida a quota individual de 5–10.
