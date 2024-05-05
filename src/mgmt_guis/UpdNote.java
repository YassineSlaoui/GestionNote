package mgmt_guis;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import gestion.*;
import launcher.launcher;
import model.*;

@SuppressWarnings("serial")
public class UpdNote extends JFrame {

    public UpdNote() {
	setTitle("Màj Note Etudiant");
	setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	Container contentPane = getContentPane();
	setSize(300, 480);
	contentPane.setLayout(new GridLayout(8, 1));

	JPanel clPanel = new JPanel();
	JLabel clLabel = new JLabel("Classe");
	JComboBox<Classe> clBox = new JComboBox<>();
	Classe selectClasse = new Classe(-1, "--- Selectionner Classe ---");
	clBox.addItem(selectClasse);
	for (Classe c : launcher.classes)
	    clBox.addItem(c);
	clPanel.add(clLabel);
	clPanel.add(clBox);
	contentPane.add(clPanel);

	JPanel etPanel = new JPanel();
	JLabel etLabel = new JLabel("Etudiant");
	JComboBox<Etudiant> etBox = new JComboBox<>();
	Etudiant selectEtudiant = new Etudiant(-1, null, "--- Selectionner Etudiant ---");
	etBox.addItem(selectEtudiant);
	etBox.setEnabled(false);
	clBox.addItemListener(new ItemListener() { // selecting a classe lets you pick a student
	    @Override
	    public void itemStateChanged(ItemEvent e1) {
		if (e1.getStateChange() == ItemEvent.SELECTED) {
		    etBox.removeAllItems();
		    etBox.addItem(selectEtudiant);
		    Classe selectedClasse = (Classe) clBox.getSelectedItem();
		    if (selectClasse.equals(selectedClasse))
			etBox.setEnabled(false);
		    else {
			for (Etudiant e : selectedClasse.listeEtudiant)
			    etBox.addItem(e);
			etBox.setEnabled(true);
		    }
		}
	    }
	});
	etPanel.add(etLabel);
	etPanel.add(etBox);
	contentPane.add(etPanel);

	JPanel semPanel = new JPanel();
	ButtonGroup semester = new ButtonGroup();
	JRadioButton sem1 = new JRadioButton("Semestre 1");
	JRadioButton sem2 = new JRadioButton("Semestre 2");
	sem1.setEnabled(false);
	sem2.setEnabled(false);
	clBox.addItemListener(new ItemListener() { // selecting a classe lets you select a semester only if you have chosen a
						   // student
	    @Override
	    public void itemStateChanged(ItemEvent e) {
		if (e.getStateChange() == ItemEvent.SELECTED) {
		    semester.clearSelection();
		    sem1.setEnabled(false);
		    sem2.setEnabled(false);
		    Etudiant selectedEtudiant = (Etudiant) etBox.getSelectedItem();
		    if (!selectEtudiant.equals(selectedEtudiant)) {
			sem1.setEnabled(true);
			sem2.setEnabled(true);
		    }
		}
	    }
	});
	etBox.addItemListener(new ItemListener() { // to choose a semester you have to have had selected a student
	    @Override
	    public void itemStateChanged(ItemEvent e) {
		if (e.getStateChange() == ItemEvent.SELECTED) {
		    Etudiant selectedEtudiant = (Etudiant) etBox.getSelectedItem();
		    if (!selectEtudiant.equals(selectedEtudiant)) {
			sem1.setEnabled(true);
			sem2.setEnabled(true);
		    } else {
			semester.clearSelection();
			sem1.setEnabled(false);
			sem2.setEnabled(false);
		    }
		}
	    }
	});
	semester.add(sem1);
	semester.add(sem2);
	semPanel.add(sem1);
	semPanel.add(sem2);
	contentPane.add(semPanel);

	JPanel matPanel = new JPanel();
	JLabel matLabel = new JLabel("Matiere");
	JComboBox<Matiere> matBox = new JComboBox<>();
	Matiere selectMatiere = new Matiere(-1, "--- Choisir Matiere ---", -1, -1, -1, -1);
	matBox.addItem(selectMatiere);
	matBox.setEnabled(false);
	sem1.addActionListener(new ActionListener() {

	    @Override
	    public void actionPerformed(ActionEvent e) {
		Classe selectedClasse = (Classe) clBox.getSelectedItem();
		for (Matiere m : selectedClasse.listeMatiereS1)
		    matBox.addItem(m);
		matBox.setEnabled(true);
	    }
	});
	sem2.addActionListener(new ActionListener() {

	    @Override
	    public void actionPerformed(ActionEvent e) {
		Classe selectedClasse = (Classe) clBox.getSelectedItem();
		for (Matiere m : selectedClasse.listeMatiereS2)
		    matBox.addItem(m);
		matBox.setEnabled(true);
	    }
	});
	sem1.addItemListener(new ItemListener() {

	    @Override
	    public void itemStateChanged(ItemEvent e) {
		if (e.getStateChange() == ItemEvent.DESELECTED) {
		    matBox.removeAllItems();
		    matBox.addItem(selectMatiere);
		    matBox.setEnabled(false);
		}
	    }
	});
	sem2.addItemListener(new ItemListener() {

	    @Override
	    public void itemStateChanged(ItemEvent e) {
		if (e.getStateChange() == ItemEvent.DESELECTED) {
		    matBox.removeAllItems();
		    matBox.addItem(selectMatiere);
		    matBox.setEnabled(false);
		}

	    }
	});
	matPanel.add(matLabel);
	matPanel.add(matBox);
	contentPane.add(matPanel);

	JPanel noteDS = new JPanel(new GridLayout(2, 1));
	JLabel dsLabel = new JLabel("Note DS");
	dsLabel.setHorizontalAlignment(SwingConstants.CENTER);
	JTextField dsField = new JTextField(10);
	JPanel dsfPanel = new JPanel();
	dsfPanel.add(dsField);
	noteDS.add(dsLabel);
	noteDS.add(dsfPanel);
	JPanel noteTP = new JPanel(new GridLayout(2, 1));
	JLabel tpLabel = new JLabel("Note TP");
	tpLabel.setHorizontalAlignment(SwingConstants.CENTER);
	JTextField tpField = new JTextField(10);
	JPanel tpfPanel = new JPanel();
	tpfPanel.add(tpField);
	noteTP.add(tpLabel);
	noteTP.add(tpfPanel);
	JPanel noteEx = new JPanel(new GridLayout(2, 1));
	JLabel exLabel = new JLabel("Note Examen");
	exLabel.setHorizontalAlignment(SwingConstants.CENTER);
	JTextField exField = new JTextField(10);
	JPanel exfPanel = new JPanel();
	exfPanel.add(exField);
	noteEx.add(exLabel);
	noteEx.add(exfPanel);
	contentPane.add(noteDS);
	contentPane.add(noteTP);
	contentPane.add(noteEx);
	dsLabel.setEnabled(false);
	dsField.setEnabled(false);
	tpLabel.setEnabled(false);
	tpField.setEnabled(false);
	exLabel.setEnabled(false);
	exField.setEnabled(false);
	matBox.addItemListener(new ItemListener() {

	    @Override
	    public void itemStateChanged(ItemEvent e) {
		if (e.getStateChange() == ItemEvent.SELECTED) {
		    Matiere selectedMatiere = (Matiere) matBox.getSelectedItem();
		    dsLabel.setEnabled(false);
		    dsField.setEnabled(false);
		    dsField.setText("");
		    tpLabel.setEnabled(false);
		    tpField.setEnabled(false);
		    tpField.setText("");
		    exLabel.setEnabled(false);
		    exField.setEnabled(false);
		    exField.setText("");
		    if (!selectedMatiere.equals(selectMatiere)) {
			NoteMatiere nm = null;
			Etudiant selectedEtudiant = (Etudiant) etBox.getSelectedItem();
			if (sem1.isSelected()) {
			    for (NoteMatiere rnm : selectedEtudiant.getNotesS1())
				if (rnm.getMatiere().equals(selectedMatiere)) {
				    nm = rnm;
				    break;
				}
			} else {
			    for (NoteMatiere rnm : selectedEtudiant.getNotesS2())
				if (rnm.getMatiere().equals(selectedMatiere)) {
				    nm = rnm;
				    break;
				}
			}
//			if (selectedMatiere.getCoefDs() > 0) {
//			    dsLabel.setEnabled(true);
//			    dsField.setEnabled(true);
//			    dsField.setText(String.valueOf(nm.getNotes().getDs()));
//			}
//			if (selectedMatiere.getCoefTp() > 0) {
//			    tpLabel.setEnabled(true);
//			    tpField.setEnabled(true);
//			    tpField.setText(String.valueOf(nm.getNotes().getTp()));
//			}
//			if (selectedMatiere.getCoefExam() > 0) {
//			    exLabel.setEnabled(true);
//			    exField.setEnabled(true);
//			    exField.setText(String.valueOf(nm.getNotes().getExam()));
//			}
			String t;
			if (selectedMatiere.getCoefDs() > 0) {
			    dsLabel.setEnabled(true);
			    dsField.setEnabled(true);
			    t = "N/A";
			    if (nm != null)
				t = String.valueOf(nm.getNotes().getDs());
			    dsField.setText(t);
			}
			if (selectedMatiere.getCoefTp() > 0) {
			    tpLabel.setEnabled(true);
			    tpField.setEnabled(true);
			    t = "N/A";
			    if (nm != null)
				t = String.valueOf(nm.getNotes().getTp());
			    tpField.setText(t);
			}
			if (selectedMatiere.getCoefExam() > 0) {
			    exLabel.setEnabled(true);
			    exField.setEnabled(true);
			    t = "N/A";
			    if (nm != null)
				t = String.valueOf(nm.getNotes().getExam());
			    exField.setText(t);
			}

		    }
		}
	    }
	});
	etBox.addItemListener(new ItemListener() { // if we change student we reset the componenets below its choice list

	    @Override
	    public void itemStateChanged(ItemEvent e) {
		if (e.getStateChange() == ItemEvent.SELECTED) {

		    dsLabel.setEnabled(false);
		    dsField.setEnabled(false);
		    dsField.setText("");
		    tpLabel.setEnabled(false);
		    tpField.setEnabled(false);
		    tpField.setText("");
		    exLabel.setEnabled(false);
		    exField.setEnabled(false);
		    exField.setText("");
		    semester.clearSelection();
		}
	    }
	});
	sem1.addItemListener(new ItemListener() {
	    @Override
	    public void itemStateChanged(ItemEvent e) {
		dsLabel.setEnabled(false);
		dsField.setEnabled(false);
		dsField.setText("");
		tpLabel.setEnabled(false);
		tpField.setEnabled(false);
		tpField.setText("");
		exLabel.setEnabled(false);
		exField.setEnabled(false);
		exField.setText("");
	    }
	});
	sem2.addItemListener(new ItemListener() {
	    @Override
	    public void itemStateChanged(ItemEvent e) {
		dsLabel.setEnabled(false);
		dsField.setEnabled(false);
		dsField.setText("");
		tpLabel.setEnabled(false);
		tpField.setEnabled(false);
		tpField.setText("");
		exLabel.setEnabled(false);
		exField.setEnabled(false);
		exField.setText("");
	    }
	});
	clBox.addItemListener(new ItemListener() { // same goes if we change classe

	    @Override
	    public void itemStateChanged(ItemEvent e) {
		dsLabel.setEnabled(false);
		dsField.setEnabled(false);
		dsField.setText("");
		tpLabel.setEnabled(false);
		tpField.setEnabled(false);
		tpField.setText("");
		exLabel.setEnabled(false);
		exField.setEnabled(false);
		exField.setText("");
		semester.clearSelection();
	    }
	});

	JPanel valPanel = new JPanel();
	JButton valider = new JButton("Valider");
	valider.setEnabled(false);
	dsField.addKeyListener(new KeyListener() {

	    @Override
	    public void keyTyped(KeyEvent e) {
		if (!valider.isEnabled())
		    valider.setEnabled(true);
	    }

	    @Override
	    public void keyPressed(KeyEvent e) {

	    }

	    @Override
	    public void keyReleased(KeyEvent e) {

	    }
	});
	tpField.addKeyListener(new KeyListener() {

	    @Override
	    public void keyTyped(KeyEvent e) {
		if (!valider.isEnabled())
		    valider.setEnabled(true);
	    }

	    @Override
	    public void keyPressed(KeyEvent e) {

	    }

	    @Override
	    public void keyReleased(KeyEvent e) {

	    }
	});
	exField.addKeyListener(new KeyListener() {

	    @Override
	    public void keyTyped(KeyEvent e) {
		if (!valider.isEnabled())
		    valider.setEnabled(true);
	    }

	    @Override
	    public void keyPressed(KeyEvent e) {

	    }

	    @Override
	    public void keyReleased(KeyEvent e) {

	    }
	});
	clBox.addItemListener(new ItemListener() {

	    @Override
	    public void itemStateChanged(ItemEvent e) {
		if (valider.isEnabled())
		    valider.setEnabled(false);
	    }
	});
	etBox.addItemListener(new ItemListener() {

	    @Override
	    public void itemStateChanged(ItemEvent e) {
		if (valider.isEnabled())
		    valider.setEnabled(false);
	    }
	});
	matBox.addItemListener(new ItemListener() {

	    @Override
	    public void itemStateChanged(ItemEvent e) {
		if (valider.isEnabled())
		    valider.setEnabled(false);
	    }
	});
	sem1.addItemListener(new ItemListener() {
	    @Override
	    public void itemStateChanged(ItemEvent e) {
		if (valider.isEnabled())
		    valider.setEnabled(false);
	    }
	});
	sem2.addItemListener(new ItemListener() {
	    @Override
	    public void itemStateChanged(ItemEvent e) {
		if (valider.isEnabled())
		    valider.setEnabled(false);
	    }
	});
	valider.addActionListener(new ActionListener() {
	    @Override
	    public void actionPerformed(ActionEvent e) {
		Boolean numeric = true;
		Boolean changesMade = false;
		Matiere selectedMatiere = (Matiere) matBox.getSelectedItem();
		Etudiant selectedEtudiant = (Etudiant) etBox.getSelectedItem();
		NoteMatiere nm = null;
		int sem = 1;
		if (sem1.isSelected()) {
		    for (NoteMatiere rnm : selectedEtudiant.getNotesS1())
			if (rnm.getMatiere().equals(selectedMatiere)) {
			    nm = rnm;
			    break;
			}
		} else {
		    sem = 2;
		    for (NoteMatiere rnm : selectedEtudiant.getNotesS2())
			if (rnm.getMatiere().equals(selectedMatiere)) {
			    nm = rnm;
			    break;
			}
		}
		double ds = 0;
		if (selectedMatiere.getCoefDs() > 0) {
		    try {
			ds = Double.valueOf(dsField.getText());
		    } catch (Exception e2) {
			numeric = false;
		    }
		    if (numeric && ds != nm.getNotes().getDs())
			changesMade = true;
		}
		double tp = 0;
		if (selectedMatiere.getCoefTp() > 0) {
		    try {
			tp = Double.valueOf(tpField.getText());
		    } catch (Exception e2) {
			numeric = false;
		    }
		    if (numeric && tp != nm.getNotes().getTp())
			changesMade = true;
		}
		double ex = 0;
		if (selectedMatiere.getCoefExam() > 0) {
		    try {
			ex = Double.valueOf(exField.getText());
		    } catch (Exception e2) {
			numeric = false;
		    }
		    if (numeric && ex != nm.getNotes().getExam())
			changesMade = true;
		}
		if (numeric && changesMade && 0<=ex && ex<=20 && 0<=ds && ds<=20 && 0<=tp && tp<=20 ) {
		    if (nm == null)
			launcher.addNM(selectedEtudiant.getId(), selectedMatiere.getId(), sem, ds, tp, ex);
		    else {
			launcher.updNM(nm.getId(), ds, tp, ex);
		    }
		    JOptionPane.showMessageDialog(contentPane, "Notes inscrites", getTitle(), JOptionPane.INFORMATION_MESSAGE);
		}else {
		    JOptionPane.showMessageDialog(contentPane, "Les Notes doivent avoir des valeurs numeriques et comprises entre 0 et 20!", getTitle(), JOptionPane.ERROR_MESSAGE);
		    dsField.setText("");
		    exField.setText("");
		    tpField.setText("");
		}
		valider.setEnabled(false);
	    }
	});
	valPanel.add(valider);
	contentPane.add(valPanel);

	setResizable(false);
//		pack();
	setLocationRelativeTo(null);
    }

    public static void main(String[] args) {
	UpdNote un = new UpdNote();
	un.setVisible(true);
    }

}
