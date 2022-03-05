package com.amtinez.api.rest.users.services.impl;

import com.amtinez.api.rest.users.daos.PasswordResetTokenDao;
import com.amtinez.api.rest.users.models.PasswordResetTokenModel;
import com.amtinez.api.rest.users.models.UserModel;
import com.amtinez.api.rest.users.services.TokenService;
import org.springframework.stereotype.Service;

import java.util.Optional;

import javax.annotation.Resource;

/**
 * @author Alejandro Martínez Cerro <amartinezcerro @ gmail.com>
 */
@Service
public class PasswordResetTokenServiceImpl implements TokenService<PasswordResetTokenModel> {

    @Resource
    private PasswordResetTokenDao passwordResetTokenDao;

    @Override
    public Optional<PasswordResetTokenModel> findToken(final Long id) {
        return passwordResetTokenDao.findById(id);
    }

    @Override
    public Optional<PasswordResetTokenModel> findToken(final String code) {
        return passwordResetTokenDao.findByCode(code);
    }

    @Override
    public Optional<PasswordResetTokenModel> findToken(final UserModel user) {
        return passwordResetTokenDao.findByUser(user);
    }

    @Override
    public PasswordResetTokenModel saveToken(final PasswordResetTokenModel token) {
        return passwordResetTokenDao.save(token);
    }

    @Override
    public void deleteToken(final PasswordResetTokenModel token) {
        passwordResetTokenDao.delete(token);
    }

    @Override
    public void deleteToken(final Long id) {
        passwordResetTokenDao.deleteById(id);
    }

    @Override
    public void deleteTokenByUserId(final Long id) {
        passwordResetTokenDao.deleteByUserId(id);
    }

}
