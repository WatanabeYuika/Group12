import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import java.io.File;

import javafx.fxml.FXML;
import javafx.event.ActionEvent;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.GridPane;
import java.io.IOException;


public class MapGame extends Application {
	Stage stage;
	MapGameController mapgameController;

	@Override
	public void start(Stage primaryStage) throws Exception {
		stage = primaryStage;
		primaryStage.setTitle("MAP GAME");
		Pane myPane_top = (Pane)FXMLLoader.load(getClass().getResource("test.fxml"));
		Scene myScene = new Scene(myPane_top);
		mus.warp();
		myScene.setOnKeyPressed(e -> {
			if (e.getCode() == KeyCode.Z) {
				System.out.println("Good Luck!");
				mus.result();
				try{
				Thread.sleep(1000);
				}catch(Exception es){}
				try{
					Scene mapScene = new Scene(FXMLLoader.load(getClass().getResource("MapGame.fxml")));
					mus.mus();
					stage.setScene(mapScene);
				}catch(IOException ex){
					ex.printStackTrace();
				}
			}
		});
		primaryStage.setScene(myScene);
		primaryStage.show();

	}
	public static void main(String[] args) {
		launch(args);
	}
}
