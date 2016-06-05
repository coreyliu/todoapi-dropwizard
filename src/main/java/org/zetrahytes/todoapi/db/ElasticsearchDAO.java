package org.zetrahytes.todoapi.db;

import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.Client;
import org.zetrahytes.todoapi.entity.Note;
import org.zetrahytes.todoapi.util.Util;

import com.fasterxml.jackson.core.JsonProcessingException;

public class ElasticsearchDAO {

    private final Client client;

    public ElasticsearchDAO(Client client) {
        this.client = client;
    }

    public String insert(Note note, String index, String type) throws JsonProcessingException {
        IndexResponse response = client.prepareIndex(index, type)
                .setSource(Util.toJson(note))
                .get();
        return response.getId();
    }

    public String get(String id, String index, String type) {
        GetResponse response = client.prepareGet(index, type, id).get();
        return response.getSourceAsString();
    }    
}
