-- ==========================================
-- 0. LIMPEZA DE DADOS EXISTENTES (Evita duplicações)
-- ==========================================
DELETE FROM registros_academicos;
DELETE FROM avaliacoes;
DELETE FROM alunos;
DELETE FROM disciplinas;
DELETE FROM turmas;

-- ==========================================
-- 1. INSERINDO AS TURMAS
-- ==========================================
INSERT INTO turmas (id_turma, codigo, periodo_semestre) VALUES
(1, 'ADS-4B-REGULAR', '2026.2'),
(2, 'ADS-DP-ESPECIAL', '2026.2');

-- ==========================================
-- 2. INSERINDO AS DISCIPLINAS
-- ==========================================
INSERT INTO disciplinas (id_disciplina, codigo, nome, carga_horaria, total_aulas) VALUES
(1, 'TEC001', 'Tecnologias Emergentes', 200, 100),
(2, 'GOV001', 'Governança em Tecnologia da Informação', 200, 100),
(3, 'PRG001', 'Técnicas de Programação', 120, 60),
(4, 'WEB001', 'Projeto Integrador: Programação WEB', 80, 40),
(5, 'SIS001', 'Sistemas Inteligentes', 80, 40);

-- ==========================================
-- 3. INSERINDO AS AVALIAÇÕES (P1, P2 e Trabalho por Disciplina)
-- ==========================================
INSERT INTO avaliacoes (id_disciplina, tipo, descricao) VALUES
(1, 'P1', 'Avaliação Regimental 1'),
(1, 'P2', 'Avaliação Regimental 2'),
(1, 'Trabalho', 'Projeto Prático da Disciplina'),
(2, 'P1', 'Avaliação Regimental 1'),
(2, 'P2', 'Avaliação Regimental 2'),
(2, 'Trabalho', 'Estudo de Caso de Governança'),
(3, 'P1', 'Avaliação Regimental 1'),
(3, 'P2', 'Avaliação Regimental 2'),
(3, 'Trabalho', 'Trabalho Prático de Programação'),
(4, 'P1', 'Avaliação Regimental 1'),
(4, 'P2', 'Avaliação Regimental 2'),
(4, 'Trabalho', 'Entrega Final do Projeto WEB'),
(5, 'P1', 'Avaliação Regimental 1'),
(5, 'P2', 'Avaliação Regimental 2'),
(5, 'Trabalho', 'Implementação de Algoritmo de IA');

-- ==========================================
-- 4. INSERINDO OS ALUNOS (A equipa)
-- ==========================================
INSERT INTO alunos (id_aluno, ra, nome, email, ativo) VALUES
(1, '5118824', 'Andrew Meusburger', 't-andrew@estudante.unisa.br', 1),
(2, '5730821', 'Flavio Fernando souza', 'ffernando1y@estudante.unisa.br', 1),
(3, '5830826', 'Luiz Arthur da Silva Costa', 'larthur1x@estudante.unisa.br', 1),
(4, '4988809', 'Matheus Soares Siqueira', 'fmatheus2y@estudante.unisa.br', 1),
(5, '5808880', 'Pedro Henrique Lobato diogo', 'rtlhpedro@estudante.unisa.br', 1);

-- ==========================================
-- 5. INSERINDO OS REGISTOS ACADÉMICOS
-- ==========================================
INSERT INTO registros_academicos (id_aluno, id_disciplina, id_turma, nota_p1, nota_p2, nota_trabalho, total_faltas, media_final, percentual_frequencia, situacao_academica) VALUES

-- 1. ANDREW MEUSBURGER (Aprovado em tudo - Turma Regular)
(1, 1, 1, 8.5, 9.0, 8.0, 2, 8.5, 98.0, 'Aprovado'),
(1, 2, 1, 7.5, 8.0, 8.5, 0, 8.0, 100.0, 'Aprovado'),
(1, 3, 1, 9.0, 9.5, 8.5, 4, 9.0, 93.3, 'Aprovado'),
(1, 4, 1, 8.0, 8.5, 9.0, 2, 8.5, 95.0, 'Aprovado'),
(1, 5, 1, 10.0, 9.0, 8.0, 0, 9.0, 100.0, 'Aprovado'),

-- 2. FLAVIO FERNANDO SOUZA (Cenários Críticos - Turma DP Especial)
(2, 1, 2, 5.0, 5.0, 5.0, 2, 5.0, 98.0, 'Exame'),
(2, 2, 2, 2.0, 3.0, 2.5, 4, 2.5, 96.0, 'Reprovado por Nota'),
(2, 3, 2, 8.0, 9.0, 8.5, 20, 8.5, 66.6, 'Reprovado por Falta'),
(2, 4, 1, 7.0, 7.5, 8.0, 2, 7.5, 95.0, 'Aprovado'),
(2, 5, 1, 8.0, 8.0, 8.0, 4, 8.0, 90.0, 'Aprovado'),

-- 3. LUIZ ARTHUR DA SILVA COSTA (Cenários Críticos - Turma DP Especial)
(3, 1, 2, 7.0, 7.0, 7.0, 30, 7.0, 70.0, 'Reprovado por Falta'),
(3, 2, 2, 4.0, 6.0, 5.0, 0, 5.0, 100.0, 'Exame'),
(3, 3, 2, 1.0, 2.0, 1.5, 5, 1.5, 91.6, 'Reprovado por Nota'),
(3, 4, 1, 9.0, 8.0, 8.5, 0, 8.5, 100.0, 'Aprovado'),
(3, 5, 1, 7.5, 8.5, 8.0, 2, 8.0, 95.0, 'Aprovado'),

-- 4. MATHEUS SOARES SIQUEIRA (Aprovado em tudo - Turma Regular)
(4, 1, 1, 7.0, 8.0, 9.0, 2, 8.0, 98.0, 'Aprovado'),
(4, 2, 1, 8.0, 7.5, 8.5, 4, 8.0, 96.0, 'Aprovado'),
(4, 3, 1, 10.0, 9.0, 8.0, 0, 9.0, 100.0, 'Aprovado'),
(4, 4, 1, 9.0, 9.5, 8.5, 2, 9.0, 95.0, 'Aprovado'),
(4, 5, 1, 8.5, 8.0, 7.5, 0, 8.0, 100.0, 'Aprovado'),

-- 5. PEDRO HENRIQUE LOBATO DIOGO (1 Exame - Turma DP Especial)
(5, 1, 2, 5.0, 6.0, 4.0, 2, 5.0, 98.0, 'Exame'),
(5, 2, 1, 9.0, 9.0, 9.0, 2, 9.0, 98.0, 'Aprovado'),
(5, 3, 1, 8.5, 7.0, 8.5, 6, 8.0, 90.0, 'Aprovado'),
(5, 4, 1, 7.5, 8.0, 8.5, 0, 8.0, 100.0, 'Aprovado'),
(5, 5, 1, 9.5, 10.0, 9.0, 2, 9.5, 95.0, 'Aprovado');