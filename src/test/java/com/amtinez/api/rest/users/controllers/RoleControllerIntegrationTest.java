package com.amtinez.api.rest.users.controllers;

import com.amtinez.api.rest.users.annotations.WithMockUser;
import com.amtinez.api.rest.users.constants.ConfigurationConstants.Profiles;
import com.amtinez.api.rest.users.dtos.Role;
import com.amtinez.api.rest.users.models.RoleModel;
import com.amtinez.api.rest.users.services.RoleService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

import static com.amtinez.api.rest.users.constants.SecurityConstants.ROLE_ADMIN;

/**
 * Integration test for {@link RoleController}
 *
 * @author Alejandro Martínez Cerro <amartinezcerro @ gmail.com>
 */
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles(Profiles.TEST)
@Transactional
public class RoleControllerIntegrationTest {

    private static final String ROLE_CONTROLLER_URL = "/roles";

    private static final String TEST_ROLE_NAME = "testRoleName";
    private static final String TEST_NOT_EXISTS_AUTHORITY_NAME = "NOT_EXISTS";

    @Resource
    private MockMvc mockMvc;
    @Resource
    private RoleService roleService;

    private RoleModel testRole;

    @BeforeEach
    public void setUp() {
        testRole = roleService.saveRole(RoleModel.builder()
                                                 .name(TEST_ROLE_NAME)
                                                 .build());
    }

    @Test
    @WithMockUser(authorities = ROLE_ADMIN)
    public void testFindAllRoles() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(ROLE_CONTROLLER_URL))
               .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @WithMockUser
    public void testFindAllRolesForbidden() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(ROLE_CONTROLLER_URL))
               .andExpect(MockMvcResultMatchers.status().isForbidden());
    }

    @Test
    public void testFindAllRolesUnauthorized() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(ROLE_CONTROLLER_URL))
               .andExpect(MockMvcResultMatchers.status().isUnauthorized());
    }

    @Test
    @WithMockUser(authorities = ROLE_ADMIN)
    public void testRegisterRole() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post(ROLE_CONTROLLER_URL)
                                              .contentType(MediaType.APPLICATION_JSON)
                                              .content(createRole(null)))
               .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @WithMockUser
    public void testRegisterRoleForbidden() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post(ROLE_CONTROLLER_URL)
                                              .contentType(MediaType.APPLICATION_JSON)
                                              .content(createRole(null)))
               .andExpect(MockMvcResultMatchers.status().isForbidden());
    }

    @Test
    public void testRegisterRoleUnauthorized() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post(ROLE_CONTROLLER_URL)
                                              .contentType(MediaType.APPLICATION_JSON)
                                              .content(createRole(null)))
               .andExpect(MockMvcResultMatchers.status().isUnauthorized());
    }

    @Test
    @WithMockUser(authorities = ROLE_ADMIN)
    public void testUpdateRole() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.put(ROLE_CONTROLLER_URL)
                                              .contentType(MediaType.APPLICATION_JSON)
                                              .content(createRole(testRole.getId())))
               .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @WithMockUser(authorities = ROLE_ADMIN)
    public void testUpdateRoleNotExists() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.put(ROLE_CONTROLLER_URL)
                                              .contentType(MediaType.APPLICATION_JSON)
                                              .content(createRole(Long.MAX_VALUE)))
               .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    @WithMockUser
    public void testUpdateRoleForbidden() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.put(ROLE_CONTROLLER_URL)
                                              .contentType(MediaType.APPLICATION_JSON)
                                              .content(createRole(testRole.getId())))
               .andExpect(MockMvcResultMatchers.status().isForbidden());
    }

    @Test
    public void testUpdateRoleUnauthorized() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.put(ROLE_CONTROLLER_URL)
                                              .contentType(MediaType.APPLICATION_JSON)
                                              .content(createRole(testRole.getId())))
               .andExpect(MockMvcResultMatchers.status().isUnauthorized());
    }

    @Test
    @WithMockUser(authorities = ROLE_ADMIN)
    public void testFindRole() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(ROLE_CONTROLLER_URL + "/" + testRole.getId()))
               .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @WithMockUser(authorities = ROLE_ADMIN)
    public void testFindRoleNotExists() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(ROLE_CONTROLLER_URL + "/" + Long.MAX_VALUE))
               .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    @WithMockUser(authorities = TEST_NOT_EXISTS_AUTHORITY_NAME)
    public void testFindRoleForbidden() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(ROLE_CONTROLLER_URL + "/" + testRole.getId()))
               .andExpect(MockMvcResultMatchers.status().isForbidden());
    }

    @Test
    public void testFindRoleUnauthorized() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(ROLE_CONTROLLER_URL + "/" + testRole.getId()))
               .andExpect(MockMvcResultMatchers.status().isUnauthorized());
    }

    @Test
    @WithMockUser(authorities = ROLE_ADMIN)
    public void testDeleteRole() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete(ROLE_CONTROLLER_URL + "/" + testRole.getId()))
               .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @WithMockUser(authorities = ROLE_ADMIN)
    public void testDeleteRoleNotExists() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete(ROLE_CONTROLLER_URL + "/" + Long.MAX_VALUE))
               .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    @WithMockUser
    public void testDeleteRoleForbidden() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete(ROLE_CONTROLLER_URL + "/" + testRole.getId()))
               .andExpect(MockMvcResultMatchers.status().isForbidden());
    }

    @Test
    public void testDeleteRoleUnauthorized() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete(ROLE_CONTROLLER_URL + "/" + testRole.getId()))
               .andExpect(MockMvcResultMatchers.status().isUnauthorized());
    }

    private String createRole(final Long id) throws JsonProcessingException {
        final Role role = Role.builder()
                              .id(id)
                              .name(TEST_ROLE_NAME)
                              .build();
        return new ObjectMapper().writeValueAsString(role);
    }

}
