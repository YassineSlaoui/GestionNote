package mgmt_guis;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import com.formdev.flatlaf.ui.FlatLineBorder;
import launcher.launcher;

@SuppressWarnings("serial")
public class AjoutEns extends JDialog implements ActionListener {

    private JPanel contentPane = new JPanel();
    private JLabel nomLabel = new JLabel("Nom Enseignant: ");
    private JTextField nomField = new JTextField(10);
    private JLabel cinLabel = new JLabel("#CIN Enseignant: ");
    private JTextField cinField = new JTextField(10);
    private JLabel loginLabel = new JLabel("Login Enseignant: ");
    private JTextField loginField = new JTextField(10);
    private JLabel pwdLabel = new JLabel("Mot de Passe: ");
    private JPasswordField pwdField = new JPasswordField(10);
    private JLabel cpwdLabel = new JLabel("Confirmer Mdp: ");
    private JPasswordField cpwdField = new JPasswordField(10);
    private DefaultTableModel tModel = null;

    public AjoutEns(DefaultTableModel tModel) {
	setModal(true);
	setTitle("Ajout d'Enseignants");
	setDefaultCloseOperation(DISPOSE_ON_CLOSE);
	getContentPane().add(contentPane);
	contentPane.setBorder(new EmptyBorder(new Insets(5, 10, 5, 10)));
	contentPane.setLayout(new BorderLayout(0, 0));

	this.tModel = tModel;

	JLabel northLabel = new JLabel("Saisir les informations suivantes:");
	northLabel.setFont(new Font("Times New Roman", Font.BOLD, 18));
	contentPane.add(northLabel, BorderLayout.NORTH);
	northLabel.setBorder(new EmptyBorder(new Insets(0, 0, 10, 0)));

	JPanel centerPanel = new JPanel();
	contentPane.add(centerPanel, BorderLayout.CENTER);
	centerPanel.setLayout(new GridLayout(5, 1));
	centerPanel.setBorder(new FlatLineBorder(new Insets(10, 5, 10, 5), Color.gray));

	JPanel nomPanel = new JPanel();
	nomPanel.add(nomLabel);
	nomPanel.add(nomField);
	centerPanel.add(nomPanel);
	nomField.addActionListener(this);

	JPanel cinPanel = new JPanel();
	cinPanel.add(cinLabel);
	cinPanel.add(cinField);
	centerPanel.add(cinPanel);
	cinField.addActionListener(this);

	JPanel loginPanel = new JPanel();
	loginPanel.add(loginLabel);
	loginPanel.add(loginField);
	centerPanel.add(loginPanel);
	loginField.addActionListener(this);

	JPanel pwdPanel = new JPanel();
	pwdPanel.add(pwdLabel);
	pwdPanel.add(pwdField);
	centerPanel.add(pwdPanel);
	pwdField.addActionListener(this);

	JPanel cpwdPanel = new JPanel();
	cpwdPanel.add(cpwdLabel);
	cpwdPanel.add(cpwdField);
	centerPanel.add(cpwdPanel);
	cpwdField.addActionListener(this);

	JPanel southPanel = new JPanel();
	contentPane.add(southPanel, BorderLayout.SOUTH);
	JButton submit = new JButton("Soumettre");
	southPanel.add(submit);
	submit.addActionListener(this);

	pack();
	contentPane.grabFocus();
	setResizable(false);
	setLocationRelativeTo(null);
    }

    @SuppressWarnings("deprecation")
    @Override
    public void actionPerformed(ActionEvent e) {
	String nom = nomField.getText().trim();
	String cin = cinField.getText().trim();
	String login = loginField.getText().trim();
	String pwd = pwdField.getText().trim();
	String cpwd = cpwdField.getText().trim();
	if (nom.equals("")) {
	    JOptionPane.showMessageDialog(contentPane, "Le nom ne peut pas etre vide!", "Attention", JOptionPane.WARNING_MESSAGE);
	} else if (login.equals("")) {
	    JOptionPane.showMessageDialog(contentPane, "Le nom d'utilisateur ne peut pas etre vide!", "Attention", JOptionPane.WARNING_MESSAGE);
	} else if (pwd.equals("")) {
	    JOptionPane.showMessageDialog(contentPane, "Le mot de passe ne peut pas etre vide!", "Attention", JOptionPane.WARNING_MESSAGE);
	} else if (!pwd.equals(cpwd)) {
	    JOptionPane.showMessageDialog(contentPane, "Verifiez le mot de passe!", "Attention", JOptionPane.WARNING_MESSAGE);
	} else {
	    int id = launcher.addEns(nom, cin, login, pwd);
	    if (id == -1)
		JOptionPane.showMessageDialog(contentPane, "Le nom d'utilisateur existe Deja!", "Attention", JOptionPane.ERROR_MESSAGE);
	    else
		tModel.addRow(new Object[] { id, nom, cin, login, pwd });
	    nomField.setText("");
	    cinField.setText("");
	    loginField.setText("");
	    pwdField.setText("");
	    cpwdField.setText("");
	}
    }

}