package view;

import javax.swing.JFrame;
import javax.swing.JButton;
import javax.swing.JTextField;
import javax.swing.JPasswordField;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class TelaLogin extends JFrame {

    public TelaLogin() {
        // 1. Configurações básicas da janela principal
        setTitle("SmartGrade - Acesso");
        setSize(350, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Fecha o processo ao fechar no X
        setLocationRelativeTo(null); // Centraliza a janela perfeitamente no ecrã
        
     // Criar o painel usando GridLayout
        javax.swing.JPanel painel = new javax.swing.JPanel(new java.awt.GridLayout(3, 2, 10, 10));
        painel.setBorder(javax.swing.BorderFactory.createEmptyBorder(20, 20, 20, 20));

        painel.add(new javax.swing.JLabel("Login:"));
        javax.swing.JTextField txtLogin = new javax.swing.JTextField();
        painel.add(txtLogin);

        painel.add(new javax.swing.JLabel("Senha:"));
        javax.swing.JPasswordField txtSenha = new javax.swing.JPasswordField();
        painel.add(txtSenha);

        painel.add(new javax.swing.JLabel("")); // Espaço para empurrar o botão
        javax.swing.JButton btnEntrar = new javax.swing.JButton("Entrar");

        // --- INÍCIO DA AÇÃO DO CLIQUE DO BOTÃO ---
        btnEntrar.addActionListener(e -> {
            String loginDigitado = txtLogin.getText();
            String senhaDigitada = new String(txtSenha.getPassword());
            
            try {
                // 1. Chama o DAO para verificar na base de dados
                dao.UsuarioDAO dao = new dao.UsuarioDAO();
                model.Usuario usuario = dao.autenticar(loginDigitado, senhaDigitada);
                
                if (usuario != null) {
                    // 1. Guarda os dados na Sessao usando o Singleton corretamente
                    model.Sessao sessao = model.Sessao.getInstance();
                    sessao.setIdUsuario(usuario.getId());
                    sessao.setNome(usuario.getNome());
                    sessao.setPerfil(usuario.getPerfil());
                    
                    if ("PROFESSOR".equals(usuario.getPerfil())) {
                        // Guarda o ID da disciplina na sessão para os filtros funcionarem
                        sessao.setIdDisciplinaProfessor(usuario.getIdDisciplina());
                    }
                    // -----------------------

                    // 2. Abre a Tela Principal
                    new TelaPrincipal().setVisible(true);
                    
                    // 3. Fecha a janela de login atual
                    this.dispose();
                   
                }
                 else {
                    // Se o utilizador não existir ou a palavra-passe estiver errada
                    javax.swing.JOptionPane.showMessageDialog(this, "Login ou palavra-passe incorretos!");
                }
            } catch (Exception ex) {
                // Se houver falha de ligação com o banco de dados, mostra o erro no ecrã
                javax.swing.JOptionPane.showMessageDialog(this, "Erro de ligação ao banco: " + ex.getMessage());
            }
        });
        // --- FIM DA AÇÃO DO CLIQUE ---

        painel.add(btnEntrar);
        add(painel);
    }

    // Método main para poder rodar e testar a tela isoladamente no Eclipse
    public static void main(String[] args) {
        // Instancia a janela e torna-a visível
        TelaLogin tela = new TelaLogin();
        tela.setVisible(true);
    }
}