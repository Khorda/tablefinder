package uniroma3.load.wikipedia;

import com.sun.deploy.util.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import uniroma3.model.WikiTable;

import java.io.*;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by khorda on 27/04/17.
 */
public class WikiTableFetcher {


    ArrayList<List<String>> tableData;


    public WikiTableFetcher(String tableHTML){

        Document table = new Document(tableHTML);

        tableData = new ArrayList<>();

        fetch(table);

    }

    public WikiTableFetcher(URI docURIPath) throws IOException {

        File source = new File(docURIPath.getPath());

        Document table = Jsoup.parse(source, "UTF-8","");

        tableData = new ArrayList<>();

        fetch(table);

    }


    private void fetch(Document table){

        Element body = table.select("tbody").first();

        Elements rows = body.select("tr");

        rows.forEach(row->{

            ArrayList<String> rowData = new ArrayList<>();

            row.select("td").forEach(cell -> {

                try{

                Element aHref = cell.select("a").first();

                String referenceID = aHref.attr("href");

                String[] urlParts = referenceID.split("/");

                String wikiID = urlParts[urlParts.length - 1];

                rowData.add(wikiID);

                }

                catch (NullPointerException e){
                    //e.printStackTrace();
                    rowData.add("");
                }

            });

            tableData.add(rowData);


        });
    }

    public WikiTable getWikiTable(){

        return new WikiTable(this.tableData);

    }
}
