package com.employee.bot.match;

import com.employee.bot.profile.Profile;

public record Match(
        String id,
        Profile profile,
        String conversationId

) {
}
