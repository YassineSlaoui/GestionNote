package gestion;

public class Admin extends Utilisateur {

    private int idAdmin;
    private String cin;
    private String name;
    private Boolean sup;

    public Admin(int id, String cin, String name, Boolean sup) {
	super();
	this.idAdmin = id;
	this.cin = cin;
	this.name = name;
	this.sup = sup;
    }

    public Admin(int id, String name, String cin, Boolean sup, String login, String pwd) {
	super(login, pwd);
	this.idAdmin = id;
	this.cin = cin;
	this.name = name;
	this.sup = sup;
    }

    public void update(int id, String name, String cin, Boolean sup, String login, String pwd) {
	super.setLogin(login);
	if (!pwd.equals(getMotDePasse()))
	    super.setMotDePasse(pwd);
	this.idAdmin = id;
	this.cin = cin;
	this.name = name;
	this.sup = sup;
    }

    public int getId() {
	return idAdmin;
    }

    public void setId(int id) {
	this.idAdmin = id;
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

    public Boolean isSuperAdmin() {
	return sup;
    }

    @Override
    public String toString() {
	return this.name;
    }

    public static void main(String[] args) {
	Admin ad = new Admin(1, "a", "TestAdmin", true);
	System.out.println(ad.toString());
    }

}
