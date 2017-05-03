package uniroma3.export;

import org.apache.commons.io.FileUtils;
import org.json.JSONArray;
import uniroma3.model.WikiTable;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by khorda on 03/05/17.
 */
public class JsonExporter {

    JSONObject jsonTable;

    public JsonExporter(WikiTable t){

        toJSON(t);

    }

    public void save(String path, String filename){

        String jsonString = jsonTable.toString(4);

        Path file = Paths.get(path + filename + ".json");

        try {
            FileUtils.writeStringToFile(file.toFile(), jsonString,"UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void toJSON (WikiTable t){

        JSONObject result = new JSONObject();

        JSONArray relationData = new JSONArray();

        List<LinkedList<String>> tableRows = t.getData();


        t.toColumns().forEach(column -> {
            relationData.put(column);
        });

        result.put("relation", relationData);

        result.put("pageTitle", "");
        result.put("title","");
        result.put("url",t.getUrl());
        result.put("hasHeader", false);
        result.put("headerPosition", "");
        result.put("tableType", "RELATION");
        result.put("tableNum", 0);
        result.put("s3Link","");
        result.put("recordEndOffset",0);
        result.put("recordOffset",0);
        result.put("tableOrientation", "HORIZONTAL");
        result.put("TableContextTimeStampBeforeTable", new JSONObject());
        result.put("TableContextTimeStampAfterTable", new JSONObject());
        result.put("lastModified", "");
        result.put("textBeforeTable","");
        result.put("textAfterTable","");
        result.put("hasKeyColumn", true);
        result.put("keyColumnIndex", t.getColumnIndex());
        result.put("headerRowIndex", -1);

        this.jsonTable = result;
    }
}
