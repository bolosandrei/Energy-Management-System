package usermicroservicegroup.usermicroservice.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import usermicroservicegroup.usermicroservice.entities.User;

import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {
    User findFirstById(UUID uuid);
    User findFirstByName(String name);
}
