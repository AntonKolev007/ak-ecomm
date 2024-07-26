package com.ecommerce.project.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class RoleTest {

    @Test
    public void testRoleDefaultConstructor() {
        Role role = new Role();
        assertNull(role.getRoleId());
        assertNull(role.getRoleName());
    }

    @Test
    public void testRoleConstructorWithRoleName() {
        Role role = new Role(AppRole.ROLE_ADMIN);

        assertNull(role.getRoleId());
        assertEquals(AppRole.ROLE_ADMIN, role.getRoleName());
    }

    @Test
    public void testRoleParameterizedConstructor() {
        Role role = new Role(1L, AppRole.ROLE_USER);

        assertEquals(1L, role.getRoleId());
        assertEquals(AppRole.ROLE_USER, role.getRoleName());
    }

    @Test
    public void testSettersAndGetters() {
        Role role = new Role();

        role.setRoleId(1L);
        assertEquals(1L, role.getRoleId());

        role.setRoleName(AppRole.ROLE_USER);
        assertEquals(AppRole.ROLE_USER, role.getRoleName());
    }

    @Test
    public void testToString() {
        Role role = new Role();
        role.setRoleId(1L);

        String expected = "Role{roleId=1}";
        assertEquals(expected, role.toString());
    }
}
