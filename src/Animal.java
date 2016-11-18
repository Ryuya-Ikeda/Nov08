import java.applet.Applet;
import java.applet.AudioClip;
import java.awt.Graphics;
import java.awt.Point;
import java.util.Random;

/**
 * 敵キャラ：動物(わんにゃん)
 * @author riked
 *
 */
public class Animal extends Sprite{
	private Random rnd;
	private AudioClip sound;
	//ジャンプ力
	private int jump_speed = 10;
	//地面に足がついているか判定
	private boolean onGround;
	
	private double vx,vy;
	
	
	public Animal(double x, double y, String fileName, Map map, MainPanel mainpanel) {
		super(x,y,fileName,map,mainpanel);
		score = 200;
		rnd = new Random();

		//ぶつかったときの効果音
		if(fileName.equals("animal/animal0.png")){
			sound = Applet.newAudioClip(getClass().getResource("music/dog.wav"));
		}
		else if(fileName.equals("animal/animal1.png")){
			sound = Applet.newAudioClip(getClass().getResource("music/cat.wav"));
		}
		//他の動物を増やした時への配慮
		else{
			sound = Applet.newAudioClip(getClass().getResource("music/tread.wav"));
		}
		

	}

	@Override
	public void Update() {
		if(rnd.nextInt(2) % 2 == 0){
			vx = Map.TILE_SIZE / 16;
		} 
		else{
			vx = -1 * Map.TILE_SIZE / 16;
		}
		//重力で下向きに加速度がかかる
		vy += Map.GRAVITY;
		Point tile;

		tile = map.GetTileCllision(this, x+vx, y);
		//衝突するタイルがないので
		if(tile == null){
			x += vx; 
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
			onGround = false; //空中にいると分かるので
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
		
		//ランダムでジャンプする(仮に10パーセント)
		if(rnd.nextInt(10) == 0 && onGround){
			Jump();
		}
	}
	
	/**
	 * ジャンプする
	 */
	public void Jump(){
		if(onGround){
			//上向きに速度を追加(javaでは天井が0だから)
			vy = -jump_speed;
			onGround = false;
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
