package net.bittreasury.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import net.bittreasury.entity.Label;

public interface LabelRepository extends JpaRepository<Label, Long> {

}
