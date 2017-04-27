package uniroma3;

import uniroma3.load.dbpedia.DBPediaResultFetcher;
import uniroma3.load.wikipedia.WikiBiColumnsFetcher;
import uniroma3.load.wikipedia.WikiTableFetcher;
import uniroma3.model.WikiTable;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.net.URI;
import java.net.URL;
import java.util.*;

/**
 * Hello world!
 */
public class App {
    public static void main(String[] args) {
        try {
            URL propertiesURL = new File("/Users/khorda/Documents/tablefinder/src/resources/conf.properties").toURI().toURL();
            //URL propertiesURL = new File(args[0]).toURI().toURL();
            Properties p = new Properties();

            p.load(new FileInputStream(propertiesURL.getFile()));


            //BEGIN


            //WikiBiColumnsFetcher wbcf = new WikiBiColumnsFetcher(p, "biColumnsNR");
            DBPediaResultFetcher dbprf = new DBPediaResultFetcher(p);
            WikiTableFetcher wtf = new WikiTableFetcher(new URI("/Users/khorda/Desktop/table.html"));

            Map<String,String> filters = new HashMap<>();
            //filters.put("","");
            List<String> projections = new LinkedList<>();
            //projections.add("");

            List<WikiTable> testTables = new LinkedList<>();
            WikiTable wt1 = new WikiTable("", new String[][] {{"Sagan_Tosu","J._League_Division_2"},
                    {"Matsumoto_Yamaga_F.C.","Japanese_Regional_Leagues"},
                    {"Matsumoto_Yamaga_F.C.","Japan_Football_League"}}, -1);
            WikiTable wt2 = new WikiTable("https://en.wikipedia.org/wiki/List_of_American_football_teams_in_the_United_Kingdom", new String[][] {{"Berkshire_Renegades","Reading,_Berkshire"}}, -1);
            WikiTable wt3 = new WikiTable("https://en.wikipedia.org/wiki/List_of_American_football_teams_in_the_United_Kingdom" ,new String[][] {{"Belfast_Trojans","Belfast"},
                    {"Tyrone_Titans", "Omagh"},
                    {"Craigavon_Cowboys", "Craigavon"}}, -1);

            //testTables.add(wt1);
            //testTables.add(wt2);
            //testTables.add(wt3);

            testTables.add(wtf.getWikiTable());

            /* commented for testing
            for(WikiTable table : wbcf.getTables(filters,projections)){
                for (LinkedList<String> row : table.getData()){
                    dbprf.getRelations(row.get(0), row.get(1));//solo perche sappiamo che hanno 2 colonne
                }
            }
            */


            for(WikiTable table : testTables){
                System.out.println("******BEGIN******");
                int totRows = 0; int relRows = 0;
                for (LinkedList<String> row : table.getData()){
                    totRows++;
                    if(!dbprf.getRelations(row.get(0), row.get(1)).isEmpty()){
                        relRows++;//solo perche sappiamo che hanno 2 colonne
                    }
                }
                System.out.println("URL: "+ table.getUrl());
                System.out.println("Total Rows: "+ totRows);
                System.out.println("Rows WITHOUT relation: "+(totRows - relRows));
                System.out.println("******END******");
            }

            //END


            p.store(new FileOutputStream(propertiesURL.getFile()), "");


        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}

