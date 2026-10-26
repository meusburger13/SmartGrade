package dao;

import conexao.ConexaoBD;
import util.HashUtil;
import java.sql.*;
import java.time.LocalDateTime;

public class AuditoriaDAO {

    public static final String GENESIS = "0".repeat(64);

    public AuditoriaDAO() {
        criarTabelaSeNaoExistir();
    }

    private void criarTabelaSeNaoExistir() {
        String sql = "CREATE TABLE IF NOT EXISTS auditoria_blocos ("
                   + "id_bloco INT AUTO_INCREMENT PRIMARY KEY, "
                   + "id_registro INT NOT NULL, "
                   + "dados VARCHAR(500) NOT NULL, "
                   + "hash_anterior CHAR(64) NOT NULL, "
                   + "hash_atual CHAR(64) NOT NULL, "
                   + "id_usuario INT, "
                   + "data_hora TIMESTAMP DEFAULT CURRENT_TIMESTAMP"
                   + ")";
        try (Connection conn = ConexaoBD.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.execute();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao criar a tabela auditoria_blocos: " + e.getMessage());
        }
    }

    // Usa a MESMA conexão (e transação) de quem gravou a nota
    public void registrarBloco(Connection conn, int idRegistro, String dadosBase, Integer idUsuario) throws SQLException {
        String hashAnterior = GENESIS;
        String sqlUltimo = "SELECT hash_atual FROM auditoria_blocos ORDER BY id_bloco DESC LIMIT 1 FOR UPDATE";
        try (PreparedStatement st = conn.prepareStatement(sqlUltimo);
             ResultSet rs = st.executeQuery()) {
            if (rs.next()) hashAnterior = rs.getString(1);
        }

        int usr = (idUsuario == null) ? 0 : idUsuario;
        String dados = dadosBase + ";TS=" + LocalDateTime.now().withNano(0) + ";USR=" + usr;
        String hashAtual = HashUtil.sha256(hashAnterior + "|" + dados);

        String sqlInsert = "INSERT INTO auditoria_blocos (id_registro, dados, hash_anterior, hash_atual, id_usuario) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement st = conn.prepareStatement(sqlInsert)) {
            st.setInt(1, idRegistro);
            st.setString(2, dados);
            st.setString(3, hashAnterior);
            st.setString(4, hashAtual);
            st.setInt(5, usr);
            st.executeUpdate();
        }
    }
}