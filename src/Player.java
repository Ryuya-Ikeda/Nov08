import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;

import javax.swing.JOptionPane;

/**
 * ゲームの主人公の設定
 * マ〇オ,ワ〇オ
 * @author riked
 *
 */

public class Player extends Sprite{
	//スピード
	private int speed = 6;

	//ジャンプ力

	private int jump_speed = 20;

	//騾溷コヲ
	//protected int vx;
	protected double vy;
	

	//蝨ー髱「縺ォ雜ウ縺後▽縺※縺ｋ縺句愛螳嚥	
	private boolean onGround;

	//元のx座標の位置を保持
	private static double initial_px;
	
	public Player(double x, double y, String fileName, Map map, MainPanel mainPanel){
		super(x,y,fileName,map,mainPanel);
		initial_px = x;
		//vx = 0;
		vy = 0;
		onGround = false;
	}

	/**
	 * 蛛懈ュ「
	 */
	public void Stop(){
		//vx = 0;
	}

	/**
	 * 蜉
	 */
	public void Accelerate(){
		//vx = speed;
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
<<<<<<< HEAD
	 * 状態を更新
=======
	 * 
>>>>>>> 27728db1c9e46f95018787dc2a2f122b18ec97d0
	 */
	@Override
	public void Update(){
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
		
		//x方向の当たり判定
		//double newX = x /*+ vx*/;//移動先座標を決定
		/*
		 * 遘サ蜍募蠎ァ讓吶〒陦晉ェ√☆繧九ち繧、繝ォ縺ョ菴咲スョ繧貞叙蠕		 * x方向だけ考える
		 */
		//tile = map.GetTileCllision(this, newX, y);

		tile = map.GetTileCllision(this, x, y);
		//衝突するタイルがないので
		if(tile == null){
			if(x < initial_px){
				x += 2; //x方向に進める
			}
		} else {
			//衝突するタイルがあるのでブロックにめり込まないように位置調整
			x = Map.TilesToPixels(tile.x) - width;
			//vx = 0; //速度を0へ
		}

		//y譁ケ蜷代蠖薙◆繧雁愛螳		
		double newY = y + vy;
		/*
		 * 移動先座標で衝突するタイルの位置を取得
		 * y方向だけ考える
		 */
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
	 * プレイヤーを描画（オーバーライド）
	 */
    public void Draw(Graphics g, int relativeX, int relativeY) {
        g.drawImage(image,
                (int)x + relativeX,
                (int)y + relativeY,
                (int)x + relativeX + width,
                (int)y + relativeY + height,
                count * width, 0,
                count * width + width, height,
                null);
    }

    /**
     * スコアを加算
     * @param score
     */
	public void Add_Score(int score){
		this.score += score;
	}
	
	public boolean next_Contact(Sprite sprite){
		Rectangle player = new Rectangle((int)x + map.TILE_SIZE, (int)y, width, height);
		Rectangle spriteRect = new Rectangle((int)sprite.GetX(), (int)sprite.GetY(), sprite.GetWidth(), sprite.GetHeight());
		if(player.intersects(spriteRect)){
			return true;
		} else { return false; }
	}
}
