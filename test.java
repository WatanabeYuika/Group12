import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;
import javafx.event.ActionEvent;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.KeyCode;
import javafx.scene.Group;
import java.io.File;

import javafx.scene.text.*;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.fxml.FXMLLoader;
public class test {
	public GridPane mapGrid;
	public MapGameController mgc;
	public void initialize(){
	}


	public void opkeyAction(KeyEvent event){
		KeyCode key = event.getCode();
		System.out.println(event.getCode());
		if(key == KeyCode.Z){
			outputAction("Z");
			Stage s = (Stage) mapGrid.getScene().getWindow();
			try {
				Scene myScene = new Scene(FXMLLoader.load(getClass().getResource("MapGame.fxml")));
				s.setScene(myScene);
			} catch(Exception e) {
				e.printStackTrace();
			}
		}			
	}
	public void outputAction(String action){
		System.out.println("Select Action: " + action);
	}
}
