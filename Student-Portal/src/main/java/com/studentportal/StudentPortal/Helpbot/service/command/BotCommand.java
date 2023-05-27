package com.studentportal.StudentPortal.Helpbot.service.command;



import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;

public interface BotCommand {


    void resolve(Update update);

    boolean apply(Update update);



    @Component
    @AllArgsConstructor
    class CommandFactory {

        private final List<BotCommand> commands;

        public BotCommand getCommand(Update update) {
            for (BotCommand command : commands) {
                if (command.apply(update)) {
                    return command;
                }
            }
            throw new IllegalArgumentException();
        }


    }


}
