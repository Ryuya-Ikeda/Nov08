import java.awt.Graphics;
import java.awt.Point;

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
	private int jump_speed = 12;

	//速度
	//protected int vx;
	protected double vy;

	//地面に足がついているか判定
	private boolean onGround;

	public Player(double x, double y, String fileName, Map map){
		super(x,y,fileName,map);

		//vx = 0;
		vy = 0;
		onGround = false;
	}

	/**
	 * 停止
	 */
	public void Stop(){
		//vx = 0;
	}

	/**
	 * 加速
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
	 * 状態を更新
	 */
	public void Update(){
		//重力で下向きに加速度がかかる
		vy += Map.GRAVITY;
		Point tile;

		//x方向の当たり判定
		//double newX = x + vx;//移動先座標を決定
		/*
		 * 移動先座標で衝突するタイルの位置を取得
		 * x方向だけ考える
		 */
		//tile = map.GetTileCllision(this, newX, y);
		//衝突するタイルがないので
		//if(tile == null){
		//	x = newX;//移動
		//} else {
		//	//衝突するタイルがあるのでブロックにめり込まないように位置調整
		//	x = Map.TilesToPixels(tile.x) - width;
		//	vx = 0; //速度を0へ
		//}

		//y方向の当たり判定
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
}
