package com.studentportal.StudentPortal.Mainbot.service;
import com.studentportal.StudentPortal.Mainbot.config.MainbotConfig;
import com.vdurmont.emoji.EmojiParser;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.commands.scope.BotCommandScopeDefault;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import java.util.ArrayList;
import java.util.List;
//@Component
public class MainBot extends TelegramLongPollingBot {
    final MainbotConfig config;
    private String user_message;
    private Buttons buttons;
    private String callbackdata;
    private long messageID;
    private String text;
    private final String rule_text =  EmojiParser.parseToUnicode("Правила користування ботом"+":clipboard:");

    public MainBot(MainbotConfig config) {
        this.config = config;
        List<BotCommand> listofCommands = new ArrayList<>();
        text = EmojiParser.parseToUnicode(":swan:"+" "+"Оберіть свій статус");
        listofCommands.add(new BotCommand("/start", text));
        text = EmojiParser.parseToUnicode(":dove:"+" "+"Допомога");
        listofCommands.add(new BotCommand("/help",text));
        try {
            this.execute(new SetMyCommands(listofCommands, new BotCommandScopeDefault(), null));
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getBotUsername() {
        return config.getUsername();
    }

    @Override

    public String getBotToken() {
        return config.getToken();
    }

    @Override

    public void onUpdateReceived(Update update) {
        Message message = update.getMessage();
        if (update.hasCallbackQuery()) {
            if(update.getCallbackQuery().getData().equals(buttons.PERFOMER.toString())){
                setMenuChoice(update.getCallbackQuery().getMessage());
            }
        }
        if(message.hasText() && update.getMessage().hasText()){
            user_message = message.getText();
            if(user_message.equals(rule_text)){
                rules_buttons(String.valueOf(message.getChatId()));
            }
            String messagetext = update.getMessage().getText();
           switch (messagetext){
               case "/start": {
                   set_menu_Reply(String.valueOf(message.getChatId()));
                   set_menu_Inline(String.valueOf(message.getChatId()));
                   break;
               }
               case "/help": {
                   help_button(String.valueOf(message.getChatId()));
                   break;
               }
               default:
                  /* send_default_sms(String.valueOf(message.getChatId()));*/
           }
        }

    }

    public void set_menu_Reply(String chatId){
        SendMessage main_menu_sms = new SendMessage();
        main_menu_sms.setChatId(chatId);
        text = EmojiParser.parseToUnicode("Привіт, друже" + ":wave:");
        main_menu_sms.setText(text);
        ReplyKeyboardMarkup keyboard = new ReplyKeyboardMarkup();
        text = EmojiParser.parseToUnicode("Правила користування ботом"+":clipboard:");
        List<KeyboardRow> menu = new ArrayList<>();
        KeyboardRow row = new KeyboardRow();
        row.add(text);
        menu.add(row);
        keyboard.setKeyboard(menu);
        keyboard.setResizeKeyboard(true);
        keyboard.setOneTimeKeyboard(false);
        keyboard.setSelective(true);
        main_menu_sms.setReplyMarkup(keyboard);
        try {
            // Send the message
            execute(main_menu_sms);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
    public void set_menu_Inline(String chatId){
        SendMessage main_menu_sms = new SendMessage();
        main_menu_sms.setChatId(chatId);
        main_menu_sms.setText("Обери дію:");
        InlineKeyboardMarkup inline_keybord = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rows_inline = new ArrayList<>();
        List<InlineKeyboardButton> row_inline=new ArrayList<>();
        var customer_Button = new InlineKeyboardButton();
        text = EmojiParser.parseToUnicode("Я замовник" + ":woman_office_worker:");
        customer_Button.setText( text );
        customer_Button.setUrl("t.me/helpstudentportal_bot");
        customer_Button.setCallbackData(buttons.CUSTOMER.toString());
        var permormer_Button = new InlineKeyboardButton();
        text = EmojiParser.parseToUnicode("Я виконавець" + ":woman_technologist:");
        permormer_Button.setText(text);
        permormer_Button.setCallbackData(buttons.PERFOMER.toString());
        row_inline.add(customer_Button);
        row_inline.add(permormer_Button);
        rows_inline.add(row_inline);
        inline_keybord.setKeyboard(rows_inline);
        main_menu_sms.setReplyMarkup(inline_keybord);
        try{
            execute(main_menu_sms);
        }catch(TelegramApiException e){
            e.printStackTrace();
        }
    }
    public void rules_buttons(String chatId){
        SendMessage main_menu_sms = new SendMessage();
        main_menu_sms.setChatId(chatId);
        text = EmojiParser.parseToUnicode("Обери тип інформації, з яким ти хочешь ознайомитись:"+":book:");
        main_menu_sms.setText(text);
        InlineKeyboardMarkup inline_keybord = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rows_inline = new ArrayList<>();
        List<InlineKeyboardButton> row_inline1 = new ArrayList<>();
        List<InlineKeyboardButton> row_inline2 = new ArrayList<>();
        List<InlineKeyboardButton> row_inline3 = new ArrayList<>();
        var bot_guide = new InlineKeyboardButton();
        text = EmojiParser.parseToUnicode("Правила користування ботом" + ":robot_face:");
        bot_guide.setText(text);
        bot_guide.setUrl("https://telegra.ph/Pravila-koristuvannya-botom-Vedmedik-05-01");
        bot_guide.setCallbackData(buttons.RULEBOT.toString());
        var bot_payment = new InlineKeyboardButton();
        text = EmojiParser.parseToUnicode("Правила оплати" + ":money_with_wings:");
        bot_payment.setText(text);
        bot_payment.setUrl("https://telegra.ph/Pravila-oplati-01-01");
        bot_payment.setCallbackData(buttons.RULEPAYMENT.toString());
        var bot_relations = new InlineKeyboardButton();
        text = EmojiParser.parseToUnicode("Правила угоди" + ":handshake:");
        bot_relations.setText(text);
        bot_relations.setUrl("https://telegra.ph/Pravila-ugodi-01-01");
        bot_relations.setCallbackData(buttons.RULERELATE.toString());
        row_inline1.add(bot_guide);
        row_inline2.add(bot_payment);
        row_inline3.add(bot_relations);
        rows_inline.add(row_inline1);
        rows_inline.add(row_inline2);
        rows_inline.add(row_inline3);
        inline_keybord.setKeyboard(rows_inline);
        main_menu_sms.setReplyMarkup(inline_keybord);
        try{
            execute(main_menu_sms);
        }catch(TelegramApiException e){
            e.printStackTrace();
        }
    }
    public void help_button(String chatId){
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        text =  EmojiParser.parseToUnicode("Якщо виникли питання звертайтесь до адміністратора:"+":rose:"+ "\n\n\n" +"@lizabespalova");
        sendMessage.setText(text);
        try {
            // Send the message
            execute(sendMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }

    }
    public void setMenuChoice(Message message){
        SendMessage main_menu_sms = new SendMessage();
        main_menu_sms.setChatId(message.getChatId());
        main_menu_sms.setText("Обери галузь, в якій ви добре знаєтесь:");
         String programming_text=EmojiParser.parseToUnicode("Програмування" + ":computer:");
         String matem_text =   EmojiParser.parseToUnicode("Математика" + ":1234:");
         String medicine_text=EmojiParser.parseToUnicode("Медицина" + ":woman_health_worker:");
         String phylosophy_text=EmojiParser.parseToUnicode("Філософія" + ":book:");
         String languages_text= EmojiParser.parseToUnicode("Мови" + ":globe_with_meridians:");
         String chemistry_text= EmojiParser.parseToUnicode("Хімія" + ":alembic: ");
         String geographe_text =EmojiParser.parseToUnicode("Географія" + ":earth_americas:");
         String another_text = "Інше...";
        InlineKeyboardMarkup inline_keybord = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rows_inline = new ArrayList<>();

        List<InlineKeyboardButton> row_inline=new ArrayList<>();
        var programming_Button = new InlineKeyboardButton();
        programming_Button.setText(programming_text);
        programming_Button.setCallbackData("PROGRAMMING");
        programming_Button.setUrl("https://t.me/program_vedmedyk/");
        var math_Button = new InlineKeyboardButton();
        math_Button.setText(matem_text);
        math_Button.setCallbackData("MATH");
        math_Button.setUrl("https://t.me/matem_vedmedyk/");
        row_inline.add(programming_Button);
        row_inline.add(math_Button);


        List<InlineKeyboardButton> row1_inline=new ArrayList<>();
        var medicine_button = new InlineKeyboardButton();
        medicine_button.setText(medicine_text);
        medicine_button.setCallbackData("MEDICINE");
        medicine_button.setUrl("https://t.me/medicine_vedmedyk/");
        var chemistry_button = new InlineKeyboardButton();
        chemistry_button.setText(chemistry_text);
        chemistry_button.setCallbackData("CHEMISTRY");
        chemistry_button.setUrl("https://t.me/chemistry_vedmedyk/");
        row1_inline.add(medicine_button);
        row1_inline.add(chemistry_button);

        List<InlineKeyboardButton> row2_inline=new ArrayList<>();
        var phylosophy_button = new InlineKeyboardButton();
        phylosophy_button.setText(phylosophy_text);
        phylosophy_button.setCallbackData("PHYLOSOPHY");
        phylosophy_button.setUrl("https://t.me/phylosophy_vedmedyk/");
        var language_button = new InlineKeyboardButton();
        language_button.setText(languages_text);
        language_button.setCallbackData("LANGUAGES");
        language_button.setUrl("https://t.me/languages_vedmedyk/");
        row2_inline.add(phylosophy_button);
        row2_inline.add(language_button);


        List<InlineKeyboardButton> row3_inline=new ArrayList<>();
        var geography_button = new InlineKeyboardButton();
        geography_button.setText(geographe_text);
        geography_button.setCallbackData("GEOGRAPHY");
        geography_button.setUrl("https://t.me/geogtaphy_vedmedyk/");
        var another_button = new InlineKeyboardButton();
        another_button.setText(another_text);
        another_button.setCallbackData("ANOTHER");
        another_button.setUrl("https://t.me/main_vedmedyk/");
        row3_inline.add(geography_button);
        row3_inline.add(another_button);

        rows_inline.add(row_inline);
        rows_inline.add(row1_inline);
        rows_inline.add(row2_inline);
        rows_inline.add(row3_inline);
        inline_keybord.setKeyboard(rows_inline);
        main_menu_sms.setReplyMarkup(inline_keybord);
        try{
            execute(main_menu_sms);
        }catch(TelegramApiException e){
            e.printStackTrace();
        }
    }

}
