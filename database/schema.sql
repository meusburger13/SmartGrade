-- Criação do Banco de Dados
CREATE DATABASE IF NOT EXISTS smartgrade_db
CHARACTER SET utf8mb4
COLLATE utf8mb4_unicode_ci;

USE smartgrade_db;

-- 1. Tabela: Alunos
CREATE TABLE IF NOT EXISTS alunos (
    id_aluno INT AUTO_INCREMENT PRIMARY KEY,
    ra VARCHAR(20) NOT NULL UNIQUE,
    nome VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    data_cadastro TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 2. Tabela: Turmas
CREATE TABLE IF NOT EXISTS turmas (
    id_turma INT AUTO_INCREMENT PRIMARY KEY,
    codigo VARCHAR(20) NOT NULL UNIQUE,
    periodo_semestre VARCHAR(10) NOT NULL
);

-- 3. Tabela: Disciplinas
CREATE TABLE IF NOT EXISTS disciplinas (
    id_disciplina INT AUTO_INCREMENT PRIMARY KEY,
    codigo VARCHAR(20) NOT NULL UNIQUE,
    nome VARCHAR(100) NOT NULL,
    carga_horaria INT NOT NULL,
    total_aulas INT NOT NULL
);

-- 4. Tabela Associativa: Turma_Alunos (Relacionamento N:N)
CREATE TABLE IF NOT EXISTS turma_alunos (
    id_turma INT NOT NULL,
    id_aluno INT NOT NULL,
    PRIMARY KEY (id_turma, id_aluno),
    FOREIGN KEY (id_turma) REFERENCES turmas(id_turma) ON DELETE CASCADE,
    FOREIGN KEY (id_aluno) REFERENCES alunos(id_aluno) ON DELETE CASCADE
);

-- 5. Tabela: Avaliações (Composição com Disciplina)
CREATE TABLE IF NOT EXISTS avaliacoes (
    id_avaliacao INT AUTO_INCREMENT PRIMARY KEY,
    id_disciplina INT NOT NULL,
    tipo VARCHAR(50) NOT NULL,
    peso DECIMAL(3,2) NOT NULL,
    descricao VARCHAR(255),
    FOREIGN KEY (id_disciplina) REFERENCES disciplinas(id_disciplina) ON DELETE CASCADE
);

-- 6. Tabela: Registros Acadêmicos (Notas, Faltas e Médias)
CREATE TABLE IF NOT EXISTS registros_academicos (
    id_registro INT AUTO_INCREMENT PRIMARY KEY,
    id_aluno INT NOT NULL,
    id_disciplina INT NOT NULL,
    id_turma INT NOT NULL,
    nota_p1 DECIMAL(4,2) DEFAULT 0.00,
    nota_p2 DECIMAL(4,2) DEFAULT 0.00,
    nota_trabalho DECIMAL(4,2) DEFAULT 0.00,
    media_final DECIMAL(4,2) DEFAULT 0.00,
    total_faltas INT DEFAULT 0,
    percentual_frequencia DECIMAL(5,2) DEFAULT 100.00,
    situacao_academica VARCHAR(30) DEFAULT 'Cursando',
    FOREIGN KEY (id_aluno) REFERENCES alunos(id_aluno) ON DELETE CASCADE,
    FOREIGN KEY (id_disciplina) REFERENCES disciplinas(id_disciplina) ON DELETE CASCADE,
    FOREIGN KEY (id_turma) REFERENCES turmas(id_turma) ON DELETE CASCADE,
    CONSTRAINT uk_aluno_disc_turma UNIQUE (id_aluno, id_disciplina, id_turma)
);

-- 7. Tabela: Análises Preditivas (IA / Risco de Evasão)
CREATE TABLE IF NOT EXISTS analises_preditivas (
    id_analise INT AUTO_INCREMENT PRIMARY KEY,
    id_registro INT NOT NULL,
    score_risco DECIMAL(4,3) NOT NULL,
    nivel_risco VARCHAR(20) NOT NULL,
    data_analise TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (id_registro) REFERENCES registros_academicos(id_registro) ON DELETE CASCADE
);