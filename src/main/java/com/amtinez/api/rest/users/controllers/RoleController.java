package com.amtinez.api.rest.users.controllers;

import com.amtinez.api.rest.users.dtos.Role;
import com.amtinez.api.rest.users.facades.RoleFacade;
import com.amtinez.api.rest.users.validations.groups.Update;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

import javax.annotation.Resource;
import javax.validation.Valid;

import static com.amtinez.api.rest.users.constants.SecurityConstants.HAS_ONLY_ROLE_ADMIN;

/**
 * @author Alejandro Martínez Cerro <amartinezcerro @ gmail.com>
 */
@Validated
@RestController
@RequestMapping("/roles")
public class RoleController {

    @Resource
    private RoleFacade roleFacade;

    @PreAuthorize(HAS_ONLY_ROLE_ADMIN)
    @GetMapping
    public ResponseEntity<List<Role>> findAllRoles() {
        return ResponseEntity.ok(roleFacade.findAllRoles());
    }

    @PreAuthorize(HAS_ONLY_ROLE_ADMIN)
    @PostMapping
    public ResponseEntity<Role> createRole(@Valid @RequestBody final Role role) {
        return ResponseEntity.ok(roleFacade.saveRole(role));
    }

    @PreAuthorize(HAS_ONLY_ROLE_ADMIN)
    @PutMapping
    public ResponseEntity<Role> updateRole(@Validated(Update.class) @RequestBody final Role role) {
        final Optional<Role> roleFound = roleFacade.updateRole(role);
        return roleFound.isPresent() ? ResponseEntity.ok().build() : ResponseEntity.notFound().build();
    }

    @PreAuthorize(HAS_ONLY_ROLE_ADMIN)
    @GetMapping("/{id}")
    public ResponseEntity<Role> findRole(@PathVariable final Long id) {
        final Optional<Role> role = roleFacade.findRole(id);
        return role.map(roleFound -> ResponseEntity.ok().body(roleFound)).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PreAuthorize(HAS_ONLY_ROLE_ADMIN)
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRole(@PathVariable final Long id) {
        final Optional<Role> role = roleFacade.findRole(id);
        if (role.isPresent()) {
            roleFacade.deleteRole(id);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }

}
