# Qodana — análise estática (LEI-129823)

Análise estática do ramo `Refactoring_129823` com o linter Qodana para JVM, em modo
headless. Complementa a inspeção IntelliJ/Qodana do IDE; não a substitui.

## Âmbito

| Item | Valor |
| --- | --- |
| Ramo | `Refactoring_129823` |
| Commit analisado | `1e4f27aa12d2edfe18fab5c8847abc4909cd0692` |
| Linter | Qodana Community for JVM 2025.1 (`jetbrains/qodana-jvm-community:2025.1`, build 251.25775) |
| Imagem | `sha256:a8b61ba98bee096bdae4f1132c9bc4f16ee7956844573c8b901fd27df4d2da78` |
| Resultados enviados para a cloud | Não |
| Data | 2026-11-06 |

## Como foi executado

O projeto foi copiado para um diretório de trabalho e analisado com:

```sh
docker run --rm \
  -v "$PWD/qodana-project:/data/project" \
  -v "$PWD/qodana-results:/data/results" \
  -v "$PWD/qodana-cache:/data/cache" \
  jetbrains/qodana-jvm-community:2025.1 --save-report
```

A variante Community foi usada porque a imagem `jetbrains/qodana-jvm` exige um token
da Qodana Cloud. Nenhum resultado saiu da máquina.

## Resultado

**8 problemas**: 3 de severidade alta e 5 moderada. Nenhum erro de compilação.

| Severidade | Regra | Ficheiro:linha | Mensagem |
| --- | --- | --- | --- |
| Alta | `ConstantValue` | `Galleon.java:65` | Condição `bearing == null` é sempre falsa |
| Alta | `ConstantValue` | `Caravel.java:54` | Condição `bearing == null` é sempre falsa |
| Alta | `RedundantStringFormatCall` | `App.java:16` | Chamada redundante a `printf()` sem argumentos |
| Moderada | `LoggingSimilarMessage` | `Tasks.java:70` | Mensagens de log semelhantes |
| Moderada | `LoggingSimilarMessage` | `Tasks.java:75` | Mensagens de log semelhantes |
| Moderada | `LoggingSimilarMessage` | `Tasks.java:99` | Mensagens de log semelhantes |
| Moderada | `LoggingSimilarMessage` | `Tasks.java:104` | Mensagens de log semelhantes |
| Moderada | `LoggingSimilarMessage` | `Tasks.java:149` | Mensagens de log semelhantes |

### Leitura dos achados

- `Galleon` e `Caravel` validam `bearing == null` **depois** de `super(...)`, que já
  rejeita o valor; a condição nunca é verdadeira. É código morto, não um defeito
  funcional novo.
- `App.main` usa `System.out.printf` sem argumentos de formatação; basta `print`.
- As cinco mensagens de `Tasks` são textos de log muito próximos entre si.

Nenhum destes achados foi corrigido neste ramo: os dois primeiros alteram
comportamento em caso de `null` e os restantes são cosméticos, pelo que ficam
registados para decisão separada.

## Limitações

- A execução foi feita pela CLI/Docker, não pela janela *Tools → Qodana* do IDE.
  O linter e o perfil (`qodana.starter`) são os mesmos, mas a captura de ecrã do IDE
  continua pendente.
- A exportação MetricsTree (XML/CSV) não é reproduzível sem a UI do IntelliJ e não
  está incluída aqui.
- O relatório HTML completo não é versionado; é regenerável com o comando acima.
