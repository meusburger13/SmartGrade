package controller;

import dao.AcademicoDAO;
import dao.AlunoDAO;
import dao.AnalisePreditivaDAO;
import dao.DisciplinaDAO;
import dao.TurmaDAO;
import model.Aluno;
import model.AnalisePreditiva;
import model.Disciplina;
import model.RegistroAcademico;
import model.Turma;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AcademicoController {

    private AcademicoDAO dao;
    private AlunoDAO alunoDAO;
    private TurmaDAO turmaDAO;
    private DisciplinaDAO disciplinaDAO;
    private PredicaoRiscoML mlService;
    private AnalisePreditivaDAO analiseDAO;

    public AcademicoController() {
        this.dao = new AcademicoDAO();
        this.alunoDAO = new AlunoDAO();
        this.turmaDAO = new TurmaDAO();
        this.disciplinaDAO = new DisciplinaDAO();
        this.mlService = new PredicaoRiscoML();
        this.analiseDAO = new AnalisePreditivaDAO();
    }

    // --- ALUNOS ---
    public String salvarAluno(String nome, String ra, String email) {
        if (nome == null || nome.trim().isEmpty()) return "O nome do aluno é obrigatório!";
        if (ra == null || ra.trim().isEmpty()) return "O RA do aluno é obrigatório!";
        if (email == null || email.trim().isEmpty() || !email.contains("@")) return "Informe um e-mail válido!";

        try {
            Aluno aluno = new Aluno(ra.trim(), nome.trim(), email.trim());
            alunoDAO.cadastrar(aluno);
            return "Aluno cadastrado com sucesso!";
        } catch (Exception e) {
            return "Erro ao salvar aluno: " + e.getMessage();
        }
    }

    public List<Aluno> listarAlunos() {
        return alunoDAO.listarTodos();
    }

    // --- CARREGAMENTO DE COMBOS ---
    public List<Turma> listarTurmas() {
        return turmaDAO.listarTodos();
    }

    public List<Disciplina> listarDisciplinas() {
        return disciplinaDAO.listarTodos();
    }

    // --- LANÇAMENTOS E REGRAS ---
    public List<RegistroAcademico> buscarRegistros(int idTurma, int idDisciplina) {
        return dao.buscarPorTurmaDisciplina(idTurma, idDisciplina);
    }

    public void processarLancamento(RegistroAcademico registro) {
        if (registro.getIdRegistro() > 0) {
            dao.atualizarNotasFaltas(registro);
        } else {
            dao.salvarRegistro(registro);
        }

        // Predição automática de inteligência / risco
        AnalisePreditiva analise = mlService.classificarRisco(registro);
        analiseDAO.cadastrar(analise);
    }

    public Map<String, Object> carregarDashboard() {
        Map<String, Object> dados = new HashMap<>();

        List<Aluno> alunos = alunoDAO.listarTodos();
        int totalAlunos = alunos.size();

        // Busca todos os registros de turmas 1 e 2 em todas as disciplinas de 1 a 5
        List<RegistroAcademico> todosRegistros = new java.util.ArrayList<>();
        for (int t = 1; t <= 2; t++) {
            for (int d = 1; d <= 5; d++) {
                List<RegistroAcademico> lista = dao.buscarPorTurmaDisciplina(t, d);
                if (lista != null) {
                    todosRegistros.addAll(lista);
                }
            }
        }

        System.out.println("Total de registros acadêmicos avaliados: " + todosRegistros.size());

        double somaMedias = 0.0;
        int countMedias = 0;
        int alertasRisco = 0;
        List<Map<String, Object>> listaRiscoTabela = new java.util.ArrayList<>();

        for (RegistroAcademico reg : todosRegistros) {
            if (reg.getMediaFinal() != null) {
                somaMedias += reg.getMediaFinal().doubleValue();
                countMedias++;
            }

            // Executa a classificação de Machine Learning
            AnalisePreditiva analise = mlService.classificarRisco(reg);
            String risco = (analise != null && analise.getNivelRisco() != null) ? analise.getNivelRisco().trim() : "Baixo";

            System.out.println("Aluno ID " + reg.getIdAluno() + " - Disc " + reg.getIdDisciplina() + " -> Média: " + reg.getMediaFinal() + " | Risco classificado: " + risco);

            boolean isRiscoAlto = risco.toUpperCase().contains("ALTO") || risco.toUpperCase().contains("CRIT") || risco.toUpperCase().contains("MÉDIO") || risco.toUpperCase().contains("MEDIO");

            if (isRiscoAlto) {
                alertasRisco++;

                Map<String, Object> item = new HashMap<>();
                item.put("idAluno", reg.getIdAluno());
                item.put("nomeAluno", reg.getNomeAluno() != null ? reg.getNomeAluno() : "Aluno ID " + reg.getIdAluno());
                item.put("idDisciplina", reg.getIdDisciplina());
                item.put("media", reg.getMediaFinal());
                item.put("frequencia", reg.getPercentualFrequencia() != null ? reg.getPercentualFrequencia() + "%" : "N/D");
                item.put("risco", risco);
                item.put("recomendacao", "Acompanhamento pedagógico sugerido para risco " + risco);
                listaRiscoTabela.add(item);
            }
        }

        double mediaGeral = countMedias > 0 ? (somaMedias / countMedias) : 0.0;

        dados.put("totalAlunos", totalAlunos);
        dados.put("mediaGeral", Math.round(mediaGeral * 100.0) / 100.0);
        dados.put("alertasAltoRisco", alertasRisco);
        dados.put("listaRisco", listaRiscoTabela);

        return dados;
    }
}