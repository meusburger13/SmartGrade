# Requisitos Funcionais — SmartGrade

1. Gestão de Alunos

| ID   | Requisito                                                                                                | Prioridade |
| ---- | -------------------------------------------------------------------------------------------------------- | ---------- |
| RF01 | O sistema deve permitir cadastrar um aluno, informando seus dados cadastrais obrigatórios.               | Alta       |
| RF02 | O sistema deve gerar ou permitir informar uma matrícula única para cada aluno.                           | Alta       |
| RF03 | O sistema deve permitir consultar alunos cadastrados.                                                    | Alta       |
| RF04 | O sistema deve permitir pesquisar alunos por nome ou matrícula.                                          | Média      |
| RF05 | O sistema deve permitir visualizar os dados completos de um aluno.                                       | Alta       |
| RF06 | O sistema deve permitir editar os dados cadastrais de um aluno.                                          | Alta       |
| RF07 | O sistema deve permitir excluir um aluno, respeitando as regras de integridade dos registros acadêmicos. | Média      |
| RF08 | O sistema deve permitir visualizar o histórico acadêmico de um aluno.                                    | Média      |


2. Gestão de Turmas

| ID   | Requisito                                                                                       | Prioridade |
| ---- | ----------------------------------------------------------------------------------------------- | ---------- |
| RF09 | O sistema deve permitir cadastrar uma turma.                                                    | Alta       |
| RF10 | O sistema deve permitir definir o período/semestre da turma.                                    | Alta       |
| RF11 | O sistema deve permitir consultar turmas cadastradas.                                           | Alta       |
| RF12 | O sistema deve permitir editar os dados de uma turma.                                           | Média      |
| RF13 | O sistema deve permitir excluir uma turma respeitando a integridade dos registros relacionados. | Média      |
| RF14 | O sistema deve permitir vincular alunos a uma turma.                                            | Alta       |
| RF15 | O sistema deve permitir remover um aluno de uma turma, respeitando as regras acadêmicas.        | Média      |
| RF16 | O sistema deve permitir consultar os alunos matriculados em uma turma.                          | Alta       |

3. Gestão de Disciplinas

| ID   | Requisito                                                                                          | Prioridade |
| ---- | -------------------------------------------------------------------------------------------------- | ---------- |
| RF17 | O sistema deve permitir cadastrar disciplinas.                                                     | Alta       |
| RF18 | O sistema deve permitir informar nome, código e demais dados necessários da disciplina.            | Alta       |
| RF19 | O sistema deve permitir consultar disciplinas cadastradas.                                         | Alta       |
| RF20 | O sistema deve permitir editar os dados de uma disciplina.                                         | Média      |
| RF21 | O sistema deve permitir excluir uma disciplina respeitando a integridade dos registros acadêmicos. | Média      |
| RF22 | O sistema deve permitir vincular disciplinas às turmas.                                            | Alta       |
| RF23 | O sistema deve permitir consultar as disciplinas associadas a uma turma.                           | Alta       |

4. Avaliações e Notas

| ID   | Requisito                                                                               | Prioridade |
| ---- | --------------------------------------------------------------------------------------- | ---------- |
| RF24 | O sistema deve permitir cadastrar avaliações para uma disciplina.                       | Alta       |
| RF25 | O sistema deve permitir definir o tipo da avaliação, como prova, trabalho ou atividade. | Média      |
| RF26 | O sistema deve permitir definir o peso de cada avaliação.                               | Alta       |
| RF27 | O sistema deve permitir lançar notas para os alunos.                                    | Alta       |
| RF28 | O sistema deve permitir alterar uma nota lançada.                                       | Alta       |
| RF29 | O sistema deve permitir consultar as notas de um aluno.                                 | Alta       |
| RF30 | O sistema deve permitir consultar as notas de todos os alunos de uma turma/disciplina.  | Alta       |
| RF31 | O sistema deve calcular automaticamente a média ponderada do aluno.                     | Alta       |
| RF32 | O sistema deve atualizar a média automaticamente após alteração de uma nota.            | Alta       |
| RF33 | O sistema deve impedir o lançamento de notas fora da escala definida pela instituição.  | Alta       |

5. Frequência

| ID   | Requisito                                                                                        | Prioridade |
| ---- | ------------------------------------------------------------------------------------------------ | ---------- |
| RF34 | O sistema deve permitir registrar a frequência dos alunos.                                       | Alta       |
| RF35 | O sistema deve permitir registrar faltas por aula/data.                                          | Alta       |
| RF36 | O sistema deve permitir consultar o histórico de frequência do aluno.                            | Alta       |
| RF37 | O sistema deve calcular automaticamente o percentual de frequência do aluno.                     | Alta       |
| RF38 | O sistema deve calcular automaticamente o percentual de faltas do aluno.                         | Alta       |
| RF39 | O sistema deve permitir consultar a frequência de todos os alunos de uma turma/disciplina.       | Alta       |
| RF40 | O sistema deve identificar alunos que estejam próximos ou abaixo do limite mínimo de frequência. | Alta       |

6. Situação Acadêmica

| ID   | Requisito                                                                                               | Prioridade |
| ---- | ------------------------------------------------------------------------------------------------------- | ---------- |
| RF41 | O sistema deve determinar a situação acadêmica do aluno com base em suas notas e frequência.            | Alta       |
| RF42 | O sistema deve identificar alunos aprovados.                                                            | Alta       |
| RF43 | O sistema deve identificar alunos reprovados por nota.                                                  | Alta       |
| RF44 | O sistema deve identificar alunos reprovados por falta.                                                 | Alta       |
| RF45 | O sistema deve identificar alunos que estejam em situação de recuperação, caso essa regra seja adotada. | Média      |
| RF46 | O sistema deve atualizar automaticamente a situação acadêmica após alterações nas notas ou frequência.  | Alta       |
| RF47 | O sistema deve permitir consultar a situação acadêmica dos alunos de uma turma.                         | Alta       |


7. Módulo Preditivo / Machine Learning

| ID   | Requisito                                                                                                                      | Prioridade |
| ---- | ------------------------------------------------------------------------------------------------------------------------------ | ---------- |
| RF48 | O sistema deve coletar os dados acadêmicos necessários para a análise preditiva.                                               | Alta       |
| RF49 | O sistema deve utilizar dados históricos de desempenho acadêmico como entrada para a análise preditiva.                        | Alta       |
| RF50 | O sistema deve considerar o desempenho em notas como variável para análise de risco.                                           | Alta       |
| RF51 | O sistema deve considerar o percentual de faltas como variável para análise de risco.                                          | Alta       |
| RF52 | O sistema deve processar os dados acadêmicos para gerar uma classificação de risco.                                            | Alta       |
| RF53 | O sistema deve classificar o aluno em níveis de risco: baixo, médio ou alto.                                                   | Alta       |
| RF54 | O sistema deve apresentar o nível de risco associado a cada aluno.                                                             | Alta       |
| RF55 | O sistema deve atualizar a classificação de risco conforme novos dados acadêmicos forem registrados.                           | Alta       |
| RF56 | O sistema deve identificar alunos que apresentem indicadores de risco de reprovação ou evasão.                                 | Alta       |
| RF57 | O sistema deve disponibilizar informações que auxiliem na identificação de alunos que necessitem de acompanhamento pedagógico. | Alta       |
| RF58 | O sistema deve registrar os resultados das análises preditivas para consulta.                                                  | Média      |


8. Dashboard

| ID   | Requisito                                                                         | Prioridade |
| ---- | --------------------------------------------------------------------------------- | ---------- |
| RF59 | O sistema deve disponibilizar um dashboard com indicadores acadêmicos.            | Alta       |
| RF60 | O sistema deve apresentar a quantidade de alunos por situação acadêmica.          | Alta       |
| RF61 | O sistema deve apresentar indicadores de frequência das turmas.                   | Média      |
| RF62 | O sistema deve apresentar indicadores de desempenho acadêmico.                    | Alta       |
| RF63 | O sistema deve apresentar a quantidade de alunos por nível de risco.              | Alta       |
| RF64 | O sistema deve destacar alunos classificados como alto risco.                     | Alta       |
| RF65 | O sistema deve permitir visualizar os alunos que necessitam de acompanhamento.    | Alta       |
| RF66 | O dashboard deve atualizar seus indicadores após alterações nos dados acadêmicos. | Média      |


9. Alertas

| ID   | Requisito                                                                                  | Prioridade |
| ---- | ------------------------------------------------------------------------------------------ | ---------- |
| RF67 | O sistema deve alertar quando um aluno atingir uma situação de risco acadêmico.            | Alta       |
| RF68 | O sistema deve alertar quando o percentual de frequência estiver próximo do limite mínimo. | Alta       |
| RF69 | O sistema deve alertar quando o aluno apresentar desempenho acadêmico insuficiente.        | Alta       |
| RF70 | O sistema deve destacar alunos classificados como alto risco pelo módulo preditivo.        | Alta       |


10. Relatórios e Consultas

| ID   | Requisito                                                                      | Prioridade |
| ---- | ------------------------------------------------------------------------------ | ---------- |
| RF71 | O sistema deve permitir consultar o desempenho acadêmico de uma turma.         | Média      |
| RF72 | O sistema deve permitir consultar o histórico acadêmico individual do aluno.   | Média      |
| RF73 | O sistema deve permitir consultar relatórios de frequência.                    | Média      |
| RF74 | O sistema deve permitir consultar relatórios de notas e médias.                | Média      |
| RF75 | O sistema deve permitir consultar um relatório de alunos em situação de risco. | Alta       |
