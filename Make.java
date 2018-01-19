import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.ArrayList;

class Make{
  public static void makenewgame(){
    for(int i = 1; i < 16; i++){
      Root.record[i][0] = -1;
      Root.record[i][16] = -1;
      for(int j = 1; j < 16; j++){
        Root.record[i][j] = 0;
      }
    }
    Root.coordx.clear();
    Root.coordy.clear();
    Root.crr = 0;
    Root.isCheat = false;
    Root.win = false;
    for(int i = 0; i < 3; i++) ChkFunc.isCheatset[i] = false;
  }

  public static void makeBoard(Graphics g) {
    final int POINT_11X = 60;
    final int POINT_11Y = 60;
    final int ISHI_SIZE = 30;
    final int CENTER_X = POINT_11X+ISHI_SIZE*7;
    final int CENTER_Y = POINT_11Y+ISHI_SIZE*7;
    g.setColor(Color.BLACK);
    g.fillOval(CENTER_X - 3*ISHI_SIZE -3, CENTER_Y - 3*ISHI_SIZE -3, 6, 6);
    g.fillOval(CENTER_X - 3*ISHI_SIZE -3, CENTER_Y + 3*ISHI_SIZE -3, 6, 6);
    g.fillOval(CENTER_X + 3*ISHI_SIZE -3, CENTER_Y - 3*ISHI_SIZE -3, 6, 6);
    g.fillOval(CENTER_X + 3*ISHI_SIZE -3, CENTER_Y + 3*ISHI_SIZE -3, 6, 6);
    g.fillOval(CENTER_X -3, CENTER_Y -3, 6, 6);
    for (int lcnt = 1; lcnt <= 15; lcnt++) {
      g.drawLine(POINT_11X, POINT_11Y+(lcnt-1)*ISHI_SIZE, 15*ISHI_SIZE, POINT_11Y+(lcnt-1)*ISHI_SIZE);
      g.drawLine(POINT_11X+(lcnt-1)+ISHI_SIZE, POINT_11Y, POINT_11X+(lcnt-1)*ISHI_SIZE, 15*ISHI_SIZE);
    }
  }
  public int[][] makereachset(int[][] r) {
		int vx = 0,vy = 0;
		int k, count, l = 0, len;
		int tx, ty;
		int[][] search = new int[Root.flag[0].length][11];
		int sea = r[Root.x][Root.y];
		final int SEALEN = (search[0].length+1)/2;

		if(sea > 0) {
			for(k = 0; k < 4 ; k++){
				vx = Root.flag[0][k];
				vy = Root.flag[1][k];
				tx = Root.x - SEALEN*vx;
				ty = Root.y - SEALEN*vy;
				Root.bs[k] = SEALEN - 1;
				for(count = 0 ;count < search[k].length; count++) search[k][count] = -1;
				for(count = 0 ;count < Root.bs[k]; count++){
					if((tx+vx)>0 && (tx+vx)<16 && (ty+vy)>0 && (ty+vy)<16) {
						Root.bs[k] = count;
						break;
					}
					vx += Root.flag[0][k];
					vy += Root.flag[1][k];
				}
				Root.bf[k] = search[k].length;
				for(count = Root.bs[k] ;count < search[k].length; count++){
					if((tx+vx)<=0||(tx+vx)>=16||(ty+vy)<=0||(ty+vy)>=16) {
						Root.bf[k] = count;
						break;
					}
					search[k][count] = r[tx+vx][ty+vy];
					vx += Root.flag[0][k];
					vy += Root.flag[1][k];
				}
				/* display reachset[k].
				for(count = 0;count < search[k].length; count++){
					if(search[k][count] == sea) System.out.print("* ");
					else if(search[k][count] > 0) System.out.print("- ");
					else if(search[k][count] == 0) System.out.print("_ ");
					else System.out.print("x ");
				}
				System.out.print("\n");
				*/
			}
		}
		return search;
	}

  public void kifu() {
    System.out.print("   ");
    for(int j = 1; j < 16; j++){
      if(j < 10) System.out.print("0"+j+" ");
      else System.out.print(j+" ");
    }
    System.out.println("   ");
    for(int i = 1; i < 17 ; i++) {
      if(i < 10) System.out.print("0"+i+" ");
      else if(i < 16) System.out.print(i+" ");
      else System.out.print("   ");
      for (int j = 1; j < 17; j++) {
        if(Root.record[j][i] >= 0) System.out.print(" "+ Root.record[j][i] + " ");
        else if(j ==16 && i == 16) System.out.print("   ");
        else System.out.print(" * ");
      }
      System.out.print("\n");
    }

  }

}
