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

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.util.Duration;

public class MapGameController implements Initializable {
    public MapData mapData;
    public MoveChara chara;
    public GridPane mapGrid;
    public ImageView[] mapImageViews;
//    public Group[] mapGroups;

    public int timeLimit = 50;
    public Timeline timer;
    public Label timeLabel;
    public Label itemLabel;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        mapData = new MapData(21,15,0);//level=0が最初
        chara = new MoveChara(1,1,mapData,0);
//        mapGroups = new Group[mapData.getHeight()*mapData.getWidth()];
        mapImageViews = new ImageView[mapData.getHeight()*mapData.getWidth()];
        for(int y=0; y<mapData.getHeight(); y++){
            for(int x=0; x<mapData.getWidth(); x++){
                int index = y*mapData.getWidth() + x;
                mapImageViews[index] = mapData.getImageView(x,y);
            }
        }
        setTimer();
        mapPrint(chara, mapData);
    }

    public void newMap() {//新しいマップの呼び出し
        int level = chara.getGoal_count();
        mapData = new MapData(21,15,level);
        if(level == 2){
            chara = new MoveChara(2,1,mapData,level);
        }else{
            chara = new MoveChara(1,1,mapData,level);
        }
        mapImageViews = new ImageView[mapData.getHeight()*mapData.getWidth()];
        for(int y=0; y<mapData.getHeight(); y++){
            for(int x=0; x<mapData.getWidth(); x++){
                int index = y*mapData.getWidth() + x;
                mapImageViews[index] = mapData.getImageView(x,y);
            }
        }
        mapPrint(chara, mapData);
    }

    public void mapPrint(MoveChara c, MapData m){
        int cx = chara.getPosX();
        int cy = chara.getPosY();
        c.Item(cx,cy);
        boolean goal =c.goalin(cx,cy);
        c.Warp(cx,cy);//ワープに関係
        cx = c.getPosX();
        cy = c.getPosY();
        mapGrid.getChildren().clear();
        for(int y=0; y<mapData.getHeight(); y++){
            for(int x=0; x<mapData.getWidth(); x++){
                int index = y*mapData.getWidth() + x;
                mapImageViews[index] = m.getImageView(x,y);
                if (x==cx && y==cy) {
                    mapGrid.add(c.getCharaImageView(), x, y);
                } else {
                    mapGrid.add(mapImageViews[index], x, y);
                }
            }
        }
        int key_count = c.getKey_count();
        String Key = String.valueOf(key_count);
        itemLabel.setText("鍵の個数　" + Key);
        if(goal == true){
            stopTimer();//タイマー
            timeLimit = 100;
            setTimer();
            newMap();//新しいマップの呼び出し
        }
    }

    public void func1ButtonAction(ActionEvent event) { }
    public void func2ButtonAction(ActionEvent event) { }
    public void func3ButtonAction(ActionEvent event) { }
    public void func4ButtonAction(ActionEvent event) { }

    public void keyAction(KeyEvent event){
        KeyCode key = event.getCode();
        if (key == KeyCode.DOWN){
            downButtonAction();
        }else if (key == KeyCode.RIGHT){
            rightButtonAction();
        }else if (key == KeyCode.UP){
            upButtonAction();
        }else if (key == KeyCode.LEFT){
            leftButtonAction();
        }
    }

    public void outputAction(String actionString) {
        System.out.println("Select Action: " + actionString);
    }

    public void downButtonAction(){
        int cx = chara.getPosX();
        int cy = chara.getPosY();
        outputAction("DOWN");
        chara.setCharaDir(MoveChara.TYPE_DOWN);
        boolean movewall = chara.DownMoveWall(cx,cy);
        if (movewall == false){
            chara.move(0, 1);
        }
        mapPrint(chara, mapData);
    }
    public void downButtonAction(ActionEvent event) {
        downButtonAction();
    }

    public void upButtonAction(){
        int cx = chara.getPosX();
        int cy = chara.getPosY();
        outputAction("UP");
        chara.setCharaDir(MoveChara.TYPE_UP);
        boolean movewall = chara.UpMoveWall(cx,cy);
        if (movewall == false){
            chara.move(0, -1);
        }
        mapPrint(chara, mapData);
    }
    public void upButtonAction(ActionEvent event) {
        upButtonAction();
    }

    public void rightButtonAction(){
        int cx = chara.getPosX();
        int cy = chara.getPosY();
        outputAction("RIGHT");
        chara.setCharaDir(MoveChara.TYPE_RIGHT);
        boolean movewall = chara.RightMoveWall(cx,cy);
        if (movewall == false){
            chara.move( 1, 0);
        }
        mapPrint(chara, mapData);
    }
    public void rightButtonAction(ActionEvent event) {
        rightButtonAction();
    }

    public void leftButtonAction(){
        int cx = chara.getPosX();
        int cy = chara.getPosY();
        outputAction("LEFT");
        chara.setCharaDir(MoveChara.TYPE_LEFT);
        boolean movewall = chara.LeftMoveWall(cx,cy);
        if (movewall == false){
            chara.move(-1, 0);
        }
        
        mapPrint(chara, mapData);
    }
    public void leftButtonAction(ActionEvent event) {
        leftButtonAction();
    }

    public void setTimer() {//タイマーセット
  		timer = new Timeline(new KeyFrame(Duration.seconds(1),new EventHandler<ActionEvent>(){
  		    @Override
  		    public void handle(ActionEvent event) {
  							timeLimit = timeLimit - 1;
                String T = String.valueOf(timeLimit);
                timeLabel.setText("残り時間　" + T);
  		    }
  		}));

  		timer.setCycleCount(timeLimit);
  		timer.play();
  	}

  	public void stopTimer() {
  		timer.stop();
  	}
}