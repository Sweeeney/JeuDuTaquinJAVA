/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jeu.du.taquin.g2;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Scanner;

/**
 *
 * @author Cynthia
 */
public class Main {
//

    /*
        A NE PAS SUPPRIMER !
        ligne en commentaire dans files/nb project/ jfx_impl.xml  ligne 1234
     */
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        //Pour démarrer avec ou sans interface
        if (args.length == 0) {
            JeuDuTaquinG2.main(args);
            ////rajouter 
            //+ modifier dans build.xml
        } else {

            Scanner t = new Scanner(System.in);
            System.out.println("Choisis la taille de ton taquin : 3, 4 ou 5");
            int taille = t.nextInt();
            System.out.println(taille);
            Plateau plateau = new Plateau(taille);
            int n = 10; //pour l'essai
            plateau.melanger(n, taille);
            plateau.affichage();

            plateau.casesMalPlacees();
            plateau.deplacementIA();
            plateau.affichage();

            /*Scanner s = new Scanner(System.in);

        //CONNEXION A LA BDD
        ResultSet rs;
        String serverName = "localhost";
        String mydatabase = "java_taquin";
        String username = "root";//utilisateur à créer dans la base
        String password = "";//mot de passe de l'utilisateur
        BDD bdd = new BDD(serverName, mydatabase, username, password); //constructeur de la bdd

        bdd.creationTableFin();

        boolean finpartie = false;

        //*Scanner s = new Scanner(System.in);
        long time, temps;
        boolean depPossible = true;
        boolean verifMot = false;
        boolean verifDep = false;
        String deplacement = "";
        String nomjoueur = "";
        int affichageClass, classement;

        time = System.currentTimeMillis();
        while (!finpartie) {
            verifDep = false;
            verifMot = false;
            while (!verifMot && !verifDep) {
                System.out.println("Joueur, choisis la façon dont tu veux te déplacer : ");
                System.out.println("Haut, bas, droite ou gauche");
                deplacement = s.next();
                if ((deplacement.equalsIgnoreCase("haut") || deplacement.equalsIgnoreCase("bas")) || (deplacement.equalsIgnoreCase("gauche") || deplacement.equalsIgnoreCase("droite"))) {                    // Théoriquement, dès qu'il y en a un à vrai, ça sort de la boucle, non ?
                    verifMot = true;
                    depPossible = plateau.verifDeplacement(deplacement);

                    int depCompteur = 0; //compteur de déplacements

                    /*boolean depPossible = true;
            boolean verifMot = false;
            boolean verifDep = false;
            String deplacement = "";
            String nomjoueur = "";
            int affichageClass, classement;*/
                    while (!finpartie) {
                        verifDep = false;
                        verifMot = false;
                        while (!verifMot && !verifDep) {
                            System.out.println("Joueur, choisis la façon dont tu veux te déplacer : ");
                            System.out.println("Haut, bas, droite ou gauche");
                            deplacement = s.next();
                            if ((deplacement.equalsIgnoreCase("haut") || deplacement.equalsIgnoreCase("bas")) || (deplacement.equalsIgnoreCase("gauche") || deplacement.equalsIgnoreCase("droite"))) {                    // Théoriquement, dès qu'il y en a un à vrai, ça sort de la boucle, non ?
                                verifMot = true;
                                depPossible = plateau.verifDeplacement(deplacement);

                                if (!depPossible) {
                                    System.out.println("ERREUR, le déplacement ne peut pas être fait. Recommence");
                                    deplacement = s.next();
                                } else {
                                    verifDep = true;

                                    depCompteur = depCompteur + 1;
                                    plateau.affichage();
                                }

                            } else {
                                System.out.println("ERREUR, recommence");
                                deplacement = s.next();
                            }

                        }
                        finpartie = plateau.finPartie();
                    }
                    finpartie = plateau.finPartie();
                }
                temps = System.currentTimeMillis() - time;
                temps = temps;
                System.out.println("CONGRATULATIONS ! YOU WIN ! 😀");

                System.out.println("Choisis un pseudo : ");
                nomjoueur = s.next();
                //bdd.insertionJoueurFin(nomjoueur, 1000, depCompteur);
                System.out.println("Désires-tu voir le classement ?");
                System.out.println("Tape 1 pour OUI / Tape 2 pour NON : ");
                affichageClass = s.nextInt();
                while (affichageClass != 1 && affichageClass != 2) {
                    System.out.println("Erreur dans la réponse, recommences");
                    System.out.println("Tape 1 pour OUI / Tape 2 pour NON : ");
                    affichageClass = s.nextInt();
                }
                if (affichageClass == 1) {
                    System.out.println("De quelle manière souhaites-tu voir le classement ?");
                    System.out.println("Tape 1 pour voir le classement SELON LE TEMPS MIS POUR JOUER / Tape 2 pour voir le classement SELON LE NOMBRE DE DEPLACEMENTS / Tape 3 pour voir le classement DES DEUX MANIERES : ");
                    classement = s.nextInt();
                    while (classement != 1 && classement != 2 && classement != 3) {
                        System.out.println("Erreur dans la réponse, recommence");
                        System.out.println("Tape 1 pour voir le classement SELON LE TEMPS MIS POUR JOUER / Tape 2 pour voir le classement SELON LE NOMBRE DE DEPLACEMENTS : ");
                        classement = s.nextInt();
                    }
                    if (classement == 1) {
                        System.out.println("Classement en fonction du temps de jeu : ");
                        bdd.classementTemps();
                    } else if (classement == 2) {
                        System.out.println("Classement en fonction du nombre de déplacement : ");
                        bdd.classementDep();
                    } else if (classement == 3) {
                        System.out.println("Classement en fonction du nombre de déplacement : ");
                        bdd.classementDep();
                        System.out.println("Classement en fonction du temps de jeu : ");
                        bdd.classementTemps();
                    }
                } else if (affichageClass == 2) {
                    System.out.println("FIN DE LA PARTIE");
                }
            }
}
        }
    }*/
        }
    }
}
