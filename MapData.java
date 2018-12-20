import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class MapData {
	public static final int TYPE_NONE   = 0;
	public static final int TYPE_WALL   = 1;
	public static final int TYPE_KEY = 2;
	public static final int TYPE_ITEM = 3;
	public static final int TYPE_GOAL = 4;
	public static final int TYPE_WARP = 5;
	public static final int TYPE_MOVEWALL = 6;
	public static final int TYPE_FALL = 7;
	public static final int TYPE_FALLWALL = 8;
	private static final String mapImageFiles[] = {
		"png/SPACE.png",
		"png/Wall.png",
		"png/Key.png",
		"png/ninnjinn.png",
		"png/kaidan.png",
		"png/WARP.png",
		"png/MOVEWALL.png",
		"png/FALL.png",
		"png/FALLWALL.png",
	};

	private Image[] mapImages;
	private ImageView[][] mapImageViews;
	private int[][] maps;
	private int width;
	private int height;

	MapData(int x, int y,int level){ 
		width  = x;
		height = y;
		maps = new int[y][x];
		if(level == 0){
			fillMap(MapData.TYPE_WALL);
			digMap(1, 3);
			putGoal(19,13);
			putItem(3,1);//鍵
			putItem(10,2);//にんじん


		}else if(level >= 1){
			maps = mapBox(level);

			if(level == 1){
				putGoal(19,13);
				putWarp(3,1);
				putMoveWall(12,5);
				putMoveWall(18,11);
				putFall(19,11);
				putItem(3,1);//鍵
				putItem(10,2);//にんじん

			}else if(level == 2){

				putGoal(4,1);
				putWarp(2,12);
				putWarp(15,13);
				putMoveWall(4,3);
				putMoveWall(6,4);
				putMoveWall(14,9);
				putFall(3,1);
				putFall(5,3);
				putFall(15,9);    
				putItem(3,1);//鍵
				putItem(10,2);//にんじん

			}else if(level == 3){
				int i;

				putGoal(19,13);
				putWarp(4,1);
				putWarp(18,8);
				putMoveWall(1,2);
				for(i = 3; i <= 11; i++){
					putMoveWall(i,i);
				}
				putFall(1,13);
				putFall(13,18);
				putItem(3,1); //鍵
				putItem(10,2);

			}else if(level == 4){
				putGoal(1,13);
				putGoal(9,13);
				putMoveWall(2,8);
				putMoveWall(7,2);
				putMoveWall(12,6);
				putMoveWall(15,9);
				putFall(1,8);
				putFall(11,6);
				putFall(9,11);

				while(true){
					int a,b;
					a = (int)(Math.random()*width);
					b = (int)(Math.random()*height);
					if(getMap(a,b) == MapData.TYPE_NONE){
						putWarp(a,b);
						break;
					}
				}

				putItem(3,1);//鍵
				putItem(10,2);//にんじん
			}  
		}

		mapImages     = new Image[9];
		mapImageViews = new ImageView[y][x];
		for (int i=0; i<9; i++) {
			mapImages[i] = new Image(mapImageFiles[i]);
		}
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
			if(x != 1 || y != 1){
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


	public int[][] mapBox(int i) {
		int[][] nextMap =
		{{1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1},
			{1,0,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1},
			{1,0,1,0,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,0,1},
			{1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,0,1},
			{1,0,1,0,1,1,1,1,1,1,0,1,0,1,1,0,1,0,1,0,1},
			{1,0,1,1,1,1,1,1,1,1,0,0,0,0,0,0,1,0,1,0,1},
			{1,0,0,0,0,0,0,0,0,1,0,1,0,1,1,1,1,0,1,0,1},
			{1,0,1,1,1,1,1,1,0,1,0,1,0,0,0,0,1,0,1,0,1},
			{1,0,1,0,0,0,0,0,0,1,0,1,1,1,1,0,1,0,1,0,1},
			{1,0,1,0,1,1,1,1,1,1,0,1,1,1,1,0,1,0,1,0,1},
			{1,0,1,0,0,0,0,0,0,1,0,1,0,0,0,0,1,1,1,1,1},
			{1,0,1,1,1,1,1,1,0,1,0,1,0,1,1,1,1,0,0,0,1},
			{1,0,1,0,0,0,0,0,0,1,1,0,0,0,0,0,0,0,1,0,1},
			{1,0,0,1,1,1,1,1,0,1,1,1,1,1,1,1,1,1,1,0,1},
			{1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1}};

		if(i == 2){
			int[][] nextMap2 =
			{{1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1},
				{1,0,0,0,0,1,0,0,0,1,0,0,0,0,0,0,0,0,0,0,1},
				{1,0,1,0,1,0,0,1,0,0,0,1,1,1,1,1,1,1,1,0,1},
				{1,0,1,0,0,0,1,0,1,0,1,0,0,0,0,0,0,0,1,0,1},
				{1,0,1,0,1,0,0,0,1,0,1,0,1,1,1,1,1,0,1,0,1},
				{1,0,1,0,1,0,1,0,1,0,1,0,1,0,0,0,1,0,1,0,1},
				{1,0,1,0,1,0,1,0,0,0,1,0,1,1,0,1,1,0,1,0,1},
				{1,0,1,0,1,0,1,1,0,1,1,0,0,0,0,0,1,0,1,0,1},
				{1,0,1,0,1,0,1,0,1,0,1,0,1,1,1,0,1,0,1,0,1},
				{1,0,1,0,1,0,1,0,1,0,1,0,1,0,0,0,1,0,1,0,1},
				{1,0,1,0,1,0,1,0,1,0,1,0,1,0,1,0,1,1,1,0,1},
				{1,0,1,0,1,0,1,0,1,0,1,0,1,0,1,0,0,0,1,0,1},
				{1,0,0,0,1,0,1,0,1,0,1,0,1,0,1,1,1,0,1,0,1},
				{1,0,1,0,0,0,0,0,0,0,0,0,0,0,0,0,1,0,0,0,1},
				{1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1}};
			return nextMap2;

		}else if(i == 3){
			int[][] nextMap3 =
			{{1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1},
				{1,0,1,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1},
				{1,0,1,1,0,1,1,1,1,1,1,1,1,1,0,0,1,0,1,0,1},
				{1,0,0,0,0,1,1,0,0,1,1,0,0,0,1,0,1,0,1,0,1},
				{1,0,0,0,0,0,0,1,0,0,1,0,1,0,1,0,0,1,1,0,1},
				{1,0,1,0,0,0,0,0,1,0,0,0,1,0,1,1,0,0,1,0,1},
				{1,0,0,1,0,0,0,0,1,1,0,0,1,0,0,0,1,0,1,0,1},
				{1,0,0,0,1,1,0,0,0,0,1,0,0,1,1,0,0,1,1,1,1},
				{1,0,1,0,0,1,0,0,0,0,1,1,0,0,0,1,0,0,0,0,1},
				{1,0,0,1,0,0,1,0,0,0,0,1,1,1,0,0,1,0,1,0,1},
				{1,0,1,1,1,0,0,1,1,0,0,0,0,0,1,0,1,0,1,0,1},
				{1,0,0,0,0,1,0,0,1,1,0,0,0,0,1,0,1,0,1,0,1},
				{1,0,1,1,1,1,1,0,0,1,0,1,1,0,0,0,1,0,1,0,1},
				{1,0,0,0,0,0,0,1,0,0,1,1,1,1,1,1,0,0,1,0,1},
				{1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1}};
			return nextMap3;

		}else if(i == 4){
			int[][] nextMap4 =
			{{1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1},
				{1,0,0,0,0,1,0,1,0,0,0,1,0,1,0,0,0,0,0,0,1},
				{1,1,0,1,0,0,0,0,0,0,0,1,0,0,0,1,1,1,1,0,1},
				{1,0,0,1,1,1,1,0,1,1,0,0,1,1,1,0,0,0,0,0,1},
				{1,0,1,0,0,0,1,0,0,1,1,0,0,0,0,0,1,1,1,0,1},
				{1,0,1,0,1,0,1,1,0,0,1,1,1,1,1,1,0,0,0,0,1},
				{1,0,1,0,0,0,0,0,1,0,0,0,0,0,0,0,0,1,1,0,1},
				{1,0,1,1,1,0,1,0,1,0,0,1,1,1,1,1,1,0,0,0,1},
				{1,0,0,0,1,0,1,0,0,1,0,0,0,0,0,0,1,0,1,0,1},
				{1,0,1,0,1,0,0,1,0,1,0,1,0,1,0,0,0,0,1,0,1},
				{1,0,1,0,0,1,0,1,0,1,0,1,0,0,0,1,1,1,1,0,1},
				{1,0,0,1,0,0,0,1,0,0,0,0,0,1,0,0,0,0,1,0,1},
				{1,1,0,1,1,1,1,0,0,1,1,1,1,1,1,1,1,1,1,0,1},
				{1,0,0,1,0,0,0,0,1,0,0,0,0,0,0,0,0,0,0,1,1},
				{1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1}};

			return nextMap4;   

		}

		return nextMap;
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

	public void putGoal(int x, int y){
		setMap(x,y,MapData.TYPE_GOAL);
	}

	public void putWarp(int x, int y){
		setMap(x, y, MapData.TYPE_WARP);
	}

	public void putMoveWall(int x, int y){
		setMap(x, y, MapData.TYPE_MOVEWALL);
	}

	public void putFall(int x, int y){
		setMap(x, y, MapData.TYPE_FALL);
	}

	public void putFallWall(int x, int y){
		setMap(x, y, MapData.TYPE_FALLWALL);
	}
}
