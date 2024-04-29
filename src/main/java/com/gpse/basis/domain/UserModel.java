package com.gpse.basis.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serial;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Document
public class UserModel implements UserDetails {
    @Serial
    private static final long serialVersionUID = 1L;
    @MongoId
    private String email;
    @JsonIgnore
    private String password;
    private  String firstname;
    private String lastname;
    private ArrayList<String> region;
    private String service;
    private static final boolean accountNonExpired = true;
    private static final boolean accountNonLocked = true;
    private static final boolean credentialsNonExpired = true;
    private static final boolean enabled = true;

    @JsonIgnore
    private transient ArrayList<String> roles;
    public UserModel(String email, String password, String firstname, String lastname) {
        this.email = email;
        this.password = password;
        this.firstname = firstname;
        this.lastname = lastname;
    }
    public void addRole(String role) {
        if (this.roles == null) {
            this.roles = new ArrayList<>();
            this.roles.add(role);
        } else {
            this.roles.add(role);
        }
    }
    public void deleteRole(String role) {
        for(String r : roles) {
            if (r.equals(role)) {
                roles.remove(r);
            }
        }
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

    public void addRegion(String region) {
        if (this.region == null) {
            this.region = new ArrayList<>();
            this.region.add(region);
        } else {
            this.region.add(region);
        }
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }
}
