import java.awt.Container;

import javax.swing.JFrame;

/**
 * プログラムの根幹
 * main関数置いています
 * 基本的にいじる必要ない。。。はず
 */
public class Nov08 extends JFrame{
 private static Container contentPane;
 public Nov08(){

  //タイトル
  setTitle("電波組.INT_作品");
  //サイズ変更不可
  setResizable(false);
  
  //ゲームの準備
  MainPanel panel = new MainPanel();
  contentPane = getContentPane();
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
 
 public static void Content(MainPanel panel){
  contentPane.remove(panel);
  panel.setVisible(false);
  MainPanel newGame = new MainPanel();
  contentPane.add(newGame);
  newGame.setVisible(true);
  contentPane.validate();
 }
}