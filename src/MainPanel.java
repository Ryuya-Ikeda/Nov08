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
 * �Q�[���̊e��ݒ�����Ă���
 * �����I�ɃQ�[���𓮂����Ă���̂͂��̃N���X
 * main����Ă΂�ē�������Ă���
 * @author riked
 *
 */
public class MainPanel extends JPanel implements Runnable, KeyListener{
	//�p�l���T�C�Y
	//public static final int WIDTH = 640;
	//public static final int HEIGHT = 480;

	public static final int WIDTH = 800;
	public static final int HEIGHT = 640;

	
	// �}�b�v
	private Map map;

	// �v���C���[
	private Player player;

	// �L�[�̏�ԁi������Ă��邩�A������ĂȂ����j
	private boolean upPressed;

	// �Q�[�����[�v�p�X���b�h
	private Thread gameLoop;

	public MainPanel(){
		//�p�l���̐����T�C�Y������B�����ŉ�ʃT�C�Y�����߂�"pack()"���g���̂ɕK�v
		setPreferredSize(new Dimension(WIDTH,HEIGHT));
		//�L�[���͂������悤�ɂ���
		setFocusable(true);

		//�}�b�v�쐬
		map = new Map("map01.dat");

		player = new Player(192, 32, "player.gif", map);

		// �L�[�C�x���g���X�i�[��o�^
		addKeyListener(this);

		// �Q�[�����[�v�J�n
		gameLoop = new Thread(this);
		gameLoop.start();
	}

	/**
	 * �Q�[�����[�v
	 */
	@Override
	public void run(){
		while(true){
			if(upPressed){
				player.Jump();
			}
			// �v���C���[�̏�Ԃ��X�V
			player.Update();

			// �}�b�v�ɂ���X�v���C�g���擾
			LinkedList sprites = map.GetSprites();            
			Iterator iterator = sprites.iterator();
			while (iterator.hasNext()) {
				Sprite sprite = (Sprite)iterator.next();

				// �X�v���C�g�̏�Ԃ��X�V����
				sprite.Update();

				// �v���C���[�ƐڐG���Ă���
				if (player.Contact(sprite)) {
					/*
                // ���ꂪ�R�C����������
                if (sprite instanceof Coin) {
                    Coin coin = (Coin)sprite;
                    // �R�C���͏�����
                    sprites.remove(coin);
                    // �����`��
                    coin.play();
                    // sprites����폜�����̂�
                    // break���Ȃ���iterator�����������Ȃ�
                    break;
                }
					 */
				}
			}
			
			map.Lotation();
			
			// �ĕ`��
			repaint();

			// �x�~
			try {
				Thread.sleep(20);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

	}

	/**
	 * �`�揈��
	 */
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		
		//�w�i��F�œh��Ԃ�
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, getWidth(), getHeight());
		
		// X�����̃I�t�Z�b�g���v�Z
        int relativeX = MainPanel.WIDTH / 2 - (int)player.GetX();
        // �}�b�v�̒[�ł̓X�N���[�����Ȃ��悤�ɂ���
        relativeX = Math.min(relativeX, 0);
        relativeX = Math.max(relativeX, MainPanel.WIDTH - map.GetWidth());

        // Y�����̃I�t�Z�b�g���v�Z
        int relativeY = MainPanel.HEIGHT / 2 - (int)player.GetY();
        // �}�b�v�̒[�ł̓X�N���[�����Ȃ��悤�ɂ���
        relativeY = Math.min(relativeY, 0);
        relativeY = Math.max(relativeY, MainPanel.HEIGHT - map.GetHeight());

        // �}�b�v��`��
        map.Draw(g, relativeX, relativeY);

        // �v���C���[��`��
        player.Draw(g, relativeX, relativeY);
        
        // �X�v���C�g��`��
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
