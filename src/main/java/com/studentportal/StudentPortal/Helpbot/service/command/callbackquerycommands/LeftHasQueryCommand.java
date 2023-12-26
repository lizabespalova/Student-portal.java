package com.studentportal.StudentPortal.Helpbot.service.command.callbackquerycommands;

import com.studentportal.StudentPortal.Helpbot.model.CustomerRepository;
import com.studentportal.StudentPortal.Helpbot.model.RoomsRepository;
import com.studentportal.StudentPortal.Helpbot.service.mainclasses.Helpbot;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
@Component
public class LeftHasQueryCommand extends QueryCommands {
    public LeftHasQueryCommand(Helpbot helpbot, CustomerRepository customerRepository, RoomsRepository roomsRepository) {
        super(helpbot, customerRepository, roomsRepository);
    }

    @Override
    public void resolve(Update update) {
        turnList(true, update.getCallbackQuery().getMessage());
    }

    @Override
    public boolean apply(Update update) {
        var messagetext = update.getCallbackQuery().getData();
        return messagetext.equals("LEFT");
    }
}
