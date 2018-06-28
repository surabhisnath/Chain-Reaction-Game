package ChainReaction;

import java.io.*;


/**
 * CHAIN REACTION GAME:
 * A multiplayer game where the aim is to remove opponents' orbs from the grid
 * @author Surabhi S Nath, Raghav Sood
 * @version 1.4
 * @since 2017-10-18
 *
 */


class App
{
    private static App app=null;
    public static App getInstance() throws Exception {
        if(app==null)
        {
            app=new App();
        }
        return app;
    }
    public MainPage mainPage;
    public Player players[];


    /**
     * This is App constructor. Here, the Players are created, older game id deserialized,
     * Mainpage is created and launched
     * @throws Exception
     */
    private App() throws Exception
    {
        players=new Player[8];
        players[0]=new Player(new Colour(255,0,0),1);
        players[1]=new Player(new Colour(0,255,0),2);
        players[2]=new Player(new Colour(0,0,255),3);
        players[3]=new Player(new Colour(255,255,0),4);
        players[4]=new Player(new Colour(255,0,255),5);
        players[5]=new Player(new Colour(0,255,255),6);
        players[6]=new Player(new Colour(255,128,0),7);
        players[7]=new Player(new Colour(255,255,255),8);


        //serialize(gamedata);
        Gamedata gamedata=deserialize();
        Game2 game=new Game2();

        game.finished=gamedata.finished;
        mainPage=new MainPage(players,game);
        MainPage.launch(MainPage.class);
    }

    /**
     * @param gamedata
     * @throws IOException
     */
    public static void serialize(Gamedata gamedata) throws IOException
    {
        ObjectOutputStream out = null;
        try
        {
            out=new	ObjectOutputStream(new FileOutputStream("out.txt"));
            out.writeObject(gamedata);
        }

        finally
        {
            out.close();
        }
    }


    /**
     * @return object holding data of the game
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public static Gamedata deserialize() throws	IOException, ClassNotFoundException
    {
        ObjectInputStream in=null;
        Gamedata gamedata=null;
        try
        {
            in=new ObjectInputStream(new FileInputStream("out.txt"));
            gamedata=(Gamedata)in.readObject();
        }

        finally
        {
            in.close();
        }
        return gamedata;
    }
}



/**
 * Class Player holds the properties of a Player in the game.
 * Every Player has a number, a boolean indicating if Player has lost and an associated Colour
 * @author Surabhi S Nath, Raghav Sood
 * @version 1.3
 * @since 2017-10-18
 *
 */
class Player implements Serializable
{
    public static final long serialVersionUID=1L;
    int num;
    boolean dead;
    private Colour colour;

    public Player(Colour col, int n)
    {
        colour=col;
        num=n;
    }

    /**
     * @return Colour of the Player
     */
    public Colour getColour()
    {
        return colour;
    }
}




/**
 * This class maintains properties of the Grid in the game.
 * It includes number of rows, columns and an array of cells
 * It implements Serializable
 * @author Surabhi S Nath, Raghav Sood
 * @version 1.1
 * @since 2017-10-18
 *
 */
class Grid implements Serializable
{
    public static final long serialVersionUID=1L;
    int row;
    int col;
    Cell[][] array;

    public Grid(int g)
    {
        if(g==1)
        {
            row=9;
            col=6;
        }

        else
        {
            row=15;
            col=10;
        }

        array=new Cell[row][col];
        for(int i=1; i<row-1; i++)
        {
            for(int j=1; j<col-1; j++)
                array[i][j]=new Cell(3,0);
        }

        for(int j=1; j<col-1; j++)
        {
            array[0][j]=new Cell(2,0);
            array[row-1][j]=new Cell(2,0);
        }

        for(int i=1; i<row-1; i++)
        {
            array[i][0]=new Cell(2,0);
            array[i][col-1]=new Cell(2,0);
        }

        array[0][0]=new Cell(1,0);
        array[0][col-1]=new Cell(1,0);
        array[row-1][0]=new Cell(1,0);
        array[row-1][col-1]=new Cell(1,0);
    }
}




/**
 * This class holds properties of each cell in the Grid
 * Properties include capacity of the cell, current number of orbs in the cell, the orb in the cell
 * Implements Serializable
 * @author Surabhi S Nath, Raghav Sood
 * @version 1.1
 * @since 2017-10-18
 */
class Cell implements Serializable
{
    public static final long serialVersionUID=1L;
    int cap;
    int currentnooforbs;
    Orb1 o[];

    public Cell(int c, int cno)
    {
        cap=c;
        currentnooforbs=cno;
        o=new Orb1[cap];
    }
}


/**
 * This class holds properties of the Orb
 * It consists of Player to which the Orb belongs
 * @author Surabhi S Nath, Raghav Sood
 * @version 1.2
 * @since 2017-10-18
 */
abstract class Orb implements Serializable
{
    public static final long serialVersionUID=1L;
    Player player;
    int red;
    int green;
    int blue;

    abstract void revolve();
}

class Orb1 extends Orb
{
    @Override

    void revolve()
    {

    }
}



/**
 * This class holds the Colour properties of each Player.
 * It stores red, green, blue components of the Colour
 * It implements Serializable
 * @author Surabhi S Nath, Raghav Sood
 * @version 1.2
 * @since 2017-10-18
 */
class Colour implements Serializable
{
    public static final long serialVersionUID=1L;
    private int red;
    private int green;
    private int blue;

    public Colour(int a, int b, int c)
    {
        red=a;
        green=b;
        blue=c;
    }

    /**
     * Set methods assign the Colour component fields
     * @param r red/green/blue component
     */
    public void setRed(int r)
    {
        red=r;
    }

    public void setGreen(int g)
    {
        green=g;
    }

    public void setBlue(int b)
    {
        blue=b;
    }


    /**
     * Get methods
     * @return integers representing red/green/blue components of Colour
     */
    public int getRed()
    {
        return red;
    }

    public int getGreen()
    {
        return green;
    }

    public int getBlue()
    {
        return blue;
    }
}



/**
 * This class creates the App object and calls its constructor
 * @author Surabhi S Nath, Raghav Sood
 * @version 1.1
 * @since 2017-10-18
 */
public class Main
{
    static App app;

    public static void main(String[] args) throws Exception
    {
        app=App.getInstance();
    }
}
