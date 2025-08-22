package cr.ac.una.Views;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class CambiarCtr extends JFrame {
    private JPanel MainPanel;
    private JPanel TopPanel;
    private JPanel MidPanel;
    private JPanel ButtomPanel;
    private JPanel TopLeftPanel;
    private JPanel TopRightPanel;
    private JLabel ActualPassLabel;
    private JPasswordField ActualPassTF;
    private JPanel MidLeftPanel;
    private JPanel MidRightPanel;
    private JLabel newPassLabel;
    private JButton ConfirmarBTN;
    private JButton DeclineBTN;
    private JPasswordField newPassTF;

    // Constructor
    public CambiarCtr() {
        ConfirmarBTN.setPreferredSize(new Dimension(50, 30));
        DeclineBTN.setPreferredSize(new Dimension(50, 20));
        newPassTF.setPreferredSize(new Dimension(200, 30));
        ActualPassTF.setPreferredSize(new Dimension(200, 30));

        ConfirmarBTN.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        DeclineBTN.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        setContentPane(MainPanel);
        ImageIcon image = new ImageIcon(getClass().getResource("/PasswordLogo.png"));
        setIconImage(image.getImage());
        setTitle("Cambiar Contraseña");
        setSize(400, 300);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);

        DeclineBTN.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                Eliminar();
            }
        });

        ConfirmarBTN.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                CambiarPass();
            }
        });
    }

    // Al cambiar clave hacer una nueva instancia de Registro() **

    public void Eliminar(){
        ActualPassTF.setText("");
        newPassTF.setText("");
    }

    public void CambiarPass(){
        String _actualPass = ActualPassTF.getText().trim();
        String _newPass = newPassTF.getText().trim();

        if (_actualPass.isEmpty() || _newPass.isEmpty()){
            JOptionPane.showMessageDialog(null, "Por favor ingrese todos los campos");
        } else {
            dispose();
            JOptionPane.showMessageDialog(null, "Clave actualizada con éxito!", "Éxito", JOptionPane.INFORMATION_MESSAGE);

            Registro registro = new Registro();
            registro.setVisible(true);
        }
    }

    // Validar que la nueva contrasena tenga mayusculas, caracteres, mayor a 8 o sus condiciones

    public JPanel getMainPanel() { return MainPanel; }
}
