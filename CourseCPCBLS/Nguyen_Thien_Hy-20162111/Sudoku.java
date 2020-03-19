package Baitap;

import java.util.ArrayList;
import java.util.Random;

import localsearch.constraints.alldifferent.AllDifferent;
import localsearch.model.ConstraintSystem;
import localsearch.model.LocalSearchManager;
import localsearch.model.VarIntLS;

public class Sudoku {
	LocalSearchManager mgr;
	VarIntLS[][] X;
	ConstraintSystem S;
	
	public void stateModel() {
		mgr = new LocalSearchManager();
		X = new VarIntLS[9][9];
		for(int i = 0 ; i < 9 ; i ++) {
			for(int j = 0 ; j < 9 ; j ++) {
				X[i][j] = new VarIntLS(mgr , 1 , 9);
				X[i][j].setValue(j + 1);
			}
		}
		S = new ConstraintSystem(mgr);
		for(int i = 0 ; i < 9 ; i ++) {
			VarIntLS[] y = new VarIntLS[9];
			for(int j = 0 ; j < 9 ; j ++) {
				y[j] = X[i][j];
			}
			S.post(new AllDifferent(y));
		}
		for(int i = 0 ; i < 9 ; i ++) {
			VarIntLS[] y = new VarIntLS[9];
			for(int j = 0 ; j < 9 ; j ++) {
				y[j] = X[j][i];
			}
			S.post(new AllDifferent(y));
		}
		for(int I = 0 ; I <= 2 ; I ++) {
			for(int J = 0 ; J <= 2 ; J ++) {
				VarIntLS[] y = new VarIntLS[9];
				int idx = -1;
				for(int i = 0 ; i <= 2 ; i ++) {
					for(int j = 0 ; j <= 2 ; j ++) {
						idx ++;
						y[idx] = X[3 * I + i][3 * J + j];
					}
				}
				S.post(new AllDifferent(y));
			}
		}
		mgr.close();
	}
	public void search() {
		class Move{
			int i;
			int j1;
			int j2;
			public Move(int i , int j1 , int j2) {
				this.i = i;
				this.j1 = j1;
				this.j2 = j2;
			}
		}
		Random R = new Random();
		ArrayList<Move> candidates = new ArrayList<Move>();
		int it = 0;
		while(it <= 100000 && S.violations() > 0) {
			candidates.clear();
			int minDelta = Integer.MAX_VALUE;
			for(int i = 0 ; i < 9 ; i ++) {
				for(int j1 = 0 ; j1 < 8 ; j1 ++) {
					for(int j2 = j1 + 1 ; j2 < 9 ; j2 ++) {
						int delta = S.getSwapDelta(X[i][j1], X[i][j2]);
						if(delta < minDelta) {
							candidates.clear();
							candidates.add(new Move(i , j1 , j2));
							minDelta = delta;
						}
						else if(delta == minDelta) {
							candidates.add(new Move(i , j1 , j2));
						}
					}
				}
				int idx = R.nextInt(candidates.size());
				Move m = candidates.get(idx);
				X[m.i][m.j1].swapValuePropagate(X[m.i][m.j2]);
				it ++;
			}
			System.out.println("Step " + it + ", violations = " + S.violations());
		}
		for(int i = 0 ; i < 9 ; i ++) {
			for(int j = 0 ; j < 9 ; j ++) {
				System.out.print(X[i][j].getValue() + " ");
			}
			System.out.println();
		}
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Sudoku app = new Sudoku();
		app.stateModel();
		app.search();
	}

}
