package admin;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.*;
import auth_gui.AuthGUI;
import java.awt.event.*;
import gestion.Admin;
import mgmt_guis.MgmtAdmins;
import mgmt_guis.MgmtClasses;
import mgmt_guis.MgmtEns;
import mgmt_guis.MgmtEts;
import mgmt_guis.MgmtMats;
import mgmt_guis.UpdNote;

@SuppressWarnings("serial")
public class AdminMainGUI extends JFrame {

    public AdminMainGUI() {
	setTitle("Administrateurs");
	setDefaultCloseOperation(EXIT_ON_CLOSE);
	JPanel contentPane = new JPanel();
	getContentPane().add(contentPane);
	contentPane.setBorder(new EmptyBorder(new Insets(5, 10, 5, 10)));
	contentPane.setLayout(new GridLayout(2, 1));

	Admin adm = (Admin) AuthGUI.activeUser;

	JMenuBar mBar = new JMenuBar();
	JMenu menu = new JMenu("Deconnexion");
	mBar.add(menu);
	menu.addMouseListener(new MouseListener() {

	    @Override
	    public void mouseReleased(MouseEvent e) {
	    }

	    @Override
	    public void mousePressed(MouseEvent e) {
	    }

	    @Override
	    public void mouseExited(MouseEvent e) {
	    }

	    @Override
	    public void mouseEntered(MouseEvent e) {
	    }

	    @Override
	    public void mouseClicked(MouseEvent e) {
		AuthGUI.loggedOut = true;
		dispose();
	    }
	});
	this.setJMenuBar(mBar);

	JLabel wlcmLabel = new JLabel("Bienvenue, " + adm.getName());
	wlcmLabel.setFont(new Font("Times new roman", Font.BOLD, 24));
	JPanel wlcmPanel = new JPanel();
	wlcmPanel.add(wlcmLabel);
	contentPane.add(wlcmPanel);

	JButton matiereButton = new JButton("Matieres");
	JButton classesButton = new JButton("Classes");
	JButton etuButton = new JButton("Etudiants");
	JButton ensButton = new JButton("Enseignants");

	JPanel butPanel = new JPanel();
	butPanel.add(matiereButton);
	butPanel.add(classesButton);
	butPanel.add(etuButton);
	butPanel.add(ensButton);
	contentPane.add(butPanel);
	matiereButton.addActionListener(new ActionListener() {

	    @Override
	    public void actionPerformed(ActionEvent e) {
		dispose();
		MgmtMats gui = new MgmtMats();
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
	classesButton.addActionListener(new ActionListener() {

	    @Override
	    public void actionPerformed(ActionEvent e) {
		dispose();
		MgmtClasses gui = new MgmtClasses();
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
	etuButton.addActionListener(new ActionListener() {

	    @Override
	    public void actionPerformed(ActionEvent e) {
		dispose();
		MgmtEts gui = new MgmtEts();
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
	ensButton.addActionListener(new ActionListener() {

	    @Override
	    public void actionPerformed(ActionEvent e) {
		dispose();
		MgmtEns gui = new MgmtEns();
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
	if (adm.isSuperAdmin()) {
	    JButton modNote = new JButton("Modifier note Etudiant");
	    butPanel.add(modNote);
	    modNote.addActionListener(new ActionListener() {

		@Override
		public void actionPerformed(ActionEvent e) {
		    dispose();
		    UpdNote gui = new UpdNote();
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

	    JButton adminsButton = new JButton("Administrateurs");
	    butPanel.add(adminsButton);
	    adminsButton.addActionListener(new ActionListener() {

		@Override
		public void actionPerformed(ActionEvent e) {
		    dispose();
		    MgmtAdmins gui = new MgmtAdmins();
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
	}

	pack();
	contentPane.grabFocus();
	setResizable(false);
	setLocationRelativeTo(null);
    }

    public static void main(String[] args) {
//	Admin ad = new Admin(0, "0125452", "TestAdmin", true, "admin", "*****");
	AdminMainGUI gui = new AdminMainGUI();
	gui.setVisible(true);
    }
}
