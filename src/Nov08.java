import java.awt.Container;

import javax.swing.JFrame;

/**
 * �v���O�����̍���
 * main�֐��u���Ă��܂�
 * ��{�I�ɂ�����K�v�Ȃ��B�B�B�͂�
 */
public class Nov08 extends JFrame{
	public Nov08(){
		//�^�C�g��
		setTitle("�d�g�g.INT_��i");
		//�T�C�Y�ύX�s��
		setResizable(false);
		
		//���C���p�l�����쐬�A�t���[���ɒǉ�
		MainPanel panel = new MainPanel();
		Container contentPane = getContentPane();
		contentPane.add(panel);
		
		//�p�l���T�C�Y�ɍ��킹�ăt���[���T�C�Y����������
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
