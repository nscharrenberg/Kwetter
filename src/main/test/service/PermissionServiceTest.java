package service;

import domain.Permission;
import domain.Tweet;
import domain.User;
import exceptions.NameNotUniqueException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import repository.collection.PermissionServiceCollImpl;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

@RunWith(MockitoJUnitRunner.class)
public class PermissionServiceTest {

    @Mock
    private PermissionServiceCollImpl pr;

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
            Permission permission = permissionService.create(new Permission(name, true, true, false, false));

            assertEquals(name, permission.getName());
            assertTrue(permission.isCanCreate());
            assertTrue(permission.isCanRead());
            assertFalse(permission.isCanUpdate());
            assertFalse(permission.isCanRemove());

        } catch (NameNotUniqueException | ClassNotFoundException e) {
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
        } catch (NameNotUniqueException e) {
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
        }catch (ClassNotFoundException e) {
            e.printStackTrace();
            fail("Exception not expected");
        }
    }
}
