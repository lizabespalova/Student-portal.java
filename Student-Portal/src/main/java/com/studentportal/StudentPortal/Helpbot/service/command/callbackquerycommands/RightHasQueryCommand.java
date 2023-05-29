package com.studentportal.StudentPortal.Helpbot.service.command.callbackquerycommands;

import com.studentportal.StudentPortal.Helpbot.model.CustomerRepository;
import com.studentportal.StudentPortal.Helpbot.model.RoomsRepository;
import com.studentportal.StudentPortal.Helpbot.service.mainclasses.Helpbot;
import org.telegram.telegrambots.meta.api.objects.Update;

public class RightHasQueryCommand extends QueryCommands {
    public RightHasQueryCommand(Helpbot helpbot, CustomerRepository customerRepository, RoomsRepository roomsRepository) {
        super(helpbot, customerRepository, roomsRepository);
    }

    @Override
    public void resolve(Update update) {
        turnList(false, update.getCallbackQuery().getMessage());
    }

    @Override
    public boolean apply(Update update) {
        var messagetext = update.getMessage().getText();
        return messagetext.equals("RIGHT");
    }
}
