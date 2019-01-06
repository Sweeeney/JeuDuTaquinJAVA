/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jeu.du.taquin.g2;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

/**
 *
 * @author Cynthia
 */
public class BDD {

    //VARIABLES GLOBALES
    private Connection con = null;
    private String query;
    private ResultSet rs;
    private String serverName, mydatabase, username, password;

    //CONSTRUCTEUR
    /**
     * Constructeur de la classe BDD
     *
     * @param sn le nom du serveur
     * @param dbn le nom de la base de données
     * @param u le nom d'utilisateur
     * @param p le mot de passe
     */
    public BDD(String sn, String dbn, String u, String p) {
        this.serverName = sn;
        this.mydatabase = dbn;
        this.username = u;
        this.password = p;
    }

    //METHODES
    /*
     * Ouvre la connexion avec la base de données.
     */
    private void openConnexion() {
        String connectUrl = "jdbc:mysql://" + this.serverName + "/" + this.mydatabase; // a JDBC url
        if (this.con != null) {
            this.closeConnexion();
        }
        try {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            this.con = DriverManager.getConnection(connectUrl, username, password);
            System.out.println("Database connection established.");
        } catch (ClassNotFoundException cnfe) {
            System.out.println("Cannot load db driver: com.mysql.jdbc.Driver");
            cnfe.printStackTrace();
        } catch (Exception e) {
            System.out.println("Erreur inattendue");
            e.printStackTrace();
        }
    }

    /**
     * Ferme la connexion avec la base de données.
     */
    private void closeConnexion() {
        if (this.con != null) {
            try {
                this.con.close();
                System.out.println("Database connection terminated.");
            } catch (Exception e) {
                /* ignore close errors */ }
        }
    }

    /**
     * Vérifie si la table 'classement' existe déjà dans la base de données.
     *
     * @return retourne vrai si la table existe, faux sinon
     */
    public boolean existeClassement() {
        try {
            this.openConnexion();
            System.out.println("openConnexion");
            Statement stmt = this.con.createStatement();

            this.query = "SELECT * FROM TABLE='classement'";

            //this.query = "SELECT * FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_NAME ='classement'";
            if (stmt.executeQuery(query) == null) {
                return false;
            } else {
                return true;
            }
        } catch (SQLException e) {
            this.closeConnexion();
            System.out.println("Erreur dans existeClassement");
            System.out.println(e);
            return false;
        }
    }

    /*
     * Appelle la méthode {@link #existeClassement() }
     * Si la table 'classement' n'existe pas encore dans la base de données,
     * cette méthode la créé.
     */
    public void creationTableFin() {
        try {
            boolean existe = existeClassement();
            if (existe) {
                System.out.println("La table 'classement' existe déjà !");
            } else {
                System.out.println("La table n'existe pas");
                this.openConnexion();
                Statement stmt = this.con.createStatement();
                this.query = "CREATE TABLE classement (nomJoueur VARCHAR(25), tempsDeJeu INTEGER, deplacement INTEGER)";
                stmt.executeUpdate(query);
                System.out.println("La table 'classement' a bien été créée");
                stmt.close();
            }
        } catch (SQLException e) {
            this.closeConnexion();
            System.out.println("Problème dans creationTableFin");
            System.out.println(e);
        }
    }


    /*
    public boolean existePartieEnCours() {
        try {
            this.openConnexion();
            Statement stmt = this.con.createStatement();
            this.query = "SELECT * FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_NAME ='partieEnCours'";
            if (stmt.executeQuery(query) == null) {
                return false;
            } else {
                return true;
            }
        } catch (SQLException e) {
            this.closeConnexion();
            System.out.println("Problème dans existePartieEnCours");
            System.out.println(e);
            return false;
        }
    }

    public void creationTableEnCours() {
        try {
            boolean existe = existePartieEnCours();
            if (existe) {
                System.out.println("La table 'partieEnCours' existe déjà !");
            } else {
                this.openConnexion();
                Statement stmt = this.con.createStatement();
                this.query = "CREATE TABLE partieEnCours (nomJoueur VARCHAR(25) PRIMARY KEY, tempsDeJeu INTEGER, deplacement INTEGER)";
                stmt.executeUpdate(query);
                System.out.println("La table 'partieEnCours' a bien été créée");
                stmt.close();
            }
        } catch (SQLException e) {
            this.closeConnexion();
            System.out.println("Problème dans creationTableEnCours");
            System.out.println(e);
        }
    }
*/
    /*
    //SI ON SOUHAITE AJOUTER LE NOM DU JOUEUR DES LE DEBUT DE LA PARTIE :
    public void insertionNomJoueur (String nomjoueur){
        try {
            this.openConnexion();
            Statement stmt = con.createStatement();
            this.query = "INSERT INTO joueurs (nomJoueur) VALUES ("+nomjoueur+")";
            stmt.execute(query);
            stmt.close();
        } catch (SQLException e) {
            this.closeConnexion();
            System.out.println("Problème");
        }
    }
    

    /**
     * Insère les caractéristiques d'un joueur dans la table 'classement' à la
     * fin d'une partie.
     *
     * @param nomjoueur le nom du joueur
     * @param tempsDeJeu le temps que le joueur a mis pour finir la partie
     * @param deplacement le nombre de déplacements que le joueur a fait pour
     * finir la partie
     */
    public void insertionJoueurFin(String nomjoueur, int tempsDeJeu, int deplacement) {
        System.out.println("Début insertionJoueurFin");
        try {
            this.openConnexion();
            Statement stmt = this.con.createStatement();
            this.query = "INSERT INTO classement VALUES ('" + nomjoueur + "'," + tempsDeJeu + "," + deplacement + ")";
            stmt.execute(query);
            System.out.println("insère la valeur");
            stmt.close();
        } catch (SQLException e) {
            this.closeConnexion();
            System.out.println("Problème dans insertionJoueurFin");
            System.out.println(e);
        }/////////////////////////////////////////////////////////////on ne peut pas mettre deux fois le même joueur (problème de clé primaire
    }
/*
    public void insertionJoueurEnCours(String nomjoueur, int tempsDeJeu, int deplacement) {
        try {
            this.openConnexion();
            Statement stmt = this.con.createStatement();
            this.query = "INSERT INTO partieEnCours VALUES (" + nomjoueur + "," + tempsDeJeu + "," + deplacement + ")";
            stmt.execute(query);
            stmt.close();
        } catch (SQLException e) {
            this.closeConnexion();
            System.out.println("Problème dans insertionJoueurEnCours");
            System.out.println(e);
        }
    }

    /**
     * Affiche dans la console le classement des parties gagnées en fonction du
     * temps mis pour y arriver.
     */
    public void classementTemps() {
        try {
            this.openConnexion();
            Statement stmt = this.con.createStatement();
            this.query = "SELECT * FROM classement ORDER BY tempsDeJeu";
            ArrayList<String> nomJoueur = new ArrayList<>();
            ArrayList<Integer> temps = new ArrayList<>();
            ArrayList<Integer> deplacement = new ArrayList<>();
            rs = stmt.executeQuery(query);
            while (rs.next()) {
                nomJoueur.add(rs.getString("nomJoueur"));
                temps.add(rs.getInt("tempsDeJeu"));
                deplacement.add(rs.getInt("deplacement"));
            }
            for (int i = 0; i < nomJoueur.size(); i++) {
                System.out.println("Joueur n° " + (i + 1) + " : " + nomJoueur.get(i) + " a joué pendant " + temps.get(i) + " secondes/millisecondes et a fait " + deplacement.get(i) + " déplacements.");
            }
            stmt.close();
        } catch (SQLException e) {
            this.closeConnexion();
            System.out.println("Problème dans classementTemps");
            System.out.println(e);
        }
    }

    /**
     * Retourne le classement des parties gagnées en fonction du temps mis pour
     * y arriver, afin de l'afficher plus tard dans l'interface graphique.
     *
     * @return retourne une liste contenant les listes des noms des joueurs, des
     * temps mis et du nombre de déplacements effectués
     */
    public ArrayList<ArrayList> classementTempsInterface() {
        try {
            this.openConnexion();
            Statement stmt = this.con.createStatement();
            this.query = "SELECT * FROM classement ORDER BY tempsDeJeu";
            ArrayList<String> nomJoueur = new ArrayList<>();
            ArrayList<Integer> temps = new ArrayList<>();
            ArrayList<Integer> deplacement = new ArrayList<>();
            rs = stmt.executeQuery(query);
            while (rs.next()) {
                nomJoueur.add(rs.getString("nomJoueur"));
                temps.add(rs.getInt("tempsDeJeu"));
                deplacement.add(rs.getInt("deplacement"));
            }
            stmt.close();
            ArrayList<ArrayList> liste = new ArrayList<>();
            liste.add(0, nomJoueur);
            liste.add(1, temps);
            liste.add(2, deplacement);
            return liste;
        } catch (SQLException e) {
            this.closeConnexion();
            System.out.println("Problème dans classementTempsInterface");
            System.out.println(e);
            return null;
        }
    }

    /**
     * Affiche dans la console le classement des parties gagnées en fonction du
     * nombre de déplacements effectués pour y arriver.
     */
    public void classementDep() {
        try {
            this.openConnexion();
            Statement stmt = this.con.createStatement();
            this.query = "SELECT * FROM classement ORDER BY deplacement";
            ArrayList<String> nomJoueur = new ArrayList<>();
            ArrayList<Integer> temps = new ArrayList<>();
            ArrayList<Integer> deplacement = new ArrayList<>();
            rs = stmt.executeQuery(query);
            while (rs.next()) {
                nomJoueur.add(rs.getString("nomJoueur"));
                temps.add(rs.getInt("tempsDeJeu"));
                deplacement.add(rs.getInt("deplacement"));
            }
            for (int i = 0; i < nomJoueur.size(); i++) {
                System.out.println("Joueur n° " + (i + 1) + " : " + nomJoueur.get(i) + " a joué pendant " + temps.get(i) + " secondes/millisecondes et a fait " + deplacement.get(i) + " déplacements.");
            }
            stmt.close();
        } catch (SQLException e) {
            this.closeConnexion();
            System.out.println("Problème dans classementDep");
            System.out.println(e);
        }
    }

    /**
     * Retourne le classement des parties gagnées en fonction du nombre de
     * déplacements effectués pour y arriver, afin de l'afficher plus tard dans
     * l'interface graphique.
     *
     * @return retourne une liste contenant les listes des noms des joueurs, des
     * temps mis et du nombre de déplacements effectués
     */
    public ArrayList<ArrayList> classementDepInterface() {
        try {
            this.openConnexion();
            Statement stmt = this.con.createStatement();
            this.query = "SELECT * FROM classement ORDER BY deplacement";
            ArrayList<String> nomJoueur = new ArrayList<>();
            ArrayList<Integer> temps = new ArrayList<>();
            ArrayList<Integer> deplacement = new ArrayList<>();
            rs = stmt.executeQuery(query);
            while (rs.next()) {
                nomJoueur.add(rs.getString("nomJoueur"));
                temps.add(rs.getInt("tempsDeJeu"));
                deplacement.add(rs.getInt("deplacement"));
            }
            stmt.close();
            ArrayList<ArrayList> liste = new ArrayList<>();
            liste.add(0, nomJoueur);
            liste.add(1, temps);
            liste.add(2, deplacement);
            return liste;
        } catch (SQLException e) {
            this.closeConnexion();
            System.out.println("Problème dans classementDepInterface");
            System.out.println(e);
            return null;
        }
    }
}
