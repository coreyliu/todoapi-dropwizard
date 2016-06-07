package org.zetrahytes.todoapi.resources;

import javax.inject.Singleton;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zetrahytes.todoapi.db.ElasticsearchDAO;
import org.zetrahytes.todoapi.entity.Note;

import com.fasterxml.jackson.core.JsonProcessingException;

@Path("/notes")
@Singleton
public class NotesResource {

    private static final Logger LOGGER = LoggerFactory.getLogger(NotesResource.class);
    private final ElasticsearchDAO elasticsearchDAO;
    private final String index;
    private final String type;

    public NotesResource(ElasticsearchDAO elasticsearchDAO, String index, String type) {
        this.elasticsearchDAO = elasticsearchDAO;
        this.index = index;
        this.type = type;
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    public String addNote(Note note) throws JsonProcessingException {
        String noteId = elasticsearchDAO.insert(note, index, type);
        return noteId;
    }

    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public String getNote(@PathParam("id") String id) {
        LOGGER.debug("request to get note with id: {}", id);
        return elasticsearchDAO.get(id, index, type);
    }

}
