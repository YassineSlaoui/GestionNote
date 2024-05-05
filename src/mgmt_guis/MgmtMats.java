package mgmt_guis;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import org.jdesktop.swingx.JXTable;
import auth_gui.AuthGUI;
import gestion.Admin;
import launcher.launcher;
import model.Matiere;

@SuppressWarnings("serial")
public class MgmtMats extends JFrame {

    public MgmtMats() {
	setTitle("Gestion des Matieres");
	setDefaultCloseOperation(DISPOSE_ON_CLOSE);
	JPanel contentPane = new JPanel();
	getContentPane().add(contentPane);
	contentPane.setBorder(new EmptyBorder(new Insets(5, 10, 5, 10)));
	contentPane.setLayout(new BorderLayout());

	Admin adm = (Admin) AuthGUI.activeUser;

	JPanel northPanel = new JPanel();
	contentPane.add(northPanel, BorderLayout.NORTH);
	JLabel headerLabel = new JLabel("Connecté en tant que: " + adm.getName());
	headerLabel.setFont(new Font("Times new roman", Font.BOLD, 24));
	northPanel.add(headerLabel);

	JPanel centerPanel = new JPanel();
	contentPane.add(centerPanel, BorderLayout.CENTER);
	String columnNames[] = { "Id", "Nom", "Coef DS", "Coef TP", "Coef EX", "Coef Matiere" };
	DefaultTableModel tModel = new DefaultTableModel(columnNames, 0);

	JXTable table = new JXTable(tModel) {
	    @Override
	    public boolean isCellEditable(int row, int column) {
		if (column == 0)
		    return false;
		return true;
	    }

	    @Override
	    public Class<?> getColumnClass(int column) {
		if (column > 1)
		    return Double.class;
		return super.getColumnClass(column);
	    }
	};

	for (Matiere m : launcher.matieres)
	    tModel.addRow(new Object[] { m.getId(), m.getNomMatiere(), m.getCoefDs(), m.getCoefTp(), m.getCoefExam(), m.getCoefMatiere() });

	table.getTableHeader().setReorderingAllowed(false);
	table.packAll();

	tModel.addTableModelListener(new TableModelListener() {

	    @Override
	    public void tableChanged(TableModelEvent e) {
		if (e.getType() == TableModelEvent.UPDATE) {
		    int col = e.getColumn();
		    int row = e.getFirstRow();
		    int id = (int) table.getValueAt(row, 0);
		    Matiere m = launcher.findMatiere(id);
		    String nom = table.getValueAt(row, 1).toString();
		    Double ds = -1.;
		    Double tp = -1.;
		    Double ex = -1.;
		    Double mat = -1.;
		    try {
			ds = Double.valueOf(table.getValueAt(row, 2).toString());
			tp = Double.valueOf(table.getValueAt(row, 3).toString());
			ex = Double.valueOf(table.getValueAt(row, 4).toString());
			mat = Double.valueOf(table.getValueAt(row, 5).toString());
		    } catch (Exception e2) {
		    }
		    if (nom.equals("")) {
			JOptionPane.showMessageDialog(contentPane, "Le nom ne peut pas etre vide!", "Attention", JOptionPane.WARNING_MESSAGE);
			table.setValueAt(m.getNomMatiere(), row, col);
		    } else if (ex < 0 || ex > 1) {
			JOptionPane.showMessageDialog(contentPane, "Coefficient Examen incorrect!", "Attention", JOptionPane.WARNING_MESSAGE);
			table.setValueAt(m.getCoefExam(), row, col);
		    } else if (ds < 0 || ds > 1) {
			JOptionPane.showMessageDialog(contentPane, "Coefficient DS incorrect!", "Attention", JOptionPane.WARNING_MESSAGE);
			table.setValueAt(m.getCoefDs(), row, col);
		    } else if (tp < 0 || tp > 1) {
			JOptionPane.showMessageDialog(contentPane, "Coefficient TP incorrect!", "Attention", JOptionPane.WARNING_MESSAGE);
			table.setValueAt(m.getCoefTp(), row, col);
		    } else if ((ds + ex + tp) != 1) {
			JOptionPane.showMessageDialog(contentPane, "La somme des coefficients doit faire 1!", "Attention", JOptionPane.WARNING_MESSAGE);
			switch (col) {
			case 2:
			    table.setValueAt(m.getCoefDs(), row, 2);
			case 3:
			    table.setValueAt(m.getCoefTp(), row, 3);
			case 4:
			    table.setValueAt(m.getCoefExam(), row, 4);			    
			}
		    } else if (mat < 0) {
			JOptionPane.showMessageDialog(contentPane, "Coefficient Matiere incorrect!", "Attention", JOptionPane.WARNING_MESSAGE);
			table.setValueAt(m.getCoefMatiere(), row, col);
		    } else {
			launcher.updMatiere(id, nom, ds, tp, ex, mat);
		    }
		}
	    }
	});

	JScrollPane scrollPane = new JScrollPane(table);
	table.setFillsViewportHeight(true);
	centerPanel.add(scrollPane);

	JPanel southPanel = new JPanel();
	contentPane.add(southPanel, BorderLayout.SOUTH);
	JButton add = new JButton("Ajouter");
	JButton del = new JButton("Supprimer");
	southPanel.add(add);
	southPanel.add(del);

	add.addActionListener(new ActionListener() {

	    @Override
	    public void actionPerformed(ActionEvent e) {
		AjoutMat gui = new AjoutMat(tModel);
		gui.setVisible(true);
	    }
	});

	del.addActionListener(new ActionListener() {

	    @Override
	    public void actionPerformed(ActionEvent e) {
		int rows[] = table.getSelectedRows();
		if (rows.length == 0) {
		    JOptionPane.showMessageDialog(centerPanel, "Veuillez selectioner les Enseignants a supprimer.", "Information", JOptionPane.INFORMATION_MESSAGE);
		} else {
		    for (int i = rows.length - 1; i >= 0; i--) {
			launcher.delMatiere((int) tModel.getValueAt(rows[i], 0));
			tModel.removeRow(rows[i]);
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
