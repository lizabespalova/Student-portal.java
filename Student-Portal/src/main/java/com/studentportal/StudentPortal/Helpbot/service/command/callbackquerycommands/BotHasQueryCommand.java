package com.studentportal.StudentPortal.Helpbot.service.command.callbackquerycommands;



import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;

public interface BotHasQueryCommand  {


    void resolve(Update update);

    boolean apply(Update update);



    @Component
    @AllArgsConstructor
    class CommandCallbackQueryFactory {

        private final List<BotHasQueryCommand> commands;

        public BotHasQueryCommand getCommand(Update update) {
            for (BotHasQueryCommand command : commands) {
                if (command.apply(update)) {
                    return command;
                }
            }
            throw new IllegalArgumentException();
        }


    }


}
