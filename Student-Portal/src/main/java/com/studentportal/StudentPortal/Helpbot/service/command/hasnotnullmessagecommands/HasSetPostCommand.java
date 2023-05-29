package com.studentportal.StudentPortal.Helpbot.service.command.hasnotnullmessagecommands;

import com.studentportal.StudentPortal.Helpbot.model.Customer;
import com.studentportal.StudentPortal.Helpbot.model.CustomerRepository;
import com.studentportal.StudentPortal.Helpbot.model.RoomsRepository;
import com.studentportal.StudentPortal.Helpbot.service.consts.Quiz;
import com.studentportal.StudentPortal.Helpbot.service.mainclasses.Helpbot;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

public class HasSetPostCommand extends HasNotNullMessageCommands{
    public HasSetPostCommand(Helpbot helpbot, CustomerRepository customerRepository, RoomsRepository roomsRepository) {
        super(helpbot, customerRepository, roomsRepository);
    }

    @Override
    public void resolve(Update update) {
        Message message = update.getMessage();
        String user_sms = message.getText();
        if(!customerRepository.findById(message.getChatId()).get().getState().isEmpty()) {
            if (customerRepository.findById(message.getChatId()).get().getState().equals(Quiz.FIXPRICE.toString())) {
                boolean check_price = debug_fix_price(user_sms, message.getChatId(), message);
                if (check_price) {
                    set_post(String.valueOf(message.getChatId()), 0, message.getChatId(), message);
                    set_last_buttons(String.valueOf(message.getChatId()));
                } else {
                    set_fix_price_menu(String.valueOf(message.getChatId()), message);
                }
                //редагування коду
                Customer customer = customerRepository.findById(message.getChatId()).get();
                customer.setAgreementsState(true);
                customer.setCheck_state(false);
                customer.setPriceFlag(1);
                customerRepository.save(customer);
            }
        }
    }

    @Override
    public boolean apply(Update update) {
        Message message = update.getMessage();
        return !customerRepository.findById(message.getChatId()).isEmpty();
    }
}
