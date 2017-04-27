package uniroma3.load.wikipedia;

import com.mongodb.Block;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import uniroma3.model.WikiTable;


import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Properties;


/**
 * Created by khorda on 26/04/17.
 */
public class WikiBiColumnsFetcher {

    MongoCollection collection;

    public WikiBiColumnsFetcher(Properties p, String collectionName){

        MongoClientURI connectionString = new MongoClientURI(p.getProperty("wiki.source.mongoDBuri"));
        MongoClient client = new MongoClient(connectionString);
        MongoDatabase database = client.getDatabase(p.getProperty("wiki.source.mongoDBname"));

        collection = database.getCollection(collectionName);



    }


    public List<WikiTable> getTables (Map<String,String> filtersMap, List<String> projectionsList){

        Document filters = new Document();
        for(Map.Entry<String,String> e : filtersMap.entrySet()){
            filters.append(e.getKey(),e.getValue());
        }

        Document projections = new Document();
        for(String projection: projectionsList){
            projections.append(projection,1);
        }

        LinkedList<WikiTable> tables = new LinkedList<>();
        collection
                .find(filters)
                .projection(projections)
                .forEach((Block<Document>) doc->{
                    //TODO mappare i risultati della query nelle istanze di WikiTable
                });

        return tables;
    }
}
