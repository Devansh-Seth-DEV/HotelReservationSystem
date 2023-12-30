import java.sql.ResultSet;
import java.util.Scanner;

class TaskHandler {
	public TaskHandler(HotelDAO dao, Scanner stdin, TaskMenu tk_m, char w_sym, char h_sym, char edge) {
		this.dao = dao;
		this.stdin = stdin;
		this.tk_m = tk_m;
		this.w_sym = w_sym;
		this.h_sym = h_sym;
		this.edge = edge;
	}

	public int reserveRoom(int w) {
		Customer cust = new Customer();
		
		tk_m.drawLbox("", w, '\n', this.h_sym);

		cust.name = tk_m.eboxString("Enter guest name: ", w, this.h_sym, stdin);
		
		cust.room_no = tk_m.eboxInt("Enter room number: ", w, this.h_sym, stdin);
		
		cust.phno = tk_m.eboxString("Enter contact number: ", w, this.h_sym, stdin);

		tk_m.drawLbox("", w, '\n', this.h_sym);
		tk_m.drawBorder(this.w_sym, this.edge, w); 
		System.out.println();
		
		if(cust.name == "") {
			System.out.println("INVALID: please enter a valid name!");
			return -1;
		} else if(cust.room_no <= 0 || cust.room_no/1000 != 0) {
			System.out.println("INVALID: please enter valid room number!");
			return -1;
		} else if(cust.phno == "" || cust.phno.length() < 10) {
			System.out.println("INVALID: please enter a valid contact number!");
			return -1;
		}

		int aff_r = dao.newRecord(cust);
	   	if(aff_r > 0) {
		   System.out.println("SUCCESS: room successfully reserved!");
		} else {
		   System.out.println("FAILED: failed to reserve room"); 
	    }

		return aff_r;
	}

	public void viewReservations() {
		dao.getRecords();
	}

	public int getRoomNumber(int w) {
		int id, room_no;
		String name;

		tk_m.drawLbox("", w, '\n', this.h_sym);
		
		id = tk_m.eboxInt("Enter reservation id: ", w, this.h_sym, stdin);
		
		name = tk_m.eboxString("Enter guest name: ", w, this.h_sym, stdin);
	
		tk_m.drawLbox("", w, '\n', this.h_sym);
		tk_m.drawBorder(this.w_sym, this.edge, w); 
		System.out.println();

		if(id == -2) {
			System.out.println("INVALID: please enter a valid id!");
			return -1;
		} else if(name == "") {
			System.out.println("INVALID: please enter a valid name!");
			return -1;
		}

		room_no = dao.getRoomRecord(id, name);
		if(room_no > 0) {
			System.out.printf("Room Number: R-%03d\n", room_no);
			return 0;
		} else {
			System.out.println("NOT FOUND: no room is reserved.");
			return 1;
		}
	}

	public int updateReservation(int w) {
		Customer cust = new Customer();
		int id;

		tk_m.drawLbox("", w, '\n', this.h_sym);
		
		id = tk_m.eboxInt("Enter reservation id to update: ", w, this.h_sym, stdin);
		if(id == -2) {
			tk_m.drawLbox("", w, '\n', this.h_sym);
			tk_m.drawBorder(this.w_sym, this.edge, w); 
			System.out.println("\nINVALID: please enter a valid id!");
			return -1;
		}

		try {
			if(!dao.isRecordExists(id)) {
				tk_m.drawLbox("", w, '\n', this.h_sym);
				tk_m.drawBorder(this.w_sym, this.edge, w); 
				System.out.println("\nNOT FOUND: no reservation found for given id!");
				return -1;
			}
		} catch (Exception err) {
			tk_m.drawLbox("", w, '\n', this.h_sym);
			tk_m.drawBorder(this.w_sym, this.edge, w); 
			System.out.println();
			System.out.println(err.getMessage() + "\nERROR: while searching for reservation id!");
			return -1;
		}


		String msg = "NEW DETAILS:";
		tk_m.drawLbox("", w, '\n', this.h_sym);
		tk_m.drawLbox(msg, w-msg.length(), '\n', this.h_sym);
		cust.name = tk_m.eboxString("    Enter new guest name: ", w, this.h_sym, stdin);
		
		cust.room_no = tk_m.eboxInt("    Enter new room number: ", w, this.h_sym, stdin);

		cust.phno = tk_m.eboxString("    Enter new contact number: ", w, this.h_sym, stdin);


		tk_m.drawLbox("", w, '\n', this.h_sym);
		tk_m.drawBorder(this.w_sym, this.edge, w); 
		System.out.println();

		if(cust.name == "") {
			System.out.println("INVALID: please enter a valid name!");
			return -1;
		} else if(cust.room_no <= 0 || cust.room_no/1000 != 0) {
			System.out.println("INVALID: please enter a valid room number!");
			return -1;
		} else if(cust.phno == "" || cust.phno.length() < 10) {
			System.out.println("INVALID: please enter a valid contact number!");
			return -1;
		}

		int aff_r = dao.updateRecord(id, cust);
		if(aff_r > 0) {
			System.out.println("SUCCESS: guest details updated successfully!");
		} else {
			System.out.println("FAILED: failed to update guest details!"); 
		}

		return aff_r;
	}

	public int deleteReservation(int w) {
		int id;

		tk_m.drawLbox("", w, '\n', this.h_sym);

		id = tk_m.eboxInt("Enter reservation id to delete: ", w, this.h_sym, stdin);
		if(id == -2) {
			tk_m.drawLbox("", w, '\n', this.h_sym);
			tk_m.drawBorder(this.w_sym, this.edge, w); 
			System.out.println("\nINVALID: please enter a valid id!");
			return -1;
		}

		try {
			if(!dao.isRecordExists(id)) {
				tk_m.drawLbox("", w, '\n', this.h_sym);
				tk_m.drawBorder(this.w_sym, this.edge, w); 
				System.out.println("\nNOT FOUND: no reservation found for given id!");
				return -1;
			}
		} catch (Exception err) {
			tk_m.drawLbox("", w, '\n', this.h_sym);
			tk_m.drawBorder(this.w_sym, this.edge, w); 
			System.out.println();
			System.out.println(err.getMessage() + "\nERROR: while searching for reservation id!");
			return -1;
		}

		tk_m.drawLbox("", w, '\n', this.h_sym);
		tk_m.drawBorder(this.w_sym, this.edge, w); 
		System.out.println();

		int aff_r = dao.deleteRecord(id);
		if(aff_r > 0) {
			System.out.println("SUCCESS: reservation deleted successfully!");
		} else {
			System.out.println("FAILED: failed to delete reservation!"); 
		}

		return aff_r;
	}

	public void exit()
		throws InterruptedException
	{
		System.out.print("Exiting System");
		int i=5;
		while(i!=0) {
			System.out.print(".");
			Thread.sleep(450);
			i--;
		}

		System.out.println();
		System.out.println("ThankYou for Using Hotel Reservation System!!!");
	}

	private static Scanner stdin;
	private char w_sym, h_sym, edge;
	private HotelDAO dao;
	private TaskMenu tk_m;
}
