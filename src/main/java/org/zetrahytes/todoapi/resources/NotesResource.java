package org.zetrahytes.todoapi.resources;

import java.util.Objects;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.inject.Singleton;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.zetrahytes.todoapi.db.ElasticsearchDAO;
import org.zetrahytes.todoapi.entity.Note;
import org.zetrahytes.todoapi.entity.User;

import com.fasterxml.jackson.core.JsonProcessingException;

import io.dropwizard.auth.Auth;
import lombok.extern.slf4j.Slf4j;

@Path("/notes")
@Singleton
@Slf4j
public class NotesResource {

    private final ElasticsearchDAO elasticsearchDAO;
    private final String index;
    private final String type;

    public NotesResource(ElasticsearchDAO elasticsearchDAO, String index, String type) {
        this.elasticsearchDAO = elasticsearchDAO;
        this.index = index;
        this.type = type;
    }

    @RolesAllowed({"ADMIN", "USER"})
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    public String addNote(@Auth User user, Note note) throws JsonProcessingException {
        log.info("{} adding a note", user.getName());
        String noteId = elasticsearchDAO.insert(note, index, type);
        return noteId;
    }

    @PermitAll
    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public String getNote(@PathParam("id") String id) {
        log.debug("request to get note with id: {}", id);
        return elasticsearchDAO.get(id, index, type);
    }

    @RolesAllowed({"ADMIN", "USER"})
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response searchNotes(@QueryParam("q") String searchQuery) {
        log.debug("request to search notes for: {}", searchQuery);
        if (Objects.isNull(searchQuery)) {
            return Response.status(Status.BAD_REQUEST).entity("Missing search term").build();
        }
        return Response.ok(elasticsearchDAO.searchNotes(searchQuery, index, type)).build();
    }

}

