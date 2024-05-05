package mgmt_guis;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import org.jdesktop.swingx.JXTable;
import org.jdesktop.swingx.autocomplete.ComboBoxCellEditor;

import auth_gui.AuthGUI;
import gestion.Admin;
import gestion.Classe;
import gestion.Enseignant;
import gestion.Utilisateur;
import launcher.launcher;
import model.ClasseMatiere;

@SuppressWarnings("serial")
public class MgmtClasses extends JFrame {
    public MgmtClasses() {
	setTitle("Gestion des Classes");
	setDefaultCloseOperation(DISPOSE_ON_CLOSE);
	JPanel contentPane = new JPanel();
	getContentPane().add(contentPane);
	contentPane.setBorder(new EmptyBorder(new Insets(5, 10, 5, 10)));
	contentPane.setLayout(new BorderLayout(10,10));

	Admin adm = (Admin) AuthGUI.activeUser;

	JPanel northPanel = new JPanel();
	contentPane.add(northPanel, BorderLayout.NORTH);
	JLabel headerLabel = new JLabel("Connecté en tant que: " + adm.getName());
	headerLabel.setFont(new Font("Times new roman", Font.BOLD, 24));
	northPanel.add(headerLabel);

	JPanel westPanel = new JPanel(new BorderLayout());
	contentPane.add(westPanel, BorderLayout.WEST);
	String clColumnNames[] = { "Id", "Nom" };
	DefaultTableModel clTModel = new DefaultTableModel(clColumnNames, 0);

	JXTable clTable = new JXTable(clTModel) {
	    @Override
	    public boolean isCellEditable(int row, int column) {
		if (column == 0)
		    return false;
		return true;
	    }
	};

	for (Classe cl : launcher.classes)
	    clTModel.addRow(new Object[] { cl.getId(), cl.getNom() });

	clTable.getTableHeader().setReorderingAllowed(false);
	clTable.packAll();

	clTModel.addTableModelListener(new TableModelListener() {

	    @Override
	    public void tableChanged(TableModelEvent e) {
		if (e.getType() == TableModelEvent.UPDATE) {
		    int col = e.getColumn();
		    int row = e.getFirstRow();
		    int id = (int) clTable.getValueAt(row, 0);
		    String nom = clTable.getValueAt(row, 1).toString().trim();
		    Classe c = launcher.findClasse(id);
		    if (nom.equals("")) {
			JOptionPane.showMessageDialog(contentPane, "Le nom ne peut pas etre vide!", "Attention", JOptionPane.WARNING_MESSAGE);
			clTable.setValueAt(c.getNom(), row, col);
		    } else
			launcher.updClasse(id, nom);
		}

	    }
	});

	JScrollPane clScrollPane = new JScrollPane(clTable);
	clTable.setFillsViewportHeight(true);
	westPanel.add(clScrollPane, BorderLayout.CENTER);

	JPanel clSouthPanel = new JPanel();
	westPanel.add(clSouthPanel, BorderLayout.SOUTH);
	JButton clAdd = new JButton("Ajouter");
	JButton clDel = new JButton("Supprimer");
	clSouthPanel.add(clAdd);
	clSouthPanel.add(clDel);

	clAdd.addActionListener(new ActionListener() {

	    @Override
	    public void actionPerformed(ActionEvent e) {
		AjoutClasse gui = new AjoutClasse(clTModel);
		gui.setVisible(true);
	    }
	});

	clDel.addActionListener(new ActionListener() {

	    @Override
	    public void actionPerformed(ActionEvent e) {
		int rows[] = clTable.getSelectedRows();
		if (rows.length == 0) {
		    JOptionPane.showMessageDialog(westPanel, "Veuillez selectioner les Classes a supprimer.", "Information", JOptionPane.INFORMATION_MESSAGE);
		} else {
		    for (int i = rows.length - 1; i >= 0; i--) {
			launcher.delClasse((int) clTModel.getValueAt(rows[i], 0));
			clTModel.removeRow(rows[i]);
		    }
		}
	    }
	});
	// -----------------------------------------------------------//
	JPanel centerPanel = new JPanel(new BorderLayout());
	JButton cmAdd = new JButton("Ajouter");
	JButton cmDel = new JButton("Supprimer");
	cmAdd.setEnabled(false);
	cmDel.setEnabled(false);
	contentPane.add(centerPanel, BorderLayout.CENTER);
	String cmColumnNames[] = { "Id", "Matiere", "Enseignée par", "Semestre" };
	DefaultTableModel cmTModel = new DefaultTableModel(cmColumnNames, 0);

	JXTable cmTable = new JXTable(cmTModel) {
	    @Override
	    public boolean isCellEditable(int row, int column) {
		if (column == 2)
		    return true;
		return false;
	    }
	};

	JComboBox<Enseignant> ensBox = new JComboBox<>();
	for (Utilisateur u : launcher.users)
	    if (u instanceof Enseignant)
		ensBox.addItem((Enseignant) u);

	cmTable.getColumn(2).setCellEditor(new ComboBoxCellEditor(ensBox));

	clTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {

	    @Override
	    public void valueChanged(ListSelectionEvent e) {
		if (!e.getValueIsAdjusting()) {
		    cmAdd.setEnabled(false);
		    cmDel.setEnabled(false);
		    for (int i = cmTModel.getRowCount() - 1; i >= 0; i--)
			cmTModel.removeRow(i);
		    if (clTable.getSelectedRowCount() == 1) {
			cmAdd.setEnabled(true);
			cmDel.setEnabled(true);
			int idCl = (int) clTable.getValueAt(clTable.getSelectedRow(), 0);
			for (ClasseMatiere cm : launcher.classesMatieres) {
			    if (cm.classe.getId() == idCl) {
				cmTModel.addRow(new Object[] { cm.idCM, cm.matiere, cm.ens, cm.semestre });
			    }
			}
			cmTable.packAll();
		    }
		}

	    }
	});

	cmTable.getTableHeader().setReorderingAllowed(false);

	cmTModel.addTableModelListener(new TableModelListener() {

	    @Override
	    public void tableChanged(TableModelEvent e) {
		if (e.getType() == TableModelEvent.UPDATE) {
		    int row = e.getFirstRow();
		    int id = (int) cmTable.getValueAt(row, 0);
		    Enseignant ens = (Enseignant) cmTable.getValueAt(row, 2);
		    launcher.updEnsCM(id, ens.getId());
		}

	    }
	});

	JScrollPane cmScrollPane = new JScrollPane(cmTable);
	cmTable.setFillsViewportHeight(true);
	centerPanel.add(cmScrollPane, BorderLayout.CENTER);

	JPanel cmSouthPanel = new JPanel();
	centerPanel.add(cmSouthPanel, BorderLayout.SOUTH);

	cmSouthPanel.add(cmAdd);
	cmSouthPanel.add(cmDel);

	cmAdd.addActionListener(new ActionListener() {

	    @Override
	    public void actionPerformed(ActionEvent e) {
		AjoutCM gui = new AjoutCM(cmTModel, launcher.findClasse((int) clTable.getValueAt(clTable.getSelectedRow(), 0)));
		gui.setVisible(true);
	    }
	});

	cmDel.addActionListener(new ActionListener() {

	    @Override
	    public void actionPerformed(ActionEvent e) {
		int rows[] = cmTable.getSelectedRows();
		if (rows.length == 0) {
		    JOptionPane.showMessageDialog(cmTable, "Veuillez selectioner les Matieres a supprimer de cette classe.", "Information", JOptionPane.INFORMATION_MESSAGE);
		} else {
		    for (int i = rows.length - 1; i >= 0; i--) {
			launcher.delCM((int) cmTModel.getValueAt(rows[i], 0));
			cmTModel.removeRow(rows[i]);
		    }
		}
	    }
	});

	pack();
	contentPane.grabFocus();
	setResizable(false);
	setLocationRelativeTo(null);
    }
}
