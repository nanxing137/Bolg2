package net.bittreasury.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import net.bittreasury.entity.LandingHistory;
import net.bittreasury.entity.User;
import net.bittreasury.repository.LandingHistoryRepository;
import net.bittreasury.repository.UserRepository;
import net.bittreasury.service.UserService;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private LandingHistoryRepository landingHistoryRepository;


	@Override
	@Cacheable("getUserById")
	public User getUserById(Long id) {
		User user = userRepository.getOne(id);
		return user;
	}

	@Override
	@Cacheable("findByUsername")
	public User findByUsername(String username) {
		User userByUsername = userRepository.getUserByUsername(username);
		return userByUsername;
	}

	@Override
	public User addUser(User user) {
		User save = userRepository.save(user);
		return save;
	}

	@Override
	@Async
	public User updateDate(User user) {
		Date date = new Date();
		user.setLastActive(date);
		LandingHistory landingHistory = new LandingHistory();
		landingHistory.setLandingDate(date);
		landingHistory.setUser(user);
		landingHistory.setLandingIP("IP还未做");
		landingHistoryRepository.save(landingHistory);
		User save = userRepository.save(user);
		return save;
	}

	@Override
	public List<User> getRandomUsers(int count) {
		List<User> findAll = userRepository.findAll();
		if (count > findAll.size() - 1) {
			return findAll;
		}
		List<User> users = new ArrayList<>(count);
		for (int i = 0; i < count; i++) {
			users.add(findAll.remove((int) (findAll.size() * (Math.random()))));
		}
		return users;
	}

}
