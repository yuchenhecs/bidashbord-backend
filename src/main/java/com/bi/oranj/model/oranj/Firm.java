package com.bi.oranj.model.oranj;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import org.joda.time.DateTime;

import javax.persistence.*;

/**
 * Created by harshavardhanpatil on 5/31/17.
 */

/**
 * Model class firm table
 */
@Entity
@Data
@Table(name = "firm")
@JsonIgnoreProperties(ignoreUnknown = true)
public class Firm {

    @Id
    @GeneratedValue
    private long id;

    @Column(name = "long_name")
    private String longName;

    private String name;
    private byte[] adv;

    @Column(name = "adv_file_name")
    private String advFileName;

    @Column(name = "terms_and_conditions")
    private byte[] termsAndConditions;

    @Column(name = "terms_and_conditions_file_name")
    private String termsAndConditionsFileName;

    @Column(name = "firm_id")
    private String firmId;

    @Column(name = "admin_first_name")
    private String adminFirstName;

    @Column(name = "admin_last_name")
    private String adminLastName;

    @Column(name = "admin_primary_phone")
    private String adminPrimaryPhone;

    @Column(name = "admin_email")
    private String adminEmail;
    private String address;
    private String address2;
    private String city;
    private String state;
    private String zip;
    private String custodian;

    @Column(name = "group_tag")
    private String groupTag;

    private byte[] logo;

    @Column(name = "logo_file_name")
    private String logoFileName;

    private byte[] favicon;

    @Column(name = "favicon_file_name")
    private String faviconFileName;

    @Column(name = "service_agreement")
    private byte[] serviceAgreement;

    @Column(name = "service_agreement_file_name")
    private String serviceAgreementFileName;
    private String disclaimer;

    @Column(name = "activevwizard")
    private boolean activeVWizard;

    @Column(name = "activebpaapp")
    private boolean activeBPAApp;

    @Column(name = "active_subfirm_creation")
    private boolean activeSubfirmCreation;

    @Column(name = "total_number_of_advisors_allowed")
    private int totalNumberOfAdvisorsAllowed;

    @Column(name = "parent_firm_id")
    private long parentFirmId;
    private String url;

    @Column(name = "firm_primary_phone")
    private String firmPrimaryPhone;

    @Column(name = "rep_code")
    private String repCode;

    @Column(name = "trial_firm")
    private Boolean trialFirm;

    @Column(name = "disclaimer_editable")
    private Boolean disclaimerEditable;

    @Column(name = "created_on")
    private DateTime createdOn;

    @Column(name = "expires_on")
    private DateTime expiresOn;

    @Column(name = "import_type")
    private Long importType;

    @Column(name = "logo_url")
    private String logoUrl;

    @Column(name = "favicon_url")
    private String faviconUrl;

    @Column(name = "service_agreement_url")
    private String serviceAgreementUrl;

    @Column(name = "adv_url")
    private String advUrl;
    private String country;

    @Column(name="grace_period_in_days")
    private Integer gracePeriodInDays;

    @Column(name="total_number_of_clients_allowed")
    private Integer totalNumberOfClientsAllowed;
    private boolean active;

    @Column(name = "admin_crd_code")
    private String adminCrdCode;

    @Column(name = "onboarding_token")
    private String onboardingToken;

    @Column(name = "firm_source")
    private String firmSource;

    @Column(name = "firm_status")
    private String firmStatus;

    public Firm() {
        // For JPA to use
    }
}
