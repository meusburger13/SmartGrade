package dao;

import conexao.ConexaoBD;
import model.Disciplina;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DisciplinaDAO {

    public DisciplinaDAO() {
        criarTabelaSeNaoExistir();
    }

    private void criarTabelaSeNaoExistir() {
        String sql = "CREATE TABLE IF NOT EXISTS disciplinas ("
                   + "id_disciplina INT AUTO_INCREMENT PRIMARY KEY, "
                   + "nome VARCHAR(150) NOT NULL, "
                   + "codigo VARCHAR(50) NOT NULL UNIQUE"
                   + ")";
        // Usamos Statement em vez de PreparedStatement para executar múltiplos comandos estruturais
        try (Connection conn = ConexaoBD.getConexao();
             Statement stmt = conn.createStatement()) {
            
            // 1. Cria a tabela
            stmt.execute(sql);
            
            // 2. Insere as disciplinas de teste se a tabela estiver vazia
            inserirDisciplinasPadraoSeVazio(conn);
            
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao verificar/criar a tabela disciplinas: " + e.getMessage());
        }
    }

    private void inserirDisciplinasPadraoSeVazio(Connection conn) {
        String sqlCheck = "SELECT COUNT(*) FROM disciplinas";
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sqlCheck)) {
            
            if (rs.next() && rs.getInt(1) == 0) {
               
                String sqlInsert = "INSERT INTO disciplinas (id_disciplina, nome, codigo) VALUES "
                        + "(1, 'Tecnologias Emergentes', 'TE01'), "
                        + "(2, 'Governança em TI', 'GTI01'), "
                        + "(3, 'Técnicas de Programação', 'TP01'), "
                        + "(4, 'Projeto Integrador', 'PI01'), "
                        + "(5, 'Sistemas Inteligentes', 'SI01')";
                        
                stmt.executeUpdate(sqlInsert);
                System.out.println("Disciplinas padrão inseridas com sucesso na base de dados!");
            }
        } catch (SQLException e) {
            System.out.println("Erro ao inserir disciplinas padrão: " + e.getMessage());
        }
    }

    public void cadastrar(Disciplina disc) {
        String sql = "INSERT INTO disciplinas (nome, codigo) VALUES (?, ?)";
        try (Connection conn = ConexaoBD.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, disc.getNome());
            stmt.setString(2, disc.getCodigo());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao cadastrar disciplina: " + e.getMessage());
        }
    }

    public List<Disciplina> listarTodos() {
        String sql = "SELECT * FROM disciplinas";
        List<Disciplina> lista = new ArrayList<>();
        try (Connection conn = ConexaoBD.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Disciplina d = new Disciplina();
                d.setIdDisciplina(rs.getInt("id_disciplina")); 
                d.setNome(rs.getString("nome"));
                d.setCodigo(rs.getString("codigo"));
                lista.add(d);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar disciplinas: " + e.getMessage());
        }
        return lista;
    }

    public void atualizar(Disciplina disc) {
        String sql = "UPDATE disciplinas SET nome = ?, codigo = ? WHERE id_disciplina = ?";
        try (Connection conn = ConexaoBD.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, disc.getNome());
            stmt.setString(2, disc.getCodigo());
            stmt.setInt(3, disc.getIdDisciplina());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao atualizar disciplina: " + e.getMessage());
        }
    }

    public void excluir(int idDisciplina) {
        String sql = "DELETE FROM disciplinas WHERE id_disciplina = ?";
        try (Connection conn = ConexaoBD.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idDisciplina);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao excluir disciplina: " + e.getMessage());
        }
    }
}