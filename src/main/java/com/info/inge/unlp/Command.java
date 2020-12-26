package com.info.inge.unlp;

import discord4j.core.event.domain.message.MessageCreateEvent;

public interface Command {
    void execute(MessageCreateEvent event);
    String help();
    String getName();
}
