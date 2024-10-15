package org.hikeSenseServer.controllers;

import java.util.List;

import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;
import org.hikeSenseServer.models.User;

import io.quarkus.mongodb.panache.PanacheMongoEntityBase;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;

@Path("/users")
public class UserController {
    
    @POST
    @Path("/test-post")
    @Consumes("application/json")
    @Produces("text/plain")
    public String testPost(@RequestBody User user) {
        user.persist();
        return "User created: " + user.getId();
    }

    @GET
    @Path("/test-get")
    @Produces("application/json")
    public List<PanacheMongoEntityBase> testGet() {
        return User.listAll();
    }


}
