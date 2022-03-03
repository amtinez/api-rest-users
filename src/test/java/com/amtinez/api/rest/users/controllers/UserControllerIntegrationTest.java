package com.amtinez.api.rest.users.controllers;

import com.amtinez.api.rest.users.annotations.WithMockUser;
import com.amtinez.api.rest.users.constants.ConfigurationConstants.Profiles;
import com.amtinez.api.rest.users.dtos.PasswordResetToken;
import com.amtinez.api.rest.users.dtos.Role;
import com.amtinez.api.rest.users.dtos.User;
import com.amtinez.api.rest.users.facades.UserFacade;
import com.amtinez.api.rest.users.models.PasswordResetTokenModel;
import com.amtinez.api.rest.users.models.RoleModel;
import com.amtinez.api.rest.users.models.UserModel;
import com.amtinez.api.rest.users.models.UserVerificationTokenModel;
import com.amtinez.api.rest.users.security.impl.UserDetailsImpl;
import com.amtinez.api.rest.users.services.RoleService;
import com.amtinez.api.rest.users.services.TokenService;
import com.amtinez.api.rest.users.services.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
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
import static com.amtinez.api.rest.users.enums.Role.ADMIN;
import static com.amtinez.api.rest.users.enums.Role.USER;

/**
 * Integration test for {@link UserController}
 *
 * @author Alejandro Martínez Cerro <amartinezcerro @ gmail.com>
 */
@Transactional
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles(Profiles.TEST)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class UserControllerIntegrationTest {

    private static final String USER_CONTROLLER_URL = "/users";

    private static final String TEST_USER_FIRST_NAME = "testUserFirstName";
    private static final String TEST_USER_LAST_NAME = "testUserLastName";
    private static final String TEST_USER_EMAIL = "test@user.com";
    private static final String TEST_USER_PASSWORD = "testUserPassword";
    private static final String TEST_USER_NEW_PASSWORD = "testUserNewPassword";

    private static final String TEST_USER_FIRST_NAME_UPDATED = "testUserFirstNameUpdated";

    private static final String TEST_USER_REGISTER_FIRST_NAME = "testUserRegisterFirstName";
    private static final String TEST_USER_REGISTER_LAST_NAME = "testUserRegisterLastName";
    private static final String TEST_USER_REGISTER_EMAIL = "user@register.com";
    private static final String TEST_USER_REGISTER_PASSWORD = "testUserRegisterPassword";

    private static final String TEST_NOT_EXISTS_ROLE_NAME = "NOT_EXISTS";

    @Resource
    private MockMvc mockMvc;
    @Resource
    private UserFacade userFacade;
    @Resource
    private UserService userService;
    @Resource
    private RoleService roleService;
    @Resource
    private TokenService<UserVerificationTokenModel> userVerificationTokenService;
    @Resource
    private TokenService<PasswordResetTokenModel> passwordResetTokenService;

    private UserModel testUser;

    @BeforeAll
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
                                                 .roles(Stream.of(roleService.saveRole(RoleModel.builder()
                                                                                                .name(USER.name())
                                                                                                .build()))
                                                              .collect(Collectors.toSet()))
                                                 .build());
    }

    @AfterAll
    public void cleanUp() {
        userService.deleteUser(testUser.getId());
        testUser.getRoles()
                .stream()
                .map(RoleModel::getId)
                .findFirst()
                .ifPresent(roleService::deleteRole);
    }

    @Test
    @WithMockUser(authorities = ROLE_ADMIN)
    void testFindAllUsers() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(USER_CONTROLLER_URL))
               .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @WithMockUser
    void testFindAllUsersForbidden() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(USER_CONTROLLER_URL))
               .andExpect(MockMvcResultMatchers.status().isForbidden());
    }

    @Test
    void testFindAllUsersUnauthorized() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(USER_CONTROLLER_URL))
               .andExpect(MockMvcResultMatchers.status().isUnauthorized());
    }

    @Test
    void testRegisterUser() throws Exception {
        final User user = createUser();
        final ObjectMapper mapper = new ObjectMapper();
        mockMvc.perform(MockMvcRequestBuilders.post(USER_CONTROLLER_URL)
                                              .contentType(MediaType.APPLICATION_JSON)
                                              .content(mapper.writeValueAsString(user))
                                              .with(SecurityMockMvcRequestPostProcessors.csrf()))
               .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void testRegisterUserWithInvalidUser() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post(USER_CONTROLLER_URL)
                                              .with(SecurityMockMvcRequestPostProcessors.csrf()))
               .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    void testConfirmRegisterUser() throws Exception {
        final User registeredUser = userFacade.registerUser(createUser());
        final Optional<UserVerificationTokenModel> tokenModel = userVerificationTokenService.findToken(UserModel.builder()
                                                                                                                .id(registeredUser.getId())
                                                                                                                .build());
        mockMvc.perform(MockMvcRequestBuilders.get(USER_CONTROLLER_URL
                                                       + "/confirm/"
                                                       + tokenModel.map(UserVerificationTokenModel::getCode)
                                                                   .orElse(StringUtils.EMPTY)))
               .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void testConfirmRegisterUserExpiredToken() throws Exception {
        final User registeredUser = userFacade.registerUser(createUser());
        final Optional<UserVerificationTokenModel> tokenModel = userVerificationTokenService.findToken(UserModel.builder()
                                                                                                                .id(registeredUser.getId())
                                                                                                                .build());
        tokenModel.ifPresent(tokenModelFound -> {
            tokenModelFound.setExpiryDate(LocalDate.now().minusDays(1));
            userVerificationTokenService.saveToken(tokenModelFound);
        });
        mockMvc.perform(MockMvcRequestBuilders.get(USER_CONTROLLER_URL
                                                       + "/confirm/"
                                                       + tokenModel.map(UserVerificationTokenModel::getCode)
                                                                   .orElse(StringUtils.EMPTY)))
               .andExpect(MockMvcResultMatchers.status().isUnauthorized());
    }

    @Test
    void testSendUserPasswordResetEmail() throws Exception {
        final User user = createUser();
        final ObjectMapper mapper = new ObjectMapper();
        mockMvc.perform(MockMvcRequestBuilders.get(USER_CONTROLLER_URL + "/reset")
                                              .contentType(MediaType.APPLICATION_JSON)
                                              .content(mapper.writeValueAsString(user)))
               .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void testResetUserPassword() throws Exception {
        final User registeredUser = userFacade.registerUser(createUser());
        userFacade.sendUserPasswordResetEmail(registeredUser);
        final Optional<PasswordResetTokenModel> tokenModel = passwordResetTokenService.findToken(UserModel.builder()
                                                                                                          .id(registeredUser.getId())
                                                                                                          .build());
        final PasswordResetToken token = tokenModel.map(tokenModelFound -> PasswordResetToken.builder()
                                                                                             .code(tokenModelFound.getCode())
                                                                                             .password(TEST_USER_NEW_PASSWORD)
                                                                                             .repeatedPassword(TEST_USER_NEW_PASSWORD)
                                                                                             .build())
                                                   .orElse(null);
        final ObjectMapper mapper = new ObjectMapper();
        mockMvc.perform(MockMvcRequestBuilders.post(USER_CONTROLLER_URL + "/reset/")
                                              .contentType(MediaType.APPLICATION_JSON)
                                              .content(mapper.writeValueAsString(token))
                                              .with(SecurityMockMvcRequestPostProcessors.csrf()))
               .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void testResetUserPasswordExpiredToken() throws Exception {
        final User registeredUser = userFacade.registerUser(createUser());
        userFacade.sendUserPasswordResetEmail(registeredUser);
        final Optional<PasswordResetTokenModel> tokenModel = passwordResetTokenService.findToken(UserModel.builder()
                                                                                                          .id(registeredUser.getId())
                                                                                                          .build());
        tokenModel.ifPresent(tokenModelFound -> {
            tokenModelFound.setExpiryDate(LocalDate.now().minusDays(1));
            passwordResetTokenService.saveToken(tokenModelFound);
        });
        final PasswordResetToken token = tokenModel.map(tokenModelFound -> PasswordResetToken.builder()
                                                                                             .code(tokenModelFound.getCode())
                                                                                             .password(TEST_USER_NEW_PASSWORD)
                                                                                             .repeatedPassword(TEST_USER_NEW_PASSWORD)
                                                                                             .build())
                                                   .orElse(null);
        final ObjectMapper mapper = new ObjectMapper();
        mockMvc.perform(MockMvcRequestBuilders.post(USER_CONTROLLER_URL + "/reset/")
                                              .contentType(MediaType.APPLICATION_JSON)
                                              .content(mapper.writeValueAsString(token))
                                              .with(SecurityMockMvcRequestPostProcessors.csrf()))
               .andExpect(MockMvcResultMatchers.status().isUnauthorized());
    }

    @Test
    @WithMockUser
    void testUpdateUser() throws Exception {
        setLoggedInUserId(testUser.getId());
        final User user = User.builder()
                              .id(testUser.getId())
                              .firstName(TEST_USER_FIRST_NAME_UPDATED)
                              .build();
        final ObjectMapper mapper = new ObjectMapper();
        mockMvc.perform(MockMvcRequestBuilders.put(USER_CONTROLLER_URL)
                                              .contentType(MediaType.APPLICATION_JSON)
                                              .content(mapper.writeValueAsString(user))
                                              .with(SecurityMockMvcRequestPostProcessors.csrf()))
               .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @WithMockUser(authorities = ROLE_ADMIN)
    void testUpdateUserNotExists() throws Exception {
        final User user = User.builder()
                              .id(Long.MAX_VALUE)
                              .build();
        final ObjectMapper mapper = new ObjectMapper();
        mockMvc.perform(MockMvcRequestBuilders.put(USER_CONTROLLER_URL)
                                              .contentType(MediaType.APPLICATION_JSON)
                                              .content(mapper.writeValueAsString(user))
                                              .with(SecurityMockMvcRequestPostProcessors.csrf()))
               .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    @WithMockUser(authorities = ROLE_ADMIN)
    void testUpdateUserAdminLoggedInUser() throws Exception {
        setLoggedInUserId(Long.MAX_VALUE);
        final User user = User.builder()
                              .id(testUser.getId())
                              .build();
        final ObjectMapper mapper = new ObjectMapper();
        mockMvc.perform(MockMvcRequestBuilders.put(USER_CONTROLLER_URL)
                                              .contentType(MediaType.APPLICATION_JSON)
                                              .content(mapper.writeValueAsString(user))
                                              .with(SecurityMockMvcRequestPostProcessors.csrf()))
               .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @WithMockUser
    void testUpdateUserOtherLoggedInUser() throws Exception {
        setLoggedInUserId(Long.MAX_VALUE);
        final User user = User.builder()
                              .id(testUser.getId())
                              .build();
        final ObjectMapper mapper = new ObjectMapper();
        mockMvc.perform(MockMvcRequestBuilders.put(USER_CONTROLLER_URL)
                                              .contentType(MediaType.APPLICATION_JSON)
                                              .content(mapper.writeValueAsString(user))
                                              .with(SecurityMockMvcRequestPostProcessors.csrf()))
               .andExpect(MockMvcResultMatchers.status().isForbidden());
    }

    @Test
    @WithMockUser(authorities = ROLE_ADMIN)
    void testUpdateUserWithInvalidUser() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.put(USER_CONTROLLER_URL)
                                              .with(SecurityMockMvcRequestPostProcessors.csrf()))
               .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    @WithMockUser(authorities = TEST_NOT_EXISTS_ROLE_NAME)
    void testUpdateUserForbidden() throws Exception {
        final ObjectMapper mapper = new ObjectMapper();
        mockMvc.perform(MockMvcRequestBuilders.put(USER_CONTROLLER_URL)
                                              .contentType(MediaType.APPLICATION_JSON)
                                              .content(mapper.writeValueAsString(User.builder()
                                                                                     .id(testUser.getId())
                                                                                     .build()))
                                              .with(SecurityMockMvcRequestPostProcessors.csrf()))
               .andExpect(MockMvcResultMatchers.status().isForbidden());
    }

    @Test
    void testUpdateUserUnauthorized() throws Exception {
        final ObjectMapper mapper = new ObjectMapper();
        mockMvc.perform(MockMvcRequestBuilders.put(USER_CONTROLLER_URL)
                                              .contentType(MediaType.APPLICATION_JSON)
                                              .content(mapper.writeValueAsString(User.builder()
                                                                                     .id(testUser.getId())
                                                                                     .build()))
                                              .with(SecurityMockMvcRequestPostProcessors.csrf()))
               .andExpect(MockMvcResultMatchers.status().isUnauthorized());
    }

    @Test
    @WithMockUser(authorities = ROLE_ADMIN)
    void testFindUser() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(USER_CONTROLLER_URL + "/" + testUser.getId()))
               .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @WithMockUser(authorities = ROLE_ADMIN)
    void testFindUserNotExists() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(USER_CONTROLLER_URL + "/" + Long.MAX_VALUE))
               .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    @WithMockUser(authorities = TEST_NOT_EXISTS_ROLE_NAME)
    void testFindUserForbidden() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(USER_CONTROLLER_URL + "/" + testUser.getId()))
               .andExpect(MockMvcResultMatchers.status().isForbidden());
    }

    @Test
    void testFindUserUnauthorized() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(USER_CONTROLLER_URL + "/" + testUser.getId()))
               .andExpect(MockMvcResultMatchers.status().isUnauthorized());
    }

    @Test
    @WithMockUser(authorities = ROLE_ADMIN)
    void testGetCurrentUser() throws Exception {
        setLoggedInUserId(testUser.getId());
        mockMvc.perform(MockMvcRequestBuilders.get(USER_CONTROLLER_URL + "/current"))
               .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @WithMockUser(authorities = ROLE_ADMIN)
    void testGetCurrentUserNotExists() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(USER_CONTROLLER_URL + "/current"))
               .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    @WithMockUser(authorities = TEST_NOT_EXISTS_ROLE_NAME)
    void testGetCurrentUserForbidden() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(USER_CONTROLLER_URL + "/current"))
               .andExpect(MockMvcResultMatchers.status().isForbidden());
    }

    @Test
    void testGetCurrentUserUnauthorized() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(USER_CONTROLLER_URL + "/current"))
               .andExpect(MockMvcResultMatchers.status().isUnauthorized());
    }

    @Test
    @WithMockUser(authorities = ROLE_ADMIN)
    void testDeleteUser() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete(USER_CONTROLLER_URL + "/" + testUser.getId())
                                              .with(SecurityMockMvcRequestPostProcessors.csrf()))
               .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @WithMockUser(authorities = ROLE_ADMIN)
    void testDeleteUserNotExists() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete(USER_CONTROLLER_URL + "/" + Long.MAX_VALUE)
                                              .with(SecurityMockMvcRequestPostProcessors.csrf()))
               .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    @WithMockUser
    void testDeleteUserForbidden() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete(USER_CONTROLLER_URL + "/" + testUser.getId())
                                              .with(SecurityMockMvcRequestPostProcessors.csrf()))
               .andExpect(MockMvcResultMatchers.status().isForbidden());
    }

    @Test
    void testDeleteUserUnauthorized() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete(USER_CONTROLLER_URL + "/" + testUser.getId())
                                              .with(SecurityMockMvcRequestPostProcessors.csrf()))
               .andExpect(MockMvcResultMatchers.status().isUnauthorized());
    }

    @Test
    @WithMockUser(authorities = ROLE_ADMIN)
    void testEnableUser() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.patch(USER_CONTROLLER_URL + "/" + testUser.getId() + "/enable")
                                              .with(SecurityMockMvcRequestPostProcessors.csrf()))
               .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @WithMockUser(authorities = ROLE_ADMIN)
    void testEnableNotExists() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.patch(USER_CONTROLLER_URL + "/" + Long.MAX_VALUE + "/enable")
                                              .with(SecurityMockMvcRequestPostProcessors.csrf()))
               .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    @WithMockUser
    void testEnableUserForbidden() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.patch(USER_CONTROLLER_URL + "/" + testUser.getId() + "/enable")
                                              .with(SecurityMockMvcRequestPostProcessors.csrf()))
               .andExpect(MockMvcResultMatchers.status().isForbidden());
    }

    @Test
    void testEnableUserUnauthorized() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.patch(USER_CONTROLLER_URL + "/" + testUser.getId() + "/enable")
                                              .with(SecurityMockMvcRequestPostProcessors.csrf()))
               .andExpect(MockMvcResultMatchers.status().isUnauthorized());
    }

    @Test
    @WithMockUser(authorities = ROLE_ADMIN)
    void testDisableUser() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.patch(USER_CONTROLLER_URL + "/" + testUser.getId() + "/disable")
                                              .with(SecurityMockMvcRequestPostProcessors.csrf()))
               .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @WithMockUser(authorities = ROLE_ADMIN)
    void testDisableNotExists() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.patch(USER_CONTROLLER_URL + "/" + Long.MAX_VALUE + "/disable")
                                              .with(SecurityMockMvcRequestPostProcessors.csrf()))
               .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    @WithMockUser
    void testDisableUserForbidden() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.patch(USER_CONTROLLER_URL + "/" + testUser.getId() + "/disable")
                                              .with(SecurityMockMvcRequestPostProcessors.csrf()))
               .andExpect(MockMvcResultMatchers.status().isForbidden());
    }

    @Test
    void testDisableUserUnauthorized() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.patch(USER_CONTROLLER_URL + "/" + testUser.getId() + "/disable")
                                              .with(SecurityMockMvcRequestPostProcessors.csrf()))
               .andExpect(MockMvcResultMatchers.status().isUnauthorized());
    }

    @Test
    @WithMockUser(authorities = ROLE_ADMIN)
    void testLockUser() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.patch(USER_CONTROLLER_URL + "/" + testUser.getId() + "/lock")
                                              .with(SecurityMockMvcRequestPostProcessors.csrf()))
               .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @WithMockUser(authorities = ROLE_ADMIN)
    void testLockNotExists() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.patch(USER_CONTROLLER_URL + "/" + Long.MAX_VALUE + "/lock")
                                              .with(SecurityMockMvcRequestPostProcessors.csrf()))
               .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    @WithMockUser
    void testLockUserForbidden() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.patch(USER_CONTROLLER_URL + "/" + testUser.getId() + "/lock")
                                              .with(SecurityMockMvcRequestPostProcessors.csrf()))
               .andExpect(MockMvcResultMatchers.status().isForbidden());
    }

    @Test
    void testLockUserUnauthorized() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.patch(USER_CONTROLLER_URL + "/" + testUser.getId() + "/lock")
                                              .with(SecurityMockMvcRequestPostProcessors.csrf()))
               .andExpect(MockMvcResultMatchers.status().isUnauthorized());
    }

    @Test
    @WithMockUser(authorities = ROLE_ADMIN)
    void testUnlockUser() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.patch(USER_CONTROLLER_URL + "/" + testUser.getId() + "/unlock")
                                              .with(SecurityMockMvcRequestPostProcessors.csrf()))
               .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @WithMockUser(authorities = ROLE_ADMIN)
    void testUnlockNotExists() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.patch(USER_CONTROLLER_URL + "/" + Long.MAX_VALUE + "/unlock")
                                              .with(SecurityMockMvcRequestPostProcessors.csrf()))
               .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    @WithMockUser
    void testUnlockUserForbidden() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.patch(USER_CONTROLLER_URL + "/" + testUser.getId() + "/unlock")
                                              .with(SecurityMockMvcRequestPostProcessors.csrf()))
               .andExpect(MockMvcResultMatchers.status().isForbidden());
    }

    @Test
    void testUnlockUserUnauthorized() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.patch(USER_CONTROLLER_URL + "/" + testUser.getId() + "/unlock")
                                              .with(SecurityMockMvcRequestPostProcessors.csrf()))
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
                                        .name(ADMIN.name())
                                        .build())
                                .collect(Collectors.toSet()))
                   .build();
    }

}
