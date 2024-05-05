package etudiant;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import org.jdesktop.swingx.JXTable;
import auth_gui.AuthGUI;
import gestion.Enseignant;
import gestion.Etudiant;
import launcher.launcher;
import model.ClasseMatiere;
import model.NoteMatiere;
import java.awt.*;
import java.awt.event.*;

@SuppressWarnings({ "serial", "unused" })
public class EtNotesGUI extends JFrame {

    public EtNotesGUI() {
	setTitle("Consultation des notes");
	JPanel contentPane = new JPanel();
	setDefaultCloseOperation(DISPOSE_ON_CLOSE);
	getContentPane().add(contentPane);
	contentPane.setBorder(new EmptyBorder(new Insets(5, 10, 5, 10)));
	contentPane.setLayout(new BorderLayout());

	Etudiant et = (Etudiant) AuthGUI.activeUser;

	JPanel northPanel = new JPanel(new GridLayout(2, 1));
	contentPane.add(northPanel, BorderLayout.NORTH);
	JLabel headerLabel = new JLabel("Connecté en tant que: " + et.getName());
	headerLabel.setHorizontalAlignment(SwingConstants.CENTER);
	headerLabel.setFont(new Font("Times new roman", Font.BOLD, 24));
	northPanel.add(headerLabel);

	JPanel centerPanel = new JPanel();
	contentPane.add(centerPanel, BorderLayout.CENTER);
	JPanel semPanel = new JPanel();
	northPanel.add(semPanel);
	JRadioButton semOne = new JRadioButton("Semestre 1");
	JRadioButton semTwo = new JRadioButton("Semestre 2");
	semPanel.add(semOne);
	semPanel.add(semTwo);
	ButtonGroup semGroup = new ButtonGroup();
	semGroup.add(semOne);
	semGroup.add(semTwo);

	String columnNames[] = { "Matiere", "Coefficient", "Enseignée par", "Note DS", "Note TP", "Note Examen" };
	DefaultTableModel tModel = new DefaultTableModel(columnNames, 0);
	JXTable table = new JXTable(tModel) {
	    @Override
	    public boolean isCellEditable(int row, int column) {
		return false;
	    }
	};
	table.getTableHeader().setReorderingAllowed(false);
	JScrollPane scrollPane = new JScrollPane(table);
	centerPanel.add(scrollPane);

	semOne.addActionListener(new ActionListener() {

	    @Override
	    public void actionPerformed(ActionEvent e) {
		for (int i = tModel.getRowCount() - 1; i >= 0; i--)
		    tModel.removeRow(i);
		for (NoteMatiere nm : et.getNotesS1()) {
		    Enseignant ens = null;
		    for (ClasseMatiere cm : launcher.classesMatieres)
			if (cm.matiere.equals(nm.getMatiere()) && et.getClId() == cm.classe.getId()) {
			    ens = cm.ens;
			    break;
			}
		    tModel.addRow(new Object[] { nm.getMatiere(), nm.getMatiere().getCoefMatiere(), ens, nm.getNotes().getDs(), nm.getNotes().getTp(), nm.getNotes().getExam() });
		}
		table.packAll();
	    }
	});

	semTwo.addActionListener(new ActionListener() {

	    @Override
	    public void actionPerformed(ActionEvent e) {
		for (int i = tModel.getRowCount() - 1; i >= 0; i--)
		    tModel.removeRow(i);
		for (NoteMatiere nm : et.getNotesS2()) {
		    Enseignant ens = null;
		    for (ClasseMatiere cm : launcher.classesMatieres)
			if (cm.matiere.equals(nm.getMatiere()) && et.getClId() == cm.classe.getId()) {
			    ens = cm.ens;
			    break;
			}
		    tModel.addRow(new Object[] { nm.getMatiere(), nm.getMatiere().getCoefMatiere(), ens, nm.getNotes().getDs(), nm.getNotes().getTp(), nm.getNotes().getExam() });
		}
		table.packAll();
	    }
	});

	pack();
	contentPane.grabFocus();
	setResizable(false);
	setLocationRelativeTo(null);
    }

    public static void main(String[] args) {
//	EtNotesGUI gui = new EtNotesGUI();
//	gui.setVisible(true);
    }
}
