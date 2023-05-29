package com.studentportal.StudentPortal.Helpbot.service.command.hasmessagecommands;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;

public interface BotHasMessageCommand {


    void resolve(Update update);

    boolean apply(Update update);



    @Component
    @AllArgsConstructor
    class CommandHasMessageFactory {

        private final List<BotHasMessageCommand> commands;

        public BotHasMessageCommand getCommand(Update update) {
            for (BotHasMessageCommand command : commands) {
                if (command.apply(update)) {
                    return command;
                }
            }
            throw new IllegalArgumentException();
        }


    }
}
