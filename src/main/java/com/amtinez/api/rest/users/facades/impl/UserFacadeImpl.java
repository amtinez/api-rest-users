package com.amtinez.api.rest.users.facades.impl;

import com.amtinez.api.rest.users.dtos.User;
import com.amtinez.api.rest.users.facades.UserFacade;
import com.amtinez.api.rest.users.mappers.UserMapper;
import com.amtinez.api.rest.users.models.UserModel;
import com.amtinez.api.rest.users.security.impl.UserDetailsImpl;
import com.amtinez.api.rest.users.services.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.annotation.Resource;

/**
 * @author Alejandro Martínez Cerro <amartinezcerro @ gmail.com>
 */
@Component
public class UserFacadeImpl implements UserFacade {

    @Resource
    private UserMapper userMapper;

    @Resource
    private UserService userService;

    @Resource
    private PasswordEncoder passwordEncoder;

    @Override
    public Optional<User> findUser(final Long id) {
        return userService.findUser(id)
                          .map(userMapper::userModelToUser);
    }

    @Override
    public Optional<User> getCurrentUser() {
        return Optional.ofNullable(SecurityContextHolder.getContext().getAuthentication())
                       .map(Authentication::getPrincipal)
                       .filter(UserDetailsImpl.class::isInstance)
                       .map(UserDetailsImpl.class::cast)
                       .map(UserDetailsImpl::getId)
                       .flatMap(this::findUser);
    }

    @Override
    public List<User> findAllUsers() {
        return userService.findAllUsers()
                          .stream()
                          .map(userMapper::userModelToUser)
                          .collect(Collectors.toList());
    }

    @Override
    public User registerUser(final User user) {
        return userMapper.userModelToUser(userService.saveUser(userMapper.userToUserModelRegisterStep(user, passwordEncoder)));
    }

    @Override
    public int enableUser(final Long id) {
        return userService.updateUserEnabledStatus(id, Boolean.TRUE);
    }

    @Override
    public int disableUser(final Long id) {
        return userService.updateUserEnabledStatus(id, Boolean.FALSE);
    }

    @Override
    public void deleteUser(final Long id) {
        userService.deleteUser(id);
    }

    @Override
    public Optional<User> updateUser(final User user) {
        final Optional<UserModel> userModel = userService.findUser(user.getId());
        return userModel.map(model -> userMapper.userModelToUser(userService.saveUser(userMapper.updateUserModelFromUser(model, user))));
    }

    @Override
    public int lockUser(final Long id) {
        return userService.updateLockedStatusById(id, Boolean.TRUE);
    }

    @Override
    public int unlockUser(final Long id) {
        return userService.updateLockedStatusById(id, Boolean.FALSE);
    }

}
