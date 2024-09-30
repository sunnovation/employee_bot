package com.employee.bot.conversation;

import java.time.LocalDateTime;

public record ChatMessag(
        String textMessage,
        String profileId,
        LocalDateTime messageTime

) {
}
