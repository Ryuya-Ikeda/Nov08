import java.applet.Applet;
import java.applet.AudioClip;
import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.ImageIcon;


public class Result {
	
	private Player player;
	private MainPanel mainPanel;
	private int Width;

	public Result(Player p, MainPanel mp){
		player = p;
		mainPanel = mp;
	}
	
	
public void result_show(Graphics g){
	
		Width = mainPanel.getWidth();
		String str = null;
		AudioClip sound;
	    System.out.println("OK");

		
		//bgmの設定
		sound = Applet.newAudioClip(getClass().getResource("music/result.wav"));
		sound.play();
		
		mainPanel.paintComponent(g);
	    Graphics2D g2 = (Graphics2D) g;
	    Image img;
	    ImageIcon icon = new ImageIcon(getClass().getResource("image/result_back.jpg"));
		img = icon.getImage();		 
	    g2.drawImage(img, 0, 0, mainPanel);
	    
	    AlphaComposite composite = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f);
        g2.setComposite(composite);
        g2.setColor(Color.BLACK);
	    g2.fillRect(0, 0, mainPanel.getWidth(), mainPanel.getHeight());
	    
	    composite = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f);
        g2.setComposite(composite);
	    g2.setColor(Color.RED);
	    
	    str = "GAME  OVER";
	    g2.drawString(str,(Width - getStringWidth(str,g)) / 2, 80);
	    
	    str = "----------------RESULT----------------";
	    g2.drawString(str,(Width - getStringWidth(str,g)) / 2, 150);
	    
	    str = "SCORE   " + player.Get_Score();
	    g2.drawString(str, (Width - getStringWidth(str,g)) / 2, 200);
	    
	    str = "RANK   " + getRank(player.Get_Score());
	    g2.drawString(str, (Width - getStringWidth(str,g)) / 2, 230);

	    str = "----------------RANKING----------------";
	    g2.drawString(str, (Width - getStringWidth(str,g)) / 2, 350);
	    
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
		    	
			    g2.drawString(i +  "   " + str, (Width - getStringWidth(str+i,g2)) / 2, height + (i-1) * 30);
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
