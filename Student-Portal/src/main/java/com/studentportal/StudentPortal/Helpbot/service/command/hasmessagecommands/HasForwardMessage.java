package com.studentportal.StudentPortal.Helpbot.service.command.hasmessagecommands;

import com.studentportal.StudentPortal.Helpbot.model.CustomerRepository;
import com.studentportal.StudentPortal.Helpbot.model.RoomsRepository;
import com.studentportal.StudentPortal.Helpbot.service.mainclasses.Helpbot;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

public class HasForwardMessage extends HasMessageCommands{
    HasForwardMessage(Helpbot helpbot, CustomerRepository customerRepository, RoomsRepository roomsRepository) {
        super(helpbot, customerRepository, roomsRepository);
    }

    @Override
    public void resolve(Update update) {
        Message message = update.getMessage();
        if(customerRepository.findById(message.getFrom().getId()).get().getThiefListState()==50)
            checkThiefFromCustomer(message);
    }

    @Override
    public boolean apply(Update update) {
        Message message = update.getMessage();
        return message.getForwardFrom()!=null;
    }
}
