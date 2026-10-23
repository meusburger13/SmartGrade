package model;

public class Sessao {
    private static Sessao instancia;
    
    private int idUsuario;
    private String nome;
    private String login;
    private String perfil; // "Administrador", "Coordenador", "Professor", "Aluno"
    private Integer idDisciplinaProfessor; // ID da disciplina vinculada (se for professor)

    private Sessao() {}

    public static Sessao getInstance() {
        if (instancia == null) {
            instancia = new Sessao();
        }
        return instancia;
    }

    public void limparSessao() {
        idUsuario = 0;
        nome = null;
        login = null;
        perfil = null;
        idDisciplinaProfessor = null;
    }

    // Getters e Setters
    public int getIdUsuario() { return idUsuario; }
    public void setIdUsuario(int idUsuario) { this.idUsuario = idUsuario; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getPerfil() { return perfil; }
    public void setPerfil(String perfil) { this.perfil = perfil; }

    public Integer getIdDisciplinaProfessor() { return idDisciplinaProfessor; }
    public void setIdDisciplinaProfessor(Integer idDisciplinaProfessor) { 
        this.idDisciplinaProfessor = idDisciplinaProfessor; 
    }
}