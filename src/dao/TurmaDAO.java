package dao;

import conexao.ConexaoBD;
import model.Turma;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TurmaDAO {

    public TurmaDAO() {
        criarTabelaSeNaoExistir();
    }

    private void criarTabelaSeNaoExistir() {
        String sqlCreate = "CREATE TABLE IF NOT EXISTS turmas ("
                   + "id_turma INT AUTO_INCREMENT PRIMARY KEY, "
                   + "codigo VARCHAR(50) NOT NULL UNIQUE, "
                   + "periodo_semestre VARCHAR(50) NOT NULL"
                   + ")";
                   
        try (Connection conn = ConexaoBD.getConexao();
             Statement stmt = conn.createStatement()) {
            
            // 1. Cria a tabela
            stmt.execute(sqlCreate);
            
            // 2. Insere as turmas padrão
            inserirTurmasPadraoSeVazio(conn);
            
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao verificar/criar a tabela turmas: " + e.getMessage());
        }
    }

    private void inserirTurmasPadraoSeVazio(Connection conn) {
        String sqlCheck = "SELECT COUNT(*) FROM turmas";
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sqlCheck)) {
            
            if (rs.next() && rs.getInt(1) == 0) {
                // Criando a turma Regular (ID 1) e a turma de DP (ID 2)
                String sqlInsert = "INSERT INTO turmas (id_turma, codigo, periodo_semestre) VALUES "
                        + "(1, 'ADS-4B-REGULAR', '4º Semestre'), "
                        + "(2, 'ADS-DP-ESPECIAL', 'Dependência')";
                        
                stmt.executeUpdate(sqlInsert);
                System.out.println("Turmas ADS-4B-REGULAR e ADS-DP-ESPECIAL criadas com sucesso!");
            }
        } catch (SQLException e) {
            System.out.println("Erro ao inserir turmas padrão: " + e.getMessage());
        }
    }

    public void cadastrar(Turma turma) {
        String sql = "INSERT INTO turmas (codigo, periodo_semestre) VALUES (?, ?)";
        try (Connection conn = ConexaoBD.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, turma.getCodigo());
            stmt.setString(2, turma.getPeriodoSemestre());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao cadastrar turma: " + e.getMessage());
        }
    }

    public List<Turma> listarTodos() {
        String sql = "SELECT * FROM turmas";
        List<Turma> lista = new ArrayList<>();
        try (Connection conn = ConexaoBD.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Turma t = new Turma();
                t.setIdTurma(rs.getInt("id_turma")); 
                t.setCodigo(rs.getString("codigo"));
                t.setPeriodoSemestre(rs.getString("periodo_semestre")); 
                lista.add(t);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar turmas: " + e.getMessage());
        }
        return lista;
    }

    public void atualizar(Turma turma) {
        String sql = "UPDATE turmas SET codigo = ?, periodo_semestre = ? WHERE id_turma = ?";
        try (Connection conn = ConexaoBD.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, turma.getCodigo());
            stmt.setString(2, turma.getPeriodoSemestre());
            stmt.setInt(3, turma.getIdTurma());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao atualizar turma: " + e.getMessage());
        }
    }

    public void excluir(int idTurma) {
        String sql = "DELETE FROM turmas WHERE id_turma = ?";
        try (Connection conn = ConexaoBD.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idTurma);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao excluir turma: " + e.getMessage());
        }
    }
}