package com.employee.bot.match.repository;

import com.employee.bot.match.Match;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface MatchRepository extends MongoRepository<Match,String> {
}
