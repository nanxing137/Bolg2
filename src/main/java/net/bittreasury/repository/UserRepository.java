package net.bittreasury.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import net.bittreasury.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {
	User getUserByUsername(String username);
}
