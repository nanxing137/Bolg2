package net.bittreasury.repository;

import java.util.List;

import org.springframework.core.annotation.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import net.bittreasury.entity.Article;
import net.bittreasury.entity.LandingHistory;
import net.bittreasury.entity.User;

public interface LandingHistoryRepository extends JpaRepository<LandingHistory, Long> {
}
