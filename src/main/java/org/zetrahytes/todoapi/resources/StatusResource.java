package org.zetrahytes.todoapi.resources;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Path("/status")
public class StatusResource {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(StatusResource.class);
    
    @GET
    public String status() {
        LOGGER.info("/status endpoint got invoked");
        return "up";
    }
}
