package dao;

import conexao.ConexaoBD;
import model.RegistroAcademico;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AcademicoDAO {

    public AcademicoDAO() {
        criarTabelaSeNaoExistir();
    }

    private void criarTabelaSeNaoExistir() {
        String sql = "CREATE TABLE IF NOT EXISTS registros_academicos ("
                   + "id_registro INT AUTO_INCREMENT PRIMARY KEY, "
                   + "id_aluno INT NOT NULL, "
                   + "id_disciplina INT NOT NULL, "
                   + "id_turma INT NOT NULL, "
                   + "nota_p1 DECIMAL(5,2), "
                   + "nota_p2 DECIMAL(5,2), "
                   + "nota_trabalho DECIMAL(5,2), "
                   + "media_final DECIMAL(5,2), "
                   + "total_faltas INT, "
                   + "percentual_frequencia DECIMAL(5,2), "
                   + "situacao_academica VARCHAR(50)"
                   + ")";
        try (Connection conn = ConexaoBD.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.execute();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao verificar/criar a tabela registros_academicos: " + e.getMessage());
        }
    }

    public boolean salvarRegistro(RegistroAcademico registro) {
        // 1. TRAVÃO DE SEGURANÇA RIGOROSO: Nenhuma nota pode ser menor que 0 ou maior que 10!
        if ((registro.getNotaP1() != null && (registro.getNotaP1().doubleValue() < 0.0 || registro.getNotaP1().doubleValue() > 10.0)) ||
            (registro.getNotaP2() != null && (registro.getNotaP2().doubleValue() < 0.0 || registro.getNotaP2().doubleValue() > 10.0)) ||
            (registro.getNotaTrabalho() != null && (registro.getNotaTrabalho().doubleValue() < 0.0 || registro.getNotaTrabalho().doubleValue() > 10.0))) {
            System.err.println("BLOQUEIO DAO: Tentativa de salvar nota inválida (< 0 ou > 10).");
            return false;
        }

        // 2. REGRA DE TURMA ESTRITA: Cursando/Aprovado/Exame = Turma 1 | Reprovado = Turma 2
        if (registro.getSituacaoAcademica() != null && 
            registro.getSituacaoAcademica().toUpperCase().contains("REPROVADO")) {
            registro.setIdTurma(2);
        } else {
            registro.setIdTurma(1);
        }

        // 3. SEGURANÇA ANTI-CRUZAMENTO: Verifica por ALUNO e DISCIPLINA (ignora IDs de cache velhos)
        String sqlCheck = "SELECT id_registro FROM registros_academicos WHERE id_aluno = ? AND id_disciplina = ?";
        try (Connection conn = ConexaoBD.getConexao();
             PreparedStatement stmtCheck = conn.prepareStatement(sqlCheck)) {
            stmtCheck.setInt(1, registro.getIdAluno());
            stmtCheck.setInt(2, registro.getIdDisciplina());
            
            try (ResultSet rs = stmtCheck.executeQuery()) {
                if (rs.next()) {
                    // Já existe para esta matéria: atualiza estritamente esta linha
                    int idExistente = rs.getInt("id_registro");
                    String sqlUpdate = "UPDATE registros_academicos SET nota_p1 = ?, nota_p2 = ?, nota_trabalho = ?, "
                                     + "media_final = ?, total_faltas = ?, percentual_frequencia = ?, "
                                     + "situacao_academica = ?, id_turma = ? WHERE id_registro = ?";
                    try (PreparedStatement stmtUpdate = conn.prepareStatement(sqlUpdate)) {
                        stmtUpdate.setBigDecimal(1, registro.getNotaP1());
                        stmtUpdate.setBigDecimal(2, registro.getNotaP2());
                        stmtUpdate.setBigDecimal(3, registro.getNotaTrabalho());
                        stmtUpdate.setBigDecimal(4, registro.getMediaFinal());
                        stmtUpdate.setInt(5, registro.getTotalFaltas());
                        stmtUpdate.setBigDecimal(6, registro.getPercentualFrequencia());
                        stmtUpdate.setString(7, registro.getSituacaoAcademica());
                        stmtUpdate.setInt(8, registro.getIdTurma());
                        stmtUpdate.setInt(9, idExistente);
                        return stmtUpdate.executeUpdate() > 0;
                    }
                } else {
                    // Não existe: insere isolado para esta matéria
                    String sqlInsert = "INSERT INTO registros_academicos (id_aluno, id_disciplina, id_turma, "
                                     + "nota_p1, nota_p2, nota_trabalho, media_final, total_faltas, "
                                     + "percentual_frequencia, situacao_academica) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
                    try (PreparedStatement stmtInsert = conn.prepareStatement(sqlInsert)) {
                        stmtInsert.setInt(1, registro.getIdAluno());
                        stmtInsert.setInt(2, registro.getIdDisciplina());
                        stmtInsert.setInt(3, registro.getIdTurma());
                        stmtInsert.setBigDecimal(4, registro.getNotaP1());
                        stmtInsert.setBigDecimal(5, registro.getNotaP2());
                        stmtInsert.setBigDecimal(6, registro.getNotaTrabalho());
                        stmtInsert.setBigDecimal(7, registro.getMediaFinal());
                        stmtInsert.setInt(8, registro.getTotalFaltas());
                        stmtInsert.setBigDecimal(9, registro.getPercentualFrequencia());
                        stmtInsert.setString(10, registro.getSituacaoAcademica());
                        return stmtInsert.executeUpdate() > 0;
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Erro ao salvar registo académico: " + e.getMessage());
            return false;
        }
    }

    public boolean atualizarNotasFaltas(RegistroAcademico registro) {
        return salvarRegistro(registro);
    }

    public List<RegistroAcademico> buscarPorTurmaDisciplina(int idTurma, int idDisc) {
        try (Connection conn = ConexaoBD.getConexao()) {
            
            // Auto-recuperação: se não houver registos para esta turma/disciplina, cria os padrões (Cursando / Turma 1)
            String sqlCheckReg = "SELECT COUNT(*) FROM registros_academicos WHERE id_turma = ? AND id_disciplina = ?";
            try (PreparedStatement stmtReg = conn.prepareStatement(sqlCheckReg)) {
                stmtReg.setInt(1, idTurma);
                stmtReg.setInt(2, idDisc);
                try (ResultSet rsReg = stmtReg.executeQuery()) {
                    if (rsReg.next() && rsReg.getInt(1) == 0 && idTurma == 1) {
                        String sqlAlunos = "SELECT id_aluno FROM alunos";
                        try (PreparedStatement stmtAlunos = conn.prepareStatement(sqlAlunos);
                             ResultSet rsAlunos = stmtAlunos.executeQuery()) {
                            
                            String sqlInsert = "INSERT INTO registros_academicos (id_aluno, id_disciplina, id_turma, nota_p1, nota_p2, nota_trabalho, media_final, total_faltas, percentual_frequencia, situacao_academica) VALUES (?, ?, 1, 0.0, 0.0, 0.0, 0.0, 0, 100.0, 'Cursando')";
                            try (PreparedStatement stmtInsert = conn.prepareStatement(sqlInsert)) {
                                while (rsAlunos.next()) {
                                    stmtInsert.setInt(1, rsAlunos.getInt("id_aluno"));
                                    stmtInsert.setInt(2, idDisc);
                                    stmtInsert.executeUpdate();
                                }
                            }
                        }
                    }
                }
            }

        } catch (SQLException e) {
            System.err.println("Erro na auto-recuperação do DAO: " + e.getMessage());
        }

        // Busca filtrando rigorosamente por turma e disciplina, ordenado alfabeticamente
        String sql = "SELECT r.*, a.nome AS nome_aluno FROM registros_academicos r "
                   + "JOIN alunos a ON r.id_aluno = a.id_aluno "
                   + "WHERE r.id_turma = ? AND r.id_disciplina = ? "
                   + "ORDER BY a.nome ASC";
                   
        List<RegistroAcademico> lista = new ArrayList<>();

        try (Connection conn = ConexaoBD.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idTurma);
            stmt.setInt(2, idDisc);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    RegistroAcademico reg = new RegistroAcademico();
                    reg.setIdRegistro(rs.getInt("id_registro"));
                    reg.setIdAluno(rs.getInt("id_aluno"));
                    reg.setIdDisciplina(rs.getInt("id_disciplina"));
                    reg.setIdTurma(rs.getInt("id_turma"));
                    reg.setNotaP1(rs.getBigDecimal("nota_p1"));
                    reg.setNotaP2(rs.getBigDecimal("nota_p2"));
                    reg.setNotaTrabalho(rs.getBigDecimal("nota_trabalho"));
                    reg.setMediaFinal(rs.getBigDecimal("media_final"));
                    reg.setTotalFaltas(rs.getInt("total_faltas"));
                    reg.setPercentualFrequencia(rs.getBigDecimal("percentual_frequencia"));
                    reg.setSituacaoAcademica(rs.getString("situacao_academica"));
                    reg.setNomeAluno(rs.getString("nome_aluno"));

                    lista.add(reg);
                }
            }
        } catch (SQLException e) {
            System.err.println("Erro SQL ao buscar registos: " + e.getMessage());
        }
        return lista;
    }
}