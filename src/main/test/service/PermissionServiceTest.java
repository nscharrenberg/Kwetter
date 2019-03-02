package service;

import domain.Permission;
import domain.Tweet;
import domain.User;
import exceptions.CreationFailedException;
import exceptions.InvalidContentException;
import exceptions.NameNotUniqueException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import repository.collection.PermissionServiceCollImpl;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

@RunWith(MockitoJUnitRunner.class)
public class PermissionServiceTest {

    @Mock
    PermissionServiceCollImpl pr;

    @InjectMocks
    private PermissionService permissionService;

    @Before
    public void setUp() {
        initMocks(this);
    }


    @Test
    public void getPermissionById() {
        int id = 6;
        Permission permissoin = mock(Permission.class);
        when(permissoin.getId()).thenReturn(id);

        assertEquals(permissoin.getId(), id);
    }

    @Test
    public void getPermissionByName() {
        String name = "Tweet";
        Permission permissoin = mock(Permission.class);
        when(permissoin.getName()).thenReturn(name);

        assertEquals(permissoin.getName(), name);
    }

    @Test
    public void createPermission() {
        String name = "Tweet";
        try {
            Permission permission = permissionService.create(new Permission(name));

            assertEquals(name, permission.getName());

        } catch (NameNotUniqueException | InvalidContentException | CreationFailedException e) {
            e.printStackTrace();
            fail("Exception not expected");
        }
    }

    @Test
    public void updatePermission() {
        int id = 6;
        String name = "a new name";
        Permission permissoin = mock(Permission.class);
        when(permissoin.getId()).thenReturn(id);

        assertEquals(permissoin.getId(), id);
        assertNotEquals(name, permissoin.getName());

        permissoin.setName(name);

        try {
            Permission result = permissionService.update(permissoin);
            assertEquals(id, result.getId());
            assertEquals(name, result.getName());
        } catch (NameNotUniqueException | InvalidContentException e) {
            e.printStackTrace();
            fail("Exception not expected");
        }
    }

    @Test
    public void deletePermission() {
        int id = 6;
        Permission permissoin = mock(Permission.class);
        when(permissoin.getId()).thenReturn(id);

        assertEquals(permissoin.getId(), id);

        try {
            boolean result = permissionService.delete(permissoin);
            assertTrue(result);
        } catch (InvalidContentException e) {
            e.printStackTrace();
            fail("Exception not expected");
        }
    }
}
