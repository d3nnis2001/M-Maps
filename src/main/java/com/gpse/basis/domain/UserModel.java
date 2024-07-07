package com.gpse.basis.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serial;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Collection;

/**
 * UserModel Class for all User specific information.
 */
@Document(collection = "userModel")
public class UserModel implements UserDetails {

    private static final boolean accountNonExpired = true;
    private static final boolean accountNonLocked = true;
    private static final boolean credentialsNonExpired = true;
    private static final boolean enabled = true;

    @Serial
    private static final long serialVersionUID = 1L;
    @MongoId
    private String email;
    @JsonIgnore
    private String password;
    private String firstname;
    private String passwordToken;
    private String lastname;
    private boolean unlocked;
    private ArrayList<String> region;
    private String service;
    private String userToken;

    @JsonIgnore
    private transient ArrayList<String> roles;

    /**
     * Constructor for UserModel.
     * @param email Email
     * @param password Password
     * @param firstname Firstname
     * @param lastname Lastname
     */
    public UserModel(String email, String password, String firstname, String lastname) {
        this.email = email;
        this.password = password;
        this.firstname = firstname;
        this.lastname = lastname;
        unlocked = false;
    }

    /**
     * Adds a role to the User.
     * @param role Role
     */
    public void addRole(String role) {
        if (this.roles == null) {
            this.roles = new ArrayList<>();
            this.roles.add(role);
        } else {
            if (!this.roles.contains(role)) {
                this.roles.add(role);
            }
        }
    }

    /**
     * Deletes the specific role from the User.
     * @param role Role
     */
    public void deleteRole(String role) {
        roles.removeIf(r -> r.equals(role));
        if (roles.isEmpty()) {
            roles = null;
        }
    }

    public ArrayList<String> getRoles() {
        return roles;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return new ArrayList<>();
    }

    @Override
    public String getUsername() {
        return this.email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return this.accountNonExpired;
    }

    @Override
    public boolean isAccountNonLocked() {
        return this.accountNonLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return this.credentialsNonExpired;
    }

    @Override
    public boolean isEnabled() {
        return this.enabled;
    }

    /**
     * Adds a region to the User.
     * @param region Region
     */
    public void addRegion(String region) {
        if (this.region == null) {
            this.region = new ArrayList<>();
            this.region.add(region);
        } else {
            this.region.add(region);
        }
    }

    /**
     * Sets the token Password.
     * @return String
     */
    public String setTokenPassword() {
        SecureRandom random = new SecureRandom();
        byte[] token = new byte[24];
        random.nextBytes(token);
        passwordToken = Base64.getUrlEncoder().withoutPadding().encodeToString(token);
        return passwordToken;
    }

    public String getTokenPassword() {
        return passwordToken;
    }

    public String setUserToken() {
        SecureRandom random = new SecureRandom();
        byte[] token = new byte[24];
        random.nextBytes(token);
        userToken = Base64  .getUrlEncoder().withoutPadding().encodeToString(token);
        return userToken;
    }
    public String getUserToken() {
        return this.userToken;
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public boolean getUnlocked() {
        return unlocked;
    }

    public void setUnlocked(boolean unlocked) {
        this.unlocked = unlocked;
    }

}
