package com.studentportal.StudentPortal.Helpbot.service.command.hasnotnullmessagecommands;

import com.studentportal.StudentPortal.Helpbot.model.CustomerRepository;
import com.studentportal.StudentPortal.Helpbot.model.RoomsRepository;
import com.studentportal.StudentPortal.Helpbot.service.command.hasmessagecommands.HasMessageCommands;
import com.studentportal.StudentPortal.Helpbot.service.consts.Text;
import com.studentportal.StudentPortal.Helpbot.service.mainclasses.Helpbot;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Component
public class HasForwardMessage extends HasNotNullMessageCommands {
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
