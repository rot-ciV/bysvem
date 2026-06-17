package visao;

// import bysvem.modelo.*;  

import java.awt.BorderLayout;
import java.awt.Font;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

public class Aplicativo extends JFrame {

  public Aplicativo() {
    super();
    setTitle("Bysvem");
    setSize(1500, 1000);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setLayout(new BorderLayout());

    Font font = new Font("Arial", Font.BOLD, 20);
    
    JLabel nb1 = new JLabel("<html>Olá, seja muito bem vindo ao Bysvem!<br>================ Menu Principal ================</html>");
    nb1.setFont(font);
    nb1.setHorizontalAlignment(JLabel.CENTER); // opcional: centralizar

    JLabel lb1 = new JLabel("Digite algo: ");
    lb1.setFont(font);
    JTextField tf1 = new JTextField();
    tf1.setFont(font);
    JButton bt1 = new JButton("CLique aqui");
    bt1.setFont(font);

    add(nb1, BorderLayout.NORTH);
    add(lb1, BorderLayout.WEST);
    add(tf1, BorderLayout.CENTER);
    add(bt1, BorderLayout.SOUTH);
}

  public static void main(String[] args) {
    Aplicativo j = new Aplicativo();
    j.setVisible(true);
  }
  
}