package util;

import model.RegistroAcademico;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class HashUtil {

    public static String sha256(String texto) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] bytes = md.digest(texto.getBytes(StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder();
            for (byte b : bytes) {
                sb.append(String.format("%02x", b)); // cada byte vira 2 caracteres hexadecimais
            }
            return sb.toString(); // 64 caracteres
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA-256 indisponível: " + e.getMessage());
        }
    }

    private static String num(BigDecimal v) {
        return (v == null ? BigDecimal.ZERO : v).setScale(2, RoundingMode.HALF_UP).toPlainString();
    }

    // Representação padronizada do registro (sem data e usuário)
    public static String dadosCanonicos(RegistroAcademico r) {
        return "REG=" + r.getIdRegistro()
             + ";ALUNO=" + r.getIdAluno()
             + ";DISC=" + r.getIdDisciplina()
             + ";TURMA=" + r.getIdTurma()
             + ";P1=" + num(r.getNotaP1())
             + ";P2=" + num(r.getNotaP2())
             + ";TR=" + num(r.getNotaTrabalho())
             + ";MED=" + num(r.getMediaFinal())
             + ";FALTAS=" + r.getTotalFaltas()
             + ";FREQ=" + num(r.getPercentualFrequencia())
             + ";SIT=" + (r.getSituacaoAcademica() == null ? "" : r.getSituacaoAcademica());
    }
}