package org.zetrahytes.todoapi.resources;

import java.util.Collection;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import javax.inject.Singleton;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zetrahytes.todoapi.entity.Note;
import io.dropwizard.hibernate.UnitOfWork;

@Path("/notes")
@Singleton
@Produces(MediaType.APPLICATION_JSON)
public class NotesResource {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(NotesResource.class);
    private final Map<String, Note> contentMap = new ConcurrentHashMap<>();
    
    @POST
    @UnitOfWork
    @Consumes(MediaType.TEXT_PLAIN)
    public Note addNote(String content) {
        String uuid = UUID.randomUUID().toString();
        Note newNote = new Note(uuid, content);
        this.contentMap.put(uuid, newNote);
        return newNote;
    }
    
    @GET
    @Path("{id}")
    public Response getNote(@PathParam("id") String id) {
        LOGGER.debug("request to get note with id: {}", id);
        Note note = this.contentMap.get(id);
        if (Objects.isNull(note)) {
            return Response.status(Status.NOT_FOUND).build();
        }
        return Response.ok(note).build();
    }
    
    @GET
    public Collection<Note> getAllNotes() {
        return contentMap.values();
    }
}
