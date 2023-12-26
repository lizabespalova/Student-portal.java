package com.studentportal.StudentPortal.Helpbot.service.command.hasmessagecommands;

import com.studentportal.StudentPortal.Helpbot.model.CustomerRepository;
import com.studentportal.StudentPortal.Helpbot.model.RoomsRepository;
import com.studentportal.StudentPortal.Helpbot.service.mainclasses.Helpbot;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
@Component
public class HasSuccessfulPaymentCommand extends HasMessageCommands{
    HasSuccessfulPaymentCommand(Helpbot helpbot, CustomerRepository customerRepository, RoomsRepository roomsRepository) {
        super(helpbot, customerRepository, roomsRepository);
    }

    @Override
    public void resolve(Update update) {
        servePayment(update.getMessage().getSuccessfulPayment());
        blockPayment(update.getMessage().getSuccessfulPayment());
    }

    @Override
    public boolean apply(Update update) {
        return update.getMessage().hasSuccessfulPayment();
    }
}
