package com.employee.bot.conversation.repository;

import com.employee.bot.conversation.Conversation;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ConversationRepository extends MongoRepository<Conversation,String> {
}
