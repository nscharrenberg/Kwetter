package controllers;

import controllers.viewModels.PermissionViewModel;
import domain.Permission;
import exceptions.InvalidContentException;
import exceptions.NameNotUniqueException;
import service.PermissionService;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.PathParam;
import java.util.List;

@Stateless
@Path("/permissions")
public class PermissionController extends Application {
    @Context
    private HttpServletRequest servletRequest;

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
    public Response create(PermissionViewModel request) {
        try {
            Permission permission = new Permission(request.getName());
            permissionService.create(permission);
            return Response.status(Response.Status.CREATED).entity(permission).build();
        } catch (InvalidContentException e) {
            e.printStackTrace();
            return Response.status(Response.Status.NOT_ACCEPTABLE).entity(e.getMessage()).build();
        }

    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{id}")
    public Response getById(@PathParam("id") int id) {
        try {
            Permission permission = permissionService.getById(id);
            return Response.status(Response.Status.OK).entity(permission).build();
        } catch (InvalidContentException e) {
            e.printStackTrace();
            return Response.status(Response.Status.NOT_ACCEPTABLE).entity(e.getMessage()).build();
        }
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/name/{name}")
    public Response getById(@PathParam("name") String name) {
        try {
            Permission permission = permissionService.getByName(name);
            return Response.status(Response.Status.OK).entity(permission).build();
        } catch (InvalidContentException e) {
            e.printStackTrace();
            return Response.status(Response.Status.NOT_ACCEPTABLE).entity(e.getMessage()).build();
        }
    }

    @PATCH
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/{id}")
    public Response update(@PathParam("id") int id, PermissionViewModel request) {
        try {
            Permission permission = new Permission(request.getName());
            permission.setId(id);
            permissionService.update(permission);
            return Response.status(Response.Status.OK).entity(permission).build();
        } catch (InvalidContentException e) {
            e.printStackTrace();
            return Response.status(Response.Status.NOT_ACCEPTABLE).entity(e.getMessage()).build();
        }
    }

    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{id}")
    public Response delete(@PathParam("id") int id) {
        try {
            Permission permission = permissionService.getById(id);
            permissionService.delete(permission);
            return Response.status(Response.Status.OK).entity("Permission " + permission.getName() + " has been deleted").build();
        } catch (InvalidContentException e) {
            e.printStackTrace();
            return Response.status(Response.Status.NOT_ACCEPTABLE).entity(e.getMessage()).build();
        }
    }
}
