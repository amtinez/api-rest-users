package com.amtinez.api.rest.users.controllers;

import com.amtinez.api.rest.users.annotations.WithMockUser;
import com.amtinez.api.rest.users.constants.ConfigurationConstants.Profiles;
import com.amtinez.api.rest.users.dtos.Role;
import com.amtinez.api.rest.users.dtos.User;
import com.amtinez.api.rest.users.facades.UserFacade;
import com.amtinez.api.rest.users.models.RoleModel;
import com.amtinez.api.rest.users.models.UserModel;
import com.amtinez.api.rest.users.models.UserVerificationTokenModel;
import com.amtinez.api.rest.users.security.impl.UserDetailsImpl;
import com.amtinez.api.rest.users.services.TokenService;
import com.amtinez.api.rest.users.services.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.annotation.Resource;

import static com.amtinez.api.rest.users.constants.SecurityConstants.ROLE_ADMIN;

/**
 * Integration test for {@link UserController}
 *
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

    private static final String TEST_USER_FIRST_NAME_UPDATED = "testUserFirstNameUpdated";

    private static final String TEST_USER_REGISTER_FIRST_NAME = "testUserRegisterFirstName";
    private static final String TEST_USER_REGISTER_LAST_NAME = "testUserRegisterLastName";
    private static final String TEST_USER_REGISTER_EMAIL = "user@register.com";
    private static final String TEST_USER_REGISTER_PASSWORD = "testUserRegisterPassword";

    private static final String TEST_ROLE_NAME = "testRoleName";
    private static final String TEST_NOT_EXISTS_ROLE_NAME = "NOT_EXISTS";

    @Resource
    private MockMvc mockMvc;
    @Resource
    private UserFacade userFacade;
    @Resource
    private UserService userService;
    @Resource
    private TokenService<UserVerificationTokenModel> userVerificationTokenService;

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
                                                 .roles(Stream.of(RoleModel.builder()
                                                                           .name(TEST_ROLE_NAME)
                                                                           .build())
                                                              .collect(Collectors.toSet()))
                                                 .build());
    }

    @Test
    @WithMockUser(authorities = ROLE_ADMIN)
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
        final User user = createUser();
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
    public void testConfirmRegisterUser() throws Exception {
        final User registeredUser = userFacade.registerUser(createUser());
        final Optional<UserVerificationTokenModel> userVerificationTokenModel = userVerificationTokenService.findToken(UserModel.builder()
                                                                                                                                .id(registeredUser.getId())
                                                                                                                                .build());
        mockMvc.perform(MockMvcRequestBuilders.get(USER_CONTROLLER_URL
                                                       + "/confirm/"
                                                       + userVerificationTokenModel.map(UserVerificationTokenModel::getCode)
                                                                                   .orElse(StringUtils.EMPTY)))
               .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void testConfirmRegisterUserExpiredToken() throws Exception {
        final User registeredUser = userFacade.registerUser(createUser());
        final Optional<UserVerificationTokenModel> userVerificationTokenModel = userVerificationTokenService.findToken(UserModel.builder()
                                                                                                                                .id(registeredUser.getId())
                                                                                                                                .build());
        userVerificationTokenModel.ifPresent(tokenModel -> {
            tokenModel.setExpiryDate(LocalDate.now().minusDays(1));
            userVerificationTokenService.saveToken(tokenModel);
        });
        mockMvc.perform(MockMvcRequestBuilders.get(USER_CONTROLLER_URL
                                                       + "/confirm/"
                                                       + userVerificationTokenModel.map(UserVerificationTokenModel::getCode)
                                                                                   .orElse(StringUtils.EMPTY)))
               .andExpect(MockMvcResultMatchers.status().isUnauthorized());
    }

    @Test
    @WithMockUser
    public void testUpdateUser() throws Exception {
        setLoggedInUserId(testUser.getId());
        final User user = User.builder()
                              .id(testUser.getId())
                              .firstName(TEST_USER_FIRST_NAME_UPDATED)
                              .build();
        final ObjectMapper mapper = new ObjectMapper();
        mockMvc.perform(MockMvcRequestBuilders.put(USER_CONTROLLER_URL)
                                              .contentType(MediaType.APPLICATION_JSON)
                                              .content(mapper.writeValueAsString(user)))
               .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @WithMockUser(authorities = ROLE_ADMIN)
    public void testUpdateUserNotExists() throws Exception {
        final User user = User.builder()
                              .id(Long.MAX_VALUE)
                              .build();
        final ObjectMapper mapper = new ObjectMapper();
        mockMvc.perform(MockMvcRequestBuilders.put(USER_CONTROLLER_URL)
                                              .contentType(MediaType.APPLICATION_JSON)
                                              .content(mapper.writeValueAsString(user)))
               .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    @WithMockUser(authorities = ROLE_ADMIN)
    public void testUpdateUserAdminLoggedInUser() throws Exception {
        setLoggedInUserId(Long.MAX_VALUE);
        final User user = User.builder()
                              .id(testUser.getId())
                              .build();
        final ObjectMapper mapper = new ObjectMapper();
        mockMvc.perform(MockMvcRequestBuilders.put(USER_CONTROLLER_URL)
                                              .contentType(MediaType.APPLICATION_JSON)
                                              .content(mapper.writeValueAsString(user)))
               .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @WithMockUser
    public void testUpdateUserOtherLoggedInUser() throws Exception {
        setLoggedInUserId(Long.MAX_VALUE);
        final User user = User.builder()
                              .id(testUser.getId())
                              .build();
        final ObjectMapper mapper = new ObjectMapper();
        mockMvc.perform(MockMvcRequestBuilders.put(USER_CONTROLLER_URL)
                                              .contentType(MediaType.APPLICATION_JSON)
                                              .content(mapper.writeValueAsString(user)))
               .andExpect(MockMvcResultMatchers.status().isForbidden());
    }

    @Test
    @WithMockUser(authorities = ROLE_ADMIN)
    public void testUpdateUserWithInvalidUser() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.put(USER_CONTROLLER_URL))
               .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    @WithMockUser(authorities = TEST_NOT_EXISTS_ROLE_NAME)
    public void testUpdateUserForbidden() throws Exception {
        final ObjectMapper mapper = new ObjectMapper();
        mockMvc.perform(MockMvcRequestBuilders.put(USER_CONTROLLER_URL)
                                              .contentType(MediaType.APPLICATION_JSON)
                                              .content(mapper.writeValueAsString(User.builder()
                                                                                     .id(testUser.getId())
                                                                                     .build())))
               .andExpect(MockMvcResultMatchers.status().isForbidden());
    }

    @Test
    public void testUpdateUserUnauthorized() throws Exception {
        final ObjectMapper mapper = new ObjectMapper();
        mockMvc.perform(MockMvcRequestBuilders.put(USER_CONTROLLER_URL)
                                              .contentType(MediaType.APPLICATION_JSON)
                                              .content(mapper.writeValueAsString(User.builder()
                                                                                     .id(testUser.getId())
                                                                                     .build())))
               .andExpect(MockMvcResultMatchers.status().isUnauthorized());
    }

    @Test
    @WithMockUser(authorities = ROLE_ADMIN)
    public void testFindUser() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(USER_CONTROLLER_URL + "/" + testUser.getId()))
               .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @WithMockUser(authorities = ROLE_ADMIN)
    public void testFindUserNotExists() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(USER_CONTROLLER_URL + "/" + Long.MAX_VALUE))
               .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    @WithMockUser(authorities = TEST_NOT_EXISTS_ROLE_NAME)
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
    @WithMockUser(authorities = ROLE_ADMIN)
    public void testGetCurrentUser() throws Exception {
        setLoggedInUserId(testUser.getId());
        mockMvc.perform(MockMvcRequestBuilders.get(USER_CONTROLLER_URL + "/current"))
               .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @WithMockUser(authorities = ROLE_ADMIN)
    public void testGetCurrentUserNotExists() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(USER_CONTROLLER_URL + "/current"))
               .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    @WithMockUser(authorities = TEST_NOT_EXISTS_ROLE_NAME)
    public void testGetCurrentUserForbidden() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(USER_CONTROLLER_URL + "/current"))
               .andExpect(MockMvcResultMatchers.status().isForbidden());
    }

    @Test
    public void testGetCurrentUserUnauthorized() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(USER_CONTROLLER_URL + "/current"))
               .andExpect(MockMvcResultMatchers.status().isUnauthorized());
    }

    @Test
    @WithMockUser(authorities = ROLE_ADMIN)
    public void testDeleteUser() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete(USER_CONTROLLER_URL + "/" + testUser.getId()))
               .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @WithMockUser(authorities = ROLE_ADMIN)
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
    @WithMockUser(authorities = ROLE_ADMIN)
    public void testEnableUser() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.patch(USER_CONTROLLER_URL + "/" + testUser.getId() + "/enable"))
               .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @WithMockUser(authorities = ROLE_ADMIN)
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
    @WithMockUser(authorities = ROLE_ADMIN)
    public void testDisableUser() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.patch(USER_CONTROLLER_URL + "/" + testUser.getId() + "/disable"))
               .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @WithMockUser(authorities = ROLE_ADMIN)
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
    @WithMockUser(authorities = ROLE_ADMIN)
    public void testLockUser() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.patch(USER_CONTROLLER_URL + "/" + testUser.getId() + "/lock"))
               .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @WithMockUser(authorities = ROLE_ADMIN)
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
    @WithMockUser(authorities = ROLE_ADMIN)
    public void testUnlockUser() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.patch(USER_CONTROLLER_URL + "/" + testUser.getId() + "/unlock"))
               .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @WithMockUser(authorities = ROLE_ADMIN)
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

    private void setLoggedInUserId(final Long id) {
        Optional.ofNullable(SecurityContextHolder.getContext().getAuthentication())
                .map(Authentication::getPrincipal)
                .filter(UserDetailsImpl.class::isInstance)
                .map(UserDetailsImpl.class::cast)
                .ifPresent(userDetails -> userDetails.setId(id));
    }

    private User createUser() {
        return User.builder()
                   .firstName(TEST_USER_REGISTER_FIRST_NAME)
                   .lastName(TEST_USER_REGISTER_LAST_NAME)
                   .email(TEST_USER_REGISTER_EMAIL)
                   .password(TEST_USER_REGISTER_PASSWORD)
                   .birthdayDate(LocalDate.now())
                   .roles(Stream.of(Role.builder()
                                        .name(TEST_ROLE_NAME)
                                        .build())
                                .collect(Collectors.toSet()))
                   .build();
    }

}
