package com.employee.bot.profile.repository;

import com.employee.bot.profile.Profile;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ProfileRepository extends MongoRepository<Profile,String> {
    @Aggregation(pipeline ={"{ $sample:{ size:1 } }"} )
    Profile randomProfile();
}
