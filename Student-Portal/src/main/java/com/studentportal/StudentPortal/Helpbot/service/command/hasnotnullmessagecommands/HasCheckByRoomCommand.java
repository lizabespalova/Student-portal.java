package com.studentportal.StudentPortal.Helpbot.service.command.hasnotnullmessagecommands;

import com.studentportal.StudentPortal.Helpbot.model.*;
import com.studentportal.StudentPortal.Helpbot.service.mainclasses.Helpbot;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.io.IOException;
@Component
public class HasCheckByRoomCommand extends HasNotNullMessageCommands{
    @Autowired
    private PurchaseRepository purchaseRepository;
    public HasCheckByRoomCommand(Helpbot helpbot, CustomerRepository customerRepository, RoomsRepository roomsRepository) {
        super(helpbot, customerRepository, roomsRepository);
    }

    @Override
    public void resolve(Update update) {
        Message message = update.getMessage();
        String user_sms = message.getText();
        if (roomsRepository.findById(getRoomNumb(message)).get().getStateInChat() != null) {
            if (roomsRepository.findById(getRoomNumb(message)).get().getStateInChat() == 0) {
                try {
                    boolean check = check_customers_price(user_sms, message);
                    if(check){
                        //редагування коду
                        Rooms rooms = roomsRepository.findById(getRoomNumb(message)).get();
                        rooms.setPrice(Integer.valueOf(user_sms));
                        rooms.setStateInChat(1);
                        roomsRepository.save(rooms);
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            if(user_sms.matches("^-?\\d+$")){
                for(int i=0; i<roomsRepository.count(); i++){
                    if(roomsRepository.findById(i+1).get().getRoomID().equals(message.getChat().getId())&&roomsRepository.findById(i+1).get().getFollowing()==2){
                        String payload = roomsRepository.findById(i+1).get().getPayload();
                        //редагування коду
                        Purchase purchase = purchaseRepository.findById(payload).get();
                        purchase.setPerformerCard(Long.valueOf(user_sms));
                        purchaseRepository.save(purchase);
                        checkCard(message);
                        break;
                    }
                }

            }
        }
    }

    @Override
    public boolean apply(Update update) {
        Message message = update.getMessage();
        return !roomsRepository.findById(getRoomNumb(message)).isEmpty();
    }
    public int getRoomNumb(Message message){
        int roomNumb = 0;
        for (int i = 0; i < roomsRepository.count(); i++) {
            if (message.getChat().getId().equals(roomsRepository.findById(i + 1).get().getRoomID())) {
                roomNumb = roomsRepository.findById(i + 1).get().getRoomNumber();
                break;
            }
        }
        return roomNumb;
    }
}
