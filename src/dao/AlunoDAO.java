package dao;

import conexao.ConexaoBD;
import model.Aluno;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AlunoDAO {

    public AlunoDAO() {
        criarTabelaSeNaoExistir();
    }

    private void criarTabelaSeNaoExistir() {
        String sqlCreate = "CREATE TABLE IF NOT EXISTS alunos ("
                   + "id_aluno INT AUTO_INCREMENT PRIMARY KEY, "
                   + "nome VARCHAR(150) NOT NULL, "
                   + "ra VARCHAR(50) NOT NULL UNIQUE, "
                   + "email VARCHAR(150) NOT NULL, "
                   + "ativo BOOLEAN DEFAULT TRUE" 
                   + ")";
                   
        try (Connection conn = ConexaoBD.getConexao();
             Statement stmt = conn.createStatement()) {
            
            // 1. Cria a tabela com a estrutura correta
            stmt.execute(sqlCreate);
            
            
            inserirAlunosPadraoSeVazio(conn);
            
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao verificar/criar a tabela alunos: " + e.getMessage());
        }
    }

    private void inserirAlunosPadraoSeVazio(Connection conn) {
        String sqlCheck = "SELECT COUNT(*) FROM alunos";
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sqlCheck)) {
            
            if (rs.next() && rs.getInt(1) == 0) {
                String sqlInsert = "INSERT INTO alunos (nome, ra, email, ativo) VALUES "
                        + "('Andrew Meusburger', '5118824', 't-andrew@estudante.unisa.br', true), "
                        + "('Matheus Soares Siqueira', '4988809', 'fmatheus2y@estudante.unisa.br', true), "
                        + "('Luiz Arthur da Silva Costa', '5830826', 'larthur1x@estudante.unisa.br', true), "
                        + "('Flavio Fernando Souza', '5730821', 'ffernando1y@estudante.unisa.br', true), "
                        + "('Pedro Henrique Lobato Diogo', '5808880', 'rtlhpedro@estudante.unisa.br', true)";
                        
                stmt.executeUpdate(sqlInsert);
                System.out.println("Alunos reais da equipe inseridos com sucesso na base de dados!");
            }
        } catch (SQLException e) {
            System.out.println("Erro ao inserir alunos padrão: " + e.getMessage());
        }
    }

    public void cadastrar(Aluno aluno) {
        String sql = "INSERT INTO alunos (ra, nome, email, ativo) VALUES (?, ?, ?, true) " +
                "ON DUPLICATE KEY UPDATE nome = VALUES(nome), email = VALUES(email), ativo = true";
        try (Connection conn = ConexaoBD.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, aluno.getRa());
            stmt.setString(2, aluno.getNome());
            stmt.setString(3, aluno.getEmail());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao cadastrar aluno: " + e.getMessage());
        }
    }

    public List<Aluno> listarTodos() {
        String sql = "SELECT * FROM alunos WHERE ativo = true";
        List<Aluno> lista = new ArrayList<>();
        try (Connection conn = ConexaoBD.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Aluno a = new Aluno();
                a.setIdAluno(rs.getInt("id_aluno")); 
                a.setNome(rs.getString("nome"));
                a.setRa(rs.getString("ra"));
                a.setEmail(rs.getString("email"));
                lista.add(a);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar alunos: " + e.getMessage());
        }
        return lista;
    }

    public void atualizar(Aluno aluno) {
        String sql = "UPDATE alunos SET nome = ?, ra = ?, email = ? WHERE id_aluno = ?";
        try (Connection conn = ConexaoBD.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, aluno.getNome());
            stmt.setString(2, aluno.getRa());
            stmt.setString(3, aluno.getEmail());
            stmt.setInt(4, aluno.getIdAluno());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao atualizar aluno: " + e.getMessage());
        }
    }

    public void excluir(int idAluno) {
        String sql = "UPDATE alunos SET ativo = false WHERE id_aluno = ?";
        try (Connection conn = ConexaoBD.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idAluno);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao inativar aluno: " + e.getMessage());
        }
     }
}