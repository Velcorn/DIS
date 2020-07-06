package de.dis.data;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Mietvertrag {
	private int id = -1;
	private Date startdate;
	private int duration;
	private float additionalcosts;
	private Vertrag vertrag;
	public Date getStartdate() {
		return startdate;
	}
	public void setStartdate(Date startdate) {
		this.startdate = startdate;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public float getAdditionalcosts() {
		return additionalcosts;
	}
	public void setAdditionalcosts(float additionalcosts) {
		this.additionalcosts = additionalcosts;
	}
	public int getDuration() {
		return duration;
	}
	public void setDuration(int duration) {
		this.duration = duration;
	}
	public Vertrag getVertrag() {
		return vertrag;
	}
	public void setVertrag(Vertrag vertrag) {
		this.vertrag = vertrag;
	}
	
	public static Mietvertrag load(int id) {
		try {
			// Hole Verbindung
			Connection con = DbConnectionManager.getInstance().getConnection();

			// Erzeuge Anfrage
			String selectSQL = "SELECT * FROM tenancy_contract WHERE contractnr = ?";
			PreparedStatement pstmt = con.prepareStatement(selectSQL);
			pstmt.setInt(1, id);

			// Führe Anfrage aus
			ResultSet rs = pstmt.executeQuery();
			if (rs.next()) {
				Mietvertrag kv = new Mietvertrag();
				kv.setId(id);
				kv.setStartdate(rs.getDate("startdate"));
				kv.setDuration(rs.getInt("duration"));
				kv.setAdditionalcosts(rs.getInt("additionalcosts"));
				kv.setVertrag(Vertrag.load(id));
				rs.close();
				pstmt.close();
				return kv;
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
			getVertrag().save();
			// Füge neues Element hinzu, wenn das Objekt noch keine ID hat.
			if (getId() == -1) {				
				
				//Id übernehmen
				setId(getVertrag().getId());
				//Nun haus
				String insertMietSQL = "INSERT INTO tenancy_contract (contractnr, startdate, duration, additionalcosts) VALUES(?, ?, ?, ?)";
				PreparedStatement pstmtH = con.prepareStatement(insertMietSQL);

				// Setze Anfrageparameter und führe Anfrage aus
				pstmtH.setInt(1, getId());
				pstmtH.setDate(2, getStartdate());
				pstmtH.setInt(3, getDuration());
				pstmtH.setFloat(4, getAdditionalcosts());



				pstmtH.executeUpdate();

				pstmtH.close();
				
			} else {				
				// Falls schon eine ID vorhanden ist, mache ein Update...
				String updateSQL = "UPDATE tenancy_contract SET startdate = ?, duration = ?, additionalcosts = ? WHERE contractnr = ?";
				PreparedStatement pstmt = con.prepareStatement(updateSQL);


				// Setze Anfrageparameter und führe Anfrage aus
				pstmt.setDate(1, getStartdate());
				pstmt.setInt(2, getDuration());
				pstmt.setFloat(3, getAdditionalcosts());
				pstmt.setInt(4, getId());

				pstmt.executeUpdate();

				pstmt.close();
				}
		
		} 
		catch (SQLException e) {
		e.printStackTrace();
		}
		

	}
	
	public static void rent(int apartId, int personId, int tenancyId) {			

		Connection con = DbConnectionManager.getInstance().getConnection();


		PreparedStatement pstmt;
		try {
			String insertSQL = "INSERT INTO rents(apartment, person, tenancy_contract) VALUES (?, ?, ?)";

			pstmt = con.prepareStatement(insertSQL);
			
			// Setze Anfrageparameter und führe Anfrage aus
			pstmt.setInt(1, apartId);
			pstmt.setInt(2, personId);
			pstmt.setInt(3, tenancyId);
			pstmt.executeUpdate();

			pstmt.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


	}
}
