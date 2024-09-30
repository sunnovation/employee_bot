package com.employee.bot.conversation;

import com.employee.bot.profile.Profile;

import java.util.List;

public record Conversation(
        String id,
        String profileId,
        List<ChatMessag> messages


) {
}
