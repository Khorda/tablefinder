package uniroma3.model;

import org.jsoup.nodes.Element;

import java.util.*;

/**
 * Created by khorda on 20/04/17.
 */
public class WikiTable {

    private LinkedList columnNames = new LinkedList();

    private LinkedList<LinkedList<String>> data = new LinkedList<>();

    private String url;

    private int keyColumnIndex;

    private String docName;

    public WikiTable(String url,String[][] rows, int labelsIndex, int keyColumnIndex){

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
        this.keyColumnIndex = keyColumnIndex;
    }

    public WikiTable(List<List<String>> table, int keyColumnIndex, String docName){

        table.forEach(row->{
            data.add(new LinkedList<String>(row));
        });

        this.url = "";

        this.keyColumnIndex = keyColumnIndex;

        this.docName= docName;

    }

    public LinkedList<LinkedList<String>> getData(){
        return this.data;
    }

    public String getUrl(){
        return this.url;
    }

    public Set<String[]> projectOnColumns(int index1, int index2){

        final class Pair{
            String e1;
            String e2;

            public Pair(String s1, String s2){
                e1 = s1;
                e2 = s2;
            }

            @Override
            public int hashCode(){
                return e1.hashCode() + e2.hashCode();
            }

            @Override
            public boolean equals(Object o){
                return (this.e1.equals(((Pair) o).getE1()) && this.e2.equals(((Pair) o).getE2()));
            }

            public String getE1(){
                return e1;
            }

            public String getE2(){
                return e2;
            }


        }

        Set<Pair> pairSet = new HashSet<Pair>();

        for(List<String> row : this.data) {
            try {
                Pair pair = new Pair(row.get(index1), row.get(index2));
                pairSet.add(pair);
            } catch (Exception e) {
                continue;
            }

        }

        Set<String[]> resultSet = new HashSet<>();

        pairSet.forEach(pair -> {
            String[] couple = new String[]{pair.getE1(),pair.getE2()};
            resultSet.add(couple);
        });

        return resultSet;

    }

    public ArrayList<ArrayList<String>> toColumns(){

        ArrayList<ArrayList<String>> tableColumns = new ArrayList<>();

        for(int i = 0 ; i < this.data.get(0).size() ; i++){

            ArrayList<String> column = new ArrayList<>();

            for (int j = 0 ; j < this.data.size() ; j++){

                String data = this.data.get(j).get(i);

                column.add(data);
            }

            tableColumns.add(column);

        }

        return tableColumns;
    }

    public int getColumnIndex() {
        return this.keyColumnIndex;
    }

    public String getDocName(){
        return this.docName;
    }
}
