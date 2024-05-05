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
import gestion.Utilisateur;
import launcher.launcher;

@SuppressWarnings("serial")
public class MgmtAdmins extends JFrame{
    public MgmtAdmins() {
	setTitle("Gestion des Administrateurs");
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
	String columnNames[] = { "Id", "Nom", "#CIN", "Super", "Nom d'utilisateur", "Mot de Passe" };
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
	        if(column==3)
	            return Boolean.class;
	        return super.getColumnClass(column);
	    }
	};

	for (Utilisateur u : launcher.users) {
	    if (u instanceof Admin) {
		Admin ad = (Admin) u;
		tModel.addRow(new Object[] { ad.getId(), ad.getName(), ad.getCin(), ad.isSuperAdmin(), ad.getLogin(), ad.getMotDePasse() });
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
		    Boolean sup = (Boolean)table.getValueAt(row, 3);
		    String username = table.getValueAt(row, 4).toString().trim();
		    String pwd = table.getValueAt(row, 5).toString().trim();
		    Admin ad = launcher.findAdm((int) table.getValueAt(row, 0));
		    if (nom.equals("")) {
			JOptionPane.showMessageDialog(contentPane, "Le nom ne peut pas etre vide!", "Attention", JOptionPane.WARNING_MESSAGE);
			table.setValueAt(ad.getName(), row, col);
		    } else if (username.equals("")) {
			JOptionPane.showMessageDialog(contentPane, "Le nom d'utilisateur ne peut pas etre vide!!", "Attention", JOptionPane.WARNING_MESSAGE);
			table.setValueAt(ad.getLogin(), row, col);
		    } else if (pwd.equals("")) {
			JOptionPane.showMessageDialog(contentPane, "Le mot de passe ne peut pas etre vide!", "Attention", JOptionPane.WARNING_MESSAGE);
			table.setValueAt(ad.getMotDePasse(), row, col);
		    } else if(!launcher.updAdmin(id, nom, cin, sup, username, pwd)) {
			JOptionPane.showMessageDialog(contentPane, "Le nouveau nom d'utilisateur est dupliqué!", "Attention", JOptionPane.WARNING_MESSAGE);
			table.setValueAt(ad.getLogin(), row, col);
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
		AjoutAdm gui = new AjoutAdm(tModel);
		gui.setVisible(true);
	    }
	});
	
	del.addActionListener(new ActionListener() {
	    
	    @Override
	    public void actionPerformed(ActionEvent e) {
		int rows[] = table.getSelectedRows();
		if(rows.length==0) {
		    JOptionPane.showMessageDialog(centerPanel, "Veuillez selectioner les Admins a supprimer.", "Information", JOptionPane.INFORMATION_MESSAGE);
		}else {
		    for(int i=rows.length-1;i>=0;i--) {
			launcher.delAdmin((int)tModel.getValueAt(rows[i], 0));
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
