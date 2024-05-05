package mgmt_guis;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import com.formdev.flatlaf.ui.FlatLineBorder;

import launcher.launcher;

@SuppressWarnings("serial")
public class AjoutClasse extends JDialog implements ActionListener {

    private JPanel contentPane = new JPanel();
    private JLabel nomLabel = new JLabel("Nom Classe: ");
    private JTextField nomField = new JTextField(10);
    private DefaultTableModel tModel = null;

    public AjoutClasse(DefaultTableModel tModel) {
	setModal(true);
	setTitle("Ajout de Classes");
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
	centerPanel.setBorder(new FlatLineBorder(new Insets(10, 5, 10, 5), Color.gray));

	JPanel nomPanel = new JPanel();
	nomPanel.add(nomLabel);
	nomPanel.add(nomField);
	centerPanel.add(nomPanel);
	nomField.addActionListener(this);

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

    @Override
    public void actionPerformed(ActionEvent e) {
	String nom = nomField.getText().trim();
	if (nom.equals("")) {
	    JOptionPane.showMessageDialog(contentPane, "Le nom ne peut pas etre vide!", "Attention", JOptionPane.WARNING_MESSAGE);
	} else {
	    int id = launcher.addClasse(nom);
	    tModel.addRow(new Object[] { id, nom });
	    nomField.setText("");
	}
    }

}