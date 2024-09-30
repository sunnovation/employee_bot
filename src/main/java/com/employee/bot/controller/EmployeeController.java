package com.employee.bot.controller;

import com.employee.bot.conversation.ChatMessag;
import com.employee.bot.conversation.Conversation;
import com.employee.bot.conversation.ProfileRequest;
import com.employee.bot.match.CreateMatchRequest;
import com.employee.bot.match.Match;
import com.employee.bot.profile.Profile;
import com.employee.bot.service.BotService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class EmployeeController {



    private final BotService botService;
    public EmployeeController(BotService botService){
        this.botService=botService;
    }

    @PostMapping("/conversations")
    public ResponseEntity<Void> createConversation(@RequestBody ProfileRequest profileRequest){

        botService.createConversation(profileRequest);
        return new ResponseEntity(HttpStatus.NO_CONTENT);

    }
    @PostMapping("/conversations/{conversationId}")
    public Conversation addMessageForConversation(
            @PathVariable("conversationId") String conversationId,
            @RequestBody ChatMessag chatMessag){
        return botService.addMessageForConversation(conversationId,chatMessag);

    }

    @PostMapping("/conversations/match/{conversationId}")
    public Conversation addMessageForConversationMatch(
            @PathVariable("conversationId") String conversationId,
            @RequestBody ChatMessag chatMessag){
        return botService.addMatchMessageForConversation(conversationId,chatMessag);

    }

    @GetMapping("/conversations/{conversationId}")
    public Conversation getConversation(
            @PathVariable("conversationId") String conversationId){
        return botService.getConversation(conversationId);

    }

    @GetMapping("/randomProfile")
    public Profile randomProfile(){
        return botService.randomProfile();
    }

    @PostMapping("/matches")
    public Match createNewMatch(@RequestBody CreateMatchRequest createMatchRequest){
        return botService.createNewMatch(createMatchRequest);
    }


}
