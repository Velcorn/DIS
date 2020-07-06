package ex6;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) throws SQLException {
        //Aufgae 1
        Aufgabe1();
    }

    private static void Aufgabe1() throws SQLException {
        //Zunächst Die Daten aus der alten Datenbank überführen.
        //Shop
        var ShopList = getDBShopData();
        //Article
        var articleList = getDBArticleData();

        //Diese Daten in die Datenbank überführen
        TransferShopData(ShopList);
        TransferArticleData(articleList);

        //Nun sales.csv Zeile für Zeile lesen und in die Datenbank transferieren. Dabei wird überprüft, ob ein neues Date-entry gesetzt werden muss.
        //Für jede Line entsteht ein 'Fact Table' Eintrag.

        //Datum
        LocalDate lastDate = LocalDate.MIN;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");

        //Für jede line...
        try (BufferedReader br = new BufferedReader(new FileReader("C:\\Users\\Enrico\\Desktop\\DIS4\\ressources\\sales.csv"))) {
            String line;
            while ((line = br.readLine()) != null) {
                var lineArray =  line.split(";");

                //Date
                //Wenn ein neues Datum vorliegt
                if (lastDate.isBefore(LocalDate.parse(lineArray[0], formatter))) {
                    //neues Date erstellen
                    var dateArray = lineArray[0].split(".");
                    Date newDate = new Date();
                    newDate.Day = Integer.parseInt(dateArray[0]);
                    newDate.Month = Integer.parseInt(dateArray[1]);
                    newDate.Month = Integer.parseInt(dateArray[2]);
                    newDate.Save();
                    //lastDate aktuallisieren
                    lastDate = LocalDate.parse(lineArray[0], formatter);
                }

                //das Fact-Entry entry zusammensetzen

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void TransferArticleData(List<Article> articleList) {
    }

    private static void TransferShopData(List<Shop> shopList) {
    }

    //Gibt aus der alten DB die Article-Daten aus und gibt sie als Liste von Article zurück.
    private static List<Article> getDBArticleData() throws SQLException {
        List<Article> articleList = new ArrayList<Article>();
        Connection con = DbConnectionManager.getInstance().getConnection();

        //Erzeuge Anfrage
        String selectSQL ="select Article.Name, Article.price, ProductGroup.Name, ProductFamily.Name, ProductCategory.Name" +
                "from Article, ProductGroup, ProductFamily, ProductCategory " +
                "where Article.productgroupid = ProductGroup.productgroupid and ProductGroup.productfamilyid = ProductFamily.productfamilyid " +
                "and ProductFamily.productcategoryid = ProductCategory.productcategoryid";

        PreparedStatement pstmt = con.prepareStatement(selectSQL);
        ResultSet rs = pstmt.executeQuery();

        while (rs.next()) {
            Article article = new Article();
            article.Name = rs.getString(0);
            article.Price = rs.getDouble(1);
            article.ProductGroup = rs.getString(2);
            article.ProductFamily = rs.getString(3);
            article.ProductCategory = rs.getString(4);
            articleList.add(article);
        }
        rs.close();
        pstmt.close();

        return articleList;
    }

    //Gibt aus der alten DB die Shop-Daten aus und gibt sie als Liste von Shops zurück.
    private static List<Shop> getDBShopData() throws SQLException {
        List<Shop> shopList = new ArrayList<>();
        Connection con = DbConnectionManager.getInstance().getConnection();

        // Erzeuge Anfrage
        String selectSQL = "SELECT Shop.Name, City.Name, Region.Name, Country.Name " +
                "FROM Shop, City, Region, Country " +
                "WHERE Shop.cityid = City.cityid and City.regionid = Region.regionid and Region.countryid = country.countryid  ";
        PreparedStatement pstmt = con.prepareStatement(selectSQL);
        ResultSet rs = pstmt.executeQuery();

        while (rs.next()) {
            Shop shop = new Shop();
            shop.Name = rs.getString(0);
            shop.City = rs.getString(1);
            shop.Region = rs.getString(2);
            shop.Country = rs.getString(3);
            shopList.add(shop);
        }
        rs.close();
        pstmt.close();
        return shopList;
    }
}
