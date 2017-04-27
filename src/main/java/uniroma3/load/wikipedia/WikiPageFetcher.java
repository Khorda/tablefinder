package uniroma3.load.wikipedia;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import uniroma3.model.WikiTable;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by khorda on 20/04/17.
 */
public class WikiPageFetcher {

    private Set<URL> allUrls;

    private final Map<URL,Elements> pagesWithTables = new LinkedHashMap<>();

    private final Set<URL> pagesWithoutTables = new HashSet<>();


    public WikiPageFetcher(WikiURLsGenerator w, Properties p) {

        this.allUrls = w.URLsSubset(Integer.parseInt(p.getProperty("wiki.fetch.setSize")))
                .map(x -> {
            try {
                return new URL(x);
            } catch (MalformedURLException e) {
                e.printStackTrace();
                return null;
            }
        })
                .filter(x->!Objects.isNull(x))
                .collect(Collectors.toSet());

        distinguish();
    }

    private void distinguish(){

        this.allUrls.forEach(x->{
            try {
                Document d = Jsoup.parse(x, 10000);
                Elements tables = d.select("table.wikitable");
                if(tables.isEmpty()){
                    pagesWithoutTables.add(x);
                }
                else {
                    pagesWithTables.put(x,tables);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }


        });

    }

    public Map<URL,Set<WikiTable>> getTables(){

        Map<URL,Set<WikiTable>> tables = new LinkedHashMap<>();
        pagesWithTables.forEach((url,elements)->{
            Set<WikiTable> wts = new HashSet();
            for (Element e: elements) {
                //wts.add(new WikiTable(e)); TODO
            }
            tables.put(url,wts);
        });
        return tables;
    }

}
