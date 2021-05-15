package com.amtinez.api.rest.users.controllers;

import com.amtinez.api.rest.users.annotations.WithMockAdminUser;
import com.amtinez.api.rest.users.constants.ConfigurationConstants.Profiles;
import com.amtinez.api.rest.users.dtos.Role;
import com.amtinez.api.rest.users.dtos.User;
import com.amtinez.api.rest.users.models.RoleModel;
import com.amtinez.api.rest.users.models.UserModel;
import com.amtinez.api.rest.users.services.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Collections;

import javax.annotation.Resource;

/**
 * @author Alejandro Martínez Cerro <amartinezcerro @ gmail.com>
 */
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles(Profiles.TEST)
@Transactional
public class UserControllerIntegrationTest {

    private static final String USER_CONTROLLER_URL = "/users";

    private static final String TEST_USER_FIRST_NAME = "testUserFirstName";
    private static final String TEST_USER_LAST_NAME = "testUserLastName";
    private static final String TEST_USER_EMAIL = "test@user.com";
    private static final String TEST_USER_PASSWORD = "testUserPassword";

    private static final String TEST_USER_REGISTER_FIRST_NAME = "testUserRegisterFirstName";
    private static final String TEST_USER_REGISTER_LAST_NAME = "testUserRegisterLastName";
    private static final String TEST_USER_REGISTER_EMAIL = "user@register.com";
    private static final String TEST_USER_REGISTER_PASSWORD = "testUserRegisterPassword";

    private static final String TEST_ROLE_NAME = "testRoleName";
    private static final String TEST_NOT_EXISTS_ROLE_NAME = "NOT_EXISTS";

    @Resource
    private MockMvc mockMvc;
    @Resource
    private UserService userService;

    private UserModel testUser;

    @BeforeEach
    public void setUp() {
        testUser = userService.saveUser(UserModel.builder()
                                                 .firstName(TEST_USER_FIRST_NAME)
                                                 .lastName(TEST_USER_LAST_NAME)
                                                 .email(TEST_USER_EMAIL)
                                                 .password(TEST_USER_PASSWORD)
                                                 .birthdayDate(LocalDate.now())
                                                 .lastAccessDate(LocalDate.now())
                                                 .lastPasswordUpdateDate(LocalDate.now())
                                                 .enabled(Boolean.TRUE)
                                                 .locked(Boolean.FALSE)
                                                 .roles(Collections.singleton(RoleModel.builder()
                                                                                       .name(TEST_ROLE_NAME)
                                                                                       .build()))
                                                 .build());
    }

    @Test
    @WithMockAdminUser
    public void testFindAllUsers() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(USER_CONTROLLER_URL))
               .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @WithMockUser
    public void testFindAllUsersForbidden() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(USER_CONTROLLER_URL))
               .andExpect(MockMvcResultMatchers.status().isForbidden());
    }

    @Test
    public void testFindAllUsersUnauthorized() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(USER_CONTROLLER_URL))
               .andExpect(MockMvcResultMatchers.status().isUnauthorized());
    }

    @Test
    public void testRegisterUser() throws Exception {
        final User user = User.builder()
                              .firstName(TEST_USER_REGISTER_FIRST_NAME)
                              .lastName(TEST_USER_REGISTER_LAST_NAME)
                              .email(TEST_USER_REGISTER_EMAIL)
                              .password(TEST_USER_REGISTER_PASSWORD)
                              .birthdayDate(LocalDate.now())
                              .roles(Collections.singleton(Role.builder()
                                                               .name(TEST_ROLE_NAME)
                                                               .build()))
                              .build();
        final ObjectMapper mapper = new ObjectMapper();
        mockMvc.perform(MockMvcRequestBuilders.post(USER_CONTROLLER_URL)
                                              .contentType(MediaType.APPLICATION_JSON)
                                              .content(mapper.writeValueAsString(user)))
               .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void testRegisterUserWithInvalidUser() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post(USER_CONTROLLER_URL))
               .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    @WithMockAdminUser
    public void testFindUser() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(USER_CONTROLLER_URL + "/" + testUser.getId()))
               .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @WithMockAdminUser
    public void testFindUserNotExists() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(USER_CONTROLLER_URL + "/" + Long.MAX_VALUE))
               .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    @WithMockUser(roles = TEST_NOT_EXISTS_ROLE_NAME)
    public void testFindUserForbidden() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(USER_CONTROLLER_URL + "/" + testUser.getId()))
               .andExpect(MockMvcResultMatchers.status().isForbidden());
    }

    @Test
    public void testFindUserUnauthorized() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(USER_CONTROLLER_URL + "/" + testUser.getId()))
               .andExpect(MockMvcResultMatchers.status().isUnauthorized());
    }

    @Test
    @WithMockAdminUser
    public void testDeleteUser() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete(USER_CONTROLLER_URL + "/" + testUser.getId()))
               .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @WithMockAdminUser
    public void testDeleteUserNotExists() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete(USER_CONTROLLER_URL + "/" + Long.MAX_VALUE))
               .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    @WithMockUser
    public void testDeleteUserForbidden() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete(USER_CONTROLLER_URL + "/" + testUser.getId()))
               .andExpect(MockMvcResultMatchers.status().isForbidden());
    }

    @Test
    public void testDeleteUserUnauthorized() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete(USER_CONTROLLER_URL + "/" + testUser.getId()))
               .andExpect(MockMvcResultMatchers.status().isUnauthorized());
    }

    @Test
    @WithMockAdminUser
    public void testEnableUser() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.patch(USER_CONTROLLER_URL + "/" + testUser.getId() + "/enable"))
               .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @WithMockAdminUser
    public void testEnableNotExists() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.patch(USER_CONTROLLER_URL + "/" + Long.MAX_VALUE + "/enable"))
               .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    @WithMockUser
    public void testEnableUserForbidden() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.patch(USER_CONTROLLER_URL + "/" + testUser.getId() + "/enable"))
               .andExpect(MockMvcResultMatchers.status().isForbidden());
    }

    @Test
    public void testEnableUserUnauthorized() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.patch(USER_CONTROLLER_URL + "/" + testUser.getId() + "/enable"))
               .andExpect(MockMvcResultMatchers.status().isUnauthorized());
    }

    @Test
    @WithMockAdminUser
    public void testDisableUser() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.patch(USER_CONTROLLER_URL + "/" + testUser.getId() + "/disable"))
               .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @WithMockAdminUser
    public void testDisableNotExists() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.patch(USER_CONTROLLER_URL + "/" + Long.MAX_VALUE + "/disable"))
               .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    @WithMockUser
    public void testDisableUserForbidden() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.patch(USER_CONTROLLER_URL + "/" + testUser.getId() + "/disable"))
               .andExpect(MockMvcResultMatchers.status().isForbidden());
    }

    @Test
    public void testDisableUserUnauthorized() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.patch(USER_CONTROLLER_URL + "/" + testUser.getId() + "/disable"))
               .andExpect(MockMvcResultMatchers.status().isUnauthorized());
    }

    @Test
    @WithMockAdminUser
    public void testLockUser() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.patch(USER_CONTROLLER_URL + "/" + testUser.getId() + "/lock"))
               .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @WithMockAdminUser
    public void testLockNotExists() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.patch(USER_CONTROLLER_URL + "/" + Long.MAX_VALUE + "/lock"))
               .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    @WithMockUser
    public void testLockUserForbidden() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.patch(USER_CONTROLLER_URL + "/" + testUser.getId() + "/lock"))
               .andExpect(MockMvcResultMatchers.status().isForbidden());
    }

    @Test
    public void testLockUserUnauthorized() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.patch(USER_CONTROLLER_URL + "/" + testUser.getId() + "/lock"))
               .andExpect(MockMvcResultMatchers.status().isUnauthorized());
    }

    @Test
    @WithMockAdminUser
    public void testUnlockUser() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.patch(USER_CONTROLLER_URL + "/" + testUser.getId() + "/unlock"))
               .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @WithMockAdminUser
    public void testUnlockNotExists() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.patch(USER_CONTROLLER_URL + "/" + Long.MAX_VALUE + "/unlock"))
               .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    @WithMockUser
    public void testUnlockUserForbidden() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.patch(USER_CONTROLLER_URL + "/" + testUser.getId() + "/unlock"))
               .andExpect(MockMvcResultMatchers.status().isForbidden());
    }

    @Test
    public void testUnlockUserUnauthorized() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.patch(USER_CONTROLLER_URL + "/" + testUser.getId() + "/unlock"))
               .andExpect(MockMvcResultMatchers.status().isUnauthorized());
    }

}
