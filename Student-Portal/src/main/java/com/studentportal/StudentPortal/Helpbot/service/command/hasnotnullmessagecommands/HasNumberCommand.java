package com.studentportal.StudentPortal.Helpbot.service.command.hasnotnullmessagecommands;

import com.studentportal.StudentPortal.Helpbot.model.CustomerRepository;
import com.studentportal.StudentPortal.Helpbot.model.RoomsRepository;
import com.studentportal.StudentPortal.Helpbot.service.mainclasses.Helpbot;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

public class HasNumberCommand extends HasNotNullMessageCommands{
    public HasNumberCommand(Helpbot helpbot, CustomerRepository customerRepository, RoomsRepository roomsRepository) {
        super(helpbot, customerRepository, roomsRepository);
    }

    @Override
    public void resolve(Update update) {
        Message message = update.getMessage();
        String user_sms = message.getText();
        int estimate = Integer.parseInt(user_sms);

        long customerID=0;
        for(int i=0; i<roomsRepository.count(); i++){
            if(roomsRepository.findById(i+1).get().getRoomID().equals(message.getChat().getId())){
                customerID = roomsRepository.findById(i+1).get().getCustomerID();
                if(roomsRepository.findById(i+1).get().getFollowing()==1){
                    checkCustomerEstimate(customerID, message,estimate);
                }
                break;
            }
        }
    }

    @Override
    public boolean apply(Update update) {

        Message message = update.getMessage();
        String user_sms = message.getText();
        return user_sms.matches("^-?\\d+$");
    }
}
