import java.io.*;
import java.lang.Math;

public class MakeMap {
	static int map_wid = 60;	//マップの横幅
	static int map_hei = 20;	//マップの縦幅
	
	static char[][] cell = null;
	
	//受け取ったファイル名のマップファイルを作成する
	public static void makemap(String mapname) {
		File map = new File(mapname);
		PrintWriter pw = null;
		try {
			pw = new PrintWriter(new BufferedWriter(new FileWriter(map)));
		} catch (IOException e){
			System.err.println("ファイルオープンエラー");
		}
		
		//セルの初期化
		init_cell();
		
		//ブロック、キャラ、コインを置く
		set_obj();
		
		//マップファイルにセルの内容を書き込む
		write_map(pw);
	
		pw.close();
	}
	
	//セルの初期化
	public static void init_cell() {
		int i, j;
		cell = new char[map_hei][map_wid];
		
		for (j=0; j<map_wid; j++) {
			cell[0][j] = 'B';
			cell[map_hei-1][j] = 'B';
		}
		for (i=1; i<map_hei-1; i++){
			for (j=0; j<map_wid; j++) {
				cell[i][j] = ' ';
			}
		}
	}
	
	//セルにブロックとキャラを置く
	public static void set_obj() {
		set_block();	//ブロックを設置する
		set_charc();	//キャラクターを設置する
		set_coin();		//コインを設置する
	}
	
	//マップファイルにセルの内容を書き込む
	public static void write_map(PrintWriter pw) {
		int i, j;
		
		for (i=0; i<map_hei; i++) {
			for (j=0; j<map_wid; j++) {
				pw.print(cell[i][j]);
			}
			pw.print('\n');
		}
	}
	
	//ブロックを設置する
	public static void set_block() {
		int i, j;
		int put_block = 0;	//置いたブロックの個数
		int block_num;	//ブロックの種類
		
		//置いたブロックの個数が10になるまで置き続ける
		while (put_block < 10) {
			//ブロックを置く位置を決める
			i = (int)(Math.random()*12) + 7;
			j = (int)(Math.random()*map_wid);
			
			//ブロックを置く種類を決める
			block_num = (int)(Math.random()*5);
			
			//ブロックが置けるかの判定をしてブロックを置く
			if (block_judge_make(i, j, block_num)) {
				put_block++;
			}

		}
	}
	
	//キャラクターを設置する
	public static void set_charc() {
		int i, j;
		boolean b;
		
		for (i=6; i<map_hei-1; i++) {
			for (j=0; j<map_wid; j++) {
				b = ((int)(Math.random()*15) == 1);	//===この値を調整してキャラの出現量を決める
				b = b && cell[i][j] == ' ';
				b = b && cell[i+1][j] == 'B';
				if (b) {
					switch ((int)(Math.random()*4)) {
					case 0:
						cell[i][j] = 'c'; break;	//猫を置く
					case 1:
						cell[i][j] = 'd'; break;	//犬を置く
					case 2:
						cell[i][j] = 's'; break;	//スライムを置く
					default:
						cell[i][j] = 'j';			//爺さんを置く
					}
				}
			}
		}
	}

	//コインを設置する
	public static void set_coin() {
		int i, j;
		int put_coin = 0;	//置いたコインの個数
		
		//置いたコインの個数が5になるまで置き続ける
		while (put_coin < 5) {
			//コインを置く位置を決める
			i = (int)(Math.random()*16) + 3;
			j = (int)(Math.random()*map_wid);
			
			//ブロックが置けるかの判定をしてブロックを置く
			if (coin_judge_make(i, j)) {
				put_coin++;
			}
		}
	}
	
	//ブロックが置けるかの判定をしてブロックを置く
	public static boolean block_judge_make(int i, int j, int block_num) {
		boolean b = false;
		//ブロックが置けるかの判定
		switch (block_num) {
		case 0:		//正方形
			if (j < map_wid-1) {
				b = cell[i][j]==' ';
				b = b && cell[i][j+1]==' ';
				b = b && cell[i+1][j]==' ';
				b = b && cell[i+1][j+1]==' ';
			}
			break;
		case 1:		//3横
			if (j < map_wid-2) {
				b = cell[i][j]==' ';
				b = b && cell[i][j+1]==' ';
				b = b && cell[i][j+2]==' ';
			}
			break;
		case 2:		//5横
			if (j < map_wid-4) {
				b = cell[i][j]==' ';
				b = b && cell[i][j+1]==' ';
				b = b && cell[i][j+2]==' ';
				b = b && cell[i][j+3]==' ';
				b = b && cell[i][j+4]==' ';
			}
			break;
		case 3:		//7横
			if (j < map_wid-6) {
				b = cell[i][j]==' ';
				b = b && cell[i][j+1]==' ';
				b = b && cell[i][j+2]==' ';
				b = b && cell[i][j+3]==' ';
				b = b && cell[i][j+4]==' ';
				b = b && cell[i][j+5]==' ';
				b = b && cell[i][j+6]==' ';
			}
			break;
		default:	//3縦
			if (i < map_hei-2) {
				b = cell[i][j]==' ';
				b = b && cell[i+1][j]==' ';
				b = b && cell[i+2][j]==' ';
			}
			break;
		}
		
		//ブロックを置く
		if (b) {
			switch (block_num) {
			case 0:		//正方形
				cell[i][j] = 'B';
				cell[i][j+1] = 'B';
				cell[i+1][j] = 'B';
				cell[i+1][j+1] = 'B';
				break;
			case 1:		//3横
				cell[i][j] = 'B';
				cell[i][j+1] = 'B';
				cell[i][j+2] = 'B';
				break;
			case 2:		//5横
				cell[i][j] = 'B';
				cell[i][j+1] = 'B';
				cell[i][j+2] = 'B';
				cell[i][j+3] = 'B';
				cell[i][j+4] = 'B';
				break;
			case 3:		//7横
				cell[i][j] = 'B';
				cell[i][j+1] = 'B';
				cell[i][j+2] = 'B';
				cell[i][j+3] = 'B';
				cell[i][j+4] = 'B';
				cell[i][j+5] = 'B';
				cell[i][j+6] = 'B';
				break;
			default:	//3縦
				cell[i][j] = 'B';
				cell[i+1][j] = 'B';
				cell[i+2][j] = 'B';
			}
		}
		
		return b;
	}

	//コインが置けるかの判定をしてコインを置く
	public static boolean coin_judge_make(int i, int j) {
		boolean b = false;
		
		if (j < map_wid-2) {
			b = cell[i][j]==' ';
			b = b && cell[i][j+1]==' ';
			b = b && cell[i][j+2]==' ';
			
			if (b) {
				cell[i][j] = 'o';
				cell[i][j+1] = 'o';
				cell[i][j+2] = 'o';
			}
		}
		
		return b;
	}
}
