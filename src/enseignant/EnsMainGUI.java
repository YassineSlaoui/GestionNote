package enseignant;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import auth_gui.AuthGUI;
import java.awt.event.*;
import gestion.Enseignant;

@SuppressWarnings("serial")
public class EnsMainGUI extends JFrame {

    public EnsMainGUI() {
	setTitle("Enseignants");
	JPanel contentPane = new JPanel();
	setDefaultCloseOperation(EXIT_ON_CLOSE);
	getContentPane().add(contentPane);
	contentPane.setBorder(new EmptyBorder(new Insets(5, 10, 5, 10)));
	contentPane.setLayout(new GridLayout(2,1));
	
	Enseignant ens=(Enseignant)AuthGUI.activeUser;
	
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

	JLabel wlcmLabel = new JLabel("Bienvenue, " + ens.getName());
	wlcmLabel.setFont(new Font("Times new roman", Font.BOLD, 24));
	JPanel wlcmPanel = new JPanel();
	wlcmPanel.add(wlcmLabel);
	contentPane.add(wlcmPanel);

	JButton modnoteButton = new JButton("Modifier Notes");
	JPanel butPanel = new JPanel();
	butPanel.add(modnoteButton);
	contentPane.add(butPanel);
	modnoteButton.addActionListener(new ActionListener() {

	    @Override
	    public void actionPerformed(ActionEvent e) {
		dispose();
		EnsNotesGUI gui = new EnsNotesGUI();
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
//	Enseignant ens = new Enseignant(0, "01234567", "TestEns", "imene.grayaa", "12345!");
	EnsMainGUI gui = new EnsMainGUI();
	gui.setVisible(true);
    }

}
