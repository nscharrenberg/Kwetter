package service;

import domain.Role;
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
import repository.collection.RoleServiceCollImpl;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

@RunWith(MockitoJUnitRunner.class)
public class RoleServiceTest {

    @Mock
    private RoleServiceCollImpl rs;

    @Mock
    private PermissionServiceCollImpl pr;

    @InjectMocks
    private RoleService roleService;

    @Before
    public void setUp() {
        initMocks(this);
    }

    @Test
    public void getRoleById() {
        int id = 6;
        Role role = mock(Role.class);
        when(role.getId()).thenReturn(id);

        assertEquals(role.getId(), id);
    }

    @Test
    public void getRoleByName() {
        String name = "Adminstrator";
        Role role = mock(Role.class);
        when(role.getName()).thenReturn(name);

        assertEquals(role.getName(), name);
    }

    @Test
    public void createRole() {
        String name = "Tweet";
        try {
            Role role = roleService.create(new Role(name));

            assertEquals(name, role.getName());
        } catch (NameNotUniqueException | InvalidContentException | CreationFailedException e) {
            e.printStackTrace();
            fail("Exception not expected");
        }
    }

    @Test
    public void updateRole() {
        int id = 6;
        String name = "a new name";
        Role role = mock(Role.class);
        when(role.getId()).thenReturn(id);

        assertEquals(role.getId(), id);
        assertNotEquals(name, role.getName());

        role.setName(name);

        try {
            Role result = roleService.update(role);
            assertEquals(id, result.getId());
            assertEquals(name, result.getName());
        } catch (NameNotUniqueException | InvalidContentException e) {
            e.printStackTrace();
            fail("Exception not expected");
        } catch (NotFoundException e) {
            e.printStackTrace();
            fail("Exception not expected");
        }
    }

    @Test
    public void deleteRole() {
        int id = 6;
        Role role = mock(Role.class);
        when(role.getId()).thenReturn(id);

        assertEquals(role.getId(), id);

        try {
            boolean result = roleService.delete(role);
            assertTrue(result);
        }catch (InvalidContentException | NotFoundException e) {
            e.printStackTrace();
            fail("Exception not expected");
        }
    }
}
