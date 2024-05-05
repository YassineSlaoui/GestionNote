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
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import com.formdev.flatlaf.ui.FlatLineBorder;
import launcher.launcher;

@SuppressWarnings("serial")
public class AjoutMat extends JDialog implements ActionListener {

    private JPanel contentPane = new JPanel();
    private JLabel nomLabel = new JLabel("Matiere: ");
    private JTextField nomField = new JTextField(10);
    private JLabel dsLabel = new JLabel("Coef DS: ");
    private JTextField dsField = new JTextField(10);
    private JLabel tpLabel = new JLabel("Coef TP: ");
    private JTextField tpField = new JTextField(10);
    private JLabel exLabel = new JLabel("Coef Examen: ");
    private JTextField exField = new JTextField(10);
    private JLabel matLabel = new JLabel("Coef Matiere: ");
    private JTextField matField = new JTextField(10);
    private DefaultTableModel tModel = null;

    public AjoutMat(DefaultTableModel tModel) {
	setModal(true);
	setTitle("Ajout de Matieres");
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
	cinPanel.add(dsLabel);
	cinPanel.add(dsField);
	centerPanel.add(cinPanel);
	dsField.addActionListener(this);

	JPanel loginPanel = new JPanel();
	loginPanel.add(tpLabel);
	loginPanel.add(tpField);
	centerPanel.add(loginPanel);
	tpField.addActionListener(this);

	JPanel pwdPanel = new JPanel();
	pwdPanel.add(exLabel);
	pwdPanel.add(exField);
	centerPanel.add(pwdPanel);
	exField.addActionListener(this);

	JPanel cpwdPanel = new JPanel();
	cpwdPanel.add(matLabel);
	cpwdPanel.add(matField);
	centerPanel.add(cpwdPanel);
	matField.addActionListener(this);

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
	Double ds = -1.;
	Double tp = -1.;
	Double ex = -1.;
	Double mat = -1.;
	try {
	    ds = Double.valueOf(dsField.getText().trim());
	    tp = Double.valueOf(tpField.getText().trim());
	    ex = Double.valueOf(exField.getText().trim());
	    mat = Double.valueOf(matField.getText().trim());
	} catch (Exception e2) {}
	if (nom.equals("")) {
	    JOptionPane.showMessageDialog(contentPane, "Le nom ne peut pas etre vide!", "Attention", JOptionPane.WARNING_MESSAGE);
	} else if (ex < 0 || ex > 1) {
	    JOptionPane.showMessageDialog(contentPane, "Coefficient Examen incorrect!", "Attention", JOptionPane.WARNING_MESSAGE);
	} else if (ds < 0 || ds > 1) {
	    JOptionPane.showMessageDialog(contentPane, "Coefficient DS incorrect!", "Attention", JOptionPane.WARNING_MESSAGE);
	}else if ((ds+ex+tp)!=1) {
	    JOptionPane.showMessageDialog(contentPane, "La somme des coefficients doit faire 1!", "Attention", JOptionPane.WARNING_MESSAGE);
	} else if (tp < 0 || tp > 1) {
	    JOptionPane.showMessageDialog(contentPane, "Coefficient TP incorrect!", "Attention", JOptionPane.WARNING_MESSAGE);
	} else if (mat < 0)
	    JOptionPane.showMessageDialog(contentPane, "Coefficient Matiere incorrect!", "Attention", JOptionPane.WARNING_MESSAGE);
	else {
	    int id = launcher.addMatiere(nom, ds, tp, ex, mat);
	    tModel.addRow(new Object[] { id, nom, ds, tp, ex ,mat});
	    nomField.setText("");
	    dsField.setText("");
	    tpField.setText("");
	    exField.setText("");
	    matField.setText("");
	}
    }

}