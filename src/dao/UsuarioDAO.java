package dao;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import conexao.ConexaoBD;
import model.Usuario;

public class UsuarioDAO {

    public UsuarioDAO() {
        criarTabelaSeNaoExistir();
    }

    private void criarTabelaSeNaoExistir() {
        String sql = "CREATE TABLE IF NOT EXISTS usuarios ("
                + "id_usuario INT AUTO_INCREMENT PRIMARY KEY, "
                + "login VARCHAR(50) NOT NULL UNIQUE, "
                + "senha VARCHAR(100) NOT NULL, "
                + "perfil VARCHAR(20) NOT NULL"
                + ")";
        try (Connection conn = ConexaoBD.getConexao();
            PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.execute();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao verificar/criar a tabela usuarios: " + e.getMessage());
        }
    }

    
    public Usuario autenticar(String login, String senha) {
        String sql = "SELECT * FROM usuarios WHERE login = ? AND senha = ?";
        
        // Utilize a sua classe de Conexao já existente
        try (Connection conn = ConexaoBD.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, login);
            stmt.setString(2, senha);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                Usuario u = new Usuario();
                u.setPerfil(rs.getString("perfil"));
                return u; // Retorna o utilizador com o perfil preenchido
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro na autenticação: " + e.getMessage());
        }
        return null; // Retorna nulo se não encontrar ninguém
    }
}