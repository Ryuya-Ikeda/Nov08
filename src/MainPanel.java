import java.applet.Applet;
import java.applet.AudioClip;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.Iterator;
import java.util.LinkedList;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import java.awt.AlphaComposite;

/**
 * ゲームの各種設定をしていく
 * 実質的にゲームを動かしているのはこのクラス
 * mainから呼ばれて動作をしていく
 * @author riked
 *
 */
public class MainPanel extends JPanel implements Runnable, KeyListener{
	//パネルサイズ
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

	//BGM用
	private AudioClip sound;

	//ボタン
	private JButton bt1;

	private boolean resultflag = false;
	private boolean menuflag = true;
	private boolean toggleflag = false;	//スタートメニューのフラグ

	public MainPanel(){

		// 自動で画面サイズを決める"pack()"を使うのに必要
		setPreferredSize(new Dimension(WIDTH,HEIGHT));
		// キー入力を許すようにする
		setFocusable(true);

		// マップ作成
		map = new Map("start.dat",this);

		player = new Player(400, 32, "player.gif", map, this);

		// スコア表示
		score = new Score(player);

		// キーイベントリスナーを登録
		addKeyListener(this);

		// BGMを設定
		sound = Applet.newAudioClip(getClass().getResource("music/bgm.wav"));

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

						// ちゃり?ん
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
			// プレイヤーの状態を更新
			player.Update();

			map.Lotation();

			// 再描画
			repaint();

			// 休止
			try {
				Thread.sleep(65);
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

		if(menuflag == true){
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
			Sprite sprite = (Sprite)iterator.next();
			sprite.Draw(g, relativeX, relativeY);
		}
		if(resultflag == true){
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
		else if (menuflag) {
			int key = e.getKeyCode();

			if (key == KeyEvent.VK_UP) {
				toggleflag = false;
				repaint();
			}
			else if (key == KeyEvent.VK_DOWN) {
				toggleflag = true;
				repaint();
			}
			else if (key == KeyEvent.VK_RIGHT && ! toggleflag) {
				menuflag = false;
				start();
			}
		}
	}



	public void keyTyped(KeyEvent arg0) {
	}

	public void startmenu(Graphics g) {
		if (! toggleflag) {
			g.setColor(Color.YELLOW);
			g.drawString("START→", 200, 200);
			g.setColor(Color.WHITE);
			g.drawString("SCORE CREAR", 200, 250);
		}
		else if (toggleflag) {
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
		stop();
		resultflag = true; //ゲームオーバフラグの起動
		sound.stop();
		sound = Applet.newAudioClip(getClass().getResource("music/result.wav"));
		sound.play();
	}

	/*
	public void actionPerformed(ActionEvent e){
		sound.stop();
		resultflag = false;
		menuflag = true;
		this.setVisible(false);
		Nov08.Content(this);
	}
	 */
	public void NewGame(){
		this.setVisible(false);
		Nov08.Content(this);
	}

	public void result_show(Graphics g){

		String str;

		//リトライボタン
		bt1 = new JButton("Retry");
		bt1.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				sound.stop();
				resultflag = false;
				menuflag = true;
				NewGame();
			}
		});

		addKeyListener(new KeyListener() {
			@Override
			public void keyTyped(KeyEvent e) {}
			@Override
			public void keyReleased(KeyEvent e) {}
			@Override
			public void keyPressed(KeyEvent e) {
				if(KeyEvent.VK_ENTER == e.getKeyCode()){
					sound.stop();
					resultflag = false;
					menuflag = true;
					NewGame();
				}
			}
		});
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

		str = "SCORE   " + player.score;
		g2.drawString(str, (this.WIDTH - getStringWidth(str,g)) / 2, 200);

		str = "RANK   " + getRank(player.score);
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

			File f1 = new File("/score/ranking.txt");
			File f2 = new File("/score/tmp.txt");

			File newf = new File("/score");

			if(!newf.exists()){
				newf.mkdirs();
				f1.createNewFile();
				f2.createNewFile();
			}


			BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(f1),"UTF-8")); //ランキング保存ファイル
			BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(f2),"UTF-8")); //更新後スコアの一時ファイル

			String str = null;


			if(f1.length() == 0){
				bw.write(player.score + "\n");
				g2.drawString(i +  "   " + player.score, (this.WIDTH - getStringWidth(str+i,g2)) / 2, height + (i-1) * 30);
				i++;
				for(int j=1;j<5;j++){
					bw.write("0\n");
					g2.drawString(i +  "   " + "0", (this.WIDTH - getStringWidth(str+i,g2)) / 2, height + (i-1) * 30);
					i++;
				}
			}

			else
			{
				//プレイヤーのスコア
				int p_score = player.score; 
				//更新後のスコアを反映
				for(int j = 0;j < 5; j++){
					str = br.readLine();
					if(str == "") str = "0";
					if( (Integer.parseInt(str) < p_score || Integer.parseInt(str) == p_score) && fl == 0){
						bw.write(String.valueOf(p_score));
						g2.drawString(i +  "   " + p_score, (this.WIDTH - getStringWidth(str+i,g2)) / 2, height + (i-1) * 30);
						bw.newLine();
						bw.write(String.valueOf(str));
						fl = 1;
						i++;
						j++; //5つまでしかスコアは保持しない。通常より、描画するスコアが多いから
					}
					else{
						bw.write(String.valueOf(str));
					}
					g2.drawString(i +  "   " + str, (this.WIDTH - getStringWidth(str+i,g2)) / 2, height + (i-1) * 30);
					bw.newLine();
					i++;
				}
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
