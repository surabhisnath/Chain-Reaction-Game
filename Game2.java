package ChainReaction;

import javafx.animation.*;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.*;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.*;
import javafx.scene.transform.Rotate;
import javafx.stage.Stage;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Optional;
import java.util.Random;

import javafx.util.Duration;


/**
 * Game2 Class launches the grid based on the selection by the user.
 * All moves done by the players which involves making and splitting of orbs
 * is done inside this class. Functionality supporting undo of a move and
 * resume the game is also implemented here.
 *
 * @author Surabhi S. Nath and Raghav Sood
 * @version 1.20
 * @since 2017-10-18
 */
public class Game2 extends Application
{
    static Player players[];
    boolean finished;
    int num_players=2;
    int grid_size=1;
    Grid G;
    Grid G_old;
    Stage primarystage;
    int x;
    Pane root;
    ArrayList<Tile> at = new ArrayList<Tile>();
    int currplay;
    boolean var;
    boolean deadarray[];
    Button undo;
    Button computer;
    int mode;


    /**
     * Default Constructor of class Game2, used to launch the application.
     * @return Nothing
     */
    public Game2()
    {

    }

    /**
     * Parameterized constructor of class Game2 used to initialise the instance variables
     * @param p Array of Players
     * @param np Number of players playing
     * @param g Size of the grid
     * @param ps Primary Stage to launch the application
     * @param m Mode to decide single player or multiplayer
     * @return Nothing
     */
    public Game2(Player[] p, int np, int g, Stage ps,int m)
    {
        players=p;
        finished=false;
        num_players=np;
        grid_size=g;
        primarystage=ps;
        G=new Grid(grid_size);
        deadarray=new boolean[8];
        mode=m;
    }

    /**
     * Calls start method to launch the application
     * @return Nothing
     * @throws Exception
     * @see Exception
     */
    public void play() throws Exception
    {
        if(grid_size==1)
            x=70;

        else
        {
            G=new Grid(2);
            x = 42;
        }

        this.start(primarystage);
    }


    /**
     * Creates a pane with Tiles on it based on the type of grid
     * Two sets of tiles are created to give a 3d effect
     * @return Parent A pane which is added to the Scene is returned.
     */
    private Parent createContent()
    {
        root=new Pane();
        root.setPrefSize(420,680);
        if(grid_size==1)
        {
            for(int i=0;i<6;i++)
            {
                for(int j=0;j<9;j++)
                {
                    Tile tile=new Tile();
                    tile.setTranslateX(i*65+15);
                    tile.setTranslateY(72.5+j*65);
                    root.getChildren().add(tile);
                }
            }


            for(int i=0;i<6;i++)
            {
                for(int j=0;j<9;j++)
                {
                    Tile tile=new Tile("hi");
                    tile.setTranslateX(i*70);
                    tile.setTranslateY(50+j*70);
                    root.getChildren().add(tile);
                }
            }


            for(int i=0;i<7;i++)
            {
                for (int j = 0; j<10; j++)
                {
                    Line line = new Line(i*65+15, 72.5+j*65,i*70, 50+j*70);
                    line.setStroke(Color.rgb(players[(count+1)%num_players].getColour().getRed(), players[(count+1)%num_players].getColour().getGreen(), players[(count+1)%num_players].getColour().getBlue()));
                    root.getChildren().add(line);
                    line.toFront();
                }
            }
        }

        else
        {
            for(int i=0; i<10; i++)
            {
                for(int j=0; j<15; j++)
                {
                    Tile tile=new Tile();
                    tile.setTranslateX(i*40+10);
                    tile.setTranslateY(65+j*40);
                    root.getChildren().add(tile);
                }
            }

            for(int i=0; i<10; i++)
            {
                for(int j=0; j<15; j++)
                {
                    Tile tile=new Tile("hi");
                    tile.setTranslateX(i*42);
                    tile.setTranslateY(50+j*42);
                    root.getChildren().add(tile);
                }
            }

            for(int i=0;i<=10;i++)
            {
                for (int j = 0; j <= 15; j++)
                {
                    Line line = new Line(i * 42, 50+j *42, i * 40 + 10, j * 40 + 65);
                    line.setStroke(Color.rgb(players[(count+1)%num_players].getColour().getRed(), players[(count+1)%num_players].getColour().getGreen(), players[(count+1)%num_players].getColour().getBlue()));
                    root.getChildren().add(line);
                    line.toFront();
                }
            }
        }

        return root;
    }


    /**
     * Launches the application of class Game2
     * Consists of a Pane with tiles, undo button and a combobox(Exit Game and Resume Game)
     * @param primaryStage Stage on which the application launches.
     * @return Nothing
     * @throws Exception
     * @see Exception
     */
    @Override
    public void start(Stage primaryStage) throws Exception
    {
        Parent roott=FXMLLoader.load(getClass().getResource("Game.fxml"));
        AnchorPane t=(AnchorPane)roott;
        t.getChildren().add(createContent());
        undo=new Button("UNDO");
        undo.setLayoutX(295);
        undo.setLayoutY(10);
        undo.setPrefHeight(30);
        undo.setStyle("-fx-background-color: rgb(25,25,25); -fx-text-fill: white");
        undo.setDisable(true);
        computer=new Button();
        computer.setLayoutX(240);
        computer.setLayoutY(10);
        computer.setPrefHeight(30);
        computer.setPrefWidth(35);
        computer.setStyle("-fx-background-color: rgb(25,25,25); -fx-text-fill: white");
        ImageView im=new ImageView("file:src/ChainReaction/Computer.png");
        im.setFitWidth(30);
        im.setFitHeight(20);
        computer.setGraphic(im);
        if(mode==0)
            computer.setVisible(false);
        computer.setDisable(true);

        computer.setOnAction(new EventHandler<ActionEvent>()
        {

            /**
             * Function called when COMP is clicked.
             * Computer plays a random move on the grid
             * @param event Event of clicking COMP button
             * @return Nothing
             */
            @Override
            public void handle(ActionEvent event) {
                Random rand=new Random();
                boolean flag=true;
                while(flag) {
                    int xcoor = rand.nextInt(G.row);
                    int ycoor = rand.nextInt(G.col);
                    Tile x = (Tile) root.getChildren().get(G.row * G.col + xcoor + G.row * ycoor);
                    if (G.array[xcoor][ycoor].currentnooforbs == 0) {
                        flag = false;
                        x.mouse();
                    } else {
                        if (colourCheck(x.orb1.player, players[1])) {
                            flag=false;
                            x.mouse();
                        }
                    }
                }
            }
        });

        undo.setOnAction(new EventHandler<ActionEvent>()
        {
            /**
             * Function is called when undo is clicked.
             * Restores all parameter of the Game2 class to the state it was in
             * one move ago.
             * @param event Event of clicking the undo button
             * @return Nothing
             */
            @Override
            public void handle(ActionEvent event)
            {
                try
                {
                    G=new Grid(grid_size);
                    count=currplay;
                    root=new Pane();
                    start(primarystage);

                    for(int i=0;i<num_players;i++)
                    {
                        players[i].dead=deadarray[i];
                    }

                    int h=0;
                    int temp=count;

                    do
                    {
                        temp++;
                    }while(players[temp%num_players].dead);

                    booi(temp);

                    for(int j=0; j<G.col; j++)
                        for(int i=0; i<G.row; i++)
                        {
                            Tile x=(Tile)root.getChildren().get(G.row*G.col+h);

                            int radius=0;
                            int translatex=0;
                            double pathy=0.0f;
                            if(grid_size==1)
                            {
                                radius = 15;
                                translatex=10;
                            }

                            else
                            {
                                radius = 10;
                                translatex=6;
                            }

                            if(G_old.array[i][j].currentnooforbs==1)
                            {
                                G.array[i][j].currentnooforbs=1;
                                x.orb1=at.get(h).orb1;
                                x.orb1.player=at.get(h).orb1.player;
                                x.orb1.player.getColour().setRed(at.get(h).orb1.player.getColour().getRed());
                                x.orb1.player.getColour().setGreen(at.get(h).orb1.player.getColour().getGreen());
                                x.orb1.player.getColour().setBlue(at.get(h).orb1.player.getColour().getBlue());
                                //x.border=at.get(h).border;
                                x.sphereA = new Sphere(radius);
                                PhongMaterial material1 = new PhongMaterial();
                                material1.setDiffuseColor(Color.rgb(x.orb1.player.getColour().getRed(),x.orb1.player.getColour().getGreen(),x.orb1.player.getColour().getBlue()));
                                x.sphereA.setMaterial(material1);
                                Group group=new Group();
                                group.getChildren().addAll(x.sphereA);
                                x.getChildren().addAll(group);
                                x.rotateAroundYAxis(group).play();
                            }


                            else if(G_old.array[i][j].currentnooforbs==2)
                            {
                                G.array[i][j].currentnooforbs=2;
                                x.orb1=at.get(h).orb1;
                                x.orb1.player=at.get(h).orb1.player;
                                x.orb1.player.getColour().setRed(at.get(h).orb1.player.getColour().getRed());
                                x.orb1.player.getColour().setGreen(at.get(h).orb1.player.getColour().getGreen());
                                x.orb1.player.getColour().setBlue(at.get(h).orb1.player.getColour().getBlue());
                                //x.border=at.get(h).border;
                                x.sphereA=new Sphere(radius);
                                x.sphereA.setTranslateX(translatex);
                                PhongMaterial material1 = new PhongMaterial();
                                material1.setDiffuseColor(Color.rgb(x.orb1.player.getColour().getRed(),x.orb1.player.getColour().getGreen(),x.orb1.player.getColour().getBlue()));
                                x.sphereA.setMaterial(material1);
                                x.sphereB=new Sphere(radius);
                                x.sphereB.setTranslateX(-translatex);
                                PhongMaterial material2 = new PhongMaterial();
                                material2.setDiffuseColor(Color.rgb(x.orb1.player.getColour().getRed(),x.orb1.player.getColour().getGreen(),x.orb1.player.getColour().getBlue()));
                                x.sphereB.setMaterial(material2);
                                Group group=new Group();
                                group.getChildren().addAll(x.sphereA,x.sphereB);
                                x.getChildren().addAll(group);
                                x.rotateAroundYAxis(group).play();
                            }


                            else if(G_old.array[i][j].currentnooforbs==3)
                            {
                                G.array[i][j].currentnooforbs=3;
                                x.orb1=at.get(h).orb1;
                                x.orb1.player=at.get(h).orb1.player;
                                x.orb1.player.getColour().setRed(at.get(h).orb1.player.getColour().getRed());
                                x.orb1.player.getColour().setGreen(at.get(h).orb1.player.getColour().getGreen());
                                x.orb1.player.getColour().setBlue(at.get(h).orb1.player.getColour().getBlue());
                                //x.border=at.get(h).border;
                                x.sphereA=new Sphere(radius);
                                x.sphereA.setTranslateX(translatex);
                                PhongMaterial material1 = new PhongMaterial();
                                material1.setDiffuseColor(Color.rgb(x.orb1.player.getColour().getRed(),x.orb1.player.getColour().getGreen(),x.orb1.player.getColour().getBlue()));
                                x.sphereA.setMaterial(material1);
                                x.sphereB=new Sphere(radius);
                                x.sphereB.setTranslateX(-translatex);
                                PhongMaterial material2 = new PhongMaterial();
                                material2.setDiffuseColor(Color.rgb(x.orb1.player.getColour().getRed(),x.orb1.player.getColour().getGreen(),x.orb1.player.getColour().getBlue()));
                                x.sphereB.setMaterial(material2);
                                x.sphereC=new Sphere(radius);
                                x.sphereC.setTranslateY(-translatex);
                                PhongMaterial material3 = new PhongMaterial();
                                material3.setDiffuseColor(Color.rgb(at.get(h).orb1.player.getColour().getRed(),at.get(h).orb1.player.getColour().getGreen(),at.get(h).orb1.player.getColour().getBlue()));
                                x.sphereC.setMaterial(material3);
                                Group group=new Group();
                                group.getChildren().addAll(x.sphereA,x.sphereB,x.sphereC);
                                x.getChildren().addAll(group);
                                x.rotateAroundYAxis(group).play();
                            }

                            h++;
                        }
                }

                catch (Exception e)
                {
                    e.printStackTrace();
                }

            }

        });

        ObservableList<String> options = FXCollections.observableArrayList("Exit Game","New Game");
        Iterator it=options.iterator();
        ComboBox<String> comboBox = new ComboBox<String>();
        while(it.hasNext())
        {
            comboBox.getItems().add(it.next().toString());
        }

        comboBox.setStyle("-fx-background-color: rgb(25,25,25); -fx-text-fill: white");
        comboBox.setPrefSize(0,30);
        comboBox.setLayoutX(355);
        comboBox.setLayoutY(10);

        comboBox.setOnAction(new EventHandler<ActionEvent>()
        {
            /**
             * Function is called on clicking the Combobox
             * Goes to the mainpage on clicing Exit Game
             * Launches a new game on clicking New Game
             * @param event Event of clicking New Game or Exit Game
             * @return Nothing
             */
            @Override
            public void handle(ActionEvent event)
            {
                if(comboBox.getValue().toString().equals("Exit Game"))
                {
                    Parent roots=null;
                    try
                    {
                        Cell tiles[][]=new Cell[G.row][G.col];

                        for(int j=0; j<G.col; j++)
                        {
                            for (int i = 0; i < G.row; i++)
                            {
                                tiles[i][j]=new Cell(G.array[i][j].cap,0);
                                tiles[i][j].o[0]=new Orb1();
                                tiles[i][j].currentnooforbs=G.array[i][j].currentnooforbs;
                                tiles[i][j].o[0]=((Tile)root.getChildren().get(G.row*G.col+i+G.row*j)).orb1;
                            }
                        }


                        Gamedata gamedata=new Gamedata(players,count,G,finished,grid_size,tiles,num_players,mode);
                        App.serialize(gamedata);
                        roots = FXMLLoader.load(getClass().getResource("MainPage2.fxml"));
                    }

                    catch (IOException e)
                    {
                        e.printStackTrace();
                    }

                    Scene scene = new Scene(roots);
                    primarystage.setScene(scene);
                    primarystage.show();
                }

                else if(comboBox.getValue().toString().equals("New Game"))
                {
                    try
                    {
                        G=new Grid(grid_size);
                        count=-1;
                        for(int i=0;i<num_players;i++)
                        {
                            players[i].dead=false;
                        }
                        root=new Pane();
                        start(primarystage);
                    }

                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                }

            }
        });

        t.getChildren().add(undo);
        t.getChildren().add(comboBox);
        t.getChildren().add(computer);
        primaryStage.setScene(new Scene(t));
        primaryStage.show();

        primaryStage.setOnCloseRequest(event ->
        {
            try
            {
                Cell tiles[][]=new Cell[G.row][G.col];

                for(int j=0; j<G.col; j++)
                {
                    for (int i = 0; i < G.row; i++)
                    {
                        tiles[i][j]=new Cell(G.array[i][j].cap,0);
                        tiles[i][j].o[0]=new Orb1();
                        tiles[i][j].currentnooforbs=G.array[i][j].currentnooforbs;
                        tiles[i][j].o[0]=((Tile)root.getChildren().get(G.row*G.col+i+G.row*j)).orb1;
                    }
                }

                Gamedata gamedata=new Gamedata(players,count,G,finished,grid_size,tiles,num_players,mode);
                App.serialize(gamedata);
            }

            catch (IOException e)
            {
                e.printStackTrace();
            }

        });
    }


    /**
     * Changes the grid colour based on the next player who is going to move
     * @param j Count to identify the next player to move
     * @return Nothing
     */
    public void booi(int j)
    {
        if(grid_size==1)
        {
            for(int i=0;i<108;i++)
            {
                Tile x=(Tile)root.getChildren().get(i);

                for(int k=0; k<num_players; k++)
                {
                    if(j%num_players==k)
                    {
                        x.border.setStroke(Color.rgb(players[k].getColour().getRed(),players[k].getColour().getGreen(),players[k].getColour().getBlue()));
                    }
                }
            }

            for(int i=0;i<70;i++)
            {
                Line x=(Line)root.getChildren().get(i+108);

                for(int k=0; k<num_players; k++)
                {
                    if(j%num_players==k)
                    {
                        x.setStroke(Color.rgb(players[k].getColour().getRed(),players[k].getColour().getGreen(),players[k].getColour().getBlue()));
                    }
                }
            }
        }

        else
        {
            for(int i=0;i<300;i++)
            {
                Tile x = (Tile) root.getChildren().get(i);

                for(int k=0; k<num_players; k++)
                {
                    if(j%num_players==k)
                    {
                        x.border.setStroke(Color.rgb(players[k].getColour().getRed(),players[k].getColour().getGreen(),players[k].getColour().getBlue()));
                    }
                }
            }

            for(int i=0;i<176;i++)
            {
                Line x=(Line)root.getChildren().get(i+300);
                for(int k=0; k<num_players; k++)
                {
                    if(j%num_players==k)
                    {
                        x.setStroke(Color.rgb(players[k].getColour().getRed(),players[k].getColour().getGreen(),players[k].getColour().getBlue()));
                    }
                }
            }
        }
    }


    /**
     * Checks if the colour of two players is same
     * @param a First Player
     * @param b Second Player
     * @return boolean true if both players have the same colour, false otherwise
     */
    public boolean colourCheck(Player a,Player b)
    {

        if(a!=null && b!=null && a.getColour().getRed()==b.getColour().getRed() && a.getColour().getBlue()==b.getColour().getBlue() && a.getColour().getGreen()==b.getColour().getGreen())
            return true;
        else
            return false;
    }

    /**
     * Checks if the current player has won the game
     * @param xcoor Xcoordinate of tile clicked
     * @param ycoor Ycoordinate of tile clicked
     * @param k Index to determine current player
     * @return Player Returns the player if it has won the game, null otherwise.
     */
    public Player winChecker(int xcoor,int ycoor,int k)
    {
        boolean flag=true;
        for(int j=0; j<G.col; j++)
        {
            for (int i = 0; i < G.row; i++)
            {
                Tile x = ((Tile) root.getChildren().get(G.row*G.col + i +G.row* j));
                if (G.array[i][j].currentnooforbs != 0)
                {
                    if (xcoor == 0 && ycoor == 0)
                    {

                        if (!(i == 1 && j == 0) && !(i == 0 && j == 1) && !(i == 0 && j == 0) && !colourCheck(players[k], x.orb1.player))
                        {
                            flag = false;
                            break;
                        }
                    }

                    else if (xcoor == G.row-1 && ycoor == 0)
                    {
                        if (!(i == G.row-1 && j == 0) && !(i == G.row-1 && j == 1) && !(i == G.row-2 && j == 0) && !colourCheck(players[k], x.orb1.player))
                        {
                            flag = false;
                            break;
                        }
                    }

                    else if (xcoor == 0 && ycoor == G.col-1)
                    {
                        if (!(i == 0 && j == G.col-1) && !(i == 0 && j == G.col-2) && !(i == 1 && j == G.col-1) && !colourCheck(players[k], x.orb1.player))
                        {
                            flag = false;
                            break;
                        }
                    }

                    else if (xcoor == G.row-1 && ycoor == G.col-1)
                    {
                        if (!(i == G.row-1 && j == G.col-1) && !(i == G.row-2 && j == G.col-1) && !(i == G.row-1 && j == G.col-2) && !colourCheck(players[k], x.orb1.player))
                        {
                            flag = false;
                            break;
                        }
                    }

                    else if (xcoor == 0)
                    {
                        if (!(i == 0 && j == ycoor + 1) && !(i == 0 && j == ycoor - 1) && !(i == 1 && j == ycoor) && !colourCheck(players[k], x.orb1.player))
                        {
                            flag = false;
                            break;
                        }
                    }

                    else if (xcoor == G.row-1)
                    {
                        if (!(i == G.row-1 && j == ycoor + 1) && !(i == G.row-1 && j == ycoor - 1) && !(i == G.row-2 && j == ycoor) && !colourCheck(players[k], x.orb1.player))
                        {
                            flag = false;
                            break;
                        }
                    }

                    else if (ycoor == 0)
                    {
                        if (!(i == xcoor + 1 && j == 0) && !(i == xcoor - 1 && j == 0) && !(i == xcoor && j == 1) && !colourCheck(players[k], x.orb1.player))
                        {
                            flag = false;
                            break;
                        }
                    }

                    else if (ycoor == G.col-1)
                    {
                        if (!(i == xcoor + 1 && j == G.col-1) && !(i == xcoor - 1 && j == G.col-1) && !(i == xcoor && j == G.col-2) && !colourCheck(players[k], x.orb1.player))
                        {
                            flag = false;
                            break;
                        }
                    }

                    else
                    {
                        if (!(i == xcoor + 1 && j == ycoor) && !(i == xcoor - 1 && j == ycoor) && !(i == xcoor && j == ycoor + 1) && !(i == xcoor && j == ycoor - 1) && !colourCheck(players[k], x.orb1.player))
                        {
                            flag = false;
                            break;
                        }
                    }
                }
            }
        }

        if(flag==true)
            return players[k];

        return null;
    }

    /**
     * Removes a player if no Cell has an orb of its colour.
     * @param p The current player playing
     * @return Nothing
     */
    public void playerRemove(Player p)
    {
        for(int k=0;k<num_players;k++)
        {
            if(colourCheck(p,players[k]))
                continue;
            boolean flag = true;
            for (int j = 0; j < G.col; j++)
            {
                for (int i = 0; i < G.row; i++)
                {
                    Tile x = ((Tile) root.getChildren().get(G.row*G.col + i + G.row* j));
                    if (G.array[i][j].currentnooforbs>0 && colourCheck(players[k], x.orb1.player))
                    {
                        flag = false;
                        break;
                    }

                }
            }

            if(flag==true)
            {
                int temp=count;
                do
                {
                    temp++;
                }while(players[temp%num_players].dead);

                players[k].dead=true;

                if(temp%num_players==k)
                {
                    int temp1=temp;
                    do
                    {
                        temp1++;
                    }while(players[temp1%num_players].dead);

                    booi(temp1);
                }
            }
        }
    }



    int count=-1;

    /**
     * Used to create clickable Cells.
     * Each cell can hold 0 to 3 orbs.
     * Each cell has a border with the colour matching the player who is currently playing.
     *
     * @author Surabhi S. Nath and Raghav Sood
     * @version 1.20
     * @since 2017-10-18
     */
    private class Tile extends StackPane implements Serializable
    {
        Orb1 orb1;
        Rectangle border;
        public Sphere sphereA=null;
        public Sphere sphereB=null;
        public Sphere sphereC=null;
        public Sphere sphereD=null;

        /**
         * Recursive function which supports the main logic of the game.
         * Called when the player clicks on any cell.
         * Supports adding of orbs to current cell,
         * splitting of orbs if necessary and
         * further recursive adding of orbs and splitting in neighbouring cells.
         * @param xcoor Xcoordinate of current cell
         * @param ycoor Ycoordinate of current cell
         * @param flag Flag is set to 0 if user has clicked a particular cell, 1 otherwise.
         * @return ParallelTransition Returns null if no splitting takes place in the cell,
         *         else returns the transition which will take place.
         */
        public ParallelTransition something(int xcoor,int ycoor,int flag)
        {
            int radius=0;
            int translatex=0;
            double pathy=0.0f;
            if(grid_size==1)
            {
                radius = 15;
                translatex=10;
                pathy=70.0f;
            }

            else
            {
                radius = 10;
                translatex=6;
                pathy=42.0f;
            }

            Tile a=(Tile)root.getChildren().get(G.row*G.col+xcoor+G.row*ycoor);

            if(G.array[xcoor][ycoor].currentnooforbs==G.array[xcoor][ycoor].cap)
            {

                if(G.array[xcoor][ycoor].cap==1)
                {
                    if((flag==0 && colourCheck(players[count%num_players],a.orb1.player)) || flag==1)
                    {
                        G.array[xcoor][ycoor].currentnooforbs = 0;
                        a.sphereA = new Sphere(radius);
                        a.sphereA.setTranslateX(translatex);
                        PhongMaterial material1 = new PhongMaterial();
                        material1.setDiffuseColor(Color.rgb(players[count % num_players].getColour().getRed(), players[count % num_players].getColour().getGreen(), players[count % num_players].getColour().getBlue()));
                        a.sphereA.setMaterial(material1);
                        a.sphereB = new Sphere(radius);
                        a.sphereB.setTranslateX(translatex);
                        a.sphereB.setMaterial(material1);

                        Path path = new Path();
                        Path path2 = new Path();

                        MoveTo moveTo = new MoveTo();
                        moveTo.setX(0.0f);
                        moveTo.setY(0.0f);

                        HLineTo hLineTo = new HLineTo();
                        VLineTo hLineTo2 = new VLineTo();

                        if (xcoor == 0 && ycoor == 0)
                        {
                            hLineTo.setX(pathy);
                            hLineTo2.setY(pathy);
                        }

                        else if (xcoor == 0 && ycoor == G.col-1)
                        {
                            hLineTo.setX(-pathy);
                            hLineTo2.setY(pathy);
                        }

                        else if (xcoor == G.row-1 && ycoor == 0)
                        {
                            hLineTo.setX(pathy);
                            hLineTo2.setY(-pathy);
                        }

                        else if (xcoor == G.row-1 && ycoor == G.col-1)
                        {
                            hLineTo.setX(-pathy);
                            hLineTo2.setY(-pathy);
                        }

                        path.getElements().add(moveTo);
                        path.getElements().add(hLineTo);
                        path2.getElements().add(moveTo);
                        path2.getElements().add(hLineTo2);
                        PathTransition pathTransition = new PathTransition();
                        pathTransition.setDuration(Duration.millis(700));
                        pathTransition.setPath(path);
                        pathTransition.setNode(a.sphereA);
                        pathTransition.setOrientation(PathTransition.OrientationType.ORTHOGONAL_TO_TANGENT);
                        PathTransition pathTransition2 = new PathTransition();
                        pathTransition2.setDuration(Duration.millis(700));
                        pathTransition2.setPath(path2);
                        pathTransition2.setNode(a.sphereB);
                        pathTransition2.setOrientation(PathTransition.OrientationType.ORTHOGONAL_TO_TANGENT);
                        ParallelTransition parallelTransition = new ParallelTransition();
                        parallelTransition.getChildren().addAll(pathTransition,pathTransition2);

                        a.getChildren().remove(1, a.getChildren().size());
                        a.getChildren().addAll(a.sphereA, a.sphereB);
                        int s=a.getChildren().size();

                        parallelTransition.setOnFinished(new EventHandler<ActionEvent>()
                        {

                            /**
                             * Empties the current cell and recursively calles the function something on adjacent cells
                             * @param arg0 Event : Clicking of the cell or adding of orbs from adjacent cells
                             * @return Nothing
                             */
                            @Override
                            public void handle(ActionEvent arg0)
                            {
                                long t0,t1;
                                t0=System.currentTimeMillis();
                                Random rand=new Random();
                                int x=rand.nextInt(30001)+20000;
                                double time=x/10000.0;
                                do
                                {
                                    t1=System.currentTimeMillis();
                                }while (t1-t0<time);

                                ParallelTransition p1 = null;
                                ParallelTransition p2 = null;
                                ParallelTransition parallelTransition1 = new ParallelTransition();

                                if (xcoor == 0 && ycoor == 0)
                                {
                                    p1 = something(1,0,1);
                                    p2 = something(0,1,1);
                                }


                                if (xcoor == 0 && ycoor == G.col-1)
                                {
                                    p1 = something(0, G.col-2,1);
                                    p2 = something(1, G.col-1,1);
                                }


                                if (xcoor == G.row-1 && ycoor == 0)
                                {
                                    p1 = something(G.row-1, 1,1);
                                    p2 = something(G.row-2, 0,1);
                                }


                                if (xcoor == G.row-1 && ycoor == G.col-1)
                                {
                                    p1 = something(G.row-2, G.col-1,1);
                                    p2 = something(G.row-1, G.col-2,1);
                                }

                                if (p1 != null && p2 != null)
                                {

                                    parallelTransition1.getChildren().addAll(p1, p2);
                                    parallelTransition1.play();
                                }

                                else if (p1 != null)
                                {
                                    parallelTransition1.getChildren().addAll(p1);
                                    parallelTransition1.play();
                                }

                                else if (p2 != null)
                                {
                                    parallelTransition1.getChildren().addAll(p2);
                                    parallelTransition1.play();
                                }

                                a.getChildren().remove(1, s);

                                Player p=winChecker(xcoor, ycoor,count%num_players);
                                if(p!=null)
                                {
                                    finished=true;
                                    fn(p.num);
                                }

                                playerRemove(players[count%num_players]);
                            }
                        });

                        return parallelTransition;
                    }

                    else
                    {
                        do
                        {
                            count--;
                        }while(players[count%num_players].dead);
                    }
                }

                else if(G.array[xcoor][ycoor].cap==2)
                {
                    if ((flag==0 && colourCheck(players[count%num_players],a.orb1.player)) || flag==1)
                    {
                        G.array[xcoor][ycoor].currentnooforbs=0;
                        a.sphereA=new Sphere(radius);
                        a.sphereA.setTranslateX(translatex);
                        PhongMaterial material1 = new PhongMaterial();
                        material1.setDiffuseColor(Color.rgb(players[count % num_players].getColour().getRed(), players[count % num_players].getColour().getGreen(), players[count % num_players].getColour().getBlue()));
                        a.sphereA.setMaterial(material1);
                        a.sphereB=new Sphere(radius);
                        a.sphereB.setTranslateX(translatex);
                        a.sphereB.setMaterial(material1);
                        a.sphereC=new Sphere(radius);
                        a.sphereC.setTranslateY(-translatex);
                        a.sphereC.setMaterial(material1);

                        Path path = new Path();
                        Path path2=new Path();
                        Path path3=new Path();

                        MoveTo moveTo = new MoveTo();
                        moveTo.setX(0.0f);
                        moveTo.setY(0.0f);

                        if(xcoor==0 || xcoor==G.row-1)
                        {
                            HLineTo hLineTo = new HLineTo();
                            HLineTo hLineTo2 = new HLineTo();
                            VLineTo hLineTo3 = new VLineTo();

                            if(xcoor==0)
                            {
                                hLineTo.setX(pathy);
                                hLineTo2.setX(-pathy);
                                hLineTo3.setY(pathy);
                            }

                            else
                            {
                                hLineTo.setX(pathy);
                                hLineTo2.setX(-pathy);
                                hLineTo3.setY(-pathy);
                            }


                            path.getElements().add(moveTo);
                            path.getElements().add(hLineTo);
                            path2.getElements().add(moveTo);
                            path2.getElements().add(hLineTo2);
                            path3.getElements().add(moveTo);
                            path3.getElements().add(hLineTo3);
                        }

                        if(ycoor==0 || ycoor==G.col-1)
                        {
                            HLineTo hLineTo = new HLineTo();
                            VLineTo hLineTo4 = new VLineTo();
                            VLineTo hLineTo3 = new VLineTo();


                            if(ycoor==0)
                            {
                                hLineTo.setX(pathy);
                                hLineTo4.setY(-pathy);
                                hLineTo3.setY(pathy);
                            }

                            else
                            {
                                hLineTo.setX(-pathy);
                                hLineTo4.setY(-pathy);
                                hLineTo3.setY(pathy);
                            }

                            path.getElements().add(moveTo);
                            path.getElements().add(hLineTo);
                            path2.getElements().add(moveTo);
                            path2.getElements().add(hLineTo4);
                            path3.getElements().add(moveTo);
                            path3.getElements().add(hLineTo3);
                        }


                        PathTransition pathTransition = new PathTransition();
                        pathTransition.setDuration(Duration.millis(700));
                        pathTransition.setPath(path);
                        pathTransition.setNode(a.sphereA);
                        pathTransition.setOrientation(PathTransition.OrientationType.ORTHOGONAL_TO_TANGENT);
                        PathTransition pathTransition2 = new PathTransition();
                        pathTransition2.setDuration(Duration.millis(700));
                        pathTransition2.setPath(path2);
                        pathTransition2.setNode(a.sphereB);
                        pathTransition2.setOrientation(PathTransition.OrientationType.ORTHOGONAL_TO_TANGENT);
                        PathTransition pathTransition3 = new PathTransition();
                        pathTransition3.setDuration(Duration.millis(700));
                        pathTransition3.setPath(path3);
                        pathTransition3.setNode(a.sphereC);
                        pathTransition3.setOrientation(PathTransition.OrientationType.ORTHOGONAL_TO_TANGENT);
                        ParallelTransition parallelTransition =new ParallelTransition();
                        parallelTransition.getChildren().addAll(pathTransition,pathTransition2,pathTransition3);
                        parallelTransition.play();
                        a.getChildren().remove(1,a.getChildren().size());
                        a.getChildren().addAll(a.sphereA,a.sphereB,a.sphereC);
                        int s=a.getChildren().size();

                        parallelTransition.setOnFinished(new EventHandler<ActionEvent>()
                        {

                            /**
                             * Empties the current cell and recursively calles the function something on adjacent cells
                             * @param arg0 Event : Clicking of the cell or adding of orbs from adjacent cells
                             * @return Nothing
                             */
                            @Override
                            public void handle(ActionEvent arg0)
                            {
                                long t0,t1;
                                t0=System.currentTimeMillis();
                                Random rand=new Random();
                                int x=rand.nextInt(30001)+20000;
                                double time=x/10000.0;
                                do
                                {
                                    t1=System.currentTimeMillis();
                                }while (t1-t0<time);

                                ParallelTransition p1 = null;
                                ParallelTransition p2 = null;
                                ParallelTransition p3= null;
                                ParallelTransition parallelTransition1 = new ParallelTransition();

                                if(xcoor==0)
                                {
                                    p1=something(0,ycoor-1,1);
                                    p2=something(0,ycoor+1,1);
                                    p3=something(1,ycoor,1);
                                }

                                else if(xcoor==G.row-1)
                                {
                                    p1=something(G.row-1,ycoor-1,1);
                                    p2=something(G.row-1,ycoor+1,1);
                                    p3=something(G.row-2,ycoor,1);

                                }

                                else if(ycoor==0)
                                {
                                    p1=something(xcoor+1,0,1);
                                    p2=something(xcoor-1,0,1);
                                    p3=something(xcoor,1,1);
                                }

                                else if(ycoor==G.col-1)
                                {
                                    p1=something(xcoor+1,G.col-1,1);
                                    p2=something(xcoor-1,G.col-1,1);
                                    p3=something(xcoor,G.col-2,1);
                                }

                                if(p1!=null && p2!=null && p3!=null)
                                {
                                    parallelTransition1.getChildren().addAll(p1,p2,p3);
                                    parallelTransition1.play();
                                }

                                else if(p1!=null && p2!=null )
                                {
                                    parallelTransition1.getChildren().addAll(p1,p2);
                                    parallelTransition1.play();
                                }

                                else if(p1!=null && p3!=null)
                                {
                                    parallelTransition1.getChildren().addAll(p1,p3);
                                    parallelTransition1.play();
                                }

                                else if(p2!=null && p3!=null)
                                {
                                    parallelTransition1.getChildren().addAll(p2,p3);
                                    parallelTransition1.play();
                                }

                                else if(p1!=null )
                                {
                                    parallelTransition1.getChildren().addAll(p1);
                                    parallelTransition1.play();
                                }

                                else if(p2!=null )
                                {
                                    parallelTransition1.getChildren().addAll(p2);
                                    parallelTransition1.play();
                                }

                                else if(p3!=null)
                                {
                                    parallelTransition1.getChildren().addAll(p3);
                                    parallelTransition1.play();
                                }

                                a.getChildren().remove(1,s);
                                Player p=winChecker(xcoor, ycoor,count%num_players);

                                if(p!=null)
                                {
                                    finished=true;
                                    fn(p.num);
                                }

                                playerRemove(players[count%num_players]);
                            }
                        });

                        return parallelTransition;
                    }

                    else
                    {
                        do
                        {
                            count--;
                        }while(players[count%num_players].dead);
                    }
                }

                else if(G.array[xcoor][ycoor].cap==3)
                {
                    if ((flag==0 && colourCheck(players[count%num_players],a.orb1.player)) || flag==1)
                    {
                        G.array[xcoor][ycoor].currentnooforbs=0;
                        a.sphereA=new Sphere(radius);
                        a.sphereA.setTranslateX(translatex);
                        PhongMaterial material1 = new PhongMaterial();
                        material1.setDiffuseColor(Color.rgb(players[count % num_players].getColour().getRed(), players[count % num_players].getColour().getGreen(), players[count % num_players].getColour().getBlue()));
                        a.sphereA.setMaterial(material1);
                        a.sphereB=new Sphere(radius);
                        a.sphereB.setTranslateX(translatex);
                        a.sphereB.setMaterial(material1);
                        a.sphereC=new Sphere(radius);
                        a.sphereC.setTranslateY(-translatex);
                        a.sphereC.setMaterial(material1);
                        a.sphereD=new Sphere(radius);
                        a.sphereD.setMaterial(material1);

                        Path path = new Path();
                        Path path2=new Path();
                        Path path3=new Path();
                        Path path4=new Path();

                        MoveTo moveTo = new MoveTo();
                        moveTo.setX(0.0f);
                        moveTo.setY(0.0f);

                        HLineTo hLineTo = new HLineTo();
                        hLineTo.setX(pathy);
                        VLineTo hLineTo2 = new VLineTo();
                        hLineTo2.setY(pathy);
                        HLineTo hLineTo3 = new HLineTo();
                        hLineTo3.setX(-pathy);
                        VLineTo hLineTo4 = new VLineTo();
                        hLineTo4.setY(-pathy);

                        path.getElements().add(moveTo);
                        path.getElements().add(hLineTo);
                        path2.getElements().add(moveTo);
                        path2.getElements().add(hLineTo2);
                        path3.getElements().add(moveTo);
                        path3.getElements().add(hLineTo3);
                        path4.getElements().add(moveTo);
                        path4.getElements().add(hLineTo4);
                        PathTransition pathTransition = new PathTransition();
                        pathTransition.setDuration(Duration.millis(700));
                        pathTransition.setPath(path);
                        pathTransition.setNode(a.sphereA);
                        pathTransition.setOrientation(PathTransition.OrientationType.ORTHOGONAL_TO_TANGENT);
                        PathTransition pathTransition2 = new PathTransition();
                        pathTransition2.setDuration(Duration.millis(700));
                        pathTransition2.setPath(path2);
                        pathTransition2.setNode(a.sphereB);
                        pathTransition2.setOrientation(PathTransition.OrientationType.ORTHOGONAL_TO_TANGENT);
                        PathTransition pathTransition3 = new PathTransition();
                        pathTransition3.setDuration(Duration.millis(700));
                        pathTransition3.setPath(path3);
                        pathTransition3.setNode(a.sphereC);
                        pathTransition3.setOrientation(PathTransition.OrientationType.ORTHOGONAL_TO_TANGENT);
                        PathTransition pathTransition4 = new PathTransition();
                        pathTransition4.setDuration(Duration.millis(700));
                        pathTransition4.setPath(path4);
                        pathTransition4.setNode(a.sphereD);
                        pathTransition4.setOrientation(PathTransition.OrientationType.ORTHOGONAL_TO_TANGENT);
                        ParallelTransition parallelTransition =new ParallelTransition();
                        parallelTransition.getChildren().addAll(pathTransition,pathTransition2,pathTransition3,pathTransition4);
                        parallelTransition.play();

                        a.getChildren().remove(1,a.getChildren().size());
                        a.getChildren().addAll(a.sphereA,a.sphereB,a.sphereC,a.sphereD);

                        int s=a.getChildren().size();

                        parallelTransition.setOnFinished(new EventHandler<ActionEvent>()
                        {
                            /**
                             * Empties the current cell and recursively calles the function something on adjacent cells
                             * @param arg0 Event : Clicking of the cell or adding of orbs from adjacent cells
                             * @return Nothing
                             */
                            @Override
                            public void handle(ActionEvent arg0)
                            {
//                                Random x=new Random();
//                                int b=x.nextInt(100);
//                                try {
//
//                                } catch (InterruptedException e) {
//                                    e.printStackTrace();
//                                }

                                long t0,t1;
                                Random rand=new Random();
                                int x=rand.nextInt(30001)+20000;
                                double time=x/10000.0;
                                t0=System.currentTimeMillis();
                                do
                                {
                                    t1=System.currentTimeMillis();
                                }while (t1-t0<time);

                                ParallelTransition p1 = null;
                                ParallelTransition p2 = null;
                                ParallelTransition p3= null;
                                ParallelTransition p4= null;
                                ParallelTransition parallelTransition1 = new ParallelTransition();

                                p1=something(xcoor+1,ycoor,1);
                                p2=something(xcoor-1,ycoor,1);
                                p3=something(xcoor,ycoor+1,1);
                                p4=something(xcoor,ycoor-1,1);
                                if(p1!=null && p2!=null && p3!=null && p4!=null )
                                {
                                    parallelTransition1.getChildren().addAll(p1,p2,p3,p4);
                                    parallelTransition1.play();
                                }

                                else if(p1!=null && p2!=null && p3!=null )
                                {
                                    parallelTransition1.getChildren().addAll(p1,p2,p3);
                                    parallelTransition1.play();
                                }

                                else if(p1!=null && p2!=null && p4!=null )
                                {
                                    parallelTransition1.getChildren().addAll(p1,p2,p4);
                                    parallelTransition1.play();
                                }

                                else if(p2!=null && p3!=null && p4!=null )
                                {
                                    parallelTransition1.getChildren().addAll(p2,p3,p4);
                                    parallelTransition1.play();
                                }

                                else if(p1!=null && p3!=null && p4!=null )
                                {
                                    parallelTransition1.getChildren().addAll(p1,p3,p4);
                                    parallelTransition1.play();
                                }

                                else if(p1!=null && p2!=null )
                                {
                                    parallelTransition1.getChildren().addAll(p1,p2);
                                    parallelTransition1.play();
                                }

                                else if(p1!=null && p3!=null  )
                                {
                                    parallelTransition1.getChildren().addAll(p1,p3);
                                    parallelTransition1.play();
                                }

                                else if(p1!=null&&p4!=null )
                                {
                                    parallelTransition1.getChildren().addAll(p1,p4);
                                    parallelTransition1.play();
                                }

                                else if( p2!=null && p3!=null)
                                {
                                    parallelTransition1.getChildren().addAll(p2,p3);
                                    parallelTransition1.play();
                                }

                                else if(p2!=null &&  p4!=null )
                                {
                                    parallelTransition1.getChildren().addAll(p2,p4);
                                    parallelTransition1.play();
                                }

                                else if(p3!=null && p4!=null )
                                {
                                    parallelTransition1.getChildren().addAll(p3,p4);
                                    parallelTransition1.play();
                                }

                                else if(p1!=null)
                                {
                                    parallelTransition1.getChildren().addAll(p1);
                                    parallelTransition1.play();
                                }

                                else if(p2!=null)
                                {
                                    parallelTransition1.getChildren().addAll(p2);
                                    parallelTransition1.play();
                                }

                                else if(p3!=null)
                                {
                                    parallelTransition1.getChildren().addAll(p3);
                                    parallelTransition1.play();
                                }

                                else if(p4!=null)
                                {
                                    parallelTransition1.getChildren().addAll(p4);
                                    parallelTransition1.play();
                                }


                                a.getChildren().remove(1,s);

                                Player p=winChecker(xcoor, ycoor,count%num_players);
                                if(p!=null)
                                {
                                    finished=true;
                                    fn(p.num);
                                }

                                playerRemove(players[count%num_players]);

                            }
                        });

                        return parallelTransition;

                    }

                    else
                    {
                        do
                        {
                            count--;
                        }while(players[count%num_players].dead);
                    }
                }
            }

            else
            {
                if (G.array[xcoor][ycoor].currentnooforbs == 0)
                {
                    G.array[xcoor][ycoor].currentnooforbs++;
                    a.orb1.player = players[count % num_players];
                    a.sphereA=new Sphere(radius);
                    PhongMaterial material1 = new PhongMaterial();
                    material1.setDiffuseColor(Color.rgb(players[count % num_players].getColour().getRed(), players[count % num_players].getColour().getGreen(), players[count % num_players].getColour().getBlue()));
                    a.sphereA.setMaterial(material1);
                    Group group=new Group();
                    group.getChildren().addAll(a.sphereA);
                    a.getChildren().addAll(group);
                    rotateAroundYAxis(group).play();
                }

                else if (G.array[xcoor][ycoor].currentnooforbs == 1)
                {
                    if ((flag==0 && colourCheck(players[count%num_players],a.orb1.player))||flag==1)
                    {
                        G.array[xcoor][ycoor].currentnooforbs++;
                        a.orb1.player = players[count % num_players];
                        a.sphereA=new Sphere(radius);
                        a.sphereA.setTranslateX(translatex);
                        PhongMaterial material1 = new PhongMaterial();
                        material1.setDiffuseColor(Color.rgb(players[count % num_players].getColour().getRed(), players[count % num_players].getColour().getGreen(), players[count % num_players].getColour().getBlue()));
                        a.sphereA.setMaterial(material1);
                        a.sphereB=new Sphere(radius);
                        a.sphereB.setTranslateX(-translatex);
                        PhongMaterial material2 = new PhongMaterial();
                        material2.setDiffuseColor(Color.rgb(players[count % num_players].getColour().getRed(), players[count % num_players].getColour().getGreen(), players[count % num_players].getColour().getBlue()));
                        a.sphereB.setMaterial(material2);
                        Group group=new Group();
                        group.getChildren().addAll(a.sphereA,a.sphereB);
                        if(a.getChildren().size()<5)
                            a.getChildren().remove(1,a.getChildren().size());
                        else
                            a.getChildren().remove(a.getChildren().size()-1);
                        a.getChildren().addAll(group);
                        rotateAroundYAxis(group).play();
                    }

                    else
                    {
                        do
                        {
                            count--;
                        }while(players[count%num_players].dead);
                    }
                }

                else if(G.array[xcoor][ycoor].currentnooforbs == 2)
                {
                    if ((flag==0  && colourCheck(players[count%num_players],a.orb1.player))||flag==1)
                    {
                        G.array[xcoor][ycoor].currentnooforbs++;
                        a.orb1.player = players[count % num_players];
                        a.sphereA=new Sphere(radius);
                        a.sphereA.setTranslateX(translatex);
                        PhongMaterial material1 = new PhongMaterial();
                        material1.setDiffuseColor(Color.rgb(players[count % num_players].getColour().getRed(), players[count % num_players].getColour().getGreen(), players[count % num_players].getColour().getBlue()));
                        a.sphereA.setMaterial(material1);
                        a.sphereB=new Sphere(radius);
                        a.sphereB.setTranslateX(-translatex);
                        PhongMaterial material2 = new PhongMaterial();
                        material2.setDiffuseColor(Color.rgb(players[count % num_players].getColour().getRed(), players[count % num_players].getColour().getGreen(), players[count % num_players].getColour().getBlue()));
                        a.sphereB.setMaterial(material2);
                        a.sphereC=new Sphere(radius);
                        a.sphereC.setTranslateY(-translatex);
                        PhongMaterial material3 = new PhongMaterial();
                        material3.setDiffuseColor(Color.rgb(players[count % num_players].getColour().getRed(), players[count % num_players].getColour().getGreen(), players[count % num_players].getColour().getBlue()));
                        a.sphereC.setMaterial(material3);
                        Group group=new Group();
                        group.getChildren().addAll(a.sphereA,a.sphereB,a.sphereC);
                        if(a.getChildren().size()<5)
                            a.getChildren().remove(1,a.getChildren().size());
                        else
                            a.getChildren().remove(a.getChildren().size()-1);
                        a.getChildren().addAll(group);
                        rotateAroundYAxis2(group).play();
                    }

                    else
                    {
                        do
                        {
                            count--;
                        }while(players[count%num_players].dead);
                    }
                }
            }

            return null;
        }

        /**
         * Default constructor of class Tile
         * Creates a tile based on the gridsize.
         * Also sets a default color for the border of the tile
         * and Positions the tile on the scene
         * @return Nothing
         */
        public Tile()
        {
            if(grid_size==1)
                border=new Rectangle(65,65);
            else
                border=new Rectangle(40,40);

            border.setFill(Color.BLACK);
            border.setStroke(Color.rgb(players[(count+1)%num_players].getColour().getRed(),players[(count+1)%num_players].getColour().getGreen(),players[(count+1)%num_players].getColour().getBlue()));
            setAlignment(Pos.CENTER);
            getChildren().addAll(border);
        }

        /**
         * Function to rotate a group of 2 orbs.
         * Orbs rotate around the Zaxis
         * They rotate continously until they are split.
         * @param node The Group of orbs which are to be rotated
         * @return RotateTransition This needs to be played for the orbs to rotate
         */
        private RotateTransition rotateAroundYAxis(Node node)
        {
            RotateTransition rotate = new RotateTransition(Duration.seconds(3),node);
            rotate.setAxis(Rotate.Z_AXIS);
            rotate.setFromAngle(0);
            rotate.setByAngle(360);
            rotate.setInterpolator(Interpolator.LINEAR);
            rotate.setCycleCount(RotateTransition.INDEFINITE);
            return rotate;
        }

        /**
         * Function to rotate a group of 3 orbs.
         * Orbs rotate around the Zaxis
         * They rotate continously until they are split.
         * @param node The Group of orbs which are to be rotated
         * @return RotateTransition This needs to be played for the orbs to rotate
         */
        private RotateTransition rotateAroundYAxis2(Node node)
        {
            RotateTransition rotate = new RotateTransition(Duration.seconds(2),node);
            rotate.setAxis(Rotate.Z_AXIS);
            rotate.setFromAngle(0);
            rotate.setByAngle(360);
            rotate.setInterpolator(Interpolator.LINEAR);
            rotate.setCycleCount(RotateTransition.INDEFINITE);
            return rotate;
        }

        /**
         * Creates tiles and also handles what happens when a tile is clicked.
         * @param s A String sent to differentiate the 2 types of tiles
         * @return Nothing
         */
        public Tile(String s)
        {
            orb1=new Orb1();
            if(grid_size==1)
                border=new Rectangle(70,70);
            else
                border=new Rectangle(42,42);

            border.setFill(Color.TRANSPARENT);
            border.setStroke(Color.rgb(players[(count+1)%num_players].getColour().getRed(),players[(count+1)%num_players].getColour().getGreen(),players[(count+1)%num_players].getColour().getBlue()));

            border.setFill(Color.TRANSPARENT);
            setAlignment(Pos.CENTER);
            getChildren().addAll(border);




            setOnMouseClicked(event ->
            {
                mouse();
            });
        }

        /**
         * Stores the current state of the game and calls the function something
         * to add and split orbs in the cells
         * @return Nothing
         */
        public void mouse()
        {
            undo.setDisable(false);
            int temp1=count;
            do
            {
                temp1++;
            }while(players[temp1%num_players].dead);

            if(this.orb1.player==null || colourCheck(players[(temp1)%num_players],this.orb1.player))
            {
                if (G.row == 9 && G.col == 6)
                    G_old = new Grid(1);
                else
                    G_old = new Grid(2);

                for (int p = 0; p < G.col; p++)
                {
                    for (int q = 0; q < G.row; q++)
                    {

                        G_old.array[q][p].currentnooforbs = G.array[q][p].currentnooforbs;
                        G_old.array[q][p].cap = G.array[q][p].cap;

                    }
                }

                currplay = count;
                for(int i=0;i<num_players;i++)
                {
                    deadarray[i]=players[i].dead;
                }

                at = new ArrayList<Tile>();

                for (int j = 0; j < G.col; j++)
                    for (int i = 0; i < G.row; i++)
                    {

                        Tile temp = new Tile();
                        temp.orb1 = new Orb1();
                        Colour colour = new Colour(0, 0, 0);
                        temp.orb1.player = new Player(colour,0);


                        if (((Tile) root.getChildren().get(G.row*G.col + i + G.row* j)).orb1.player != null)
                        {
                            Tile x = ((Tile) root.getChildren().get(G.row*G.col + i + G.row * j));
                            x.orb1.red = x.orb1.player.getColour().getRed();
                            x.orb1.green = x.orb1.player.getColour().getGreen();
                            x.orb1.blue = x.orb1.player.getColour().getBlue();
                            int red = x.orb1.red;
                            int green = x.orb1.green;
                            int blue = x.orb1.blue;
                            temp.orb1.player.getColour().setRed(red);
                            temp.orb1.player.getColour().setGreen(green);
                            temp.orb1.player.getColour().setBlue(blue);
                            temp.orb1.player.num=x.orb1.player.num;
                        }

                        temp.sphereA = ((Tile) root.getChildren().get(G.row*G.col + i + G.row * j)).sphereA;
                        temp.sphereB = ((Tile) root.getChildren().get(G.row*G.col + i + G.row * j)).sphereB;
                        temp.sphereC = ((Tile) root.getChildren().get(G.row*G.col + i + G.row * j)).sphereC;
                        temp.sphereD = ((Tile) root.getChildren().get(G.row*G.col + i + G.row * j)).sphereD;
                        at.add(temp);
                    }
            }

            //printcol();

            do
            {
                count++;
            }while(players[count%num_players].dead);

            Sphere sphere=null;
            Sphere sphere2=null;
            Sphere sphere3=null;

            if(grid_size==1)
            {
                int xcoor=(int)(this.getTranslateY()-50)/70;
                int ycoor=(int)this.getTranslateX()/70;
                ParallelTransition p=something(xcoor,ycoor,0);
                if(p!=null)
                {
                    p.play();
                }
            }


            else //grid size==2
            {

                int xcoor = (int) (this.getTranslateY() - 50) / 40;
                int ycoor = (int) this.getTranslateX() / 40;
                ParallelTransition p=something(xcoor,ycoor,0);
                if(p!=null)
                {
                    p.play();
                }
            }

            int temp=count;
            do
            {
                temp++;
            }while(players[temp%num_players].dead);

            booi(temp);
            if(temp%num_players==0 && mode==1) {
                computer.setDisable(true);
                undo.setDisable(true);
                for(int i=0; i<G.col; i++) {
                    for (int j = 0; j < G.row; j++) {
                        Tile a = (Tile) root.getChildren().get(G.row * G.col + j + G.row * i);
                        a.setDisable(false);
                    }
                }
            }
            else if(temp%num_players==1 && mode==1) {
                computer.setDisable(false);
                undo.setDisable(false);
                for(int i=0; i<G.col; i++) {
                    for (int j = 0; j < G.row; j++) {
                        Tile a = (Tile) root.getChildren().get(G.row * G.col + j + G.row * i);
                        a.setDisable(true);
                    }
                }
            }
        }
    }

//    public void printcol()
//    {
//        for(int i=0; i<G.col; i++)
//        {
//            for(int j=0; j<G.row; j++)
//            {
//                Tile a=(Tile)root.getChildren().get(G.row*G.col+j+G.row*i);
//                if(a.orb1.player!=null)
//                    System.out.print(a.orb1.player.getColour()+" ");
//                else
//                    System.out.print("null ");
//            }
//
//            System.out.println();
//        }
//    }

    /**
     * Function to check if game is over
     * @return boolean Returns true if game is over, false otherwise.
     */
    public boolean getfinished()
    {
        return finished;
    }

    /**
     * Function called to display a popup when a player wins.
     * @param v Index of Player who has won the game.
     * @return Nothing
     */
    public void fn(int v)
    {
        if(var==false)
        {
            Alert alert=new Alert(AlertType.INFORMATION);
            alert.setTitle("Chain Reaction");
            alert.setHeaderText("Congratulations");

            switch(v)
            {
                case 1:alert.setContentText("Player 1 won!");
                    break;
                case 2:
                        if(mode==0)
                            alert.setContentText("Player 2 won!");
                        else
                            alert.setContentText("Computer won!");
                    break;
                case 3:alert.setContentText("Player 3 won!");
                    break;
                case 4:alert.setContentText("Player 4 won!");
                    break;
                case 5:alert.setContentText("Player 5 won!");
                    break;
                case 6:alert.setContentText("Player 6 won!");
                    break;
                case 7:alert.setContentText("Player 7 won!");
                    break;
                case 8:alert.setContentText("Player 8 won!");
                    break;
            }

            alert.setOnHidden(evt ->
            {
                for(int i=0;i<num_players;i++)
                {
                    players[i].dead=false;
                }
                alert.close();
                Parent root=null;
                try
                {
                    root = FXMLLoader.load(getClass().getResource("MainPage.fxml"));
                }

                catch (IOException e)
                {
                    e.printStackTrace();
                }

                Scene scene = new Scene(root);
                primarystage.setScene(scene);
                primarystage.show();
            });

            alert.show();
            var=true;
            return;
        }

        return;
    }


    /**
     * Class used to restore the data of the game post desrialization.
     * A new game is launched with the same data as that of the last running game.
     * Used when user clicks on resume button.
     *
     * @author Surabhi S. Nath and Raghav Sood
     * @version 1.4
     * @since 2017-11-13
     */
    class UltuGameData
    {
        Player[] array;
        int count;
        Grid grid;
        ArrayList<Tile> at;
        int grid_size;
        Cell[][] tiles;
        Stage primarystage;
        int mode;


        /**
         * Initializes the instance variables of Game2 class with the old game's data
         * @param array Array of players
         * @param count Current count to identify next player to move
         * @param grid Grid of cells storing the current number of orbs in each cell
         * @param grid_size 1/2 based on the type of grid
         * @param tiles A matrix of tiles to get the current player in each cell
         * @param primarystage PrimaryStage to launch the application
         * @param num_players Current number of players
         * @param mode Single Player or Multiplayer
         * @return Nothing
         * @throws Exception
         */
        public UltuGameData(Player[] array, int count, Grid grid,int grid_size,Cell[][] tiles,Stage primarystage,int num_players,int mode) throws Exception
        {
            this.array = array;
            this.count = count;
            this.grid = grid;
            this.at = at;
            this.grid_size=grid_size;
            this.primarystage=primarystage;
            this.tiles=tiles;
            this.mode=mode;

            Game2 game=new Game2();
            game.count=this.count;
            game.mode=this.mode;
            Game2.players=this.array;
            game.G=this.grid;
            game.grid_size=this.grid_size;
            game.primarystage=this.primarystage;
            game.num_players=num_players;
            game.deadarray = new boolean[8];
            game.play();

            int radius=0;
            int translatex=0;
            double pathy=0.0f;
            if(grid_size==1)
            {
                radius = 15;
                translatex=10;
            }

            else
            {
                radius = 10;
                translatex=6;
            }

            int h=0;
            int temp=game.count;
            do
            {
                temp++;
            }while(Game2.players[temp%num_players].dead);

            game.booi(temp);
            if(game.mode==1 && temp%num_players==1) {
                System.out.println("hi");
                for (int i = 0; i < game.G.col; i++) {
                    for (int j = 0; j <game. G.row; j++) {
                        Tile a = (Tile) game.root.getChildren().get(game.G.row * game.G.col + j + game.G.row * i);
                        a.setDisable(true);
                    }
                }
                game.computer.setDisable(false);
            }

            for(int j=0; j<game.G.col; j++)
                for(int i=0; i<game.G.row; i++)
                {
                    Tile x=(Tile)game.root.getChildren().get(game.G.row*game.G.col+h);
                    x.orb1=new Orb1();

                    if(this.grid.array[i][j].currentnooforbs==1)
                    {
                        game.G.array[i][j].currentnooforbs=1;
                        x.orb1=this.tiles[i][j].o[0];

                        x.orb1.player=this.tiles[i][j].o[0].player;
                        x.orb1.player.getColour().setRed(this.tiles[i][j].o[0].player.getColour().getRed());
                        x.orb1.player.getColour().setGreen(this.tiles[i][j].o[0].player.getColour().getGreen());
                        x.orb1.player.getColour().setBlue(this.tiles[i][j].o[0].player.getColour().getBlue());
                        //x.border=at.get(h).border;

                        x.sphereA = new Sphere(radius);
                        PhongMaterial material1 = new PhongMaterial();
                        material1.setDiffuseColor(Color.rgb(x.orb1.player.getColour().getRed(),x.orb1.player.getColour().getGreen(),x.orb1.player.getColour().getBlue()));
                        x.sphereA.setMaterial(material1);

                        Group group=new Group();
                        group.getChildren().addAll(x.sphereA);
                        x.getChildren().addAll(group);
                        x.rotateAroundYAxis(group).play();
                    }


                    else if(this.grid.array[i][j].currentnooforbs==2)
                    {
                        game.G.array[i][j].currentnooforbs=2;
                        x.orb1=this.tiles[i][j].o[0];
                        x.orb1.player=this.tiles[i][j].o[0].player;
                        x.orb1.player.getColour().setRed(this.tiles[i][j].o[0].player.getColour().getRed());
                        x.orb1.player.getColour().setGreen(this.tiles[i][j].o[0].player.getColour().getGreen());
                        x.orb1.player.getColour().setBlue(this.tiles[i][j].o[0].player.getColour().getBlue());
                        //x.border=at.get(h).border;
                        x.sphereA=new Sphere(radius);
                        x.sphereA.setTranslateX(translatex);
                        PhongMaterial material1 = new PhongMaterial();
                        material1.setDiffuseColor(Color.rgb(x.orb1.player.getColour().getRed(),x.orb1.player.getColour().getGreen(),x.orb1.player.getColour().getBlue()));
                        x.sphereA.setMaterial(material1);
                        x.sphereB=new Sphere(radius);
                        x.sphereB.setTranslateX(-translatex);
                        PhongMaterial material2 = new PhongMaterial();
                        material2.setDiffuseColor(Color.rgb(x.orb1.player.getColour().getRed(),x.orb1.player.getColour().getGreen(),x.orb1.player.getColour().getBlue()));
                        x.sphereB.setMaterial(material2);

                        Group group=new Group();
                        group.getChildren().addAll(x.sphereA,x.sphereB);
                        x.getChildren().addAll(group);
                        x.rotateAroundYAxis(group).play();
                    }


                    else if(this.grid.array[i][j].currentnooforbs==3)
                    {
                        game.G.array[i][j].currentnooforbs=3;
                        x.orb1=this.tiles[i][j].o[0];
                        x.orb1.player=this.tiles[i][j].o[0].player;
                        x.orb1.player.getColour().setRed(this.tiles[i][j].o[0].player.getColour().getRed());
                        x.orb1.player.getColour().setGreen(this.tiles[i][j].o[0].player.getColour().getGreen());
                        x.orb1.player.getColour().setBlue(this.tiles[i][j].o[0].player.getColour().getBlue());
                        //x.border=at.get(h).border;

                        x.sphereA=new Sphere(radius);
                        x.sphereA.setTranslateX(translatex);
                        PhongMaterial material1 = new PhongMaterial();
                        material1.setDiffuseColor(Color.rgb(x.orb1.player.getColour().getRed(),x.orb1.player.getColour().getGreen(),x.orb1.player.getColour().getBlue()));
                        x.sphereA.setMaterial(material1);
                        x.sphereB=new Sphere(radius);
                        x.sphereB.setTranslateX(-translatex);
                        PhongMaterial material2 = new PhongMaterial();
                        material2.setDiffuseColor(Color.rgb(x.orb1.player.getColour().getRed(),x.orb1.player.getColour().getGreen(),x.orb1.player.getColour().getBlue()));
                        x.sphereB.setMaterial(material2);
                        x.sphereC=new Sphere(radius);
                        x.sphereC.setTranslateY(-translatex);
                        PhongMaterial material3 = new PhongMaterial();
                        material3.setDiffuseColor(Color.rgb(x.orb1.player.getColour().getRed(),x.orb1.player.getColour().getGreen(),x.orb1.player.getColour().getBlue()));
                        x.sphereC.setMaterial(material3);

                        Group group=new Group();
                        group.getChildren().addAll(x.sphereA,x.sphereB,x.sphereC);
                        x.getChildren().addAll(group);
                        x.rotateAroundYAxis(group).play();
                    }


                    h++;
                }
        }
    }
}


/**
 * Class used to save the data of the game before serialization(while exiting game)
 *
 * @author Surabhi S. Nath and Raghav Sood
 * @version 1.4
 * @since 2017-11-13
 */
class Gamedata implements Serializable
{
    public static final long serialVersionUID=1L;
    Player[] array;
    int count;
    Grid g;
    boolean finished;
    int grid_size;
    Cell[][] tiles;
    int num_players;
    int mode;

    /**
     * @param array Array of players
     * @param count Current count to identify next player to move
     * @param g Grid of cells storing the current number of orbs in each cell
     * @param finished Stores whether game is over or not
     * @param grid_size 1/2 based on the type of grid
     * @param tiles A matrix of tiles to get the current player in each cell
     * @param num_players Current number of players
     * @param mode Single Player or MultiPlayer
     */
    public Gamedata(Player[] array, int count, Grid g, boolean finished,int grid_size, Cell[][] tiles,int num_players,int mode)
    {
        this.array = array;
        this.count = count;
        this.g = g;
        this.finished=finished;
        this.grid_size=grid_size;
        this.tiles=tiles;
        this.num_players=num_players;
        this.mode=mode;
    }
}

