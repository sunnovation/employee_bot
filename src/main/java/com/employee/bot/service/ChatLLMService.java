package com.employee.bot.service;

import com.employee.bot.conversation.ChatMessag;
import com.employee.bot.conversation.Conversation;
import com.employee.bot.profile.Profile;
import org.springframework.ai.chat.messages.*;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class ChatLLMService {


    private final OpenAiChatModel chatClient;

    public ChatLLMService(OpenAiChatModel chatClient) {
        this.chatClient = chatClient;
    }

    public Conversation generateProfileResponse(Conversation conversation, Profile profile,Profile user){

        String systemMessageText= """
                 Welcome to Employee bot you have to talk with {profile.name()}.
    """;

        //System Message (Context of message)
        SystemMessage systemMessage=new SystemMessage(systemMessageText);
        String userMessageText=conversation.messages().get(0).textMessage();


        List<AbstractMessage> conversationMessages=conversation.messages().stream()
                .map(message->{
                   if(message.profileId().equalsIgnoreCase(profile.id())){
                       return new AssistantMessage(message.textMessage());
                   }else{
                       return new UserMessage(message.textMessage());
                   }
                }).toList();
        //Assistant Messages AI Generated messages
       List<Message> allMessage=new ArrayList<>();
       allMessage.add(systemMessage);
       allMessage.addAll(conversationMessages);


        Prompt prompt=new Prompt(allMessage);

        ChatResponse chatResponse=chatClient.call(prompt);

        conversation.messages().add(
                new ChatMessag(
                chatResponse.getResult().getOutput().getContent(),
                profile.id(),
                LocalDateTime.now()
                )
        );
        return conversation;
    }

}
