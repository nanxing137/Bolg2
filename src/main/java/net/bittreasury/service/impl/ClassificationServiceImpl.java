package net.bittreasury.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.bittreasury.entity.Classification;
import net.bittreasury.repository.ClassificationRepository;
import net.bittreasury.service.ClassificationService;

@Service
public class ClassificationServiceImpl implements ClassificationService {

	@Autowired
	private ClassificationRepository classificationRepository;

	@Override
	public List<Classification> getAll() {
		List<Classification> findAll = classificationRepository.findAll();
		return findAll;
	}

}
