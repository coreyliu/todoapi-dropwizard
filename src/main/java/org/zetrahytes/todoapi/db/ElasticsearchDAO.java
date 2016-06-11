package org.zetrahytes.todoapi.db;

import java.util.List;

import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.client.Client;
import org.elasticsearch.index.query.QueryBuilders;
import org.zetrahytes.todoapi.entity.Note;
import org.zetrahytes.todoapi.util.Util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.common.collect.Lists;

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

    public List<Note> searchNotes(String searchQuery, String index, String type) {
        List<Note> searchResults = Lists.newArrayList();
        SearchResponse searchResponse = client.prepareSearch(index)
                .setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
                .setTypes(type)
                .setQuery(QueryBuilders.termsQuery("content", searchQuery))
                .execute()
                .actionGet();

        searchResponse.getHits().forEach((hit) -> {
                String id = hit.getId();
                String content = (String) hit.getSource().get("content");
                searchResults.add(new Note(id, content));
            });

        return searchResults;
    }
}
