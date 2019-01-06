/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jeu.du.taquin.g2;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Optional;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;

/**
 *
 * @author ameli
 */
public class Controller extends Thread {

    //VARIABLES GLOBALES
    private Vue vue;
    private boolean appuieBouton;
    private Long chronoAvant;

    //CONSTRUCTEUR
    /**
     * Constructeur de la classe Controller. Permet de la lier à la vue.
     *
     * @param v vue à lier au controleur
     */
    public Controller(Vue v) {
        this.vue = v;
    }

    //METHODES
    /**
     * Exécute des instructions lorsque nous appuyons sur le bouton "Nouveau
     * jeu" : appelle la méthode {@link #creationInterfaceRenseignements() }
     * afin de créer l'interface qui demandera des renseignements au joueur puis
     * l'affiche.
     *
     * @param play bouton "Nouveau Jeu" sur lequel nous cliquons
     */
    public void buttonNewGame(Button play) {
        appuieBouton = false;
        play.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent me) {

                //sert à savoir si on lance un nouveau jeu ou si on en prend un ancien
                vue.setBooleanJeu(true);

                creationInterfaceRenseignements();

                vue.getStage().setScene(vue.getSceneRenseignements());
                vue.getRootRenseignements().setEffect(new GaussianBlur(100));
                Task task = new Task<Void>() { // on définit une tâche parallèle pour mettre à jour la vue
                    int objectif = 0;
                    int tmp = 50;

                    @Override
                    public Void call() throws Exception {
                        while (tmp != objectif) {
                            Platform.runLater(new Runnable() {
                                @Override
                                public void run() {
                                    tmp = tmp - 1;
                                    final GaussianBlur gaussianBlur = new GaussianBlur(tmp);
                                    vue.getRootRenseignements().setEffect(gaussianBlur);
                                }
                            }
                            );
                            Thread.sleep(10);
                        }

                        return null;
                    }

                };
                Thread th = new Thread(task);
                th.setDaemon(false);
                th.start();
            }
        }
        );
    }

    /**
     * Exécute des instructions lorsque nous appuyons sur le bouton "Recharger
     * la partie" : récupère le fichier sérialisé, le désérialise, remet les
     * éléments dans les variables, appelle les méthodes 
     * {@link #creationInterfaceGame() }
     * et {@link #deroulementJeu() }
     * puis affiche la scène.
     *
     * @param oldGame bouton "Recharger la partie" sur lequel nous cliquons
     */
    public void buttonOldGame(Button oldGame) {
        appuieBouton = false;
        oldGame.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent me) {
                ObjectOutputStream oos = null;
                ObjectInputStream ois = null;
                try {
                    final FileInputStream fichierIn = new FileInputStream("jeu.ser");
                    System.out.println("Le fichier existe ? : " + (new File("jeu.ser")).exists());
                    ois = new ObjectInputStream(fichierIn);
                    Plateau p;
                    Object o = ois.readObject();
                    if (o instanceof Plateau) {
                        vue.setPlateau(((Plateau) o).getTaille(), ((Plateau) o).getStyle(), ((Plateau) o).getNomJoueur(), ((Plateau) o).getPlateau());
                    } else {
                        System.out.println("Problème désérialisation du plateau");
                    }
                    vue.setLabelNbDeplacements(new Label(String.valueOf(ois.readInt())));
                    vue.setChrono(ois.readLong());
                    chronoAvant=vue.getChrono();
                    
                    creationInterfaceGame();
                    deroulementJeu();

                    //vue.setTempsDepart(System.currentTimeMillis());
                    //vue.setChrono(vue.getChrono() + (System.currentTimeMillis() - vue.getTempsDepart()));
                    vue.setBooleanJeu(false);

                    vue.getStage().setScene(vue.getSceneGame());

                    vue.getRootGame().setEffect(new GaussianBlur(100));
                    Task task = new Task<Void>() {
                        int objectif = 0;
                        int tmp = 50;

                        @Override
                        public Void call() throws Exception {
                            while (tmp != objectif) {
                                Platform.runLater(new Runnable() {
                                    @Override
                                    public void run() {
                                        tmp = tmp - 1;
                                        final GaussianBlur gaussianBlur = new GaussianBlur(tmp);
                                        vue.getRootGame().setEffect(gaussianBlur);
                                    }
                                }
                                );
                                Thread.sleep(10);
                            }

                            return null;
                        }

                    };
                    Thread th = new Thread(task);
                    th.setDaemon(false);
                    th.start();

                } catch (final java.io.IOException e) {
                    e.printStackTrace();
                } catch (final ClassNotFoundException e) {
                    e.printStackTrace();
                } finally {
                    try {
                        if (ois != null) {
                            ois.close();
                        }
                    } catch (final IOException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        });
    }

    /**
     * Initialise le plateau du départ : mélange le plateau, créé la GridPane et
     * appelle la méthode
     * {@link #affichageGrilleJeu(javafx.scene.layout.GridPane)}
     *
     * @return le GridPane qui a été créée
     */
    public GridPane initialiserPlateauDepart() {
        vue.getPlateau().melanger(500, vue.getPlateau().getTaille());
        GridPane grilleJeu = new GridPane();

        if (vue.getPlateau().getTaille() == 4) {
            grilleJeu.getColumnConstraints().setAll(
                    new ColumnConstraints(40, Region.USE_COMPUTED_SIZE, Region.USE_COMPUTED_SIZE),
                    new ColumnConstraints(40, Region.USE_COMPUTED_SIZE, Region.USE_COMPUTED_SIZE),
                    new ColumnConstraints(40, Region.USE_COMPUTED_SIZE, Region.USE_COMPUTED_SIZE),
                    new ColumnConstraints(40, Region.USE_COMPUTED_SIZE, Region.USE_COMPUTED_SIZE));
            grilleJeu.getRowConstraints().setAll(
                    new RowConstraints(40, Region.USE_COMPUTED_SIZE, Region.USE_COMPUTED_SIZE),
                    new RowConstraints(40, Region.USE_COMPUTED_SIZE, Region.USE_COMPUTED_SIZE),
                    new RowConstraints(40, Region.USE_COMPUTED_SIZE, Region.USE_COMPUTED_SIZE),
                    new RowConstraints(40, Region.USE_COMPUTED_SIZE, Region.USE_COMPUTED_SIZE));
        } else if (vue.getPlateau().getTaille() == 3) {
            grilleJeu.getColumnConstraints().setAll(
                    new ColumnConstraints(40, Region.USE_COMPUTED_SIZE, Region.USE_COMPUTED_SIZE),
                    new ColumnConstraints(40, Region.USE_COMPUTED_SIZE, Region.USE_COMPUTED_SIZE),
                    new ColumnConstraints(40, Region.USE_COMPUTED_SIZE, Region.USE_COMPUTED_SIZE));
            grilleJeu.getRowConstraints().setAll(
                    new RowConstraints(40, Region.USE_COMPUTED_SIZE, Region.USE_COMPUTED_SIZE),
                    new RowConstraints(40, Region.USE_COMPUTED_SIZE, Region.USE_COMPUTED_SIZE),
                    new RowConstraints(40, Region.USE_COMPUTED_SIZE, Region.USE_COMPUTED_SIZE));
        } else {
            grilleJeu.getColumnConstraints().setAll(
                    new ColumnConstraints(40, Region.USE_COMPUTED_SIZE, Region.USE_COMPUTED_SIZE),
                    new ColumnConstraints(40, Region.USE_COMPUTED_SIZE, Region.USE_COMPUTED_SIZE),
                    new ColumnConstraints(40, Region.USE_COMPUTED_SIZE, Region.USE_COMPUTED_SIZE),
                    new ColumnConstraints(40, Region.USE_COMPUTED_SIZE, Region.USE_COMPUTED_SIZE),
                    new ColumnConstraints(40, Region.USE_COMPUTED_SIZE, Region.USE_COMPUTED_SIZE));
            grilleJeu.getRowConstraints().setAll(
                    new RowConstraints(40, Region.USE_COMPUTED_SIZE, Region.USE_COMPUTED_SIZE),
                    new RowConstraints(40, Region.USE_COMPUTED_SIZE, Region.USE_COMPUTED_SIZE),
                    new RowConstraints(40, Region.USE_COMPUTED_SIZE, Region.USE_COMPUTED_SIZE),
                    new RowConstraints(40, Region.USE_COMPUTED_SIZE, Region.USE_COMPUTED_SIZE),
                    new RowConstraints(40, Region.USE_COMPUTED_SIZE, Region.USE_COMPUTED_SIZE));
        }
        affichageGrilleJeu(grilleJeu);
        return grilleJeu;
    }

    /**
     * Permet de mettre les "bonnes" images dans la GridPane en fonction du
     * chiffre de chaque case et du style choisi (et la taille du jeu).
     *
     * @param grille GridPAne dans laquelle on met les images
     */
    public void affichageGrilleJeu(GridPane grille) {
        ImageView caseGrille;
        int n;
        for (int i = 0; i < vue.getPlateau().getTaille(); i++) {
            for (int j = 0; j < vue.getPlateau().getTaille(); j++) {
                if (vue.getPlateau().getPlateau()[i][j] == 0) {
                    caseGrille = new ImageView(new Image(Vue.class
                            .getResourceAsStream("images/Image" + vue.getPlateau().getTaille() + "x" + vue.getPlateau().getTaille() + "-motif" + vue.getPlateau().getStyle() + ".vide.bmp")));
                    caseGrille.setPreserveRatio(true);
                    if (vue.getPlateau().getTaille() == 3) {
                        caseGrille.setFitHeight(110.);
                        caseGrille.setFitWidth(110.);
                    } else if (vue.getPlateau().getTaille() == 4) {
                        caseGrille.setFitHeight(90.);
                        caseGrille.setFitWidth(90.);
                    } else {
                        caseGrille.setFitHeight(60.);
                        caseGrille.setFitWidth(60.);
                    }
                    grille.add(caseGrille, j, i);
                } else {
                    n = vue.getPlateau().getPlateau()[i][j];
                    caseGrille
                            = new ImageView(new Image(Vue.class
                                    .getResourceAsStream("images/Image" + vue.getPlateau().getTaille() + "x" + vue.getPlateau().getTaille() + "-motif" + vue.getPlateau().getStyle() + "." + n + ".bmp")));
                    caseGrille.setPreserveRatio(true);
                    if (vue.getPlateau().getTaille() == 3) {
                        caseGrille.setFitHeight(110.);
                        caseGrille.setFitWidth(110.);
                    } else if (vue.getPlateau().getTaille() == 4) {
                        caseGrille.setFitHeight(90.);
                        caseGrille.setFitWidth(90.);
                    } else {
                        caseGrille.setFitHeight(60.);
                        caseGrille.setFitWidth(60.);
                    }

                    grille.add(caseGrille, j, i);
                }
            }
        }
        grille.setPrefSize(400, 400);
        grille.setPadding(new Insets(10, 10, 10, 10));
        grille.setAlignment(Pos.CENTER);
        grille.setGridLinesVisible(false);
    }

    /**
     * Exécute des instructions lorsque nous appuyons sur le bouton "Valider"
     * pour valider les renseignements : récupère la taille du jeu, le style et
     * le nom du joueur avant de les mettre dans les variables. Appelle ensuite
     * {@link #deroulementJeu()} et {@link #creationInterfaceGame() }
     * puis affiche la scène.
     *
     * @param valider bouton sur lequel nous cliquons
     * @param nomJoueur le champ où le joueur a indiqué son nom
     * @param tailleJeu les boutons radio pour choisir la taille du jeu
     * @param styleJeu les boutons radio pour choisir le style du jeu
     */
    public void buttonValider(Button valider, TextField nomJoueur, ToggleGroup tailleJeu, ToggleGroup styleJeu) {
        valider.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent me) {
                int taille;
                if (tailleJeu.getToggles().get(0).isSelected()) {
                    taille = 3;
                } else if (tailleJeu.getToggles().get(1).isSelected()) {
                    taille = 4;
                } else {
                    taille = 5;
                }
                String s;
                if (styleJeu.getToggles().get(0).isSelected()) {
                    s = "1";
                } else if (styleJeu.getToggles().get(1).isSelected()) {
                    s = "2";
                } else {
                    s = "3";
                }
                String nom = "PasDeNom";
                if ((nomJoueur.getText() != null && !nomJoueur.getText().isEmpty())) {
                    nom = nomJoueur.getText();
                }
                vue.getPlateau().setNomJoueur(nom);
                vue.setLabelName(new Label(nom));
                vue.setPlateau(taille, s, nom);

                deroulementJeu();
                creationInterfaceGame();

                //vue.setTempsDepart(System.currentTimeMillis());
                //vue.setChrono(System.currentTimeMillis() - vue.getTempsDepart());
                vue.setChrono(0L);

                vue.getStage().setScene(vue.getSceneGame());

                vue.getRootGame().setEffect(new GaussianBlur(100));
                Task task = new Task<Void>() {
                    int objectif = 0;
                    int tmp = 50;

                    @Override
                    public Void call() throws Exception {
                        while (tmp != objectif) {
                            Platform.runLater(new Runnable() {
                                @Override
                                public void run() {
                                    tmp = tmp - 1;
                                    final GaussianBlur gaussianBlur = new GaussianBlur(tmp);
                                    vue.getRootGame().setEffect(gaussianBlur);
                                }
                            }
                            );
                            Thread.sleep(10);
                        }

                        return null;
                    }

                };
                Thread th = new Thread(task);
                th.setDaemon(false);
                th.start();

            }
        });

    }

    /**
     * Crée l'interface de la partie en cours.
     */
    public void creationInterfaceGame() {

        //===========================================================
        //CREATION DE L'INTERFACE DU JEU (NOUVELLE PARTIE)
        //===========================================================
        //CREATION DE LA PARTIE HAUTE
        //partie en haut à gauche : titre, nom et nombre de déplacments
        VBox boxGauche = new VBox(2);

        HBox boxDep = new HBox(2);
        Label labelDep = new Label("Nombre de déplacements :");
        if (vue.getBooleanJeu()) {
            vue.setLabelNbDeplacements(new Label("0"));
        }
        labelDep.setFont(Font.font("Rockwell", FontWeight.NORMAL, 15));
        labelDep.setTextFill(Color.WHITE);
        vue.getLabelNbDeplacements().setFont(Font.font("Rockwell", FontWeight.NORMAL, 15));
        vue.getLabelNbDeplacements().setTextFill(Color.WHITE);
        boxDep.getChildren().addAll(labelDep, vue.getLabelNbDeplacements());

        Task task2 = new Task<Void>() {
            @Override
            public Void call() throws Exception {
                while (!(vue.getPlateau().finPartie())) {
                    while (boxDep.getChildren().size() != 0) {
                        boxDep.getChildren().remove(0);
                    }
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            while (boxDep.getChildren().size() != 0) {
                                boxDep.getChildren().remove(0);
                            }
                            boxDep.getChildren().addAll(labelDep, vue.getLabelNbDeplacements());

                        }
                    }
                    );
                    Thread.sleep(5);
                }
                return null;
            }
        };
        Thread th2 = new Thread(task2); // on crée un contrôleur de Thread
        th2.setDaemon(true); // le Thread s'exécutera en arrière-plan (démon informatique)
        th2.start();

        vue.getLabelName().setFont(Font.font("Rockwell", FontWeight.NORMAL, 15));
        vue.getLabelName().setTextFill(Color.WHITE);

        boxGauche.getChildren().addAll(vue.getLabelName(), boxDep);
        boxGauche.setAlignment(Pos.CENTER);
        boxGauche.setSpacing(5.);

        //partie en haut à droite : chronomètre
        VBox boxDroite = new VBox(1);
        ImageView time = new ImageView(new Image(Vue.class.getResourceAsStream("images/chrono.png")));
        time.setFitHeight(50.);
        time.setFitWidth(50.);

        if (vue.getBooleanJeu()) {
            //nouveau jeu
            vue.setChrono(0L);
        }

        vue.setLabelTime(new Label(String.valueOf(vue.getChrono() / 1000)));
        vue.getLabelTime().setTextFill(Color.WHITE);
        StackPane chrono = new StackPane(time, vue.getLabelTime());

        Task task = new Task<Void>() {
            @Override
            public Void call() throws Exception {

                while (!(vue.getPlateau().finPartie())) {
                    if (appuieBouton) {
                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                if (vue.getBooleanJeu()) {
                                    vue.setChrono(System.currentTimeMillis() - vue.getTempsDepart());
                                } else {
                                    vue.setChrono((chronoAvant + (System.currentTimeMillis() - vue.getTempsDepart())));
                                }

                                vue.setLabelTime(new Label(String.valueOf(vue.getChrono() / 1000)));

                                vue.getLabelTime().setTextFill(Color.WHITE);
                                StackPane chrono = new StackPane(time, vue.getLabelTime());
                                boxDroite.getChildren().addAll(chrono);
                                boxDroite.getChildren().remove(0);
                            }
                        }
                        );
                        Thread.sleep(1000);
                    }
                }
                return null;
            }
        };
        Thread th = new Thread(task);
        th.setDaemon(true);
        th.start();

        boxDroite.getChildren().addAll(chrono);
        boxDroite.setAlignment(Pos.CENTER);
        boxDroite.setSpacing(5.);

        //on met la partie droite et la partie gauche dans une HBox
        HBox boxDuHaut = new HBox(2);
        boxDuHaut.getChildren().addAll(boxGauche, boxDroite);
        boxDuHaut.setPadding(new Insets(5, 5, 5, 5));
        boxDuHaut.setSpacing(20.);
        boxDuHaut.setAlignment(Pos.CENTER);

        //CREATION DE LA PARTIE CENTRALE
        if (vue.getBooleanJeu()) {
            vue.setGrilleJeu(initialiserPlateauDepart());
        } else {
            vue.setGrilleJeu(new GridPane());
            affichageGrilleJeu(vue.getGrilleJeu());
        }

        labelDep.setUnderline(true); //test

        //CREATION DE LA PARTIE BASSE
        //création de la HBox du bas avec les 4 boutons
        HBox boxDuBas = new HBox(4);

        Button save = new Button();
        save.setText("Sauvegarder");
        save.setTextAlignment(TextAlignment.CENTER);
        save.autosize();
        save.setFont(Font.font("Rockwell", FontWeight.NORMAL, 15));

        Button help = new Button();
        help.setText("Aide");
        help.setTextAlignment(TextAlignment.CENTER);
        help.autosize();
        help.setFont(Font.font("Rockwell", FontWeight.NORMAL, 15));

        Button iAplayer = new Button();
        iAplayer.setText("Laisser jouer l'IA");
        iAplayer.setTextAlignment(TextAlignment.CENTER);
        iAplayer.autosize();
        iAplayer.setFont(Font.font("Rockwell", FontWeight.NORMAL, 15));

        Button stopIA = new Button();
        stopIA.setText("Interrompre l'IA");
        stopIA.setTextAlignment(TextAlignment.CENTER);
        stopIA.autosize();
        stopIA.setFont(Font.font("Rockwell", FontWeight.NORMAL, 15));

        boxDuBas.getChildren().addAll(save, help, iAplayer, stopIA);
        boxDuBas.setPadding(new Insets(5, 5, 5, 5));
        boxDuBas.setSpacing(10.);
        boxDuBas.setAlignment(Pos.CENTER);

        //REGROUPEMENT DES PARTIES HAUTE, CENTRALE ET BASSE
        VBox vBoxGlobale = new VBox(4);
        vBoxGlobale.getChildren().addAll(vue.getLabelTitre(), boxDuHaut, vue.getGrilleJeu(), boxDuBas);
        vBoxGlobale.setSpacing(10.);
        vBoxGlobale.setPrefSize(600, 600);
        vBoxGlobale.setAlignment(Pos.CENTER);

        vue.getRootGame().getChildren().add(vBoxGlobale);
        buttonSauvegarder(save);
        buttonAide(help);
        //FIN DE LA METHODE QUI INITALISE L'INTERFACE

    }

    /**
     * Créé l'interface de renseignements que le joueur doit remplir. Appelle
     * ensuite
     * {@link #buttonValider(Button, TextField, ToggleGroup, ToggleGroup)}
     */
    public void creationInterfaceRenseignements() {
        //==================================================
        //CREATION DE LA PAGE DE DEMANDE DE RENSEIGNEMENTS
        //==================================================
        VBox vChoix = new VBox(4);

        //création de la partie haute (nom du joueur)
        HBox joueur = new HBox(2);
        Label entrerNom = new Label("Entre ton nom :");
        entrerNom.setFont(Font.font("Rockwell", FontWeight.NORMAL, 15));
        entrerNom.setTextFill(Color.WHITE);
        TextField nomJoueur = new TextField();
        joueur.getChildren().addAll(entrerNom, nomJoueur);
        joueur.setAlignment(Pos.CENTER);
        joueur.setSpacing(5.);
        joueur.setPadding(new Insets(5, 20, 5, 20));

        //création de la partie du milieu
        HBox hBoxChoixTaille = new HBox(3);

        ToggleGroup tailleJeu = new ToggleGroup();
        RadioButton taille3x3 = new RadioButton("Jeu 3x3");
        RadioButton taille4x4 = new RadioButton("Jeu 4x4");
        RadioButton taille5x5 = new RadioButton("Jeu 5x5");
        taille3x3.setFont(Font.font("Rockwell", FontWeight.NORMAL, 15));
        taille3x3.setTextFill(Color.WHITE);
        taille4x4.setFont(Font.font("Rockwell", FontWeight.NORMAL, 15));
        taille4x4.setTextFill(Color.WHITE);
        taille5x5.setFont(Font.font("Rockwell", FontWeight.NORMAL, 15));
        taille5x5.setTextFill(Color.WHITE);
        taille3x3.setToggleGroup(tailleJeu);
        taille4x4.setToggleGroup(tailleJeu);
        taille4x4.setSelected(true);
        taille5x5.setToggleGroup(tailleJeu);
        hBoxChoixTaille.getChildren().addAll(taille3x3, taille4x4, taille5x5);
        hBoxChoixTaille.setAlignment(Pos.CENTER);
        hBoxChoixTaille.setPadding(new Insets(10, 10, 10, 10));
        hBoxChoixTaille.setSpacing(20);

        //création de la partie basse
        HBox hBoxChoixStyle = new HBox(2);

        VBox vBoxChoixStyle = new VBox(3);
        ToggleGroup styleJeu = new ToggleGroup();
        RadioButton motif1 = new RadioButton();
        RadioButton motif2 = new RadioButton();
        RadioButton motif3 = new RadioButton();
        motif1.setToggleGroup(styleJeu);
        motif2.setToggleGroup(styleJeu);
        motif3.setToggleGroup(styleJeu);
        motif1.setSelected(true);
        vBoxChoixStyle.getChildren().addAll(motif1, motif2, motif3);
        vBoxChoixStyle.setAlignment(Pos.CENTER);
        vBoxChoixStyle.setSpacing(70);

        VBox imageStyle = new VBox(3);
        ImageView style1 = new ImageView(new Image(Vue.class.getResourceAsStream("images/motif1.jpg")));
        style1.setFitWidth(150.);
        style1.setPreserveRatio(true);
        ImageView style2 = new ImageView(new Image(Vue.class.getResourceAsStream("images/motif2.jpg")));
        style2.setFitWidth(150.);
        style2.setPreserveRatio(true);
        ImageView style3 = new ImageView(new Image(Vue.class.getResourceAsStream("images/motif3.jpg")));
        style3.setFitWidth(150.);
        style3.setPreserveRatio(true);
        imageStyle.getChildren().addAll(style1, style2, style3);
        imageStyle.setAlignment(Pos.CENTER);
        imageStyle.setSpacing(5);

        hBoxChoixStyle.getChildren().addAll(vBoxChoixStyle, imageStyle);
        hBoxChoixStyle.setAlignment(Pos.CENTER);

        //création de la dernière partie pour le bouton valider
        Button valider = new Button();
        valider.setText("Valider");
        valider.setTextAlignment(TextAlignment.CENTER);
        valider.autosize();
        valider.setFont(Font.font("Rockwell", FontWeight.NORMAL, 15));

        vChoix.getChildren().addAll(joueur, hBoxChoixTaille, hBoxChoixStyle, valider);
        vChoix.setAlignment(Pos.CENTER);
        vChoix.setPrefSize(600, 500);
        vChoix.setPadding(new Insets(15, 30, 30, 30));
        vChoix.setSpacing(15);

        VBox avecTitre = new VBox(2);
        avecTitre.getChildren().addAll(vue.getLabelTitre(), vChoix);
        avecTitre.setPrefSize(600, 600);
        avecTitre.setSpacing(5);
        avecTitre.setAlignment(Pos.CENTER);

        vue.getRootRenseignements().getChildren().add(avecTitre);
        buttonValider(valider, nomJoueur, tailleJeu, styleJeu);
    }

    /**
     * Exécute des actions lorsque que nous appuyons sur une touche (q, s, d ou
     * z): augmente le nombre de déplacements, réactualise l'affichage et la
     * grille, vérifie si il s'agit de la fin de la partie (si oui, appelle
     * {@link #finPartieInterface()})
     */
    public void deroulementJeu() {
        vue.getRootGame().setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent ke) {
                if (!appuieBouton) {
                    vue.setTempsDepart(System.currentTimeMillis());
                }
                if (!vue.getPlateau().finPartie()) {
                    appuieBouton = true;
                    String touche = ke.getText();
                    if (!vue.getPlateau().finPartie()) {
                        if ((touche.compareToIgnoreCase("q") == 0)) { //déplacement à gauche
                            if (vue.getPlateau().verifDeplacement("gauche")) {
                                vue.setLabelNbDeplacements(new Label(String.valueOf(Integer.parseInt(vue.getLabelNbDeplacements().getText()) + 1)));

                                vue.getRootGame().getChildren().remove(0);

                                //ajouter 1 au nombre de déplacements
                                //réactualiser le Label
                                rechargeInterfaceGame();
                                affichageGrilleJeu(vue.getGrilleJeu());

                                if (vue.getPlateau().finPartie()) {
                                    System.out.println("Fin partie");
                                    finPartieInterface();
                                }
                            }
                        } else if ((touche.compareToIgnoreCase("s") == 0)) { //déplacement en bas
                            if (vue.getPlateau().verifDeplacement("bas")) {
                                vue.setLabelNbDeplacements(new Label(String.valueOf(Integer.parseInt(vue.getLabelNbDeplacements().getText()) + 1)));

                                vue.getRootGame().getChildren().remove(0);

                                rechargeInterfaceGame();
                                affichageGrilleJeu(vue.getGrilleJeu());
                                if (vue.getPlateau().finPartie()) {
                                    System.out.println("Fin partie");
                                    finPartieInterface();
                                }
                            }
                        } else if ((touche.compareToIgnoreCase("d") == 0)) { //déplacement à droite
                            if (vue.getPlateau().verifDeplacement("droite")) {
                                vue.setLabelNbDeplacements(new Label(String.valueOf(Integer.parseInt(vue.getLabelNbDeplacements().getText()) + 1)));

                                vue.getRootGame().getChildren().remove(0);

                                rechargeInterfaceGame();
                                affichageGrilleJeu(vue.getGrilleJeu());
                                if (vue.getPlateau().finPartie()) {
                                    System.out.println("Fin partie");
                                    finPartieInterface();
                                }
                            }
                        } else if ((touche.compareToIgnoreCase("z") == 0)) { //déplacement en haut
                            if (vue.getPlateau().verifDeplacement("haut")) {
                                vue.setLabelNbDeplacements(new Label(String.valueOf(Integer.parseInt(vue.getLabelNbDeplacements().getText()) + 1)));

                                vue.getRootGame().getChildren().remove(0);

                                rechargeInterfaceGame();
                                affichageGrilleJeu(vue.getGrilleJeu());
                                if (vue.getPlateau().finPartie()) {
                                    System.out.println("Fin partie");
                                    finPartieInterface();
                                }
                            }
                        }
                    }
                } else {
                    System.out.println("Fin partie");
                    finPartieInterface();
                }
            }
        });
    }

    /**
     * Permet de recharger l'interface du jeu (surtout pour mettre à jour le
     * nombre de déplacements)
     */
    public void rechargeInterfaceGame() {
        //CREATION DE LA PARTIE HAUTE
        //partie en haut à gauche : titre et nombre de déplacments
        VBox boxGauche = new VBox(2);
        HBox boxDep = new HBox(2);
        Label labelDep = new Label("Nombre de déplacements :");
        labelDep.setFont(Font.font("Rockwell", FontWeight.NORMAL, 15));
        labelDep.setTextFill(Color.WHITE);
        vue.getLabelNbDeplacements().setFont(Font.font("Rockwell", FontWeight.NORMAL, 15));
        vue.getLabelNbDeplacements().setTextFill(Color.WHITE);
        boxDep.getChildren().addAll(labelDep, vue.getLabelNbDeplacements());
        Task task2 = new Task<Void>() {
            @Override
            public Void call() throws Exception {
                while (!(vue.getPlateau().finPartie())) {
                    while (boxDep.getChildren().size() != 0) {
                        boxDep.getChildren().remove(0);
                    }
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            while (boxDep.getChildren().size() != 0) {
                                boxDep.getChildren().remove(0);
                            }
                            boxDep.getChildren().addAll(labelDep, vue.getLabelNbDeplacements());
                        }
                    }
                    );
                    Thread.sleep(5);
                }
                return null;
            }
        };
        Thread th2 = new Thread(task2);
        th2.setDaemon(true);
        th2.start();
        vue.getLabelName().setFont(Font.font("Rockwell", FontWeight.NORMAL, 15));
        vue.getLabelName().setTextFill(Color.WHITE);
        boxGauche.getChildren().addAll(vue.getLabelName(), boxDep);
        boxGauche.setAlignment(Pos.CENTER);
        boxGauche.setSpacing(5.);
        //partie en haut à droite : chronomètre et nom du joueur
        VBox boxDroite = new VBox(1);
        ImageView time = new ImageView(new Image(Vue.class
                .getResourceAsStream("images/chrono.png")));
        time.setFitHeight(50.);
        time.setFitWidth(50.);
        if (vue.getBooleanJeu()) {
            vue.setChrono(System.currentTimeMillis() - vue.getTempsDepart());
        } else {
            vue.setChrono((chronoAvant + (System.currentTimeMillis() - vue.getTempsDepart())));
        }
        vue.setLabelTime(new Label(String.valueOf(vue.getChrono() / 1000)));
        vue.getLabelTime().setTextFill(Color.WHITE);
        StackPane chrono = new StackPane(time, vue.getLabelTime());
        Task task = new Task<Void>() {
            @Override
            public Void call() throws Exception {
                while (!(vue.getPlateau().finPartie())) {
                    if (appuieBouton) {
                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                if (vue.getBooleanJeu()) {
                                    vue.setChrono(System.currentTimeMillis() - vue.getTempsDepart());
                                } else {
                                    vue.setChrono((chronoAvant + (System.currentTimeMillis() - vue.getTempsDepart())));
                                }
                                vue.setLabelTime(new Label(String.valueOf(vue.getChrono() / 1000)));
                                vue.getLabelTime().setTextFill(Color.WHITE);
                                StackPane chrono = new StackPane(time, vue.getLabelTime());
                                boxDroite.getChildren().addAll(chrono);
                                boxDroite.getChildren().remove(0);
                            }
                        }
                        );
                        Thread.sleep(1000);
                    }
                }
                return null;
            }
        };
        Thread th = new Thread(task);
        th.setDaemon(true);
        th.start();
        boxDroite.getChildren().addAll(chrono);
        boxDroite.setAlignment(Pos.CENTER);
        boxDroite.setSpacing(5.);
        //on met la partie droite et la partie gauche dans une HBox
        HBox boxDuHaut = new HBox(2);
        boxDuHaut.getChildren().addAll(boxGauche, boxDroite);
        boxDuHaut.setPadding(new Insets(5, 5, 5, 5));
        boxDuHaut.setSpacing(20.);
        boxDuHaut.setAlignment(Pos.CENTER);
        //CREATION DE LA PARTIE BASSE
        //création de la HBox du bas avec les 4 boutons
        HBox boxDuBas = new HBox(4);
        Button save = new Button();
        save.setText("Sauvegarder");
        save.setTextAlignment(TextAlignment.CENTER);
        save.autosize();
        save.setFont(Font.font("Rockwell", FontWeight.NORMAL, 15));
        Button help = new Button();
        help.setText("Aide");
        help.setTextAlignment(TextAlignment.CENTER);
        help.autosize();
        help.setFont(Font.font("Rockwell", FontWeight.NORMAL, 15));
        Button iAplayer = new Button();
        iAplayer.setText("Laisser jouer l'IA");
        iAplayer.setTextAlignment(TextAlignment.CENTER);
        iAplayer.autosize();
        iAplayer.setFont(Font.font("Rockwell", FontWeight.NORMAL, 15));
        Button stopIA = new Button();
        stopIA.setText("Interrompre l'IA");
        stopIA.setTextAlignment(TextAlignment.CENTER);
        stopIA.autosize();
        stopIA.setFont(Font.font("Rockwell", FontWeight.NORMAL, 15));
        boxDuBas.getChildren().addAll(save, help, iAplayer, stopIA);
        boxDuBas.setPadding(new Insets(5, 5, 5, 5));
        boxDuBas.setSpacing(10.);
        boxDuBas.setAlignment(Pos.CENTER);
        //REGROUPEMENT DES PARTIES HAUTE, CENTRALE ET BASSE
        VBox vBoxGlobale = new VBox(4);
        vBoxGlobale.getChildren().addAll(vue.getLabelTitre(), boxDuHaut, vue.getGrilleJeu(), boxDuBas);
        vBoxGlobale.setSpacing(10.);
        vBoxGlobale.setPrefSize(600, 600);
        vBoxGlobale.setAlignment(Pos.CENTER);
        ImageView image = new ImageView(new Image(Vue.class.getResourceAsStream("images/yoda_sombre.jpg")));
        image.setPreserveRatio(true);
        image.setFitHeight(800);
        image.setFitWidth(800);
        vue.getRootGame().getChildren().setAll(image);
        vue.getRootGame().getChildren().add(vBoxGlobale);
        buttonSauvegarder(save);
        buttonAide(help);
        //FIN DE LA METHODE QUI RECHARGE L'INTERFACE
    }

    /**
     * Créé l'interface du classement.
     */
    public void finPartieInterfaceClassement(Button b) {
        b.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent me) {
                vue.getBDD().creationTableFin();
                vue.getBDD().insertionJoueurFin(vue.getLabelName().getText(), vue.getChrono().intValue(), Integer.parseInt(vue.getLabelNbDeplacements().getText()));

                HBox classement = new HBox(2);

                Button classementDeplacement = new Button();
                classementDeplacement.setText("Trier par déplacements");
                classementDeplacement.setTextAlignment(TextAlignment.CENTER);
                classementDeplacement.autosize();
                classementDeplacement.setFont(Font.font("Rockwell", FontWeight.NORMAL, 15));

                Button classementTemps = new Button();
                classementTemps.setText("Trier par temps");
                classementTemps.setTextAlignment(TextAlignment.CENTER);
                classementTemps.autosize();
                classementTemps.setFont(Font.font("Rockwell", FontWeight.NORMAL, 15));

                buttonClassementTemps(classementTemps);
                buttonClassementDeplacement(classementDeplacement);

                classement.getChildren().addAll(classementDeplacement, classementTemps);
                classement.setPadding(new Insets(10, 5, 5, 30));
                classement.setAlignment(Pos.CENTER);
                classement.setSpacing(100.);

                VBox vBoxFinPartie = new VBox();
                vBoxFinPartie.getChildren().addAll(vue.getLabelTitre(), classement);
                vBoxFinPartie.setAlignment(Pos.CENTER);
                vBoxFinPartie.setSpacing(10);
                vBoxFinPartie.setPadding(new Insets(5, 5, 5, 30));

                vue.getRootClassement().getChildren().add(vBoxFinPartie);

                vue.getStage().setScene(vue.getSceneClassement());
                vue.getRootClassement().setEffect(new GaussianBlur(100));
                Task task = new Task<Void>() {
                    int objectif = 0;
                    int tmp = 50;

                    @Override
                    public Void call() throws Exception {
                        while (tmp != objectif) {
                            Platform.runLater(new Runnable() {
                                @Override
                                public void run() {
                                    tmp = tmp - 1;
                                    final GaussianBlur gaussianBlur = new GaussianBlur(tmp);
                                    vue.getRootClassement().setEffect(gaussianBlur);
                                }
                            }
                            );
                            Thread.sleep(10);
                        }
                        return null;
                    }
                };
                Thread th = new Thread(task);
                th.setDaemon(false);
                th.start();
            }
        });
    }

    /**
     * Exécute des actions si nous cliquons sur le bouton "Sauvegarder" :
     * sérialise la partie et affiche un message lorsque cela est fait.
     *
     * @param sauvegarder bouton sur lequel nous cliquons
     */
    public void buttonSauvegarder(Button sauvegarder) {
        sauvegarder.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent me) {
                ObjectOutputStream oos = null;

                try {
                    final FileOutputStream fichier = new FileOutputStream("jeu.ser");
                    oos = new ObjectOutputStream(fichier);
                    oos.writeObject(vue.getPlateau());
                    oos.writeInt(Integer.valueOf(vue.getLabelNbDeplacements().getText()));
                    oos.writeLong(vue.getChrono());
                    //oos.writeChars(/*AJOUTER ICI*/);
                    oos.flush();
                    Alert alert = new Alert(AlertType.INFORMATION);
                    alert.setTitle("Information");
                    alert.setHeaderText(null);
                    alert.setContentText("Votre partie a bien été enregistrée. A bientôt.");
                    //alert.showAndWait();
                    Optional<ButtonType> result = alert.showAndWait();
                    if (result.isPresent()) {
                        System.out.println("Appuie sur OK");
                        System.exit(0);
                    } else {
                        System.out.println("Boite de dialogue fermée (appuie sur la croix)");
                        System.exit(0);
                    }
                } catch (final java.io.IOException e) {
                    e.printStackTrace();
                } finally {
                    try {
                        if (oos != null) {
                            oos.flush();
                            oos.close();
                        }
                    } catch (final IOException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        }
        );
    }

    /**
     * Affiche le classement et le range par temps lorsque nous cliquons sur le
     * bouton "Trier par temps".
     *
     * @param temps bouton sur lequel nous cliquons
     */
    public void buttonClassementTemps(Button temps) {
        temps.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent me) {
                ArrayList<ArrayList> liste = vue.getBDD().classementTempsInterface();
                VBox classement = new VBox();
                HBox titre = new HBox(3);
                Label titreJoueur = new Label("Nom");
                titreJoueur.setFont(Font.font("Rockwell", FontWeight.NORMAL, 25));
                titreJoueur.setTextFill(Color.WHITE);
                Label titreTemps = new Label("Temps");
                titreTemps.setFont(Font.font("Rockwell", FontWeight.NORMAL, 25));
                titreTemps.setTextFill(Color.WHITE);
                Label titreDep = new Label("Déplacements");
                titreDep.setFont(Font.font("Rockwell", FontWeight.NORMAL, 25));
                titreDep.setTextFill(Color.WHITE);
                titre.getChildren().addAll(titreJoueur, titreTemps, titreDep);
                for (int i = 0; i < liste.get(0).size(); i++) {
                    System.out.println("Joueur n° " + (i + 1) + " : " + liste.get(0).get(i) + " a joué pendant " + liste.get(1).get(i) + " secondes/millisecondes et a fait " + liste.get(2).get(i) + " déplacements.");
                    HBox ligneJoueur = new HBox(3);
                    ligneJoueur.setPadding(new Insets(5, 5, 5, 5));
                    ligneJoueur.setSpacing(110.);
                    ligneJoueur.setAlignment(Pos.CENTER);
                    Label joueur = new Label((String) liste.get(0).get(i));
                    joueur.setFont(Font.font("Rockwell", FontWeight.NORMAL, 15));
                    joueur.setTextFill(Color.WHITE);
                    Label temps = new Label(String.valueOf(liste.get(1).get(i)));
                    temps.setFont(Font.font("Rockwell", FontWeight.NORMAL, 15));
                    temps.setTextFill(Color.WHITE);
                    Label deplacement = new Label(String.valueOf(liste.get(2).get(i)));
                    deplacement.setFont(Font.font("Rockwell", FontWeight.NORMAL, 15));
                    deplacement.setTextFill(Color.WHITE);
                    ligneJoueur.getChildren().addAll(new Label(String.valueOf(i + 1)), joueur, temps, deplacement);
                    classement.getChildren().add(ligneJoueur);
                }
                titre.setPadding(new Insets(5, 5, 5, 100));
                titre.setSpacing(70.);
                titre.setAlignment(Pos.CENTER);

                classement.setPadding(new Insets(5, 100, 5, 0));
                classement.setSpacing(5.);
                classement.setAlignment(Pos.CENTER_LEFT);

                VBox total = new VBox(2);
                total.setPadding(new Insets(5, 5, 5, 5));
                total.setSpacing(5.);
                total.setAlignment(Pos.CENTER);
                total.getChildren().addAll(titre, classement);

                HBox hBoxclassement = new HBox(2);

                Button classementDeplacement = new Button();
                classementDeplacement.setText("Trier par déplacements");
                classementDeplacement.setTextAlignment(TextAlignment.CENTER);
                classementDeplacement.autosize();
                classementDeplacement.setFont(Font.font("Rockwell", FontWeight.NORMAL, 15));

                Button classementTemps = new Button();
                classementTemps.setText("Trier par temps");
                classementTemps.setTextAlignment(TextAlignment.CENTER);
                classementTemps.autosize();
                classementTemps.setFont(Font.font("Rockwell", FontWeight.NORMAL, 15));

                buttonClassementTemps(classementTemps);
                buttonClassementDeplacement(classementDeplacement);

                hBoxclassement.getChildren().addAll(classementDeplacement, classementTemps);
                hBoxclassement.setAlignment(Pos.CENTER);
                hBoxclassement.setSpacing(130.);

                VBox boxTotaleClassement = new VBox();
                boxTotaleClassement.setPadding(new Insets(10, 5, 5, 5));
                boxTotaleClassement.getChildren().addAll(vue.getLabelTitre(), hBoxclassement, total);
                boxTotaleClassement.setSpacing(10);
                boxTotaleClassement.setAlignment(Pos.CENTER);

                vue.getRootClassement().getChildren().remove(0);
                vue.getRootClassement().getChildren().addAll(boxTotaleClassement);
                vue.getStage().setScene(vue.getSceneClassement());

            }
        });

    }

    /**
     * Affiche le classement et le range par déplacements lorsque nous cliquons
     * sur le bouton "Trier par déplacements".
     *
     * @param dep bouton sur lequel nous cliquons
     */
    public void buttonClassementDeplacement(Button dep) {
        dep.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent me) {
                // affichage par déplacements
                ArrayList<ArrayList> liste = vue.getBDD().classementDepInterface();
                VBox classement = new VBox();
                HBox titre = new HBox(3);
                Label titreJoueur = new Label("Nom");
                titreJoueur.setFont(Font.font("Rockwell", FontWeight.NORMAL, 25));
                titreJoueur.setTextFill(Color.WHITE);
                Label titreTemps = new Label("Temps");
                titreTemps.setFont(Font.font("Rockwell", FontWeight.NORMAL, 25));
                titreTemps.setTextFill(Color.WHITE);
                Label titreDep = new Label("Déplacements");
                titreDep.setFont(Font.font("Rockwell", FontWeight.NORMAL, 25));
                titreDep.setTextFill(Color.WHITE);
                titre.getChildren().addAll(titreJoueur, titreTemps, titreDep);
                for (int i = 0; i < liste.get(0).size(); i++) {
                    System.out.println("Joueur n° " + (i + 1) + " : " + liste.get(0).get(i) + " a joué pendant " + liste.get(1).get(i) + " secondes/millisecondes et a fait " + liste.get(2).get(i) + " déplacements.");
                    HBox ligneJoueur = new HBox(3);
                    ligneJoueur.setPadding(new Insets(5, 5, 5, 5));
                    ligneJoueur.setSpacing(110.);
                    ligneJoueur.setAlignment(Pos.CENTER);
                    Label joueur = new Label((String) liste.get(0).get(i));
                    joueur.setFont(Font.font("Rockwell", FontWeight.NORMAL, 15));
                    joueur.setTextFill(Color.WHITE);
                    Label temps = new Label(String.valueOf(liste.get(1).get(i)));
                    temps.setFont(Font.font("Rockwell", FontWeight.NORMAL, 15));
                    temps.setTextFill(Color.WHITE);
                    Label deplacement = new Label(String.valueOf(liste.get(2).get(i)));
                    deplacement.setFont(Font.font("Rockwell", FontWeight.NORMAL, 15));
                    deplacement.setTextFill(Color.WHITE);
                    ligneJoueur.getChildren().addAll(new Label(String.valueOf(i + 1)), joueur, temps, deplacement);
                    classement.getChildren().add(ligneJoueur);
                }
                titre.setPadding(new Insets(5, 5, 5, 100));
                titre.setSpacing(70.);
                titre.setAlignment(Pos.CENTER);

                classement.setPadding(new Insets(5, 100, 5, 0));
                classement.setSpacing(5.);
                classement.setAlignment(Pos.CENTER_LEFT);

                VBox total = new VBox(2);
                total.setPadding(new Insets(5, 5, 5, 5));
                total.setSpacing(5.);
                total.setAlignment(Pos.CENTER);
                total.getChildren().addAll(titre, classement);

                HBox hBoxclassement = new HBox(2);

                Button classementDeplacement = new Button();
                classementDeplacement.setText("Trier par déplacements");
                classementDeplacement.setTextAlignment(TextAlignment.CENTER);
                classementDeplacement.autosize();
                classementDeplacement.setFont(Font.font("Rockwell", FontWeight.NORMAL, 15));

                Button classementTemps = new Button();
                classementTemps.setText("Trier par temps");
                classementTemps.setTextAlignment(TextAlignment.CENTER);
                classementTemps.autosize();
                classementTemps.setFont(Font.font("Rockwell", FontWeight.NORMAL, 15));

                buttonClassementTemps(classementTemps);
                buttonClassementDeplacement(classementDeplacement);

                hBoxclassement.getChildren().addAll(classementDeplacement, classementTemps);
                hBoxclassement.setAlignment(Pos.CENTER);
                hBoxclassement.setSpacing(130.);

                VBox boxTotaleClassement = new VBox();
                boxTotaleClassement.setPadding(new Insets(10, 5, 5, 5));
                boxTotaleClassement.getChildren().addAll(vue.getLabelTitre(), hBoxclassement, total);
                boxTotaleClassement.setSpacing(10);
                boxTotaleClassement.setAlignment(Pos.CENTER);

                vue.getRootClassement().getChildren().remove(0);
                vue.getRootClassement().getChildren().addAll(boxTotaleClassement);
                vue.getStage().setScene(vue.getSceneClassement());

            }
        });

    }

    /**
     * Exécute des actions si nous cliquons sur le bouton "Aide" : appelle l'IA
     * pour qu'elle effectue un déplacement et met à jour la scène.
     *
     * @param help bouton sur lequel nous cliquons.
     */
    public void buttonAide(Button help) {
        help.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent me) {
                vue.getPlateau().deplacementIA();
                vue.setLabelNbDeplacements(new Label(String.valueOf(Integer.parseInt(vue.getLabelNbDeplacements().getText()) + 1)));
                System.out.println(vue.getLabelNbDeplacements().getText());
                vue.getRootGame().getChildren().remove(0);
                rechargeInterfaceGame();
                deroulementJeu();
                affichageGrilleJeu(vue.getGrilleJeu());
            }
        }
        );
    }

    /**
     * Créé l'image de fin de partie.
     */
    public void finPartieInterface() {
        //si on appelle cette méthode, c'est que le jeu est terminé
        //But de la méthode : proposer des choix au joueur concernant le classement + message de victoire

        String style = vue.getPlateau().getStyle();
        int taille = vue.getPlateau().getTaille();
        if (taille == 3) {
            vue.getGrilleJeu().getChildren().removeAll(vue.getGrilleJeu().getChildren());
            ImageView motif = new ImageView(new Image(Vue.class.getResourceAsStream("images/Motif" + style + "Fin.jpg")));
            motif.setFitHeight(300.);
            motif.setPreserveRatio(true);
            GridPane grilleFinPartie = new GridPane();
            grilleFinPartie.add(motif, 0, 0);
            vue.setGrilleJeu(grilleFinPartie);
            vue.getGrilleJeu().setAlignment(Pos.CENTER);
            chargeInterfaceFinPartie();
        } else if (taille == 4) {
            vue.getGrilleJeu().getChildren().removeAll(vue.getGrilleJeu().getChildren());
            ImageView motif = new ImageView(new Image(Vue.class.getResourceAsStream("images/Motif" + style + "Fin.jpg")));
            motif.setFitHeight(330.);
            motif.setPreserveRatio(true);
            GridPane grilleFinPartie = new GridPane();
            grilleFinPartie.add(motif, 0, 0);
            vue.setGrilleJeu(grilleFinPartie);
            vue.getGrilleJeu().setAlignment(Pos.CENTER);
            chargeInterfaceFinPartie();
        } else if (taille == 5) {
            vue.getGrilleJeu().getChildren().removeAll(vue.getGrilleJeu().getChildren());
            ImageView motif = new ImageView(new Image(Vue.class.getResourceAsStream("images/Motif" + style + "Fin.jpg")));
            motif.setFitHeight(270.);
            motif.setPreserveRatio(true);
            GridPane grilleFinPartie = new GridPane();
            grilleFinPartie.add(motif, 0, 0);
            vue.setGrilleJeu(grilleFinPartie);
            vue.getGrilleJeu().setAlignment(Pos.CENTER);
            chargeInterfaceFinPartie();
        } else {
            System.out.println("Erreur méthode finPartieInterface");
        }
    }

    /**
     * Crée l'interface de la fin de la partie
     */
    public void chargeInterfaceFinPartie() {

        HBox felicitation = new HBox(2);
        vue.getLabelName().setTextFill(Color.WHITE);
        Label bravo = new Label("Félicitations ");
        bravo.setFont(Font.font("Rockwell", FontWeight.NORMAL, 15));
        bravo.setTextFill(Color.WHITE);
        felicitation.getChildren().addAll(bravo, vue.getLabelName());
        felicitation.setAlignment(Pos.CENTER);

        HBox boxDep = new HBox(1);
        vue.getLabelNbDeplacements().setFont(Font.font("Rockwell", FontWeight.NORMAL, 15));
        vue.getLabelNbDeplacements().setTextFill(Color.WHITE);
        Label labelDep = new Label(" - " + vue.getLabelNbDeplacements().getText() + " déplacements.");
        labelDep.setFont(Font.font("Rockwell", FontWeight.NORMAL, 15));
        labelDep.setTextFill(Color.WHITE);
        boxDep.getChildren().addAll(labelDep);
        boxDep.setAlignment(Pos.CENTER);

        HBox boxTemps = new HBox(1);
        Label labelTemps = new Label(" - " + vue.getChrono() / 1000 + " secondes.");
        labelTemps.setFont(Font.font("Rockwell", FontWeight.NORMAL, 15));
        labelTemps.setTextFill(Color.WHITE);
        boxTemps.getChildren().addAll(labelTemps);
        boxTemps.setAlignment(Pos.CENTER);

        Label reussi = new Label("Vous avez réussi en : ");
        reussi.setFont(Font.font("Rockwell", FontWeight.NORMAL, 15));
        reussi.setTextFill(Color.WHITE);
        reussi.setAlignment(Pos.CENTER);

        VBox infos = new VBox(4);
        infos.getChildren().addAll(felicitation, reussi, boxDep, boxTemps);
        infos.setAlignment(Pos.CENTER);
        infos.setSpacing(10.);

        VBox boxDuBas = new VBox(1);
        Button classement = new Button();
        classement.setText("Classement");
        classement.setTextAlignment(TextAlignment.CENTER);
        classement.autosize();
        classement.setAlignment(Pos.CENTER);
        classement.setFont(Font.font("Rockwell", FontWeight.NORMAL, 15));
        boxDuBas.getChildren().add(classement);

        //REGROUPEMENT DES PARTIES HAUTE, CENTRALE
        VBox vBoxGlobale = new VBox(4);
        vBoxGlobale.getChildren().addAll(vue.getLabelTitre(), vue.getGrilleJeu(), infos, boxDuBas); //boxDuHaut, boxDuBas
        vBoxGlobale.setSpacing(10.);
        vBoxGlobale.setPrefSize(600, 600);
        vBoxGlobale.setAlignment(Pos.CENTER);

        vue.getRootFinPartie().getChildren().add(vBoxGlobale);

        vue.getStage().setScene(vue.getSceneFinPartie());
        vue.getRootFinPartie().setEffect(new GaussianBlur(100));
        Task task = new Task<Void>() { // on définit une tâche parallèle pour mettre à jour la vue
            int objectif = 0;
            int tmp = 50;

            @Override
            public Void call() throws Exception {
                while (tmp != objectif) {
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            tmp = tmp - 1;
                            final GaussianBlur gaussianBlur = new GaussianBlur(tmp);
                            vue.getRootFinPartie().setEffect(gaussianBlur);
                        }
                    }
                    );
                    Thread.sleep(10);
                }

                return null;
            }

        };
        Thread th = new Thread(task);
        th.setDaemon(false);
        th.start();

        finPartieInterfaceClassement(classement);

        //FIN DE LA METHODE QUI INITALISE L'INTERFACE DE FIN DE PARTIE
    }
}
