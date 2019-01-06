/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jeu.du.taquin.g2;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;

/**
 *
 * @author schtt
 */
public class Plateau implements Serializable {

    // VARIABLES GLOBALES
    private int taille;
    private int[][] plateau;
    private String style = "1";
    private String nomJoueur = "bug";

    //CONSTRUCTEURS
    /**
     * Constructeur de la classe Plateau.
     *
     * @param taille taille du plateau
     */
    public Plateau(int taille) {
        this.taille = taille;
        this.plateau = new int[taille][taille];
        this.initPlateau();
    }

    /**
     * Constructeur de la classe Plateau.
     *
     * @param taille taille du plateau
     * @param s style du plateau
     * @param nom nom du joueur
     */
    public Plateau(int taille, String s, String nom) {
        this.taille = taille;
        this.plateau = new int[taille][taille];
        this.initPlateau();
        this.style = s;
        this.nomJoueur = nom;
    }

    /**
     * Constructeur de la classe plateau.
     *
     * @param p plateau du jeu
     */
    public Plateau(int t, int[][] p) {
        this.taille = t;
        this.plateau = p;
    }

    //GETTER ET SETTER
    /**
     * Getter du plateau.
     *
     * @return plateau
     */
    public int[][] getPlateau() {
        return this.plateau;
    }

    /**
     * Setter du plateau.
     *
     * @param p plateau
     */
    public void setPlateau(int[][] p) {
        this.plateau = p;
    }

    /**
     * Getter du nom du joueur.
     *
     * @return nom du joueur
     */
    public String getNomJoueur() {
        return this.nomJoueur;
    }

    /**
     * Setter du nom du joueur.
     *
     * @param s nom du joueur
     */
    public void setNomJoueur(String s) {
        this.nomJoueur = s;
    }

    /**
     * Getter du style.
     *
     * @return style du jeu
     */
    public String getStyle() {
        return this.style;
    }

    /**
     * Setter du style.
     *
     * @param s style du jeu
     */
    public void setStyle(String s) {
        this.style = s;
    }

    /**
     * Getter de la taille du jeu.
     *
     * @return taille du jeu
     */
    public int getTaille() {
        return this.taille;
    }

    /**
     * Setter de la taille du jeu.
     *
     * @param n taille du jeu
     */
    public void setTaille(int n) {
        this.taille = n;
    }

    //METHODES
    /**
     * Initialise le plateau (le remplit avec des entiers).
     */
    public void initPlateau() {
        int m = 1;
        for (int i = 0; i < this.taille; i++) {
            for (int j = 0; j < this.taille; j++) { // On parcourt le tableau pour pouvoir le remplir
                if (i == this.taille - 1 && j == this.taille - 1) {
                    this.plateau[i][j] = 0; //On suppose que la case vide sera la derniere case du plateau
                } else {
                    this.plateau[i][j] = m;
                    m = m + 1;
                }
            }
        }
    }

    /**
     * Echange deux cases du plateau.
     *
     * @param taille taille du plateau
     */
    public void echanger(int taille) {
        /* La méthode échange 2 cases du tableau.
        Je choisi 4 indices de manière aléatoire, pour déterminer 2 cases du tableau aléatoirement.
        Remarque : je multiplie par un double n pour avoir [0;n[ selon la "formule de base" : 
        int nombreAleatoire = Min + (int)(Math.random() * ((Max - Min) + 1));*/
        if (taille == 3) {
            int i = (int) (Math.random() * 3.0); //rend un nombre aléatoire : 0,1 ou 2 (Tableau 2D, numérotation qui commence à 0)
            int j = (int) (Math.random() * 3.0);
            int k = (int) (Math.random() * 3.0);
            int l = (int) (Math.random() * 3.0);
            //on procède à l'échange
            int temp = this.plateau[k][l];
            this.plateau[k][l] = this.plateau[i][j];
            this.plateau[i][j] = temp;
        } else if (taille == 4) {
            int i = (int) (Math.random() * 4.0); //rend un nombre aléatoire : 0,1,2 ou 3 (Tableau 2D, numérotation qui commence à 0)
            int j = (int) (Math.random() * 4.0);
            int k = (int) (Math.random() * 4.0);
            int l = (int) (Math.random() * 4.0);
            //on procède à l'échange
            int temp = this.plateau[k][l];
            this.plateau[k][l] = this.plateau[i][j];
            this.plateau[i][j] = temp;
        } else if (taille == 5) {
            int i = (int) (Math.random() * 5.0); //rend un nombre aléatoire : 0,1,2,3 ou 4 (Tableau 2D, numérotation qui commence à 0)
            int j = (int) (Math.random() * 5.0);
            int k = (int) (Math.random() * 5.0);
            int l = (int) (Math.random() * 5.0);
            //on procède à l'échange
            int temp = this.plateau[k][l];
            this.plateau[k][l] = this.plateau[i][j];
            this.plateau[i][j] = temp;
        } else {
            System.out.println("La taille rentrée en paramètre n'est pas possible.");
        }
    }

    /**
     * Appelle n fois {@link #echanger(int) } pour mélanger le plateau.
     *
     * @param n nombre de fois qu'on appelle la méthode {@link #echanger(int) }
     * @param taille taille du plateau
     */
    public void melanger(int n, int taille) {
        //on appelle n fois la méthode échanger pour mélanger le jeu
        for (int i = 0; i < n; i++) {
            this.echanger(taille);
        }
    }

    /**
     * Permet d'afficher le plateau dans la console.
     */
    public void affichage() {
        for (int ligne = 0; ligne < this.taille; ligne++) { //parcours la ligne du plateau
            System.out.print("[");
            for (int colonne = 0; colonne < this.taille; colonne++) { //parcours la colonne
                System.out.print(this.plateau[ligne][colonne]); //affichage de la case
                if (colonne < this.taille - 1) {
                    System.out.print(","); //séparation de chaque case par une virgule
                }
            }
            System.out.println("]"); //pour fermer l'affichage avec ]
        }
    }

    /**
     * Cherche la case vide et vérifie si le déplacement est possible. Si oui,
     * appelle la méthode {@link #bougerPion(String, int, int) }.
     *
     * @param s déplacement (bas, haut, droite ou gauche)
     * @return retourne vrai si le déplacement est possible, faux sinon
     */
    public boolean verifDeplacement(String s) {
        boolean trouve = false;
        int i = 0;
        int j = 0;
        int ligne = 0;
        int colonne = 0;
        //on cherche où se trouve la case vide sur le plateau
        while (trouve == false && i < this.taille) {
            j = 0;
            while (trouve == false && j < this.taille) {
                if (this.plateau[i][j] == 0) {
                    trouve = true;
                    ligne = i;
                    colonne = j;
                } else {
                    j = j + 1;
                }
            }
            i = i + 1;
        }
        if (trouve == false) {
            System.out.println("Erreur, la case vide n'a pas été trouvée. (méthode verifDeplacement, classe Plateau)");
            return false;
        }
        // La case vide (0) est donc à l'emplacement [i-1][j]
        /*
        i=i+1;
        //Pour des raisons de praticité dans la suite de la méthode, le i a été modifié afin que la case vide soit [i][j]
         */
        if (s.equalsIgnoreCase("haut") && ((ligne + 1) < this.taille && (ligne + 1) >= 0)) {
            /*si l'utilisateur veut déplacer une pièce vers le haut, il faut regarder si la case sous le trou existe
            La colonne est l même que précédemment, nous l'avons déjà explorée donc c'est ok. Il faut vérifier la ligne (ligne+1).
             */
            bougerPion(s, ligne, colonne);// on envoie à la prochaine méthode les coordonnées de la case vide
            return true;
        } else if (s.equalsIgnoreCase("bas") && ((ligne - 1) < this.taille && (ligne - 1) >= 0)) {
            /*si l'utilisateur veut déplacer une pièce vers le bas, il faut regarder si la case au dessus du trou existe
            La colonne est la même que précédemment, nous l'avons déjà explorée donc c'est ok. Il faut vérifier la ligne (ligne-1).
             */
            bougerPion(s, ligne, colonne);
            return true;
        } else if (s.equalsIgnoreCase("droite") && ((colonne - 1) < this.taille && (colonne - 1) >= 0)) {
            /*si l'utilisateur veut déplacer une pièce vers la droite, il faut regarder si la case à la gauche du trou existe
            La ligne est la même que précédemment, nous l'avons déjà explorée donc c'est ok. Il faut vérifier la colonne (colonne-1).
             */
            bougerPion(s, ligne, colonne);
            return true;
        } else if (s.equalsIgnoreCase("gauche") && ((colonne + 1) < this.taille && (colonne + 1) >= 0)) {
            /*si l'utilisateur veut déplacer une pièce vers la gauche, il faut regarder si la case à la droite du trou  existe
            La ligne est la même que précédemment, nous l'avons déjà explorée donc c'est ok. Il faut vérifier la colonne (colonne+1).
             */
            bougerPion(s, ligne, colonne);
            return true;
        } else {
            //si on ne rentre dans aucun des cas précédents, alors le déplacement est impossible
            System.out.println("Déplacement impossible.");
            return false;
        }
    }

    /**
     * Echange deux cases (bouge le pion).
     *
     * @param s déplacement (heut, bas, droite ou gauche)
     * @param i ligne de l'emplacement vide
     * @param j colonne de l'emplacement vide
     */
    public void bougerPion(String s, int i, int j) {
        //Rappel : i et j correspondent aux coordonnées de la case vide 
        //Le mouvement est possible, on a juste à échanger la case vide avec la case correspondante
        if (s.equalsIgnoreCase("haut")) {
            int temp = this.plateau[i][j];
            this.plateau[i][j] = this.plateau[i + 1][j];
            this.plateau[i + 1][j] = temp;
        } else if (s.equalsIgnoreCase("bas")) {
            int temp = this.plateau[i][j];
            this.plateau[i][j] = this.plateau[i - 1][j];
            this.plateau[i - 1][j] = temp;
        } else if (s.equalsIgnoreCase("droite")) {
            int temp = this.plateau[i][j];
            this.plateau[i][j] = this.plateau[i][j - 1];
            this.plateau[i][j - 1] = temp;
        } else if (s.equalsIgnoreCase("gauche")) {
            int temp = this.plateau[i][j];
            this.plateau[i][j] = this.plateau[i][j + 1];
            this.plateau[i][j + 1] = temp;
        } else {
            System.out.println("Problème, il ne fallait pas appeler la méthode bougerPion"); //à enlever quand sûres qu'il n'y a pas du tout de pbs
        }
    }

    /**
     * Vérifie si c'est la fin de la partie.
     *
     * @return retourne vrai si c'est la fin de la partie, faux sinon
     */
    public boolean finPartie() {
        int i = 0;
        int j;
        boolean verif = true;
        int ind = 1;
        while (i < this.taille && verif == true) {
            j = 0;
            while (j < this.taille && verif == true) {
                if ((i == (this.taille - 1) && j == (this.taille - 1)) && (this.plateau[i][j] == 0)) {
                    verif = true;
                    // si la dernière case n'est pas un 0 (vide) alors le plateau n'est pas bien rangé
                } else if (this.plateau[i][j] != ind) {
                    verif = false;
                }
                ind = ind + 1;
                j = j + 1;
            }

            i = i + 1;
        }
        return verif;
    }

    /**
     * Compte le nombre de cases mal placées sur le plateau
     *
     * @return le nombre de cases mal placées
     */
    public int casesMalPlacees() {
        Plateau newPlateau = new Plateau(this.taille);
        int[][] plateauRange = newPlateau.plateau;
        int nb = 0;
        int i;
        int j;
        for (i = 0; i < this.taille; i++) {
            for (j = 0; j < this.taille; j++) {
                if (plateauRange[i][j] != this.plateau[i][j]) {
                    nb = nb + 1;
                }
            }
        }
        System.out.println("Il y a " + nb + " cases mal placées.");
        return nb;
    }

    /**
     * Compte le nombre de cases bien placées sur le plateau
     *
     * @return le nombre de cases bien placées
     */
    public int casesBienPlacees() {
        Plateau newPlateau = new Plateau(this.taille);
        int[][] plateauRange = newPlateau.plateau;
        int nb = 0;
        int i;
        int j;
        for (i = 0; i < this.taille; i++) {
            for (j = 0; j < this.taille; j++) {
                if (plateauRange[i][j] == this.plateau[i][j]) {
                    nb = nb + 1;
                }
            }
        }
        System.out.println("Il y a " + nb + " cases bien placées.");
        return nb;
    }

    /**
     * Avec cette IA il y a un risque pour que des suites de déplacements se
     * répètent. Cette méthode regarde les déplacments possibles et choisit
     * celui qui minimise le nombre de cases mal placées.
     */
    public void deplacementIA() {
        ArrayList<ArrayList> deplacements = new ArrayList<>();
        Plateau p1 = new Plateau(this.taille, this.plateau);
        Plateau p2 = new Plateau(this.taille, this.plateau);
        Plateau p3 = new Plateau(this.taille, this.plateau);
        Plateau p4 = new Plateau(this.taille, this.plateau);

        deplacements.add(p1.deplacementIAbas(p1));
        deplacements.add(p3.deplacementIAdroite(p3));
        deplacements.add(p2.deplacementIAgauche(p2));
        deplacements.add(p4.deplacementIAhaut(p4));

        //on prend le mouvement qui permet d'avoir le moins de cases mal placées
        //ou les mouvements si plusieurs sont identiques
        int k;
        int min = 26; //nombre maximal de cases mal placées + 1
        ArrayList<ArrayList> temp = new ArrayList<>();
        for (k = 0; k < deplacements.size(); k++) {
            if (((Integer) (deplacements.get(k).get(1))).intValue() < min) {
                if (temp.size() != 0) {
                    temp.clear();
                }
                temp.add(deplacements.get(k));
                min = ((Integer) (deplacements.get(k).get(1))).intValue();
            } else if (((Integer) (deplacements.get(k).get(1))).intValue() == min) {
                //si les valeurs sont égales, il y a une chance sur deux pour que ça reste identique
                // et donc une chance sur deux pour que le minimum change
                int l = (int) (Math.random() * 2.0);
                if (l == 0) {
                    if (temp.size() != 0) {
                        temp.clear();
                    }
                    temp.add(deplacements.get(k));
                    min = ((Integer) (deplacements.get(k).get(1))).intValue();

                }
            }
        }
        if (temp.size() == 0) {
            System.out.println("Problème dans deplacementIA case Plateau, il n'y pas de déplacement dans la liste temp");
        } else if (temp.size() != 1) {
            System.out.println("Problème dans deplacementIA case Plateau, il n'y a pas qu'un déplacement dans la liste temp");
        } else {
            if (((Integer) (temp.get(0).get(0))).intValue() == 1) { //déplacement vers le bas
                System.out.println("Déplacement vers le bas choisi.");
                verifDeplacement("bas");
            } else if (((Integer) (temp.get(0).get(0))).intValue() == 2) { //déplacement vers le haut
                System.out.println("Déplacement vers le haut choisi.");
                verifDeplacement("haut");
            } else if (((Integer) (temp.get(0).get(0))).intValue() == 3) { //déplacement vers la droite
                System.out.println("Déplacement vers la droite choisi.");
                verifDeplacement("droite");
            } else if (((Integer) (temp.get(0).get(0))).intValue() == 4) { //déplacement vers la gauche
                System.out.println("Déplacement vers la gauche choisi.");
                verifDeplacement("gauche");
            }
        }

    }

    /**
     * Vérifie si un déplacement vers le bas est possible. Si oui, stocke le
     * nombre de cases mal placées qu'il y aura.
     *
     * @param pl Plateau à vérifier
     * @return liste contenant 1 (pour bas) et le nombre de cases mal placées
     */
    public ArrayList<Integer> deplacementIAbas(Plateau pl) {
        //int [][] clonePlateauBas = this.plateau.clone();
        Plateau p = new Plateau(pl.taille, pl.plateau);

        if (p.verifDeplacement("bas")) {
            int nb = p.casesMalPlacees();
            System.out.println("Avec un déplacement vers le bas il y aura " + nb + " cases mal placées");
            ArrayList<Integer> liste = new ArrayList<>();
            liste.add(Integer.valueOf(1));
            liste.add(Integer.valueOf(nb));
            /*on a une liste composée de "1" qui est le déplacement vers le bas 
            et "nb", le nombre de cases mal placées après le déplacement*/
            p.verifDeplacement("haut"); //permet de remettre dans le bon sens
            return liste;
        } else {
            System.out.println("Déplacement impossible dans suiteIA classe Plateau");
            ArrayList<Integer> liste = new ArrayList<>();
            liste.add(Integer.valueOf(5));
            liste.add(Integer.valueOf(90));
            return liste;
        }
    }

    /**
     * Vérifie si un déplacement vers le haut est possible. Si oui, stocke le
     * nombre de cases mal placées qu'il y aura.
     *
     * @param pl Plateau à vérifier
     * @return liste contenant 2 (pour haut) et le nombre de cases mal placées
     */
    public ArrayList<Integer> deplacementIAhaut(Plateau pl) {
        Plateau p = new Plateau(pl.taille, pl.plateau);
        if (p.verifDeplacement("haut")) {
            int nb = p.casesMalPlacees();
            System.out.println("Avec un déplacement vers le haut il y aura " + nb + " cases mal placées");
            ArrayList<Integer> liste = new ArrayList<>();
            liste.add(Integer.valueOf(2));
            liste.add(Integer.valueOf(nb));
            /*on a une liste composée de "2" qui est le déplacement vers le haut 
            et "nb", le nombre de cases mal placées après le déplacement*/
            p.verifDeplacement("bas"); //permet de remettre dans le bon sens
            return liste;
        } else {
            System.out.println("Déplacement impossible dans suiteIA classe Plateau");
            ArrayList<Integer> liste = new ArrayList<>();
            liste.add(Integer.valueOf(5));
            liste.add(Integer.valueOf(90));
            return liste;
        }
    }

    /**
     * Vérifie si un déplacement vers la droite est possible. Si oui, stocke le
     * nombre de cases mal placées qu'il y aura.
     *
     * @param pl Plateau à vérifier
     * @return liste contenant 3 (pour droite) et le nombre de cases mal placées
     */
    public ArrayList<Integer> deplacementIAdroite(Plateau pl) {
        Plateau p = new Plateau(pl.taille, pl.plateau);
        if (p.verifDeplacement("droite")) {
            int nb = p.casesMalPlacees();
            System.out.println("Avec un déplacement vers la droite il y aura " + nb + " cases mal placées");
            ArrayList<Integer> liste = new ArrayList<>();
            liste.add(Integer.valueOf(3));
            liste.add(Integer.valueOf(nb));
            /*on a une liste composée de "3" qui est le déplacement vers la droite 
            et "nb", le nombre de cases mal placées après le déplacement*/
            p.verifDeplacement("gauche"); //permet de remettre dans le bon sens
            return liste;
        } else {
            System.out.println("Déplacement impossible dans suiteIA classe Plateau");
            ArrayList<Integer> liste = new ArrayList<>();
            liste.add(Integer.valueOf(5));
            liste.add(Integer.valueOf(90));
            return liste;
        }
    }

    /**
     * Vérifie si un déplacement vers la gauche est possible. Si oui, stocke le
     * nombre de cases mal placées qu'il y aura.
     *
     * @param pl Plateau à vérifier
     * @return liste contenant 4 (pour gauche) et le nombre de cases mal placées
     */
    public ArrayList<Integer> deplacementIAgauche(Plateau pl) {
        Plateau p = new Plateau(pl.taille, pl.plateau);
        if (this.verifDeplacement("gauche")) {
            int nb = p.casesMalPlacees();
            System.out.println("Avec un déplacement vers la gauche il y aura " + nb + " cases mal placées");
            ArrayList<Integer> liste = new ArrayList<>();
            liste.add(Integer.valueOf(4));
            liste.add(Integer.valueOf(nb));
            /*on a une liste composée de "4" qui est le déplacement vers la gauche 
            et "nb", le nombre de cases mal placées après le déplacement*/
            p.verifDeplacement("droite"); //permet de remettre dans le bon sens
            return liste;
        } else {
            System.out.println("Déplacement impossible dans suiteIA classe Plateau");
            ArrayList<Integer> liste = new ArrayList<>();
            liste.add(Integer.valueOf(5));
            liste.add(Integer.valueOf(90));
            return liste;
        }
    }

    /*
    public void jeuIA(Plateau plateau) { //plateau du jeu en cours
    //deux noueuds n et n' : plutôt des plateaux de plateaux    
    Plateau n = new Plateau(this.taille);
        Plateau m = new Plateau(this.taille);
    
        ArrayList<Plateau> open = new ArrayList<>(); //tous les éléments non traités
        ArrayList<Plateau> closed = new ArrayList<>(); //tous les éléments traités ou en cours de traitement
    
        on insère le noeud initial dans open
        open.add(plateau);
    
    
        while (!finPartie()) {
            if (open.isEmpty()) {
                System.out.println("La liste est vide");
                //il n'y a pas d'éléments non traités
                break;
            }
            n = open.get(0); //récupère le premier élément de la liste TOUJOURS
            open.remove(0);
            closed.add(n);
            if (n.finPartie() == true) {

            }
        }
    }*/
}
