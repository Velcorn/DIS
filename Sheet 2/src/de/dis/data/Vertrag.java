package de.dis.data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

public class Vertrag {
	private int id = -1;
	private Date date;
	private String place;
	
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public Date getDate() {
		return date;
	}
	
	public void setDate(Date date) {
		this.date = date;
	}
	
	public String getPlace() {
		return place;
	}
	
	public void setPlace(String place) {
		this.place = place;
	}
	
	/**
	 * Lädt einen Vertrag aus der Datenbank
	 * @param id ID des zu ladenden Vertrags
	 * @return Vertrag-Instanz
	 */
	public static Vertrag load(int id) {
		try {
			// Hole Verbindung
			Connection con = DbConnectionManager.getInstance().getConnection();

			// Erzeuge Anfrage
			String selectSQL = "SELECT * FROM contract WHERE contractnr = ?";
			PreparedStatement pstmt = con.prepareStatement(selectSQL);
			pstmt.setInt(1, id);

			// Führe Anfrage aus
			ResultSet rs = pstmt.executeQuery();
			if (rs.next()) {
				Vertrag vertrag = new Vertrag();
				vertrag.setId(id);
				vertrag.setDate(rs.getDate("date"));
				vertrag.setPlace(rs.getString("place"));

				rs.close();
				pstmt.close();
				return vertrag;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	
	/**
	 * Speichert den Vertrag in der Datenbank. Ist noch keine ID vergeben
	 * worden (id = -1), wird die generierte Id von der DB geholt und dem Model übergeben.
	 */
	public void save() {
		// Hole Verbindung
		Connection con = DbConnectionManager.getInstance().getConnection();

		try {
			// Füge neues Element hinzu, wenn das Objekt noch keine ID hat.
			if (getId() == -1) {
				
				//zunächst die höchste id in der Table finden
				String selectSQL = "SELECT * FROM contract ORDER BY contractnr DESC LIMIT 1";
				
				PreparedStatement pstmtPre = con.prepareStatement(selectSQL);
				ResultSet rs = pstmtPre.executeQuery();
				//id um 1 erhöhen und als id dieses Vertrages festlegen
				if (rs.next()) {
					setId(rs.getInt("contractnr") + 1);
				}
				else {
					setId(1);
				}
				
				rs.close();
				

				String insertSQL = "INSERT INTO contract(contractnr, date, place) VALUES (?, ?, ?)";

				PreparedStatement pstmt = con.prepareStatement(insertSQL);

				// Setze Anfrageparameter und führe Anfrage aus
				pstmt.setInt(1, getId());
				pstmt.setDate(2, (java.sql.Date) getDate());
				pstmt.setString(3, getPlace());
				pstmt.executeUpdate();

				pstmt.close();
			} else {
				// Falls schon eine ID vorhanden ist, mache ein Update...
				String updateSQL = "UPDATE contract SET date = ?, place = ? WHERE contractnr = ?";
				PreparedStatement pstmt = con.prepareStatement(updateSQL);

				// Setze Anfrage Parameter
				pstmt.setDate(1, (java.sql.Date) getDate());
				pstmt.setString(2, getPlace());
				pstmt.setInt(3, getId());
				pstmt.executeUpdate();

				pstmt.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	
	public static void delete(int id) {
		// Hole Verbindung
		Connection con = DbConnectionManager.getInstance().getConnection();
		try {
			String updateSQL = "DELETE FROM contract WHERE contractnr = ?";
			PreparedStatement pstmt = con.prepareStatement(updateSQL);

			// Setze Anfrageparameter und führe Anfrage aus
			pstmt.setInt(1, id);

			pstmt.executeUpdate();

			pstmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	

	
}
