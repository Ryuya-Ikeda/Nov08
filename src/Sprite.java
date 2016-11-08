import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;

import javax.swing.ImageIcon;

/**
 * 物体の抽象クラス
 * 主人公や敵キャラクター、その他オブジェクトなどは
 * すべてこのクラスを継承して作る
 * ただし、ブロックはかなり数が多いので省く。
 */

public abstract class Sprite {

	//位置
	protected double x;
	protected double y;

	//幅
	protected int width;
	//高さ
	protected int height;

	//画像
	protected Image image;

	//カウンタ(アニメーション用,2つ以上の画像を交互に表示する用)
	protected int count;

	//マップへの参照
	protected Map map;

	public Sprite(double x, double y, String fileName, Map map){
		this.x = x;
		this.y = y;
		this.map = map;

		width = 32;
		height = 32;

		LoadImage(fileName);

		count = 0;

		AnimationThread thread = new AnimationThread();
		thread.start();
	}

    public double GetX() {
        return x;
    }

    public double GetY() {
        return y;
    }

    public int GetWidth() {
        return width;
    }

    public int GetHeight() {
        return height;
    }

	/**
	 * 物体の状態を更新
	 */
	public abstract void Update();

	/**
	 * 描画
	 */
	public void Draw(Graphics g, int relative_x, int relative_y){
		g.drawImage(image,
				(int)x + relative_x,
				(int)y + relative_y,
				(int)x + relative_x + width,
				(int)y + relative_y + height,
				count * width, 0,
				count * width + width,
				height,
				null);
	}

	/**
	 * 他の物体とぶつかっているか
	 */
	public boolean Contact(Sprite sprite){
		Rectangle player = new Rectangle((int)x, (int)y, width, height);
		Rectangle spriteRect = new Rectangle((int)sprite.GetX(), (int)sprite.GetY(), sprite.GetWidth(), sprite.GetHeight());
		if(player.intersects(spriteRect)){
			return true;
		} else { return false; }
	}

	/**
	 * 画像をロード
	 */
	private void LoadImage(String fileName){
		ImageIcon icon = new ImageIcon(getClass().getResource("image/" + fileName));
		image = icon.getImage();
	}

	/**
	 * アニメーション表示を行うためのスレッド
	 */
	private class AnimationThread extends Thread{
		public void run(){
			while(true){
				//count切り替え
				if(count == 0) { count = 1; }
				else if (count == 1){ count = 0; }

				try{
					Thread.sleep(300);
				} catch (InterruptedException e){
					e.printStackTrace(); //例外情報を出力
				}
			}

		}
	}
}
