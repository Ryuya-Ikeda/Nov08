import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;

import javax.swing.ImageIcon;

/**
 * ���̂̒��ۃN���X
 * ��l����G�L�����N�^�[�A���̑��I�u�W�F�N�g�Ȃǂ�
 * ���ׂĂ��̃N���X���p�����č��
 * �������A�u���b�N�͂��Ȃ萔�������̂ŏȂ��B
 */

public abstract class Sprite {

	//�ʒu
	protected double x;
	protected double y;

	//��
	protected int width;
	//����
	protected int height;

	//�摜
	protected Image image;

	//�J�E���^(�A�j���[�V�����p,2�ȏ�̉摜�����݂ɕ\������p)
	protected int count;

	//�}�b�v�ւ̎Q��
	protected Map map;

	public Sprite(double x, double y, String fileName, Map map){
		this.x = x;
		this.y = y;
		this.map = map;

		width = 32;
		height = 32;

		LoadImage(fileName);

		count = 0;

		AnimationThread thread = new AnimationThread();
		thread.start();
	}

    public double GetX() {
        return x;
    }

    public double GetY() {
        return y;
    }

    public int GetWidth() {
        return width;
    }

    public int GetHeight() {
        return height;
    }

	/**
	 * ���̂̏�Ԃ��X�V
	 */
	public abstract void Update();

	/**
	 * �`��
	 */
	public void Draw(Graphics g, int relative_x, int relative_y){
		g.drawImage(image,
				(int)x + relative_x,
				(int)y + relative_y,
				(int)x + relative_x + width,
				(int)y + relative_y + height,
				count * width, 0,
				count * width + width,
				height,
				null);
	}

	/**
	 * ���̕��̂ƂԂ����Ă��邩
	 */
	public boolean Contact(Sprite sprite){
		Rectangle player = new Rectangle((int)x, (int)y, width, height);
		Rectangle spriteRect = new Rectangle((int)sprite.GetX(), (int)sprite.GetY(), sprite.GetWidth(), sprite.GetHeight());
		if(player.intersects(spriteRect)){
			return true;
		} else { return false; }
	}

	/**
	 * �摜�����[�h
	 */
	private void LoadImage(String fileName){
		ImageIcon icon = new ImageIcon(getClass().getResource("image/" + fileName));
		image = icon.getImage();
	}

	/**
	 * �A�j���[�V�����\�����s�����߂̃X���b�h
	 */
	private class AnimationThread extends Thread{
		public void run(){
			while(true){
				//count�؂�ւ�
				if(count == 0) { count = 1; }
				else if (count == 1){ count = 0; }

				try{
					Thread.sleep(300);
				} catch (InterruptedException e){
					e.printStackTrace(); //��O�����o��
				}
			}

		}
	}
}
