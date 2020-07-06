package de.dis.data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Immobilie {
	private int id = -1;
	private String city;
	private int postalcode;
	private String street;
	private int streetnumber;
	private float squarearea;
	private int manages;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public int getPostalcode() {
		return postalcode;
	}

	public void setPostalcode(int postalcode) {
		this.postalcode = postalcode;
	}

	public String getStreet() {
		return street;
	}

	public void setStreet(String street) {
		this.street = street;
	}

	public int getStreetnumber() {
		return streetnumber;
	}

	public void setStreetnumber(int streetnumber) {
		this.streetnumber = streetnumber;
	}

	public float getSquarearea() {
		return squarearea;
	}

	public void setSquarearea(float squarearea) {
		this.squarearea = squarearea;
	}

	public int getManages() {
		return manages;
	}

	public void setManages(int manages) {
		this.manages = manages;
	}
	
	public static void delete(int id) {
		// Hole Verbindung
		Connection con = DbConnectionManager.getInstance().getConnection();
		try {
			String updateSQL = "DELETE FROM estate WHERE id = ?";
			PreparedStatement pstmt = con.prepareStatement(updateSQL);

			// Setze Anfrageparameter und führe Anfrage aus
			pstmt.setInt(1, id);

			pstmt.executeUpdate();

			pstmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static Immobilie load(int id) {
		try {
			// Hole Verbindung
			Connection con = DbConnectionManager.getInstance().getConnection();

			// Erzeuge Anfrage
			String selectSQL = "SELECT * FROM estate WHERE id = ?";
			PreparedStatement pstmt = con.prepareStatement(selectSQL);
			pstmt.setInt(1, id);

			// Führe Anfrage aus
			ResultSet rs = pstmt.executeQuery();
			if (rs.next()) {
				Immobilie Im = new Immobilie();
				Im.setId(id);
				Im.setCity(rs.getString("city"));
				Im.setPostalcode(rs.getInt("postalcode"));
				Im.setStreet(rs.getString("street"));
				Im.setStreetnumber(rs.getInt("streetnumber"));
				Im.setSquarearea(rs.getFloat("squarearea"));
				Im.setManages(rs.getInt("manages"));
				rs.close();
				pstmt.close();
				return Im;
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
				String selectSQL = "SELECT * FROM estate ORDER BY id DESC LIMIT 1";

				PreparedStatement pstmtPre = con.prepareStatement(selectSQL);
				ResultSet rs = pstmtPre.executeQuery();
				// id um 1 erhöhen und als id dieser immobilie festlegen
				if (rs.next()) {
					setId(rs.getInt("id") + 1);
				}
				else {
					setId(1);
				}

				rs.close();

				String insertSQL = "INSERT INTO estate (id, city, postalcode, street, streetnumber, squarearea, manages) VALUES(?, ?, ?, ?, ?, ?, ?)";

				PreparedStatement pstmt = con.prepareStatement(insertSQL);

				// Setze Anfrageparameter und führe Anfrage aus
				pstmt.setInt(1, getId());
				pstmt.setString(2, getCity());
				pstmt.setInt(3, getPostalcode());
				pstmt.setString(4, getStreet());
				pstmt.setInt(5, getStreetnumber());
				pstmt.setFloat(6, getSquarearea());
				pstmt.setInt(7, getManages());

				pstmt.executeUpdate();

				pstmt.close();
			} else {
				// Falls schon eine ID vorhanden ist, mache ein Update...
				String updateSQL = "UPDATE estate SET city = ?, postalcode = ?, street = ?, streetnumber = ? , squarearea = ?, manages = ? WHERE id = ?";
				PreparedStatement pstmt = con.prepareStatement(updateSQL);

				// Setze Anfrageparameter und führe Anfrage aus
				pstmt.setString(1, getCity());
				pstmt.setInt(2, getPostalcode());
				pstmt.setString(3, getStreet());
				pstmt.setInt(4, getStreetnumber());
				pstmt.setFloat(5, getSquarearea());
				pstmt.setInt(6, getManages());
				pstmt.setInt(7, getId());

				pstmt.executeUpdate();

				pstmt.close();
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}
