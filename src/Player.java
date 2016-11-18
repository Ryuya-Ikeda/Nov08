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

	//加速度
	//protected int vx;
	protected double vy;

	//地面に足がついているか判定
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
	 * 止まる
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
		 * x方向だけ考える
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

		//y方向のあたり判定	
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
	 * スコアを加算
	 * @param score
	 */
	public void Add_Score(int score){
		this.score += score;
	}

	/**
	 * 敵を踏んだ時
	 */
	public void Tread(){
		onGround = true;
		Jump();
	}
}
