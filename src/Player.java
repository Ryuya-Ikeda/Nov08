import java.awt.Graphics;
import java.awt.Point;

/**
 * �Q�[���̎�l���̐ݒ�
 * �}�Z�I,���Z�I
 * @author riked
 *
 */

public class Player extends Sprite{
	//�X�s�[�h
	private int speed = 6;
	//�W�����v��
	private int jump_speed = 12;

	//���x
	//protected int vx;
	protected double vy;

	//�n�ʂɑ������Ă��邩����
	private boolean onGround;

	public Player(double x, double y, String fileName, Map map){
		super(x,y,fileName,map);

		//vx = 0;
		vy = 0;
		onGround = false;
	}

	/**
	 * ��~
	 */
	public void Stop(){
		//vx = 0;
	}

	/**
	 * ����
	 */
	public void Accelerate(){
		//vx = speed;
	}

	/**
	 * �W�����v����
	 */
	public void Jump(){
		if(onGround){
			//������ɑ��x��ǉ�(java�ł͓V�䂪0������)
			vy = -jump_speed;
			onGround = false;
		}
	}

	/**
	 * ��Ԃ��X�V
	 */
	public void Update(){
		//�d�͂ŉ������ɉ����x��������
		vy += Map.GRAVITY;
		Point tile;

		//x�����̓����蔻��
		//double newX = x + vx;//�ړ�����W������
		/*
		 * �ړ�����W�ŏՓ˂���^�C���̈ʒu���擾
		 * x���������l����
		 */
		//tile = map.GetTileCllision(this, newX, y);
		//�Փ˂���^�C�����Ȃ��̂�
		//if(tile == null){
		//	x = newX;//�ړ�
		//} else {
		//	//�Փ˂���^�C��������̂Ńu���b�N�ɂ߂荞�܂Ȃ��悤�Ɉʒu����
		//	x = Map.TilesToPixels(tile.x) - width;
		//	vx = 0; //���x��0��
		//}

		//y�����̓����蔻��
		double newY = y + vy;
		/*
		 * �ړ�����W�ŏՓ˂���^�C���̈ʒu���擾
		 * y���������l����
		 */
		tile = map.GetTileCllision(this, x, newY);
		//�Փ˂���^�C�����Ȃ��̂�
		if(tile == null){
			y = newY;//�ړ�
			onGround = false;//�󒆂ɂ���ƕ�����̂�
		} else {
			//�Փ˂���^�C��������̂�
			if(vy > 0){ //���ֈړ���
				//�ʒu����
				y = Map.TilesToPixels(tile.y) - height;
				vy = 0; //���n�����̂�y�������x��0��
				onGround = true; //���n�t���O�𗧂Ă�
			} else if(vy < 0){ //�V��ւԂ������̂�
				//��ֈړ���
				y = Map.TilesToPixels(tile.y + 1);
				vy = 0;
			}
		}
	}

	/**
	 * �v���C���[��`��i�I�[�o�[���C�h�j
	 */
    public void Draw(Graphics g, int relativeX, int relativeY) {
        g.drawImage(image,
                (int)x + relativeX,
                (int)y + relativeY,
                (int)x + relativeX + width,
                (int)y + relativeY + height,
                count * width, 0,
                count * width + width, height,
                null);
    }
}
