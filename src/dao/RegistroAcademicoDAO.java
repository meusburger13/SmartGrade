package dao;

import conexao.ConexaoBD;
import model.RegistroAcademico;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RegistroAcademicoDAO {

    public RegistroAcademicoDAO() {
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

    public void cadastrar(RegistroAcademico reg) {
        String sql = "INSERT INTO registros_academicos (id_aluno, id_disciplina, id_turma, "
                   + "nota_p1, nota_p2, nota_trabalho, media_final, total_faltas, "
                   + "percentual_frequencia, situacao_academica) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = ConexaoBD.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, reg.getIdAluno());
            stmt.setInt(2, reg.getIdDisciplina());
            stmt.setInt(3, reg.getIdTurma());
            stmt.setBigDecimal(4, reg.getNotaP1());
            stmt.setBigDecimal(5, reg.getNotaP2());
            stmt.setBigDecimal(6, reg.getNotaTrabalho());
            stmt.setBigDecimal(7, reg.getMediaFinal());
            stmt.setInt(8, reg.getTotalFaltas());
            stmt.setBigDecimal(9, reg.getPercentualFrequencia());
            stmt.setString(10, reg.getSituacaoAcademica());

            stmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao cadastrar registro acadêmico: " + e.getMessage());
        }
    }

    // Método principal chamado pelo PainelLancamento para buscar por Turma e Disciplina
    public List<RegistroAcademico> listarPorTurmaEDisciplina(int idTurma, int idDisciplina) {
        // 1. Verifica se já existem registros para esta turma e disciplina. Se não existirem, cria automaticamente para os alunos matriculados.
        String sqlCheck = "SELECT COUNT(*) FROM registros_academicos WHERE id_turma = ? AND id_disciplina = ?";
        try (Connection conn = ConexaoBD.getConexao();
             PreparedStatement stmtCheck = conn.prepareStatement(sqlCheck)) {
            stmtCheck.setInt(1, idTurma);
            stmtCheck.setInt(2, idDisciplina);
            try (ResultSet rs = stmtCheck.executeQuery()) {
                if (rs.next() && rs.getInt(1) == 0) {
                    String sqlAlunos = "SELECT id_aluno FROM turma_alunos WHERE id_turma = ?";
                    try (PreparedStatement stmtAlunos = conn.prepareStatement(sqlAlunos)) {
                        stmtAlunos.setInt(1, idTurma);
                        try (ResultSet rsAlunos = stmtAlunos.executeQuery()) {
                            String sqlInsert = "INSERT INTO registros_academicos (id_aluno, id_disciplina, id_turma, nota_p1, nota_p2, nota_trabalho, media_final, total_faltas, percentual_frequencia, situacao_academica) VALUES (?, ?, ?, 0.0, 0.0, 0.0, 0.0, 0, 100.0, 'Cursando')";
                            try (PreparedStatement stmtInsert = conn.prepareStatement(sqlInsert)) {
                                while (rsAlunos.next()) {
                                    stmtInsert.setInt(1, rsAlunos.getInt("id_aluno"));
                                    stmtInsert.setInt(2, idDisciplina);
                                    stmtInsert.setInt(3, idTurma);
                                    stmtInsert.executeUpdate();
                                }
                            }
                        }
                    }
                }
            }
        } catch (SQLException e) {
            System.out.println("Erro ao inicializar registros padrão: " + e.getMessage());
        }

        // 2. Retorna a lista fazendo JOIN com a tabela alunos para trazer o nome completo
        String sql = "SELECT r.*, a.nome AS nome_aluno FROM registros_academicos r "
                   + "JOIN alunos a ON r.id_aluno = a.id_aluno "
                   + "WHERE r.id_turma = ? AND r.id_disciplina = ?";
        List<RegistroAcademico> lista = new ArrayList<>();
        
        try (Connection conn = ConexaoBD.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, idTurma);
            stmt.setInt(2, idDisciplina);
            
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
                    reg.setNomeAluno(rs.getString("nome_aluno")); // Nome recuperado via JOIN
                    
                    lista.add(reg);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar registros por turma e disciplina: " + e.getMessage());
        }
        return lista;
    }

    public List<RegistroAcademico> listarPorAluno(int idAluno) {
        String sql = "SELECT r.*, a.nome AS nome_aluno FROM registros_academicos r "
                   + "JOIN alunos a ON r.id_aluno = a.id_aluno WHERE r.id_aluno = ?";
        List<RegistroAcademico> lista = new ArrayList<>();

        try (Connection conn = ConexaoBD.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idAluno);

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
            throw new RuntimeException("Erro ao listar registros acadêmicos: " + e.getMessage());
        }
        return lista;
    }
    
    public List<RegistroAcademico> listarPorDisciplina(int idDisciplina) {
        String sql = "SELECT r.*, a.nome AS nome_aluno FROM registros_academicos r "
                   + "JOIN alunos a ON r.id_aluno = a.id_aluno WHERE r.id_disciplina = ?";
        List<RegistroAcademico> lista = new ArrayList<>();
        
        try (Connection conn = ConexaoBD.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, idDisciplina);
            
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
            throw new RuntimeException("Erro ao listar registros por disciplina: " + e.getMessage());
        }
        return lista;
    }
     
    public List<RegistroAcademico> listarTodos() {
        String sql = "SELECT r.*, a.nome AS nome_aluno FROM registros_academicos r "
                   + "JOIN alunos a ON r.id_aluno = a.id_aluno";
        List<RegistroAcademico> lista = new ArrayList<>();
        
        try (Connection conn = ConexaoBD.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
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
            throw new RuntimeException("Erro ao listar todos os registros: " + e.getMessage());
        }
        return lista;
    }

    public void atualizar(RegistroAcademico reg) {
        String sql = "UPDATE registros_academicos SET id_aluno = ?, id_disciplina = ?, id_turma = ?, "
                   + "nota_p1 = ?, nota_p2 = ?, nota_trabalho = ?, media_final = ?, total_faltas = ?, "
                   + "percentual_frequencia = ?, situacao_academica = ? WHERE id_registro = ?";

        try (Connection conn = ConexaoBD.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, reg.getIdAluno());
            stmt.setInt(2, reg.getIdDisciplina());
            stmt.setInt(3, reg.getIdTurma());
            stmt.setBigDecimal(4, reg.getNotaP1());
            stmt.setBigDecimal(5, reg.getNotaP2());
            stmt.setBigDecimal(6, reg.getNotaTrabalho());
            stmt.setBigDecimal(7, reg.getMediaFinal());
            stmt.setInt(8, reg.getTotalFaltas());
            stmt.setBigDecimal(9, reg.getPercentualFrequencia());
            stmt.setString(10, reg.getSituacaoAcademica());
            stmt.setInt(11, reg.getIdRegistro());

            stmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao atualizar registro acadêmico: " + e.getMessage());
        }
    }

    public void excluir(int idRegistro) {
        String sql = "DELETE FROM registros_academicos WHERE id_registro = ?";

        try (Connection conn = ConexaoBD.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idRegistro);
            stmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao excluir registro acadêmico: " + e.getMessage());
        }
    }
    
    public void atualizarNotaFalta(int idAluno, double notaP1, double notaP2, double notaTrabalho, int novasFaltas) {
        double novaMedia = (notaP1 + notaP2 + notaTrabalho) / 3.0;
        double novaFrequencia = 100.0 - (novasFaltas * 2.5);
        if (novaFrequencia < 0) novaFrequencia = 0.0;
        
        String sql = "UPDATE registros_academicos SET nota_p1 = ?, nota_p2 = ?, nota_trabalho = ?, total_faltas = ?, media_final = ?, percentual_frequencia = ? WHERE id_aluno = ?";
        
        try (Connection conn = ConexaoBD.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
             
            stmt.setDouble(1, notaP1);
            stmt.setDouble(2, notaP2);
            stmt.setDouble(3, notaTrabalho);
            stmt.setInt(4, novasFaltas);
            stmt.setDouble(5, novaMedia);       
            stmt.setDouble(6, novaFrequencia);  
            stmt.setInt(7, idAluno);
            
            stmt.executeUpdate();
            
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao atualizar notas: " + e.getMessage());
        }
    }
}