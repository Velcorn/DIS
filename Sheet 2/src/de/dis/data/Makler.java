package de.dis.data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Makler-Bean
 * 
 * Beispiel-Tabelle:
 * CREATE TABLE makler (
 * name varchar(255), 
 * address varchar(255), 
 * login varchar(40) UNIQUE, 
 * password varchar(40), 
 * id serial primary key);
 */
public class Makler {
	private int id = -1;
	private String name;
	private String address;
	private String login;
	private String password;
	
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getAddress() {
		return address;
	}
	
	public void setAddress(String address) {
		this.address = address;
	}
	
	public String getLogin() {
		return login;
	}
	
	public void setLogin(String login) {
		this.login = login;
	}
	
	public String getPassword() {
		return password;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}
	
	/**
	 * Lädt einen Makler aus der Datenbank
	 * @param id ID des zu ladenden Maklers
	 * @return Makler-Instanz
	 */
	public static Makler load(int id) {
		try {
			// Hole Verbindung
			Connection con = DbConnectionManager.getInstance().getConnection();

			// Erzeuge Anfrage
			String selectSQL = "SELECT * FROM estate_agent WHERE anr = ?";
			PreparedStatement pstmt = con.prepareStatement(selectSQL);
			pstmt.setInt(1, id);

			// Führe Anfrage aus
			ResultSet rs = pstmt.executeQuery();
			if (rs.next()) {
				Makler ts = new Makler();
				ts.setId(id);
				ts.setName(rs.getString("name"));
				ts.setAddress(rs.getString("address"));
				ts.setLogin(rs.getString("login"));
				ts.setPassword(rs.getString("password"));

				rs.close();
				pstmt.close();
				return ts;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	
	/**
	 * Speichert den Makler in der Datenbank. Ist noch keine ID 1vergeben
	 * worden, wird die generierte Id von der DB geholt uäänd dem Model übergeben.ääää
	 */
	public void save() {
		// Hole Verbindung
		Connection con = DbConnectionManager.getInstance().getConnection();

		try {
			// Füge neues Element hinzu, wenn das Objekt noch keine ID hat.
			if (getId() == -1) {
				
				//zunächst die höchste id in der Table finden
				String selectSQL = "SELECT * FROM estate_agent ORDER BY anr DESC LIMIT 1";
				
				PreparedStatement pstmtPre = con.prepareStatement(selectSQL);
				ResultSet rs = pstmtPre.executeQuery();
				//id um 1 erhöhen und als id dieser immobilie festlegen
				if (rs.next()) {
					setId(rs.getInt("anr") + 1);
				}
				
				rs.close();
				

				String insertSQL = "INSERT INTO estate_agent(anr, name, address, login, password) VALUES (?, ?, ?, ?, ?)";

				PreparedStatement pstmt = con.prepareStatement(insertSQL);

				// Setze Anfrageparameter und führe Anfrage aus
				pstmt.setInt(1, getId());
				pstmt.setString(2, getName());
				pstmt.setString(3, getAddress());
				pstmt.setString(4, getLogin());
				pstmt.setString(5, getPassword());
				pstmt.executeUpdate();

				pstmt.close();
			} else {
				// Falls schon eine ID vorhanden ist, mache ein Update...
				String updateSQL = "UPDATE estate_agent SET name = ?, address = ?, login = ?, password = ? WHERE anr = ?";
				PreparedStatement pstmt = con.prepareStatement(updateSQL);

				// Setze Anfrage Parameter
				pstmt.setString(1, getName());
				pstmt.setString(2, getAddress());
				pstmt.setString(3, getLogin());
				pstmt.setString(4, getPassword());
				pstmt.setInt(5, getId());
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
			String updateSQL = "DELETE FROM estate_agent WHERE anr = ?";
			PreparedStatement pstmt = con.prepareStatement(updateSQL);


			// Setze Anfrageparameter und führe Anfrage aus
			pstmt.setInt(1, id);

			pstmt.executeUpdate();

			pstmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static Makler login(String login, String password) {
		
		Makler makler = new Makler();
		Connection con = DbConnectionManager.getInstance().getConnection();
		try {
			String loginSQL = "Select * FROM estate_agent WHERE login = ? AND password = ?";
			PreparedStatement pstmt = con.prepareStatement(loginSQL);
			pstmt.setString(1, login);
			pstmt.setString(2, password);

			ResultSet res = pstmt.executeQuery();
			if (res.next()) {
				makler.setId(res.getInt("anr"));
				makler.setName(res.getString("name"));
				makler.setAddress("address");
				makler.setLogin(login);
				makler.setPassword(password);
				return makler;
			}
			
			pstmt.close();
			res.close();

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	
	}
	
	
	
	
	
	
	
	

	
	
	
	
	

