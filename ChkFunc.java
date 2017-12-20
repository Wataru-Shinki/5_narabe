import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.ArrayList;

class ChkFunc{
  static boolean[] isCheatset = {false,false,false};
  static boolean[] isFirstcheatset = {false,false};

  public static void check(MouseEvent e){
		Root.x = (e.getX()-15)/30;
		Root.y = (e.getY()-15)/30;
		if (Root.crr==0) {
			Root.x = 8; Root.y = 8;
		}
		else if(Root.x > 0 && Root.x < 16 && Root.y > 0 && Root.y < 16) {
			if (Root.record[Root.x][Root.y] > 0) {
 				Root.x = 0; Root.y = 0;
			}
			else if (Root.crr==1 && (Root.x < 7 || Root.x > 9 || Root.y < 7 || Root.y > 9 ))  {
				Root.x = 0; Root.y = 0;
			}
		}
		else {
 			Root.x = 0; Root.y = 0;
		}
		if(Root.x != 0) {
			Root.coordx.add(Root.x * 30);
			Root.coordy.add(Root.y * 30);
			Root.crr++;
		}
	}

  public static boolean ischeat(int r) {
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

  public static int isReach(int[] r, int kk){
		int ss = r[r.length/2];
		int i, j, rn = 1, m = 0;
		int start = (Root.bs[kk] == 0) ? Root.bs[kk] + 1 : Root.bs[kk];
		int fin = (Root.bf[kk] == r.length) ? Root.bf[kk] - 1: Root.bf[kk];
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
