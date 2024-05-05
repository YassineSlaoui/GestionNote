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
import gestion.Enseignant;
import gestion.Utilisateur;
import launcher.launcher;

@SuppressWarnings("serial")
public class MgmtEns extends JFrame{
    
    public MgmtEns() {
	setTitle("Gestion des Enseignants");
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
	String columnNames[] = { "Id", "Nom", "#CIN", "Nom d'utilisateur", "Mot de Passe" };
	DefaultTableModel tModel = new DefaultTableModel(columnNames, 0);

	JXTable table = new JXTable(tModel) {
	    @Override
	    public boolean isCellEditable(int row, int column) {
		if (column == 0)
		    return false;
		return true;
	    }
	};

	for (Utilisateur u : launcher.users) {
	    if (u instanceof Enseignant) {
		Enseignant ens = (Enseignant) u;
		tModel.addRow(new Object[] { ens.getId(), ens.getName(), ens.getCin(), ens.getLogin(), ens.getMotDePasse() });
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
		    String username = table.getValueAt(row, 3).toString().trim();
		    String pwd = table.getValueAt(row, 4).toString().trim();
		    Enseignant ens = launcher.findEns((int) table.getValueAt(row, 0));
		    if (nom.equals("")) {
			JOptionPane.showMessageDialog(contentPane, "Le nom ne peut pas etre vide!", "Attention", JOptionPane.WARNING_MESSAGE);
			table.setValueAt(ens.getName(), row, col);
		    } else if (username.equals("")) {
			JOptionPane.showMessageDialog(contentPane, "Le nom d'utilisateur ne peut pas etre vide!!", "Attention", JOptionPane.WARNING_MESSAGE);
			table.setValueAt(ens.getLogin(), row, col);
		    } else if (pwd.equals("")) {
			JOptionPane.showMessageDialog(contentPane, "Le mot de passe ne peut pas etre vide!", "Attention", JOptionPane.WARNING_MESSAGE);
			table.setValueAt(ens.getMotDePasse(), row, col);
		    } else
			launcher.updEns(id, nom, cin, username, pwd);
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
		AjoutEns gui = new AjoutEns(tModel);
		gui.setVisible(true);
	    }
	});
	
	del.addActionListener(new ActionListener() {
	    
	    @Override
	    public void actionPerformed(ActionEvent e) {
		int rows[] = table.getSelectedRows();
		if(rows.length==0) {
		    JOptionPane.showMessageDialog(centerPanel, "Veuillez selectioner les Enseignants a supprimer.", "Information", JOptionPane.INFORMATION_MESSAGE);
		}else {
		    for(int i=rows.length-1;i>=0;i--) {
			launcher.delEns((int)tModel.getValueAt(rows[i], 0));
			tModel.removeRow(rows[i]);
		    }
		}
	    }
	});
	
	pack();
	contentPane.grabFocus();
	setResizable(false);
	setLocationRelativeTo(null);    }
}
