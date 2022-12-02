package com.appsdeveloperblog.photo.ApiUser.ui.model;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;


/**
 * Bean that hold User data from @RequestBody in UsersController
 * Validation annotation to each field to check if incoming data is correct
 */
public class CreateUserModel {

    @NotNull(message="First name cannot be empty")
    @Size(min=2, message= "First name must be at east two characters")
    private String firstname;

    @NotNull(message="First name cannot be empty")
    @Size(min=2, message= "Last name must be at east two characters")
    private String lastname;

    @Size(min=2, max=8, message= "Password must be between 2~8 characters")
    private String password;

    @NotNull(message = "Email can not be empty")
    @Email
    private String email;

    public CreateUserModel() {
    }

    public CreateUserModel(String firstname, String lastname, String password, String email) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.password = password;
        this.email = email;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "CreateUserModel{" +
                "firstname='" + firstname + '\'' +
                ", lastname='" + lastname + '\'' +
                ", password='" + password + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
