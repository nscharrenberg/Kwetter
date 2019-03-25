package controllers;

import domain.Permission;
import dtos.permissions.CreatePermissionRequestObject;
import dtos.permissions.EditPermissionRequestObject;
import dtos.permissions.PermissionDto;
import org.modelmapper.ModelMapper;
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
        try {
            ObjectResponse<List<Permission>> response = permissionService.all();

            if(response.getObject() == null) {
                return Response.status(response.getCode()).entity(response.getMessage()).build();
            }

            ModelMapper mapper = new ModelMapper();
            PermissionDto[] permissionDto = mapper.map(response.getObject(), PermissionDto[].class);

            return Response.status(response.getCode()).entity(permissionDto).build();
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
        }
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response create(CreatePermissionRequestObject request) {
        try {
            Permission permission = new Permission(request.getName());

            ObjectResponse<Permission> response = permissionService.create(permission);

            if(response.getObject() == null) {
                return Response.status(response.getCode()).entity(response.getMessage()).build();
            }

            ModelMapper mapper = new ModelMapper();
            PermissionDto permissionDto = mapper.map(response.getObject(), PermissionDto.class);

            return Response.status(response.getCode()).entity(permissionDto).build();
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

            if(response.getObject() == null) {
                return Response.status(response.getCode()).entity(response.getMessage()).build();
            }

            ModelMapper mapper = new ModelMapper();
            PermissionDto permissionDto = mapper.map(response.getObject(), PermissionDto.class);

            return Response.status(response.getCode()).entity(permissionDto).build();
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

            if(response.getObject() == null) {
                return Response.status(response.getCode()).entity(response.getMessage()).build();
            }

            ModelMapper mapper = new ModelMapper();
            PermissionDto permissionDto = mapper.map(response.getObject(), PermissionDto.class);

            return Response.status(response.getCode()).entity(permissionDto).build();
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
        }
    }

    @PATCH
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/{id}")
    public Response update(@PathParam("id") int id, EditPermissionRequestObject request) {
        try {
            Permission permission = new Permission(request.getName());
            permission.setId(id);
            ObjectResponse<Permission> response = permissionService.update(permission);

            if(response.getObject() == null) {
                return Response.status(response.getCode()).entity(response.getMessage()).build();
            }

            ModelMapper mapper = new ModelMapper();
            PermissionDto permissionDto = mapper.map(response.getObject(), PermissionDto.class);

            return Response.status(response.getCode()).entity(permissionDto).build();
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
                return Response.status(response.getCode()).entity(response.getMessage()).build();
            }

            ObjectResponse<Permission> result = permissionService.delete(response.getObject());

            if(result.getObject() == null) {
                return Response.status(response.getCode()).entity(response.getMessage()).build();
            }

            ModelMapper mapper = new ModelMapper();
            PermissionDto permissionDto = mapper.map(response.getObject(), PermissionDto.class);

            return Response.status(result.getCode()).entity(permissionDto).build();
        }catch (Exception e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
        }
    }
}
