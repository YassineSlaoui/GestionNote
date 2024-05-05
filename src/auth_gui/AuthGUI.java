package auth_gui;

import javax.swing.*;

import admin.AdminMainGUI;
import enseignant.EnsMainGUI;
import etudiant.EtMainGUI;
import gestion.Admin;
import gestion.Enseignant;
import gestion.Etudiant;
import gestion.Utilisateur;
import launcher.launcher;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

@SuppressWarnings("serial")
public class AuthGUI extends JFrame implements ActionListener {

    ArrayList<Utilisateur> users = null;
    JTextField unTf = new JTextField(10);
    JPasswordField pwdTf = new JPasswordField(10);
    static public Utilisateur activeUser=null;
    static public Boolean loggedOut=false;

    public AuthGUI() {
	this.users = launcher.users;
	setTitle("Authentication");
	setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	setResizable(false);

	Container ContentPane = getContentPane();
	ContentPane.setLayout(new BorderLayout(0, 7));

	JPanel northPanel = new JPanel();
	ContentPane.add(northPanel, BorderLayout.NORTH);
	JPanel centerPanel = new JPanel(new GridLayout(2, 1, 0, 10));
	ContentPane.add(centerPanel, BorderLayout.CENTER);

	JPanel southPanel = new JPanel();
	ContentPane.add(southPanel, BorderLayout.SOUTH);

	JButton fgtPwdBtn = new JButton("Mot de Passe oublié?");
	southPanel.add(fgtPwdBtn);
	fgtPwdBtn.addActionListener(new ActionListener() {

	    @Override
	    public void actionPerformed(ActionEvent e) {
		JOptionPane.showMessageDialog(ContentPane, "Priere de contacter un administrateur.", getTitle(), JOptionPane.INFORMATION_MESSAGE);
	    }
	});

	JLabel loginTitle = new JLabel("LOGIN");
	loginTitle.setFont(new Font(null, 0, 30));
	northPanel.add(loginTitle, BorderLayout.NORTH);
//		JButton crtAccBtn = new JButton("Create new Account");
//		southPanel.add(crtAccBtn);

	JLabel unLabel = new JLabel("Nom d'utilisateur");
	JPanel unPanel = new JPanel();
	unPanel.add(unLabel);
	unPanel.add(unTf);
	unTf.grabFocus();
	unTf.addActionListener(this);

	JLabel pwdLabel = new JLabel("Mot de passe        ");
	JPanel pwdPanel = new JPanel();
	pwdPanel.add(pwdLabel);
	pwdPanel.add(pwdTf);
	pwdTf.addActionListener(this);

	JPanel loginPanel = new JPanel(new GridLayout(2, 1, 0, 5));
	loginPanel.add(unPanel);
	loginPanel.add(pwdPanel);
	centerPanel.add(loginPanel);
	JPanel butPanel = new JPanel();
	centerPanel.add(butPanel);
	butPanel.setLayout(new GridLayout(1, 0, 0, 0));

	JPanel panel = new JPanel();
	butPanel.add(panel);
	JButton loginBut = new JButton("Login");
	panel.add(loginBut);
	loginBut.addActionListener(this);

//		pack();
	setSize(385, 265);
	setLocationRelativeTo(null);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
	Boolean found = false;
	if (users != null)
	    for (Utilisateur user : users) {
		if (user.seConnecter(unTf.getText(), String.valueOf(pwdTf.getPassword()))) {
		    found = true;
		    dispose();
		    unTf.setText("");
		    unTf.grabFocus();
		    pwdTf.setText("");
		    activeUser=user;
		    loggedOut=false;
		    JFrame childGui = null;
		    if (user instanceof Etudiant)
			childGui = new EtMainGUI();
		    if (user instanceof Enseignant)
			childGui = new EnsMainGUI();
		    if (user instanceof Admin)
			childGui = new AdminMainGUI();
		    childGui.setVisible(true);
		    childGui.addWindowListener(new WindowListener() {

			@Override
			public void windowOpened(WindowEvent e) {}

			@Override
			public void windowIconified(WindowEvent e) {}

			@Override
			public void windowDeiconified(WindowEvent e) {}

			@Override
			public void windowDeactivated(WindowEvent e) {}

			@Override
			public void windowClosing(WindowEvent e) {
			    
			}

			@Override
			public void windowClosed(WindowEvent e) {
			    if(loggedOut)
				setVisible(true);
//			    activeUser=null;
			}

			@Override
			public void windowActivated(WindowEvent e) {}
		    });
		    break;
		}
	    }
	if (!found) {
	    JOptionPane.showMessageDialog(getContentPane(), "Wrong Credentials", getTitle(), JOptionPane.ERROR_MESSAGE);
	}
	if(launcher.users.size()==0) {
	    dispose();
	    unTf.setText("");
	    unTf.grabFocus();
	    pwdTf.setText("");
	    activeUser=new Admin(-1, null, "First Start Super Admin", true);
	    loggedOut=false;
	    JFrame childGui = null;
	    childGui = new AdminMainGUI();
	    childGui.setVisible(true);
	    childGui.addWindowListener(new WindowListener() {

		@Override
		public void windowOpened(WindowEvent e) {}

		@Override
		public void windowIconified(WindowEvent e) {}

		@Override
		public void windowDeiconified(WindowEvent e) {}

		@Override
		public void windowDeactivated(WindowEvent e) {}

		@Override
		public void windowClosing(WindowEvent e) {
		    
		}

		@Override
		public void windowClosed(WindowEvent e) {
		    if(loggedOut)
			setVisible(true);
//		    activeUser=null;
		}

		@Override
		public void windowActivated(WindowEvent e) {}
	    });
	}
    }

    public static void main(String[] args) {
	AuthGUI authGUI = new AuthGUI();
	authGUI.setVisible(true);
    }

}
