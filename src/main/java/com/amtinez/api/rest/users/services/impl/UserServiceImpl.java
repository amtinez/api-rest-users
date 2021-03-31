package com.amtinez.api.rest.users.services.impl;

import com.amtinez.api.rest.users.daos.UserDao;
import com.amtinez.api.rest.users.models.UserModel;
import com.amtinez.api.rest.users.services.UserService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import javax.annotation.Resource;

/**
 * @author Alejandro Martínez Cerro <amartinezcerro @ gmail.com>
 */
@Service
public class UserServiceImpl implements UserService {

    @Resource
    private UserDao userDao;

    @Override
    public Optional<UserModel> findUser(final Long id) {
        return userDao.findById(id);
    }

    @Override
    public List<UserModel> findAllUsers() {
        return userDao.findAll();
    }

    @Override
    public UserModel saveUser(final UserModel user) {
        return userDao.save(user);
    }

    @Override
    public void deleteUser(final Long id) {
        userDao.deleteById(id);
    }

    @Override
    public boolean existsUserEmail(final String email) {
        return userDao.existsByEmail(email);
    }

    @Override
    public int updateUserEnabledStatus(final Long id, final Boolean enabled) {
        return userDao.updateEnabledStatusById(id, enabled);
    }

}
