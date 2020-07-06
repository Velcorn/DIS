package ex6;

public class Date {
    private static int count = 0;

    public int Id;
    public int Day;
    public int Month;
    public int Year;

    public Date() {
        count++;
        Id = count;
    }

    // setzt das Date-Objekt in die Datenbank
    public void Save() {
    }
}
