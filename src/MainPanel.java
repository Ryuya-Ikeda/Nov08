﻿import java.applet.Applet;
import java.applet.AudioClip;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Iterator;
import java.util.LinkedList;
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
	
	//スコア
	private Score score;

	// マップ
	private Map map;

	// プレイヤー
	private Player player;

	// キーの状態（押されているか、押されてないか）
	private boolean upPressed;

	// ゲームループ用スレッド
	private Thread gameLoop;

	private AudioClip sound;

	public MainPanel(){

		//自動で画面サイズを決める"pack()"を使うのに必要
		setPreferredSize(new Dimension(WIDTH,HEIGHT));
		//キー入力を許すようにする
		setFocusable(true);

		//マップ作成
		map = new Map("map01.dat",this);

		//player = new Player(192, 32, "player.gif", map, this);
		player = new Player(320, 32, "player.gif", map, this);

		//スコア表示
		score = new Score(player);

		// キーイベントリスナーを登録
		addKeyListener(this);

		sound = Applet.newAudioClip(getClass().getResource("music/bgm.wav"));
		
		// ゲームループ開始
		start();
	}

	public void start(){
		if(gameLoop == null){
			gameLoop = new Thread(this);
			gameLoop.start();
			sound.loop();
		}
	}

	/**
	 * ゲームループ
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

						// ちゃり〜ん
						coin.play();
						player.Add_Score(coin.score);
						// spritesから削除したので
						// breakしないとiteratorがおかしくなる
						break;
					}
					else if(sprite instanceof GrandFather){
						GrandFather gf = (GrandFather)sprite;
						
						//ジャンプで踏んだら
						if(player.GetY() < gf.GetY()){
							//おじいちゃんを踏み台にプレイヤーはジャンプする
							player.Tread();
						}
						
						// 爺さんは亡くなる
						sprites.remove(gf);
						map.Sprite_delete(Map.PixelsToTiles(gf.x),Map.PixelsToTiles(gf.y));
						
						// ゴキッ
						gf.play();
						player.Add_Score(gf.score);
						break;
					}
					else if(sprite instanceof Slime){
						Slime slime = (Slime)sprite;
						
						//ジャンプで踏んだら
						if(player.GetY() < slime.GetY()){
							//スライムは消える
							sprites.remove(slime);
							map.Sprite_delete(Map.PixelsToTiles(slime.x),Map.PixelsToTiles(slime.y));
							//スライムを踏み台にプレイヤーはジャンプする
							player.Tread();
							
							//スライムを踏んだ音
							slime.play();
							player.Add_Score(slime.score);
						}
						//普通にぶつかったら
						else{
							GameOver();
						}
						break;
					}
					else if(sprite instanceof Animal){
						Animal animal = (Animal)sprite;
						
						//ジャンプで踏んだら
						if(player.GetY() < animal.GetY()){
							//動物は消える
							sprites.remove(animal);
							map.Sprite_delete(Map.PixelsToTiles(animal.x),Map.PixelsToTiles(animal.y));
							//動物を踏み台にプレイヤーはジャンプする
							player.Tread();
							
							//動物の鳴き声
							animal.play();
							player.Add_Score(animal.score);
						}
						//普通にぶつかったら
						else{
							GameOver();
						}
						break;
					}
				} 
			}

			map.Lotation();

			// 再描画
			repaint();

			// 休止
			try {
				Thread.sleep(60);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}


		}

	}

	public void stop(){
		gameLoop = null;
	}


	/**
	 * 描画処理
	 */

	public void paintComponent(Graphics g){
		super.paintComponent(g);

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
			Sprite sprite = (Sprite)iterator.next();
			sprite.Draw(g, relativeX, relativeY);
		}
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
		sound.stop();
		JOptionPane.showMessageDialog(null, "GAME_OVER");
		stop();
		this.setVisible(false);
		Nov08.Content(this);
	}
}
