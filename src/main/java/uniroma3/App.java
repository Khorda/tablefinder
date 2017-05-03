package uniroma3;

import uniroma3.export.JsonExporter;
import uniroma3.load.dbpedia.DBPediaResultFetcher;
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

            //String htmlTablePath = args[0];
            //String firstColumn = args[1];
            //String secondColumn = args[2];


            //WikiBiColumnsFetcher wbcf = new WikiBiColumnsFetcher(p, "biColumnsNR");
            DBPediaResultFetcher dbprf = new DBPediaResultFetcher(p);
            WikiTableFetcher wtf = new WikiTableFetcher(new URI(p.getProperty("app.load.path") + "table.html"));

            //Map<String,String> filters = new HashMap<>();
            //filters.put("","");
            //List<String> projections = new LinkedList<>();
            //projections.add("");

            List<WikiTable> testTables = new LinkedList<>();
            //WikiTable wt1 = new WikiTable("", new String[][] {{"Sagan_Tosu","J._League_Division_2"},
                    //{"Matsumoto_Yamaga_F.C.","Japanese_Regional_Leagues"},
                    //{"Matsumoto_Yamaga_F.C.","Japan_Football_League"}}, -1);
            //WikiTable wt2 = new WikiTable("https://en.wikipedia.org/wiki/List_of_American_football_teams_in_the_United_Kingdom", new String[][] {{"Berkshire_Renegades","Reading,_Berkshire"}}, -1);
            //WikiTable wt3 = new WikiTable("https://en.wikipedia.org/wiki/List_of_American_football_teams_in_the_United_Kingdom" ,new String[][] {{"Belfast_Trojans","Belfast"},
                    //{"Tyrone_Titans", "Omagh"},
                    //{"Craigavon_Cowboys", "Craigavon"}}, -1);

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


            int columnA = 0;
            int columnB = 1;
            List<String[]> relatedIDs = new ArrayList<>();
            List<String[]> unrelatedIDs = new ArrayList<>();
            for(WikiTable table : testTables){
                System.out.println("******BEGIN******");
                int totRows = 0; int relRows = 0; int emptyData = 0;
                for (String[] couple : table.projectOnColumns(columnA,columnB)){
                    totRows++;
                    Set<String> relations = dbprf.getRelations(couple[0],couple[1]);
                    if(couple[0].isEmpty() || couple[1].isEmpty()){
                        emptyData++;
                        continue;
                    }
                    if(!relations.isEmpty()){
                        relRows++;//solo perche sappiamo che hanno 2 colonne
                        relatedIDs.add(new String[]{couple[0],couple[1]});
                    }
                    else{
                        unrelatedIDs.add(new String[]{couple[0],couple[1]});
                    }
                }
                System.out.println("URL: "+ table.getUrl());
                System.out.println("Total parsed IDs: "+ totRows);
                System.out.println("**********************");
                System.out.println("Rows WITHOUT IDs (at least 1): "+ emptyData);
                System.out.println("**********************");
                System.out.println("Rows WITHOUT A relation: "+(totRows - relRows - emptyData));
                for (String[] couple : unrelatedIDs){
                    System.out.println(couple[0]+"<->"+couple[1]);
                }
                System.out.println("**********************");
                System.out.println("Rows WITH A relation: "+(relRows));
                for (String[] couple : relatedIDs){
                    System.out.println(couple[0]+"<->"+couple[1]);
                }
                System.out.println("******END******");

                //save table in json format
                JsonExporter j = new JsonExporter(table);
                j.save(p.getProperty("app.save.path"), table.getDocName()+"."+Integer.toString(table.hashCode()));
            }

            //END


            p.store(new FileOutputStream(propertiesURL.getFile()), "");


        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}

