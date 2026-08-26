package co.simplon.library.repository;

import co.simplon.library.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, String> {
    public Optional<UserEntity> findByUserName(String userName);
}
