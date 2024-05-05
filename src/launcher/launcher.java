package launcher;

import java.sql.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Scanner;
import javax.swing.UIManager;

import com.formdev.flatlaf.FlatDarkLaf;
import auth_gui.AuthGUI;
import gestion.Admin;
import gestion.Classe;
import gestion.Enseignant;
import gestion.Etudiant;
import gestion.Utilisateur;
import model.ClasseMatiere;
import model.Matiere;
import model.Note;
import model.NoteMatiere;

@SuppressWarnings("unused")
public class launcher {

    private static final String DB_URL = "jdbc:mysql://localhost:3306/gestion_notes";
    private static final String USER = "root";
    private static final String PASS = "toor";

    static public ArrayList<Utilisateur> users = new ArrayList<>();
    static public ArrayList<Classe> classes = new ArrayList<>();
    static public ArrayList<Matiere> matieres = new ArrayList<>();
    static public ArrayList<ClasseMatiere> classesMatieres = new ArrayList<>();

    public launcher() {
        this(false);
    }

    public launcher(Boolean dbEnabled) {
        if (!dbEnabled)
            setupStaticData();
        else if (!connectDb())
            System.err.println("Failed connecting to dataBase!");
        else {
            System.out.println("Connected to dataBase successfully.");
            importFromDB();
        }
        AuthGUI maingui = new AuthGUI();
        maingui.setVisible(true);
    }

    private Boolean connectDb() {
        try (Connection connection = DriverManager.getConnection(DB_URL, USER, PASS);) {

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    private void importFromDB() {
        try (Connection connection = DriverManager.getConnection(DB_URL, USER, PASS); Statement statement = connection.createStatement();) {
            ResultSet rs = statement.executeQuery("SELECT * from Enseignant;");
            while (rs.next()) {
                users.add(new Enseignant(rs.getInt("idEns"), rs.getString("nomEns"), rs.getString("cinEns"), rs.getString("loginEns"), rs.getString("pwdEns")));
            }
            rs = statement.executeQuery("SELECT * from Admin;");
            while (rs.next()) {
                users.add(new Admin(rs.getInt("idAdmin"), rs.getString("nomAdmin"), rs.getString("cinAdmin"), rs.getBoolean("isSuper"), rs.getString("loginAdmin"), rs.getString("pwdAdmin")));
            }
            rs = statement.executeQuery("SELECT * from Classe;");
            while (rs.next()) {
                classes.add(new Classe(rs.getInt("idClasse"), rs.getString("nomClasse")));
            }
            rs = statement.executeQuery("SELECT * from Etudiant;");
            while (rs.next()) {
                Etudiant et = new Etudiant(rs.getInt("idEtu"), rs.getString("nomEtu"), rs.getString("cinEtu"), rs.getInt("idClasse"), rs.getString("loginEtu"), rs.getString("pwdEtu"));
                users.add(et);
                findClasse(et.getClId()).listeEtudiant.add(et);
            }
            rs = statement.executeQuery("SELECT * from Matiere;");
            while (rs.next()) {
                matieres.add(new Matiere(rs.getInt("idMatiere"), rs.getString("nomMatiere"), rs.getDouble("coefEX"), rs.getDouble("coefDS"), rs.getDouble("coefTP"), rs.getDouble("coefMatiere")));
            }
            rs = statement.executeQuery("SELECT * from ClasseMatiere;");
            while (rs.next()) {
                classesMatieres.add(new ClasseMatiere(rs.getInt("idCM"), rs.getInt("idClasse"), rs.getInt("idMatiere"), rs.getInt("idEns"), rs.getInt("Semestre")));
                if (rs.getInt("Semestre") == 1)
                    findClasse(rs.getInt("idClasse")).listeMatiereS1.add(findMatiere(rs.getInt("idMatiere")));
                else
                    findClasse(rs.getInt("idClasse")).listeMatiereS2.add(findMatiere(rs.getInt("idMatiere")));
            }
            rs = statement.executeQuery("SELECT * from NoteMatiere;");
            while (rs.next()) {
                if (rs.getInt("Semestre") == 1)
                    findEtu(rs.getInt("idEtu")).getNotesS1().add(new NoteMatiere(rs.getInt("idNM"), findMatiere(rs.getInt("idMatiere")), new Note(rs.getDouble("noteEx"), rs.getDouble("noteDS"), rs.getDouble("noteTP"))));
                else
                    findEtu(rs.getInt("idEtu")).getNotesS2().add(new NoteMatiere(rs.getInt("idNM"), findMatiere(rs.getInt("idMatiere")), new Note(rs.getDouble("noteEx"), rs.getDouble("noteDS"), rs.getDouble("noteTP"))));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    static public void delEns(int idEns) {
        try (Connection connection = DriverManager.getConnection(DB_URL, USER, PASS); PreparedStatement pStatement = connection.prepareStatement("DELETE FROM Enseignant WHERE idEns = ?");) {
            pStatement.setInt(1, idEns);
            for (ClasseMatiere cm : classesMatieres) { // Avant de supprimer un enseignant on supprime
                if (cm.ens.getId() == idEns) // toutes les relations ClasseMatiere ou il
                    delCM(cm.idCM); // enseigne une matiere à une classe, toutes
            } // les notesMatieres des etudiants de la matiere
            pStatement.executeUpdate(); // ensignée par cet enseignant seront supprimés.
            users.remove(findEns(idEns));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    static public boolean updEns(int idEns, String nom, String cin, String login, String pwd) {
        try (Connection connection = DriverManager.getConnection(DB_URL, USER, PASS);) {
            PreparedStatement pStatement = null;
            Enseignant ens = findEns(idEns);
            if (!pwd.equals(ens.getMotDePasse())) {
                pStatement = connection.prepareStatement("UPDATE Enseignant SET nomEns = ?,cinEns = ?,loginEns = ?,pwdEns = ? WHERE idEns = ?");
                pStatement.setString(4, pwd);
                pStatement.setInt(5, idEns);
            } else {
                pStatement = connection.prepareStatement("UPDATE Enseignant SET nomEns = ?,cinEns = ?,loginEns = ? WHERE idEns = ?");
                pStatement.setInt(4, idEns);
            }
            pStatement.setString(1, nom);
            pStatement.setString(2, cin);
            pStatement.setString(3, login);
            pStatement.executeUpdate();
            pStatement.close();
            ens.update(idEns, nom, cin, login, pwd);
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    static public int addEns(String nom, String cin, String login, String pwd) {
        try (Connection connection = DriverManager.getConnection(DB_URL, USER, PASS);
             PreparedStatement pStatement = connection.prepareStatement("INSERT INTO Enseignant(nomEns,cinEns,loginEns,pwdEns) VALUES (?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);) {
            pStatement.setString(1, nom);
            pStatement.setString(2, cin);
            pStatement.setString(3, login);
            pStatement.setString(4, pwd);
            pStatement.executeUpdate();
            ResultSet rs = pStatement.getGeneratedKeys();
            if (rs.next()) {
                int auto_id = rs.getInt(1);
                users.add(new Enseignant(auto_id, nom, cin, login, pwd));
                return auto_id;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }
        return 0;
    }

    static public void delEtu(int idEt) {
        try (Connection connection = DriverManager.getConnection(DB_URL, USER, PASS); PreparedStatement pStatement = connection.prepareStatement("DELETE FROM Etudiant WHERE idEtu = ?");) {
            pStatement.setInt(1, idEt);
            Etudiant et = findEtu(idEt);
            ArrayList<Integer> nmIds = new ArrayList<>();
            for (NoteMatiere nm : et.getNotesS1()) // Avant de supprimer un etudiant,
                nmIds.add(nm.getId()); // on supprime ses notesMatieres.
            for (NoteMatiere nm : et.getNotesS2())
                nmIds.add(nm.getId());
            for (Integer i : nmIds)
                delNM(i);
            pStatement.executeUpdate();
            findClasse(et.getClId()).listeEtudiant.remove(et); // On supprime l'etudiant de la liste de sa classe
            users.remove(findEtu(idEt)); // On le supprime aussi de la liste des utilisateurs
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    static public boolean updEtu(int idEtu, String nom, String cin, int idCl, String login, String pwd) {
        try (Connection connection = DriverManager.getConnection(DB_URL, USER, PASS);) {
            Etudiant et = findEtu(idEtu);
            PreparedStatement pStatement = null;
            if (!pwd.equals(et.getMotDePasse())) {
                pStatement = connection.prepareStatement("UPDATE Etudiant SET nomEtu = ?,cinEtu = ?,idClasse = ?,loginEtu = ?,pwdEtu = ? WHERE idEtu = ?");
                pStatement.setString(5, pwd);
                pStatement.setInt(6, idEtu);
            } else {
                pStatement = connection.prepareStatement("UPDATE Etudiant SET nomEtu = ?,cinEtu = ?,idClasse = ?,loginEtu = ? WHERE idEtu = ?");
                pStatement.setInt(5, idEtu);
            }
            pStatement.setString(1, nom);
            pStatement.setString(2, cin);
            pStatement.setInt(3, idCl);
            pStatement.setString(4, login);
            if (idCl != et.getClId()) { // Si l'etudiant change de classe, on supprime ses notesMatieres de cette classe
                ArrayList<Integer> nmIds = new ArrayList<>();
                for (NoteMatiere nm : et.getNotesS1())
                    nmIds.add(nm.getId());
                for (NoteMatiere nm : et.getNotesS2())
                    nmIds.add(nm.getId());
                for (Integer i : nmIds)
                    delNM(i);
                findClasse(et.getClId()).listeEtudiant.remove(et); // On le supprime de la liste d'etudiants de sa classe d'origine
                findClasse(idCl).listeEtudiant.add(et);// On ajoute l'etudiant a la listeEtudiants de sa nouvelle classe
                for (ClasseMatiere cm : classesMatieres) { // En ajoutant un etudiant, on lui attribue des 0
                    if (cm.classe.getId() == idCl) { // Dans toutes les matieres enseignées dans sa nouvelle classe
                        addNM(idEtu, cm.matiere.getId(), cm.semestre, 0, 0, 0);
                    }
                }
            }
            pStatement.executeUpdate();
            pStatement.close();
            et.update(idEtu, nom, cin, idCl, login, pwd);
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    static public int addEtu(String nom, String cin, int idClasse, String login, String pwd) {
        try (Connection connection = DriverManager.getConnection(DB_URL, USER, PASS);
             PreparedStatement pStatement = connection.prepareStatement("INSERT INTO Etudiant(nomEtu,cinEtu,idClasse,loginEtu,pwdEtu) VALUES (?, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);) {
            pStatement.setString(1, nom);
            pStatement.setString(2, cin);
            pStatement.setInt(3, idClasse);
            pStatement.setString(4, login);
            pStatement.setString(5, pwd);
            pStatement.executeUpdate();
            ResultSet rs = pStatement.getGeneratedKeys();
            if (rs.next()) {
                int auto_id = rs.getInt(1);
                Etudiant et = new Etudiant(auto_id, nom, cin, idClasse, login, pwd);
                users.add(et);
                findClasse(idClasse).listeEtudiant.add(et);// On ajoute l'etudiant a la listeEtudiants de sa classe
                for (ClasseMatiere cm : classesMatieres) { // En ajoutant un etudiant, on lui attribue des 0
                    if (cm.classe.getId() == idClasse) { // Dans toutes les matieres enseignées dans sa classe.
                        addNM(auto_id, cm.matiere.getId(), cm.semestre, 0, 0, 0);
                    }
                }
                return auto_id;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }
        return 0;
    }

    static public void delAdmin(int idAd) {
        try (Connection connection = DriverManager.getConnection(DB_URL, USER, PASS); PreparedStatement pStatement = connection.prepareStatement("DELETE FROM `Admin` WHERE idAdmin = ?");) {
            pStatement.setInt(1, idAd);
            pStatement.executeUpdate();
            users.remove(findAdm(idAd));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    static public boolean updAdmin(int idAd, String nom, String cin, Boolean sup, String login, String pwd) {
        try (Connection connection = DriverManager.getConnection(DB_URL, USER, PASS);) {
            PreparedStatement pStatement = null;
            Admin adm = findAdm(idAd);
            if (!pwd.equals(adm.getMotDePasse())) {
                pStatement = connection.prepareStatement("UPDATE `Admin` SET nomAdmin = ?,cinAdmin = ?,isSuper = ?,loginAdmin = ?,pwdAdmin = ? WHERE idAdmin = ?");
                pStatement.setString(5, pwd);
                pStatement.setInt(6, idAd);
            } else {
                pStatement = connection.prepareStatement("UPDATE `Admin` SET nomAdmin = ?,cinAdmin = ?,isSuper = ?,loginAdmin = ? WHERE idAdmin = ?");
                pStatement.setInt(5, idAd);
            }
            pStatement.setString(1, nom);
            pStatement.setString(2, cin);
            pStatement.setBoolean(3, sup);
            pStatement.setString(4, login);
            pStatement.executeUpdate();
            pStatement.close();
            adm.update(idAd, nom, cin, sup, login, pwd);
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    static public int addAdmin(String nom, String cin, Boolean sup, String login, String pwd) {
        try (Connection connection = DriverManager.getConnection(DB_URL, USER, PASS);
             PreparedStatement pStatement = connection.prepareStatement("INSERT INTO `Admin`(nomAdmin,cinAdmin,isSuper,loginAdmin,pwdAdmin) VALUES (?, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);) {
            pStatement.setString(1, nom);
            pStatement.setString(2, cin);
            pStatement.setBoolean(3, sup);
            pStatement.setString(4, login);
            pStatement.setString(5, pwd);
            pStatement.executeUpdate();
            ResultSet rs = pStatement.getGeneratedKeys();
            if (rs.next()) {
                int auto_id = rs.getInt(1);
                users.add(new Admin(auto_id, nom, cin, sup, login, pwd));
                return auto_id;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }
        return 0;
    }

    static public void delClasse(int idCl) {
        try (Connection connection = DriverManager.getConnection(DB_URL, USER, PASS); PreparedStatement pStatement = connection.prepareStatement("DELETE FROM Classe WHERE idClasse = ?");) {
            pStatement.setInt(1, idCl);
            for (ClasseMatiere cm : classesMatieres) { // Avant de suuprimer une classe on
                if (cm.classe.getId() == idCl) { // supprime toute relation qu'elle
                    delCM(cm.idCM); // a dans la table ClasseMatiere.
                }
            }
            for (Utilisateur u : users) { // On supprime aussi tous les
                if (u instanceof Etudiant) { // etudiants inscrits dedons.
                    Etudiant et = (Etudiant) u;
                    if (et.getClId() == idCl)
                        delEtu(et.getId());
                }
            }
            pStatement.executeUpdate();
            classes.remove(findClasse(idCl));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    static public boolean updClasse(int idCl, String nom) {
        try (Connection connection = DriverManager.getConnection(DB_URL, USER, PASS); PreparedStatement pStatement = connection.prepareStatement("UPDATE Classe SET nomClasse = ? WHERE idClasse = ?");) {
            pStatement.setString(1, nom);
            pStatement.setInt(2, idCl);
            pStatement.executeUpdate();
            findClasse(idCl).setNom(nom);
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    static public int addClasse(String nomClasse) {
        try (Connection connection = DriverManager.getConnection(DB_URL, USER, PASS); PreparedStatement pStatement = connection.prepareStatement("INSERT INTO Classe(nomClasse) VALUES (?)", Statement.RETURN_GENERATED_KEYS);) {
            pStatement.setString(1, nomClasse);
            pStatement.executeUpdate();
            ResultSet rs = pStatement.getGeneratedKeys();
            if (rs.next()) {
                int auto_id = rs.getInt(1);
                classes.add(new Classe(auto_id, nomClasse));
                return auto_id;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }
        return 0;
    }

    static public void delMatiere(int idM) {
        try (Connection connection = DriverManager.getConnection(DB_URL, USER, PASS); PreparedStatement pStatement = connection.prepareStatement("DELETE FROM Matiere WHERE idMatiere = ?");) {
            pStatement.setInt(1, idM);
            for (ClasseMatiere cm : classesMatieres) { // Avant de supprimer une matiere on
                if (cm.matiere.getId() == idM) { // supprime toute relation qu'elle a
                    delCM(cm.idCM); // avec toutes les classes, en supprimant
                } // ces relations avec delCM, les notesMatieres
            } // des etudiants sont supprimées aussi.
            pStatement.executeUpdate();
            matieres.remove(findMatiere(idM));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    static public boolean updMatiere(int idMat, String nomMat, double coefDS, double coefTP, double coefEX, double coefMat) {
        try (Connection connection = DriverManager.getConnection(DB_URL, USER, PASS);
             PreparedStatement pStatement = connection.prepareStatement("UPDATE Matiere SET nomMatiere = ?,CoefEX = ?,coefDS = ?,coefTP = ?,coefMatiere = ? WHERE idMatiere = ?");) {
            pStatement.setString(1, nomMat);
            pStatement.setDouble(2, coefEX);
            pStatement.setDouble(3, coefDS);
            pStatement.setDouble(4, coefTP);
            pStatement.setDouble(5, coefMat);
            pStatement.setInt(6, idMat);
            pStatement.executeUpdate();
            findMatiere(idMat).update(idMat, nomMat, coefDS, coefTP, coefEX, coefMat);
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    static public int addMatiere(String nomMat, double coefDS, double coefTP, double coefEX, double coefMat) {
        try (Connection connection = DriverManager.getConnection(DB_URL, USER, PASS);
             PreparedStatement pStatement = connection.prepareStatement("INSERT INTO Matiere(nomMatiere,CoefEX,coefDS,coefTP,coefMatiere) VALUES (?, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);) {
            pStatement.setString(1, nomMat);
            pStatement.setDouble(2, coefEX);
            pStatement.setDouble(3, coefDS);
            pStatement.setDouble(4, coefTP);
            pStatement.setDouble(5, coefMat);
            pStatement.executeUpdate();
            ResultSet rs = pStatement.getGeneratedKeys();
            if (rs.next()) {
                int auto_id = rs.getInt(1);
                matieres.add(new Matiere(auto_id, nomMat, coefEX, coefDS, coefTP, coefMat));
                return auto_id;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }
        return 0;
    }

    static public void delCM(int idCM) {
        try (Connection connection = DriverManager.getConnection(DB_URL, USER, PASS); PreparedStatement pStatement = connection.prepareStatement("DELETE FROM ClasseMatiere WHERE idCM = ?");) {
            pStatement.setInt(1, idCM);
            ClasseMatiere cm = findCM(idCM);
            for (Utilisateur u : users) { // Si on supprime une relation entre classe et matiere,
                if (u instanceof Etudiant) { // on supprime toutes les NoteMatieres des etudiant
                    Etudiant et = (Etudiant) u; // de la classe concernée pour cette matière.
                    if (et.getClId() == cm.classe.getId())
                        if (cm.semestre == 1) {
                            for (NoteMatiere nm : et.getNotesS1())
                                if (nm.getMatiere().getId() == cm.matiere.getId()) {
                                    delNM(nm.getId());
                                    break;
                                }
                        } else
                            for (NoteMatiere nm : et.getNotesS2())
                                if (nm.getMatiere().getId() == cm.matiere.getId()) {
                                    delNM(nm.getId());
                                    break;
                                }
                }
            }
            if (cm.semestre == 1) // On supprime la matiere de la liste des matieres de sa classe
                cm.classe.listeMatiereS1.remove(cm.matiere);
            else
                cm.classe.listeMatiereS2.remove(cm.matiere);
            pStatement.executeUpdate();
            classesMatieres.remove(findCM(idCM));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    static public boolean updEnsCM(int idCM, int idEns) {
        try (Connection connection = DriverManager.getConnection(DB_URL, USER, PASS); PreparedStatement pStatement = connection.prepareStatement("UPDATE ClasseMatiere SET idEns = ? WHERE idCM = ?");) {
            pStatement.setInt(1, idEns);
            pStatement.setInt(2, idCM);
            pStatement.executeUpdate();
            findCM(idCM).ens = findEns(idEns);
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    static public int addCM(int idC, int idM, int idE, int sem) {
        try (Connection connection = DriverManager.getConnection(DB_URL, USER, PASS);
             PreparedStatement pStatement = connection.prepareStatement("INSERT INTO ClasseMatiere(idClasse,idMatiere,idEns,semestre) VALUES (?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);) {
            pStatement.setInt(1, idC);
            pStatement.setInt(2, idM);
            pStatement.setInt(3, idE);
            pStatement.setInt(4, sem);
            pStatement.executeUpdate();
            ResultSet rs = pStatement.getGeneratedKeys();
            if (rs.next()) {
                int auto_id = rs.getInt(1);
                classesMatieres.add(new ClasseMatiere(auto_id, idC, idM, idE, sem));
                for (Utilisateur u : users) { // En ajoutant une relation classeMatiere
                    if (u instanceof Etudiant) { // on attribue des 0 aux etudiants de la
                        Etudiant et = (Etudiant) u; // classe concernée par cette relation
                        if (et.getClId() == idC) // dans la matiere ajoutée.
                            addNM(et.getId(), idM, sem, 0, 0, 0);
                    }
                }
                if (sem == 1) // On ajoute la matiere dans la liste matiere de sa classe aussi
                    findClasse(idC).listeMatiereS1.add(findMatiere(idM));
                else
                    findClasse(idC).listeMatiereS2.add(findMatiere(idM));
                return auto_id;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }
        return 0;
    }

    static public void delNM(int idNM) {
        try (Connection connection = DriverManager.getConnection(DB_URL, USER, PASS); PreparedStatement pStatement = connection.prepareStatement("DELETE FROM NoteMatiere WHERE idNM = ?");) {
            pStatement.setInt(1, idNM);
            pStatement.executeUpdate();
            for (Utilisateur u : users) {
                if (u instanceof Etudiant) {
                    Etudiant et = (Etudiant) u;
                    for (NoteMatiere nm : et.getNotesS1())
                        if (nm.getId() == idNM) {
                            et.getNotesS1().remove(nm);
                            break;
                        }
                    for (NoteMatiere nm : et.getNotesS2())
                        if (nm.getId() == idNM) {
                            et.getNotesS2().remove(nm);
                            break;
                        }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    static public boolean updNM(int idNM, double noteDS, double noteTP, double noteEX) {
        try (Connection connection = DriverManager.getConnection(DB_URL, USER, PASS); PreparedStatement pStatement = connection.prepareStatement("UPDATE NoteMatiere SET noteDS = ?,noteTP = ?,noteEX = ? WHERE idNM = ?");) {
            pStatement.setDouble(1, noteDS);
            pStatement.setDouble(2, noteTP);
            pStatement.setDouble(3, noteEX);
            pStatement.setInt(4, idNM);
            pStatement.executeUpdate();
            findNM(idNM).updateNotes(noteDS, noteTP, noteEX);
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    static public int addNM(int idEt, int idMat, int sem, double noteDS, double noteTP, double noteEX) {
        try (Connection connection = DriverManager.getConnection(DB_URL, USER, PASS);
             PreparedStatement pStatement = connection.prepareStatement("INSERT INTO NoteMatiere(idEtu,idMatiere,semestre,noteDS,noteTP,noteEX) VALUES (?, ?, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);) {
            pStatement.setInt(1, idEt);
            pStatement.setInt(2, idMat);
            pStatement.setInt(3, sem);
            pStatement.setDouble(4, noteDS);
            pStatement.setDouble(5, noteTP);
            pStatement.setDouble(6, noteEX);
            pStatement.executeUpdate();
            ResultSet rs = pStatement.getGeneratedKeys();
            if (rs.next()) {
                int auto_id = rs.getInt(1);
                if (sem == 1)
                    findEtu(idEt).getNotesS1().add(new NoteMatiere(auto_id, findMatiere(idMat), new Note(noteEX, noteDS, noteTP)));
                else
                    findEtu(idEt).getNotesS2().add(new NoteMatiere(auto_id, findMatiere(idMat), new Note(noteEX, noteDS, noteTP)));
                return auto_id;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }
        return 0;
    }

    static public Etudiant findEtu(int idEt) {
        for (Utilisateur u : users)
            if (u instanceof Etudiant) {
                Etudiant et = (Etudiant) u;
                if (et.getId() == idEt)
                    return et;
            }
        return null;
    }

    static public Enseignant findEns(int idEns) {
        for (Utilisateur u : users)
            if (u instanceof Enseignant) {
                Enseignant ens = (Enseignant) u;
                if (ens.getId() == idEns)
                    return ens;
            }
        return null;
    }

    static public Admin findAdm(int idAd) {
        for (Utilisateur u : users)
            if (u instanceof Admin) {
                Admin ad = (Admin) u;
                if (ad.getId() == idAd)
                    return ad;
            }
        return null;
    }

    static public Matiere findMatiere(int idMat) {
        for (Matiere m : matieres) {
            if (m.getId() == idMat)
                return m;
        }
        return null;
    }

    static public Classe findClasse(int idCl) {
        for (Classe c : classes) {
            if (c.getId() == idCl)
                return c;
        }
        return null;
    }

    static public ClasseMatiere findCM(int idCM) {
        for (ClasseMatiere cm : classesMatieres) {
            if (cm.getId() == idCM)
                return cm;
        }
        return null;
    }

    static public NoteMatiere findNM(int idNM) {
        for (Utilisateur u : users) {
            if (u instanceof Etudiant) {
                Etudiant et = (Etudiant) u;
                for (NoteMatiere nm : et.getNotesS1())
                    if (nm.getId() == idNM)
                        return nm;
                for (NoteMatiere nm : et.getNotesS2())
                    if (nm.getId() == idNM)
                        return nm;
            }
        }
        return null;
    }

    private void setupStaticData() {
        initStaticUserList();
        initStaticMatList();
        initStaticClasseList();
        initStaticCMList();
        fillClasses();
    }

    private void initStaticUserList() {
        users.add(new Enseignant(0, "Nadia Ben Hamadi", "21354351", "nadia.bh", "12345!"));
        users.add(new Enseignant(1, "Zouhour Ben Dhiaf", "21354351", "zouhour.bd", "12345!"));
        users.add(new Enseignant(2, "Yosr Slama", "21354351", "yosr.slama", "12345!"));
        users.add(new Enseignant(3, "Imene Grayaa", "01234567", "imene.grayaa", "12345!"));
        users.add(new Enseignant(4, "Anissa Omrane", "01234534", "anissa.omrane", "12345!"));
        users.add(new Enseignant(5, "Leila Ben Othman", "21354351", "leila.bo", "12345!"));

        users.add(new Etudiant(0, "Ahmed Ben Ahmed", "00000000", 0, "ahmed", "1234"));
        users.add(new Etudiant(1, "Sami Fallah", "00000022", 0, "sami", "1234"));
        users.add(new Etudiant(2, "Yassine Slaoui", "00000011", 1, "yassine", "1234"));
        users.add(new Etudiant(3, "Yasmine Frikha", "00000033", 1, "yasmine", "1234"));

        users.add(new Admin(0, "Walid Admoun", "01254525", false, "admin", "*****"));
        users.add(new Admin(1, "Walid Admoun Pro Max", "01254527", true, "sadmin", "*****"));
    }

    private void initStaticMatList() {
        matieres.add(new Matiere(0, "Math", 0.7, 0.3, 0, 1));
        matieres.add(new Matiere(1, "C++", 0.7, 0, 0.3, 1));
        matieres.add(new Matiere(2, "Algorithmique", 0.7, 0.3, 0, 2));
        matieres.add(new Matiere(3, "JAVA", 0.7, 0, 0.3, 1));
        matieres.add(new Matiere(4, "Systeme d'Exploitation", 0.7, 0.3, 0, 1));
        matieres.add(new Matiere(5, "Python", 0.7, 0, 0.3, 1));
    }

    private void initStaticCMList() {
        classesMatieres.add(new ClasseMatiere(0, classes.get(0), matieres.get(0), (Enseignant) users.get(0), 1));
        classesMatieres.add(new ClasseMatiere(1, classes.get(0), matieres.get(1), (Enseignant) users.get(1), 1));
        classesMatieres.add(new ClasseMatiere(2, classes.get(0), matieres.get(2), (Enseignant) users.get(2), 1));
        classesMatieres.add(new ClasseMatiere(3, classes.get(0), matieres.get(3), (Enseignant) users.get(3), 2));
        classesMatieres.add(new ClasseMatiere(4, classes.get(0), matieres.get(4), (Enseignant) users.get(4), 2));
        classesMatieres.add(new ClasseMatiere(5, classes.get(0), matieres.get(5), (Enseignant) users.get(5), 2));

        classesMatieres.add(new ClasseMatiere(6, classes.get(1), matieres.get(0), (Enseignant) users.get(0), 1));
        classesMatieres.add(new ClasseMatiere(7, classes.get(1), matieres.get(1), (Enseignant) users.get(1), 1));
        classesMatieres.add(new ClasseMatiere(8, classes.get(1), matieres.get(2), (Enseignant) users.get(2), 1));
        classesMatieres.add(new ClasseMatiere(9, classes.get(1), matieres.get(3), (Enseignant) users.get(3), 2));
        classesMatieres.add(new ClasseMatiere(10, classes.get(1), matieres.get(4), (Enseignant) users.get(4), 2));
        classesMatieres.add(new ClasseMatiere(11, classes.get(1), matieres.get(5), (Enseignant) users.get(5), 2));
    }

    private void initStaticClasseList() {
        classes.add(new Classe(0, "MI A"));
        classes.add(new Classe(1, "MI B"));
    }

    private void fillClasseEtu(Classe c) {
        for (Utilisateur u : users) {
            if (u instanceof Etudiant) {
                Etudiant e = (Etudiant) u;
                if (e.getClId() == c.getId())
                    c.listeEtudiant.add(e);
            }
        }
    }

    private void fillClasseMatiere() {
        for (ClasseMatiere a : classesMatieres) {
            if (a.semestre == 1)
                a.classe.listeMatiereS1.add(a.matiere);
            else
                a.classe.listeMatiereS2.add(a.matiere);
        }
    }

    private void fillClasses() {
        for (Classe c : classes)
            fillClasseEtu(c);
        fillClasseMatiere();
    }

    public static void main(String[] args) {
//	System.out.println("Database enabled?[Y/n]: ");
        String dbEn;
//		try (Scanner scanner = new Scanner(System.in)) {
//			dbEn = scanner.nextLine();
//		}
        dbEn = "y";
        FlatDarkLaf.setup();
        try {
            UIManager.setLookAndFeel(new FlatDarkLaf());
        } catch (Exception e) {
            System.err.println("Failed to initialize LaF");
        }
        launcher launch = null;
        if (dbEn.toUpperCase().equals("Y"))
            launch = new launcher(true);
        else {
            launch = new launcher();
            classes.get(1).listeEtudiant.get(0).ajouterNote(new NoteMatiere(0, classes.get(1).listeMatiereS1.get(0), new Note(15, 17, 0)));
        }

    }

}
