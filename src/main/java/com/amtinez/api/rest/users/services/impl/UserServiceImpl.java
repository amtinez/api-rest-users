package com.amtinez.api.rest.users.services.impl;

import com.amtinez.api.rest.users.daos.UserDao;
import com.amtinez.api.rest.users.models.UserModel;
import com.amtinez.api.rest.users.services.UserService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
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
    public Optional<UserModel> findUser(final String email) {
        return userDao.findByEmail(email);
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
    public int updateUserPassword(final Long id, final String password) {
        return userDao.updatePasswordById(id, password, LocalDate.now());
    }

    @Override
    public int updateUserEnabledStatus(final Long id, final Boolean enabled) {
        return userDao.updateEnabledStatusById(id, enabled);
    }

    @Override
    public int updateLockedStatusById(final Long id, final Boolean locked) {
        return userDao.updateLockedStatusById(id, locked);
    }

}
