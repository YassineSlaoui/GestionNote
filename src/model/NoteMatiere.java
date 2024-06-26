package model;

public class NoteMatiere {
    
	private int idNM;
	private Matiere matiere;
	private Note notes;

	public NoteMatiere(int id, Matiere matiere, Note notes) {
		this.idNM = id;
		this.matiere = matiere;
		this.notes = notes;
	}

	public NoteMatiere(Matiere matiere) {
		this.matiere = matiere;
	}

	public double moyenne() {
		return (notes.getExam() * matiere.getCoefExam() + notes.getTp() * matiere.getCoefTp()
				+ notes.getDs() * matiere.getCoefDs());

	}
	
	public void updateNotes(double noteDS, double noteTP, double noteEX) {
	    notes.setDs(noteDS);
	    notes.setTp(noteTP);
	    notes.setExam(noteEX);
	}

	public void setId(int id) {
		this.idNM = id;
	}
	
	public int getId() {
		return this.idNM;
	}
	
	public Matiere getMatiere() {
		return matiere;
	}

	public void setMatiere(Matiere matiere) {
		this.matiere = matiere;
	}

	public Note getNotes() {
		return notes;
	}

	public void setNotes(Note notes) {
		this.notes = notes;
	}

	@Override
	public String toString() {
		return "NoteMatiere [matiere=" + matiere.toString() + ", notes=" + notes.toString() + "]";
	}

	public static void main(String[] args) {
		Matiere m = new Matiere(0,"JAVA",0.7, 0, 0.3, 1);
		Note n = new Note(14, 0, 17);
		NoteMatiere note = new NoteMatiere(0,m, n);
		double x = note.moyenne();
		System.out.println(x);
	}

}
