package service;

import domain.Permission;
import domain.Role;
import exceptions.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import repository.collection.PermissionServiceCollImpl;
import repository.collection.RoleServiceCollImpl;
import repository.interfaces.RoleRepository;
import repository.jpa.RoleServiceJPAImpl;

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

    @Test
    public void getRoleById() throws NotFoundException, InvalidContentException {
        int id = 6;
        Role role = mock(Role.class);
        role.setId(id);
        when(rr.getById(id)).thenReturn(role);

        roleService.getById(id);
        verify(rr, atLeastOnce()).getById(id);
    }

    @Test
    public void getRoleByName() throws InvalidContentException, NotFoundException {
        String name = "Adminstrator";
        Role role = mock(Role.class);
        role.setName(name);
        when(rr.getByName(name)).thenReturn(role);

        roleService.getByName(name);
        verify(rr, atLeastOnce()).getByName(name);
    }

    @Test(expected = NotFoundException.class)
    public void getRoleByNameNotFound() throws NotFoundException, InvalidContentException {
        String name = "Adminstrator";
        when(rr.getByName(name)).thenReturn(null);

        roleService.getByName(name);
        verify(rr, never()).getByName(name);
    }

    @Test(expected = NotFoundException.class)
    public void getRoleByIdNotFound() throws NotFoundException, InvalidContentException {
        int id = 543;
        when(rr.getById(id)).thenReturn(null);

        roleService.getById(id);
        verify(rr, never()).getById(id);
    }

    @Test
    public void createRole() throws InvalidContentException, NameNotUniqueException, CreationFailedException, NotFoundException {
        String name = "Tweet";
        Role role = new Role(name);
        role.setId(1);

        when(rr.create(role)).thenReturn(role);

        roleService.create(role);
        verify(rr, atLeastOnce()).create(role);
    }

    @Test(expected = NameNotUniqueException.class)
    public void createRoleDuplicate() throws InvalidContentException, NameNotUniqueException, CreationFailedException, NotFoundException {
        String name = "Tweet";
        Role role = new Role(name);
        Role role2 = new Role(name);
        role.setId(1);
        role2.setId(2);

        when(rr.getByName(role.getName())).thenReturn(role);

        when(rr.getByName(role2.getName())).thenReturn(role);

        roleService.create(role2);
        verify(rr, never()).create(role);
    }

    @Test
    public void updateRole() throws InvalidContentException, NameNotUniqueException, NotFoundException {
        int id = 6;
        String name = "a new name";
        Role role = new Role("member");
        role.setId(id);
        when(rr.getById(id)).thenReturn(role);

        role.setName(name);

        when(rr.update(role)).thenReturn(role);

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
        when(rr.getByName("Member")).thenReturn(role);

        /**
         * Role 2
         */
        int id2 = 2643;
        Role role2 = new Role("Administrator");
        role2.setId(id2);
        when(rr.getById(id2)).thenReturn(role2);
        role2.setName("Member");

        roleService.update(role2);
        verify(rr, never()).update(role2);
    }

    @Test
    public void deleteRole() throws InvalidContentException, NotFoundException {
        int id = 6;
        Role role = new Role("member");
        role.setId(id);
        when(rr.getById(id)).thenReturn(role);

        when(rr.delete(role)).thenReturn(true);

        roleService.delete(role);
        verify(rr, atLeastOnce()).delete(role);
    }

    @Test
    public void addPermission() throws InvalidContentException, ActionForbiddenException, NotFoundException {
        // Role
        int roleId = 543;
        Role role = new Role();
        role.setId(roleId);

        // Permission
        int permissionId = 9373;
        Permission permission = new Permission();
        permission.setId(permissionId);

        when(rr.getById(roleId)).thenReturn(role);
        when(pr.getById(permissionId).getObject()).thenReturn(permission);
        when(rr.addPermission(role, permission)).thenReturn(role);

        roleService.addPermission(role, permission);
        verify(rr, atLeastOnce()).addPermission(role, permission);
    }

    @Test(expected = ActionForbiddenException.class)
    public void addPermissionDuplicate() throws InvalidContentException, ActionForbiddenException, NotFoundException {
        // Role
        int roleId = 543;
        Role role = new Role();
        role.setId(roleId);

        // Permission
        int permissionId = 9373;
        Permission permission = new Permission();
        permission.setId(permissionId);

        role.addPermission(permission);

        when(rr.getById(roleId)).thenReturn(role);
        when(pr.getById(permissionId).getObject()).thenReturn(permission);

        roleService.addPermission(role, permission);
        verify(rr, never()).addPermission(role, permission);
    }

    @Test
    public void removePermission() throws InvalidContentException, ActionForbiddenException, NotFoundException {
        // Role
        int roleId = 543;
        Role role = new Role();
        role.setId(roleId);

        // Permission
        int permissionId = 9373;
        Permission permission = new Permission();
        permission.setId(permissionId);

        role.addPermission(permission);

        when(rr.getById(roleId)).thenReturn(role);
        when(pr.getById(permissionId).getObject()).thenReturn(permission);
        when(rr.removePermission(role, permission)).thenReturn(role);

        roleService.removePermission(role, permission);
        verify(rr, atLeastOnce()).removePermission(role, permission);
    }

    @Test(expected = ActionForbiddenException.class)
    public void removePermissionThatIsNotAdded() throws InvalidContentException, ActionForbiddenException, NotFoundException {
        // Role
        int roleId = 543;
        Role role = new Role();
        role.setId(roleId);

        // Permission
        int permissionId = 9373;
        Permission permission = new Permission();
        permission.setId(permissionId);

        when(rr.getById(roleId)).thenReturn(role);
        when(pr.getById(permissionId).getObject()).thenReturn(permission);

        roleService.removePermission(role, permission);
        verify(rr, never()).removePermission(role, permission);
    }
}
