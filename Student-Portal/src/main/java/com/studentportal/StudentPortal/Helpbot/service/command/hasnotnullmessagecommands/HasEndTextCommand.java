package com.studentportal.StudentPortal.Helpbot.service.command.hasnotnullmessagecommands;

import com.studentportal.StudentPortal.Helpbot.model.Customer;
import com.studentportal.StudentPortal.Helpbot.model.CustomerRepository;
import com.studentportal.StudentPortal.Helpbot.model.RoomsRepository;
import com.studentportal.StudentPortal.Helpbot.service.command.Commands;
import com.studentportal.StudentPortal.Helpbot.service.consts.Text;
import com.studentportal.StudentPortal.Helpbot.service.mainclasses.Helpbot;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

public class HasEndTextCommand extends HasNotNullMessageCommands {
    public HasEndTextCommand(Helpbot helpbot, CustomerRepository customerRepository, RoomsRepository roomsRepository) {
        super(helpbot, customerRepository, roomsRepository);
    }

    @Override
    public void resolve(Update update) {
        Message message = update.getMessage();
        helpbot.set_main_menu(String.valueOf(message.getChatId()), message);
        //редагування коду
        Customer customer = customerRepository.findById(message.getChatId()).get();
        customer.setAgreementsState(true);
        customer.setCheck_state(true);
        customer.setPriceFlag(1);
        customerRepository.save(customer);
    }

    @Override
    public boolean apply(Update update) {
        Message message = update.getMessage();
        String user_sms = message.getText();
        return user_sms.equals(Text.end_text);
    }
}
