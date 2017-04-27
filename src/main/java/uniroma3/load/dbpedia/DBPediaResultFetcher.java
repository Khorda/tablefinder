package uniroma3.load.dbpedia;

import uniroma3.load.dbpedia.DBPediaKeyValueIndex;

import java.util.HashSet;
import java.util.Properties;
import java.util.Set;

/**
 * Created by khorda on 20/04/17.
 */
public class DBPediaResultFetcher {

    final DBPediaKeyValueIndex indexKG;

    public DBPediaResultFetcher(Properties p){

        indexKG = new DBPediaKeyValueIndex(p.getProperty("dbpedia.source.kvIndexPath"));
    }


    public Set<String> getRelations(String wikidSubject, String wikidObject) {
        Set<String> relations = new HashSet<String>();
        for (String relation : indexKG.retrieveValues(wikidSubject + "###" + wikidObject))
            relations.add(relation);
        for (String relation : indexKG.retrieveValues(wikidObject + "###" + wikidSubject)){
            if(!relations.contains(relation))
                relations.add(relation + "(-1)");
        }
        return relations;
    }

}
