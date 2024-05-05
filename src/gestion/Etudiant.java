package gestion;

import java.util.ArrayList;

import model.Matiere;
import model.NoteMatiere;

public class Etudiant extends Utilisateur {

    private int idEtu;
    private String cin;
    private String name;
    private int idClasse;
    private ArrayList<NoteMatiere> notesS1 = new ArrayList<>();
    private ArrayList<NoteMatiere> notesS2 = new ArrayList<>();

    public Etudiant(int id, String cin, String name, int idcl, ArrayList<NoteMatiere> notesS1, ArrayList<NoteMatiere> notesS2) {
	super();
	this.idEtu = id;
	this.cin = cin;
	this.name = name;
	this.idClasse = idcl;
	this.notesS1 = notesS1;
	this.notesS2 = notesS2;
    }

    public Etudiant(int id, String cin, String name) {
	super();
	this.idEtu = id;
	this.cin = cin;
	this.name = name;
    }

    public Etudiant(int id, String name, String cin, int idCl, String login, String pwd) {
	super(login, pwd);
	this.idEtu = id;
	this.cin = cin;
	this.name = name;
	this.idClasse = idCl;
    }

    public void update(int id, String name, String cin, int idCl, String login, String pwd) {
	super.setLogin(login);
	if (!pwd.equals(getMotDePasse()))
	    super.setMotDePasse(pwd);
	this.idEtu = id;
	this.cin = cin;
	this.name = name;
	this.idClasse = idCl;
    }

    public void setClId(int idcl) {
	this.idClasse = idcl;
    }

    public int getClId() {
	return this.idClasse;
    }

    public int getId() {
	return idEtu;
    }

    public void setId(int id) {
	this.idEtu = id;
    }

    public String getCin() {
	return cin;
    }

    public void setCin(String cin) {
	this.cin = cin;
    }

    public String getName() {
	return name;
    }

    public void setName(String name) {
	this.name = name;
    }

    public ArrayList<NoteMatiere> getNotesS1() {
	return notesS1;
    }

    protected void setNotesS1(ArrayList<NoteMatiere> notesS1) {
	this.notesS1 = notesS1;
    }

    public ArrayList<NoteMatiere> getNotesS2() {
	return notesS2;
    }

    protected void setNotesS2(ArrayList<NoteMatiere> notesS2) {
	this.notesS2 = notesS2;
    }

    @Override
    public String toString() {
	return this.name;
    }

    private double moyenneSemestre(ArrayList<NoteMatiere> notesSemestre) {
	double sum = 0.0;
	double totalCoef = 0.0;
	for (NoteMatiere notes : notesSemestre) {
	    sum += notes.moyenne() * notes.getMatiere().getCoefMatiere();
	    totalCoef += notes.getMatiere().getCoefMatiere();
	}
	return Math.round(sum / totalCoef);

    }

    public double moyenneS1() {
	return moyenneSemestre(notesS1);
    }

    public double moyenneS2() {
	return moyenneSemestre(notesS2);
    }

    public double moyenne() {
	return (moyenneS1() + moyenneS2()) / 2;
    }

    public void ajouterNote(NoteMatiere item) {
	notesS1.add(item);
    }

    // initialiser les matieres
    public void setListeMatiereS1(ArrayList<Matiere> listeMatiere) {
	if (notesS1.isEmpty()) {
	    for (Matiere m : listeMatiere) {
		notesS1.add(new NoteMatiere(m));
	    }
	} else {
	    // TODO: make sure if you will add the list or to show an alert
	    System.err.println("should be implemented add to list noteMatiere");
	}

    }

    public void setListeMatiereS2(ArrayList<Matiere> listeMatiere) {
	if (notesS2.isEmpty()) {
	    for (Matiere m : listeMatiere) {
		notesS2.add(new NoteMatiere(m));
	    }
	} else {
	    // TODO: make sure if you will add the list or to show an alert
	    System.err.println("should be implemented add to list noteMatiere");
	}

    }

}
