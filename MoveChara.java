import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Rectangle;
import javafx.animation.AnimationTimer;

public class MoveChara {
    public static final int TYPE_DOWN  = 0;
    public static final int TYPE_LEFT  = 1;
    public static final int TYPE_RIGHT = 2;
    public static final int TYPE_UP    = 3;

    private final String[] dirStrings  = { "d", "l", "r", "u" };
    private final String[] kindStrings = { "1", "2", "3" };
    private final String pngPathBefore = "png/neko";
    private final String pngPathAfter  = ".png";

    private int posX;
    private int posY;

    private MapData mapData;
    private MapGameController MapGameController;
    private mus mus;

    private Image[][] charaImages;
    private ImageView[] charaImageViews;
    private ImageAnimation[] charaImageAnimations;

    private int count   = 0;
    private int diffx   = 1;
    private int charaDir;
    private int key_count = 0;//鍵のカウント
    private int item_count = 0; //アイテムのカウント
    private int goal_count = 0;//ゴールした回数

    MoveChara(int startX, int startY, MapData mapData,int goal){
        this.mapData = mapData;

        charaImages = new Image[4][3];
        charaImageViews = new ImageView[4];
        charaImageAnimations = new ImageAnimation[4];

        for (int i=0; i<4; i++) {
            charaImages[i] = new Image[3];
            for (int j=0; j<3; j++) {
                charaImages[i][j] = new Image(pngPathBefore + dirStrings[i] + kindStrings[j] + pngPathAfter);
            }
            charaImageViews[i] = new ImageView(charaImages[i][0]);
            charaImageAnimations[i] = new ImageAnimation( charaImageViews[i], charaImages[i] );
        }

        posX = startX;
        posY = startY;

        setCharaDir(TYPE_DOWN);
    }

    public void changeCount(){
        count = count + diffx;
        if (count > 2) {
            count = 1;
            diffx = -1;
        } else if (count < 0){
            count = 1;
            diffx = 1;
        }
    }

    public int getPosX(){
        return posX;
    }

    public int getPosY(){
        return posY;
    }

    public void setCharaDir(int cd){
        charaDir = cd;
        for (int i=0; i<4; i++) {
            if (i == charaDir) {
                charaImageAnimations[i].start();
            } else {
                charaImageAnimations[i].stop();
            }
        }
    }

    public int getItem_count(){
        return item_count;
    }

    public int getKey_count(){
        return key_count;
    }

    public int getGoal_count(){
        return goal_count;
    }

    public boolean canMove(int dx, int dy){
        if (mapData.getMap(posX+dx, posY+dy) == MapData.TYPE_WALL){
            return false;
        } else if (mapData.getMap(posX+dx, posY+dy) == MapData.TYPE_NONE){
            return true;
        }else if (mapData.getMap(posX+dx, posY+dy) == MapData.TYPE_KEY){//アイテム上を動けるように
            return true;
        }else if (mapData.getMap(posX+dx, posY+dy) == MapData.TYPE_ITEM){
            return true;
        }else if (mapData.getMap(posX+dx, posY+dy) == MapData.TYPE_GOAL){
            return true;
        }else if (mapData.getMap(posX+dx, posY+dy) == MapData.TYPE_WARP){//ワープ上を動けるように
            return true;
        }else if (mapData.getMap(posX+dx, posY+dy) == MapData.TYPE_MOVEWALL){
                return false;
        }else if (mapData.getMap(posX+dx, posY+dy) == MapData.TYPE_FALL){//落とし穴では動けない
            return false;
        }else if (mapData.getMap(posX+dx, posY+dy) == MapData.TYPE_FALLWALL){ //壁を落としたあと動けるように
            return true;
        }
        return false;
    }

    public boolean move(int dx, int dy){
        if (canMove(dx,dy)){
            posX += dx;
            posY += dy;
            return true;
        }else {
            return false;
        }
    }


    public boolean Item(int cx, int cy){  //アイテム取得メソッド
        if(mapData.getMap(cx, cy) == MapData.TYPE_KEY || mapData.getMap(cx, cy) == MapData.TYPE_ITEM){
            if(mapData.getMap(cx, cy) == MapData.TYPE_KEY){
                key_count++;
                System.out.println("Key"+ key_count);
		mus.key();
            }else if(mapData.getMap(cx, cy) == MapData.TYPE_ITEM){
                item_count++;
                System.out.println("Item"+ item_count);
		mus.item();
            }
            mapData.setMap(cx, cy, MapData.TYPE_NONE);
            mapData.setImageViews();
            return true;
        }
        return true;
    }

    public boolean Warp(int cx, int cy){ //ワープできるように
        if(mapData.getMap(cx, cy) == mapData.TYPE_WARP){
            int width, height, x, y;
            width = mapData.getWidth();
            height = mapData.getHeight();
            while(true){
                x = (int)(Math.random()*width);
                y = (int)(Math.random()*height);
                if(mapData.getMap(x,y) == mapData.TYPE_NONE){
                    posX = x;
                    posY = y;
                    break;
                }
            }
    		mus.warp();	    
            System.out.print("Warp!");
        }
        return true;
    }

    public boolean DownMoveWall(int cx, int cy){ //下に壁を動かす
        if(mapData.getMap(cx, cy+1) == mapData.TYPE_MOVEWALL && mapData.getMap(cx, cy+2) == mapData.TYPE_NONE){
            mapData.setMap(cx, cy+1, MapData.TYPE_NONE);
            mapData.setMap(cx, cy+2, MapData.TYPE_MOVEWALL);
            System.out.print("move wall down\n");
            mapData.setImageViews();
            return true;            
        }else if(mapData.getMap(cx, cy+1) == mapData.TYPE_MOVEWALL && mapData.getMap(cx, cy+2) == mapData.TYPE_FALL){
            mapData.setMap(cx, cy+1, MapData.TYPE_NONE);
            mapData.setMap(cx, cy+2, MapData.TYPE_FALLWALL);
            System.out.print("move wall down and fall\n");
            mapData.setImageViews();
            return true;    
        }
        return false;
    }

    public boolean UpMoveWall(int cx, int cy){ //上に壁を動かす
        if(mapData.getMap(cx, cy-1) == mapData.TYPE_MOVEWALL && mapData.getMap(cx, cy-2) == mapData.TYPE_NONE){
            mapData.setMap(cx, cy-1, MapData.TYPE_NONE);
            mapData.setMap(cx, cy-2, MapData.TYPE_MOVEWALL);
            System.out.print("move wall up\n");
            mapData.setImageViews();
            return true;            
        }else if(mapData.getMap(cx, cy-1) == mapData.TYPE_MOVEWALL && mapData.getMap(cx, cy-2) == mapData.TYPE_FALL){
            mapData.setMap(cx, cy-1, MapData.TYPE_NONE);
            mapData.setMap(cx, cy-2, MapData.TYPE_FALLWALL);
            System.out.print("move wall up and fall\n");
            mapData.setImageViews();
            return true;    
        }
        return false;
    }


    public boolean RightMoveWall(int cx, int cy){ //右に壁を動かす
        if(mapData.getMap(cx+1, cy) == mapData.TYPE_MOVEWALL && mapData.getMap(cx+2, cy) == mapData.TYPE_NONE){
            mapData.setMap(cx+1, cy, MapData.TYPE_NONE);
            mapData.setMap(cx+2, cy, MapData.TYPE_MOVEWALL);
            System.out.print("move wall right\n");
            mapData.setImageViews();
            return true;            
        }else if(mapData.getMap(cx+1, cy) == mapData.TYPE_MOVEWALL && mapData.getMap(cx+2, cy) == mapData.TYPE_FALL){
            mapData.setMap(cx+1, cy, MapData.TYPE_NONE);
            mapData.setMap(cx+2, cy, MapData.TYPE_FALLWALL);
            System.out.print("move wall right and fall\n");
            mapData.setImageViews();
            return true;    
        }
        return false;
    }

    public boolean LeftMoveWall(int cx, int cy){ //左に壁を動かす
        if(mapData.getMap(cx-1, cy) == mapData.TYPE_MOVEWALL && mapData.getMap(cx-2, cy) == mapData.TYPE_NONE){
            mapData.setMap(cx-1, cy, MapData.TYPE_NONE);
            mapData.setMap(cx-2, cy, MapData.TYPE_MOVEWALL);
            System.out.print("move wall left\n");
            mapData.setImageViews();
            return true;            
        }else if(mapData.getMap(cx-1, cy) == mapData.TYPE_MOVEWALL && mapData.getMap(cx-2, cy) == mapData.TYPE_FALL){
            mapData.setMap(cx-1, cy, MapData.TYPE_NONE);
            mapData.setMap(cx-2, cy, MapData.TYPE_FALLWALL);
            System.out.print("move wall left and fall\n");
            mapData.setImageViews();
            return true;    
        }
        return false;
    }

    public boolean goalin(int cx, int cy){ //ゴールしたら
        if((mapData.getMap(cx,cy) == MapData.TYPE_GOAL ) && (key_count >= 3)){
            if(goal_count < 4){
                goal_count++;
                System.out.print("GOAL!\n");
            }else if(goal_count == 4){
                System.out.print("Finish!!");
			    System.exit(0);
            }
            return true;
        }

       return false;
    }

    public ImageView getCharaImageView(){
        return charaImageViews[charaDir];
    }

    private class ImageAnimation extends AnimationTimer {
        // アニメーション対象ノード
        private ImageView   charaView     = null;
        private Image[]     charaImages   = null;
        private int         index       = 0;

        private long        duration    = 500 * 1000000L;   // 500[ms]
        private long        startTime   = 0;

        private long count = 0L;
        private long preCount;
        private boolean isPlus = true;

        public ImageAnimation( ImageView charaView , Image[] images ) {
            this.charaView   = charaView;
            this.charaImages = images;
            this.index      = 0;
        }

        

        @Override
        public void handle( long now ) {
            if( startTime == 0 ){ startTime = now; }

            preCount = count;
            count  = ( now - startTime ) / duration;
            if (preCount != count) {
                if (isPlus) {
                    index++;
                } else {
                    index--;
                }
                if ( index < 0 || 2 < index ) {
                    index = 1;
                    isPlus = !isPlus; // true == !false, false == !true
                }
                charaView.setImage(charaImages[index]);
            }
        }
    }
}
