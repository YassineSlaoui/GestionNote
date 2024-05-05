package enseignant;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import org.jdesktop.swingx.JXTable;
import auth_gui.AuthGUI;
import gestion.Classe;
import gestion.Enseignant;
import gestion.Etudiant;
import gestion.Utilisateur;
import launcher.launcher;
import model.ClasseMatiere;
import model.Matiere;
import model.NoteMatiere;

@SuppressWarnings({ "rawtypes", "serial" })
public class EnsNotesGUI extends JFrame {
    public EnsNotesGUI() {
	setTitle("Saisie des notes");
	JPanel contentPane = new JPanel();
	setDefaultCloseOperation(DISPOSE_ON_CLOSE);
	getContentPane().add(contentPane);
	contentPane.setBorder(new EmptyBorder(new Insets(5, 10, 5, 10)));
	contentPane.setLayout(new BorderLayout());

	Enseignant ens = (Enseignant) AuthGUI.activeUser;

	JPanel northPanel = new JPanel(new GridLayout(2, 1));
	contentPane.add(northPanel, BorderLayout.NORTH);
	JLabel headerLabel = new JLabel("Connecté en tant que: " + ens.getName());
	headerLabel.setHorizontalAlignment(SwingConstants.CENTER);
	headerLabel.setFont(new Font("Times new roman", Font.BOLD, 24));
	northPanel.add(headerLabel);

	JPanel centerPanel = new JPanel();
	contentPane.add(centerPanel, BorderLayout.CENTER);
	JPanel chPanel = new JPanel();
	northPanel.add(chPanel);
	JComboBox<Classe> clBox = new JComboBox<>();
	JComboBox<Matiere> matBox = new JComboBox<>();
	JComboBox<Integer> semBox = new JComboBox<>();
	clBox.setBorder(new TitledBorder("Classe:"));
	matBox.setBorder(new TitledBorder("Matiere"));
	semBox.setBorder(new TitledBorder("Semestre:"));
	chPanel.add(clBox);
	chPanel.add(matBox);
	chPanel.add(semBox);
	for (ClasseMatiere cm : launcher.classesMatieres) {
	    if (cm.ens.equals(ens)) {
		if (((DefaultComboBoxModel) clBox.getModel()).getIndexOf(cm.classe) < 0)
		    clBox.addItem(cm.classe);
	    }
	}

	String columnNames[] = { "Id", "Etudiant", "Note DS", "Note TP", "Note Examen" };
	DefaultTableModel tModel = new DefaultTableModel(columnNames, 0);
	JXTable table = new JXTable(tModel) {
	    @Override
	    public Class<?> getColumnClass(int column) {
		if (column > 1)
		    return Double.class;
	        return super.getColumnClass(column);
	    }
	    @Override
	    public boolean isCellEditable(int row, int column) {
		if (column > 1)
		    if (getValueAt(row, column)!=null && Double.valueOf(getValueAt(row, column).toString()) == 0.0)
			return true;
		return false;
		
	    }
	};
	
	clBox.addActionListener(new ActionListener() {
	    
	    @Override
	    public void actionPerformed(ActionEvent e) {
		 for (int i = tModel.getRowCount() - 1; i >= 0; i--)
			tModel.removeRow(i);
		    matBox.removeAllItems();
		    Classe selClasse = (Classe) clBox.getSelectedItem();
		    for (ClasseMatiere cm : launcher.classesMatieres)
			if (cm.ens.equals(ens) && cm.classe.equals(selClasse)) {
			    if (((DefaultComboBoxModel) matBox.getModel()).getIndexOf(cm.matiere) < 0)
				matBox.addItem(cm.matiere);
			}
		
	    }
	});
	
	matBox.addItemListener(new ItemListener() {

	    @Override
	    public void itemStateChanged(ItemEvent e) {
		if (e.getStateChange() == ItemEvent.SELECTED) {
		    for (int i = tModel.getRowCount() - 1; i >= 0; i--)
			tModel.removeRow(i);
		    semBox.removeAllItems();
		    Classe selClasse = (Classe) clBox.getSelectedItem();
		    Matiere selMatiere = (Matiere) matBox.getSelectedItem();
		    for (ClasseMatiere cm : launcher.classesMatieres)
			if (cm.ens.equals(ens) && cm.classe.equals(selClasse) && cm.matiere.equals(selMatiere))
			    semBox.addItem(cm.semestre);
		}
	    }
	});

	semBox.addItemListener(new ItemListener() {

	    @Override
	    public void itemStateChanged(ItemEvent e) {
		if (e.getStateChange() == ItemEvent.SELECTED) {
		    for (int i = tModel.getRowCount() - 1; i >= 0; i--)
			tModel.removeRow(i);
		    Classe selClasse = (Classe) clBox.getSelectedItem();
		    Matiere selMatiere = (Matiere) matBox.getSelectedItem();
		    int sem = (int) semBox.getSelectedItem();
		    for (Utilisateur u : launcher.users)
			if (u instanceof Etudiant) {
			    Etudiant et = (Etudiant) u;
			    if (et.getClId() == selClasse.getId()) {
				if (sem == 1) {
				    for (NoteMatiere nm : et.getNotesS1())
					if (nm.getMatiere().equals(selMatiere))
					    tModel.addRow(new Object[] { nm.getId(), et.getName(), nm.getNotes().getDs(), nm.getNotes().getTp(), nm.getNotes().getExam() });
				} else {
				    for (NoteMatiere nm : et.getNotesS2())
					if (nm.getMatiere().equals(selMatiere))
					    tModel.addRow(new Object[] { nm.getId(), et.getName(), nm.getNotes().getDs(), nm.getNotes().getTp(), nm.getNotes().getExam() });
				}
			    }
			}
		}
	    }
	});
	
	tModel.addTableModelListener(new TableModelListener() {
	    
	    @Override
	    public void tableChanged(TableModelEvent e) {
		if(e.getType()==TableModelEvent.UPDATE) {
		    int col = e.getColumn();
		    int row = e.getFirstRow();
		    int id = (int) table.getValueAt(row, 0);
		    Double noteEx = -1.;
		    Double noteDs = -1.;
		    Double noteTp = -1.;
		    try {
			noteDs = Double.valueOf(table.getValueAt(row, 2).toString());
			noteTp = Double.valueOf(table.getValueAt(row, 3).toString());
			noteEx = Double.valueOf(table.getValueAt(row, 4).toString());
		    } catch (Exception e2) {
			e2.printStackTrace();
		    }
		    if (noteEx < 0 || noteEx > 20) {
			JOptionPane.showMessageDialog(contentPane, "Les Notes doivent etre comprises entre 0 et 20!", "Attention", JOptionPane.WARNING_MESSAGE);
			table.setValueAt(launcher.findNM(id).getNotes().getExam(), row, col);
		    } else if (noteDs < 0 || noteDs > 20) {
			JOptionPane.showMessageDialog(contentPane, "Les Notes doivent etre comprises entre 0 et 20!", "Attention", JOptionPane.WARNING_MESSAGE);
			table.setValueAt(launcher.findNM(id).getNotes().getDs(), row, col);
		    } else if (noteTp < 0 || noteTp > 20) {
			JOptionPane.showMessageDialog(contentPane, "Les Notes doivent etre comprises entre 0 et 20!", "Attention", JOptionPane.WARNING_MESSAGE);
			table.setValueAt(launcher.findNM(id).getNotes().getTp(), row, col);
		    } else
			launcher.updNM(id, noteDs, noteTp, noteEx);
		}
	    }
	});
	
	table.getTableHeader().setReorderingAllowed(false);
	JScrollPane scrollPane = new JScrollPane(table);
	centerPanel.add(scrollPane);

	pack();
	contentPane.grabFocus();
	setResizable(false);
	setLocationRelativeTo(null);
    }
}
