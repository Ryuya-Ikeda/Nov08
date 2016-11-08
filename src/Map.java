import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.Robot;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.Random;

import javax.swing.ImageIcon;

/**
 * マップの設定
 * マップデータの読み込み、マップデータの更新、マップ内での衝突など
 * 処理していく
 */
public class Map {
	//タイルの大きさ
	public static final int TILE_SIZE = 32;
	//重力
	public static final double GRAVITY = 1.0;

	//マップ
	private char[][] map;

	//控えのマップ
	private char[][] next_map;

	// 行数
	private static final int ROW = 20;
	// 列数
	private static final int COL = 60;
	//幅
	private int width;
	//高さ
	private int height;

	//ブロックの画像
	private Image blockImg;

	//物体のリスト
	private LinkedList sprites;

	//乱数を使用するために
	Random rnd = new Random();
	
	//マップの更新回数(マップの次のデータを読み込むキューとして活用)
	private int update_map_num = 0;
	
	//マップの数
	private static final int MAP_NUM = 1;

	public Map(String fileName){
		sprites = new LinkedList();

		//マップのロード
		Load(fileName);
		//控えのマップのロード
		//Reflection(next_map, "map" + (rnd.nextInt(MAP_NUM) + 1) + ".dat");
		Reflection(next_map, "map01.dat");
		width = TILE_SIZE * COL;
		height = TILE_SIZE * ROW;

		//画像のロード
		LoadImage();

	}

	/**
	 * ピクセル単位をタイル単位に変更する
	 */
	public static int PixelsToTiles(double pixels) {
		return (int)Math.floor(pixels / TILE_SIZE);
	}

	/**
	 * タイル単位をピクセル単位に変更する
	 */
	public static int TilesToPixels(int tiles) {
		return tiles * TILE_SIZE;
	}

	/**
	 * マップの描画
	 */
	public void Draw(Graphics g, int relative_x, int relative_y){
		//引数から描画範囲を考える
		int startTileX = PixelsToTiles(-relative_x);
		int endTileX = startTileX + PixelsToTiles(MainPanel.WIDTH) + 1;
		//描画範囲がマップより大きくならないように調整
		endTileX = Math.min(endTileX, COL);

		int startTileY = PixelsToTiles(-relative_y);
		int endTileY = startTileY + PixelsToTiles(MainPanel.HEIGHT) + 1;
		//描画範囲がマップより大きくならないように調整
		endTileY = Math.min(endTileX, ROW);

		for(int i = startTileY; i < endTileY; i++){
			for(int j = startTileX; j < endTileX; j++){
				switch(map[i][j]){
				case 'B' :
					g.drawImage(blockImg,
							TilesToPixels(j) + relative_x,
							TilesToPixels(i) + relative_y
							,null);
					break;
					/* この先描画するブロックの種類が増えるなら追加 */
				}
			}
		}
	}

	/**
	 * 引数のnewX,newYでぶつかるブロックの座標を返す
	 */
	public Point GetTileCllision(Sprite sprite, double newX, double newY){

		// 小数点以下切り上げ
		// 浮動小数点の関係で切り上げしないと衝突してないと判定される場合がある

		int fromTileX = PixelsToTiles(Math.min(sprite.GetX(), Math.ceil(newX)));
		int fromTileY = PixelsToTiles(Math.min(sprite.GetY(), Math.ceil(newY)));
		int toTileX = PixelsToTiles(Math.max(sprite.GetX(), newX) + sprite.GetWidth() - 1);
		int toTileY = PixelsToTiles(Math.max(sprite.GetY(), newY) + sprite.GetHeight() - 1);


		//ぶつかっているか探していく
		for(int x = fromTileX; x <= toTileX; x++){
			for(int y = fromTileY; y <= toTileY; y++){
				//画面外は衝突判定とする
				if(x < 0 || x >= COL){
					return new Point(x,y);
				}
				else if(y < 0 || y >= ROW){
					return new Point(x, y);
				}
				else if(map[y][x] == 'B'){
					return new Point(x, y);
				}
			}
		}
		return null;
	}

	/**
	 * 画像をロード
	 */
	private void LoadImage(){
		ImageIcon icon = new ImageIcon(getClass().getResource("image/block.gif"));
		blockImg = icon.getImage();
	}

	/**
	 * マップをロード
	 */
	private void Load(String fileName){
		map = new char[ROW][COL];
		next_map = new char[ROW][COL];
		Reflection(map, fileName);
	}

	/**
	 * マップにマップファイルを反映させる。
	 * @param map
	 * @param fileName
	 */
	public void Reflection(char map[][], String fileName){
		String line;//ファイル読み込み用
		try{
			BufferedReader br = new BufferedReader(new InputStreamReader(
					getClass().getResourceAsStream("map/" + fileName)));
			//マップを作成

			for(int i = 0; i < ROW; i++){
				line = br.readLine(); //1行読み取り
				for(int j = 0; j < COL; j++){
					map[i][j] = line.charAt(j);
					switch (map[i][j]) {
					/* case 'o':
						sprites.add(new Coin(tilesToPixels(j), tilesToPixels(i), "coin.gif", this));
						break;
						//この先描画するブロックの種類が増えるなら追加
					 */
					}
				}
			}
		} catch (Exception e){
			e.printStackTrace();
		}
	}
	
	public int GetWidth() {
		return width;
	}


	public int GetHeight() {
		return height;
	}

	/**
	 *物体のLinkedListを返す
	 */
	public LinkedList GetSprites() {
		return sprites;
	}
	
	public void Lotation(){
		String str = " "; 
		//マップをずらす
		for(int i = 0; i < ROW - 1; i++){
			for(int j = 0; j < COL - 1; j++){
				map[i][j] = map[i][j+1];
			}
		}
		//次のマップを読み込ませる
		for(int i = 0; i < ROW; i++){
			map[i][COL-1] = next_map[i][0];
		}
		//次に読み込ませるマップをずらす
		for(int i = 0; i < ROW - 1; i++){
			for(int j = 0; j < COL - 1; j++){
				next_map[i][j] = next_map[i][j+1];
			}
		}
		//空いたスペースを埋める
		
		for(int i = 0; i < ROW; i++){
			next_map[i][COL-1] = str.charAt(0);
		}
		
		//System.out.println(next_map.length);
		
		update_map_num++;
		if(update_map_num == COL){
			//Reflection(next_map, "map" + (rnd.nextInt(MAP_NUM) + 1) + ".dat");
			Reflection(next_map, "map01.dat");
			update_map_num = 0;
		}
	}
}
