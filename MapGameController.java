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

public class MapGameController implements Initializable {
    public MapData mapData;
    public MoveChara chara;
    public GridPane mapGrid;
    public ImageView[] mapImageViews;
//    public Group[] mapGroups;

    public Label itemLabel;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        mapData = new MapData(21,15,0);//level=0が最初
        chara = new MoveChara(1,1,mapData);
//        mapGroups = new Group[mapData.getHeight()*mapData.getWidth()];
        mapImageViews = new ImageView[mapData.getHeight()*mapData.getWidth()];
        for(int y=0; y<mapData.getHeight(); y++){
            for(int x=0; x<mapData.getWidth(); x++){
                int index = y*mapData.getWidth() + x;
                mapImageViews[index] = mapData.getImageView(x,y);
            }
        }
        mapPrint(chara, mapData);
    }

    public void newMap() {//新しいマップの呼び出し
        int level = chara.getGoal_count();
        if(level == 1){
            mapData = new MapData(21,15,level);
            chara = new MoveChara(1,1,mapData);
        }else if(level == 2){
            mapData = new MapData(21,15,level);//マップの追加に
            chara = new MoveChara(1,1,mapData);
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
        cx = c.getPosX();//ワープに関係
        cy = c.getPosY();//ワープに関係
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
        itemLabel.setText(Key);
        if(goal == true){
            newMap();
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
        boolean movewall = chara.DownMoveWall(cx,cy);//動く壁があるかどうかの判定
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
        boolean movewall = chara.UpMoveWall(cx,cy);//動く壁があるかどうかの判定
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
        boolean movewall = chara.RightMoveWall(cx,cy);//動く壁があるかどうかの判定
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
        boolean movewall = chara.LeftMoveWall(cx,cy);//動く壁があるかどうかの判定
        if (movewall == false){
            chara.move(-1, 0);
        }
        
        mapPrint(chara, mapData);
    }
    public void leftButtonAction(ActionEvent event) {
        leftButtonAction();
    }
}