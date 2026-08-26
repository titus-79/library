package co.simplon.library.entity;

import jakarta.annotation.Nonnull;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "users")
public class UserEntity {

//    un identifiant UUID
    @Id
    @Setter(AccessLevel.PROTECTED)
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
//    un nom d'utilisateur
    @Nonnull
    @Column(nullable = false)
    private String userName;
//    une adresse e-mail
    @Nonnull
    @Column(nullable = false, unique = true)
    private String userMail;
//    un mot de passe
    @Nonnull
    @Column(nullable = false)
    private String userPassword;

}
