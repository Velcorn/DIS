package de.dis;

import java.sql.Date;
import java.util.Calendar;
import java.util.List;

import de.dis.data.Apartment;
import de.dis.data.Haus;
import de.dis.data.Immobilie;
import de.dis.data.Kaufvertrag;
import de.dis.data.Makler;
import de.dis.data.Mietvertrag;
import de.dis.data.Person;
import de.dis.data.Verkaufen;
import de.dis.data.Vermieten;
import de.dis.data.Vertrag;

/**
 * Hauptklasse
 */
public class Main {
	/**
	 * Startet die Anwendung
	 */
	public static void main(String[] args) {
		showMainMenu();
	}

	/**
	 * Zeigt das Hauptmenü
	 */
	public static void showMainMenu() {

		// Erzeuge Menü
		Menu mainMenu = new Menu("Hauptmenü");
		mainMenu.addEntry("Makler-Verwaltung", 0);
		mainMenu.addEntry("Imobilien-Verwaltung", 1);
		mainMenu.addEntry("Vertrag-Verwaltung", 2);
		mainMenu.addEntry("Beenden", 3);
		// Verarbeite Eingabe
		while (true) {
			int response = mainMenu.show();

			switch (response) {
			case 0:
				String password = FormUtil.readString("Passwort?");
				if (password.equals("passwort")) {
					showMaklerMenu();
				} else
					System.out.println("Passwort inkorrekt!");
				break;
			case 1:
				Makler makler = Makler.login(FormUtil.readString("Benutzername?"), FormUtil.readString("Passwort?"));
				if (makler != null) {
					showImmobilienMenu(makler);
				} else {
					System.out.println("Benutzername oder Passwort inkorrekt.");
				}
				break;
			case 2:
				showVertragsMenu();
				break;
			case 3:
				Runtime.getRuntime().exit(0);
				return;
			}
		}
	}

	/**
	 * Zeigt die Maklerverwaltung
	 */
	public static void showMaklerMenu() {

		// Maklerverwaltungsmenü
		Menu maklerMenu = new Menu("Makler-Verwaltung");
		maklerMenu.addEntry("Neuer Makler", 0);
		maklerMenu.addEntry("Editiere Makler", 1);
		maklerMenu.addEntry("Lösche Makler", 2);
		maklerMenu.addEntry("Zurück zum Hauptmenü", 3);

		// Verarbeite Eingabe
		while (true) {
			int response = maklerMenu.show();
			Makler makler = new Makler();
			switch (response) {
			case 0:
				makler.setName(FormUtil.readString("Name?"));
				makler.setAddress(FormUtil.readString("Adresse?"));
				makler.setLogin(FormUtil.readString("Login?"));
				makler.setPassword(FormUtil.readString("Passwort?"));
				makler.save();
				System.out.println("Makler wurde angelegt.");
				break;
			case 1:
				makler = Makler.load(FormUtil.readInt("Geben Sie die Id von dem Makler an, den Sie editieren wollen"));
				showEditMaklerMenu(makler);
				break;
			case 2:
				Makler.delete(
						FormUtil.readInt("geben Sie die Id von dem Makler ein, den Sie endgültig löschen wollen"));
				System.out.println("Makler wurde erfolgreich gelöscht");
				break;
			case 3:
				showMainMenu();
				break;
			}
		}
	}

	public static void showImmobilienMenu(Makler makler) {

		// Maklerverwaltungsmenü
		Menu maklerMenu = new Menu("Immobilien-Verwaltung");
		maklerMenu.addEntry("Neue Immobilie", 0);
		maklerMenu.addEntry("Editiere Immobilie", 1);
		maklerMenu.addEntry("Lösche Immobilie", 2);
		maklerMenu.addEntry("Zurück zum Hauptmenü", 3);

		// Verarbeite Eingabe
		while (true) {
			int response = maklerMenu.show();

			switch (response) {
			case 0: {
				// Nun klären, ob es sich um ein Haus oder Appartment handelt.
				int rs = 0;
				while (rs < 1 || rs > 2) {
					rs = FormUtil.readInt("Antwort als Zahl: handelt es sich um ein Apartment (1) oder ein Haus (2) ?");
				}
				// Nacheinander wird der Benutzer nach den Eigenschaften der neuen Immobilie
				// gefragt.
				Immobilie immobilie = new Immobilie();
				immobilie.setCity(FormUtil.readString("Stadt?"));
				immobilie.setPostalcode(FormUtil.readInt("Postleitzahl?"));
				immobilie.setStreet(FormUtil.readString("Straße?"));
				immobilie.setStreetnumber(FormUtil.readInt("Nummer?"));
				immobilie.setSquarearea(FormUtil.readFloat("Quadratmeter?"));
				// TODO: Abfagen von fehler; überprüfe, ob Id existiert
				immobilie.setManages(makler.getId());

				// Appartment
				if (rs == 1) {
					Apartment apartment = new Apartment();
					apartment.setImmobilie(immobilie);
					apartment.setFloors(FormUtil.readInt("Anzahl Stockwerke?"));
					apartment.setRent(FormUtil.readInt("Miete?"));
					apartment.setRooms(FormUtil.readInt("Anzahl der Räume?"));
					rs = 0;
					while (rs < 1 || rs > 2) {
						rs = FormUtil.readInt("Antwort als Zahl: Balkon vorhanden? ja (1) | nein (2) ?");
					}
					if (rs == 1) {
						apartment.setBalcony(true);
						
					} else {
						apartment.setBalcony(false);
					}
					rs = 0;
					while (rs < 1 || rs > 2) {
						rs = FormUtil.readInt("Antwort als Zahl: Einbauküche vorhanden? ja (1) | nein (2) ?");
					}
					if (rs == 1) {
						apartment.setKitchen(true);
						
					} else {
						apartment.setKitchen(false);
					}
					apartment.save();
					System.out.println("Apartment wurde hinzugefügt.");

				}
				// Haus
				else {
					Haus haus = new Haus();
					haus.setImmobilie(immobilie);
					haus.setFloors(FormUtil.readInt("Anzahl der Stockwerke?"));
					haus.setPrice(FormUtil.readInt("Preis?"));
					int rs1 = 0;
					while (rs1 < 1 || rs1 > 2) {
						rs1 = FormUtil.readInt("Antwort als Zahl: Garten vorhanden? ja (1) | nein (2) ?");
					}
					if (rs1 == 1) {
						haus.setGarden(true);
						
					} else {
						haus.setGarden(false);
					}					
					haus.save();
					System.out.println("Haus wurde hinzugefügt.");
				}
				break;
			}
			case 1: {
				// Nun klären, ob es sich um ein Haus oder Appartment handelt.
				int rs = 0;
				while (rs < 1 || rs > 2) {
					rs = FormUtil.readInt("Antwort als Zahl: handelt es sich um ein Apartment (1) oder ein Haus (2) ?");
				}
				// Apartment
				if (rs == 1) {
					Apartment apartment = new Apartment();
					apartment = Apartment
							.load(FormUtil.readInt("Geben Sie die Id von dem Apartment an, das Sie editieren wollen "));
					showEditimmobilieMenu(null, apartment, makler);
				}
				// Haus
				else {
					Haus haus = new Haus();
					haus = Haus.load(FormUtil.readInt("Geben Sie die Id von dem Haus an, das Sie editieren wollen "));
					showEditimmobilieMenu(haus, null, makler);
				}
				break;
			}
			case 2:
				// Nun klären, ob es sich um ein Haus oder Appartment handelt.ä
				int rs = 0;
				while (rs < 1 || rs > 2) {
					rs = FormUtil.readInt("Antwort als Zahl: handelt es sich um ein Apartment (1) oder ein Haus (2) ?");
				}
				// Apartment
				if (rs == 1) {
					Apartment.delete(FormUtil
							.readInt("Geben Sie die Id von dem Apartment an, welches Sie entgültig löschen wollen"));
					System.out.println("Apartment wurde aus der DB gelöscht.");
				}
				// Haus
				else {
					Haus.delete(
							FormUtil.readInt("Geben Sie die Id von dem Haus an, welches Sie entgültig löschen wollen"));
					System.out.println("Haus wurde aus der DB gelöscht.");
				}

				break;
			case 3:
				showMainMenu();
				break;
			}
		}
	}

	public static void showVertragsMenu() {

		// Maklerverwaltungsmenü
		Menu maklerMenu = new Menu("Makler-Verwaltung");
		maklerMenu.addEntry("Neuer Kunde", 0);
		maklerMenu.addEntry("Erstelle Vertrag", 1);
		maklerMenu.addEntry("Verträge einsehen", 2);
		maklerMenu.addEntry("Zurück zum Hauptmenü", 3);

		// Verarbeite Eingabe
		while (true) {
			int response = maklerMenu.show();

			switch (response) {
			case 0:
				Person personnew = new Person();
				personnew.setFirstname(FormUtil.readString("Vorname?"));
				personnew.setName(FormUtil.readString("Nachname?"));
				personnew.setAddress(FormUtil.readString("Adresse?"));
				personnew.save();
				System.out.println("Kunde wurde erfolgreich hinzugefügt.");
				break;
			case 1:
				int rs = 0;
				while (rs < 1 || rs > 2) {
					rs = FormUtil.readInt("Antwort als Zahl: handelt es sich um ein Apartment (1) oder ein Haus (2) ?");
				}
				// Apartment
				if (rs == 1) {
					// TODO: Checken, ob Eingaben korrekt sind.
					Apartment apartment = Apartment.load(FormUtil.readInt("Geben Sie die Id des Arpatments an."));

					Person person = Person.loadPerName(FormUtil.readString("Vorname des Mieters?"),
							FormUtil.readString("Nachname des Mieters?"));

					Mietvertrag mietvertrag = new Mietvertrag();
					mietvertrag.setStartdate(new Date(System.currentTimeMillis()));
					mietvertrag.setDuration(FormUtil.readInt("Vertragszeit in Monaten?"));
					mietvertrag.setAdditionalcosts(FormUtil.readFloat("Zusätzliche Kosten?"));

					Vertrag vertrag = new Vertrag();

					Calendar calendar = Calendar.getInstance();
					calendar.set(Calendar.DAY_OF_MONTH, FormUtil.readInt("aktueller Tag?"));
					calendar.set(Calendar.MONTH, FormUtil.readInt("aktueller Monat?")); 
					calendar.set(Calendar.YEAR, FormUtil.readInt("aktuelles Jahr?"));


					java.sql.Date date = new java.sql.Date(calendar.getTime().getTime());

					vertrag.setDate(date);
					vertrag.setPlace(FormUtil.readString("ort der Vereinbarung?"));

					mietvertrag.setVertrag(vertrag);
					mietvertrag.save();

					Mietvertrag.rent(apartment.getId(), person.getId(), mietvertrag.getId());

					System.out.println("Mietvertrag wurde der Datenbank hinzugefügt.");

				}
				// Haus
				else {
					Haus haus = Haus.load(FormUtil.readInt("Geben Sie die Id des Hauses an."));
					Person person = Person.loadPerName(FormUtil.readString("Vorname des Käufers?"),
							FormUtil.readString("Nachname des Käufers?"));

					Kaufvertrag kaufvertrag = new Kaufvertrag();
					kaufvertrag.setNoofinstallments(FormUtil.readInt("Installationskosten?"));
					kaufvertrag.setInterestrate(FormUtil.readFloat("Zinszatz?"));

					Vertrag vertrag = new Vertrag();

					Calendar calendar = Calendar.getInstance();

					calendar.set(Calendar.DAY_OF_MONTH, FormUtil.readInt("aktueller Tag?"));
					calendar.set(Calendar.MONTH, FormUtil.readInt("aktueller Monat?")); 
					calendar.set(Calendar.YEAR, FormUtil.readInt("aktuelles Jahr?"));

					java.sql.Date date = new java.sql.Date(calendar.getTime().getTime());

					vertrag.setDate(date);
					vertrag.setPlace(FormUtil.readString("ort der Vereinbarung?"));

					kaufvertrag.setVertrag(vertrag);
					kaufvertrag.save();

					Kaufvertrag.sell(haus.getId(), person.getId(), kaufvertrag.getId());
					System.out.println("Kaufvertrag wurde der Datenbank hinzugefügt.");

				}

				break;
			case 2:
				// Alle Verträge werden nacheinander aufgelistet. Zuerst Kaufverträge
				System.out.println("Alle Kaufverträge von Häusern:");

				List<Verkaufen> verkaufenList = Verkaufen.laodAll();

				for (Verkaufen v : verkaufenList) {
					System.out.println(
							v.getHaus().getImmobilie().getCity() + " | " + v.getHaus().getImmobilie().getStreet() + " "
									+ v.getHaus().getImmobilie().getStreetnumber() + " | "
									+ v.getHaus().getImmobilie().getSquarearea() + " m² | Kaufpreis: "
									+ v.getHaus().getPrice() + " | Zinssatz: " + v.getKaufvertrag().getInterestrate()
									+ " | Käufer: " + v.getPerson().getFirstname() + " " + v.getPerson().getName());

				}

				System.out.println("Alle Mietverträge von Apartments:");
				List<Vermieten> vermietenList = Vermieten.laodAll();
				for (Vermieten v : vermietenList) {
					System.out.println(
							v.getApartment().getImmobilie().getCity() + " | " + v.getApartment().getImmobilie().getStreet() + " "
									+ v.getApartment().getImmobilie().getStreetnumber() + " | "
									+ v.getApartment().getImmobilie().getSquarearea() + " m² | Kaufpreis: "
									+ v.getApartment().getRent() + " | Laufzeit: " + v.getMietvertrag().getDuration()
									+ " | Mieter: " + v.getPerson().getFirstname() + " " + v.getPerson().getName());

				}
				break;
			case 3:
				showMainMenu();
				break;
			}
		}
	}

	// Menü zum editieren von Immobilien. Jeder menüpunkt spiegelt eine Eigenschaft
	// wieder, die durch auswählen geändert werden kann.
	// Änderungen werden zwischengespeichert. In die Datenbank wird erst nach
	// Verlassen des Menüs geschrieben.
	public static void showEditimmobilieMenu(Haus haus, Apartment apartment, Makler makler) {
		if (haus != null) {

			Menu editImm = new Menu("Haus-Editieren. Wählen Sie Sie die Eigenschaft, die Sie ändern möchten");
			editImm.addEntry("Stadt: " + haus.getImmobilie().getCity(), 0);
			editImm.addEntry("Postleitzahl: " + haus.getImmobilie().getPostalcode(), 1);
			editImm.addEntry("Straße: " + haus.getImmobilie().getStreet(), 2);
			editImm.addEntry("Straßennummer: " + haus.getImmobilie().getStreetnumber(), 3);
			editImm.addEntry("Quadratmeter: " + haus.getImmobilie().getSquarearea(), 4);
			editImm.addEntry("Zuständiger Makler (Id): " + haus.getImmobilie().getManages(), 5);
			editImm.addEntry("Anzahl Stockwerke: " + haus.getFloors(), 6);
			editImm.addEntry("Preis: " + haus.getPrice(), 7);
			editImm.addEntry("Gartenquadratmeter: " + haus.getGarden(), 8);
			editImm.addEntry("Zurück zur Immobilien-Verwaltung", 9);

			while (true) {
				int response = editImm.show();
				switch (response) {
				case 0:
					haus.getImmobilie().setCity(FormUtil.readString("neue Stadt?"));
					showEditimmobilieMenu(haus, apartment, makler);
					break;
				case 1:
					haus.getImmobilie().setPostalcode(FormUtil.readInt("neue Postleitzahl?"));
					showEditimmobilieMenu(haus, apartment, makler);
					break;
				case 2:
					haus.getImmobilie().setStreet(FormUtil.readString("neue Straße?"));
					showEditimmobilieMenu(haus, apartment, makler);
					break;
				case 3:
					haus.getImmobilie().setStreetnumber(FormUtil.readInt("neue Straßennummer?"));
					showEditimmobilieMenu(haus, apartment, makler);
					break;
				case 4:
					haus.getImmobilie().setSquarearea(FormUtil.readFloat("neue Quadratmeteranzahl?"));
					showEditimmobilieMenu(haus, apartment, makler);
					break;
				case 5:
					haus.getImmobilie().setManages(FormUtil.readInt("neuer zuständiger Makler (id)?"));
					showEditimmobilieMenu(haus, apartment, makler);
					break;
				case 6:
					haus.setFloors(FormUtil.readInt("neue Stockwerkanzahl?"));
					showEditimmobilieMenu(haus, apartment, makler);
					break;
				case 7:
					haus.setPrice(FormUtil.readInt("neuer Preis?"));
					showEditimmobilieMenu(haus, apartment, makler);
					break;
				case 8:
					int rs1 = 0;
					while (rs1 < 1 || rs1 > 2) {
						rs1 = FormUtil.readInt("Antwort als Zahl: Garten vorhanden? ja (1) | nein (2) ?");
					}
					if (rs1 == 1) {
						haus.setGarden(true);
						
					} else {
						haus.setGarden(false);
					}					
					showEditimmobilieMenu(haus, apartment, makler);
					break;
				case 9:
					haus.save();
					showImmobilienMenu(makler);
					break;
				}

			}
		}
		// apartment
		else {
			Menu editImm = new Menu("Haus-Editieren. Wählen Sie Sie die Eigenschaft, die Sie ändern möchten");
			editImm.addEntry("Stadt: " + apartment.getImmobilie().getCity(), 0);
			editImm.addEntry("Postleitzahl: " + apartment.getImmobilie().getPostalcode(), 1);
			editImm.addEntry("Straße: " + apartment.getImmobilie().getStreet(), 2);
			editImm.addEntry("Straßennummer: " + apartment.getImmobilie().getStreetnumber(), 3);
			editImm.addEntry("Quadratmeter: " + apartment.getImmobilie().getSquarearea(), 4);
			editImm.addEntry("Zuständiger Makler (Id): " + apartment.getImmobilie().getManages(), 5);
			editImm.addEntry("Anzahl Stockwerke: " + apartment.getFloors(), 6);
			editImm.addEntry("Miete: " + apartment.getRent(), 7);
			editImm.addEntry("Räume: " + apartment.getRooms(), 8);
			editImm.addEntry("Balkon: " + apartment.getBalcony(), 9);
			editImm.addEntry("Einbauküche: " + apartment.getKitchen(), 10);
			editImm.addEntry("Zurück zur Immobilien-Verwaltung", 11);

			while (true) {
				int response = editImm.show();
				switch (response) {
				case 0:
					apartment.getImmobilie().setCity(FormUtil.readString("neue Stadt?"));
					showEditimmobilieMenu(haus, apartment, makler);
					break;
				case 1:
					apartment.getImmobilie().setPostalcode(FormUtil.readInt("neue Postleitzahl?"));
					showEditimmobilieMenu(haus, apartment, makler);
					break;
				case 2:
					apartment.getImmobilie().setStreet(FormUtil.readString("neue Straße?"));
					showEditimmobilieMenu(haus, apartment, makler);
					break;
				case 3:
					apartment.getImmobilie().setStreetnumber(FormUtil.readInt("neue Straßennummer?"));
					showEditimmobilieMenu(haus, apartment, makler);
					break;
				case 4:
					apartment.getImmobilie().setSquarearea(FormUtil.readFloat("neue Quadratmeteranzahl?"));
					showEditimmobilieMenu(haus, apartment, makler);
					break;
				case 5:
					apartment.getImmobilie().setManages(FormUtil.readInt("neuer zuständiger Makler (id)?"));
					showEditimmobilieMenu(haus, apartment, makler);
					break;
				case 6:
					apartment.setFloors(FormUtil.readInt("neue Stockwerkanzahl?"));
					showEditimmobilieMenu(haus, apartment, makler);
					break;
				case 7:
					apartment.setRent(FormUtil.readInt("neue Miete?"));
					showEditimmobilieMenu(haus, apartment, makler);
					break;
				case 8:
					apartment.setRooms(FormUtil.readInt("neue Raumanzahl?"));
					showEditimmobilieMenu(haus, apartment, makler);
					break;
				case 9:
					int rs = 0;
					while (rs < 1 || rs > 2) {
						rs = FormUtil.readInt("Antwort als Zahl: Balkon vorhanden? ja (1) | nein (2) ?");
					}
					if (rs == 1) {
						apartment.setBalcony(true);
						
					} else {
						apartment.setBalcony(false);
					}
					rs = 0;
					
					showEditimmobilieMenu(haus, apartment, makler);
					break;
				case 10:
					int rs1 = 0;
					while (rs1 < 1 || rs1 > 2) {
						rs1 = FormUtil.readInt("Antwort als Zahl: Einbauküche vorhanden? ja (1) | nein (2) ?");
					}
					if (rs1 == 1) {
						apartment.setKitchen(true);
						
					} else {
						apartment.setKitchen(false);
					}					
					showEditimmobilieMenu(haus, apartment, makler);
					break;
				case 11:
					apartment.save();
					showImmobilienMenu(makler);
					break;

				}

			}
		}
	}

	// Menü zum editieren von Makler. Jeder menüpunkt spiegelt eine Eigenschaft
	// wieder, die durch Auswählen verändert werden kann.
	// Änderungen werden zwischengespeichert. In die Datenbank wird erst nach
	// Verlassen des Menüs geschrieben.
	public static void showEditMaklerMenu(Makler makler) {
		Menu editImm = new Menu("Makler-Editieren. Wählen Sie Sie die Eigenschaft, die Sie ändern möchten");
		editImm.addEntry("Name: " + makler.getName(), 0);
		editImm.addEntry("Adresse: " + makler.getAddress(), 1);
		editImm.addEntry("LogIn-Name: " + makler.getLogin(), 2);
		editImm.addEntry("Passwort: " + makler.getPassword(), 3);
		editImm.addEntry("Zurück zur Immobilien-Verwaltung", 4);

		while (true) {
			int response = editImm.show();
			switch (response) {
			case 0:
				makler.setName(FormUtil.readString("neuer Name?"));
				showEditMaklerMenu(makler);
				break;
			case 1:
				makler.setAddress(FormUtil.readString("neue Adresse?"));
				showEditMaklerMenu(makler);
				break;
			case 2:
				makler.setLogin(FormUtil.readString("neuer LogIn-Name?"));
				showEditMaklerMenu(makler);
				break;
			case 3:
				makler.setPassword(FormUtil.readString("neues Passwort?"));
				showEditMaklerMenu(makler);
				break;
			case 4:
				makler.save();
				showMaklerMenu();
				break;
			}

		}

	}
}
