package model;

import gestion.*;
import launcher.launcher;

public class ClasseMatiere {

    public int idCM;
    public Classe classe = null;
    public Matiere matiere = null;
    public Enseignant ens = null;
    public int semestre;

    public ClasseMatiere(int id, Classe c, Matiere m, Enseignant e, int s) {
	this.idCM = id;
	this.classe = c;
	this.matiere = m;
	this.semestre = s;
	this.ens = e;
    }

    public ClasseMatiere(int id, int idc, int idm, int ide, int s) {
	this.idCM = id;
	this.classe=launcher.findClasse(idc);
	this.matiere=launcher.findMatiere(idm);
	this.ens=launcher.findEns(ide);
	this.semestre=s;
    }

    public int getId() {
	return this.idCM;
    }
    
    public void setId(int id) {
	this.idCM=id;
    }
    
    public String toString() {
	return this.classe.getNom();
    }
    
    public static void main(String[] args) {

    }

}
