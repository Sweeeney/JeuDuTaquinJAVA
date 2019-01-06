/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jeu.du.taquin.g2;

import java.io.Serializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;

/**
 *
 * @author ameli
 */
public class Vue implements Serializable {

    //VARIABLES GLOBALES
    private transient Controller cont;
    private transient Stage stage;
    private transient Scene sceneGame, scene, sceneClassement;
    private transient Scene sceneRenseignements, sceneFinPartie;
    private Plateau plateau = new Plateau(3);
    private transient Label name;
    private transient GridPane grilleJeu;
    private transient Label labelTitre;
    private Label nbDeplacements;
    private transient Group root, rootGame, rootRenseignements, rootClassement, rootFinPartie;
    private transient BDD bdd;
    private Long chrono;
    private boolean nouveauJeu;
    private transient Long tempsDepart = 0L;
    private transient Label time;

    //CONSTRUCTEUR
    /**
     * Constructeur de la classe Vue
     *
     * @param s Stage
     */
    public Vue(Stage s) {
        this.stage = s;
        cont = new Controller(this);
        this.initialize();
    }

    //GETTER ET SETTER
    /**
     * Setter du booléen pour savoir s'il s'agit d'un nouveau jeu.
     *
     * @param b booléen nouveau jeu
     */
    public void setBooleanJeu(boolean b) {
        this.nouveauJeu = b;
    }

    /**
     * Getter pour savoir s'il s'agit d'un nouveau jeu.
     *
     * @return booléen nouveau jeu
     */
    public boolean getBooleanJeu() {
        return this.nouveauJeu;
    }

    /**
     * Setter du chronomètre.
     *
     * @param c chronomètre
     */
    public void setChrono(Long c) {
        this.chrono = c;
    }

    /**
     * Getter du chronomètre.
     *
     * @return chronomètre
     */
    public Long getChrono() {
        return this.chrono;
    }

    /**
     * Getter de la bdd
     *
     * @return bdd
     */
    public BDD getBDD() {
        return this.bdd;
    }

    /**
     * Getter du label titre (Jeu du Taquin)
     *
     * @return label titre
     */
    public Label getLabelTitre() {
        return this.labelTitre;
    }

    /**
     * Setter de la grille du jeu.
     *
     * @param g grille du jeu
     */
    public void setGrilleJeu(GridPane g) {
        this.grilleJeu = g;
    }

    /**
     * Getter de la grille du jeu.
     *
     * @return grille du jeu
     */
    public GridPane getGrilleJeu() {
        return this.grilleJeu;
    }

    /**
     * Setter du label name (label du nom du joueur).
     *
     * @param n label du nom du joueur
     */
    public void setLabelName(Label n) {
        this.name = n;
    }

    /**
     * Getter du label name (label du nom du joueur).
     *
     * @return label du nom du joueur
     */
    public Label getLabelName() {
        return this.name;
    }

    /**
     * Setter du label du temps.
     *
     * @param n label du temps
     */
    public void setLabelTime(Label n) {
        this.time = n;
    }

    /**
     * Getter du label du temps.
     *
     * @return label du temps
     */
    public Label getLabelTime() {
        return this.time;
    }

    /**
     * Getter du Stage.
     *
     * @return stage
     */
    public Stage getStage() {
        return this.stage;
    }

    /**
     * Getter de la scène du jeu.
     *
     * @return scène du jeu
     */
    public Scene getSceneGame() {
        return this.sceneGame;
    }

    /**
     * Getter de la scène du départ.
     *
     * @return scène du départ
     */
    public Scene getScene() {
        return this.scene;
    }

    /**
     * Getter de la scène du classement.
     *
     * @return scène du classement
     */
    public Scene getSceneClassement() {
        return this.sceneClassement;
    }

    /**
     * Getter de la scène de renseignements.
     *
     * @return scène de renseignements
     */
    public Scene getSceneRenseignements() {
        return this.sceneRenseignements;
    }

    /**
     * Getter du root renseignements.
     *
     * @return root renseignements
     */
    public Group getRootRenseignements() {
        return this.rootRenseignements;
    }

    /**
     * Getter du root classement.
     *
     * @return root classement
     */
    public Group getRootClassement() {
        return this.rootClassement;
    }

    /**
     * Getter du root du départ.
     *
     * @return root du départ
     */
    public Group getRoot() {
        return this.root;
    }

    /**
     * Getter du root du jeu.
     *
     * @return root du jeu
     */
    public Group getRootGame() {
        return this.rootGame;
    }

    /**
     * Setter du root du jeu.
     *
     * @param p root du jeu
     */
    public void setRootGame(Group p) {
        this.rootGame = p;
    }

    /**
     * Getter du root de la fin de la partie.
     *
     * @return root du jeu
     */
    public Group getRootFinPartie() {
        return this.rootFinPartie;
    }

    /**
     * Setter du root de la fin de la partie.
     *
     * @param p root du jeu
     */
    public void setRootFinPartie(Group p) {
        this.rootFinPartie = p;
    }

    /**
     * Getter de la scène de la fin de la partie.
     *
     * @return root du jeu
     */
    public Scene getSceneFinPartie() {
        return this.sceneFinPartie;
    }

    /**
     * Setter de la scene de la fin de la partie.
     *
     * @param p root du jeu
     */
    public void setSceneFinPartie(Scene p) {
        this.sceneFinPartie = p;
    }

    /**
     * Getter du plateau.
     *
     * @return plateau
     */
    public Plateau getPlateau() {
        return this.plateau;
    }

    /**
     * Setter du plateau.
     *
     * @param taille taille du plateau
     * @param style style du plateau
     * @param nomJoueur nom du joueur
     */
    public void setPlateau(int taille, String style, String nomJoueur) {
        this.plateau = new Plateau(taille, style, nomJoueur);
        this.name = new Label(nomJoueur);
    }

    /**
     * Setter du plateau.
     *
     * @param taille taille du plateau
     * @param s style du plateau
     * @param nom nom du joueur
     * @param p plateau
     */
    public void setPlateau(int taille, String s, String nom, int[][] p) {
        this.plateau = new Plateau(taille, s, nom);
        this.name = new Label(nom);
        this.plateau.setPlateau(p);
    }

    /**
     * Setter du label du nombre de déplacements.
     *
     * @param l label du nombre de déplacements
     */
    public void setLabelNbDeplacements(Label l) {
        this.nbDeplacements = l;
    }

    /**
     * Getter du label du nombre de déplacements.
     *
     * @return label du nombre de déplacements
     */
    public Label getLabelNbDeplacements() {
        return this.nbDeplacements;
    }

    /**
     * Setter de la variable tempsDepart.
     *
     * @param l long représentant le temps
     */
    public void setTempsDepart(Long l) {
        this.tempsDepart = l;
    }

    /**
     * Getter de la variable tempsDepart.
     *
     * @return long représentant le temps
     */
    public Long getTempsDepart() {
        return this.tempsDepart;
    }

    //METHODES
    /**
     * Méthode qui initialise l'interface
     */
    public final void initialize() {

        String serverName = "localhost";
        String mydatabase = "java_taquin";
        String username = "root";//utilisateur à créer dans la base
        String password = "";//mot de passe de l'utilisateur
        this.bdd = new BDD(serverName, mydatabase, username, password); //constructeur de la bdd

        this.labelTitre = new Label("Jeu du Taquin");
        this.labelTitre.setFont(Font.font("SF Distant Galaxy", FontWeight.BOLD, 50));  // mettre la police SF Distant Galaxy, taille50
        this.labelTitre.setTextFill(Color.YELLOW);
        DropShadow drop = new DropShadow();
        drop.setRadius(5.0);
        drop.setOffsetX(3);
        drop.setOffsetY(3);
        drop.setColor(Color.color(0.5, 0.5, 0.5));
        this.labelTitre.setEffect(drop);
        this.labelTitre.setCache(true);

        //===============================================================
        //CREATION DES DIFFERENTES FENETRES QUI COMPOSENT NOTRE INTERFACE
        //===============================================================
        //Fenêtre du début
        ImageView imagedebut = new ImageView(new Image(Vue.class.getResourceAsStream("images/yoda_sombre.jpg")));
        imagedebut.setPreserveRatio(true);
        imagedebut.setFitHeight(800);
        imagedebut.setFitWidth(800);
        this.root = new Group();
        this.root.getChildren().setAll(imagedebut);
        this.scene = new Scene(root, 600, 600, Color.BLACK); //IMAGE DE FOND A RAJOUTER + VOIR TAILLE

        //Fenêtre jeu
        ImageView imageNew = new ImageView(new Image(Vue.class.getResourceAsStream("images/yoda_sombre.jpg")));
        imageNew.setPreserveRatio(true);
        imageNew.setFitHeight(800);
        imageNew.setFitWidth(800);
        this.rootGame = new Group();
        this.rootGame.getChildren().setAll(imageNew);
        this.sceneGame = new Scene(rootGame, 600, 600, Color.BLACK);

        //Fenêtre pour demander les renseignements
        ImageView imageRenseignements = new ImageView(new Image(Vue.class.getResourceAsStream("images/yoda_sombre.jpg")));
        imageRenseignements.setPreserveRatio(true);
        imageRenseignements.setFitHeight(800);
        imageRenseignements.setFitWidth(800);
        this.rootRenseignements = new Group();
        this.rootRenseignements.getChildren().setAll(imageRenseignements);
        this.sceneRenseignements = new Scene(rootRenseignements, 600, 600, Color.BLACK);

        //Fenêtre pour l'affichage du classement
        ImageView imageClassement = new ImageView(new Image(Vue.class.getResourceAsStream("images/yoda_sombre.jpg")));
        imageClassement.setPreserveRatio(true);
        imageClassement.setFitHeight(800);
        imageClassement.setFitWidth(800);
        this.rootClassement = new Group();
        this.rootClassement.getChildren().setAll(imageClassement);
        this.sceneClassement = new Scene(rootClassement, 600, 600, Color.BLACK);

        //Fenêtre pour l'affichage de la fin de la partie
        ImageView imageFinPartie = new ImageView(new Image(Vue.class.getResourceAsStream("images/yoda_sombre.jpg")));
        imageFinPartie.setPreserveRatio(true);
        imageFinPartie.setFitHeight(800);
        imageFinPartie.setFitWidth(800);
        this.rootFinPartie = new Group();
        this.rootFinPartie.getChildren().setAll(imageClassement);
        this.sceneFinPartie = new Scene(rootFinPartie, 600, 600, Color.BLACK);

        //on ajoute la première scene au theatre
        stage.setScene(scene);

        //affichage du theatre
        stage.show();

        //====================================================
        //CREATION DE LA PREMIERE "PAGE"
        //====================================================
        //création du bouton "Nouveau jeu" 
        Button play = new Button();
        play.setText("Nouveau jeu");
        play.setTextAlignment(TextAlignment.CENTER);
        play.setFont(Font.font("CASTELLAR", FontWeight.EXTRA_BOLD, 20));
        play.autosize();
        this.cont.buttonNewGame(play);

        //création du bouton "Recharger la partie"
        Button oldGame = new Button();
        oldGame.setText("Recharger la partie");
        oldGame.setTextAlignment(TextAlignment.CENTER);
        oldGame.setFont(Font.font("CASTELLAR", FontWeight.EXTRA_BOLD, 20));
        oldGame.autosize();
        this.cont.buttonOldGame(oldGame);

        //création de la box verticale pour insérer les boutons précédents et le titre
        VBox vBox = new VBox(3);
        vBox.getChildren().addAll(this.labelTitre, play, oldGame);
        vBox.setAlignment(Pos.CENTER);
        vBox.setPadding(new Insets(30, 30, 30, 30));
        vBox.setSpacing(10);
        vBox.setPrefSize(600, 400);
        //ajout du tout dans le programme du theatre 
        this.root.getChildren().add(vBox);

    }
}
