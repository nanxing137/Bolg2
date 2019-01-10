package net.bittreasury.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import net.bittreasury.entity.Classification;

public interface ClassificationRepository extends JpaRepository<Classification, Long> {

	
}
