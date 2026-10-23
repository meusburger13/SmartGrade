package dao;

import conexao.ConexaoBD;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TurmaAlunoDAO {

    public TurmaAlunoDAO() {
        criarTabelaSeNaoExistir();
    }

    private void criarTabelaSeNaoExistir() {
        // Tabela associativa com chave primária composta para evitar alunos duplicados na mesma turma
        String sqlCreate = "CREATE TABLE IF NOT EXISTS turma_alunos ("
                   + "id_turma INT NOT NULL, "
                   + "id_aluno INT NOT NULL, "
                   + "PRIMARY KEY (id_turma, id_aluno)"
                   + ")";
                   
        try (Connection conn = ConexaoBD.getConexao();
             Statement stmt = conn.createStatement()) {
            
            // 1. Cria a tabela
            stmt.execute(sqlCreate);
            
            // 2. Vincula à turma padrão
            inserirMatriculasPadraoSeVazio(conn);
            
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao verificar/criar a tabela turma_alunos: " + e.getMessage());
        }
    }

    private void inserirMatriculasPadraoSeVazio(Connection conn) {
        String sqlCheck = "SELECT COUNT(*) FROM turma_alunos";
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sqlCheck)) {
            
            if (rs.next() && rs.getInt(1) == 0) {
                // Matricula os alunos 1, 2, 3, 4 e 5  na turma 1
                String sqlInsert = "INSERT INTO turma_alunos (id_turma, id_aluno) VALUES "
                        + "(1, 1), " // Andrew
                        + "(1, 2), " // Matheus
                        + "(1, 3), " // Luiz Arthur
                        + "(1, 4), " // Flávio Fernando
                        + "(1, 5)";  // Pedro
                        
                stmt.executeUpdate(sqlInsert);
                System.out.println("Alunos matriculados na turma padrão com sucesso!");
            }
        } catch (SQLException e) {
            System.out.println("Erro ao inserir matrículas padrão: " + e.getMessage());
        }
    }

    public void matricularAluno(int idTurma, int idAluno) {
        String sql = "INSERT INTO turma_alunos (id_turma, id_aluno) VALUES (?, ?)";
        try (Connection conn = ConexaoBD.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idTurma);
            stmt.setInt(2, idAluno);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao vincular aluno na turma: " + e.getMessage());
        }
    }

    public void desmatricularAluno(int idTurma, int idAluno) {
        String sql = "DELETE FROM turma_alunos WHERE id_turma = ? AND id_aluno = ?";
        try (Connection conn = ConexaoBD.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idTurma);
            stmt.setInt(2, idAluno);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao desvincular aluno da turma: " + e.getMessage());
        }
    }

    // Retorna os IDs dos alunos matriculados em uma determinada turma
    public List<Integer> listarAlunosPorTurma(int idTurma) {
        String sql = "SELECT id_aluno FROM turma_alunos WHERE id_turma = ?";
        List<Integer> alunosIds = new ArrayList<>();
        try (Connection conn = ConexaoBD.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idTurma);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    alunosIds.add(rs.getInt("id_aluno"));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar alunos da turma: " + e.getMessage());
        }
        return alunosIds;
    }
}