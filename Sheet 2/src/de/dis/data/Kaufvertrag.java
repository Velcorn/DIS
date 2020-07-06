package de.dis.data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Kaufvertrag {
	private int id = -1;
	private int noofinstallments;
	private float interestrate;
	private Vertrag vertrag;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getNoofinstallments() {
		return noofinstallments;
	}
	public void setNoofinstallments(int noofinstallments) {
		this.noofinstallments = noofinstallments;
	}
	public float getInterestrate() {
		return interestrate;
	}
	public void setInterestrate(float interestrate) {
		this.interestrate = interestrate;
	}
	
	public Vertrag getVertrag() {
		return vertrag;
	}
	public void setVertrag(Vertrag vertrag) {
		this.vertrag = vertrag;
	}
	public static Kaufvertrag load(int id) {
		try {
			// Hole Verbindung
			Connection con = DbConnectionManager.getInstance().getConnection();

			// Erzeuge Anfrage
			String selectSQL = "SELECT * FROM purchase_contract WHERE contractnr = ?";
			PreparedStatement pstmt = con.prepareStatement(selectSQL);
			pstmt.setInt(1, id);

			// Führe Anfrage aus
			ResultSet rs = pstmt.executeQuery();
			if (rs.next()) {
				Kaufvertrag kv = new Kaufvertrag();
				kv.setId(id);
				kv.setNoofinstallments(rs.getInt("noofinstallments"));
				kv.setInterestrate(rs.getInt("interestrate"));
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
				String insertKaufSQL = "INSERT INTO purchase_contract (contractnr, noofinstallments, interestrate) VALUES(?, ?, ?)";
				PreparedStatement pstmtH = con.prepareStatement(insertKaufSQL);

				// Setze Anfrageparameter und führe Anfrage aus
				pstmtH.setInt(1, getId());
				pstmtH.setInt(2, getNoofinstallments());
				pstmtH.setFloat(3, getInterestrate());


				pstmtH.executeUpdate();

				pstmtH.close();
				
			} else {				
				// Falls schon eine ID vorhanden ist, mache ein Update...
				String updateSQL = "UPDATE purchase_contract SET noofinstallments = ?, interestrate = ? WHERE contractnr = ?";
				PreparedStatement pstmt = con.prepareStatement(updateSQL);


				// Setze Anfrageparameter und führe Anfrage aus
				pstmt.setInt(1, getNoofinstallments());
				pstmt.setFloat(2, getInterestrate());
				pstmt.setInt(3, getId());

				pstmt.executeUpdate();

				pstmt.close();
				}
		
		} 
		catch (SQLException e) {
		e.printStackTrace();
		}
	}
	
	
	
	public static void sell(int houseId, int personId, int purchaseId) {			

		Connection con = DbConnectionManager.getInstance().getConnection();


		PreparedStatement pstmt;
		try {
			String insertSQL = "INSERT INTO sells(house, person, purchase_contract) VALUES (?, ?, ?)";

			pstmt = con.prepareStatement(insertSQL);
			
			// Setze Anfrageparameter und führe Anfrage aus
			pstmt.setInt(1, houseId);
			pstmt.setInt(2, personId);
			pstmt.setInt(3, purchaseId);
			pstmt.executeUpdate();

			pstmt.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


	}
}
