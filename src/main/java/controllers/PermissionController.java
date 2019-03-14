package controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import controllers.viewModels.PermissionViewModel;
import domain.Permission;
import exceptions.CreationFailedException;
import exceptions.InvalidContentException;
import exceptions.NameNotUniqueException;
import exceptions.NotFoundException;
import responses.HttpStatusCodes;
import responses.JaxResponse;
import responses.ObjectResponse;
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
    public Response all() {
        List<Permission> permissions = permissionService.all();
        try {
            return Response.status(Response.Status.OK).entity(new ObjectMapper().writeValueAsString(permissions)).build();
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
        }
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response create(PermissionViewModel request) {
        try {
            Permission permission = new Permission(request.getName());

            ObjectResponse<Permission> response = permissionService.create(permission);

            return JaxResponse.checkObjectResponse(response);
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
        }
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{id}")
    public Response getById(@PathParam("id") int id) {
        try {
            ObjectResponse<Permission> response = permissionService.getById(id);

            return JaxResponse.checkObjectResponse(response);
        }catch (Exception e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
        }
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/name/{name}")
    public Response getByName(@PathParam("name") String name) {
        try {
            ObjectResponse<Permission> response = permissionService.getByName(name);

            return JaxResponse.checkObjectResponse(response);
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
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
            ObjectResponse<Permission> response = permissionService.update(permission);

            return JaxResponse.checkObjectResponse(response);
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
        }
    }

    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{id}")
    public Response delete(@PathParam("id") int id) {
        try {
            ObjectResponse<Permission> response = permissionService.getById(id);

            if(response.getObject() == null) {
                return JaxResponse.checkObjectResponse(response);
            }

            ObjectResponse<Permission> result = permissionService.delete(response.getObject());

            return JaxResponse.checkObjectResponse(result);
        }catch (Exception e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
        }
    }
}
