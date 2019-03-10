package service;

import domain.Permission;
import domain.Tweet;
import domain.User;
import exceptions.CreationFailedException;
import exceptions.InvalidContentException;
import exceptions.NameNotUniqueException;
import exceptions.NotFoundException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import repository.collection.PermissionServiceCollImpl;
import repository.interfaces.PermissionRepository;

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


    @Test
    public void getPermissionById() throws InvalidContentException, NotFoundException {
        int id = 6;
        Permission permission = mock(Permission.class);
        permission.setId(id);

        when(pr.getById(id)).thenReturn(permission);

        permissionService.getById(id);
        verify(pr, atLeastOnce()).getById(id);
    }

    @Test
    public void getPermissionByName() throws InvalidContentException, NotFoundException {
        String name = "Tweet";
        Permission permission = mock(Permission.class);
        permission.setName(name);
        when(pr.getByName(name)).thenReturn(permission);

        permissionService.getByName(name);
        verify(pr, atLeastOnce()).getByName(name);
    }

    @Test
    public void createPermission() throws InvalidContentException, NameNotUniqueException, CreationFailedException {
        String name = "Tweet";
        Permission permission = new Permission(name);
        when(pr.create(permission)).thenReturn(permission);
        permissionService.create(permission);

        verify(pr, atLeastOnce()).create(permission);

    }

    @Test
    public void updatePermission() throws InvalidContentException, NameNotUniqueException, NotFoundException {
        int id = 6;
        String name = "a new name";
        String newName = "a new name";
        Permission permission = new Permission();
        permission.setId(id);
        permission.setName(name);

        permission.setName(newName);

        when(pr.getByName(newName)).thenReturn(null);
        when(pr.update(permission)).thenReturn(permission);

        permissionService.update(permission);
        verify(pr, atLeastOnce()).update(permission);
    }

    @Test
    public void updatePermissionDuplicateName() throws InvalidContentException, NameNotUniqueException, NotFoundException {
        int id = 6;
        String name = "a new name";
        Permission permission = new Permission();
        permission.setId(id);
        permission.setName(name);

        int id2 = 43452;
        String name2 = "another name";
        Permission permission2 = new Permission();
        permission2.setId(id2);
        permission2.setName(name2);

        permission2.setName(name);

        when(pr.getByName(name)).thenReturn(permission);
        when(pr.update(permission)).thenReturn(null);

        permissionService.update(permission);
        verify(pr, atLeastOnce()).update(permission);
    }

    @Test
    public void deletePermission() throws InvalidContentException, NotFoundException {
        int id = 6;
        Permission permission = new Permission();
        permission.setId(id);

        when(pr.getById(id)).thenReturn(permission);
        when(pr.delete(permission)).thenReturn(true);

        permissionService.delete(permission);
        verify(pr, atLeastOnce()).delete(permission);
    }
}
