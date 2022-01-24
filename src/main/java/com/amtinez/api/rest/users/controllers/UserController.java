package com.amtinez.api.rest.users.controllers;

import com.amtinez.api.rest.users.dtos.User;
import com.amtinez.api.rest.users.facades.UserFacade;
import com.amtinez.api.rest.users.utils.ResponseEntityUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

import javax.annotation.Resource;
import javax.validation.Valid;

import static com.amtinez.api.rest.users.constants.SecurityConstants.HAS_ANY_ROLE;
import static com.amtinez.api.rest.users.constants.SecurityConstants.HAS_ONLY_ROLE_ADMIN;
import static com.amtinez.api.rest.users.constants.SecurityConstants.PERMIT_ALL;

/**
 * @author Alejandro Martínez Cerro <amartinezcerro @ gmail.com>
 */
@Validated
@RestController
@RequestMapping("/users")
public class UserController {

    @Resource
    private UserFacade userFacade;

    @PreAuthorize(HAS_ONLY_ROLE_ADMIN)
    @GetMapping
    public ResponseEntity<List<User>> findAllUsers() {
        return ResponseEntity.ok(userFacade.findAllUsers());
    }

    @PreAuthorize(PERMIT_ALL)
    @PostMapping
    public ResponseEntity<User> registerUser(@Valid @RequestBody final User user) {
        return ResponseEntity.ok(userFacade.registerUser(user));
    }

    @PreAuthorize(HAS_ONLY_ROLE_ADMIN)
    @GetMapping("/{id}")
    public ResponseEntity<User> findUser(@PathVariable final Long id) {
        final Optional<User> user = userFacade.findUser(id);
        return user.map(userFound -> ResponseEntity.ok().body(userFound)).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PreAuthorize(HAS_ANY_ROLE)
    @GetMapping("/current")
    public ResponseEntity<User> getCurrentUser() {
        final Optional<User> currentUser = userFacade.getCurrentUser();
        return currentUser.map(currentUserFound -> ResponseEntity.ok().body(currentUserFound))
                          .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PreAuthorize(HAS_ONLY_ROLE_ADMIN)
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable final Long id) {
        final Optional<User> user = userFacade.findUser(id);
        if (user.isPresent()) {
            userFacade.deleteUser(id);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }

    @PreAuthorize(HAS_ONLY_ROLE_ADMIN)
    @PatchMapping("/{id}/enable")
    public ResponseEntity<Void> enableUser(@PathVariable final Long id) {
        return ResponseEntityUtils.getResponseEntityByAffectedEntities(userFacade.enableUser(id));
    }

    @PreAuthorize(HAS_ONLY_ROLE_ADMIN)
    @PatchMapping("/{id}/disable")
    public ResponseEntity<Void> disableUser(@PathVariable final Long id) {
        return ResponseEntityUtils.getResponseEntityByAffectedEntities(userFacade.disableUser(id));
    }

    @PreAuthorize(HAS_ONLY_ROLE_ADMIN)
    @PatchMapping("/{id}/lock")
    public ResponseEntity<Void> lockUser(@PathVariable final Long id) {
        return ResponseEntityUtils.getResponseEntityByAffectedEntities(userFacade.lockUser(id));
    }

    @PreAuthorize(HAS_ONLY_ROLE_ADMIN)
    @PatchMapping("/{id}/unlock")
    public ResponseEntity<Void> unlockUser(@PathVariable final Long id) {
        return ResponseEntityUtils.getResponseEntityByAffectedEntities(userFacade.unlockUser(id));
    }

}
