package ex6;

public class Article {

    public int Id;
    public String Name;
    public double Price;
    public String ProductGroup;
    public String ProductFamily;
    public String ProductCategory;

    private static int count = 0;


    public Article() {
        count++;
        Id = count;
    }

}
