package com.appsdeveloperblog.photo.ApiUser.ui.model;


/**
 * Explained in 77.
 * DTO simple POJO bean that holds data that is then used as a vehicle to transfer data between layers
 * Presentation    |     Service      |      Data Layer
 * UserModel      DTO                DTO       Entity
 *
 */
public class UserDto {
    private String firstname;
    private String lastname;
    private String password;
    private String email;
    private String userId;
    private String encryptedPassword;

    public UserDto() {}

    public UserDto(String firstname, String lastname, String password, String email, String userId, String encryptedPassword) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.password = password;
        this.email = email;
        this.userId = userId;
        this.encryptedPassword = encryptedPassword;
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

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getEncryptedPassword() {
        return encryptedPassword;
    }

    public void setEncryptedPassword(String encryptedPassword) {
        this.encryptedPassword = encryptedPassword;
    }

    @Override
    public String toString() {
        return "UserDto{" +
                "firstname='" + firstname + '\'' +
                ", lastname='" + lastname + '\'' +
                ", password='" + password + '\'' +
                ", email='" + email + '\'' +
                ", userId='" + userId + '\'' +
                ", encryptedPassword='" + encryptedPassword + '\'' +
                '}';
    }
}
