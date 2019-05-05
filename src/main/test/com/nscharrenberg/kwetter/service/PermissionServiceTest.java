package com.nscharrenberg.kwetter.service;

import com.nscharrenberg.kwetter.domain.Permission;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import com.nscharrenberg.kwetter.repository.interfaces.PermissionRepository;
import com.nscharrenberg.kwetter.responses.HttpStatusCodes;
import com.nscharrenberg.kwetter.responses.ObjectResponse;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;

@RunWith(MockitoJUnitRunner.class)
public class PermissionServiceTest {

    @InjectMocks
    private PermissionService permissionService;

    @Mock
    PermissionRepository pr;

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
        List<Permission> permissionList = new ArrayList<>();
        permissionList.add(new Permission("create_tweet"));
        permissionList.add(new Permission("update_tweet"));
        permissionList.add(new Permission("create_user"));
        permissionList.add(new Permission("update_user"));
        when(pr.all()).thenReturn(permissionList);

        // Act
        ObjectResponse<List<Permission>> response = permissionService.all();

        // Assert
        verify(pr, atLeastOnce()).all();
        assertEquals(HttpStatusCodes.OK, response.getCode());
        assertEquals(permissionList, response.getObject());
        assertEquals(permissionList.size(), response.getObject().size());
    }


    /*
     * Get Permission By Id Tests
     */
    @Test
    public void getById_ExistingId_StatusCodeOk() {
        // Arange
        int id = 6;
        String name = "create_tweet";
        Permission permission = new Permission();
        permission.setId(id);
        permission.setName(name);
        when(pr.getById(id)).thenReturn(permission);

        // Act
        ObjectResponse<Permission> response = permissionService.getById(id);

        // Assert
        verify(pr, atLeastOnce()).getById(id);
        assertEquals(HttpStatusCodes.OK, response.getCode());
        assertEquals(permission, response.getObject());
    }

    @Test
    public void getById_IdNull_StatusCodeNotAceptable() {
        // Arange
        int id = 0;

        // Act
        ObjectResponse<Permission> response = permissionService.getById(id);

        // Assert
        assertEquals(HttpStatusCodes.NOT_ACCEPTABLE, response.getCode());
        assertNull(response.getObject());
    }

    @Test
    public void getById_NotExistingId_StatusCodeNotFound() {
        // Arange
        int id = 6;
        when(pr.getById(id)).thenReturn(null);

        // Act
        ObjectResponse<Permission> response = permissionService.getById(id);

        // Assert
        verify(pr, atLeastOnce()).getById(id);
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
        String name = "create_tweet";
        Permission permission = new Permission();
        permission.setId(id);
        permission.setName(name);
        when(pr.getByName(name)).thenReturn(permission);

        // Act
        ObjectResponse<Permission> response = permissionService.getByName(name);

        // Assert
        verify(pr, atLeastOnce()).getByName(name);
        assertEquals(HttpStatusCodes.OK, response.getCode());
        assertEquals(permission, response.getObject());
    }

    @Test
    public void getByName_NoName_StatusCodeNotAcceptable() {
        // Arange
        int id = 6;
        String name = "create_tweet";
        Permission permission = new Permission();
        permission.setId(id);
        permission.setName(name);

        // Act
        ObjectResponse<Permission> response = permissionService.getByName("");

        // Assert
        assertEquals(HttpStatusCodes.NOT_ACCEPTABLE, response.getCode());
        assertNull(response.getObject());
    }

    @Test
    public void getByName_NotExistingName_StatusCodeNotFound() {
        // Arange
        String name = "create_tweet";

        // Act
        ObjectResponse<Permission> response = permissionService.getByName(name);

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
        String name = "Tweet";
        Permission permission = new Permission(name);
        when(pr.create(permission)).thenReturn(permission);

        // Act
        ObjectResponse<Permission> response = permissionService.create(permission);

        // Assert
        verify(pr, atLeastOnce()).create(permission);
        assertEquals(HttpStatusCodes.CREATED, response.getCode());
        assertEquals(permission.getName(), response.getObject().getName());
    }

    @Test
    public void create_NewWithoutName_StatusCodeNotAcceptable() {
        // Arrange
        String name = "";
        Permission permission = new Permission(name);

        // Act
        ObjectResponse<Permission> response = permissionService.create(permission);

        // Assert
        verify(pr, never()).create(permission);
        assertEquals(HttpStatusCodes.NOT_ACCEPTABLE, response.getCode());
        assertNull(response.getObject());
    }

    @Test
    public void create_NewWithDuplicateName_StatusCodeConflict() {
        // Arrange
        String name = "create_tweet";
        Permission permission = new Permission(name);

        when(pr.getByName(name)).thenReturn(permission);

        // Act
        ObjectResponse<Permission> response = permissionService.create(permission);

        // Assert
        verify(pr, atLeastOnce()).getByName(name);
        verify(pr, never()).create(permission);
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
        String name = "a new name";
        Permission permission = new Permission();
        permission.setId(id);
        permission.setName(name);
        when(pr.getById(id)).thenReturn(permission);
        when(pr.getByName(name)).thenReturn(null);
        when(pr.update(permission)).thenReturn(permission);

        //Act
        ObjectResponse<Permission> response = permissionService.update(permission);

        //Assert
        verify(pr, atLeastOnce()).getById(id);
        verify(pr, atLeastOnce()).getByName(name);
        verify(pr, atLeastOnce()).update(permission);
        assertEquals(HttpStatusCodes.OK, response.getCode());
        assertEquals(permission.getName(), response.getObject().getName());
    }

    @Test
    public void update_ExistingWithDuplicateName_StatusCodeConflict() {
        //Arange
        String name = "a new name";

        int existingId = 654;
        Permission existingPermission = new Permission();
        existingPermission.setId(existingId);
        existingPermission.setName(name);

        int id = 6;

        Permission permission = new Permission();
        permission.setId(id);
        permission.setName(name);
        when(pr.getById(id)).thenReturn(permission);
        when(pr.getByName(name)).thenReturn(existingPermission);

        //Act
        ObjectResponse<Permission> response = permissionService.update(permission);

        //Assert
        verify(pr, atLeastOnce()).getById(id);
        verify(pr, atLeastOnce()).getByName(name);
        assertEquals(HttpStatusCodes.CONFLICT, response.getCode());
        assertNull(response.getObject());
    }

    @Test
    public void update_withNoName_StatusCodeNotAcceptable() {
        //Arange
        int id = 6;
        String name = "";
        Permission permission = new Permission();
        permission.setId(id);
        permission.setName(name);

        //Act
        ObjectResponse<Permission> response = permissionService.update(permission);

        //Assert
        assertEquals(HttpStatusCodes.NOT_ACCEPTABLE, response.getCode());
        assertNull(response.getObject());
    }

    @Test
    public void update_withIdNull_StatusCodeNotAcceptable() {
        //Arange
        int id = 0;
        String name = "this is my name";
        Permission permission = new Permission();
        permission.setId(id);
        permission.setName(name);

        //Act
        ObjectResponse<Permission> response = permissionService.update(permission);

        //Assert
        assertEquals(HttpStatusCodes.NOT_ACCEPTABLE, response.getCode());
        assertNull(response.getObject());
    }

    @Test
    public void update_IdDoesNotExist_StatusCodeOk() {
        //Arange
        int id = 6;
        String name = "a new name";
        Permission permission = new Permission();
        permission.setId(id);
        permission.setName(name);
        when(pr.getById(id)).thenReturn(null);

        //Act
        ObjectResponse<Permission> response = permissionService.update(permission);

        //Assert
        verify(pr, atLeastOnce()).getById(id);
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
        String name = "permission name";
        Permission permission = new Permission();
        permission.setId(id);
        permission.setName(name);
        when(pr.getById(id)).thenReturn(permission);
        when(pr.delete(permission)).thenReturn(true);

        // Act
        ObjectResponse<Permission> response = permissionService.delete(permission);

        // Assert
        verify(pr, atLeastOnce()).getById(id);
        verify(pr, atLeastOnce()).delete(permission);
        assertEquals(HttpStatusCodes.OK, response.getCode());
        assertNull(response.getObject());
    }

    @Test
    public void delete_IdNull_StatusCodeNotAcceptable() {
        // Arrange
        int id = 0;
        String name = "permission name";
        Permission permission = new Permission();
        permission.setId(id);
        permission.setName(name);

        // Act
        ObjectResponse<Permission> response = permissionService.delete(permission);

        // Assert
        assertEquals(HttpStatusCodes.NOT_ACCEPTABLE, response.getCode());
        assertNull(response.getObject());
    }

    @Test
    public void delete_IdDoesNotExist_StatusCodeNotFound() {
        // Arrange
        int id = 6;
        String name = "permission name";
        Permission permission = new Permission();
        permission.setId(id);
        permission.setName(name);
        when(pr.getById(id)).thenReturn(null);

        // Act
        ObjectResponse<Permission> response = permissionService.delete(permission);

        // Assert
        verify(pr, atLeastOnce()).getById(id);
        assertEquals(HttpStatusCodes.NOT_FOUND, response.getCode());
        assertNull(response.getObject());
    }
}
