package co.simplon.library.entity;

import jakarta.annotation.Nonnull;
import jakarta.persistence.*;

@Entity
@Table(name = "users")
public class UserEntity {

//    un identifiant UUID
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    public String id;
//    un nom d'utilisateur
    @Nonnull
    @Column(nullable = false)
    public String userName;
//    une adresse e-mail
    @Nonnull
    @Column(nullable = false)
    public String userMail;
//    un mot de passe
    @Nonnull
    @Column(nullable = false)
    public String userPassword;

    public UserEntity(String id, @Nonnull String userName, @Nonnull String userMail, @Nonnull String userPassword) {
        this.id = id;
        this.userName = userName;
        this.userMail = userMail;
        this.userPassword = userPassword;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Nonnull
    public String getUserName() {
        return userName;
    }

    public void setUserName(@Nonnull String userName) {
        this.userName = userName;
    }

    @Nonnull
    public String getUserMail() {
        return userMail;
    }

    public void setUserMail(@Nonnull String userMail) {
        this.userMail = userMail;
    }

    @Nonnull
    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(@Nonnull String userPassword) {
        this.userPassword = userPassword;
    }

    @Override
    public String toString() {
        return "UserEntity{" +
                "id='" + id + '\'' +
                ", userName='" + userName + '\'' +
                ", userMail='" + userMail + '\'' +
                ", userPassword='" + userPassword + '\'' +
                '}';
    }
}
