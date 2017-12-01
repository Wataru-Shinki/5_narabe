import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.ArrayList;
import javax.swing.JFrame;
import javax.swing.JPanel;


public class root extends Frame {
	int x, y, t, c = 0, num;
	int sum = 0,chnum;
	ArrayList<Integer> bx = new ArrayList<Integer>();
	ArrayList<Integer> by = new ArrayList<Integer>();
	int[][] record = new int[17][17];
	boolean IschkFoul, cheat, win;
	boolean[] Ischeat = {false,false,false};
	boolean[] Warncheat = {false,false};
	static int[][] flag = new int[2][4];
	static final double DEG = 45.0;

	public static void main(String[] args) {
		root panel = new root();
    panel.setVisible(true);
		panel.setBackground(new Color(250,150,50));

	}

	public root(){
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
		setTitle("aaa");
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
		bx.clear();
		by.clear();
		c = 0;
	}

	public void dot(Graphics g, int x, int y) {
		g.setColor(Color.BLACK);
		g.fillOval(x-3, y-3, 6, 6);
	}
	public void MakeBoard(Graphics g) {
		final int CENTER = 270, T = 120;
		g.setColor(Color.BLACK);
		dot(g, CENTER-T, CENTER-T);
		dot(g, CENTER-T, CENTER+T);
		dot(g, CENTER+T, CENTER-T);
		dot(g, CENTER+T, CENTER+T);
		dot(g, CENTER, CENTER);
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
			int[] Reachset= new int[4];
			if(cheat || win) {
				makenewgame();
				cheat = false;
				win = false;
				for(int i = 0; i < 3; i++) Ischeat[i] = false;
				System.out.println("\nNEW GAME.\n");
			}

			check(e);
			IschkFoul = (c%2==0);
			String player = (IschkFoul) ? "BLACK" : "WHITE";
			record[x][y] = (IschkFoul) ? 1 : 2;
			record[0][0] = -1;
			Reachset = IsReach(record);
			cheat = Ischeat(Reachset, IschkFoul);
			win = Iswin(Reachset);
			if(cheat) System.out.println("CHEAT!\nWIN: WHITE");
			else if(win) {
				System.out.println("WIN: " + player);
			}
			for(int i = 0; i < 17 ; i++) {
				for (int j = 0; j < 17; j++){
					System.out.print(record[j][i]);
				}
				System.out.print("\n");
			}

		}

		public boolean Ischeat(int[] Rset, boolean Icf) {
			int t;
			if(Icf){
				for(int i = 0; i < 4; i++) {
					if(Rset[i] == 3 || Rset[i] == 4) {
						t = (Rset[i] == 3) ? 0 : 1;
						if(Warncheat[t]) {
							Ischeat[t] = true;
							return true;
						}
						else Warncheat[t] = true;
					}
					else if(Rset[i] >= 6) {
						Ischeat[2] = true;
						return true;
					}
				}
				for(int i = 0; i < 2; i++) Warncheat[i] = false;
			}
			return false;
		}

		public boolean Iswin(int[] Rset) {
			for(int i = 0; i < 4; i++) {
				if(Rset[i] >= 5) return true;
			}
			return false;
		}

	 	public void mouseReleased(MouseEvent e){
		}

	}

	public void check(MouseEvent e){
		x = (e.getX()-15)/30;
		y = (e.getY()-15)/30;
		if (c==0) {
			x = 8; y = 8;
		}
		else if(x > 0 && x < 16 && y > 0 && y < 16) {
			if (record[x][y] > 0) Insert();
			else if (c==1 && (x < 7 || x > 9 || y < 7 || y > 9 )) Insert();
		}
		else Insert();
		if(x != 0) {
			repaint();
			bx.add(x * 30);
			by.add(y * 30);
			c++;
		}
	}
	void Insert() {
		x = 0; y = 0;
	}

	public void paint(Graphics g) {
		MakeBoard(g);
		for (num = 0; num <bx.size(); num++) {
				IschkFoul = (num % 2 == 0);
				g.setColor((IschkFoul) ? Color.BLACK : Color.WHITE);
				g.fillOval(bx.get(num)+15, by.get(num)+15, 30, 30);
		}
	}

	public int[] IsReach(int[][] r) {
		int vx = 0,vy = 0;
		int k, count, bs, bf, l = 0, len;
		int tx, ty;
		int[] search = new int[11];
		int sea = r[x][y];
		final int SEALEN = (search.length+1)/2;
		int[] reacharray = new int[4];

		if(sea > 0) {
			for(k = 0; k < 4 ; k++){
				vx = flag[0][k];
				vy = flag[1][k];
				tx = x - SEALEN*vx;
				ty = y - SEALEN*vy;
				bs = SEALEN - 1;
				for(count = 0 ;count < search.length; count++) search[count] = -1;
				for(count = 0 ;count < bs; count++){
					if((tx+vx)>0 && (tx+vx)<16 && (ty+vy)>0 && (ty+vy)<16) {
						bs = count;
						break;
					}
					vx += flag[0][k];
					vy += flag[1][k];
				}
				bf = search.length;
				for(count = bs ;count < search.length; count++){
					if((tx+vx)<=0||(tx+vx)>=16||(ty+vy)<=0||(ty+vy)>=16) {
						bf = count;
						break;
					}
					search[count] = r[tx+vx][ty+vy];
					vx += flag[0][k];
					vy += flag[1][k];
				}
				reacharray[k] = IsLineReach(search, bs, bf);
				for(count = 0;count < search.length; count++){
					if(search[count] == sea) System.out.print("* ");
					else if(search[count] > 0) System.out.print("- ");
					else if(search[count] == 0) System.out.print("_ ");
					else System.out.print("x ");
				}
				System.out.print(" : " + reacharray[k]);
				System.out.print("\n");
			}
		}
		return reacharray;
	}

		public int IsLineReach(int[] r,int s, int f){
			int ss = r[(r.length - 1)/2];
			int i, j, c = 1, m = 0;
			int start = (s == 0) ? s+1 : s;
			int fin = (f == r.length) ? f -1: f;
			System.out.print("start: " + start + " : fin : " + fin + "    ");
			for(i = start; i < 6; i++) {
				if(fin - start < 5) break;
				else if(r[i] != ss) continue;
				if(r[i+1] != 0 || r[i+3] != 0) {
					for(j = i + 1; j < fin ;j++) {
						if(r[j] == ss) {
							c++;
							m = j;
						}
						else if(r[j] > 0 || (r[j]==0 && r[j+1]==0)) break;
					}
				}
				if (c >= 3) {
					if (r[i - 1] > 0 && r[m + 1] > 0 && r[i - 1] != ss && r[m + 1] != ss) {
						i += (m - i);
						continue;
					}
					else return c;
				}
			}
			return 0;
		}
}
