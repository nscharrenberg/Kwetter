package controllers;

import controllers.requests.PermissionRequest;
import domain.Permission;
import exceptions.NameNotUniqueException;
import service.PermissionService;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Stateless
@Path("/permissions")
public class PermissionController extends Application {

    @Inject
    private PermissionService permissionService;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Permission> all() {
        return permissionService.all();
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response create(PermissionRequest request) {
        Permission permission = new Permission(request.getName(), request.isCanCreate(), request.isCanRead(), request.isCanUpdate(), request.isCanRemove());

        try {
            permissionService.create(permission);
            return Response.status(Response.Status.CREATED).entity(permission).build();
        } catch (NameNotUniqueException | ClassNotFoundException e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
        }
    }

}
