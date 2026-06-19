package bysvem;

import bysvem.visao.TelaLogin;
import javax.swing.SwingUtilities;

public class Programa {

    public static void main(String[] args) {
        // Inicia a interface gráfica na thread de eventos do Swing
        SwingUtilities.invokeLater(() -> {
            new TelaLogin();
        });
    }
}