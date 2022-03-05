package com.amtinez.api.rest.users.models;

import com.amtinez.api.rest.users.constants.DatabaseConstants.Table.UserVerificationToken;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * @author Alejandro Martínez Cerro <amartinezcerro @ gmail.com>
 */
@SuperBuilder
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = UserVerificationToken.TABLE_NAME)
public class UserVerificationTokenModel extends TokenModel {

}
