-- Criação do banco de dados 
CREATE DATABASE smartgrade_db;
USE smartgrade_db;

-- 1. Tabela Alunos 
CREATE TABLE alunos (
    id_aluno INT AUTO_INCREMENT PRIMARY KEY,
    ra VARCHAR(20) UNIQUE NOT NULL,
    nome VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL,
    ativo BOOLEAN NOT NULL DEFAULT TRUE
);

-- 2. Tabela Disciplinas
CREATE TABLE disciplinas (
    id_disciplina INT AUTO_INCREMENT PRIMARY KEY,
    codigo VARCHAR(20) UNIQUE NOT NULL,
    nome VARCHAR(100) NOT NULL,
    carga_horaria INT NOT NULL,
    total_aulas INT NOT NULL
);

-- 3. Tabela Turmas
CREATE TABLE turmas (
    id_turma INT AUTO_INCREMENT PRIMARY KEY,
    codigo VARCHAR(20) UNIQUE NOT NULL,
    periodo_semestre VARCHAR(20) NOT NULL
);

-- 4. Tabela Avaliações (Alinhada com id_disciplina, tipo e descricao)
CREATE TABLE avaliacoes (
    id_avaliacao INT AUTO_INCREMENT PRIMARY KEY,
    id_disciplina INT NOT NULL,
    tipo VARCHAR(50) NOT NULL,
    descricao VARCHAR(255),
    FOREIGN KEY (id_disciplina) REFERENCES disciplinas(id_disciplina) ON DELETE RESTRICT
);

-- 5. Tabela Registros Acadêmicos (FKs com RESTRICT atendendo ao RNF08)
CREATE TABLE registros_academicos (
    id_registro INT AUTO_INCREMENT PRIMARY KEY,
    id_aluno INT NOT NULL,
    id_disciplina INT NOT NULL,
    id_turma INT NOT NULL,
    nota_p1 DECIMAL(5,2),
    nota_p2 DECIMAL(5,2),
    nota_trabalho DECIMAL(5,2),
    total_faltas INT DEFAULT 0,
    media_final DECIMAL(5,2),
    percentual_frequencia DECIMAL(5,2),
    situacao_academica VARCHAR(30),
    FOREIGN KEY (id_aluno) REFERENCES alunos(id_aluno) ON DELETE RESTRICT,
    FOREIGN KEY (id_disciplina) REFERENCES disciplinas(id_disciplina) ON DELETE RESTRICT,
    FOREIGN KEY (id_turma) REFERENCES turmas(id_turma) ON DELETE RESTRICT
);

-- 6. Tabela Usuários (Sistema de Autenticação)
CREATE TABLE usuarios (
    id_usuario INT AUTO_INCREMENT PRIMARY KEY,
    login VARCHAR(50) UNIQUE NOT NULL,
    senha VARCHAR(100) NOT NULL,
    perfil VARCHAR(20) NOT NULL
);