package de.dis.data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Apartment {
	private int id = -1;
	private int floors;
	private float rent;
	private int rooms;
	private boolean balcony;
	private boolean kitchen;
	private Immobilie immobilie;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getRooms() {
		return rooms;
	}
	public void setRooms(int rooms) {
		this.rooms = rooms;
	}
	public float getRent() {
		return rent;
	}
	public void setRent(float rent) {
		this.rent = rent;
	}
	public int getFloors() {
		return floors;
	}
	public void setFloors(int floors) {
		this.floors = floors;
	}
	public boolean getKitchen() {
		return kitchen;
	}
	public void setKitchen(boolean kitchen) {
		this.kitchen = kitchen;
	}
	public boolean getBalcony() {
		return balcony;
	}
	public void setBalcony(boolean balcony) {
		this.balcony = balcony;
	}
	
	public Immobilie getImmobilie() {
		return immobilie;
	}
	public void setImmobilie(Immobilie immobilie) {
		this.immobilie = immobilie;
	}
	public static Apartment load(int id) {
		try {
			// Hole Verbindung
			Connection con = DbConnectionManager.getInstance().getConnection();

			Apartment apartment = new Apartment();
			
			// Erzeuge Anfrage für Immobilie
			String selectImSQL = "SELECT * FROM estate WHERE id = ?";
			PreparedStatement pstmtIm = con.prepareStatement(selectImSQL);
			pstmtIm.setInt(1, id);

			// Führe Anfrage aus
			ResultSet rs = pstmtIm.executeQuery();
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
				pstmtIm.close();
				apartment.setImmobilie(Im);

				//Nun Anfrage für apartment
				String selectSQL = "SELECT * FROM apartment WHERE id = ?";
				PreparedStatement pstmt = con.prepareStatement(selectSQL);
				pstmt.setInt(1, id);

				ResultSet rs2 = pstmt.executeQuery();
				if (rs2.next()) {
					apartment.setId(id);
					apartment.setFloors(rs2.getInt("floors"));
					apartment.setRent(rs2.getInt("rent"));
					apartment.setRooms(rs2.getInt("rooms"));
					apartment.setBalcony(rs2.getBoolean("balcony"));
					apartment.setKitchen(rs2.getBoolean("builtinkitchen"));

					rs2.close();
					pstmt.close();
					return apartment;
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static void delete(int id) {
		// Hole Verbindung
		Connection con = DbConnectionManager.getInstance().getConnection();
		try {
			String updateSQL = "DELETE FROM apartment WHERE id = ?";
			PreparedStatement pstmt = con.prepareStatement(updateSQL);
	
	
			// Setze Anfrageparameter und führe Anfrage aus
			pstmt.setInt(1, id);
	
			pstmt.executeUpdate();
	
			pstmt.close();
			//Nun Immobilie löschen
			Immobilie.delete(id);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void save() {
		
		// Hole Verbindung
		Connection con = DbConnectionManager.getInstance().getConnection();

		try {
			
			//	Zunächst Immobilie Saven		
			getImmobilie().save();
			
			// Füge neues Element hinzu, wenn das Objekt noch keine ID hat.
			if (getId() == -1) {				
				
				//Id übernehmen
				setId(getImmobilie().getId());
				//Nun apartment
				String insertApartSQL = "INSERT INTO apartment (id, floors, rent, rooms, balcony, builtinkitchen) VALUES(?, ?, ?, ?, ?, ?)";
				PreparedStatement pstmtH = con.prepareStatement(insertApartSQL);

				// Setze Anfrageparameter und führe Anfrage aus
				pstmtH.setInt(1, getId());
				pstmtH.setInt(2, getFloors());
				pstmtH.setFloat(3, getRent());
				pstmtH.setInt(4, getRooms());
				pstmtH.setBoolean(5, getBalcony());
				pstmtH.setBoolean(6, getKitchen());

				pstmtH.executeUpdate();
				pstmtH.close();
				
			} else {				
				// Falls schon eine ID vorhanden ist, mache ein Update...
				String updateSQL = "UPDATE apartment SET floors = ?, rent = ?, rooms = ?, balcony = ?, builtinkitchen = ? WHERE id = ?";
				PreparedStatement pstmt = con.prepareStatement(updateSQL);


				// Setze Anfrageparameter und führe Anfrage aus
				pstmt.setInt(1, getFloors());
				pstmt.setFloat(2, getRent());
				pstmt.setInt(3, getRooms());
				pstmt.setBoolean(4, getBalcony());
				pstmt.setBoolean(5, getKitchen());
				pstmt.setInt(6, getId());


				pstmt.executeUpdate();

				pstmt.close();
				}
		
		} 
		catch (SQLException e) {
		e.printStackTrace();
		}
	}
}
