import java.awt.Container;

import javax.swing.JFrame;

/**
 * プログラムの根幹
 * main関数置いています
 * 基本的にいじる必要ない。。。はず
 */
public class Nov08 extends JFrame{
	public Nov08(){
		//タイトル
		setTitle("電波組.INT_作品");
		//サイズ変更不可
		setResizable(false);
		
		//メインパネルを作成、フレームに追加
		MainPanel panel = new MainPanel();
		Container contentPane = getContentPane();
		contentPane.add(panel);
		
		//パネルサイズに合わせてフレームサイズを自動決定
		pack();
	}
	
	/**
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		Nov08 frame = new Nov08();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}
}
