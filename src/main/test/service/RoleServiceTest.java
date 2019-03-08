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

import static io.restassured.RestAssured.given;
import static org.junit.Assert.*;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;

@RunWith(MockitoJUnitRunner.class)
public class RoleServiceTest {

    @InjectMocks
    private RoleService roleService;

    @Mock
    private RoleServiceCollImpl rr;

    @Mock
    private PermissionServiceCollImpl pr;

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

    @Test(expected = NotFoundException.class)
    public void getRoleByNameNotFound() throws NotFoundException, InvalidContentException {
        String name = "Adminstrator";
        when(rr.getByName(name)).thenReturn(null);

        roleService.getByName(name);
        verify(rr, atLeastOnce()).getByName(name);
    }

    @Test(expected = NotFoundException.class)
    public void getRoleByIdNotFound() throws NotFoundException, InvalidContentException {
        int id = 543;
        when(rr.getById(id)).thenReturn(null);

        roleService.getById(id);
        verify(rr, atLeastOnce()).getById(id);
    }

    @Test
    public void createRole() throws InvalidContentException, NameNotUniqueException, CreationFailedException {
        String name = "Tweet";
        Role role = new Role(name);
        role.setId(1);

        when(rr.getById(role.getId())).thenReturn(null);
        when(rr.getByName(role.getName())).thenReturn(null);

        roleService.create(role);
        verify(rr, atLeastOnce()).create(role);
    }

    @Test(expected = NameNotUniqueException.class)
    public void createRoleDuplicate() throws InvalidContentException, NameNotUniqueException, CreationFailedException {
        String name = "Tweet";
        Role role = new Role(name);
        Role role2 = new Role(name);
        role.setId(1);
        role2.setId(2);

        when(rr.getById(role.getId())).thenReturn(role);
        when(rr.getByName(role.getName())).thenReturn(role);

        when(rr.getById(role2.getId())).thenReturn(null);
        when(rr.getByName(role2.getName())).thenReturn(null);

        roleService.create(role2);
        verify(rr, atLeastOnce()).create(role);
    }

    @Test
    public void updateRole() throws InvalidContentException, NameNotUniqueException, NotFoundException {
        int id = 6;
        String name = "a new name";
        Role role = new Role("member");
        role.setId(id);
        when(rr.getById(id)).thenReturn(role);
        when(rr.getByName("member")).thenReturn(role);

        role.setName(name);

        roleService.update(role);
        verify(rr, atLeastOnce()).update(role);
    }

    @Test(expected = NameNotUniqueException.class)
    public void updateRoleDuplicateName() throws InvalidContentException, NameNotUniqueException, NotFoundException {
        /**
         * Role 1
         */
        int id = 1;
        Role role = new Role("Member");
        role.setId(id);
        when(rr.getById(id)).thenReturn(role);
        when(rr.getByName("Member")).thenReturn(role);

        /**
         * Role 2
         */
        int id2 = 2643;
        Role role2 = new Role("Administrator");
        role2.setId(id2);
        when(rr.getById(id2)).thenReturn(role2);
        when(rr.getByName("Administrator")).thenReturn(role2);

        role2.setName("Member");

        roleService.update(role2);
        verify(rr, atLeastOnce()).update(role2);
    }

    @Test
    public void deleteRole() throws InvalidContentException, NotFoundException {
        int id = 6;
        Role role = new Role("member");
        role.setId(id);
        when(rr.getById(id)).thenReturn(role);
        when(rr.getByName("member")).thenReturn(role);
        roleService.delete(role);
        verify(rr, atLeastOnce()).delete(role);
    }
}
