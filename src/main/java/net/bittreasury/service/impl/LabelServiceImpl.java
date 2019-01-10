package net.bittreasury.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.bittreasury.entity.Label;
import net.bittreasury.repository.LabelRepository;
import net.bittreasury.service.LabelService;
@Service
public class LabelServiceImpl implements LabelService {

	
	@Autowired
	private LabelRepository labelRepository;
	
	@Override
	public List<Label> getAll() {
		List<Label> findAll = labelRepository.findAll();
		return findAll;
	}

}
