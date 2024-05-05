package model;

public class Matiere {

    private int idMat;
    private String nomMatiere;
    private double coefExam;
    private double coefDs;
    private double coefTp;
    private double coefMatiere;

    public Matiere(int id, String nomMatiere, double coefExam, double coefds, double coefTp, double coefMatiere) {
	this.idMat = id;
	this.coefExam = coefExam;
	this.coefDs = coefds;
	this.coefTp = coefTp;
	this.coefMatiere = coefMatiere;
	this.nomMatiere = nomMatiere;
    }

    public void update(int id, String nomMat, double coefDS, double coefTP, double coefEX, double coefMat) {
	this.idMat = id;
	this.coefExam = coefEX;
	this.coefDs = coefDS;
	this.coefTp = coefTP;
	this.coefMatiere = coefMat;
	this.nomMatiere = nomMat;
    }

    public double getCoefExam() {
	return coefExam;
    }

    public void setCoefExam(double coefExam) {
	this.coefExam = coefExam;
    }

    public double getCoefTp() {
	return coefTp;
    }

    public void setCoefTp(double coefTp) {
	this.coefTp = coefTp;
    }

    public double getCoefMatiere() {
	return coefMatiere;
    }

    public void setCoefMatiere(double coefMatiere) {
	this.coefMatiere = coefMatiere;
    }

    public String getNomMatiere() {
	return nomMatiere;
    }

    public void setNomMatiere(String nomMatiere) {
	this.nomMatiere = nomMatiere;
    }

    public int getId() {
	return idMat;
    }

    public void setId(int id) {
	this.idMat = id;
    }

    public double getCoefDs() {
	return coefDs;
    }

    public void setCoefDs(double coefDs) {
	this.coefDs = coefDs;
    }

    @Override
    public String toString() {
	return this.nomMatiere;
    }

}
