import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.HeadlessException;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Iterator;
import java.util.LinkedList;

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

	
	// マップ
	private Map map;

	// プレイヤー
	private Player player;

	// キーの状態（押されているか、押されてないか）
	private boolean upPressed;

	// ゲームループ用スレッド
	private Thread gameLoop;

	public MainPanel(){
		//パネルの推奨サイズを決定。自動で画面サイズを決める"pack()"を使うのに必要
		setPreferredSize(new Dimension(WIDTH,HEIGHT));
		//キー入力を許すようにする
		setFocusable(true);

		//マップ作成
		map = new Map("map01.dat");

		player = new Player(192, 32, "player.gif", map);

		// キーイベントリスナーを登録
		addKeyListener(this);

		// ゲームループ開始
		gameLoop = new Thread(this);
		gameLoop.start();
	}

	/**
	 * ゲームループ
	 */
	@Override
	public void run(){
		while(true){
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
					/*
                // それがコインだったら
                if (sprite instanceof Coin) {
                    Coin coin = (Coin)sprite;
                    // コインは消える
                    sprites.remove(coin);
                    // ちゃり～ん
                    coin.play();
                    // spritesから削除したので
                    // breakしないとiteratorがおかしくなる
                    break;
                }
					 */
				}
			}
			
			map.Lotation();
			
			// 再描画
			repaint();

			// 休止
			try {
				Thread.sleep(20);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

	}

	/**
	 * 描画処理
	 */
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		
		//背景を色で塗りつぶす
		g.setColor(Color.BLACK);
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
}
