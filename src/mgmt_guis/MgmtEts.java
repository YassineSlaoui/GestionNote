package mgmt_guis;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import org.jdesktop.swingx.JXTable;
import org.jdesktop.swingx.autocomplete.ComboBoxCellEditor;
import auth_gui.AuthGUI;
import gestion.Admin;
import gestion.Classe;
import gestion.Etudiant;
import gestion.Utilisateur;
import launcher.launcher;
import java.awt.*;
import java.awt.event.*;

@SuppressWarnings("serial")
public class MgmtEts extends JFrame {

    public MgmtEts() {
	setTitle("Gestion des Etudiants");
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
	String columnNames[] = { "Id", "Nom", "#CIN", "Classe", "Nom d'utilisateur", "Mot de Passe" };
	DefaultTableModel tModel = new DefaultTableModel(columnNames, 0);

	JXTable table = new JXTable(tModel) {
	    @Override
	    public boolean isCellEditable(int row, int column) {
		if (column == 0)
		    return false;
		return true;
	    }
	};

	JComboBox<Classe> clBox = new JComboBox<>();
	for (Classe c : launcher.classes) {
	    clBox.addItem(c);
	}

	table.getColumn(3).setCellEditor(new ComboBoxCellEditor(clBox));

	for (Utilisateur u : launcher.users) {
	    if (u instanceof Etudiant) {
		Etudiant et = (Etudiant) u;
		tModel.addRow(new Object[] { et.getId(), et.getName(), et.getCin(), launcher.findClasse(et.getClId()), et.getLogin(), et.getMotDePasse() });
	    }
	}
	table.getTableHeader().setReorderingAllowed(false);
	table.packAll();

	tModel.addTableModelListener(new TableModelListener() {

	    @Override
	    public void tableChanged(TableModelEvent e) {
		if (e.getType() == TableModelEvent.UPDATE) {
		    int col = e.getColumn();
		    int row = e.getFirstRow();
		    int id = (int) table.getValueAt(row, 0);
		    String nom = table.getValueAt(row, 1).toString().trim();
		    String cin = table.getValueAt(row, 2).toString().trim();
		    Classe cl = (Classe) table.getValueAt(row, 3);
		    String username = table.getValueAt(row, 4).toString().trim();
		    String pwd = table.getValueAt(row, 5).toString().trim();
		    Etudiant et = launcher.findEtu((int) table.getValueAt(row, 0));
		    if (nom.equals("")) {
			JOptionPane.showMessageDialog(contentPane, "Le nom ne peut pas etre vide!", "Attention", JOptionPane.WARNING_MESSAGE);
			table.setValueAt(et.getName(), row, col);
		    } else if (username.equals("")) {
			JOptionPane.showMessageDialog(contentPane, "Le nom d'utilisateur ne peut pas etre vide!", "Attention", JOptionPane.WARNING_MESSAGE);
			table.setValueAt(et.getLogin(), row, col);
		    } else if (pwd.equals("")) {
			JOptionPane.showMessageDialog(contentPane, "Le mot de passe ne peut pas etre vide!", "Attention", JOptionPane.WARNING_MESSAGE);
			table.setValueAt(et.getMotDePasse(), row, col);
		    } else if(!launcher.updEtu(id, nom, cin, cl.getId(), username, pwd)) {
			JOptionPane.showMessageDialog(contentPane, "Le nouveau nom d'utilisateur est dupliqué!", "Attention", JOptionPane.WARNING_MESSAGE);
			table.setValueAt(et.getLogin(), row, col);
		    }
		}

	    }
	});

	JScrollPane scrollPane = new JScrollPane(table);
	table.setFillsViewportHeight(true);
	centerPanel.add(scrollPane);
	
	JPanel southPanel = new JPanel();
	contentPane.add(southPanel,BorderLayout.SOUTH);
	JButton add = new JButton("Ajouter");
	JButton del = new JButton("Supprimer");
	southPanel.add(add);
	southPanel.add(del);
	
	add.addActionListener(new ActionListener() {
	    
	    @Override
	    public void actionPerformed(ActionEvent e) {
		AjoutEt gui = new AjoutEt(tModel);
		gui.setVisible(true);

	    }
	});
	
	del.addActionListener(new ActionListener() {
	    
	    @Override
	    public void actionPerformed(ActionEvent e) {
		int rows[] = table.getSelectedRows();
		if(rows.length==0) {
		    JOptionPane.showMessageDialog(centerPanel, "Veuillez selectioner les Etudiants a supprimer.", "Information", JOptionPane.INFORMATION_MESSAGE);
		}else {
		    for(int i=rows.length-1;i>=0;i--) {
			launcher.delEtu((int)tModel.getValueAt(rows[i], 0));
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
