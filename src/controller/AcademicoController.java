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

import java.util.ArrayList;
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
        // Usuário logado: vai dentro do bloco de auditoria (quem lançou a nota)
        int idUsuario = model.Sessao.getInstance().getIdUsuario();

        // Grava a nota e o bloco de auditoria na mesma transação.
        // O DAO decide entre inserir e atualizar e preenche o idRegistro no objeto.
        boolean gravou = dao.salvarRegistro(registro, idUsuario);

        if (!gravou) {
            // Nota rejeitada (inválida) ou erro de banco: não gera análise de risco
            return;
        }

        // Predição automática de risco (usa o idRegistro já preenchido pelo DAO)
        AnalisePreditiva analise = mlService.classificarRisco(registro);
        analiseDAO.cadastrar(analise);
    }

    public Map<String, Object> carregarDashboard() {
        Map<String, Object> dados = new HashMap<>();
        int totalAlunos = alunoDAO.listarTodos().size();

        // Percorre TODAS as turmas e disciplinas cadastradas (sem IDs fixos)
        List<Disciplina> disciplinas = disciplinaDAO.listarTodos();
        List<RegistroAcademico> todosRegistros = new ArrayList<>();
        for (Turma t : turmaDAO.listarTodos()) {
            for (Disciplina d : disciplinas) {
                todosRegistros.addAll(dao.buscarPorTurmaDisciplina(t.getIdTurma(), d.getIdDisciplina()));
            }
        }

        double somaMedias = 0.0;
        int countMedias = 0, alto = 0, medio = 0;
        List<Map<String, Object>> listaRisco = new ArrayList<>();

        for (RegistroAcademico reg : todosRegistros) {
            if (reg.getMediaFinal() != null) {
                somaMedias += reg.getMediaFinal().doubleValue();
                countMedias++;
            }

            AnalisePreditiva analise = mlService.classificarRisco(reg);
            String risco = (analise != null && analise.getNivelRisco() != null) ? analise.getNivelRisco().toUpperCase().trim() : "BAIXO";

            String recomendacao;
            if (risco.contains("ALTO")) {
                alto++;
                recomendacao = "Acompanhamento pedagógico imediato (prioridade máxima)";
            } else if (risco.contains("MEDIO") || risco.contains("MÉDIO")) {
                medio++;
                recomendacao = "Monitoramento preventivo";
            } else {
                continue; // BAIXO não entra na tabela de atenção do Dashboard
            }

            Map<String, Object> item = new HashMap<>();
            item.put("idAluno", reg.getIdAluno());
            item.put("nomeAluno", reg.getNomeAluno() != null ? reg.getNomeAluno() : "Aluno ID " + reg.getIdAluno());
            item.put("idDisciplina", reg.getIdDisciplina());
            item.put("media", reg.getMediaFinal());
            item.put("frequencia", reg.getPercentualFrequencia() != null ? reg.getPercentualFrequencia() + "%" : "N/D");
            item.put("risco", risco);
            item.put("recomendacao", recomendacao);
            listaRisco.add(item);
        }

        double mediaGeral = countMedias > 0 ? somaMedias / countMedias : 0.0;
        dados.put("totalAlunos", totalAlunos);
        dados.put("mediaGeral", Math.round(mediaGeral * 100.0) / 100.0);
        dados.put("alertasAltoRisco", alto);
        dados.put("alertasMedioRisco", medio);
        dados.put("listaRisco", listaRisco);
        return dados;
    }
}