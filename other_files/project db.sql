CREATE DATABASE `Gestion_Notes`;
USE `Gestion_Notes`;

CREATE TABLE Classe (
	idClasse INT NOT NULL AUTO_INCREMENT,
    nomClasse VARCHAR(45),
    PRIMARY KEY (idClasse));
    
CREATE TABLE Matiere (
	idMatiere INT NOT NULL AUTO_INCREMENT,
    nomMatiere VARCHAR(45),
    coefDS DOUBLE,
    coefTP DOUBLE,
    coefEX DOUBLE,
    coefMatiere DOUBLE,
    PRIMARY KEY (idMatiere));
    
CREATE TABLE `Admin` (
	idAdmin INT NOT NULL AUTO_INCREMENT,
    nomAdmin VARCHAR(45),
    cinAdmin VARCHAR(8),
	isSuper BOOLEAN,
	loginAdmin VARCHAR(45),
	pwdAdmin VARCHAR(45),
    UNIQUE (loginAdmin),
    PRIMARY KEY (idAdmin));
    
CREATE TABLE Enseignant (
	idEns INT NOT NULL AUTO_INCREMENT,
	nomEns VARCHAR(45),
	cinEns VARCHAR(8),
	loginEns VARCHAR(45),
	pwdEns VARCHAR(45),
    UNIQUE (loginEns),
    PRIMARY KEY (idEns));

CREATE TABLE Etudiant (
	idEtu INT NOT NULL AUTO_INCREMENT,
	nomEtu VARCHAR(45),
	cinEtu VARCHAR(8),
	idClasse INT,
	loginEtu VARCHAR(45),
	pwdEtu VARCHAR(45),
    PRIMARY KEY (idEtu),
    UNIQUE (loginEtu),
	FOREIGN KEY (idClasse) REFERENCES Classe (idClasse));
    
CREATE TABLE EnseignantMatiere (
	idEM INT NOT NULL AUTO_INCREMENT,
	idClasse INT NOT NULL,
    idMatiere INT NOT NULL,
    idEns INT NOT NULL,
    semestre INT NOT NULL,
    PRIMARY KEY (idEM),
    UNIQUE (idClasse,idMatiere,idEns,semestre),
    FOREIGN KEY (idClasse) REFERENCES Classe (idClasse),
	FOREIGN KEY (idMatiere) REFERENCES Matiere (idMatiere),
    FOREIGN KEY (idEns) REFERENCES Enseignant (idEns));

CREATE TABLE NoteMatiere (
	idNM INT NOT NULL AUTO_INCREMENT,
    idEtu INT NOT NULL,
    idMatiere INT NOT NULL,
    semestre INT NOT NULL,
    noteDS DOUBLE,
    noteTP DOUBLE,
    noteEX DOUBLE,
    PRIMARY KEY (idNM),
    FOREIGN KEY (idEtu) REFERENCES Etudiant (idEtu),
    FOREIGN KEY (idMatiere) REFERENCES Matiere (idMatiere));

CREATE TABLE Reclamation (
  idReclamation INT NOT NULL AUTO_INCREMENT,
  idSource INT NULL,
  typeUser VARCHAR(45) NULL,
  objet VARCHAR(45) NULL,
  contenu LONGTEXT NULL,
  PRIMARY KEY (idReclamation));
  