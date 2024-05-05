package gestion;

import java.util.ArrayList;

import model.Matiere;

public class Classe {
	
	private int idClasse;
	private String nomClasse;
	public ArrayList<Matiere> listeMatiereS1 = new ArrayList<>();
	public ArrayList<Matiere> listeMatiereS2 = new ArrayList<>();
	public ArrayList<Etudiant> listeEtudiant = new ArrayList<>();

	public Classe() {
		
	}
	
	public Classe(int id,String nom) {
		this.idClasse=id;
		this.nomClasse=nom;
	}
	
	public void setNom(String nom) {
		this.nomClasse=nom;
	}
	
	public String getNom() {
		return nomClasse;
	}
	
	public void setId(int id)
	{
		this.idClasse=id;
	}
	
	public int getId() {
		return(this.idClasse);
	}
	
	public String toString() {
		return this.nomClasse;
	}
	
	public static void main(String[] args) {

		Classe mi2 = new Classe();

		//mi2.listeMatiereS1.add(new Matiere(5,"Algo",0.7, 0.3, 0, 2 ));
		mi2.listeMatiereS1.add(new Matiere(0,"math",0.7, 0.3, 0, 1 ));
		mi2.listeMatiereS1.add(new Matiere(1,"c++",0.7, 0, 0.3, 1 ));

		mi2.listeMatiereS2.add(new Matiere(2,"JAVA",0.7, 0, 0.3, 1 ));
		mi2.listeMatiereS2.add(new Matiere(3,"proba",0.7, 0.3, 0, 1 ));
		//mi2.listeMatiereS2.add(new Matiere(4,"Python",0.7, 0, 0.3, 1 ));
		
		mi2.listeEtudiant.add(new Etudiant(0, "00000000", "Ahmed"));
		mi2.listeEtudiant.add(new Etudiant(1, "00000022", "Sami"));
		//mi2.listeEtudiant.add(new Etudiant("01-3", "00000033", "Sana"));
		//mi2.listeEtudiant.add(new Etudiant("01-4", "00000044", "Rim"));
		
		Calcule c = new Calcule(mi2);
		//c.saisirLesNoteParEtudiant();
		c.saisirLesNoteParMatiere();

	}

}
