import javafx.animation.AnimationTimer;

import javafx.animation.PathTransition;
import javafx.animation.RotateTransition;
import  javafx.application.Application;

import javafx.geometry.Point3D;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;

import javafx.scene.control.*;
import javafx.scene.image.Image;

import javafx.scene.image.ImageView;

import javafx.scene.input.KeyCode;

import javafx.scene.input.MouseButton;
import javafx.scene.layout.Pane;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.stage.Stage;
import javafx.util.Duration;


import java.util.ArrayList;

import java.util.HashMap;



public class Game extends Application {

    public static ArrayList<Block> platforms = new ArrayList<>();    //õðàíèì ïëàòôîðìû

    private HashMap<KeyCode,Boolean> keys = new HashMap<>(); //Õðàíèê êîäû êíîïîê.

    Media media=new Media("file:/C:/music.mp3");

    MediaPlayer mediaPlayer=new MediaPlayer(media);

    Image backgroundImg = new Image(getClass().getResourceAsStream("gg.png"));


    Line line=new Line(0,595,1200,595);

    Circle circle=new Circle(10);

    Label loading=new Label("Loading.......");

    PathTransition pathTransition=new PathTransition(Duration.millis(5000),line,circle);

    RotateTransition rotateTransition=new RotateTransition(Duration.millis(1000),loading);

    public static final int BLOCK_SIZE = 45;

    public static final int BAT_SIZE = 60;



    public static Pane appRoot = new Pane();

    public static Pane gameRoot = new Pane();



    public Character player;

    int levelNumber = 0;

    private int levelWidth;



    private void initContent(){

        ImageView backgroundIV = new ImageView(backgroundImg);

        backgroundIV.setFitHeight(14*BLOCK_SIZE);

//        backgroundIV.setFitWidth(28*BLOCK_SIZE);



        levelWidth = LevelData.levels[levelNumber][0].length()*BLOCK_SIZE;

        for(int i = 0; i < LevelData.levels[levelNumber].length; i++){

            String line = LevelData.levels[levelNumber][i];

            for(int j = 0; j < line.length();j++){

                switch (line.charAt(j)){

                    case '0':

                        break;

                    case '1':

                        Block platformFloor = new Block(Block.BlockType.PLATFORM, j * BLOCK_SIZE, i * BLOCK_SIZE);

                        break;

                    case '2':

                        Block brick = new Block(Block.BlockType.BRICK,j*BLOCK_SIZE,i*BLOCK_SIZE);

                        break;

                        case '9':

                        Block bonus = new Block(Block.BlockType.BONUS,j*BLOCK_SIZE,i*BLOCK_SIZE);

                        break;

                    case '7':

                        Block stone = new Block(Block.BlockType.STONE,j * BLOCK_SIZE, i * BLOCK_SIZE);

                        break;

//                    case '5':
//
//                        Block PipeTopBlock = new Block(Block.BlockType.PIPE_TOP,j * BLOCK_SIZE, i * BLOCK_SIZE);
//
//                        break;
//
//                    case '6':
//
//                        Block PipeBottomBlock = new Block(Block.BlockType.PIPE_BOTTOM,j * BLOCK_SIZE, i * BLOCK_SIZE);
//
//                        break;
//
                    case '*':

                        Block InvisibleBlock = new Block(Block.BlockType.STAR,j * BLOCK_SIZE, i * BLOCK_SIZE);

                        break;

                }

            }



        }

        player =new Character();

        player.setTranslateX(0);

        player.setTranslateY(400);

        player.translateXProperty().addListener((obs,old,newValue)->{

            int offset = newValue.intValue();

            if(offset>640 && offset<levelWidth-640){

                gameRoot.setLayoutX(-(offset-640));

                backgroundIV.setLayoutX(-(offset-640));

            }

        });

        gameRoot.getChildren().add(player);

        appRoot.getChildren().addAll(backgroundIV,gameRoot);

    }



    private void update(){


        if(isPressed(KeyCode.UP) && player.getTranslateY()>=5){

            player.jumpPlayer();

        }

        if(isPressed(KeyCode.LEFT) && player.getTranslateX()>=5){

            player.imageView.setFitWidth(60);
            player.setScaleX(-1);

            player.animation.play();

            player.moveX(-5);

        }

        if(isPressed(KeyCode.RIGHT) && player.getTranslateX()+40 <=levelWidth-5){

            player.imageView.setFitWidth(60);

            player.setScaleX(1);

            player.animation.play();

            player.moveX(5);

        }

        if(player.playerVelocity.getY()<10){                          //ãðàâèòàöèÿ

            player.playerVelocity = player.playerVelocity.add(0,1);

        }

        player.moveY((int)player.playerVelocity.getY());

    }

    private boolean isPressed(KeyCode key){

        return keys.getOrDefault(key,false);

    }

    public void start(Stage primaryStage) throws Exception {

        mediaPlayer.play();

        rotateTransition.setAxis(new Point3D(0,1,0));

        loading.setTextFill(Color.YELLOW);

        loading.setStyle("-fx-font-size: 20;");

        loading.setTranslateY(550);

        loading.setTranslateX(550);

        circle.setFill(Color.YELLOW);

        line.setStroke(Color.YELLOW);

        initContent();

        rotateTransition.setFromAngle(0);

        rotateTransition.setToAngle(360);

        rotateTransition.setCycleCount(-1);


        Pane startPane=new Pane(line,circle,loading);

        startPane.setStyle("-fx-background-image: url(batfon.jpg);-fx-background-size: 1200 650");

        pathTransition.play();

        rotateTransition.play();

        ImageView musOn=new ImageView("musOn.png");

        musOn.setFitWidth(35);

        musOn.setFitHeight(35);

        ImageView musOff=new ImageView("musOff.png");

        musOff.setFitWidth(35);

        musOff.setFitHeight(35);

        Button play=new Button("Play");

        ToggleButton btnMusOn=new ToggleButton();

        btnMusOn.setTranslateY(500);

        play.setTranslateX(100);

        if (btnMusOn.isSelected()==true) {

            btnMusOn.setGraphic(musOff);

        }

        if (btnMusOn.isSelected()==false){

            btnMusOn.setGraphic(musOn);

        }

        Pane menu=new Pane(btnMusOn,play);

        menu.setStyle("-fx-background-image: url(fonMenu.jpg);-fx-background-size: 1200 650");

        Scene menuScene=new Scene(menu,1200,620);

        Scene startScene=new Scene(startPane,1200,620);

        Scene scene = new Scene(appRoot,1200,620);

        scene.setOnKeyPressed(event-> keys.put(event.getCode(), true));

        scene.setOnMouseClicked(event -> {

            if (event.getButton()== MouseButton.PRIMARY) {

                player.imageView.setFitWidth(55);

                player.animationHit1.play();
            }

            if (event.getButton()==MouseButton.SECONDARY){

                player.imageView.setFitWidth(70);

                if(player.canJump){

                    player.playerVelocity = player.playerVelocity.add(0,-23);

                    player.canJump = false;

                }

                player.animationKick.play();

            }



        });

        player.animationKick.setOnFinished(event -> {

            player.animation.stop();

            player.imageView.setFitWidth(37);

            player.imageView.setViewport(new Rectangle2D(0,305,37,60));

        });

        player.animationHit1.setOnFinished(event -> {

            player.animation.stop();

            player.imageView.setFitWidth(37);

            player.imageView.setViewport(new Rectangle2D(0,305,37,60));

        });

        scene.setOnKeyReleased(event -> {

            keys.put(event.getCode(), false);

            player.animation.stop();

            player.imageView.setFitWidth(37);

            player.animationHit1.stop();

            player.imageView.setViewport(new Rectangle2D(0,305,37,60));

        });

        primaryStage.setTitle("Batman");

        primaryStage.setScene(startScene);

        pathTransition.setOnFinished(event -> {

            primaryStage.setScene(menuScene);

        });

        play.setOnAction(event -> {

            primaryStage.setScene(scene);

        });

        primaryStage.show();

        AnimationTimer timer = new AnimationTimer() {

            @Override

            public void handle(long now) {

                update();

            }

        };

        timer.start();

    }

    public static void main(String[] args) {

        launch(args);

    }

}