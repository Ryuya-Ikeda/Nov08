import java.applet.Applet;
import java.applet.AudioClip;
import java.awt.Graphics;
import java.awt.Point;
import java.util.Random;

public class GrandFather extends Sprite{

	//向き
    private static final int RIGHT = 0;
    private static final int LEFT = 1;
    private int dir = 0;
	
	private Random rnd;
	private AudioClip sound;

	private double vx,vy;

	//地面に足がついているか判定
	private boolean onGround;

	public GrandFather(double x, double y, String fileName, Map map, MainPanel mainpanel) {
		super(x,y,fileName,map,mainpanel);
		score = 50;
		rnd = new Random();

		//ぶつかったときの効果音
		sound = Applet.newAudioClip(getClass().getResource("music/break.wav"));;

	}

	@Override
	public void Update() {
		int i = rnd.nextInt(10);
		if(i % 2 == 0){
			vx += map.TILE_SIZE / 32;
		} 
		else {
			vx -= map.TILE_SIZE / 32;
		}

		//重力で下向きに加速度がかかる
		vy += Map.GRAVITY;
		Point tile;

		/*
		 * ゲームオーバー判定
		 */
		if(x<0 || y > MainPanel.HEIGHT || y < 0){
			mainPanel.GameOver();
			return;
		}

		tile = map.GetTileCllision(this, x+vx, y);
		//衝突するタイルがないので
		if(tile == null){
			x += vx; 
			if(vx > 0){
				dir = LEFT;
			}
			else{
				dir = RIGHT;
			}
		} else {
			//衝突するタイルがあるのでブロックにめり込まないように位置調整
			x = Map.TilesToPixels(tile.x) - width;
		}

		//y方向のあたり判定	
		double newY = y + vy;
		//移動先座標で衝突するタイルの位置を取得
		tile = map.GetTileCllision(this, x, newY);
		//衝突するタイルがないので
		if(tile == null){
			y = newY;//移動
			onGround = false;//空中にいると分かるので
		} else {
			//衝突するタイルがあるので
			if(vy > 0){ //下へ移動中
				//位置調整
				y = Map.TilesToPixels(tile.y) - height;
				vy = 0; //着地したのでy方向速度を0へ
				onGround = true; //着地フラグを立てる
			} else if(vy < 0){ //天井へぶつかったので
				//上へ移動中
				y = Map.TilesToPixels(tile.y + 1);
				vy = 0;
			}
		}
	}

	/**
	 * 描画
	 * @param g
	 * @param relative_x
	 * @param relative_y
	 */
	@Override
	public void Draw(Graphics g, int relative_x, int relative_y){
		g.drawImage(image,
				(int)x + relative_x,
				(int)y + relative_y,
				(int)x + relative_x + width,
				(int)y + relative_y + height,
				count * width, dir * height,
				count * width + width,
				height + dir * height,
				null);
	}
	
	
	/**
	 * サウンドの再生
	 */
	public void play(){
		sound.play();
	}

}
