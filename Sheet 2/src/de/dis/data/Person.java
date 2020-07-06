package de.dis.data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Person {
	private int id = -1;
	private String firstname;
	private String name;
	private String address;
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getFirstname() {
		return firstname;
	}
	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	
	public static Person loadPerName(String firstname, String name) {
		try {
			// Hole Verbindung
			Connection con = DbConnectionManager.getInstance().getConnection();

			// Erzeuge Anfrage
			String selectSQL = "SELECT * FROM person WHERE firstname = ? AND name = ?";
			PreparedStatement pstmt = con.prepareStatement(selectSQL);
			pstmt.setString(1, firstname);
			pstmt.setString(2, name);


			// Führe Anfrage aus
			ResultSet rs = pstmt.executeQuery();
			if (rs.next()) {
				Person ps = new Person();
				ps.setId(rs.getInt("pnr"));
				ps.setFirstname(rs.getString("firstname"));
				ps.setName(rs.getString("name"));
				ps.setAddress(rs.getString("address"));
				
				rs.close();
				pstmt.close();
				return ps;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static Person load(int id) {
		try {
			// Hole Verbindung
			Connection con = DbConnectionManager.getInstance().getConnection();

			// Erzeuge Anfrage
			String selectSQL = "SELECT * FROM person WHERE pnr = ?";
			PreparedStatement pstmt = con.prepareStatement(selectSQL);
			pstmt.setInt(1, id);


			// Führe Anfrage aus
			ResultSet rs = pstmt.executeQuery();
			if (rs.next()) {
				Person ps = new Person();
				ps.setId(id);
				ps.setFirstname(rs.getString("firstname"));
				ps.setName(rs.getString("name"));
				ps.setAddress(rs.getString("address"));
				
				rs.close();
				pstmt.close();
				return ps;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public void save() {
		// Hole Verbindung
		Connection con = DbConnectionManager.getInstance().getConnection();

		try {
			// Füge neues Element hinzu, wenn das Objekt noch keine ID hat.
			if (getId() == -1) {

				// zunächst die höchste id in der Table finden
				String selectSQL = "SELECT * FROM person ORDER BY pnr DESC LIMIT 1";

				PreparedStatement pstmtPre = con.prepareStatement(selectSQL);
				ResultSet rs = pstmtPre.executeQuery();
				// id um 1 erhöhen und als id dieser immobilie festlegen
				if (rs.next()) {
					setId(rs.getInt("pnr") + 1);
				}
				else {
					setId(1);
				}

				rs.close();

				String insertSQL = "INSERT INTO person (pnr, firstname, name, address) VALUES(?, ?, ?, ?)";

				PreparedStatement pstmt = con.prepareStatement(insertSQL);

				// Setze Anfrageparameter und führe Anfrage aus
				pstmt.setInt(1, getId());
				pstmt.setString(2, getFirstname());
				pstmt.setString(3, getName());
				pstmt.setString(4, getAddress());

				pstmt.executeUpdate();

				pstmt.close();
			} else {
				// Falls schon eine ID vorhanden ist, mache ein Update...
				String updateSQL = "UPDATE estate SET firstname = ?, name = ?, address = ? WHERE pnr = ?";
				PreparedStatement pstmt = con.prepareStatement(updateSQL);

				// Setze Anfrageparameter und führe Anfrage aus
				pstmt.setString(1, getFirstname());
				pstmt.setString(2, getName());
				pstmt.setString(3, getAddress());
				pstmt.setInt(4, getId());

				pstmt.executeUpdate();

				pstmt.close();
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
