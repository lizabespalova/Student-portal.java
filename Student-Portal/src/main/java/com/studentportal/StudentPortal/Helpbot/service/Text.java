package com.studentportal.StudentPortal.Helpbot.service;

import com.vdurmont.emoji.EmojiParser;
import lombok.Data;

@Data
public class Text {
    private final String back_text =  EmojiParser.parseToUnicode(":back:"+"Назад");
    private final String end_text =  EmojiParser.parseToUnicode("Відмінити"+":x:");
    private final String ready_text =  EmojiParser.parseToUnicode("Готово"+":+1:");
    private final String agreement_text =  EmojiParser.parseToUnicode("Зробити оголошення"+":woman_student:");
    private final String matem_text =   EmojiParser.parseToUnicode("Математика" + ":1234:");
    private final String programming_text=EmojiParser.parseToUnicode("Програмування" + ":computer:");
    private final String medicine_text=EmojiParser.parseToUnicode("Медицина" + ":woman_health_worker:");
    private final String phylosophy_text=EmojiParser.parseToUnicode("Філософія" + ":book:");
    private final String languages_text= EmojiParser.parseToUnicode("Мови" + ":globe_with_meridians:");
    private final String chemistry_text= EmojiParser.parseToUnicode("Хімія" + ":alembic: ");
    private final String geographe_text =EmojiParser.parseToUnicode("Географія" + ":earth_americas:");
    private final String another_text = "Інше...";
    private final String fix_price_text = "Фіксована";
    private final String agreement_price_text = "Домовлена";
    private final String active_state = EmojiParser.parseToUnicode("Активно" + ":white_check_mark:");
    private final String passive_state = EmojiParser.parseToUnicode("Зроблено" + ":x:");
    private final String price_text = EmojiParser.parseToUnicode(":moneybag:" + "Ціна");
    private final String change_text =  EmojiParser.parseToUnicode("Змінити"+":carousel_horse:");
    private final String public_text =  EmojiParser.parseToUnicode("Публікувати"+":loudspeaker:");
    private final String change_subject = EmojiParser.parseToUnicode("Змінити галузь" + ":mortar_board:");
    private final String change_description = EmojiParser.parseToUnicode("Змінити опис" + ":shark:");
    private final String change_price =  EmojiParser.parseToUnicode("Змінити вартість" + ":money_with_wings:");
    private final String change_photo_or_file = EmojiParser.parseToUnicode("Змінити фото" + ":selfie:");
    private final String take_button = EmojiParser.parseToUnicode("Беру" + ":handshake: ");
    private final String warning = EmojiParser.parseToUnicode("Не можемо опублікувати цей пост, спробуйте ще"+":confused:" );
    public Text() {

    }
}
