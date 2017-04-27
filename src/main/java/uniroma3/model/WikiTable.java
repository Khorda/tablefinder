package uniroma3.model;

import org.jsoup.nodes.Element;

import java.util.LinkedHashSet;
import java.util.LinkedList;

/**
 * Created by khorda on 20/04/17.
 */
public class WikiTable {

    private LinkedList columnNames = new LinkedList();

    private LinkedList<LinkedList<String>> data = new LinkedList<>();

    private String url;

    public WikiTable(String url,String[][] rows, int labelsIndex){

        for(String[] row : rows){
            LinkedList<String> tmpRow = new LinkedList();
            for(String cell : row){
                tmpRow.add(cell);
            }
            data.add(tmpRow);
        }

        if(labelsIndex != -1) {
            data.remove(labelsIndex);

            for(String label : rows[labelsIndex]){
                columnNames.add(label);
            }
        }

        this.url = url;
    }

    public LinkedList<LinkedList<String>> getData(){
        return this.data;
    }

    public String getUrl(){
        return this.url;
    }

}
