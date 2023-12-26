package com.studentportal.StudentPortal.Helpbot.service.command.callbackquerycommands;

import com.studentportal.StudentPortal.Helpbot.model.CustomerRepository;
import com.studentportal.StudentPortal.Helpbot.model.Rooms;
import com.studentportal.StudentPortal.Helpbot.model.RoomsRepository;
import com.studentportal.StudentPortal.Helpbot.service.mainclasses.Helpbot;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.groupadministration.ExportChatInviteLink;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.IOException;
@Component
public class YesSureHasQueryCommand extends QueryCommands {
    public YesSureHasQueryCommand(Helpbot helpbot, CustomerRepository customerRepository, RoomsRepository roomsRepository) {
        super(helpbot, customerRepository, roomsRepository);
    }

    @Override
    public void resolve(Update update) {
        generateNewLink(update);
    }

    @Override
    public boolean apply(Update update) {
        var messagetext = update.getCallbackQuery().getData();
        return messagetext.equals("YESSURE");
    }
}
