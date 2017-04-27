package uniroma3.load.wikipedia;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Properties;
import java.util.stream.Stream;

/**
 * Created by khorda on 20/04/17.
 */
public class WikiURLsGenerator {


    private Stream<String> source;

    private int cursor;

    private String baseURL;

    private Properties prop;

    public WikiURLsGenerator(Properties p){

        try {
            this.prop = p;
            this.source = Files.lines(Paths.get(p.getProperty("wiki.source.path")));
            this.baseURL = p.getProperty("wiki.source.baseurl");
            if(!Boolean.parseBoolean(p.getProperty("wiki.source.restartFromZero"))){
                this.cursor = Integer.parseInt(p.getProperty("wiki.source.lastUsedLine"));
            }
            else{
                this.cursor = 0;
            }
        }
        catch (Exception e){

        }

    }

    public WikiURLsGenerator(Properties p, int currentLine){
        this(p);
        this.cursor = currentLine;
    }

    public Stream<String> URLsSubset(int number){

        Stream<String> URLsubSet = source.skip(this.cursor).limit(number).map(x->baseURL.concat(x.split("\t")[1]));

        this.cursor += number;

        if(!Boolean.parseBoolean(this.prop.getProperty("wiki.source.restartFromZero"))) {
            saveCursor();
        }

        return URLsubSet;

    }

    private void saveCursor(){

        this.prop.setProperty("wiki.source.lastUsedLine", Integer.toString(this.cursor));


    }





}
