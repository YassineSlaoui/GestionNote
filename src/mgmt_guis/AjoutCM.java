package mgmt_guis;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import gestion.Classe;
import gestion.Enseignant;
import gestion.Utilisateur;
import launcher.launcher;
import model.Matiere;
import java.awt.*;
import java.awt.event.*;

@SuppressWarnings("serial")
public class AjoutCM extends JDialog {

    

    public AjoutCM(DefaultTableModel tModel,Classe cl) {
	setModal(true);
	setTitle("Ajout d'Enseignants");
	setDefaultCloseOperation(DISPOSE_ON_CLOSE);
	JPanel contentPane = new JPanel();
	getContentPane().add(contentPane);
	contentPane.setBorder(new EmptyBorder(new Insets(5, 10, 5, 10)));
	contentPane.setLayout(new BorderLayout(0, 0));
	contentPane.setLayout(new BorderLayout());
	
	JLabel northLabel = new JLabel("Selectionner les informations suivantes:");
	northLabel.setFont(new Font("Times New Roman", Font.BOLD, 18));
	contentPane.add(northLabel, BorderLayout.NORTH);
	northLabel.setBorder(new EmptyBorder(new Insets(0, 0, 10, 0)));

	JPanel centerPanel = new JPanel(new GridLayout(3,1));
	contentPane.add(centerPanel,BorderLayout.CENTER);
	
	JComboBox<Enseignant> ensBox = new JComboBox<Enseignant>();
	ensBox.setBorder(new TitledBorder("Enseignant"));
	for (Utilisateur u : launcher.users)
		if (u instanceof Enseignant)
			ensBox.addItem((Enseignant) u);
	JPanel ensPanel = new JPanel();
	ensPanel.add(ensBox);
	centerPanel.add(ensPanel);

	JComboBox<Matiere> matBox = new JComboBox<Matiere>();
	matBox.setBorder(new TitledBorder("Matiere"));
	for (Matiere m : launcher.matieres)
		matBox.addItem(m);
	JPanel matPanel = new JPanel();
	matPanel.add(matBox);
	centerPanel.add(matPanel);

	JComboBox<Integer> semBox = new JComboBox<Integer>();
	semBox.setBorder(new TitledBorder("Semestre"));
	semBox.addItem(1);
	semBox.addItem(2);
	JPanel semPanel = new JPanel();
	semPanel.add(semBox);
	centerPanel.add(semPanel);
	
	JPanel southPanel = new JPanel();
	contentPane.add(southPanel, BorderLayout.SOUTH);
	JButton submit = new JButton("Soumettre");
	southPanel.add(submit);
	submit.addActionListener(new ActionListener() {
	    
	    @Override
	    public void actionPerformed(ActionEvent e) {
		Matiere mat = (Matiere) matBox.getSelectedItem();
		Enseignant ens = (Enseignant) ensBox.getSelectedItem();
		int sem = (int) semBox.getSelectedItem();
		int id = launcher.addCM(cl.getId(), mat.getId(), ens.getId(), sem);
		if(id==-1)
			JOptionPane.showMessageDialog(contentPane, "EXISTE DEJA!", "Attention", JOptionPane.WARNING_MESSAGE);
		else
			tModel.addRow(new Object[]{id, mat, ens, sem});
	    }
	});

	pack();
	contentPane.grabFocus();
	setResizable(false);
	setLocationRelativeTo(null);
    }
}
