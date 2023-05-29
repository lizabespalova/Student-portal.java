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
        var chatId = update.getCallbackQuery().getMessage().getChatId();
        long customerID=0;
        long performerID=0;
        for(int i=0; i<roomsRepository.count();i++){
            if(roomsRepository.findById(i+1).get().getRoomID().equals(update.getCallbackQuery().getMessage().getChat().getId())){
                customerID = roomsRepository.findById(i+1).get().getCustomerID();
                performerID = roomsRepository.findById(i+1).get().getPerformerID();
            }
        }

        try {
            //Генерувати нове посилання на кімнату
            deleteMember(update.getCallbackQuery().getMessage().getChat().getId(), customerID);
            deleteMember(update.getCallbackQuery().getMessage().getChat().getId(), performerID);
            for(int i=0; i<roomsRepository.count();i++){
                String inviteLink = "";
                if(roomsRepository.findById(i+1).get().getRoomID().equals(update.getCallbackQuery().getMessage().getChat().getId())){
                    String newChatLink = "";
                    long chanels = roomsRepository.findById(i+1).get().getRoomID();
                    ExportChatInviteLink exportChatInviteLink = new ExportChatInviteLink();
                    exportChatInviteLink.setChatId(chanels);
                    try {
                        // Send the message
                        newChatLink = helpbot.execute(exportChatInviteLink);
                    } catch (TelegramApiException e) {
                        e.printStackTrace();
                    }
                    Rooms rooms = roomsRepository.findById(i+1).get();
                    rooms.setChatLink(newChatLink);
                    rooms.setIsFree(true);
                    rooms.setStateInChat(0);
                    rooms.setFollowing(0);
                    roomsRepository.save(rooms);
                    break;
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean apply(Update update) {
        var messagetext = update.getCallbackQuery().getMessage().getText();
        return messagetext.equals("YESSURE");
    }
}
