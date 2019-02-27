package net.bittreasury.service;

import java.util.List;

import org.hibernate.annotations.Cache;
import org.springframework.cache.annotation.Cacheable;

import net.bittreasury.entity.User;

public interface UserService {

	User getUserById(Long id);

	User findByUsername(String username);
	
	User addUser(User user);
	
	User updateDate(User user);
	

	List<User> getRandomUsers(int count);

	List<User> getAllUsers();
}
