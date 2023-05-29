package com.studentportal.StudentPortal.Helpbot.service.command.callbackquerycommands;

import com.studentportal.StudentPortal.Helpbot.model.CustomerRepository;
import com.studentportal.StudentPortal.Helpbot.model.PurchaseRepository;
import com.studentportal.StudentPortal.Helpbot.model.RoomsRepository;
import com.studentportal.StudentPortal.Helpbot.service.mainclasses.Helpbot;
import org.springframework.beans.factory.annotation.Autowired;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.io.IOException;

public class PriceHasQueryCommand extends QueryCommands {
    @Autowired
    private RoomsRepository roomsRepository;
    @Autowired
    private PurchaseRepository purchaseRepository;
    public PriceHasQueryCommand(Helpbot helpbot, CustomerRepository customerRepository, RoomsRepository roomsRepository) {
        super(helpbot, customerRepository,roomsRepository);
    }

    @Override
    public void resolve(Update update) {
        var chatId = update.getCallbackQuery().getMessage().getChatId();
        String Payload = "";
        for(int i=0; i<roomsRepository.count();i++){
            if(roomsRepository.findById(i+1).get().getRoomID().equals(update.getCallbackQuery().getMessage().getChat().getId())){
                Payload = roomsRepository.findById(i+1).get().getPayload();
                break;
            }
        }
        if(Payload==null||purchaseRepository.findById(Payload).get().getPriceToPerformer()==0) {
            try {
                fix_finish_text_price_customer(update.getCallbackQuery().getMessage());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }else{blockActionsWhilePayed(update);}
    }

    @Override
    public boolean apply(Update update) {
        var messagetext = update.getMessage().getText();
        return messagetext.equals("PRICE");
    }
}
