import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.concurrent.Delayed;

import javax.swing.JOptionPane;
import javax.swing.JPanel;

/**
 * ゲームの各種設定をしていく
 * 実質的にゲームを動かしているのはこのクラス
 * mainから呼ばれて動作をしていく
 * @author riked
 *
 */
public class MainPanel extends JPanel implements Runnable, KeyListener{
	//パネルサイズ
	//public static final int WIDTH = 640;
	//public static final int HEIGHT = 480;

	public static final int WIDTH = 800;
	public static final int HEIGHT = 640;

<<<<<<< HEAD
=======
	//スコア
	private Score score;
	
>>>>>>> 70f28d5e1b6246ee955eac76b4c9a9bd18a9fc39
	// マップ
	private Map map;
	
	// 郢ァ繧ア郢ァ繧ヲ郢ァ縲	private Score score;

<<<<<<< HEAD
	// プレイヤー
	private Player player;

	// キーの状態（押されているか、押されてないか）
=======
	// 繝励Ξ繧、繝、繝シ
	private Player player;

	// 繧ュ繝シ縺ョ迥カ諷具シ域款縺輔ｌ縺ヲ縺ｋ縺九押されてないか）
>>>>>>> 70f28d5e1b6246ee955eac76b4c9a9bd18a9fc39
	private boolean upPressed;

	// ゲームループ用スレッド
	private Thread gameLoop;

	//private static boolean gameflag = true; //ゲームの続行フラグ

	public MainPanel(){
<<<<<<< HEAD
		//パネルの推奨サイズを決定。自動で画面サイズを決める"pack()"を使うのに必要
=======
		//繝代ロ繝ォ縺ョ謗ィ螂ィ繧オ繧、繧コ繧呈アコ螳壹自動で画面サイズを決める"pack()"を使うのに必要
>>>>>>> 70f28d5e1b6246ee955eac76b4c9a9bd18a9fc39
		setPreferredSize(new Dimension(WIDTH,HEIGHT));
		//キー入力を許すようにする
		setFocusable(true);

<<<<<<< HEAD
=======


		
>>>>>>> 70f28d5e1b6246ee955eac76b4c9a9bd18a9fc39
		//マップ作成
		map = new Map("map01.dat",this);

		//player = new Player(192, 32, "player.gif", map, this);
		player = new Player(320, 32, "player.gif", map, this);
		
<<<<<<< HEAD
=======
		//スコア表示
		score = new Score(player);
		
		
>>>>>>> 70f28d5e1b6246ee955eac76b4c9a9bd18a9fc39
		// キーイベントリスナーを登録
		addKeyListener(this);

		// ゲームループ開始
		start();
	}

	public void start(){
		if(gameLoop == null){
			gameLoop = new Thread(this);
			gameLoop.start();
		}
	}

	/**
<<<<<<< HEAD
	 * ゲームループ
=======
	 * 繧イ繝シ繝ープ
>>>>>>> 70f28d5e1b6246ee955eac76b4c9a9bd18a9fc39
	 */
	@Override
	public void run(){
		Thread thisThread = Thread.currentThread();
		while(gameLoop == thisThread){
			if(upPressed){
				player.Jump();
			}
			// プレイヤーの状態を更新
			player.Update();

			// マップにいるスプライトを取得
			LinkedList sprites = map.GetSprites();            
			Iterator iterator = sprites.iterator();
			while (iterator.hasNext()) {
				Sprite sprite = (Sprite)iterator.next();

				// スプライトの状態を更新する
				sprite.Update();

				
				// プレイヤーと接触してたら
				if (player.Contact(sprite)) {

					// それがコインだったら
					if (sprite instanceof Coin) {
						Coin coin = (Coin)sprite;

						// コインは消える
						sprites.remove(coin);
						map.Sprite_delete(Map.PixelsToTiles(coin.x),Map.PixelsToTiles(coin.y));
<<<<<<< HEAD
						// ちゃり～ん
						coin.play();
=======
						// ちゃり〜ん
						coin.play();
						player.Add_Score(coin.score);
>>>>>>> 70f28d5e1b6246ee955eac76b4c9a9bd18a9fc39
						// spritesから削除したので
						// breakしないとiteratorがおかしくなる
						break;
					} 
				} 
			}

			map.Lotation();
<<<<<<< HEAD
=======

>>>>>>> 70f28d5e1b6246ee955eac76b4c9a9bd18a9fc39
			// 再描画
			repaint();
			
			// 休止
			try {
				Thread.sleep(40);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}


		}

	}

	public void stop(){
		gameLoop = null;
	}


	/**
<<<<<<< HEAD
	 * 描画処理
	 */
=======
	 * 謠冗判蜃ヲ逅	 */
>>>>>>> 70f28d5e1b6246ee955eac76b4c9a9bd18a9fc39
	public void paintComponent(Graphics g){
		int i=0;
		super.paintComponent(g);

<<<<<<< HEAD
=======

>>>>>>> 70f28d5e1b6246ee955eac76b4c9a9bd18a9fc39
		//背景を色で塗りつぶす
		g.setColor(Color.black);
		g.fillRect(0, 0, getWidth(), getHeight());

		// X方向のオフセットを計算
		int relativeX = MainPanel.WIDTH / 2 - (int)player.GetX();
		// マップの端ではスクロールしないようにする
		relativeX = Math.min(relativeX, 0);
		relativeX = Math.max(relativeX, MainPanel.WIDTH - map.GetWidth());

		// Y方向のオフセットを計算
		int relativeY = MainPanel.HEIGHT / 2 - (int)player.GetY();
		// マップの端ではスクロールしないようにする
		relativeY = Math.min(relativeY, 0);
		relativeY = Math.max(relativeY, MainPanel.HEIGHT - map.GetHeight());

		// マップを描画
		map.Draw(g, relativeX, relativeY);

		// プレイヤーを描画
		player.Draw(g, relativeX, relativeY);
		
		//スコアを描画
		score.Draw(g);

		// スプライトを描画
		LinkedList sprites = map.GetSprites();            
		Iterator iterator = sprites.iterator();
		while (iterator.hasNext()) {
			i++;
			Sprite sprite = (Sprite)iterator.next();
			sprite.Draw(g, relativeX, relativeY);
		}

		System.out.printf("%d\n",i);
		i=0;
	}

	@Override
	public void keyPressed(KeyEvent e) {
		int key = e.getKeyCode();
		if (key == KeyEvent.VK_UP) {
			upPressed = true;
		}

	}

	@Override
	public void keyReleased(KeyEvent e) {
		int key = e.getKeyCode();
		if (key == KeyEvent.VK_UP) {
			upPressed = false;
		}
	}

	public void keyTyped(KeyEvent arg0) {
	}


	/**
	 * ゲームオーバーの処理
	 */
	public void GameOver(){
		JOptionPane.showMessageDialog(null, "GAME_OVER");
		stop();
	}


}
