package com.studentportal.StudentPortal.Helpbot.service.command.hasmessagecommands;

import com.studentportal.StudentPortal.Helpbot.model.CustomerRepository;
import com.studentportal.StudentPortal.Helpbot.model.RoomsRepository;
import com.studentportal.StudentPortal.Helpbot.service.consts.Quiz;
import com.studentportal.StudentPortal.Helpbot.service.mainclasses.Helpbot;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.io.IOException;
@Component
public class HasDocumentCommand  extends HasMessageCommands{
    HasDocumentCommand(Helpbot helpbot, CustomerRepository customerRepository, RoomsRepository roomsRepository) {
        super(helpbot, customerRepository, roomsRepository);
    }

    @Override
    public void resolve(Update update) {
//        const_text = new Text();
        try {
            get_file_description(update);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public boolean apply(Update update) {
        Message message = update.getMessage();
        return update.getMessage().hasDocument() && customerRepository.findById(message.getChatId()).get().getState().equals(Quiz.TEXTDESCRIPTION.toString());
    }
}
