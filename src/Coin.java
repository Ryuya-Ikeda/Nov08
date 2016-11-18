import java.applet.Applet;
import java.applet.AudioClip;

public class Coin extends Sprite{
	//コインを取ったときの音
	private AudioClip sound;
	
	public Coin(double x, double y, String fileName, Map map, MainPanel mainpanel){
		super(x,y,fileName,map,mainpanel);
		
		//サウンドのロード
		sound = Applet.newAudioClip(getClass().getResource("music/coin.wav"));
		
		//スコアの決定(仮に)
		score = 1000;
	}

	@Override
	public void Update() {
		// TODO Auto-generated method stub
		
	}
	
	/**
	 * サウンドの再生
	 */
	public void play(){
		sound.play();
	}
	
	
}
