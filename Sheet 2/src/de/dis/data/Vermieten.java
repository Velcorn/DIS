package de.dis.data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Vermieten {
	private Apartment apartment;
	private Person person;
	private Mietvertrag mietvertrag;

	public Apartment getApartment() {
		return apartment;
	}

	public void setApartment(Apartment apartment) {
		this.apartment = apartment;
	}

	public Person getPerson() {
		return person;
	}

	public void setPerson(Person person) {
		this.person = person;
	}

	public Mietvertrag getMietvertrag() {
		return mietvertrag;
	}

	public void setMietvertrag(Mietvertrag mietvertrag) {
		this.mietvertrag = mietvertrag;
	}

	public static List<Vermieten> laodAll() {
		List<Vermieten> vermietenList = new ArrayList<>();
		try {
			// Hole Verbindung
			Connection con = DbConnectionManager.getInstance().getConnection();

			// Erzeuge Anfrage
			String selectSQL = "SELECT * FROM rents ";
			PreparedStatement pstmt = con.prepareStatement(selectSQL);

			// Führe Anfrage aus
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				Vermieten vermieten = new Vermieten();
				// Für jede Spalte den dazugehörigen Tabellen-Eintrag finden
				vermieten.setApartment(Apartment.load(rs.getInt("apartment")));
				vermieten.setPerson(Person.load(rs.getInt("person")));
				vermieten.setMietvertrag(Mietvertrag.load(rs.getInt("tenancy_contract")));
				vermietenList.add(vermieten);
			}
		} catch (SQLException e) {

			e.printStackTrace();

		}
		return vermietenList;
	}
}
