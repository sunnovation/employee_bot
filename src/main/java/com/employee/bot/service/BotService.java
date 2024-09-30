package com.employee.bot.service;

import com.employee.bot.constant.BotConstant;
import com.employee.bot.conversation.ChatMessag;
import com.employee.bot.conversation.Conversation;
import com.employee.bot.conversation.ProfileRequest;
import com.employee.bot.conversation.repository.ConversationRepository;
import com.employee.bot.match.CreateMatchRequest;
import com.employee.bot.match.Match;
import com.employee.bot.match.repository.MatchRepository;
import com.employee.bot.profile.Profile;
import com.employee.bot.profile.repository.ProfileRepository;
import com.employee.bot.util.BotUtility;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
public class BotService {

    private List<Profile> generatedProfiles = new ArrayList<>();
    private final ConversationRepository conversationRepository;
    private final ProfileRepository profileRepository;
    private final MatchRepository matchRepository;
    private static final String PROFILES_FILE_PATH = "E:\\aws\\employeebotai\\profiles.json";

    private final ChatLLMService chatLLMService;

    public void createConversation(ProfileRequest profile){
        log.info("BotService::createConversation");
        profileRepository.findById(profile.profileId()).orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND));
        Conversation conversation=new Conversation(
                BotUtility.generateUId(),
                profile.profileId(),
                new ArrayList<>());

        Conversation conversation1=conversationRepository.save(conversation);
        log.info(conversation1.toString());


    }

    public BotService(ConversationRepository conversationRepository,ProfileRepository profileRepository,MatchRepository matchRepository,ChatLLMService chatLLMService) {
        this.conversationRepository = conversationRepository;
        this.profileRepository=profileRepository;
        this.matchRepository=matchRepository;
        this.chatLLMService=chatLLMService;
    }

    public Conversation addMessageForConversation(String conversationId, ChatMessag chatMessag) {

        Conversation conversation=conversationRepository.findById(conversationId).orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND,"Unable to find ID"+ conversationId));
       Profile profile= profileRepository.findById(chatMessag.profileId()).orElseThrow(
                ()->new ResponseStatusException(HttpStatus.NOT_FOUND,"Unable to finf ID: "+chatMessag.profileId())
        );

        ChatMessag chatMessag1=new ChatMessag(
                chatMessag.textMessage(),
                chatMessag.profileId(),
                LocalDateTime.now()
        );

        conversation.messages().add(chatMessag1);
        conversationRepository.save(conversation);
        return conversation;

    }
    public Conversation addMatchMessageForConversation(String conversationId, ChatMessag chatMessag) {

        Conversation conversation=conversationRepository.findById(conversationId).orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND,"Unable to find ID"+ conversationId));

        String matchProfileId=conversation.profileId();


        Profile profile= profileRepository.findById(matchProfileId).orElseThrow(
                ()->new ResponseStatusException(HttpStatus.NOT_FOUND,"Unable to finf ID: "+chatMessag.profileId())
        );

        Profile user= profileRepository.findById(chatMessag.profileId()).orElseThrow(
                ()->new ResponseStatusException(HttpStatus.NOT_FOUND,"Unable to finf ID: "+chatMessag.profileId())
        );

        ChatMessag chatMessag1=new ChatMessag(
                chatMessag.textMessage(),
                chatMessag.profileId(),
                LocalDateTime.now()
        );

        conversation.messages().add(chatMessag1);
         chatLLMService.generateProfileResponse(conversation, profile,user);
//        conversation.messages().add(llmMessages);
        conversationRepository.save(conversation);
        return conversation;

    }

    public Conversation getConversation(String conversationId) {

        return  conversationRepository.findById(conversationId)
                .orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND,"Unable to find the ID: "+conversationId));
    }


public HashMap<String, String> jsoData() throws FileNotFoundException
{

    BufferedReader bufferedReader = new BufferedReader(new FileReader(PROFILES_FILE_PATH));

    Gson gson = new Gson();
    HashMap<String, String> json = gson.fromJson(bufferedReader, HashMap.class);
    return json;
}

    public Profile randomProfile() {

        return profileRepository.randomProfile();
    }

    public Match createNewMatch(CreateMatchRequest createMatchRequest) {
        Profile profile=profileRepository.findById(createMatchRequest.profileId()).orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND));
        Conversation conversation=new Conversation(
                BotUtility.generateUId(),
                profile.id(),
                new ArrayList<>());

        Conversation conversation1=conversationRepository.save(conversation);
        Match match =new Match(BotUtility.generateConversationID(), profile, conversation1.id());
        return matchRepository.save(match);
    }


}
