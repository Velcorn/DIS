package ex6;

public class Shop {

    public int Id;
    public String Name;
    public String City;
    public String Region;
    public String Country;

    private static int count = 0;

    public Shop() {
        count++;
        Id = count;
    }
}
