package com.amtinez.api.rest.users.models.users;

import com.amtinez.api.rest.users.constants.DatabaseConstants.Table.User;
import com.amtinez.api.rest.users.constants.DatabaseConstants.Table.UsersRoles;
import com.amtinez.api.rest.users.models.roles.RoleModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

/**
 * @author Alejandro Martínez Cerro <amartinezcerro @ gmail.com>
 */
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = User.TABLE_NAME)
public class UserModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = User.FIRST_NAME_FIELD, length = User.FIRST_NAME_FIELD_LENGTH, nullable = false)
    private String firstName;

    @Column(name = User.LAST_NAME_FIELD, length = User.LAST_NAME_FIELD_LENGTH, nullable = false)
    private String lastName;

    @Column(name = User.EMAIL_FIELD, length = User.EMAIL_FIELD_LENGTH, nullable = false, unique = true)
    private String email;

    @Column(name = User.PASSWORD_FIELD, length = User.PASSWORD_FIELD_LENGTH, nullable = false)
    private String password;

    @Column(name = User.BIRTHDAY_DATE_FIELD, nullable = false)
    private LocalDateTime birthdayDate;

    @Column(name = User.LAST_ACCESS_DATE_FIELD)
    private LocalDateTime lastAccessDate;

    @Column(name = User.LAST_PASSWORD_UPDATE_DATE_FIELD)
    private LocalDateTime lastPasswordUpdateDate;

    @Column(name = User.ENABLED_FIELD, nullable = false)
    private Boolean enabled;

    @Builder.Default
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = UsersRoles.TABLE_NAME,
               joinColumns = {@JoinColumn(name = UsersRoles.USER_ID_FIELD)},
               inverseJoinColumns = {@JoinColumn(name = UsersRoles.ROLE_ID_FIELD)})
    private Set<RoleModel> roles = new HashSet<>(0);

}
