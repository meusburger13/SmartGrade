package view;

import controller.AcademicoController;
import model.Disciplina;
import model.RegistroAcademico;
import model.Turma;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

public class PainelLancamento extends JPanel {

    private AcademicoController controller;
    private JComboBox<String> comboTurmas;
    private JComboBox<String> comboDisciplinas;
    private JTable tabelaNotasFaltas;
    private DefaultTableModel tableModel;
    private JButton btnSalvarLancamento;

    private List<Turma> listaTurmasCache = new ArrayList<>();
    private List<Disciplina> listaDisciplinasCache = new ArrayList<>();
    private List<RegistroAcademico> listaRegistrosCache = new ArrayList<>();

    public PainelLancamento() {
        this.controller = new AcademicoController();
        setLayout(new BorderLayout(10, 10));

        // Filtros no topo (Norte)
        JPanel painelFiltro = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 10));
        painelFiltro.setBorder(BorderFactory.createTitledBorder("Seleção de Turma e Disciplina"));

        painelFiltro.add(new JLabel("Turma:"));
        comboTurmas = new JComboBox<>();
        painelFiltro.add(comboTurmas);

        painelFiltro.add(new JLabel("Disciplina:"));
        comboDisciplinas = new JComboBox<>();
        painelFiltro.add(comboDisciplinas);

        JButton btnCarregar = new JButton("Carregar Alunos");
        btnCarregar.addActionListener(e -> carregarAlunosTurma());
        painelFiltro.add(btnCarregar);

        add(painelFiltro, BorderLayout.NORTH);

        // Configuração da Tabela
        // Apenas P1 (col 2), P2 (col 3), Trab (col 4) e Faltas (col 6) são editáveis pelo professor
        tableModel = new DefaultTableModel(
            new Object[]{"ID Reg.", "ID Aluno", "Nota P1", "Nota P2", "Trabalho", "Média Final", "Faltas", "% Freq.", "Situação"}, 0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return (column == 2 || column == 3 || column == 4 || column == 6);
            }
        };

        tabelaNotasFaltas = new JTable(tableModel);
        tabelaNotasFaltas.setRowHeight(24);
        add(new JScrollPane(tabelaNotasFaltas), BorderLayout.CENTER);

        // Botão de salvar no rodapé (Sul)
        JPanel painelBotoes = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 10));
        JButton btnCalcular = new JButton("Recalcular Médias");
        btnCalcular.addActionListener(e -> atualizarCalculosTabela());
        painelBotoes.add(btnCalcular);

        btnSalvarLancamento = new JButton("Salvar Lançamentos e Avaliar Risco");
        btnSalvarLancamento.setFont(new Font("Arial", Font.BOLD, 12));
        btnSalvarLancamento.addActionListener(e -> salvarNotas());
        painelBotoes.add(btnSalvarLancamento);

        add(painelBotoes, BorderLayout.SOUTH);

        carregarCombos();
        
        javax.swing.JPanel painelAcoes = new javax.swing.JPanel();
        javax.swing.JButton btnEditar = new javax.swing.JButton("Editar Nota/Falta do Aluno Selecionado");
        
        btnEditar.addActionListener(e -> {
            int linhaSelecionada = tabelaNotasFaltas.getSelectedRow();

            if (linhaSelecionada >= 0) {
                try {
                    // 1. Pega o ID do aluno na coluna 1
                    int idAluno = Integer.parseInt(tabelaNotasFaltas.getValueAt(linhaSelecionada, 1).toString());

                    // 2. Pede as 3 notas separadamente
                    String strP1 = javax.swing.JOptionPane.showInputDialog(this, "Digite a NOTA P1:");
                    if (strP1 == null || strP1.trim().isEmpty()) return;

                    String strP2 = javax.swing.JOptionPane.showInputDialog(this, "Digite a NOTA P2:");
                    if (strP2 == null || strP2.trim().isEmpty()) return;

                    String strTrab = javax.swing.JOptionPane.showInputDialog(this, "Digite a NOTA DO TRABALHO:");
                    if (strTrab == null || strTrab.trim().isEmpty()) return;

                    // 3. Pede o novo número de faltas
                    String strFaltas = javax.swing.JOptionPane.showInputDialog(this, "Digite o novo total de FALTAS:");
                    if (strFaltas == null || strFaltas.trim().isEmpty()) return;

                    // 4. Converte os valores
                    double notaP1 = Double.parseDouble(strP1.replace(",", "."));
                    double notaP2 = Double.parseDouble(strP2.replace(",", "."));
                    double notaTrabalho = Double.parseDouble(strTrab.replace(",", "."));
                    int novasFaltas = Integer.parseInt(strFaltas);

                    // 5. Envia para o Banco de Dados com todas as variáveis (AGORA COMBINA COM O DAO)
                    // RN17: nota só pode ir de 0 a 10
                    if (notaP1 < 0 || notaP1 > 10 || notaP2 < 0 || notaP2 > 10
                            || notaTrabalho < 0 || notaTrabalho > 10 || novasFaltas < 0) {
                        javax.swing.JOptionPane.showMessageDialog(this, "Notas devem estar entre 0 e 10 e faltas não podem ser negativas.");
                        return;
                    }

                    RegistroAcademico reg = listaRegistrosCache.get(linhaSelecionada);
                    reg.setNotaP1(BigDecimal.valueOf(notaP1));
                    reg.setNotaP2(BigDecimal.valueOf(notaP2));
                    reg.setNotaTrabalho(BigDecimal.valueOf(notaTrabalho));
                    reg.setTotalFaltas(novasFaltas);

                    int totalAulas = listaDisciplinasCache.get(comboDisciplinas.getSelectedIndex()).getTotalAulas();
                    reg.calcularMediaPonderada();
                    reg.calcularFrequencia(totalAulas);
                    reg.atualizarSituacao();

                    controller.processarLancamento(reg);

                    javax.swing.JOptionPane.showMessageDialog(this, "Dados atualizados com sucesso!");

                    // 6. Recarrega a tabela visualmente
                    carregarAlunosTurma(); 

                } catch (NumberFormatException ex) {
                    javax.swing.JOptionPane.showMessageDialog(this, "Por favor, digite apenas números válidos (ex: 8.5 ou 8,5).");
                } catch (Exception ex) {
                    javax.swing.JOptionPane.showMessageDialog(this, "Erro: " + ex.getMessage());
                }
            } else {
                javax.swing.JOptionPane.showMessageDialog(this, "Selecione a linha de um aluno na tabela primeiro.");
            }
        });

        painelAcoes.add(btnEditar);
        add(painelAcoes, java.awt.BorderLayout.SOUTH);
        // --------------------------------------------------
    }

    private void carregarCombos() {
        comboTurmas.removeAllItems();
        listaTurmasCache = controller.listarTurmas();
        for (Turma t : listaTurmasCache) {
            comboTurmas.addItem(t.getCodigo() + " (" + t.getPeriodoSemestre() + ")");
        }

        comboDisciplinas.removeAllItems();
        listaDisciplinasCache = controller.listarDisciplinas();
        for (Disciplina d : listaDisciplinasCache) {
            comboDisciplinas.addItem(d.getNome());
        }
    }

    public void carregarAlunosTurma() {
        int idxTurma = comboTurmas.getSelectedIndex();
        int idxDisc = comboDisciplinas.getSelectedIndex();

        if (idxTurma < 0 || idxDisc < 0 || listaTurmasCache.isEmpty() || listaDisciplinasCache.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Selecione uma turma e disciplina válidas.");
            return;
        }

        int idTurma = listaTurmasCache.get(idxTurma).getIdTurma();
        int idDisc = listaDisciplinasCache.get(idxDisc).getIdDisciplina();

        listaRegistrosCache = controller.buscarRegistros(idTurma, idDisc);
        tableModel.setRowCount(0);

        for (RegistroAcademico r : listaRegistrosCache) {
            tableModel.addRow(new Object[]{
                r.getIdRegistro(),
                r.getIdAluno(),
                r.getNotaP1() != null ? r.getNotaP1() : "0.0",
                r.getNotaP2() != null ? r.getNotaP2() : "0.0",
                r.getNotaTrabalho() != null ? r.getNotaTrabalho() : "0.0",
                r.getMediaFinal() != null ? r.getMediaFinal() : "0.0",
                r.getTotalFaltas(),
                r.getPercentualFrequencia() != null ? r.getPercentualFrequencia() : "100.0",
                r.getSituacaoAcademica() != null ? r.getSituacaoAcademica() : "Cursando"
            });
        }

        if (listaRegistrosCache.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Nenhum registro encontrado para a turma e disciplina selecionadas.");
        }
    }

    public void atualizarCalculosTabela() {
        for (int i = 0; i < tableModel.getRowCount(); i++) {
            try {
                double p1 = Double.parseDouble(tableModel.getValueAt(i, 2).toString().replace(",", "."));
                double p2 = Double.parseDouble(tableModel.getValueAt(i, 3).toString().replace(",", "."));
                double trab = Double.parseDouble(tableModel.getValueAt(i, 4).toString().replace(",", "."));
                int faltas = Integer.parseInt(tableModel.getValueAt(i, 6).toString());

                // Média ponderada típica: 30% P1, 40% P2, 30% Trabalho
                double media = (p1 * 0.30) + (p2 * 0.40) + (trab * 0.30);
                
                // Exemplo: 80 horas de aula padrão
                int totalAulas = listaDisciplinasCache.get(comboDisciplinas.getSelectedIndex()).getTotalAulas();
                double percFreq = ((totalAulas - faltas) / (double) totalAulas) * 100.0;
                if (percFreq < 0) percFreq = 0;

                String situacao = "Aprovado";
                if (percFreq < 75.0) {
                    situacao = "Reprovado por Falta";
                } else if (media < 5.0) {
                    situacao = "Reprovado por Nota";
                } else if (media < 7.0) {
                    situacao = "Exame";
                }

                tableModel.setValueAt(BigDecimal.valueOf(media).setScale(2, RoundingMode.HALF_UP), i, 5);
                tableModel.setValueAt(BigDecimal.valueOf(percFreq).setScale(2, RoundingMode.HALF_UP), i, 7);
                tableModel.setValueAt(situacao, i, 8);

            } catch (Exception ex) {
                // Se algum valor for inválido, ignora a linha durante a digitação
            }
        }
    }

    public void salvarNotas() {
        atualizarCalculosTabela();

        for (int i = 0; i < tableModel.getRowCount(); i++) {
            RegistroAcademico reg = listaRegistrosCache.get(i);

            reg.setNotaP1(new BigDecimal(tableModel.getValueAt(i, 2).toString().replace(",", ".")));
            reg.setNotaP2(new BigDecimal(tableModel.getValueAt(i, 3).toString().replace(",", ".")));
            reg.setNotaTrabalho(new BigDecimal(tableModel.getValueAt(i, 4).toString().replace(",", ".")));
            reg.setMediaFinal(new BigDecimal(tableModel.getValueAt(i, 5).toString().replace(",", ".")));
            reg.setTotalFaltas(Integer.parseInt(tableModel.getValueAt(i, 6).toString()));
            reg.setPercentualFrequencia(new BigDecimal(tableModel.getValueAt(i, 7).toString().replace(",", ".")));
            reg.setSituacaoAcademica(tableModel.getValueAt(i, 8).toString());

            controller.processarLancamento(reg);
        }

        JOptionPane.showMessageDialog(this, "Todos os lançamentos foram atualizados e a IA processou as análises de risco!");
    }
}