USE smartgrade_db;

-- Limpar dados anteriores
DELETE FROM analises_preditivas;
DELETE FROM registros_academicos;
DELETE FROM turma_alunos;
DELETE FROM avaliacoes;
DELETE FROM disciplinas;
DELETE FROM turmas;
DELETE FROM alunos;

-- Inserir Alunos
INSERT INTO alunos (id_aluno, ra, nome, email) VALUES
(1, '5118824', 'Andrew Meusburger', 't-andrew@estudante.unisa.br'),
(2, '5730821', 'Flavio Fernando Souza', 'ffernando1y@estudante.unisa.br'),
(3, '5830826', 'Luiz Arthur da Silva Costa', 'larthur1x@estudante.unisa.br'),
(4, '4988809', 'Matheus Soares Siqueira', 'fmatheus2y@estudante.unisa.br'),
(5, '5808880', 'Pedro Henrique Lobato Diogo', 'rtlhpedro@estudante.unisa.br');

-- Inserir Turmas
INSERT INTO turmas (id_turma, codigo, periodo_semestre) VALUES
(1, 'ADS-4B-NOTURNO', '2026.2'),
(2, 'ADS-DP-ESPECIAL', '2026.2');

-- Inserir Disciplinas do 4º Semestre
INSERT INTO disciplinas (id_disciplina, codigo, nome, carga_horaria, total_aulas) VALUES
(1, 'ADS401', 'Tecnologias Emergentes', 80, 40),
(2, 'ADS402', 'Governança em Tecnologia da Informação', 80, 40),
(3, 'ADS403', 'Técnicas de Programação', 80, 40),
(4, 'ADS404', 'Projeto Integrador: Programação Web', 40, 20),
(5, 'ADS405', 'Sistemas Inteligentes', 40, 20);

-- Matrículas nas Turmas
INSERT INTO turma_alunos (id_turma, id_aluno) VALUES
(1, 1), (1, 2), (1, 3), (1, 4), (1, 5),
(2, 5);

-- Inserir Registros Acadêmicos (Notas e Status)
INSERT INTO registros_academicos 
(id_registro, id_aluno, id_disciplina, id_turma, nota_p1, nota_p2, nota_trabalho, media_final, total_faltas, percentual_frequencia, situacao_academica) 
VALUES
(1, 1, 3, 1, 8.50, 9.00, 9.50, 8.90, 2, 95.00, 'Aprovado'),
(2, 2, 3, 1, 5.00, 5.50, 5.00, 5.20, 6, 85.00, 'Exame'),
(3, 3, 3, 1, 7.50, 8.00, 8.50, 7.90, 4, 90.00, 'Aprovado'),
(4, 4, 3, 1, 8.00, 7.00, 8.00, 7.60, 12, 70.00, 'Reprovado por Falta (DP)'),
(5, 5, 3, 2, 3.50, 3.00, 4.00, 3.40, 8, 80.00, 'Cursando DP');

-- Inserir Análises Preditivas de Risco
INSERT INTO analises_preditivas (id_registro, score_risco, nivel_risco) VALUES
(1, 0.050, 'Baixo Risco'),
(2, 0.450, 'Médio Risco'),
(3, 0.100, 'Baixo Risco'),
(4, 0.980, 'Alto Risco'),
(5, 0.850, 'Alto Risco');