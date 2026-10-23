package view;

import controller.AcademicoController;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import java.util.Map;

public class PainelDashboard extends JPanel {

    private AcademicoController controller;
    private JLabel lblTotalAlunos;
    private JLabel lblMediaGeral;
    private JLabel lblAlertasRisco;
    private JTable tabelaRisco;
    private DefaultTableModel tableModel;

    public PainelDashboard() {
        this.controller = new AcademicoController();
        setLayout(new BorderLayout(15, 15));
        setBorder(new EmptyBorder(15, 15, 15, 15));

        // --- PAINEL NORTE: CARDS DE INDICADORES (KPIs) ---
        JPanel painelCards = new JPanel(new GridLayout(1, 3, 15, 0));

        lblTotalAlunos = new JLabel("0", SwingConstants.CENTER);
        lblMediaGeral = new JLabel("0.0", SwingConstants.CENTER);
        lblAlertasRisco = new JLabel("0", SwingConstants.CENTER);

        painelCards.add(criarCardKPI("Total de Alunos", lblTotalAlunos, new Color(52, 152, 219)));
        painelCards.add(criarCardKPI("Média Geral do Curso", lblMediaGeral, new Color(46, 204, 113)));
        painelCards.add(criarCardKPI("Casos em Alto Risco", lblAlertasRisco, new Color(231, 76, 60)));

        add(painelCards, BorderLayout.NORTH);

        // --- PAINEL CENTRO: TABELA DE CASOS DE ATENÇÃO / RISCO ---
        JPanel painelTabela = new JPanel(new BorderLayout(5, 5));
        painelTabela.setBorder(BorderFactory.createTitledBorder("Casos Identificados pelo Modelo Preditivo (PredicaoRiscoML)"));

        // Substituído "ID Aluno" por "Nome do Aluno"
        tableModel = new DefaultTableModel(
            new Object[]{"Nome do Aluno", "ID Disciplina", "Média Final", "Frequência", "Nível de Risco", "Fatores / Intervenção"}, 0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tabelaRisco = new JTable(tableModel);
        tabelaRisco.setRowHeight(24);
        painelTabela.add(new JScrollPane(tabelaRisco), BorderLayout.CENTER);

        add(painelTabela, BorderLayout.CENTER);

        // --- PAINEL SUL: BOTÃO DE ATUALIZAR ---
        JPanel painelAcoes = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnAtualizar = new JButton("Atualizar Indicadores");
        btnAtualizar.addActionListener(e -> carregarDados());
        painelAcoes.add(btnAtualizar);

        add(painelAcoes, BorderLayout.SOUTH);

        carregarDados();
    }

    private JPanel criarCardKPI(String titulo, JLabel lblValor, Color corBorda) {
        JPanel card = new JPanel(new BorderLayout(5, 5));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(corBorda, 2),
            new EmptyBorder(10, 10, 10, 10)
        ));

        JLabel lblTitulo = new JLabel(titulo, SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 14));
        lblTitulo.setForeground(Color.DARK_GRAY);

        lblValor.setFont(new Font("Arial", Font.BOLD, 26));
        lblValor.setForeground(corBorda);

        card.add(lblTitulo, BorderLayout.NORTH);
        card.add(lblValor, BorderLayout.CENTER);
        return card;
    }

    public void carregarDados() {
        Map<String, Object> dados = controller.carregarDashboard();

        lblTotalAlunos.setText(String.valueOf(dados.get("totalAlunos")));
        lblMediaGeral.setText(String.valueOf(dados.get("mediaGeral")));
        lblAlertasRisco.setText(String.valueOf(dados.get("alertasAltoRisco")));

        tableModel.setRowCount(0);
        @SuppressWarnings("unchecked")
        List<Map<String, Object>> listaRisco = (List<Map<String, Object>>) dados.get("listaRisco");

        String perfil = model.Sessao.getInstance().getPerfil();
        int idUsuarioLogado = model.Sessao.getInstance().getIdUsuario();

        if (listaRisco != null) {
            for (Map<String, Object> r : listaRisco) {
                
                if ("ALUNO".equals(perfil)) {
                    int idAlunoDaLinha = (int) r.get("idAluno");
                    
                    if (idAlunoDaLinha == idUsuarioLogado) {
                        tableModel.addRow(new Object[]{
                            r.get("nomeAluno"), // Exibe o nome em vez do ID
                            r.get("idDisciplina"),
                            r.get("media"),
                            r.get("frequencia"),
                            r.get("risco"),
                            r.get("recomendacao")
                        });
                    }
                } else {
                    tableModel.addRow(new Object[]{
                        r.get("nomeAluno"), // Exibe o nome em vez do ID
                        r.get("idDisciplina"),
                        r.get("media"),
                        r.get("frequencia"),
                        r.get("risco"),
                        r.get("recomendacao")
                    });
                }
            }
        }
    }
}