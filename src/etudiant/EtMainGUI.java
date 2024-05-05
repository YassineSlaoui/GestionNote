package etudiant;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import auth_gui.AuthGUI;
import java.awt.event.*;
import gestion.Etudiant;

@SuppressWarnings("serial")
public class EtMainGUI extends JFrame {
    public EtMainGUI() {
	setTitle("Etudiant");
	setDefaultCloseOperation(EXIT_ON_CLOSE);
	JPanel contentPane = new JPanel();
	getContentPane().add(contentPane);
	contentPane.setBorder(new EmptyBorder(new Insets(5, 10, 5, 10)));
	contentPane.setLayout(new GridLayout(2,1));
	
	Etudiant e=(Etudiant)AuthGUI.activeUser;
	
	JMenuBar mBar = new JMenuBar();
	JMenu menu = new JMenu("Deconnexion");
	mBar.add(menu);
	menu.addMouseListener(new MouseListener() {
	    
	    @Override
	    public void mouseReleased(MouseEvent e) {}
	    
	    @Override
	    public void mousePressed(MouseEvent e) {}
	    
	    @Override
	    public void mouseExited(MouseEvent e) {}
	    
	    @Override
	    public void mouseEntered(MouseEvent e) {}
	    
	    @Override
	    public void mouseClicked(MouseEvent e) {
		AuthGUI.loggedOut=true;
		dispose();
	    }
	});
	this.setJMenuBar(mBar);

	JLabel wlcmLabel = new JLabel("Bienvenue, " + e.getName());
	wlcmLabel.setFont(new Font("Times new roman", Font.BOLD, 24));
	JPanel wlcmPanel = new JPanel();
	wlcmPanel.add(wlcmLabel);
	contentPane.add(wlcmPanel);

	JButton consButton = new JButton("Consulter Notes");
	JPanel butPanel = new JPanel();
	butPanel.add(consButton);
	contentPane.add(butPanel);
	consButton.addActionListener(new ActionListener() {

	    @Override
	    public void actionPerformed(ActionEvent e) {
		dispose();
		EtNotesGUI gui = new EtNotesGUI();
		gui.setVisible(true);
		gui.addWindowListener(new WindowListener() {

		    @Override
		    public void windowOpened(WindowEvent e) {

		    }

		    @Override
		    public void windowIconified(WindowEvent e) {

		    }

		    @Override
		    public void windowDeiconified(WindowEvent e) {

		    }

		    @Override
		    public void windowDeactivated(WindowEvent e) {

		    }

		    @Override
		    public void windowClosing(WindowEvent e) {
		    }

		    @Override
		    public void windowClosed(WindowEvent e) {
			setVisible(true);
			contentPane.grabFocus();
		    }

		    @Override
		    public void windowActivated(WindowEvent e) {

		    }
		});
	    }
	});

	pack();
	contentPane.grabFocus();
	setResizable(false);
	setLocationRelativeTo(null);
    }

    public static void main(String[] args) {
//	Etudiant et = new Etudiant(1, "12345678", "TestEtu", 1, "yass123", "toor");
	EtMainGUI gui = new EtMainGUI();
	gui.setVisible(true);
    }
}
