package com.chihab_eddine98.eatit.model;

public class User
{

    private String nom;
    private String prenom;
    private String dateDeNaissance;
    private String mdp;


    public User(String nom, String prenom, String dateDeNaissance, String mdp) {
        this.nom = nom;
        this.prenom = prenom;
        this.dateDeNaissance = dateDeNaissance;
        this.mdp = mdp;
    }

    public User() {
    }

    public String getMdp() {
        return mdp;
    }
}
