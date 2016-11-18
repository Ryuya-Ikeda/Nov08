import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Random;

import javax.swing.ImageIcon;

/**
 * マップの設定
 * マップデータの読み込み、マップデータの更新、マップ内での衝突など
 * 処理していく
 * @author riked
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

	private MainPanel mainPanel;

		//繝槭ャ繝励繝ュ繝シ繝焔=======
	public Map(String fileName, MainPanel mainPanel){
		sprites = new LinkedList();
		this.mainPanel = mainPanel;
		
		//マップのロード
		Load(fileName);

		//控えのマップのロード
		//Reflection(next_map, "map" + (rnd.nextInt(MAP_NUM) + 1) + ".dat");
		Reflection(next_map, fileName);
		width = TILE_SIZE * COL;
		height = TILE_SIZE * ROW;

		//逕サ蜒上繝ュ繝シ繝		LoadImage();

	}

	/**
	 * ピクセル単位をタイル単位に変更する
	 * @param pixels
	 * @return
	 */
	public static int PixelsToTiles(double pixels) {
		return (int)Math.floor(pixels / TILE_SIZE);
	}

	/**
	 * タイル単位をピクセル単位に変更する
	 * @param tiles
	 * @return
	 */
	public static int TilesToPixels(int tiles) {
		return tiles * TILE_SIZE;
	}

	/**
	 * マップの描画
	 * @param g
	 * @param relative_x
	 * @param relative_y
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
	 * @param sprite
	 * @param newX
	 * @param newY
	 * @return
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
	 * @param fileName
	 */
	private void Load(String fileName){
		map = new char[ROW][COL];
		next_map = new char[ROW][COL];
		String line;//ファイル読み込み用
		try{
			BufferedReader br = new BufferedReader(new InputStreamReader(
					getClass().getResourceAsStream("map/" + fileName)));
			//マップを作成

			for(int i = 0; i < ROW; i++){
				line = br.readLine(); //1行読み取り
				for(int j = 0; j < COL; j++){
					map[i][j] = line.charAt(j);
					if(map[i][j] != ' ')
						Sprite_load(i, j, map);
				}
			}
		} catch (Exception e){
			e.printStackTrace();
		}
	}

	/**
	 * 繝槭ャ繝励↓繝槭ャ繝励ヵ繧。繧、繝ォ繧貞渚譏せる。
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

				}
			}
		} catch (Exception e){
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * @return
	 */
	public int GetWidth() {
		return width;
	}

	/**
	 * 
	 * @return
	 */
	public int GetHeight() {
		return height;
	}

	/**
	 * 物体のLinkedListを返す
	 * @return
	 */
	public LinkedList GetSprites() {
		return sprites;
	}

	/**
	 * マップの更新
	 */
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
			Sprite_load(i, COL-1, map);
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

		//スプライトの座標の移動と削除
		Iterator iterator = sprites.iterator();
		while (iterator.hasNext()) {
			Sprite sprite = (Sprite)iterator.next();
			if (sprite instanceof Coin) {
				sprite.x -= TILE_SIZE;
			}
			if(sprite.x < 0){
				iterator.remove();
			}
		}

		update_map_num++;
		if(update_map_num == COL){
			Reflection(next_map, "map01.dat");
			update_map_num = 0;
		}
	}

	/**
	 * 繝槭ャ繝嶺クュ縺ョ蠑墓焚縺ョ菴咲スョ縺ョ繧ケ繝励Λ繧、繝医ｒ蜑企勁
	 * @param x
	 * @param y
	 */
	public void Sprite_delete(int x, int y){
		System.out.printf("x:%d y:%d \n", x,y);
		map[y][x] = ' ';
		
		for(int i=0;i<ROW;i++){
			for(int j=0;j<COL;j++){
				System.out.printf("%c",map[i][j]);
			}
			System.out.println("");
		}
		
	}

	/**
	 * 謖㍾ョ壹＠縺滉ス咲スョ縺ョ驟榊縺九ｉ繧ケ繝励Λ繧、繝医ｒ隱ュ縺ソ霎シ縺セ縺帙ｋ
	 * @param i
	 * @param j
	 * @param map
	 */
	public void Sprite_load(int i, int j, char map[][]){
		switch (map[i][j]) {
		case 'o':
			sprites.add(new Coin((double)TilesToPixels(j), (double)TilesToPixels(i), "coin.gif", this, mainPanel));
			break;
			//縺薙蜈域緒逕サ縺吶ｋ繝悶Ο繝け縺ョ遞ョ鬘槭′蠅励∴繧九↑繧芽ソス蜉		}
	}
	}
}

		
