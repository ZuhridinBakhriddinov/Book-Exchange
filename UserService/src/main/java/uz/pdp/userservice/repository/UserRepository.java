package uz.pdp.userservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.pdp.userservice.entity.User;

public interface UserRepository extends JpaRepository<User,Integer> {
}
