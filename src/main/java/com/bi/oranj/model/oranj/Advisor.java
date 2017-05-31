package com.bi.oranj.model.oranj;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import javax.persistence.*;


/**
 * Created by harshavardhanpatil on 5/30/17.
 */

/**
 * Model class advisor table
 */
@Entity
@Data
@Table(name = "advisor")
@JsonIgnoreProperties(ignoreUnknown = true)
public class Advisor {

    @Id
    @GeneratedValue
    private long id;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "advisor_id",nullable = true)
    private String advisorId;
    private String title;

    @Column(name = "primary_phone")
    private String primaryPhone;

    @Column(name = "secondary_phone")
    private String secondaryPhone;
    private String fax;
    private String company;
    private byte[] avatar;

    @Column(name = "avatar_file_name")
    private String avatarFileName;
    private String bio;

    @Column(name = "email_signature")
    private String emailSignature;
    private String email;

    @Column(name = "share_access")
    private String shareAccess;
    private boolean active;

    @Column(name = "cell_phone")
    private String cellPhone;

    @Column(name = "advisor_user_id",nullable = true)
    private String advisorUserId;

    @Column(name="firm_id", nullable=false)
    private Long firmId;

    @Column(name="docusign_credential_id", nullable=true)
    private String docusignCredentialId;

    @Column(name = "read_on_boarding_tooltip")
    private boolean readOnBoardingTooltip;

    @Column(name = "advisor_avatar_url")
    private String advisorAvatarUrl;

    private boolean deleted = false;

    public Advisor(){
        // For JPA to use
    }

}
