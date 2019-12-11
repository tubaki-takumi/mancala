//参考サイトhttps://gist.github.com/raicos/ea2d9da1ff7453e572a00325eed8db71
package boardgame;

import java.util.Scanner;
import java.util.Random;

public class mancala {
	static int nextmove;

	static void display(int x[]) {

		System.out.print("    ");
		for(int i = 10; i > 5;i--) {
			System.out.print(i + " ");
			if(i < 10) {
				System.out.print(" ");
			}
		}
		System.out.println();

		for(int i = 11; i > 5;i--) {
			System.out.print("|" + x[i]);
			if(x[i] < 10) {
				System.out.print(" ");
			}
		}
		System.out.println("|");

		for(int i = 0; i < 6;i++) {
			System.out.print("|" + x[i]);
			if(x[i] < 10) {
				System.out.print(" ");
			}
		}
		System.out.println("|");

		System.out.print(" ");
		for(int i = 0; i < 5;i++) {
			System.out.print(i + "  ");
		}
		System.out.println();
		System.out.println();
	}

	static void reset(int x[]) {
		for (int i = 0; i < 5; i++) {
			x[i] = 3;
		}
		for (int i = 6; i < 11; i++) {
			x[i] = 3;
		}
		x[5] = 0;
		x[11] = 0;
	}

	static int finish(int x[]) {
		int a, b;
		a = 0;
		b = 0;
		for (int i = 0; i < 5; i++) {
			a = a +	x[i];
		}
		for (int i = 6; i < 11; i++) {
			b = b + x[i] ;
		}
		if (a == 0) {
			return 0;
		}
		else if(b == 0) {
			return 1;
		}
		else {
			return 2;
		}
	}

	static int evaluation(int x[]) {
		int a,b;
		a = 0;
		b = 0;
		for (int i = 0; i < 5; i++) {
			a = a +	x[i];
		}
		for (int i = 6; i < 11; i++) {
			b = b + x[i] ;
		}
		return b - a;
	}

	static boolean continuous_check(int x[], int y, boolean sengo) {
		int a = x[y];
		a = a + y;
		if(a > 11) {
			a = a - 12;
		}
		if(sengo) {
			if (a == 5) {
				return true;
			}
			else {
				return false;
			}
		}
		else {
			if (a == 11) {
				return true;
			}
			else {
				return false;
			}
		}
	}

	static void sowing(int x[] ,int y) {
		//種まきの処理を書く、配列の中身を１づつ順に移動させる
		int a,b;
		//まくべき種の数
		a = x[y];
		x[y] = 0;
		//まいていく場所の添え字
		b = y + 1;
		while(true) {
			x[b] = x[b] + 1;
			b++;
			if(b == 12) {
				b = 0;
			}
			a--;
			if(a == 0) {
				break;
			}
		}
	}

	static void playermove(int x[], boolean sengo) {

		boolean continuous;
		while(true) {
			try{
				Scanner scan = new Scanner(System.in);
				int input = scan.nextInt();

				if(sengo) {
					if(0 <= input && input <= 4 && x[input] != 0) {
						//種まきの処理,初めに自分の穴にとまるか計算しておく
						continuous = continuous_check(x,input,sengo);
						sowing(x,input);
						display(x);
					}
					else {
						throw new Exception();
					}
				}
				else {
					if (6 <= input && input <= 10 && x[input] != 0) {
						//種まきの処理,初めに自分の穴にとまるか計算しておく
						continuous = continuous_check(x,input,sengo);
						sowing(x,input);
						display(x);
					}
					else { 
						throw new Exception();
					}
				}
				if(!continuous) {
					break;
				}
				int a = finish(x);
				
					if(a != 2) {
						break;
					}
				System.out.println("連荘です。");
			}

			catch(Exception e) {
				System.out.println("もう一度入力してください");
			}
		}
	}

	static void search(int x[],boolean sengo) {

		if(sengo) {
			MaxValue(x,-99999, 99999, 0);
		}
		else {
			MinValue(x, -99999, 99999, 0);
		}
	}

	static int MaxValue(int x[], int a, int b,int depth) {
		int finishcheck = finish(x);
		int v = -99999999;
		int TBDnextmove = 0;
		int t = v;
		if(finishcheck == 0) {
			return 10000;
		}
		else if(finishcheck == 1) {
			return -10000;
		}
		if(depth == 10) {
			return evaluation(x);
		}


		for(int i = 0; i < 5; i++) {
			if(x[i] != 0) {
				int copyx[] = x.clone();
				boolean continuous = continuous_check(x,i,true);
				sowing(copyx,i);
				if(continuous) {
					v = Math.max(v, MaxValue(copyx, a, b, depth+1));
				}
				else {
					v = Math.max(v, MinValue(copyx, a, b, depth+1));
				}
				if(t != v) {
					TBDnextmove = i;
					t = v;
				}
				if(v >= b) {
					nextmove = TBDnextmove;
					return v;
				}
				if(v > a) {
					a = v;
				}
			}
		}
		nextmove = TBDnextmove;
		return v;
	}

	static int MinValue(int x[], int a, int b, int depth) {
		int finishcheck = finish(x);
		int v = 99999999;
		int TBDnextmove = 6;
		int t = v;
		if(finishcheck == 0) {
			return 10000;
		}
		else if(finishcheck == 1) {
			return -10000;
		}

		if(depth == 10) {
			return evaluation(x);
		}

		for(int i = 6; i < 11; i++) {
			if(x[i] != 0) {
				int copyx[] = x.clone();
				boolean continuous = continuous_check(x,i,false);
				sowing(copyx,i);
				if(continuous) {
					v = Math.min(v, MinValue(copyx, a, b, depth+1));
				}
				else {
					v = Math.min(v, MaxValue(copyx, a, b, depth+1));
				}
				if(t != v) {
					TBDnextmove = i;
					t = v;
				}
				if(v <= a) {
					nextmove = TBDnextmove;
					return v;
				}
				if(v < b) {
					b = v;
				}
			}
		}
		nextmove = TBDnextmove;
		return v;
	}

	static void aimove(int x[], boolean sengo) {
		while (true) {
			search(x,sengo);
			boolean continuous = continuous_check(x,nextmove,sengo);
			sowing(x,nextmove);
			display(x);
			if(!continuous) {
				break;
			}
			int a = finish(x);
				if(a != 2) {
					break;
				}
			System.out.println("連荘です。");
		}
	}
	public static void main(String[]args) {

		int board [] = new int [12];
		boolean aifirst;
		int check;
		Random rand = new Random();
		if(rand.nextInt(2) == 0) {
			aifirst = true;
			System.out.println("AIの先手です。");
		}
		else {
			aifirst = false;
			System.out.println("AIの後手です。");
		}
		reset(board);
		System.out.println("初期盤面");
		display(board);

		while(true) {
			if(aifirst) {
				System.out.println("AIの番です。");
				aimove(board,true);
				check = finish(board);
				if(check == 0) {
					System.out.println("First player is winner");
					break;
				}
				System.out.println("プレイヤーの番です。");
				playermove(board,false);
				check = finish(board);
				if(check == 1) {
					System.out.println("Second player is winner");
					break;
				}
			}
			else {
				System.out.println("プレイヤーの番です。");
				playermove(board, true);
				check = finish(board);
				if(check == 0) {
					System.out.println("First player is winner");
					break;
				}
				System.out.println("AIの番です。");
				aimove(board,false);
				check = finish(board);
				if(check == 1) {
					System.out.println("Second player is winner");
					break;
				}
			}
		}		
	}
}
