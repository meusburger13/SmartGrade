# Regras de Negócio — SmartGrade

## 1. Objetivo

Este documento define as regras de negócio que determinam o comportamento
e as decisões acadêmicas do sistema SmartGrade.

As regras estabelecem os critérios para cadastro e relacionamento dos
dados acadêmicos, lançamento e cálculo de notas, controle de frequência,
determinação da situação acadêmica e análise preditiva de risco.

As regras de negócio são independentes da tecnologia utilizada na
implementação do sistema.

---

# 2. Cadastro e Identificação

### RN01 — Identificação única do aluno

Cada aluno deve possuir um Registro Acadêmico (RA) único no sistema.

### RN02 — E-mail único

Cada aluno deve possuir um endereço de e-mail único no sistema.

### RN03 — Identificação da turma

Cada turma deve possuir um código único e estar associada a um período
letivo.

### RN04 — Identificação da disciplina

Cada disciplina deve possuir um código único no sistema.

### RN05 — Dados obrigatórios

Os dados obrigatórios definidos para alunos, turmas e disciplinas devem
ser preenchidos antes da conclusão do cadastro.

### RN06 — Integridade dos cadastros

Um registro cadastral não deve ser excluído quando sua remoção resultar
em inconsistência nos registros acadêmicos relacionados.

---

# 3. Matrícula e Relacionamentos Acadêmicos

### RN07 — Associação de aluno à turma

Um aluno pode ser associado a uma ou mais turmas, desde que respeitadas
as regras acadêmicas do período letivo.

### RN08 — Matrícula única na turma

Um aluno não pode possuir mais de uma associação com a mesma turma.

### RN09 — Associação de disciplina à turma

Uma disciplina pode ser oferecida em uma ou mais turmas.

### RN10 — Registro acadêmico

Um registro acadêmico deve estar obrigatoriamente relacionado a um aluno,
uma disciplina e uma turma existentes no sistema.

### RN11 — Unicidade do registro acadêmico

Um aluno não pode possuir mais de um registro acadêmico para a mesma
disciplina dentro da mesma turma.

---

# 4. Avaliações

### RN12 — Associação da avaliação

Toda avaliação deve estar associada a uma disciplina cadastrada.

### RN13 — Identificação da avaliação

Toda avaliação deve possuir um tipo e uma descrição que permitam
identificá-la no contexto da disciplina.

### RN14 — Peso da avaliação

Toda avaliação deve possuir um peso utilizado no cálculo da média.

### RN15 — Soma dos pesos

A soma dos pesos das avaliações que compõem a média final deve totalizar
100% (1,00).

### RN16 — Escala de notas

As notas devem utilizar uma escala de 0,00 a 10,00.

### RN17 — Validação da nota

O sistema não deve permitir o lançamento de notas inferiores a 0,00 ou
superiores a 10,00.

### RN18 — Alteração de nota

Uma nota já lançada pode ser alterada por meio das funcionalidades
autorizadas do sistema.

### RN19 — Recalculo da média

A média final deve ser recalculada sempre que uma nota utilizada em seu
cálculo for incluída ou alterada.

---

# 5. Cálculo da Média

### RN20 — Média ponderada

A média final deve ser calculada utilizando as notas das avaliações e
seus respectivos pesos.

A fórmula geral será:

Média Final = Σ (Nota × Peso)

### RN21 — Pesos definidos para o projeto

Para o modelo acadêmico adotado pelo SmartGrade, serão consideradas três
avaliações principais:

- P1: peso de 30%;
- P2: peso de 40%;
- Trabalho: peso de 30%.

A fórmula adotada será:

Média Final = (P1 × 0,30) + (P2 × 0,40) + (Trabalho × 0,30)

### RN22 — Arredondamento

A média final deve ser apresentada com duas casas decimais.

O arredondamento deve ocorrer somente para apresentação e armazenamento
do resultado final, sem alterar os valores das notas individuais.

---

# 6. Frequência

### RN23 — Registro de faltas

As faltas devem ser registradas para cada aluno associado à disciplina.

### RN24 — Cálculo da frequência

O percentual de frequência deve ser calculado com base na quantidade de
aulas realizadas e nas faltas registradas.

### RN25 — Percentual de frequência

O percentual de frequência será calculado pela seguinte fórmula:

Frequência (%) = ((Total de aulas - Total de faltas) / Total de aulas) × 100

### RN26 — Limite mínimo de frequência

O percentual mínimo de frequência para aprovação será de 75%.

### RN27 — Reprovação por falta

O aluno que apresentar frequência inferior a 75% será considerado
reprovado por falta, independentemente de sua média final.

### RN28 — Alerta de frequência

O sistema deverá considerar como situação de atenção os alunos que
apresentarem frequência entre 75% e 80%.

### RN29 — Atualização da frequência

O percentual de frequência deve ser atualizado após o registro ou
alteração das faltas.

---

# 7. Situação Acadêmica

### RN30 — Critérios de situação acadêmica

A situação acadêmica deve considerar simultaneamente a média final e o
percentual de frequência do aluno.

### RN31 — Aprovação direta

O aluno será considerado "Aprovado" quando:

- Média final maior ou igual a 7,00; e
- Frequência maior ou igual a 75%.

### RN32 — Situação de exame

O aluno será considerado "Exame" quando:

- Média final maior ou igual a 5,00 e inferior a 7,00; e
- Frequência maior ou igual a 75%.

### RN33 — Reprovação por nota

O aluno será considerado "Reprovado por Nota" quando:

- Média final inferior a 5,00; e
- Frequência maior ou igual a 75%.

### RN34 — Reprovação por falta

O aluno será considerado "Reprovado por Falta" quando sua frequência
for inferior a 75%, independentemente de sua média final.

### RN35 — Prioridade da frequência

Quando um aluno não atingir o percentual mínimo de frequência, a
situação "Reprovado por Falta" terá prioridade sobre as situações
determinadas pela média.

### RN36 — Situação inicial

Enquanto os dados necessários para determinar a situação acadêmica ainda
não estiverem disponíveis, o aluno poderá permanecer na situação
"Cursando".

### RN37 — Atualização da situação

A situação acadêmica deve ser recalculada automaticamente após alterações
nas notas ou na frequência.

### RN38 — Exame fora do escopo inicial

A identificação da situação "Exame" faz parte do escopo do sistema,
porém o cálculo de uma eventual nota pós-exame não faz parte da primeira
versão do projeto.

---

# 8. Análise Preditiva

### RN39 — Objetivo da análise preditiva

O módulo preditivo tem como objetivo identificar precocemente alunos que
apresentem maior risco acadêmico, auxiliando no acompanhamento
pedagógico.

### RN40 — Dados utilizados na análise

A análise preditiva deve utilizar dados acadêmicos disponíveis no
sistema, podendo considerar:

- notas;
- médias;
- percentual de frequência;
- quantidade de faltas;
- evolução do desempenho acadêmico;
- quantidade de avaliações realizadas.

### RN41 — Score de risco

A análise preditiva deve produzir um score de risco representado por um
valor entre 0,00 e 1,00.

Quanto maior o score, maior será o risco acadêmico estimado.

### RN42 — Classificação do risco

O score de risco será classificado nas seguintes categorias:

| Score | Classificação |
|---|---|
| 0,00 a 0,39 | Baixo Risco |
| 0,40 a 0,69 | Médio Risco |
| 0,70 a 1,00 | Alto Risco |

### RN43 — Baixo risco

Alunos com score entre 0,00 e 0,39 serão classificados como "Baixo
Risco".

### RN44 — Médio risco

Alunos com score entre 0,40 e 0,69 serão classificados como "Médio
Risco".

Esses alunos deverão ser considerados como casos que merecem atenção
pedagógica.

### RN45 — Alto risco

Alunos com score entre 0,70 e 1,00 serão classificados como "Alto
Risco".

Esses alunos deverão ser destacados para acompanhamento pedagógico.

### RN46 — Atualização da análise

A análise preditiva deve ser atualizada quando novos dados acadêmicos
relevantes forem registrados ou alterados.

### RN47 — Histórico da análise

Cada análise preditiva deve possuir data de realização e poderá ser
armazenada para permitir o acompanhamento da evolução do risco do aluno.

### RN48 — Independência da previsão

A classificação de risco não deve alterar automaticamente a situação
acadêmica oficial do aluno.

A situação acadêmica oficial deve continuar sendo determinada pelas
regras de notas e frequência.

### RN49 — Interpretação da previsão

A classificação de risco deve ser utilizada como ferramenta de apoio à
decisão e acompanhamento pedagógico, não como confirmação de reprovação
ou evasão.

---

# 9. Alertas Acadêmicos

### RN50 — Alerta de baixo desempenho

O sistema deverá identificar alunos cuja média esteja abaixo de 7,00,
considerando-os como alunos que necessitam de atenção.

### RN51 — Alerta de frequência

O sistema deverá destacar alunos cuja frequência esteja entre 75% e
80%.

### RN52 — Alerta de reprovação por falta

O sistema deverá destacar alunos cuja frequência esteja abaixo de 75%.

### RN53 — Alerta de alto risco

O sistema deverá destacar alunos classificados pelo modelo preditivo como
"Alto Risco".

### RN54 — Múltiplos indicadores

Um mesmo aluno poderá apresentar simultaneamente diferentes alertas,
como baixo desempenho, baixa frequência e alto risco preditivo.

---

# 10. Histórico e Análises

### RN55 — Histórico acadêmico

Os registros acadêmicos devem permitir a consulta das notas, médias,
faltas, frequência e situação acadêmica do aluno.

### RN56 — Histórico de risco

As análises preditivas armazenadas devem permitir acompanhar a evolução
do score e da classificação de risco ao longo do tempo.

### RN57 — Data da análise

Toda análise preditiva armazenada deve possuir a data e hora em que foi
realizada.

---

# 11. Integridade dos Dados

### RN58 — Integridade do aluno

Todo registro acadêmico deve estar associado a um aluno existente.

### RN59 — Integridade da disciplina

Todo registro acadêmico deve estar associado a uma disciplina existente.

### RN60 — Integridade da turma

Todo registro acadêmico deve estar associado a uma turma existente.

### RN61 — Integridade da avaliação

Toda avaliação deve estar associada a uma disciplina existente.

### RN62 — Integridade da análise preditiva

Toda análise preditiva deve estar associada a um registro acadêmico
existente.

### RN63 — Exclusão de dados relacionados

A exclusão de um registro que possua informações dependentes deverá
respeitar as regras de integridade definidas no banco de dados.

---

# 12. Consistência das Informações

### RN64 — Consistência entre notas e média

A média final armazenada deve corresponder às notas e aos pesos
registrados para o aluno.

### RN65 — Consistência entre faltas e frequência

O percentual de frequência deve corresponder à quantidade de aulas e
faltas registradas.

### RN66 — Consistência da situação acadêmica

A situação acadêmica armazenada deve estar de acordo com os critérios
definidos para média e frequência.

### RN67 — Atualização dos indicadores

Alterações nos dados acadêmicos devem refletir nos indicadores e alertas
correspondentes.

---

# 13. Considerações sobre o Machine Learning

### RN68 — Modelo preditivo

O modelo de Machine Learning deverá utilizar dados acadêmicos disponíveis
para identificar padrões associados ao risco acadêmico.

### RN69 — Treinamento do modelo

O modelo deverá ser treinado utilizando dados históricos adequados para
a finalidade de classificação de risco.

### RN70 — Classificação

O resultado do modelo deverá ser convertido para o formato de score e
nível de risco utilizado pelo SmartGrade.

### RN71 — Atualização do modelo

O modelo poderá ser atualizado ou retreinado conforme novos dados
históricos estejam disponíveis.

### RN72 — Transparência da análise

Sempre que possível, o sistema deverá disponibilizar informações sobre
os principais dados considerados na análise, permitindo melhor
interpretação do resultado pelo usuário.

---

# 14. Resumo das Regras Acadêmicas

## Notas

- Escala: 0,00 a 10,00.
- P1: 30%.
- P2: 40%.
- Trabalho: 30%.
- Média mínima para aprovação direta: 7,00.

## Frequência

- Frequência mínima: 75%.
- Frequência entre 75% e 80%: situação de atenção.
- Frequência inferior a 75%: reprovação por falta.

## Situação acadêmica

| Condição | Situação |
|---|---|
| Média ≥ 7,00 e frequência ≥ 75% | Aprovado |
| Média ≥ 5,00 e < 7,00 e frequência ≥ 75% | Exame |
| Média < 5,00 e frequência ≥ 75% | Reprovado por Nota |
| Frequência < 75% | Reprovado por Falta |

## Análise preditiva

| Score | Nível |
|---|---|
| 0,00 – 0,39 | 🟢 Baixo Risco |
| 0,40 – 0,69 | 🟡 Médio Risco |
| 0,70 – 1,00 | 🔴 Alto Risco |

> A análise preditiva possui caráter de apoio à decisão e não altera
> automaticamente a situação acadêmica oficial do aluno.