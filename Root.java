import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.ArrayList;


public class Root extends Frame {
	int x, y, crr = 0;
	int[] bs = new int[4], bf =new int[4];
	int sum = 0,chnum;
	ArrayList<Integer> coordx = new ArrayList<Integer>();
	ArrayList<Integer> coordy = new ArrayList<Integer>();
	int[][] record = new int[17][17];
	boolean isBlack, isCheat, win;
	boolean[] isCheatset = {false,false,false};
	boolean[] isFirstcheatset = {false,false};
	static int[][] flag = new int[2][4];
	static final double DEG = 45.0;

	public static void main(String[] args) {
		Root panel = new Root();
    panel.setVisible(true);
		panel.setBackground(new Color(250,150,50));
	}

	public Root(){
		super();
		for(int k = 0;k < flag[0].length; k++) {
			flag[0][k] = (int)(1.8 * Math.cos(Math.toRadians(DEG * k)));
			flag[1][k] = (int)(1.8 * Math.sin(Math.toRadians(DEG * k)));
		}
		for (int i = 0;i < 17; i++) {
			record[0][i] = -1;
			record[16][i] = -1;
		}
		makenewgame();
		setTitle("GOMOKU_NARABE");
		setBackground(new Color(250,150,50));
		addMouseListener(new MAdapter());
		addWindowListener(new WListener());
		setSize(800, 600);

	}

	public void makenewgame(){
		for(int i = 1; i < 16; i++){
			record[i][0] = -1;
			record[i][16] = -1;
			for(int j = 1; j < 16; j++){
				record[i][j] = 0;
			}
		}
		coordx.clear();
		coordy.clear();
		crr = 0;
	}

	public void makeBoard(Graphics g) {
		final int CENTER = 270, T = 120;
		final int ISHI_SIZE = 30;
		g.setColor(Color.BLACK);
		g.fillOval(CENTER - 3*ISHI_SIZE -3, CENTER - 3*ISHI_SIZE -3, 6, 6);
		g.fillOval(CENTER - 3*ISHI_SIZE -3, CENTER + 3*ISHI_SIZE -3, 6, 6);
		g.fillOval(CENTER + 3*ISHI_SIZE -3, CENTER - 3*ISHI_SIZE -3, 6, 6);
		g.fillOval(CENTER + 3*ISHI_SIZE -3, CENTER + 3*ISHI_SIZE -3, 6, 6);
		g.fillOval(CENTER -3, CENTER -3, 6, 6);
		for (int lcnt = 60; lcnt <= 480; lcnt += 30) {
			g.drawLine(60, lcnt, 480, lcnt);
			g.drawLine(lcnt, 60, lcnt, 480);
		}

	}

	class WListener extends WindowAdapter{
		public void windowClosing(WindowEvent e){
         		System.exit(0);
      		}
	}


		/*MouseListener*/

	class MAdapter extends MouseAdapter {
		public void mouseClicked(MouseEvent e){
		}

	 	public void mouseEntered(MouseEvent e){
		}

		public void mouseExited(MouseEvent e){
		}

		public void mousePressed(MouseEvent e){
			int[][] reachset= new int[flag[0].length][11];
			if(isCheat || win) {
				makenewgame();
				isCheat = false;
				win = false;
				for(int i = 0; i < 3; i++) isCheatset[i] = false;
				System.out.println("\nNEW GAME.\n");
			}

			check(e);
			isBlack = (crr%2==1);

			String player = (isBlack) ? "BLACK" : "WHITE";
			record[x][y] = (isBlack) ? 1 : 2;
			record[0][0] = -1;
			reachset = makereachset(record);
			for(int k = 0; k < flag[0].length; k++) {
				int rnum = isReach(reachset[k], k);
				if(x == 0) break;
				if(isBlack) isCheat = ischeat(rnum);
				if(isCheat) {
					System.out.println("CHEAT!\nWIN: WHITE");
					break;
				}
				if(!win) {
					win = (rnum >= 5);
				}
			}
			if(!isCheat && win) System.out.println("WIN: " + player);
			for(int i = 0; i < 2; i++) isFirstcheatset[i] = false;
			for(int i = 0; i < 17 ; i++) {
				for (int j = 0; j < 17; j++) System.out.print(record[j][i]);
				System.out.print("\n");
			}

		}

		public boolean ischeat(int r) {
			int t;
			if(r == 3 || r == 4) {
				t = (r == 3) ? 0 : 1;
				if(isFirstcheatset[t]) {
					isCheatset[t] = true;
					for(int i = 0; i < 2; i++) isFirstcheatset[i] = false;
					return true;
				}
				else isFirstcheatset[t] = true;
			}
			else if(r >= 6) {
				isCheatset[2] = true;
				return true;
			}
			return false;
		}

	 	public void mouseReleased(MouseEvent e){
		}
	}

	public void check(MouseEvent e){
		x = (e.getX()-15)/30;
		y = (e.getY()-15)/30;
		if (crr==0) {
			x = 8; y = 8;
		}
		else if(x > 0 && x < 16 && y > 0 && y < 16) {
			if (record[x][y] > 0) {
 				x = 0; y = 0;
			}
			else if (crr==1 && (x < 7 || x > 9 || y < 7 || y > 9 ))  {
				x = 0; y = 0;
			}
		}
		else {
 			x = 0; y = 0;
		}
		if(x != 0) {
			repaint();
			coordx.add(x * 30);
			coordy.add(y * 30);
			crr++;
		}
	}

	public void paint(Graphics g) {
		makeBoard(g);
		for (int num = 0; num <coordx.size(); num++) {
				isBlack = (num % 2 == 0);
				g.setColor((isBlack) ? Color.BLACK : Color.WHITE);
				g.fillOval(coordx.get(num)+15, coordy.get(num)+15, 30, 30);
		}
	}

	public int[][] makereachset(int[][] r) {
		int vx = 0,vy = 0;
		int k, count, l = 0, len;
		int tx, ty;
		int[][] search = new int[flag[0].length][11];
		int sea = r[x][y];
		final int SEALEN = (search[0].length+1)/2;

		if(sea > 0) {
			for(k = 0; k < 4 ; k++){
				vx = flag[0][k];
				vy = flag[1][k];
				tx = x - SEALEN*vx;
				ty = y - SEALEN*vy;
				bs[k] = SEALEN - 1;
				for(count = 0 ;count < search[k].length; count++) search[k][count] = -1;
				for(count = 0 ;count < bs[k]; count++){
					if((tx+vx)>0 && (tx+vx)<16 && (ty+vy)>0 && (ty+vy)<16) {
						bs[k] = count;
						break;
					}
					vx += flag[0][k];
					vy += flag[1][k];
				}
				bf[k] = search[k].length;
				for(count = bs[k] ;count < search[k].length; count++){
					if((tx+vx)<=0||(tx+vx)>=16||(ty+vy)<=0||(ty+vy)>=16) {
						bf[k] = count;
						break;
					}
					search[k][count] = r[tx+vx][ty+vy];
					vx += flag[0][k];
					vy += flag[1][k];
				}
				for(count = 0;count < search[k].length; count++){
					if(search[k][count] == sea) System.out.print("* ");
					else if(search[k][count] > 0) System.out.print("- ");
					else if(search[k][count] == 0) System.out.print("_ ");
					else System.out.print("x ");
				}
				System.out.print("\n");
			}
		}
		return search;
	}

		public int isReach(int[] r, int kk){
			int ss = r[r.length/2];
			int i, j, rn = 1, m = 0;
			int start = (bs[kk] == 0) ? bs[kk] + 1 : bs[kk];
			int fin = (bf[kk] == r.length) ? bf[kk] - 1: bf[kk];
			for(i = start; i < (r.length + 1)/2; i++) {
				if(fin - start < 5) break;
				else if(r[i] != ss) continue;
				for(j = i + 1; j < fin ;j++) {
					if(r[j] == ss) {
						rn++;
						m = j;
					}
						else if(r[j] > 0 || (r[j]==0 && r[j+1]==0) || (r[i+1] == 0 && r[i+3] != 0) ) break;
				}
				if (rn >= 3) {
					if (r[i - 1] > 0 && r[m + 1] > 0 && r[i - 1] != ss && r[m + 1] != ss) {
						i += (m - i);
						continue;
					}
					else return rn;
				}
			}
			return 0;
		}
}
