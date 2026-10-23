package view; 

import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        // Aplica o tema Nimbus para modernizar o visual
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Inicia a aplicação abrindo a Tela de Login
        java.awt.EventQueue.invokeLater(() -> {
            new TelaLogin().setVisible(true);
        });
    }
}