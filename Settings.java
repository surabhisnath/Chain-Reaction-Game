package ChainReaction;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * This class represents Settings
 * It holds an array of Players, an instance of the MainPage
 * @author Surabhi S Nath, Raghav Sood
 * @version 1.2
 * @since 2017-10-18
 */
public class Settings extends Application
{
	static Player players[];
	static Stage ps;
	static MainPage mainpage;

	public Settings()
	{

	}

	public Settings(Player[] players, Stage s, MainPage mp)
	{
		mainpage=mp;
		this.players=players;
		ps=s;
	}

	public void start(Stage primaryStage) throws Exception
	{
		Parent root=FXMLLoader.load(getClass().getResource("Settings.fxml"));
		Scene scene = new Scene(root);
		primaryStage.setScene(scene);
		primaryStage.show();
	}

	/**
	 * This method displays the Settings page
	 * @throws Exception
	 */
	public void display() throws Exception
	{
		Parent root=FXMLLoader.load(getClass().getResource("Settings.fxml"));
		Scene scene = new Scene(root);
		ps.setScene(scene);
		ps.show();
	}

	/**
	 * This method is called when the Back button is pressed
	 * It displays the MainPage
	 * @throws Exception
	 */
	public void displaymp() throws Exception
	{
		mainpage.display();
	}


	/**
	 * Go to RGB methods go to corresponding Player's set RGB options by instantiating a ChooseColour object
	 * @throws Exception
	 */
	public void gottoRGB1() throws Exception
	{
		ChooseColour obj=new ChooseColour(players[0],players,this);
		obj.start(ps);
	}

	public void gottoRGB2() throws Exception
	{
		ChooseColour obj=new ChooseColour(players[1],players,this);
		obj.start(ps);
	}

	public void gottoRGB3() throws Exception
	{
		ChooseColour obj=new ChooseColour(players[2],players,this);
		obj.start(ps);
	}

	public void gottoRGB4() throws Exception
	{
		ChooseColour obj=new ChooseColour(players[3],players,this);
		obj.start(ps);
	}

	public void gottoRGB5() throws Exception
	{
		ChooseColour obj=new ChooseColour(players[4],players,this);
		obj.start(ps);
	}

	public void gottoRGB6() throws Exception
	{
		ChooseColour obj=new ChooseColour(players[5],players,this);
		obj.start(ps);
	}

	public void gottoRGB7() throws Exception
	{
		ChooseColour obj=new ChooseColour(players[6],players,this);
		obj.start(ps);
	}

	public void gottoRGB8() throws Exception
	{
		ChooseColour obj=new ChooseColour(players[7],players,this);
		obj.start(ps);
	}
} 
