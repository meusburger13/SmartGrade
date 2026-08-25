# Requisitos Não Funcionais — SmartGrade

## 1. Desempenho

| ID | Requisito | Prioridade |
|---|---|---|
| RNF01 | O sistema deve responder às operações comuns de cadastro, consulta e atualização em tempo adequado para utilização em ambiente acadêmico. | Alta |
| RNF02 | O sistema deve atualizar os cálculos de médias e frequência imediatamente após alterações nos dados acadêmicos. | Alta |
| RNF03 | O dashboard deve carregar os principais indicadores acadêmicos sem atrasos perceptíveis ao usuário. | Média |
| RNF04 | O módulo preditivo deve processar os dados disponíveis em tempo adequado para permitir a análise durante a utilização do sistema. | Média |

---

## 2. Segurança

| ID | Requisito | Prioridade |
|---|---|---|
| RNF05 | O sistema deve controlar o acesso às funcionalidades de acordo com o nível de acesso do usuário, caso diferentes perfis sejam adotados. | Alta |
| RNF06 | O sistema deve impedir operações inválidas ou não autorizadas sobre os registros acadêmicos. | Alta |
| RNF07 | O sistema deve proteger os dados acadêmicos armazenados no banco de dados contra alterações inconsistentes. | Alta |
| RNF08 | O sistema deve utilizar mecanismos de validação para impedir a inserção de dados inválidos. | Alta |

---

## 3. Integridade dos Dados

| ID | Requisito | Prioridade |
|---|---|---|
| RNF09 | O sistema deve garantir a integridade referencial dos dados armazenados no MySQL. | Alta |
| RNF10 | O sistema deve impedir o cadastro de registros que violem regras de unicidade, como matrículas duplicadas. | Alta |
| RNF11 | O sistema deve garantir a consistência dos dados durante operações de inclusão, alteração e exclusão. | Alta |
| RNF12 | O sistema deve utilizar transações para operações que envolvam múltiplas alterações relacionadas, quando necessário. | Média |

---

## 4. Usabilidade

| ID | Requisito | Prioridade |
|---|---|---|
| RNF13 | A interface gráfica deve ser desenvolvida de forma clara e intuitiva para facilitar a utilização pelos usuários. | Alta |
| RNF14 | O sistema deve apresentar mensagens claras para erros, confirmações e operações realizadas. | Alta |
| RNF15 | Os campos de entrada de dados devem possuir identificação clara e objetiva. | Alta |
| RNF16 | O sistema deve fornecer feedback visual após operações de cadastro, alteração ou exclusão. | Média |
| RNF17 | O sistema deve utilizar uma organização visual consistente entre suas diferentes telas. | Média |

---

## 5. Manutenibilidade

| ID | Requisito | Prioridade |
|---|---|---|
| RNF18 | O sistema deve utilizar a arquitetura Model-View-Controller (MVC). | Alta |
| RNF19 | O sistema deve utilizar o padrão Data Access Object (DAO) para separação do acesso aos dados. | Alta |
| RNF20 | O código-fonte deve ser organizado em pacotes de acordo com suas responsabilidades. | Alta |
| RNF21 | O sistema deve manter separadas as regras de negócio, a interface gráfica e o acesso ao banco de dados. | Alta |
| RNF22 | O código deve seguir padrões de nomenclatura e organização definidos pela equipe. | Média |
| RNF23 | O sistema deve possuir documentação suficiente para facilitar sua manutenção e evolução. | Média |

---

## 6. Compatibilidade

| ID | Requisito | Prioridade |
|---|---|---|
| RNF24 | O sistema deve ser desenvolvido utilizando Java JDK 21. | Alta |
| RNF25 | O sistema deve utilizar Java Swing para construção da interface gráfica. | Alta |
| RNF26 | O sistema deve utilizar MySQL 8.0 como sistema gerenciador de banco de dados. | Alta |
| RNF27 | A comunicação entre a aplicação Java e o banco de dados deve utilizar JDBC. | Alta |
| RNF28 | O sistema deve ser executável em ambientes compatíveis com o Java JDK 21. | Alta |

---

## 7. Confiabilidade

| ID | Requisito | Prioridade |
|---|---|---|
| RNF29 | O sistema deve tratar erros de comunicação com o banco de dados sem encerrar inesperadamente a aplicação. | Alta |
| RNF30 | O sistema deve informar ao usuário quando uma operação não puder ser concluída. | Alta |
| RNF31 | O sistema deve evitar a perda de dados durante operações de cadastro e atualização. | Alta |
| RNF32 | O sistema deve manter a consistência dos registros acadêmicos mesmo após ocorrência de erros durante uma operação. | Alta |

---

## 8. Escalabilidade

| ID | Requisito | Prioridade |
|---|---|---|
| RNF33 | A estrutura do sistema deve permitir a inclusão de novas funcionalidades sem exigir alterações significativas em módulos não relacionados. | Média |
| RNF34 | O banco de dados deve ser estruturado de forma a permitir o crescimento da quantidade de alunos, turmas, disciplinas e registros acadêmicos. | Média |
| RNF35 | O módulo preditivo deve permitir a atualização ou substituição do modelo de Machine Learning sem necessidade de reestruturar toda a aplicação. | Média |

---

## 9. Versionamento e Desenvolvimento

| ID | Requisito | Prioridade |
|---|---|---|
| RNF36 | O código-fonte do projeto deve ser versionado utilizando Git. | Alta |
| RNF37 | O projeto deve ser hospedado em um repositório GitHub. | Alta |
| RNF38 | As alterações relevantes devem ser realizadas por meio de branches e Pull Requests. | Média |
| RNF39 | O projeto deve manter documentação das principais decisões e funcionalidades implementadas. | Média |

---

## 10. Machine Learning

| ID | Requisito | Prioridade |
|---|---|---|
| RNF40 | O modelo preditivo deve apresentar resultados de forma compreensível para o usuário do sistema. | Alta |
| RNF41 | O módulo preditivo deve permitir a atualização do modelo conforme novos dados de treinamento estejam disponíveis. | Média |
| RNF42 | O processamento preditivo não deve comprometer significativamente a utilização das demais funcionalidades do sistema. | Média |
| RNF43 | O sistema deve permitir identificar quais dados foram utilizados para gerar uma classificação de risco, quando tecnicamente possível. | Média |

---

## 11. Documentação

| ID | Requisito | Prioridade |
|---|---|---|
| RNF44 | O projeto deve possuir documentação das funcionalidades e requisitos do sistema. | Alta |
| RNF45 | O projeto deve possuir documentação das instruções necessárias para instalação e execução da aplicação. | Alta |
| RNF46 | O projeto deve possuir documentação da estrutura do banco de dados. | Média |
| RNF47 | O projeto deve possuir documentação das principais tecnologias utilizadas. | Média |