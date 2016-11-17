import java.applet.Applet;
import java.applet.AudioClip;

public class Coin extends Sprite{
	//�R�C����������Ƃ��̉�
	private AudioClip sound;
	
	public Coin(double x, double y, String fileName, Map map, MainPanel mainpanel){
		super(x,y,fileName,map,mainpanel);
		
		//�T�E���h�̃��[�h
		sound = Applet.newAudioClip(getClass().getResource("music/coin.wav"));
		
		//�X�R�A�̌���(����)
		score = 1000;
	}

	@Override
	public void Update() {
		// TODO Auto-generated method stub
		
	}
	
	/**
	 * �T�E���h�̍Đ�
	 */
	public void play(){
		sound.play();
	}
	
	
}
