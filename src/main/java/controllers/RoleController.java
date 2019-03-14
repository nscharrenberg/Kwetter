package controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import controllers.viewModels.RoleViewModel;
import domain.Permission;
import domain.Role;
import exceptions.*;
import exceptions.NotFoundException;
import responses.JaxResponse;
import responses.ObjectResponse;
import service.PermissionService;
import service.RoleService;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Stateless
@Path("/roles")
public class RoleController {
    @Inject
    private RoleService roleService;

    @Inject
    private PermissionService permissionService;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response all() {
        try {
            ObjectResponse<List<Role>> response = roleService.all();
            return JaxResponse.checkObjectResponse(response);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
        }
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response create(RoleViewModel request) {
        try {
            Role object = new Role(request.getName());

            ObjectResponse<Role> response = roleService.create(object);

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
            ObjectResponse<Role> response = roleService.getById(id);

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
            ObjectResponse<Role> response = roleService.getByName(name);

            return JaxResponse.checkObjectResponse(response);
        }catch (Exception e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
        }
    }

    @PATCH
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/{id}")
    public Response update(@PathParam("id") int id, RoleViewModel request) {
        try {
            Role role = new Role(request.getName());
            role.setId(id);
            ObjectResponse<Role> response = roleService.update(role);

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
            ObjectResponse<Role> response = roleService.getById(id);

            if(response.getObject() == null) {
                return JaxResponse.checkObjectResponse(response);
            }

            ObjectResponse<Role> result = roleService.delete(response.getObject());

            return JaxResponse.checkObjectResponse(result);
        }catch (Exception e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
        }
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/{roleId}/permissions/{permissionId}")
    public Response addPermission(@PathParam("roleId") int roleId, @PathParam("permissionId") int permissionId) {
        try {
            ObjectResponse<Role> getRoleByIdResponse = roleService.getById(roleId);
            ObjectResponse<Permission> getPermissionByIdResponse = permissionService.getById(permissionId);

            if(getRoleByIdResponse.getObject() == null) {
                return JaxResponse.checkObjectResponse(getRoleByIdResponse);
            }

            if(getPermissionByIdResponse.getObject() == null) {
                return JaxResponse.checkObjectResponse(getPermissionByIdResponse);
            }

            ObjectResponse<Role> result = roleService.addPermission(getRoleByIdResponse.getObject(), getPermissionByIdResponse.getObject());
            return JaxResponse.checkObjectResponse(result);
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
        }
    }

    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/{roleId}/permissions/{permissionId}")
    public Response deletePermission(@PathParam("roleId") int roleId, @PathParam("permissionId") int permissionId) {
        try {
            ObjectResponse<Role> getRoleByIdResponse = roleService.getById(roleId);
            ObjectResponse<Permission> getPermissionByIdResponse = permissionService.getById(permissionId);

            if(getRoleByIdResponse.getObject() == null) {
                return JaxResponse.checkObjectResponse(getRoleByIdResponse);
            }

            if(getPermissionByIdResponse.getObject() == null) {
                return JaxResponse.checkObjectResponse(getPermissionByIdResponse);
            }

            ObjectResponse<Role> result = roleService.removePermission(getRoleByIdResponse.getObject(), getPermissionByIdResponse.getObject());
            return JaxResponse.checkObjectResponse(result);
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
        }
    }
}
