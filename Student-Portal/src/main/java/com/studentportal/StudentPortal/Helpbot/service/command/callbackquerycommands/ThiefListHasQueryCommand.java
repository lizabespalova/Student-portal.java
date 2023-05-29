package com.studentportal.StudentPortal.Helpbot.service.command.callbackquerycommands;

import com.studentportal.StudentPortal.Helpbot.model.Customer;
import com.studentportal.StudentPortal.Helpbot.model.CustomerRepository;
import com.studentportal.StudentPortal.Helpbot.model.RoomsRepository;
import com.studentportal.StudentPortal.Helpbot.service.consts.Text;
import com.studentportal.StudentPortal.Helpbot.service.dopclasses.CustomerActions;
import com.studentportal.StudentPortal.Helpbot.service.mainclasses.Helpbot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ThiefListHasQueryCommand extends QueryCommands {
    public ThiefListHasQueryCommand(Helpbot helpbot, CustomerRepository customerRepository, RoomsRepository roomsRepository) {
        super(helpbot, customerRepository, roomsRepository);
    }

    @Override
    public void resolve(Update update) {
        var chatId = update.getCallbackQuery().getMessage().getChatId();
        showThiefList(update);
    }

    @Override
    public boolean apply(Update update) {
        var messagetext = update.getMessage().getText();
        return messagetext.equals("THIEFLIST");
    }
    public void showThiefList(Update update){
        String list = "";
        String resultString="";
        try {
            FileReader fileReader = new FileReader("ThiefDataTableDownload");
            BufferedReader br = new BufferedReader(fileReader);
            for(int i=0; i<10;i++){
                list += br.readLine();
            }
            fileReader.close();
            br.close();
            list=list.replace("null", "");
            resultString = thiefRow(list,1);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        CustomerActions customerActions = new CustomerActions(customerRepository);
        list = customerActions.getThiefList(resultString);
        SendMessage sendMessage=new SendMessage();
        sendMessage.setChatId(update.getCallbackQuery().getFrom().getId());
        sendMessage.setText(list);

        InlineKeyboardMarkup inline_keybord = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rows_inline = new ArrayList<>();

        List<InlineKeyboardButton> row_inline=new ArrayList<>();
        var leftButton = new InlineKeyboardButton();
        leftButton.setText(Text.leftSide);
        leftButton.setCallbackData("LEFT");
        var rightButton = new InlineKeyboardButton();
        rightButton.setText(Text.rightSide);
        rightButton.setCallbackData("RIGHT");
        row_inline.add(leftButton);
        row_inline.add(rightButton);
        rows_inline.add(row_inline);
        inline_keybord.setKeyboard(rows_inline);
        sendMessage.setReplyMarkup(inline_keybord);
        try {
            // Send the message
            Message message = helpbot.execute(sendMessage);
            Customer customer = customerRepository.findById(update.getCallbackQuery().getFrom().getId()).get();
            customer.setThiefListState(1);
            customer.setMessageThiefID(message.getMessageId());
            customerRepository.save(customer);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}
