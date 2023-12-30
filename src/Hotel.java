import java.util.Scanner;
import java.io.IOException;

class Hotel {
	static
	{
		host = "localhost";
		port = "3306";
		uname = "devansh";
		pswd = "devroot";
		os = System.getProperty("os.name");
		clrsc();
	}

	public static void main(String[] argv)
		throws InterruptedException
	{
		HotelDAO h_dao = new HotelDAO(host, port, uname, pswd);
		Scanner stdin = new Scanner(System.in);
		TaskMenu tk_m = new TaskMenu();
		TaskHandler tk_h = new TaskHandler(h_dao, stdin, tk_m, '-', '|', '+');
		boolean run=true;
		int sel = TaskMenu.DEF_SEL;
		int curr_sel = sel;
		int wn_w = 40;
		final int MAX_OP = TaskMenu.MAX_OP;
		final int SLP_MISEC = 1500;
		String temp=null;
		String title;
		int exit_c;

		exit_c = h_dao.connect();
		if(exit_c>0) {
			run = false;
		}
		
		while(run) {
			while(temp!="") {
				tk_m.mainMenu(curr_sel, wn_w, '-', '|', '+');
				System.out.println();
				System.out.print("Press <enter> to run | Enter number to select: ");
				try {
					temp = stdin.nextLine();
					if(temp == "") {
						sel = curr_sel;
						break;
					} else {
						sel = Integer.parseInt(temp);
					}
				} catch (Exception err) {
					sel = -1;
					System.out.println("\nINCORRECT: please enter a valid input!");
					Thread.sleep(SLP_MISEC);
				}
				
				if(sel>0 && sel <= MAX_OP) {
					curr_sel = sel;
				} else if(sel != -1) {
					sel = curr_sel;
					System.out.println("\nINCORRECT: please enter a valid input!");
					Thread.sleep(SLP_MISEC);
				}

				clrsc();
			}
			
			switch(sel) {
				case MAX_OP:
					break;

				case 1:
					clrsc();
					title = "Reserve Room";
					tk_m.drawTbox(title, wn_w+20, '-', '|', '+');

					exit_c = tk_h.reserveRoom(wn_w+20+title.length());
					if(exit_c>0) {
						Thread.sleep(SLP_MISEC);
					} else {
						System.out.println("\nBACK <enter>");
						stdin.nextLine();
		   				TaskMenu.cons_row+=2;
					}

					break;

				case 2:
					clrsc();
					try {
						int w = h_dao.getRecordsWidth()-2;
						title = "Current Reservations";
						tk_m.drawTbox(title, w-title.length(), '-', '|', '+');

						tk_h.viewReservations();
						System.out.println("\nBACK <enter>");
						stdin.nextLine();
					} catch (Exception err) {
						System.out.println(err.getMessage() + "\nERROR: while checing reservations!");
					}
					break;

				case 3:
					clrsc();
					title = "Room Number Info.";
					tk_m.drawTbox(title, wn_w+20, '-', '|', '+');
					
					tk_h.getRoomNumber(wn_w+20+title.length());
					System.out.println("\nBACK <enter>");
					stdin.nextLine();
					break;
				
				case 4:
					clrsc();
					title = "Update Reservation Details";
					tk_m.drawTbox(title, wn_w+10, '-', '|', '+');

					exit_c = tk_h.updateReservation(wn_w+10+title.length());
					if(exit_c == -1 || exit_c == 0) {
						System.out.println("\nBACK <enter>");
						stdin.nextLine();
					} else {
						Thread.sleep(SLP_MISEC);
					}
					break;
					
				case 5:
					clrsc();
					title = "Delete Reservation";
					tk_m.drawTbox(title, wn_w, '-', '|', '+');

					exit_c = tk_h.deleteReservation(wn_w+title.length());
					if(exit_c == -1 || exit_c == 0) {
						System.out.println("\nBACK <enter>");
						stdin.nextLine();
					} else {
						Thread.sleep(SLP_MISEC);
					}
					break;
			}
			
			if(sel == MAX_OP) {
				run = false;
			} else {
				clrsc();
				temp = (curr_sel == -1) ? "" : null;
				curr_sel = sel;
			}
		}


		try {
			h_dao.disconnect();
			System.out.println();
			tk_h.exit();
			stdin.close();
		} catch (InterruptedException err) {
			h_dao.disconnect();
			System.out.println(err.getMessage());
			stdin.close();
		}
	}

	private static void clrsc() {
		try {
			if(os.contains("Windows")) {
				new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
			} else {
				//Runtime.getRuntime().exec("clear");
				System.out.print("\033[H\033[2J");
				System.out.flush();
				TaskMenu.cons_row = 1;
			}
		} catch (Exception err) {
			System.out.println("ERROR: Unable to clear screen");
		}
	}

	private static String host;
	private static String port;
	private static String uname;
	private static String pswd;
	private static String os;
}
