package de.dis.data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Haus {
	private int id = -1;
	private int floors;
	private float price;
	private boolean garden;
	private Immobilie immobilie;
	
	public int getId()	{
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}

	public int getFloors() {
		return floors;
	}

	public void setFloors(int floors) {
		this.floors = floors;
	}

	public boolean getGarden() {
		return garden;
	}

	public void setGarden(boolean garden) {
		this.garden = garden;
	}

	public float getPrice() {
		return price;
	}

	public void setPrice(float price) {
		this.price = price;
	}

	public Immobilie getImmobilie() {
		return immobilie;
	}

	public void setImmobilie(Immobilie immobilie) {
		this.immobilie = immobilie;
	}
	
	
	public static Haus load(int id) {
		try {
			// Hole Verbindung
			Connection con = DbConnectionManager.getInstance().getConnection();

			Haus haus = new Haus();
			
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
				haus.setImmobilie(Im);

				//Nun Anfrage für Haus
				String selectSQL = "SELECT * FROM house WHERE id = ?";
				PreparedStatement pstmt = con.prepareStatement(selectSQL);
				pstmt.setInt(1, id);

				ResultSet rs2 = pstmt.executeQuery();
				if (rs2.next()) {
					haus.setId(id);
					haus.setFloors(rs2.getInt("floors"));
					haus.setPrice(rs2.getInt("price"));
					//TODO: haus.setGarden(rs2.getInt("garden"));
					rs2.close();
					pstmt.close();
					return haus;
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
			String updateSQL = "DELETE FROM house WHERE id = ?";
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
				//Nun haus
				String insertHouseSQL = "INSERT INTO house (id, floors, price, garden) VALUES(?, ?, ?, ?)";
				//TODO: Garden!!!
				PreparedStatement pstmtH = con.prepareStatement(insertHouseSQL);

				// Setze Anfrageparameter und führe Anfrage aus
				pstmtH.setInt(1, getId());
				pstmtH.setInt(2, getFloors());
				pstmtH.setFloat(3, getPrice());
				pstmtH.setBoolean(4, getGarden());


				pstmtH.executeUpdate();

				pstmtH.close();
				
			} else {				
				// Falls schon eine ID vorhanden ist, mache ein Update...
				String updateSQL = "UPDATE house SET floors = ?, price = ? , garden = ? WHERE id = ?";
				PreparedStatement pstmt = con.prepareStatement(updateSQL);


				// Setze Anfrageparameter und führe Anfrage aus
				pstmt.setInt(1, getFloors());
				pstmt.setFloat(2, getPrice());
				pstmt.setBoolean(3, getGarden());
				pstmt.setInt(4, getId());

				pstmt.executeUpdate();

				pstmt.close();
				}
		
		} 
		catch (SQLException e) {
		e.printStackTrace();
		}
	}
	
	
}
