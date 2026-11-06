# Auditoria das Fichas 3 e 4 — 2026-11-06

## Checkpoint e âmbito inspecionado

Remote solicitado: https://github.com/LEI-124708/DiscoveriesBattleshipGame.
Main `19072f9`; clone limpo e separado. A pasta EngSoft local tem o mesmo HEAD,
remote antigo josel1337 e ficheiros não versionados .metals/ e .idea/go.imports.xml;
foi apenas consultada. Não foram alterados esses ficheiros.

Inventário integral de ficheiros versionados: `file-inventory.csv`, com tamanho,
SHA-256 e distinção texto/assets binários. Leitura detalhada de todos os fontes
Java, POM, testes, workflows, configuração Maven, README e labels; documentação
HTML/JS/CSS gerada e imagens inventariadas como artefactos, não código funcional.
Não foi feita revisão visual individual de cada imagem/HTML gerado.

Pastas: src/main/java contém modelo e consola; src/test/java continha só AppTest
trivial (JUnit 3 através de JUnit 4); src/main/resources configura Log4j;
src/site contém descritor do site; docs contém Javadoc gerado (inclui AppTest,
portanto não é evidência de qualidade/cobertura); images contém ilustrações;
.idea e .mvn contêm configuração local/build. .mvn/jvm.config e maven.config
estão vazios. Não havia CodeMetrics, reports, RefactoringOpportunities.md,
configuração Qodana nem workflow de build/testes.

`branches.txt` e `history.txt` registam branches e histórico completos clonados.
Nove branches remotos: main, Fixing-style-issue, actions/pr-notification,
docs/javadoc-game, docs/javadoc-position, docs/regras-adicionais,
javadoc---Galleon,-Fleet-&-Caravel, javadoc---Ship,-Barge e javadocs.
Todos os históricos remotos estão integrados em main; não existem branches
UnitTests/Refactoring anteriores nem código IA/JSON nesses históricos.

PRs #1/#2/#3 (estilos), #4 (regras), #7 (notificação), #16 (Galleon/Fleet/Caravel),
#17 (Ship/Barge), #18 (Position), #19 (Game), #20 (restantes navios): todos merged,
confirmados pela API. Nenhum PR aberto na inspeção inicial. PR #17 contém caixas
de build/Javadoc/testes não assinaladas; merge não é prova dessas validações.

Issues #5/#6/#8/#9/#10/#11/#12/#13 descrevem tabuleiro, posicionamento, visualização,
vencedor, repetição de partida e rajada. Foram lidos como requisitos/contexto,
não certificados como concluídos. #14 e #15: abertos, atribuídos a LEI-129823,
sem comentários. #14 exige adversário IA completo, rajadas não repetidas, turnos
e vitória; #15 exige guardar/carregar duas frotas, tiros e jogador atual em JSON.
O modelo atual Game só guarda uma frota; App executa taskB, não taskD. Não há
prova de seleção de modo IA, alternância de jogadores ou persistência JSON.

## Responsabilidade individual e coletiva

LEI-129823: PRs anteriores #1/#2/#3 de estilos, #7 de notificação e #17 de Javadoc
Ship/Barge. As tarefas funcionais #14/#15 são suas mas continuam por implementar.
Testes Ship/Barge têm continuidade nesse antecedente; os restantes são apoio
extra ao grupo, não atribuições individuais aprovadas. CI deve ser formalizado
pelo membro de menor número (124708, dentre os identificados no histórico).
Etiqueta e issue #21 foram preparados para testes; faltam issues por cada outro
membro, reunião e rateio. Não foram atribuídas tarefas nem emitidas aprovações em
nome de colegas. Issues novos #21 (testes) e #22 (defeitos) não fecham #14/#15.

## Checklist Ficha 3

- [x] Inspeção direta dos fontes, inventário e identificação de problemas.
- [x] Proposta da pasta CodeMetrics, métricas Lizard reproduzíveis, CSV e ranking CC.
- [x] Issue #22 documenta defeitos reproduzidos pelos testes.
- [ ] Inspect Code Java/Security e Qodana no IDE, qodana.yaml e relatório real.
- [ ] MetricsTree e respetivos limiares/fitness functions confirmados.
- [ ] Identificar commit de implementação individual antes do PR (não inventar).
- [ ] XML MetricsTree e Excel individual com cinco folhas e dois gráficos exigidos.
- [ ] Análise coletiva e CodeMetrics integrado em main após revisão.

## Checklist Ficha 4

- [x] JUnit 6/Jupiter, Java 17 mínimo, validação com Java 21.
- [x] 79 execuções sem falhas; um commit por classe alvo e @DisplayName.
- [x] Workflow Maven em push/PR e README preparados; confirmar Actions remoto.
- [x] Relatório JaCoCo completo, XML/CSV/HTML e Surefire em reports/LEI-129823.
- [ ] 100% linhas/ramos: 95,63%/96,02% medidos; limitações descritas no relatório.
- [ ] Distribuição coletiva e relatório IntelliJ filtrado pelas classes individuais.
- [x] RefactoringOpportunities.md com as quatro colunas requeridas.
- [ ] Refactoring_129823: validar e registar cada alteração antes/depois e commit próprio.
- [ ] Confirmar 5–10 tipos de refabricação elegíveis por membro; não contar correções.
- [ ] Revisão dos PRs por colegas diferentes, sem autoaprovação nem merge automático.

## Achados adicionais

Position.hashCode inclui estado mutável ignorado por equals (#22).
Fleet permite onze navios com FLEET_SIZE=10 e <=; coincide com o total descrito
no README mas carece de nomenclatura coerente. Não foi alterado.
Ship expõe lista mutável; refabricar exige rever clientes e contrato.
Workflows preexistentes: labels.yml apaga todas as labels antes de recriar;
não foi executado. pull-request-notification.yml publica comentários automáticos;
não foi alterado por esta tarefa. Não existem resultados Qodana/Sonar validados.
