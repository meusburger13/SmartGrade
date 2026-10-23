package controller;

import model.AnalisePreditiva;
import model.RegistroAcademico;
import java.math.BigDecimal;
import java.sql.Timestamp;

public class PredicaoRiscoML {

    // Limiar definido no diagrama
    public static final double LIMIAR_ALTO_RISCO = 0.70;

    public AnalisePreditiva classificarRisco(RegistroAcademico registro) {
        AnalisePreditiva analise = new AnalisePreditiva();
        analise.setIdRegistro(registro.getIdRegistro());
        analise.setDataAnalise(new Timestamp(System.currentTimeMillis()));

        // Cálculo preditivo básico: combina nota baixa e baixa frequência
        double media = registro.getMediaFinal() != null ? registro.getMediaFinal().doubleValue() : 0.0;
        double frequencia = registro.getPercentualFrequencia() != null ? registro.getPercentualFrequencia().doubleValue() : 100.0;

        // Fator de risco de 0.0 a 1.0
        double riscoNota = (10.0 - media) / 10.0; // se média for 3, risco é 0.7
        double riscoFrequencia = (100.0 - frequencia) / 100.0; // se freq for 60%, risco é 0.4
        
        // Média ponderada do risco: 60% peso nota, 40% peso presença
        double scoreCalculado = (riscoNota * 0.6) + (riscoFrequencia * 0.4);
        if (scoreCalculado < 0) scoreCalculado = 0;
        if (scoreCalculado > 1) scoreCalculado = 1;

        analise.setScoreRisco(new BigDecimal(String.format(java.util.Locale.US, "%.3f", scoreCalculado)));

        if (scoreCalculado >= LIMIAR_ALTO_RISCO) {
            analise.setNivelRisco("ALTO");
        } else if (scoreCalculado >= 0.40) {
            analise.setNivelRisco("MEDIO");
        } else {
            analise.setNivelRisco("BAIXO");
        }

        return analise;
    }

    public void extrairPadroesHistoricos() {
        // Método de apoio para treinar/ajustar pesos se necessário
    }
}