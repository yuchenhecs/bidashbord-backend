package com.bi.oranj.model.oranj;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import javax.persistence.*;

/**
 * Created by harshavardhanpatil on 5/26/17.
 */

/**
 * Model class auth_user table
 */
@Entity
@Data
@Table(name = "auth_user")
@JsonIgnoreProperties(ignoreUnknown = true)
public class User {

    @Id
    private long id;
    private String email;
    private String password;

    @Column(name = "password_reset_token")
    private String passwordResetToken;

    @Column(name = "password_reset_token_expiration_date")
    private String passwordResetTokenExpirationDate;

    @Column(name = "yodlee_info")
    private String yodleeInfo;
    private boolean active;
    private String created;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "yodlee_password")
    private String yodleePassword;

    @Column(name = "yodlee_user")
    private String yodleeUser;

    @Column(name = "recovery_password")
    private String recoveryPassword;

    @Column(name = "fde_password")
    private String fdePassword;

    @Column(name = "fde_password_creation")
    private String fdePasswordCreation;

    @Column(name = "fde_user")
    private String fdeUser;

    @Column(name = "invite_sent")
    private boolean inviteSent;

    @Column(name = "nick_name")
    private String nickName;

    @Column(name = "invite_timestamp")
    private String inviteTimestamp;
    private String source;
    private boolean deleted;

    @Column(name="firm_id")
    private Long firmId;

    @Column(name="advisor_id")
    private Long advisor;

    public User() {
        // For JPA to use
    }
}
