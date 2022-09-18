package com.amtinez.api.rest.users.controllers;

import com.amtinez.api.rest.users.annotations.WithMockUser;
import com.amtinez.api.rest.users.constants.ValidationConstants.Fields;
import com.amtinez.api.rest.users.constants.ValidationConstants.Fields.Values;
import com.amtinez.api.rest.users.constants.ValidationConstants.Messages;
import com.amtinez.api.rest.users.dtos.Role;
import com.amtinez.api.rest.users.models.RoleModel;
import com.amtinez.api.rest.users.services.RoleService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.context.MessageSourceAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

import static com.amtinez.api.rest.users.constants.SecurityConstants.ROLE_ADMIN;
import static com.amtinez.api.rest.users.enums.Role.ADMIN;
import static com.amtinez.api.rest.users.enums.Role.USER;

/**
 * Integration test for {@link RoleController}
 *
 * @author Alejandro Martínez Cerro <amartinezcerro @ gmail.com>
 */
@Transactional
@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ImportAutoConfiguration(MessageSourceAutoConfiguration.class)
class RoleControllerIntegrationTest implements ControllerIntegrationTest {

    private static final String ROLE_CONTROLLER_URL = "/roles";
    private static final String TEST_NOT_EXISTS_AUTHORITY_NAME = "NOT_EXISTS";

    @Resource
    private MockMvc mockMvc;
    @Resource
    private RoleService roleService;

    private RoleModel testRole;

    @BeforeAll
    public void setUp() {
        testRole = roleService.saveRole(RoleModel.builder()
                                                 .name(USER.name())
                                                 .build());
    }

    @AfterAll
    public void cleanUp() {
        roleService.deleteRole(testRole.getId());
    }

    @Test
    @WithMockUser(authorities = ROLE_ADMIN)
    void testFindAllRoles() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(ROLE_CONTROLLER_URL))
               .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @WithMockUser
    void testFindAllRolesForbidden() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(ROLE_CONTROLLER_URL))
               .andExpect(MockMvcResultMatchers.status().isForbidden());
    }

    @Test
    void testFindAllRolesUnauthorized() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(ROLE_CONTROLLER_URL))
               .andExpect(MockMvcResultMatchers.status().isUnauthorized());
    }

    @Test
    @WithMockUser(authorities = ROLE_ADMIN)
    void testCreateRole() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post(ROLE_CONTROLLER_URL)
                                              .contentType(MediaType.APPLICATION_JSON)
                                              .content(createRole(null))
                                              .with(SecurityMockMvcRequestPostProcessors.csrf()))
               .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @WithMockUser(authorities = ROLE_ADMIN)
    void testCreateRoleEmptyName() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post(ROLE_CONTROLLER_URL)
                                              .contentType(MediaType.APPLICATION_JSON)
                                              .content(getJson(Role.builder()
                                                                   .build()))
                                              .with(SecurityMockMvcRequestPostProcessors.csrf()))
               .andExpect(MockMvcResultMatchers.status().isBadRequest())
               .andExpect(MockMvcResultMatchers.content().json(createFieldError(Fields.NAME, Messages.BLANK)));
    }

    @Test
    @WithMockUser(authorities = ROLE_ADMIN)
    void testCreateRoleOverSizeName() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post(ROLE_CONTROLLER_URL)
                                              .contentType(MediaType.APPLICATION_JSON)
                                              .content(getJson(Role.builder()
                                                                   .name(Values.ONE_LETTER.repeat(51))
                                                                   .build()))
                                              .with(SecurityMockMvcRequestPostProcessors.csrf()))
               .andExpect(MockMvcResultMatchers.status().isBadRequest())
               .andExpect(MockMvcResultMatchers.content().json(createFieldError(Fields.NAME, Messages.OVERSIZE_50)));
    }

    @Test
    @WithMockUser(authorities = ROLE_ADMIN)
    void testCreateRoleExistName() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post(ROLE_CONTROLLER_URL)
                                              .contentType(MediaType.APPLICATION_JSON)
                                              .content(getJson(Role.builder()
                                                                   .name(USER.name())
                                                                   .build()))
                                              .with(SecurityMockMvcRequestPostProcessors.csrf()))
               .andExpect(MockMvcResultMatchers.status().isBadRequest())
               .andExpect(MockMvcResultMatchers.content().json(createFieldError(Fields.NAME, Messages.EXISTS_ROLE)));
    }

    @Test
    @WithMockUser
    void testCreateRoleForbidden() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post(ROLE_CONTROLLER_URL)
                                              .contentType(MediaType.APPLICATION_JSON)
                                              .content(createRole(null))
                                              .with(SecurityMockMvcRequestPostProcessors.csrf()))
               .andExpect(MockMvcResultMatchers.status().isForbidden());
    }

    @Test
    void testCreateRoleUnauthorized() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post(ROLE_CONTROLLER_URL)
                                              .contentType(MediaType.APPLICATION_JSON)
                                              .content(createRole(null))
                                              .with(SecurityMockMvcRequestPostProcessors.csrf()))
               .andExpect(MockMvcResultMatchers.status().isUnauthorized());
    }

    @Test
    @WithMockUser(authorities = ROLE_ADMIN)
    void testUpdateRole() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.put(ROLE_CONTROLLER_URL)
                                              .contentType(MediaType.APPLICATION_JSON)
                                              .content(createRole(testRole.getId()))
                                              .with(SecurityMockMvcRequestPostProcessors.csrf()))
               .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @WithMockUser(authorities = ROLE_ADMIN)
    void testUpdateRoleNotExists() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.put(ROLE_CONTROLLER_URL)
                                              .contentType(MediaType.APPLICATION_JSON)
                                              .content(createRole(Long.MAX_VALUE))
                                              .with(SecurityMockMvcRequestPostProcessors.csrf()))
               .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    @WithMockUser
    void testUpdateRoleForbidden() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.put(ROLE_CONTROLLER_URL)
                                              .contentType(MediaType.APPLICATION_JSON)
                                              .content(createRole(testRole.getId()))
                                              .with(SecurityMockMvcRequestPostProcessors.csrf()))
               .andExpect(MockMvcResultMatchers.status().isForbidden());
    }

    @Test
    void testUpdateRoleUnauthorized() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.put(ROLE_CONTROLLER_URL)
                                              .contentType(MediaType.APPLICATION_JSON)
                                              .content(createRole(testRole.getId()))
                                              .with(SecurityMockMvcRequestPostProcessors.csrf()))
               .andExpect(MockMvcResultMatchers.status().isUnauthorized());
    }

    @Test
    @WithMockUser(authorities = ROLE_ADMIN)
    void testUpdateRoleNullId() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.put(ROLE_CONTROLLER_URL)
                                              .contentType(MediaType.APPLICATION_JSON)
                                              .content(getJson(Role.builder()
                                                                   .name(ADMIN.name())
                                                                   .build()))
                                              .with(SecurityMockMvcRequestPostProcessors.csrf()))
               .andExpect(MockMvcResultMatchers.status().isBadRequest())
               .andExpect(MockMvcResultMatchers.content().json(createFieldError(Fields.ID, Messages.NULL)));
    }

    @Test
    @WithMockUser(authorities = ROLE_ADMIN)
    void testFindRole() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(ROLE_CONTROLLER_URL + "/" + testRole.getId()))
               .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @WithMockUser(authorities = ROLE_ADMIN)
    void testFindRoleNotExists() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(ROLE_CONTROLLER_URL + "/" + Long.MAX_VALUE))
               .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    @WithMockUser(authorities = TEST_NOT_EXISTS_AUTHORITY_NAME)
    void testFindRoleForbidden() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(ROLE_CONTROLLER_URL + "/" + testRole.getId()))
               .andExpect(MockMvcResultMatchers.status().isForbidden());
    }

    @Test
    void testFindRoleUnauthorized() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(ROLE_CONTROLLER_URL + "/" + testRole.getId()))
               .andExpect(MockMvcResultMatchers.status().isUnauthorized());
    }

    @Test
    @WithMockUser(authorities = ROLE_ADMIN)
    void testDeleteRole() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete(ROLE_CONTROLLER_URL + "/" + testRole.getId())
                                              .with(SecurityMockMvcRequestPostProcessors.csrf()))
               .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @WithMockUser(authorities = ROLE_ADMIN)
    void testDeleteRoleNotExists() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete(ROLE_CONTROLLER_URL + "/" + Long.MAX_VALUE)
                                              .with(SecurityMockMvcRequestPostProcessors.csrf()))
               .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    @WithMockUser
    void testDeleteRoleForbidden() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete(ROLE_CONTROLLER_URL + "/" + testRole.getId())
                                              .with(SecurityMockMvcRequestPostProcessors.csrf()))
               .andExpect(MockMvcResultMatchers.status().isForbidden());
    }

    @Test
    void testDeleteRoleUnauthorized() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete(ROLE_CONTROLLER_URL + "/" + testRole.getId())
                                              .with(SecurityMockMvcRequestPostProcessors.csrf()))
               .andExpect(MockMvcResultMatchers.status().isUnauthorized());
    }

    private String createRole(final Long id) throws JsonProcessingException {
        return getJson(Role.builder()
                           .id(id)
                           .name(ADMIN.name())
                           .build());
    }

}
