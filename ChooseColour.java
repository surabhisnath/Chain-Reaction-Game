package ChainReaction;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;

/**
 * This class sets Colours for a Player and checks for SameColourException
 * It holds list of Players, the concerned Player, an instance of Settings, Texfields for the 3 Colour components
 * @author Surabhi S Nath, Raghav Sood
 * @version 1.3
 * @since 2017-10-18
 */
public class ChooseColour extends Application implements Initializable
{
	static Player player;
	static Player players[];
	static Settings settings;
	@FXML private TextField red;
	@FXML private TextField green;
	@FXML private TextField blue;
	@FXML private Button b;
	boolean in;

	public ChooseColour()
	{

	}

	public ChooseColour(Player player,Player players[], Settings settings)
	{
		this.player=player;
		this.players=players;
		this.settings=settings;
	}

	@Override
	public void start(Stage primaryStage) throws Exception
	{
		Parent root=FXMLLoader.load(getClass().getResource("SetRGB.fxml"));
		Scene scene = new Scene(root);
		primaryStage.setScene(scene);
		primaryStage.show();
	}

	@Override
	public void initialize(URL url, ResourceBundle rb)
	{
		red.setVisible(false);
		green.setVisible(false);
		blue.setVisible(false);
		b.setVisible(false);
	}


	/**
	 * Get Colour data methods are called when buttons for setting red/green/blue components are clicked.
	 * It makes the corresponding TextField visible
	 */
	public void getReddata()
	{
		red.setVisible(true);
		b.setVisible(true);
	}

	public void getGreendata()
	{
		green.setVisible(true);
		b.setVisible(true);
	}

	public void getBluedata()
	{
		blue.setVisible(true);
		b.setVisible(true);
	}


	/**
	 * This method is called when the Back button is clicked
	 * It displays the Settings page
	 * @throws Exception
	 */
	public void displaysettings() throws Exception
	{
		settings.display();
	}


	class SameColourException extends Exception
	{
		public SameColourException(String message)
		{
			super(message);
		}
	}


	/**
	 * This method is called when the OK button is clicked.
	 * It sets the red, green, blue components based on the entries of the user.
	 * It checks if any other player has the same Colour.
	 * If it finds another Player with same Colour, it displays an error popup promt and resets the Colour to default
	 * If there is no such Player, the new Colour of player is set and a popup is displayed.
	 * @throws SameColourException
	 */
	public void clickOK() throws SameColourException
	{
		int a,b,c;
		a=player.getColour().getRed();
		b=player.getColour().getGreen();
		c=player.getColour().getBlue();

		try
		{
			player.getColour().setRed(Integer.parseInt(red.getText()));
		}
		catch(NumberFormatException e)
		{

		}

		try
		{
			player.getColour().setGreen(Integer.parseInt(green.getText()));
		}
		catch(NumberFormatException e)
		{

		}

		try
		{
			player.getColour().setBlue(Integer.parseInt(blue.getText()));
		}
		catch(NumberFormatException e)
		{

		}


		for(int z=0; z<8; z++)
		{
			for(int y=z+1; y<8; y++)
			{
				if(players[z].getColour().getRed()==players[y].getColour().getRed() && players[z].getColour().getGreen()==players[y].getColour().getGreen() && players[z].getColour().getBlue()==players[y].getColour().getBlue())
				{
					//throw new SameColourException("Two players cannot have the same colour");

					Alert alert=new Alert(AlertType.ERROR);
					alert.setTitle("Chain Reaction");
					alert.setHeaderText("Error");
					alert.setContentText("Two players can not have same colours. Default value reset");
					alert.show();

					red.setText("");
					green.setText("");
					blue.setText("");

					player.getColour().setRed(a);
					player.getColour().setGreen(b);
					player.getColour().setBlue(c);
					in=true;
				}
			}
		}

		if(in==false)
		{
			Alert alert=new Alert(AlertType.INFORMATION);
			alert.setTitle("Chain Reaction");
			alert.setHeaderText("Saved");
			alert.setContentText("Player colour set");
			alert.show();
		}
	}
}
