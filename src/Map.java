import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.Robot;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.Random;

import javax.swing.ImageIcon;

/**
 * �}�b�v�̐ݒ�
 * �}�b�v�f�[�^�̓ǂݍ��݁A�}�b�v�f�[�^�̍X�V�A�}�b�v���ł̏Փ˂Ȃ�
 * �������Ă���
 */
public class Map {
	//�^�C���̑傫��
	public static final int TILE_SIZE = 32;
	//�d��
	public static final double GRAVITY = 1.0;

	//�}�b�v
	private char[][] map;

	//�T���̃}�b�v
	private char[][] next_map;

	// �s��
	private static final int ROW = 20;
	// ��
	private static final int COL = 60;
	//��
	private int width;
	//����
	private int height;

	//�u���b�N�̉摜
	private Image blockImg;

	//���̂̃��X�g
	private LinkedList sprites;

	//�������g�p���邽�߂�
	Random rnd = new Random();
	
	//�}�b�v�̍X�V��(�}�b�v�̎��̃f�[�^��ǂݍ��ރL���[�Ƃ��Ċ��p)
	private int update_map_num = 0;
	
	//�}�b�v�̐�
	private static final int MAP_NUM = 1;

	public Map(String fileName){
		sprites = new LinkedList();

		//�}�b�v�̃��[�h
		Load(fileName);
		//�T���̃}�b�v�̃��[�h
		//Reflection(next_map, "map" + (rnd.nextInt(MAP_NUM) + 1) + ".dat");
		Reflection(next_map, "map01.dat");
		width = TILE_SIZE * COL;
		height = TILE_SIZE * ROW;

		//�摜�̃��[�h
		LoadImage();

	}

	/**
	 * �s�N�Z���P�ʂ��^�C���P�ʂɕύX����
	 */
	public static int PixelsToTiles(double pixels) {
		return (int)Math.floor(pixels / TILE_SIZE);
	}

	/**
	 * �^�C���P�ʂ��s�N�Z���P�ʂɕύX����
	 */
	public static int TilesToPixels(int tiles) {
		return tiles * TILE_SIZE;
	}

	/**
	 * �}�b�v�̕`��
	 */
	public void Draw(Graphics g, int relative_x, int relative_y){
		//��������`��͈͂��l����
		int startTileX = PixelsToTiles(-relative_x);
		int endTileX = startTileX + PixelsToTiles(MainPanel.WIDTH) + 1;
		//�`��͈͂��}�b�v���傫���Ȃ�Ȃ��悤�ɒ���
		endTileX = Math.min(endTileX, COL);

		int startTileY = PixelsToTiles(-relative_y);
		int endTileY = startTileY + PixelsToTiles(MainPanel.HEIGHT) + 1;
		//�`��͈͂��}�b�v���傫���Ȃ�Ȃ��悤�ɒ���
		endTileY = Math.min(endTileX, ROW);

		for(int i = startTileY; i < endTileY; i++){
			for(int j = startTileX; j < endTileX; j++){
				switch(map[i][j]){
				case 'B' :
					g.drawImage(blockImg,
							TilesToPixels(j) + relative_x,
							TilesToPixels(i) + relative_y
							,null);
					break;
					/* ���̐�`�悷��u���b�N�̎�ނ�������Ȃ�ǉ� */
				}
			}
		}
	}

	/**
	 * ������newX,newY�łԂ���u���b�N�̍��W��Ԃ�
	 */
	public Point GetTileCllision(Sprite sprite, double newX, double newY){

		// �����_�ȉ��؂�グ
		// ���������_�̊֌W�Ő؂�グ���Ȃ��ƏՓ˂��ĂȂ��Ɣ��肳���ꍇ������

		int fromTileX = PixelsToTiles(Math.min(sprite.GetX(), Math.ceil(newX)));
		int fromTileY = PixelsToTiles(Math.min(sprite.GetY(), Math.ceil(newY)));
		int toTileX = PixelsToTiles(Math.max(sprite.GetX(), newX) + sprite.GetWidth() - 1);
		int toTileY = PixelsToTiles(Math.max(sprite.GetY(), newY) + sprite.GetHeight() - 1);


		//�Ԃ����Ă��邩�T���Ă���
		for(int x = fromTileX; x <= toTileX; x++){
			for(int y = fromTileY; y <= toTileY; y++){
				//��ʊO�͏Փ˔���Ƃ���
				if(x < 0 || x >= COL){
					return new Point(x,y);
				}
				else if(y < 0 || y >= ROW){
					return new Point(x, y);
				}
				else if(map[y][x] == 'B'){
					return new Point(x, y);
				}
			}
		}
		return null;
	}

	/**
	 * �摜�����[�h
	 */
	private void LoadImage(){
		ImageIcon icon = new ImageIcon(getClass().getResource("image/block.gif"));
		blockImg = icon.getImage();
	}

	/**
	 * �}�b�v�����[�h
	 */
	private void Load(String fileName){
		map = new char[ROW][COL];
		next_map = new char[ROW][COL];
		Reflection(map, fileName);
	}

	/**
	 * �}�b�v�Ƀ}�b�v�t�@�C���𔽉f������B
	 * @param map
	 * @param fileName
	 */
	public void Reflection(char map[][], String fileName){
		String line;//�t�@�C���ǂݍ��ݗp
		try{
			BufferedReader br = new BufferedReader(new InputStreamReader(
					getClass().getResourceAsStream("map/" + fileName)));
			//�}�b�v���쐬

			for(int i = 0; i < ROW; i++){
				line = br.readLine(); //1�s�ǂݎ��
				for(int j = 0; j < COL; j++){
					map[i][j] = line.charAt(j);
					switch (map[i][j]) {
					/* case 'o':
						sprites.add(new Coin(tilesToPixels(j), tilesToPixels(i), "coin.gif", this));
						break;
						//���̐�`�悷��u���b�N�̎�ނ�������Ȃ�ǉ�
					 */
					}
				}
			}
		} catch (Exception e){
			e.printStackTrace();
		}
	}
	
	public int GetWidth() {
		return width;
	}


	public int GetHeight() {
		return height;
	}

	/**
	 *���̂�LinkedList��Ԃ�
	 */
	public LinkedList GetSprites() {
		return sprites;
	}
	
	public void Lotation(){
		String str = " "; 
		//�}�b�v�����炷
		for(int i = 0; i < ROW - 1; i++){
			for(int j = 0; j < COL - 1; j++){
				map[i][j] = map[i][j+1];
			}
		}
		//���̃}�b�v��ǂݍ��܂���
		for(int i = 0; i < ROW; i++){
			map[i][COL-1] = next_map[i][0];
		}
		//���ɓǂݍ��܂���}�b�v�����炷
		for(int i = 0; i < ROW - 1; i++){
			for(int j = 0; j < COL - 1; j++){
				next_map[i][j] = next_map[i][j+1];
			}
		}
		//�󂢂��X�y�[�X�𖄂߂�
		
		for(int i = 0; i < ROW; i++){
			next_map[i][COL-1] = str.charAt(0);
		}
		
		//System.out.println(next_map.length);
		
		update_map_num++;
		if(update_map_num == COL){
			//Reflection(next_map, "map" + (rnd.nextInt(MAP_NUM) + 1) + ".dat");
			Reflection(next_map, "map01.dat");
			update_map_num = 0;
		}
	}
}
