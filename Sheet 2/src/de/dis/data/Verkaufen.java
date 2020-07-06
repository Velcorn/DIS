package de.dis.data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Verkaufen {
	private Haus haus;
	private Person person;
	private Kaufvertrag kaufvertrag;
	
	public Haus getHaus() {
		return haus;
	}
	public void setHaus(Haus haus) {
		this.haus = haus;
	}
	public Person getPerson() {
		return person;
	}
	public void setPerson(Person person) {
		this.person = person;
	}
	public Kaufvertrag getKaufvertrag() {
		return kaufvertrag;
	}
	public void setKaufvertrag(Kaufvertrag kaufvertrag) {
		this.kaufvertrag = kaufvertrag;
	}
	
	public static List<Verkaufen> laodAll() {
		List<Verkaufen> verkaufenList = new ArrayList<>();
		try {
			// Hole Verbindung
			Connection con = DbConnectionManager.getInstance().getConnection();

			// Erzeuge Anfrage
			String selectSQL = "SELECT * FROM sells ";
			PreparedStatement pstmt = con.prepareStatement(selectSQL);

			// Führe Anfrage aus
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				Verkaufen verkaufen = new Verkaufen();
				// Für jede Spalte den dazugehörigen Tabellen-Eintrag finden
				verkaufen.setHaus(Haus.load(rs.getInt("house")));
				verkaufen.setPerson(Person.load(rs.getInt("person")));
				verkaufen.setKaufvertrag(Kaufvertrag.load(rs.getInt("purchase_contract")));
				verkaufenList.add(verkaufen);
			}
		} catch (SQLException e) {

			e.printStackTrace();

		}
		return verkaufenList;
	}
}
