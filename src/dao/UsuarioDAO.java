package dao;

import model.Usuario;
import conexao.ConexaoBD;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class UsuarioDAO {

    // O construtor é chamado automaticamente sempre que o DAO é instanciado
    public UsuarioDAO() {
        criarTabelaSeNaoExistir();
    }

    private void criarTabelaSeNaoExistir() {
        String sqlCreate = "CREATE TABLE IF NOT EXISTS usuarios ("
                + "id INT AUTO_INCREMENT PRIMARY KEY, "
                + "nome VARCHAR(100) NOT NULL, "
                + "login VARCHAR(50) UNIQUE NOT NULL, "
                + "senha VARCHAR(255) NOT NULL, "
                + "perfil VARCHAR(20) NOT NULL, "
                + "id_disciplina INT DEFAULT NULL, "
                + "ativo BOOLEAN DEFAULT TRUE" 
                + ")";

        try (Connection conn = ConexaoBD.getConexao();
             Statement stmt = conn.createStatement()) {
            
            stmt.execute(sqlCreate);
            inserirUsuariosPadraoSeVazio(conn);
            
        } catch (SQLException e) {
            System.out.println("Erro ao criar tabela de usuários: " + e.getMessage());
        }
    }

    private void inserirUsuariosPadraoSeVazio(Connection conn) {
        String sqlCheck = "SELECT COUNT(*) FROM usuarios";
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sqlCheck)) {
            
            if (rs.next() && rs.getInt(1) == 0) {
                // Insere os perfis reais de toda a equipa e professores
                String sqlInsert = "INSERT INTO usuarios (nome, login, senha, perfil, id_disciplina) VALUES "
                        // Coordenadora
                        + "('Olinda Coordenadora', 'olinda', '123456', 'COORDENADOR', NULL), "
                        
                        // Professores (supondo IDs de disciplina 1, 2 e 3 no seu banco)
                        + "('Rafael Guem Murakami', 'TEC001', '123456', 'PROFESSOR', 1), " 
                        + "('Dirk de Mello', 'GTI001', '123456', 'PROFESSOR', 2), " 
                        + "('Angelo Luiz da Cruz Oliveira', 'PRG001', '123456', 'PROFESSOR', 3), " 
                        
                        // Alunos (Usando RA como login)
                        + "('Andrew Meusburger', '5118824', '123456', 'ALUNO', NULL), "
                        + "('Flavio Fernando Souza', '5730821', '123456', 'ALUNO', NULL), "
                        + "('Luiz Arthur da Silva Costa', '5830826', '123456', 'ALUNO', NULL), "
                        + "('Matheus Soares Siqueira', '4988809', '123456', 'ALUNO', NULL), "
                        + "('Pedro Henrique Lobato Diogo', '5808880', '123456', 'ALUNO', NULL)";
                        
                stmt.executeUpdate(sqlInsert);
                System.out.println("Usuários da equipe e professores inseridos com sucesso na base de dados!");
            }
        } catch (SQLException e) {
            System.out.println("Erro ao verificar/inserir usuários reais: " + e.getMessage());
        }
    }

    public Usuario autenticar(String login, String senha) {
        String sql = "SELECT * FROM usuarios WHERE login = ? AND senha = ?";
        
        try (Connection conn = ConexaoBD.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, login);
            stmt.setString(2, senha);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Usuario u = new Usuario();
                    
                    // Preenchemos TODOS os dados que o sistema precisa para a Sessão funcionar sem erros
                    u.setId(rs.getInt("id"));
                    u.setNome(rs.getString("nome"));
                    u.setPerfil(rs.getString("perfil"));
                    
                    // Puxa a disciplina da base de dados (se for aluno ou coordenador, retorna 0 ou null)
                    u.setIdDisciplina(rs.getInt("id_disciplina"));
                    
                    return u; // Retorna o utilizador completo
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro na autenticação: " + e.getMessage());
        }
        return null; // Retorna nulo se não encontrar ninguém com esse login/senha
    }
}