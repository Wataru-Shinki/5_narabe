import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.ArrayList;


public class Root extends Frame {
	static int x, y, crr = 0;
	static int[] bs = new int[4], bf =new int[4];
	int sum = 0,chnum;
	static ArrayList<Integer> coordx = new ArrayList<Integer>();
	static ArrayList<Integer> coordy = new ArrayList<Integer>();
	static int[][] record = new int[17][17];
	static boolean isCheat = false, win = false;
	boolean isBlack;
	static int[][] flag = new int[2][4];
	static final double DEG = 45.0;

	public static void main(String[] args) {
		Root panel = new Root();
    panel.setVisible(true);
		panel.setBackground(new Color(250,150,50));
	}

	public Root(){
		super();
		System.out.println("1 :BLACK, 2 :WHITE");
		for(int k = 0;k < flag[0].length; k++) {
      double tmp = Math.cos(Math.toRadians(DEG * k));
      flag[0][k] = (k == 2) ? 0 : (int)(tmp/Math.abs(tmp));
      tmp = Math.sin(Math.toRadians(DEG * k));
      flag[1][k] = (k == 0) ? (int)tmp : (int)(tmp/Math.abs(tmp));
    }
		for (int i = 0;i < 17; i++) {
			record[0][i] = -1;
			record[16][i] = -1;
		}
		Make.makenewgame();
		setTitle("GOMOKU_NARABE");
		setBackground(new Color(250,150,50));
		addMouseListener(new MAdapter());
		addWindowListener(new WListener());
		setSize(800, 600);

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
			Make m = new Make();
			ChkFunc c = new ChkFunc();
			if(isCheat || win) {
				Make.makenewgame();
				System.out.println("\nNEW GAME.\n");
			}

			c.check(e);
			if(x != 0) repaint();
			isBlack = (crr%2==1);

			String player = (isBlack) ? "BLACK" : "WHITE";
			record[x][y] = (isBlack) ? 1 : 2;
			record[0][0] = -1;
			reachset = m.makereachset(record);
			for(int k = 0; k < flag[0].length; k++) {
				int rnum = ChkFunc.isReach(reachset[k], k);
				if(x == 0) break;
				if(isBlack) isCheat = ChkFunc.isCheat(rnum);
				if(isCheat) {
					System.out.println("CHEAT!\nWIN: WHITE");
					break;
				}
				if(!win) win = (rnum >= 5);
			}
			if(!isCheat && win) System.out.println("WIN: " + player);
			if(isCheat || win) System.out.println("Press the screen to play new game.");
			for(int i = 0; i < 2; i++) ChkFunc.isFirstcheatset[i] = false;
			m.kifu();
		}

	 	public void mouseReleased(MouseEvent e){
		}
	}


	public void paint(Graphics g) {
		Make.makeBoard(g);
		for (int num = 0; num <coordx.size(); num++) {
			boolean isBk = (num%2 == 0);
			g.setColor((isBk) ? Color.BLACK : Color.WHITE);
			g.fillOval(coordx.get(num)+15, coordy.get(num)+15, 30, 30);
		}
	}


}
