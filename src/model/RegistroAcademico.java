package model;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class RegistroAcademico {
    private int idRegistro;
    private int idAluno;
    private int idDisciplina;
    private int idTurma;
    private BigDecimal notaP1;
    private BigDecimal notaP2;
    private BigDecimal notaTrabalho;
    private BigDecimal mediaFinal;
    private int totalFaltas;
    private BigDecimal percentualFrequencia;
    private String situacaoAcademica;

    public RegistroAcademico() {}

    public RegistroAcademico(int idRegistro, int idAluno, int idDisciplina, int idTurma,
                             BigDecimal notaP1, BigDecimal notaP2, BigDecimal notaTrabalho,
                             BigDecimal mediaFinal, int totalFaltas, BigDecimal percentualFrequencia,
                             String situacaoAcademica) {
        this.idRegistro = idRegistro;
        this.idAluno = idAluno;
        this.idDisciplina = idDisciplina;
        this.idTurma = idTurma;
        this.notaP1 = notaP1;
        this.notaP2 = notaP2;
        this.notaTrabalho = notaTrabalho;
        this.mediaFinal = mediaFinal;
        this.totalFaltas = totalFaltas;
        this.percentualFrequencia = percentualFrequencia;
        this.situacaoAcademica = situacaoAcademica;
    }

    public int getIdRegistro() {
        return idRegistro;
    }

    public void setIdRegistro(int idRegistro) {
        this.idRegistro = idRegistro;
    }

    public int getIdAluno() {
        return idAluno;
    }

    public void setIdAluno(int idAluno) {
        this.idAluno = idAluno;
    }

    public int getIdDisciplina() {
        return idDisciplina;
    }

    public void setIdDisciplina(int idDisciplina) {
        this.idDisciplina = idDisciplina;
    }

    public int getIdTurma() {
        return idTurma;
    }

    public void setIdTurma(int idTurma) {
        this.idTurma = idTurma;
    }

    public BigDecimal getNotaP1() {
        return notaP1;
    }

    public void setNotaP1(BigDecimal notaP1) {
        this.notaP1 = notaP1;
    }

    public BigDecimal getNotaP2() {
        return notaP2;
    }

    public void setNotaP2(BigDecimal notaP2) {
        this.notaP2 = notaP2;
    }

    public BigDecimal getNotaTrabalho() {
        return notaTrabalho;
    }

    public void setNotaTrabalho(BigDecimal notaTrabalho) {
        this.notaTrabalho = notaTrabalho;
    }

    public BigDecimal getMediaFinal() {
        return mediaFinal;
    }

    public void setMediaFinal(BigDecimal mediaFinal) {
        this.mediaFinal = mediaFinal;
    }

    public int getTotalFaltas() {
        return totalFaltas;
    }

    public void setTotalFaltas(int totalFaltas) {
        this.totalFaltas = totalFaltas;
    }

    public BigDecimal getPercentualFrequencia() {
        return percentualFrequencia;
    }

    public void setPercentualFrequencia(BigDecimal percentualFrequencia) {
        this.percentualFrequencia = percentualFrequencia;
    }

    public String getSituacaoAcademica() {
        return situacaoAcademica;
    }

    public void setSituacaoAcademica(String situacaoAcademica) {
        this.situacaoAcademica = situacaoAcademica;
    }

    public void calcularMediaPonderada() {
    BigDecimal p1 = notaP1 != null ? notaP1 : BigDecimal.ZERO;
    BigDecimal p2 = notaP2 != null ? notaP2 : BigDecimal.ZERO;
    BigDecimal tr = notaTrabalho != null ? notaTrabalho : BigDecimal.ZERO;
    mediaFinal = p1.multiply(new BigDecimal("0.30"))
            .add(p2.multiply(new BigDecimal("0.40")))
            .add(tr.multiply(new BigDecimal("0.30")))
            .setScale(2, RoundingMode.HALF_UP);
}

public void calcularFrequencia(int totalAulas) {
    if (totalAulas <= 0) {
        percentualFrequencia = new BigDecimal("100.00");
        return;
    }
    double perc = ((totalAulas - totalFaltas) / (double) totalAulas) * 100.0;
    if (perc < 0) perc = 0;
    percentualFrequencia = BigDecimal.valueOf(perc).setScale(2, RoundingMode.HALF_UP);
}

public void atualizarSituacao() {
    double media = mediaFinal != null ? mediaFinal.doubleValue() : 0.0;
    double freq = percentualFrequencia != null ? percentualFrequencia.doubleValue() : 100.0;

    if (freq < 75.0)       situacaoAcademica = "Reprovado por Falta"; // RN34/35: precede nota
    else if (media >= 7.0) situacaoAcademica = "Aprovado";            // RN31
    else if (media >= 5.0) situacaoAcademica = "Exame";               // RN32
    else                   situacaoAcademica = "Reprovado por Nota";  // RN33
}
}