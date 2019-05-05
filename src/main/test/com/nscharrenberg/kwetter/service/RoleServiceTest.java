package com.nscharrenberg.kwetter.service;

import com.nscharrenberg.kwetter.domain.Permission;
import com.nscharrenberg.kwetter.domain.Role;
import com.nscharrenberg.kwetter.exceptions.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import com.nscharrenberg.kwetter.repository.collection.PermissionServiceCollImpl;
import com.nscharrenberg.kwetter.repository.collection.RoleServiceCollImpl;
import com.nscharrenberg.kwetter.repository.interfaces.RoleRepository;
import com.nscharrenberg.kwetter.repository.jpa.RoleServiceJPAImpl;
import com.nscharrenberg.kwetter.responses.HttpStatusCodes;
import com.nscharrenberg.kwetter.responses.ObjectResponse;

import java.util.ArrayList;
import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsSame.sameInstance;
import static org.junit.Assert.*;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;

@RunWith(MockitoJUnitRunner.class)
public class RoleServiceTest {

    @InjectMocks
    private RoleService roleService;

    @Mock
    private RoleRepository rr;

    @Mock
    private PermissionService pr;

    @Before
    public void setUp() {
        initMocks(this);
    }

    /*
     * Get All Permissions Test
     */
    @Test
    public void all_StatusCodeOk() {
        // Arrange
        List<Role> roleList = new ArrayList<>();
        roleList.add(new Role("Admin"));
        roleList.add(new Role("Member"));
        roleList.add(new Role("Moderator"));
        when(rr.all()).thenReturn(roleList);

        // Act
        ObjectResponse<List<Role>> response = roleService.all();

        // Assert
        verify(rr, atLeastOnce()).all();
        assertEquals(HttpStatusCodes.OK, response.getCode());
        assertEquals(roleList, response.getObject());
        assertEquals(roleList.size(), response.getObject().size());
    }


    /*
     * Get Permission By Id Tests
     */
    @Test
    public void getById_ExistingId_StatusCodeOk() {
        // Arange
        int id = 6;
        String name = "Admin";
        Role role = new Role();
        role.setId(id);
        role.setName(name);
        when(rr.getById(id)).thenReturn(role);

        // Act
        ObjectResponse<Role> response = roleService.getById(id);

        // Assert
        verify(rr, atLeastOnce()).getById(id);
        assertEquals(HttpStatusCodes.OK, response.getCode());
        assertEquals(role, response.getObject());
    }

    @Test
    public void getById_IdNull_StatusCodeNotAceptable() {
        // Arange
        int id = 0;

        // Act
        ObjectResponse<Role> response = roleService.getById(id);

        // Assert
        assertEquals(HttpStatusCodes.NOT_ACCEPTABLE, response.getCode());
        assertNull(response.getObject());
    }

    @Test
    public void getById_NotExistingId_StatusCodeNotFound() {
        // Arange
        int id = 6;
        when(rr.getById(id)).thenReturn(null);

        // Act
        ObjectResponse<Role> response = roleService.getById(id);

        // Assert
        verify(rr, atLeastOnce()).getById(id);
        assertEquals(HttpStatusCodes.NOT_FOUND, response.getCode());
        assertNull(response.getObject());
    }

    /*
     * Get Permission By Name Tests
     */
    @Test
    public void getByName_ExistingName_StatusCodeOk() {
        // Arange
        int id = 6;
        String name = "Admin";
        Role role = new Role();
        role.setId(id);
        role.setName(name);
        when(rr.getByName(name)).thenReturn(role);

        // Act
        ObjectResponse<Role> response = roleService.getByName(name);

        // Assert
        verify(rr, atLeastOnce()).getByName(name);
        assertEquals(HttpStatusCodes.OK, response.getCode());
        assertEquals(role, response.getObject());
    }

    @Test
    public void getByName_NoName_StatusCodeNotAcceptable() {
        // Arange
        int id = 6;
        String name = "Admin";
        Role role = new Role();
        role.setId(id);
        role.setName(name);

        // Act
        ObjectResponse<Role> response = roleService.getByName("");

        // Assert
        assertEquals(HttpStatusCodes.NOT_ACCEPTABLE, response.getCode());
        assertNull(response.getObject());
    }

    @Test
    public void getByName_NotExistingName_StatusCodeNotFound() {
        // Arange
        String name = "Admin";

        // Act
        ObjectResponse<Role> response = roleService.getByName(name);

        // Assert
        assertEquals(HttpStatusCodes.NOT_FOUND, response.getCode());
        assertNull(response.getObject());
    }



    /*
     * Create Permission Tests
     */
    @Test
    public void create_NewWithName_StatusCodeCreated() {
        // Arrange
        String name = "Admin";
        Role role = new Role(name);
        when(rr.create(role)).thenReturn(role);

        // Act
        ObjectResponse<Role> response = roleService.create(role);

        // Assert
        verify(rr, atLeastOnce()).create(role);
        assertEquals(HttpStatusCodes.CREATED, response.getCode());
        assertEquals(role.getName(), response.getObject().getName());
    }

    @Test
    public void create_NewWithoutName_StatusCodeNotAcceptable() {
        // Arrange
        String name = "";
        Role role = new Role(name);

        // Act
        ObjectResponse<Role> response = roleService.create(role);

        // Assert
        verify(rr, never()).create(role);
        assertEquals(HttpStatusCodes.NOT_ACCEPTABLE, response.getCode());
        assertNull(response.getObject());
    }

    @Test
    public void create_NewWithDuplicateName_StatusCodeConflict() {
        // Arrange
        String name = "Admin";
        Role role = new Role(name);

        when(rr.getByName(name)).thenReturn(role);

        // Act
        ObjectResponse<Role> response = roleService.create(role);

        // Assert
        verify(rr, atLeastOnce()).getByName(name);
        verify(rr, never()).create(role);
        assertEquals(HttpStatusCodes.CONFLICT, response.getCode());
        assertNull(response.getObject());
    }


    /*
     * Update Permission Tests
     */
    @Test
    public void update_ExistingWithNewName_StatusCodeOk() {
        //Arange
        int id = 6;
        String name = "Admin";
        Role role = new Role();
        role.setId(id);
        role.setName(name);
        when(rr.getById(id)).thenReturn(role);
        when(rr.getByName(name)).thenReturn(null);
        when(rr.update(role)).thenReturn(role);

        //Act
        ObjectResponse<Role> response = roleService.update(role);

        //Assert
        verify(rr, atLeastOnce()).getById(id);
        verify(rr, atLeastOnce()).getByName(name);
        verify(rr, atLeastOnce()).update(role);
        assertEquals(HttpStatusCodes.OK, response.getCode());
        assertEquals(role.getName(), response.getObject().getName());
    }

    @Test
    public void update_ExistingWithDuplicateName_StatusCodeConflict() {
        //Arange
        String name = "Admin";

        int existingId = 654;
        Role existingRole = new Role();
        existingRole.setId(existingId);
        existingRole.setName(name);

        int id = 6;

        Role role = new Role();
        role.setId(id);
        role.setName(name);
        when(rr.getById(id)).thenReturn(role);
        when(rr.getByName(name)).thenReturn(existingRole);

        //Act
        ObjectResponse<Role> response = roleService.update(role);

        //Assert
        verify(rr, atLeastOnce()).getById(id);
        verify(rr, atLeastOnce()).getByName(name);
        assertEquals(HttpStatusCodes.CONFLICT, response.getCode());
        assertNull(response.getObject());
    }

    @Test
    public void update_withNoName_StatusCodeNotAcceptable() {
        //Arange
        int id = 6;
        String name = "";
        Role role = new Role();
        role.setId(id);
        role.setName(name);

        //Act
        ObjectResponse<Role> response = roleService.update(role);

        //Assert
        assertEquals(HttpStatusCodes.NOT_ACCEPTABLE, response.getCode());
        assertNull(response.getObject());
    }

    @Test
    public void update_withIdNull_StatusCodeNotAcceptable() {
        //Arange
        int id = 0;
        String name = "Admin";
        Role role = new Role();
        role.setId(id);
        role.setName(name);

        //Act
        ObjectResponse<Role> response = roleService.update(role);

        //Assert
        assertEquals(HttpStatusCodes.NOT_ACCEPTABLE, response.getCode());
        assertNull(response.getObject());
    }

    @Test
    public void update_IdDoesNotExist_StatusCodeOk() {
        //Arange
        int id = 6;
        String name = "Admin";
        Role role = new Role();
        role.setId(id);
        role.setName(name);
        when(rr.getById(id)).thenReturn(null);

        //Act
        ObjectResponse<Role> response = roleService.update(role);

        //Assert
        verify(rr, atLeastOnce()).getById(id);
        assertEquals(HttpStatusCodes.NOT_FOUND, response.getCode());
        assertNull(response.getObject());
    }

    /*
     * Delete Permission Tests
     */
    @Test
    public void delete_ExistingId_StatusCodeOk() {
        // Arrange
        int id = 6;
        String name = "Admin";
        Role role = new Role();
        role.setId(id);
        role.setName(name);
        when(rr.getById(id)).thenReturn(role);
        when(rr.delete(role)).thenReturn(true);

        // Act
        ObjectResponse<Role> response = roleService.delete(role);

        // Assert
        verify(rr, atLeastOnce()).getById(id);
        verify(rr, atLeastOnce()).delete(role);
        assertEquals(HttpStatusCodes.OK, response.getCode());
        assertNull(response.getObject());
    }

    @Test
    public void delete_IdNull_StatusCodeNotAcceptable() {
        // Arrange
        int id = 0;
        String name = "permission name";
        Role role = new Role();
        role.setId(id);
        role.setName(name);

        // Act
        ObjectResponse<Role> response = roleService.delete(role);

        // Assert
        assertEquals(HttpStatusCodes.NOT_ACCEPTABLE, response.getCode());
        assertNull(response.getObject());
    }

    @Test
    public void delete_IdDoesNotExist_StatusCodeNotFound() {
        // Arrange
        int id = 6;
        String name = "Admin";
        Role role = new Role();
        role.setId(id);
        role.setName(name);
        when(rr.getById(id)).thenReturn(null);

        // Act
        ObjectResponse<Role> response = roleService.delete(role);

        // Assert
        verify(rr, atLeastOnce()).getById(id);
        assertEquals(HttpStatusCodes.NOT_FOUND, response.getCode());
        assertNull(response.getObject());
    }

    /*
     * Add Permission Tests
     */
    @Test
    public void addPermission_withExistingRoleAndPermission_StatusCodeOk() {
        // Arange
        int roleId = 6;
        String roleName = "Admin";
        Role role = new Role();
        role.setId(roleId);
        role.setName(roleName);

        int permissionId = 563;
        String permissionName = "create_tweet";
        Permission permission = new Permission();
        permission.setId(permissionId);
        permission.setName(permissionName);

        when(rr.getById(roleId)).thenReturn(role);
        when(pr.getById(permissionId)).thenReturn(new ObjectResponse<>(HttpStatusCodes.OK, "Permission with name: " + permission.getName() + " found", permission));
        when(rr.addPermission(role, permission)).thenReturn(role);

        // Act
        ObjectResponse<Role> response = roleService.addPermission(role, permission);

        // Assert
        verify(rr, atLeastOnce()).getById(roleId);
        verify(pr, atLeastOnce()).getById(permissionId);
        verify(rr, atLeastOnce()).addPermission(role, permission);
        assertEquals(HttpStatusCodes.OK, response.getCode());
        assertEquals(role, response.getObject());
    }

    @Test
    public void addPermission_RoleIdNull_StatusCodeNotAcceptable() {
        // Arange
        int roleId = 0;
        String roleName = "Admin";
        Role role = new Role();
        role.setId(roleId);
        role.setName(roleName);

        int permissionId = 563;
        String permissionName = "create_tweet";
        Permission permission = new Permission();
        permission.setId(permissionId);
        permission.setName(permissionName);

        // Act
        ObjectResponse<Role> response = roleService.addPermission(role, permission);

        // Assert
        assertEquals(HttpStatusCodes.NOT_ACCEPTABLE, response.getCode());
        assertNull(response.getObject());
    }

    @Test
    public void addPermission_PermissionIdNull_StatusCodeNotAcceptable() {
        // Arange
        int roleId = 654;
        String roleName = "Admin";
        Role role = new Role();
        role.setId(roleId);
        role.setName(roleName);

        int permissionId = 0;
        String permissionName = "create_tweet";
        Permission permission = new Permission();
        permission.setId(permissionId);
        permission.setName(permissionName);

        // Act
        ObjectResponse<Role> response = roleService.addPermission(role, permission);

        // Assert
        assertEquals(HttpStatusCodes.NOT_ACCEPTABLE, response.getCode());
        assertNull(response.getObject());
    }

    @Test
    public void addPermission_PermissionDoesNotExist_StatusCodeOk() {
        // Arange
        int roleId = 6;
        String roleName = "Admin";
        Role role = new Role();
        role.setId(roleId);
        role.setName(roleName);

        int permissionId = 563;
        String permissionName = "create_tweet";
        Permission permission = new Permission();
        permission.setId(permissionId);
        permission.setName(permissionName);

        when(pr.getById(permissionId)).thenReturn(new ObjectResponse<>(HttpStatusCodes.NOT_FOUND, "Permission not found"));

        // Act
        ObjectResponse<Role> response = roleService.addPermission(role, permission);

        // Assert
        verify(pr, atLeastOnce()).getById(permissionId);
        assertEquals(HttpStatusCodes.NOT_FOUND, response.getCode());
        assertNull(response.getObject());
    }

    @Test
    public void addPermission_RoleDoesNotExist_StatusCodeOk() {
        // Arange
        int roleId = 6;
        String roleName = "Admin";
        Role role = new Role();
        role.setId(roleId);
        role.setName(roleName);

        int permissionId = 563;
        String permissionName = "create_tweet";
        Permission permission = new Permission();
        permission.setId(permissionId);
        permission.setName(permissionName);

        when(rr.getById(roleId)).thenReturn(null);
        when(pr.getById(permissionId)).thenReturn(new ObjectResponse<>(HttpStatusCodes.OK, "Permission with name: " + permission.getName() + " found", permission));

        // Act
        ObjectResponse<Role> response = roleService.addPermission(role, permission);

        // Assert
        verify(rr, atLeastOnce()).getById(roleId);
        verify(pr, atLeastOnce()).getById(permissionId);
        assertEquals(HttpStatusCodes.NOT_FOUND, response.getCode());
        assertNull(response.getObject());
    }

    @Test
    public void addPermission_RoleAndPermissionAlreadyAdded_StatusCodeOk() {
        // Arange
        int roleId = 6;
        String roleName = "Admin";
        Role role = new Role();
        role.setId(roleId);
        role.setName(roleName);

        int permissionId = 563;
        String permissionName = "create_tweet";
        Permission permission = new Permission();
        permission.setId(permissionId);
        permission.setName(permissionName);

        role.addPermission(permission);

        when(rr.getById(roleId)).thenReturn(role);
        when(pr.getById(permissionId)).thenReturn(new ObjectResponse<>(HttpStatusCodes.OK, "Permission with name: " + permission.getName() + " found", permission));

        // Act
        ObjectResponse<Role> response = roleService.addPermission(role, permission);

        // Assert
        verify(rr, atLeastOnce()).getById(roleId);
        verify(pr, atLeastOnce()).getById(permissionId);
        assertEquals(HttpStatusCodes.FORBIDDEN, response.getCode());
        assertNull(response.getObject());
    }

    /*
     * Remove Permission Tests
     */
    @Test
    public void removePermission_withExistingRoleAndPermission_StatusCodeOk() {
        // Arange
        int roleId = 6;
        String roleName = "Admin";
        Role role = new Role();
        role.setId(roleId);
        role.setName(roleName);

        int permissionId = 563;
        String permissionName = "create_tweet";
        Permission permission = new Permission();
        permission.setId(permissionId);
        permission.setName(permissionName);

        role.addPermission(permission);

        when(rr.getById(roleId)).thenReturn(role);
        when(pr.getById(permissionId)).thenReturn(new ObjectResponse<>(HttpStatusCodes.OK, "Permission with name: " + permission.getName() + " found", permission));
        when(rr.removePermission(role, permission)).thenReturn(role);

        // Act
        ObjectResponse<Role> response = roleService.removePermission(role, permission);

        // Assert
        verify(rr, atLeastOnce()).getById(roleId);
        verify(pr, atLeastOnce()).getById(permissionId);
        verify(rr, atLeastOnce()).removePermission(role, permission);
        assertEquals(HttpStatusCodes.OK, response.getCode());
        assertEquals(role, response.getObject());
    }

    @Test
    public void removePermission_RoleIdNull_StatusCodeNotAcceptable() {
        // Arange
        int roleId = 0;
        String roleName = "Admin";
        Role role = new Role();
        role.setId(roleId);
        role.setName(roleName);

        int permissionId = 563;
        String permissionName = "create_tweet";
        Permission permission = new Permission();
        permission.setId(permissionId);
        permission.setName(permissionName);

        role.addPermission(permission);

        // Act
        ObjectResponse<Role> response = roleService.removePermission(role, permission);

        // Assert
        assertEquals(HttpStatusCodes.NOT_ACCEPTABLE, response.getCode());
        assertNull(response.getObject());
    }

    @Test
    public void removePermission_PermissionIdNull_StatusCodeNotAcceptable() {
        // Arange
        int roleId = 654;
        String roleName = "Admin";
        Role role = new Role();
        role.setId(roleId);
        role.setName(roleName);

        int permissionId = 0;
        String permissionName = "create_tweet";
        Permission permission = new Permission();
        permission.setId(permissionId);
        permission.setName(permissionName);

        role.addPermission(permission);

        // Act
        ObjectResponse<Role> response = roleService.removePermission(role, permission);

        // Assert
        assertEquals(HttpStatusCodes.NOT_ACCEPTABLE, response.getCode());
        assertNull(response.getObject());
    }

    @Test
    public void removePermission_PermissionDoesNotExist_StatusCodeNotFound() {
        // Arange
        int roleId = 6;
        String roleName = "Admin";
        Role role = new Role();
        role.setId(roleId);
        role.setName(roleName);

        int permissionId = 563;
        String permissionName = "create_tweet";
        Permission permission = new Permission();
        permission.setId(permissionId);
        permission.setName(permissionName);

        role.addPermission(permission);

        when(pr.getById(permissionId)).thenReturn(new ObjectResponse<>(HttpStatusCodes.NOT_FOUND, "Permission not found"));

        // Act
        ObjectResponse<Role> response = roleService.removePermission(role, permission);

        // Assert
        verify(pr, atLeastOnce()).getById(permissionId);
        assertEquals(HttpStatusCodes.NOT_FOUND, response.getCode());
        assertNull(response.getObject());
    }

    @Test
    public void removePermission_RoleDoesNotExist_StatusCodeNotFound() {
        // Arange
        int roleId = 6;
        String roleName = "Admin";
        Role role = new Role();
        role.setId(roleId);
        role.setName(roleName);

        int permissionId = 563;
        String permissionName = "create_tweet";
        Permission permission = new Permission();
        permission.setId(permissionId);
        permission.setName(permissionName);

        role.addPermission(permission);

        when(rr.getById(roleId)).thenReturn(null);
        when(pr.getById(permissionId)).thenReturn(new ObjectResponse<>(HttpStatusCodes.OK, "Permission with name: " + permission.getName() + " found", permission));

        // Act
        ObjectResponse<Role> response = roleService.removePermission(role, permission);

        // Assert
        verify(rr, atLeastOnce()).getById(roleId);
        verify(pr, atLeastOnce()).getById(permissionId);
        assertEquals(HttpStatusCodes.NOT_FOUND, response.getCode());
        assertNull(response.getObject());
    }

    @Test
    public void removePermission_RoleAndPermissionAreNotAdded_StatusCodeForbidden() {
        // Arange
        int roleId = 6;
        String roleName = "Admin";
        Role role = new Role();
        role.setId(roleId);
        role.setName(roleName);

        int permissionId = 563;
        String permissionName = "create_tweet";
        Permission permission = new Permission();
        permission.setId(permissionId);
        permission.setName(permissionName);

        when(rr.getById(roleId)).thenReturn(role);
        when(pr.getById(permissionId)).thenReturn(new ObjectResponse<>(HttpStatusCodes.OK, "Permission with name: " + permission.getName() + " found", permission));

        // Act
        ObjectResponse<Role> response = roleService.removePermission(role, permission);

        // Assert
        verify(rr, atLeastOnce()).getById(roleId);
        verify(pr, atLeastOnce()).getById(permissionId);
        assertEquals(HttpStatusCodes.FORBIDDEN, response.getCode());
        assertNull(response.getObject());
    }
}
