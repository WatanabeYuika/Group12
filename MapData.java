import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class MapData {
    public static final int TYPE_NONE   = 0;
    public static final int TYPE_WALL   = 1;
    public static final int TYPE_KEY = 2;
    public static final int TYPE_ITEM = 3;
    public static final int TYPE_GOAL = 4;
    public static final int TYPE_WARP = 5;
    private static final String mapImageFiles[] = {
        "png/SPACE.png",
        "png/WALL.png",
        "png/Key.png",
        "png/ninnjinn.png",
        "png/FALL.png" , //Goalに対応。階段がないことへの臨時対応なので、　後に差し替え
	"png/WARP.png"
    };

    private Image[] mapImages;
    private ImageView[][] mapImageViews;
    private int[][] maps;
    private int width;
    private int height;

    MapData(int x, int y){  //fin
        mapImages     = new Image[6];
        mapImageViews = new ImageView[y][x];
        for (int i=0; i<6; i++) {
            mapImages[i] = new Image(mapImageFiles[i]);
        }

        width  = x;
        height = y;
        maps = new int[y][x];

        fillMap(MapData.TYPE_WALL);
        digMap(1, 3);
        putItem(3,1);
        putItem(10,2);
	putWarp(1);
        putGoal(1);
        setImageViews();
    }

    public int getHeight(){
        return height;
    }

    public int getWidth(){
        return width;
    }

    public int getMap(int x, int y) { //fin
        if (x < 0 || width <= x || y < 0 || height <= y) {
            return -1;
        }
        return maps[y][x];
    }

    public ImageView getImageView(int x, int y) {
        return mapImageViews[y][x];
    }

    public void setMap(int x, int y, int type){ //fin
        if (x < 1 || width <= x-1 || y < 1 || height <= y-1) {
            return;
        }
        maps[y][x] = type;
    }

    public void setImageViews() { //fin
        for (int y=0; y<height; y++) {
            for (int x=0; x<width; x++) {
                mapImageViews[y][x] = new ImageView(mapImages[maps[y][x]]);
            }
        }
    }

    public void fillMap(int type){  //fin
        for (int y=0; y<height; y++){
            for (int x=0; x<width; x++){
                maps[y][x] = type;
            }
        }
    }

    public void digMap(int x, int y){ //fin
        setMap(x, y, MapData.TYPE_NONE);
        int[][] dl = {{0,1},{0,-1},{-1,0},{1,0}};
        int[] tmp;

        for (int i=0; i<dl.length; i++) {
            int r = (int)(Math.random()*dl.length);
            tmp = dl[i];
            dl[i] = dl[r];
            dl[r] = tmp;
        }

        for (int i=0; i<dl.length; i++){
            int dx = dl[i][0];
            int dy = dl[i][1];
            if (getMap(x+dx*2, y+dy*2) == MapData.TYPE_WALL){
                setMap(x+dx, y+dy, MapData.TYPE_NONE);
                digMap(x+dx*2, y+dy*2);

            }
        }
    }

    public void putItem(int n, int j){ //アイテムの設置
        int i = 0;
        while(i < n){
            int x,y;
            x = (int)(Math.random()*width);
            y = (int)(Math.random()*height);
            if(x != 1 && y != 1){
                if(getMap(x,y) == MapData.TYPE_NONE){
                    if(j == 1){
                        setMap(x,y,MapData.TYPE_KEY);//鍵の配置
                    }else if(j == 2){
                        setMap(x,y,MapData.TYPE_ITEM);//にんじんの配置
                    }
                i++;
                }
            }
        }
}

    public void printMap(){
        for (int y=0; y<height; y++){
            for (int x=0; x<width; x++){
                if (getMap(x,y) == MapData.TYPE_WALL){
                    System.out.print("++");
                }else{
                    System.out.print("  ");
                }
            }
            System.out.print("\n");
        }
    }

    public void putGoal(int n){
        int x = 19 ;
        int y = 13 ;
        int i = 0 ;
        while( i < n ){
           setMap(x,y,MapData.TYPE_GOAL);
           i++;
                }
    }

    public void putWarp(int n){
	int x, y;
	int i = 0;
	while(i < n){
	    x = (int)(Math.random()*width);
            y = (int)(Math.random()*height);
	    if(getMap(x,y) == MapData.TYPE_NONE){
		setMap(x, y, MapData.TYPE_WARP);
		    i++;
	    }
	}
    }
}
