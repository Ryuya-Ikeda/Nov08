import java.applet.AudioClip;
import java.applet.Applet;
import java.awt.AlphaComposite;
import java.awt.Button;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.StringWriter;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.concurrent.Delayed;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

/**
 * ゲームの各種設定をしていく
 * 実質的にゲームを動かしているのはこのクラス
 * mainから呼ばれて動作をしていく
 * @author riked
 *
 */
public class MainPanel extends JPanel implements Runnable, KeyListener, ActionListener{
	//パネルサイズ
	//public static final int WIDTH = 640;
	//public static final int HEIGHT = 480;

	public static final int WIDTH = 800;
	public static final int HEIGHT = 640;
	private static final Thread NULL = null;
		
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
	
	private JButton bt1;
	
	
	public static int resultflag = 0;
	
	private int menuflag = 1;
	private int gof = 0;
	
	private int toggleflag = 0;	/////スタートメニューのフラグ


	//private static boolean gameflag = true; //ゲームの続行フラグ

	public MainPanel(){

		//繝代ロ繝ォ縺ョ謗ィ螂ィ繧オ繧、繧コ繧呈アコ螳壹自動で画面サイズを決める"pack()"を使うのに必要

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

		// ゲームループ開始
		//start();
	}

	public void start(){
		if(gameLoop == null){
			gameLoop = new Thread(this);
			gameLoop.start();
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
				} 
			}

			map.Lotation();

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

	public void paintComponent(Graphics g){
		int i=0;
		super.paintComponent(g);


		//背景を色で塗りつぶす
		g.setColor(Color.black);
		g.fillRect(0, 0, getWidth(), getHeight());
		
		if(menuflag == 1){
			startmenu(g);
			return;
		}
		

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
		
		
		
		if(resultflag == 1){
			result_show(g);
		}
	}
	
	@Override
	 public void keyReleased(KeyEvent e) {
	  int key = e.getKeyCode();
	  if (key == KeyEvent.VK_UP) {
	   upPressed = false;
	  }
	 }

	@Override
	public void keyPressed(KeyEvent e) {
		//ゲーム開始後のとき
		if (gameLoop != null) {
			int key = e.getKeyCode();
			if (key == KeyEvent.VK_UP) {
				upPressed = true;
			}
		}
		
		//ゲーム開始前のとき(メニュー選択時)
		else if (menuflag == 1) {
			int key = e.getKeyCode();
			
			if (key == KeyEvent.VK_UP) {
				toggleflag = 0;
				repaint();
			}
			else if (key == KeyEvent.VK_DOWN) {
				toggleflag = 1;
				repaint();
			}
			else if (key == KeyEvent.VK_RIGHT && toggleflag==0) {
				menuflag = 0;
				start();
			}
		}
	}
	
	

	public void keyTyped(KeyEvent arg0) {
	}
	
	 public void startmenu(Graphics g) {
	    	if (toggleflag == 0) {
	    		g.setColor(Color.YELLOW);
	    		g.drawString("START→", 200, 200);
	    		g.setColor(Color.WHITE);
	    		g.drawString("SCORE CREAR", 200, 250);
	    	}
	    	else if (toggleflag == 1) {
	    		g.setColor(Color.WHITE);
	    		g.drawString("START→", 200, 200);
	    		g.setColor(Color.YELLOW);
	    		g.drawString("SCORE CREAR", 200,250);
	    	}
	    }


	/**
	 * ゲームオーバーの処理
	 */
	
	public void GameOver(){
		//JOptionPane.showMessageDialog(null, "GAME_OVER");
		stop();
		resultflag = 1; //ゲームオーバフラグの起動
		gof = 1;
		sound = Applet.newAudioClip(getClass().getResource("music/result.wav"));
		sound.play();
	}
	
	public void actionPerformed(ActionEvent e){
		
		sound.stop();
		
		resultflag = 0;
		menuflag = 1;
		this.requestFocus();
		repaint();
		
	}
	
	public void result_show(Graphics g){
		
		String str;
		
		//リトライボタン
		bt1 = new JButton("Retry");
		bt1.addActionListener(this);
		bt1.setBounds((this.WIDTH - getStringWidth("Retry", g)) / 2, this.HEIGHT - 80, 100, 30);
		this.add(bt1);
		
		super.paintComponent(g);
	    Graphics2D g2 = (Graphics2D) g;
	    Image img;
	    ImageIcon icon = new ImageIcon(getClass().getResource("image/result_back.jpg"));
		img = icon.getImage();		 
	    g2.drawImage(img, 0, 0, this);
	    
	    AlphaComposite composite = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f);
        g2.setComposite(composite);
        g2.setColor(Color.BLACK);
	    g2.fillRect(0, 0, getWidth(), getHeight());
	    
	    composite = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f);
        g2.setComposite(composite);
	    g2.setColor(Color.RED);
	    
	    str = "GAME  OVER";
	    g2.drawString(str,(this.WIDTH - getStringWidth(str,g)) / 2, 80);
	    
	    str = "----------------RESULT----------------";
	    g2.drawString(str,(this.WIDTH - getStringWidth(str,g)) / 2, 150);
	    
	    str = "SCORE   " + player.Get_Score();
	    g2.drawString(str, (this.WIDTH - getStringWidth(str,g)) / 2, 200);
	    
	    str = "RANK   " + getRank(player.Get_Score());
	    g2.drawString(str, (this.WIDTH - getStringWidth(str,g)) / 2, 230);

	    str = "----------------RANKING----------------";
	    g2.drawString(str, (this.WIDTH - getStringWidth(str,g)) / 2, 350);
	    
	    drawRanking(g2, 400);

	}
	
	
	public  int getStringWidth(String str,Graphics g){
		FontMetrics fm;
		fm = g.getFontMetrics();
		int a = fm.stringWidth(str);
		return a;		
	}
	
	public String getRank(int score){
		String rank = null;
		
		if(score < 25000){
			rank = "D";
		}
		
		else if(score <= 45000){
			rank = "C";
		}
		
		else if(score <= 75000){
			rank = "B";
		}
		
		else if(score <= 100000){
			rank = "A";
		}
		
		else{
			rank = "S";
		}
		
		return rank;
	}
	
	public void drawRanking(Graphics g2, int height){
		
		int i = 1;
		int fl = 0;
	

		try{
			
			File f1 = new File("src/score/ranking.txt");
			File f2 = new File("src/score/tmp.txt");

			BufferedReader br = new BufferedReader(new FileReader(f1)); //ランキング保存ファイル
			BufferedWriter bw = new BufferedWriter(new FileWriter(f2)); //更新後スコアの一時ファイル

		    String str = null;
		    
		    //更新後のスコアを反映
		    while((str = br.readLine()) != null){
		    	
		    	int p_score = player.Get_Score();
		    	if( (Integer.parseInt(str) < p_score || Integer.parseInt(str) == p_score) && fl == 0){
		    		str = String.valueOf(p_score);
		    		bw.write(String.valueOf(p_score));
		    		fl = 1;
		    	}
		    	
		    	else{
		    		bw.write(str);
		    	}
		    	
			    g2.drawString(i +  "   " + str, (this.WIDTH - getStringWidth(str+i,g2)) / 2, height + (i-1) * 30);
	    		bw.newLine();
		        i++;
		    }
		 
		    br.close();
		    bw.close();
		    
			f1.delete();
			f2.renameTo(f1);

		 
		    }catch(IOException e){
		        System.out.println(e);
		    }
		
	}
	
	 


}
