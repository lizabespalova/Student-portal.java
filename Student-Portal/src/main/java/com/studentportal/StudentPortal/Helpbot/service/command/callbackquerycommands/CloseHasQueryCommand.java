package com.studentportal.StudentPortal.Helpbot.service.command.callbackquerycommands;

import com.studentportal.StudentPortal.Helpbot.model.CustomerRepository;
import com.studentportal.StudentPortal.Helpbot.model.PurchaseRepository;
import com.studentportal.StudentPortal.Helpbot.model.Rooms;
import com.studentportal.StudentPortal.Helpbot.model.RoomsRepository;
import com.studentportal.StudentPortal.Helpbot.service.mainclasses.Helpbot;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.List;
@Component
public class CloseHasQueryCommand extends QueryCommands {
    @Autowired
    private PurchaseRepository purchaseRepository;
    public CloseHasQueryCommand(Helpbot helpbot, CustomerRepository customerRepository, RoomsRepository roomsRepository) {
        super(helpbot, customerRepository, roomsRepository);
    }

    @Override
    public void resolve(Update update) {
        setSurveyOrEnd(update);
    }

    @Override
    public boolean apply(Update update) {
        var messagetext = update.getCallbackQuery().getData();
        return messagetext.equals("CLOSE");
    }
    public void setSurveyOrEnd(Update update){
        String payloadID="";
        int roomID=0;
        for(int i=0; i<roomsRepository.count();i++){
            if(roomsRepository.findById(i+1).get().getRoomID().equals(update.getCallbackQuery().getMessage().getChat().getId())){
                payloadID = roomsRepository.findById(i+1).get().getPayload();
                roomID = roomsRepository.findById(i+1).get().getRoomNumber();
            }
        }
        if(payloadID == null || !purchaseRepository.findById(payloadID).get().isFlag()){
            setSure(update);
        }
        else{
            SendMessage sendMessage = new SendMessage();
            sendMessage.setChatId(update.getCallbackQuery().getMessage().getChat().getId());
            sendMessage.setText("Користувач, оцініть , будь ласка виконавця за шкалою від 0 до 5. Надішліть у чат цифру. Виконавець побачить вашу оцінку, це не анонімно, але оцінюйте чесно.");
            Rooms rooms = roomsRepository.findById(roomID).get();
            rooms.setFollowing(1);
            roomsRepository.save(rooms);
            try {
                // Send the message
                helpbot.execute(sendMessage);
            } catch (TelegramApiException e){e.printStackTrace();}
        }
    }
    public void setSure(Update update){
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(update.getCallbackQuery().getMessage().getChat().getId());
        sendMessage.setText("ви не домовились, ви впевнені, що хочете завершити угоду?");
        InlineKeyboardMarkup inline_keybord = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rows_inline = new ArrayList<>();
        List<InlineKeyboardButton> row_inline=new ArrayList<>();
        var yesButton = new InlineKeyboardButton();
        yesButton.setText("Так");
        yesButton.setCallbackData("YESSURE");
        var noButton = new InlineKeyboardButton();
        noButton.setText("Ні");
        noButton.setCallbackData("NOSURE");
        row_inline.add(yesButton);
        row_inline.add(noButton);
        rows_inline.add(row_inline);
        inline_keybord.setKeyboard(rows_inline);
        sendMessage.setReplyMarkup(inline_keybord);
        try {
            // Send the message
            helpbot.execute(sendMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}
