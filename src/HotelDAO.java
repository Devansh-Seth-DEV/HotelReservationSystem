import java.sql.*;

class Customer {
	public void setFromRS(ResultSet rs) 
		throws SQLException 
	{		
		id = rs.getInt(1);
		name = rs.getString(2);
		room_no = rs.getInt(3);
		phno = rs.getString(4);
		date = rs.getTimestamp(5).toString();
	}

	public String getTformat(int[] c_Width) {
		// data format in tabular form
		String t_format = 	new String(
							"| %"+(c_Width[0])+"d |"+ 				// Reservation ID
							" %-"+(c_Width[1])+"s |"+				// Guest Name
							" %"+(c_Width[2]-3)+"s"+"%03d |"+		// Room Number
							" %-"+(c_Width[3])+"s |"+				// Contact Number
							" %-"+(c_Width[4])+"s |"				// Reservation Date
							);
																
		// converting the table structure into String format
		String str = String.format(t_format, id, name, "R-",room_no, phno, date);
		return str;
	}

	public int id;
	public String name;
	public int room_no;
	public String phno;
	public String date;
}

class HotelDAO {
	static 
	{
		try {
			// loading the MySQL driver
			Class.forName("com.mysql.cj.jdbc.Driver");
		} catch (ClassNotFoundException err) {
			System.out.println(err + "\nERROR: MySQL driver not found!");
		}
	}

	public HotelDAO(String host, String port, String uname, String pswd) {
		this.url = "jdbc:mysql://"+host+":"+port+"/hotel_db";
		this.uname = uname;
		this.pswd = pswd;
		this.t_name = "reservations";
	}

	public int connect() {
		try {
			// establishing the connection with the MySQL driver/server
			con = DriverManager.getConnection(url, uname, pswd);
			return 0;
		} catch (SQLException err) {
			System.out.println(err.getMessage() + "\nSERVER ERROR: server not reachable!");
			return 1;
		}
	}

	public int disconnect() {
		try {
			con.close();  //closing the established connetion
			return 0;
		} catch (SQLException err) {
			System.out.println(err.getMessage() + "\nSERVER ERROR: while disconnecting the established connection!");
			return 1;
		} catch (NullPointerException err) {
			return 2;
		}
	}

	public int newRecord(Customer cust) {
		String query = new String(
						"INSERT INTO "+t_name+"(guest_name, room_number, contact_number) "+
						"VALUES (?, ?, ?)"
						);
		try {
			PreparedStatement pst = con.prepareStatement(query);
			
			pst.setString(1, cust.name);
			pst.setInt(2, cust.room_no);
			pst.setString(3, cust.phno);
			
			int aff_r = pst.executeUpdate(); // returns the no. of rows affected
			pst.close();
			return aff_r;

		} catch (SQLException err) {
			System.out.println(err.getMessage() + "\nINSERTION ERROR: recieves an error while reserving the room!");
		}

		return 0;
	}

	public void getRecords() {
		String query = "SELECT * FROM "+t_name;

		try {
			Statement st = con.createStatement();
			ResultSet rs = st.executeQuery(query);
			this.printTable(rs); // prints the Database data in tabular format
			st.close();
		} catch (SQLException err) {
			System.out.println(err.getMessage() + "\nFETCH ERROR: Unable to fetch reservations!");
		}
	}

	public int getRoomRecord(int id, String name) {
		String query = 	new String(
						"SELECT room_number FROM "+t_name+" "+
						"WHERE reservation_id=? AND guest_name=?"
						);

		try {
			PreparedStatement pst = con.prepareStatement(query);
			pst.setInt(1, id);
			pst.setString(2, name);
			
			ResultSet rs = pst.executeQuery();

			// checking whether the data in result set points to NULL
			if(rs.next()) {
				int room_no = rs.getInt(1);
				pst.close();
				return room_no;
			} else {
				pst.close();
				return 0;
			}
		} catch (SQLException err) {
			System.out.println(err.getMessage() + "\nFETCH ERROR: failed to check room number!");
			return -1;
		}
	}

	public boolean isRecordExists(int id)
		throws SQLException
	{
		String query = 	new String(
				"SELECT reservation_id FROM "+t_name+" "+
				"WHERE reservation_id="+id
				);

		Statement st = con.createStatement();
		ResultSet rs = st.executeQuery(query);
		return rs.next();
	}

	public int updateRecord(int id, Customer cust) {
		String query = new String(
						"UPDATE "+t_name+" "+
						"SET guest_name=?, room_number=?, contact_number=? "+
						"WHERE reservation_id="+id
						);
		
		try {
			PreparedStatement pst = con.prepareStatement(query);
			pst.setString(1, cust.name);
			pst.setInt(2, cust.room_no);
			pst.setString(3, cust.phno);

			int aff_r = pst.executeUpdate();
			pst.close();
			return aff_r;
		} catch (SQLException err) {
			System.out.println(err.getMessage() + "\nUPDATION ERROR: while updating the guest details!");
			return -1;
		}
	}

	public int deleteRecord(int id) {
		String query = new String(
						"DELETE FROM "+t_name+" "+
						"WHERE reservation_id="+id
						);

		try {
			Statement st = con.createStatement();
			
			int aff_r = st.executeUpdate(query);
			st.close();
			return aff_r;

		} catch (SQLException err) {
			System.out.println(err.getMessage() + "\nDELETION ERROR: while removing the reservation!");
		}

		return 0;
	}

	public void printTable(ResultSet rs) 
		throws SQLException 
	{
		String query = "SELECT MAX(LENGTH(guest_name)) FROM "+t_name;
		Statement st=null;
		ResultSet t_rs=null;
		
		try {
			st = con.createStatement();
			t_rs = st.executeQuery(query);
		} catch (SQLException err) {
			System.out.println(err.getMessage() + "\nFETCH ERROR: while checking guest_name's max length");
		}

		// length-offset for each column in the table
		int c_LEN[] = {
								14, 
								(t_rs.next()) ? t_rs.getInt(1): 10,
								11,
								14,
								19
							};

		// top and bottom horizontal border of table
		String border = 	"+"+"-".repeat(c_LEN[0]+2)+			// Reservation ID
							"+"+"-".repeat(c_LEN[1]+2)+			// Guest Name
							"+"+"-".repeat(c_LEN[2]+2)+			// Room Number
							"+"+"-".repeat(c_LEN[3]+2)+			// Contact Number
							"+"+"-".repeat(c_LEN[4]+4)+"+";		// Reservation Date

		st.close();

		System.out.println(border); // heading top border
		System.out.printf("| Reservation ID | %-"+(c_LEN[1])+"s | Room Number | Contact Number | %-"+(c_LEN[4]+2)+"s |\n", "Guest Name", "Reservation Date");
		System.out.println(border); // heading bottom border

		Customer cust = new Customer();
		while(rs.next()) {
			cust.setFromRS(rs); // initialize the member variables using method
			System.out.println(cust.getTformat(c_LEN)); // converts the object data into a tabular format w.r.t length-offset given
		}

		System.out.println(border); // bottom border of the table
	}

	public int getRecordsWidth()
		throws SQLException
	{
		String query = "SELECT MAX(LENGTH(guest_name)) FROM "+t_name;
		Statement st=null;
		ResultSet t_rs=null;
		
		try {
			st = con.createStatement();
			t_rs = st.executeQuery(query);
		} catch (SQLException err) {
			System.out.println(err.getMessage() + "\nFETCH ERROR: while checking guest_name's max length");
		}

		// length-offset for each column in the table
		int c_LEN[] = {
								14, 
								(t_rs.next()) ? t_rs.getInt(1): 10,
								11,
								14,
								19
							};

		// top and bottom horizontal border of table
		String border = 	"+"+"-".repeat(c_LEN[0]+2)+			// Reservation ID
							"+"+"-".repeat(c_LEN[1]+2)+			// Guest Name
							"+"+"-".repeat(c_LEN[2]+2)+			// Room Number
							"+"+"-".repeat(c_LEN[3]+2)+			// Contact Number
							"+"+"-".repeat(c_LEN[4]+4)+"+";		// Reservation Date

		st.close();
		return border.length(); // width of table
	}


	private String url;
	private String uname;
	private String pswd;
	private Connection con;
	private String t_name;
}
