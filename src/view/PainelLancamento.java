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
        tableModel = new DefaultTableModel(
            new Object[]{"Nome do Aluno", "Nota P1", "Nota P2", "Trabalho", "Média Final", "Faltas", "% Freq.", "Situação"}, 0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return (column == 1 || column == 2 || column == 3 || column == 5);
            }
        };

        tabelaNotasFaltas = new JTable(tableModel);
        tabelaNotasFaltas.setRowHeight(24);
        add(new JScrollPane(tabelaNotasFaltas), BorderLayout.CENTER);

        // Rodapé com botões
        JPanel painelSulCompleto = new JPanel(new BorderLayout());

        JPanel painelBotoes = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 10));
        JButton btnCalcular = new JButton("Recalcular Médias");
        btnCalcular.addActionListener(e -> atualizarCalculosTabela());
        painelBotoes.add(btnCalcular);

        btnSalvarLancamento = new JButton("Salvar Lançamentos e Avaliar Risco");
        btnSalvarLancamento.setFont(new Font("Arial", Font.BOLD, 12));
        btnSalvarLancamento.addActionListener(e -> salvarNotas());
        painelBotoes.add(btnSalvarLancamento);

        painelSulCompleto.add(painelBotoes, BorderLayout.NORTH);

        // Painel de edição avulsa
        JPanel painelAcoes = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton btnEditar = new JButton("Editar Nota/Falta do Aluno Selecionado");
        
        btnEditar.addActionListener(e -> {
            int linhaSelecionada = tabelaNotasFaltas.getSelectedRow();
            if (linhaSelecionada >= 0) {
                try {
                    RegistroAcademico regSelecionado = listaRegistrosCache.get(linhaSelecionada);

                    String strP1 = JOptionPane.showInputDialog(this, "Digite a NOTA P1 (0 a 10):");
                    if (strP1 == null || strP1.trim().isEmpty()) return;

                    String strP2 = JOptionPane.showInputDialog(this, "Digite a NOTA P2 (0 a 10):");
                    if (strP2 == null || strP2.trim().isEmpty()) return;

                    String strTrab = JOptionPane.showInputDialog(this, "Digite a NOTA DO TRABALHO (0 a 10):");
                    if (strTrab == null || strTrab.trim().isEmpty()) return;

                    String strFaltas = JOptionPane.showInputDialog(this, "Digite o novo total de FALTAS:");
                    if (strFaltas == null || strFaltas.trim().isEmpty()) return;

                    double notaP1 = Double.parseDouble(strP1.replace(",", "."));
                    double notaP2 = Double.parseDouble(strP2.replace(",", "."));
                    double notaTrabalho = Double.parseDouble(strTrab.replace(",", "."));
                    int novasFaltas = Integer.parseInt(strFaltas);

                    if (notaP1 < 0 || notaP1 > 10 || notaP2 < 0 || notaP2 > 10 || notaTrabalho < 0 || notaTrabalho > 10) {
                        JOptionPane.showMessageDialog(this, "ERRO: As notas devem ser entre 0.0 e 10.0!", "Nota Inválida", JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    dao.AcademicoDAO dao = new dao.AcademicoDAO();
                    regSelecionado.setNotaP1(BigDecimal.valueOf(notaP1));
                    regSelecionado.setNotaP2(BigDecimal.valueOf(notaP2));
                    regSelecionado.setNotaTrabalho(BigDecimal.valueOf(notaTrabalho));
                    regSelecionado.setTotalFaltas(novasFaltas);
                    dao.salvarRegistro(regSelecionado);

                    JOptionPane.showMessageDialog(this, "Dados atualizados com sucesso!");
                    carregarAlunosTurma(); 

                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(this, "Por favor, digite apenas números válidos.");
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Erro: " + ex.getMessage());
                }
            } else {
                JOptionPane.showMessageDialog(this, "Selecione a linha de um aluno na tabela primeiro.");
            }
        });

        painelAcoes.add(btnEditar);
        painelSulCompleto.add(painelAcoes, BorderLayout.SOUTH);
        add(painelSulCompleto, BorderLayout.SOUTH);

        String perfil = model.Sessao.getInstance().getPerfil();
        if ("ALUNO".equals(perfil)) {
            painelBotoes.setVisible(false);
            painelAcoes.setVisible(false);
            comboTurmas.setEnabled(false);
            comboDisciplinas.setEnabled(false);
        }

        carregarCombos();
    }

    private void carregarCombos() {
        comboTurmas.removeAllItems();
        listaTurmasCache = controller.listarTurmas();
        for (Turma t : listaTurmasCache) {
            comboTurmas.addItem(t.getCodigo() + " (" + t.getPeriodoSemestre() + ")");
        }

        comboDisciplinas.removeAllItems();
        String perfil = model.Sessao.getInstance().getPerfil();
        
        if ("PROFESSOR".equals(perfil)) {
            Integer idDisciplinaProf = model.Sessao.getInstance().getIdDisciplinaProfessor();
            listaDisciplinasCache = new ArrayList<>();
            if (idDisciplinaProf != null) {
                for (Disciplina d : controller.listarDisciplinas()) {
                    if (d.getIdDisciplina() == idDisciplinaProf) {
                        listaDisciplinasCache.add(d);
                    }
                }
            }
        } else {
            listaDisciplinasCache = controller.listarDisciplinas();
        }

        for (Disciplina d : listaDisciplinasCache) {
            comboDisciplinas.addItem(d.getNome());
        }
    }

    public void carregarAlunosTurma() {
        tableModel.setRowCount(0);
        String perfil = model.Sessao.getInstance().getPerfil();
        int idUsuarioLogado = model.Sessao.getInstance().getIdUsuario();

        if ("ALUNO".equals(perfil)) {
            // Lógica isolada para aluno
        } else {
            int idxTurma = comboTurmas.getSelectedIndex();
            int idxDisc = comboDisciplinas.getSelectedIndex();

            if (idxTurma < 0 || idxDisc < 0 || listaTurmasCache.isEmpty() || listaDisciplinasCache.isEmpty()) return;

            int idTurma = listaTurmasCache.get(idxTurma).getIdTurma();
            int idDisc = listaDisciplinasCache.get(idxDisc).getIdDisciplina();

            listaRegistrosCache = controller.buscarRegistros(idTurma, idDisc);

            for (RegistroAcademico r : listaRegistrosCache) {
                tableModel.addRow(new Object[]{
                    r.getNomeAluno() != null ? r.getNomeAluno() : "Aluno ID " + r.getIdAluno(),
                    r.getNotaP1() != null ? r.getNotaP1() : "0.00",
                    r.getNotaP2() != null ? r.getNotaP2() : "0.00",
                    r.getNotaTrabalho() != null ? r.getNotaTrabalho() : "0.00",
                    r.getMediaFinal() != null ? r.getMediaFinal() : "0.00",
                    r.getTotalFaltas(),
                    r.getPercentualFrequencia() != null ? r.getPercentualFrequencia() : "100.00",
                    r.getSituacaoAcademica() != null ? r.getSituacaoAcademica() : "Cursando"
                });
            }
        }
    }

    public void atualizarCalculosTabela() {
        // Bloqueia a edição em segurança
        if (tabelaNotasFaltas.isEditing() && tabelaNotasFaltas.getCellEditor() != null) {
            tabelaNotasFaltas.getCellEditor().stopCellEditing();
        }

        boolean avisoMostrado = false;

        for (int i = 0; i < tableModel.getRowCount(); i++) {
            try {
                // Lê garantindo que não há NullPointerException e ignora espaços/vírgulas
                String strP1 = String.valueOf(tableModel.getValueAt(i, 1)).replace(",", ".").trim();
                String strP2 = String.valueOf(tableModel.getValueAt(i, 2)).replace(",", ".").trim();
                String strTrab = String.valueOf(tableModel.getValueAt(i, 3)).replace(",", ".").trim();
                String strFaltas = String.valueOf(tableModel.getValueAt(i, 5)).replace(",", ".").trim();

                double p1 = strP1.isEmpty() || strP1.equals("null") ? 0.0 : Double.parseDouble(strP1);
                double p2 = strP2.isEmpty() || strP2.equals("null") ? 0.0 : Double.parseDouble(strP2);
                double trab = strTrab.isEmpty() || strTrab.equals("null") ? 0.0 : Double.parseDouble(strTrab);
                int faltas = strFaltas.isEmpty() || strFaltas.equals("null") ? 0 : (int) Double.parseDouble(strFaltas);

                // TRAVÃO VISUAL DE INTERFACE IMEDIATO
                if (p1 > 10.0) { p1 = 10.0; tableModel.setValueAt("10.00", i, 1); avisoMostrado = true; }
                if (p1 < 0.0)  { p1 = 0.0;  tableModel.setValueAt("0.00", i, 1); avisoMostrado = true; }
                
                if (p2 > 10.0) { p2 = 10.0; tableModel.setValueAt("10.00", i, 2); avisoMostrado = true; }
                if (p2 < 0.0)  { p2 = 0.0;  tableModel.setValueAt("0.00", i, 2); avisoMostrado = true; }
                
                if (trab > 10.0) { trab = 10.0; tableModel.setValueAt("10.00", i, 3); avisoMostrado = true; }
                if (trab < 0.0)  { trab = 0.0;  tableModel.setValueAt("0.00", i, 3); avisoMostrado = true; }

                // CÁLCULO PRECISO DA MÉDIA 
                double media = (p1 + p2 + trab) / 3.0;
                
                int totalAulas = 40;
                double percFreq = ((totalAulas - faltas) / (double) totalAulas) * 100.0;
                if (percFreq < 0) percFreq = 0;

                String situacao = "Aprovado";
                if (percFreq < 75.0) {
                    situacao = "Reprovado por Falta";
                } else if (media < 5.0) {
                    situacao = "Reprovado";
                } else if (media < 6.5) {
                    situacao = "Exame";
                }

                // Aplica de volta no ecrã formatado!
                tableModel.setValueAt(BigDecimal.valueOf(media).setScale(2, RoundingMode.HALF_UP).toString(), i, 4);
                tableModel.setValueAt(BigDecimal.valueOf(percFreq).setScale(2, RoundingMode.HALF_UP).toString(), i, 6);
                tableModel.setValueAt(situacao, i, 7);

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, 
                    "Erro ao calcular na linha " + (i+1) + " (Provável erro de formatação/letras).\n" + ex.getMessage(), 
                    "Erro de Cálculo", JOptionPane.ERROR_MESSAGE);
                return; // Se houver erro, avisa e para, em vez de continuar mudo!
            }
        }

        if (avisoMostrado) {
            JOptionPane.showMessageDialog(this, 
                "Atenção: Algumas notas excederam o limite de 10.0 (ou inferiores a 0) e foram corrigidas automaticamente para o máximo/mínimo permitido.", 
                "Correção Automática Aplicada", JOptionPane.WARNING_MESSAGE);
        }
    }

    public void salvarNotas() {
        atualizarCalculosTabela(); // Recalcula e força correções ANTES de gravar

        int idxTurma = comboTurmas.getSelectedIndex();
        int idxDisc = comboDisciplinas.getSelectedIndex();
        if (idxTurma < 0 || idxDisc < 0) return;

        int idTurmaSelecionada = listaTurmasCache.get(idxTurma).getIdTurma();
        int idDiscSelecionada = listaDisciplinasCache.get(idxDisc).getIdDisciplina();

        for (int i = 0; i < tableModel.getRowCount(); i++) {
            try {
                RegistroAcademico reg = listaRegistrosCache.get(i);

                reg.setIdTurma(idTurmaSelecionada);
                reg.setIdDisciplina(idDiscSelecionada);

                String strP1 = String.valueOf(tableModel.getValueAt(i, 1)).replace(",", ".");
                String strP2 = String.valueOf(tableModel.getValueAt(i, 2)).replace(",", ".");
                String strTrab = String.valueOf(tableModel.getValueAt(i, 3)).replace(",", ".");
                String strMedia = String.valueOf(tableModel.getValueAt(i, 4)).replace(",", ".");
                String strFaltas = String.valueOf(tableModel.getValueAt(i, 5)).replace(",", ".");
                String strFreq = String.valueOf(tableModel.getValueAt(i, 6)).replace(",", ".");
                String situacao = String.valueOf(tableModel.getValueAt(i, 7));

                reg.setNotaP1(new BigDecimal(strP1));
                reg.setNotaP2(new BigDecimal(strP2));
                reg.setNotaTrabalho(new BigDecimal(strTrab));
                reg.setMediaFinal(new BigDecimal(strMedia));
                reg.setTotalFaltas((int)Double.parseDouble(strFaltas));
                reg.setPercentualFrequencia(new BigDecimal(strFreq));
                reg.setSituacaoAcademica(situacao);

                controller.processarLancamento(reg);
            } catch (Exception ex) {
                 JOptionPane.showMessageDialog(this, "Erro crítico ao gravar linha " + (i+1) + ". " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
                 return;
            }
        }

        JOptionPane.showMessageDialog(this, "Lançamentos salvos e processados com segurança para esta disciplina!");
        carregarAlunosTurma(); // Limpa e puxa da base novamente para refletir o estado real
    }
}