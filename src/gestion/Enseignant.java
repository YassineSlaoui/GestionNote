package gestion;

public class Enseignant extends Utilisateur {
    private int idEns;
    private String cin;
    private String name;

    public Enseignant(int id, String cin, String name) {
	super();
	this.idEns = id;
	this.cin = cin;
	this.name = name;
    }

    public Enseignant(int id, String name, String cin, String login, String pwd) {
	super(login, pwd);
	this.idEns = id;
	this.cin = cin;
	this.name = name;
    }

    public void update(int idEns, String nom, String cin, String login, String pwd) {
	super.setLogin(login);
	if (!pwd.equals(getMotDePasse()))
	    super.setMotDePasse(pwd);
	this.idEns = idEns;
	this.cin = cin;
	this.name = nom;
    }

    public int getId() {
	return idEns;
    }

    public void setId(int id) {
	this.idEns = id;
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

    @Override
    public String toString() {
	return this.name;
    }
}
