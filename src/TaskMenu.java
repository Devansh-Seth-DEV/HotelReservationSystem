import java.util.Scanner;

class TaskMenu {
	static 
	{
		cons_row = 1; // to get the current row of cursor in console
	}

	public void drawTbox(String title, int w, char w_sym, char h_sym, char edge) {
		if(w<=0) {
			w = 2; // minimum width of title box
		}
		int n = title.length();
		this.drawBorder(w_sym, edge, n+w); // upper border of title in frame
		this.drawLbox(title, w, '\n', h_sym); // title label in frame
		this.drawBorder(w_sym, edge, n+w); // lower border of title in frame
	}

	public void drawBorder(char sym, char edge, int w) {
		// upper and lower border of frame window
		String border = new String(
						"%s"+String.valueOf(sym).repeat(w)+"%s"
						);

		// converting the border into String
		border = String.format(border, edge, edge);
		System.out.println(border);
		cons_row++; // incrementing the row for current position of cursor
	}

	public void drawLbox(String label, int w, char escp, char sym) {
		int n = label.length();
		
		// label element for the frame
		String l_format = new String(
						"%s %-"+(n+w-2)+"s %s"
						);

		// bliting the label into the frame
		System.out.printf(l_format+escp, sym, label, sym);

		if(escp == '\n') {
			cons_row++; // incrementing the row for current position of cursor
		}
	}

	public int eboxInt(String label, int w, char sym, Scanner stdin) {
		int n = label.length();
		char escCode = 0x1B;

		// text box label poisition element for the frame
		String l_format = new String(
							sym+" "+label+
							"%"+(w-n)+"s\n"
							);
		String text;
		int ip;
		int col;

		System.out.printf(l_format, sym);
		col = n+3;

		// bliting the entry box into the frame window
		System.out.printf("%c[%d;%df", escCode, cons_row, col);
		text = stdin.nextLine(); // getting the integer input from the text box
		if(text=="") {
			cons_row++; // incrementing the row for current position of cursor
			return -2;
		}

		try {
			ip = Integer.parseInt(text); // converting the String to integer
		} catch (Exception err) {
			cons_row++;
			return -1;
		}

		cons_row++;
		return ip;
	}

	public String eboxString(String label, int w, char sym, Scanner stdin) {
		int n = label.length();
		char escCode = 0x1B;
		String l_format = new String(
							sym+" "+label+
							"%"+(w-n)+"s\n"
							);
		String text;
		int col;

		System.out.printf(l_format, sym);
		col = n+3;
		System.out.printf("%c[%d;%df", escCode, cons_row, col);

		text = stdin.nextLine();
		cons_row++;

		return text;
	}

	public void mainMenu(int def_sel, int width, char w_sym, char h_sym, char edge) {
		String title = "HOTEL RESERVATION SYSTEM";
		int n = title.length();
		String t_format = String.format("%"+((width+n+2)/4)+"s"+title, "");
		char selsym = '*'; // option select symbol
		String[] options = {
					"Reserve a room",
					"View Reservations",
					"Get Room Number",
					"Update Reservations",
					"Delete Reservations",
					"Exit"
					};

		// title box for main menu frame
		this.drawTbox(t_format, (width+n)-t_format.length(), w_sym, h_sym, edge);
		this.drawLbox("", (n+width), '\n', h_sym); // empty label of width same as frame width
		String label;

		// bliting all the options as a label in frame window
		for(int i=1; i<=options.length; i++) {
			label = "    "+
					((def_sel==i) ? "["+selsym+"]" :"[ ]") +" "+ // checks which option to select
					i+". "+options[i-1];

			this.drawLbox(label, 
					(n+width)-label.length(),
					'\n', h_sym);
		}
		this.drawLbox("", (n+width), '\n', h_sym);
		this.drawBorder(w_sym, edge, n+width); // bottom border of frame window
	}

	public static final int DEF_SEL=1;
	public static final int MAX_OP=6;
	public static int cons_row;
}
