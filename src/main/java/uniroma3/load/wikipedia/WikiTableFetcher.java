package uniroma3.load.wikipedia;

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

    String docName;


    public WikiTableFetcher(String tableHTML) {

        Document table = new Document(tableHTML);

        tableData = new ArrayList<>();

        fetch(table);

    }

    public WikiTableFetcher(URI docURIPath) throws IOException {

        String[] pathElements = docURIPath.getPath().split("/");

        this.docName = pathElements[pathElements.length -1];

        File source = new File(docURIPath.getPath());

        Document table = Jsoup.parse(source, "UTF-8", "");

        tableData = new ArrayList<>();

        fetch(table);

    }


    private void fetch(Document table) {

        Element body = table.select("tbody").first();

        Elements rows = body.select("tr");

        rows.forEach(row -> {

            ArrayList<ArrayList<String>> rowData = new ArrayList<>();

            row.select("td").forEach(cell -> {


                Elements a = cell.select("a");

                ArrayList<String> cellElements = new ArrayList<>();

                if (a.isEmpty()) {
                    cellElements.add("");
                } else {

                    a.forEach(aHref -> {

                        String referenceID = aHref.attr("href");

                        String[] urlParts = referenceID.split("/");

                        String wikiID = urlParts[urlParts.length - 1];

                        if (wikiID.contains("%")) {
                            try {
                                wikiID = java.net.URLDecoder.decode(wikiID, "UTF-8");
                            } catch (UnsupportedEncodingException e) {

                            }
                        }
                        //wikiID = StringEscapeUtils.unescapeHtml3(wikiID);

                        if (wikiID.contains("File:") || wikiID.contains("#")) {
                            cellElements.add(""); //insert placeholder
                        } else if (wikiID.contains("index.php")) {
                            int beginIndex = wikiID.indexOf("title=") + 6;
                            int endIndex = wikiID.indexOf("&action=");
                            wikiID = wikiID.substring(beginIndex, endIndex);
                            cellElements.add(wikiID);
                        } else {
                            cellElements.add(wikiID);
                        }


                    });


                }

                rowData.add(cellElements);

            });


            ArrayList<ArrayList<String>> normalizedResult = new ArrayList();
            recursiveRowGenerator(0, rowData, new ArrayList<>(), normalizedResult);
            normalizedResult.forEach(dataRow -> {
                tableData.add(dataRow);
            });


        });
    }

    public WikiTable getWikiTable() {

        return new WikiTable(this.tableData, 0, this.docName);

    }

    private void recursiveRowGenerator(int depth, ArrayList<ArrayList<String>> struct, ArrayList<String> tmpResult, ArrayList<ArrayList<String>> finalResult) {

        if (struct.size() <= depth) {
            finalResult.add(new ArrayList<>(tmpResult));
            return;
        }

        ArrayList<String> currentList = struct.get(depth);

        for (String s : currentList) {

            tmpResult.add(s);

            recursiveRowGenerator(depth + 1, struct, tmpResult, finalResult);

            tmpResult.remove(s);

        }

    }
}
