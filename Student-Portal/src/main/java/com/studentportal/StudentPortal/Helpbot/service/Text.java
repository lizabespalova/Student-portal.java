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
    private final String first_performer = EmojiParser.parseToUnicode("Це перша угода ");
    private final String agree_text = EmojiParser.parseToUnicode("Спілкуватися"+":handshake:");
    private final String cancel_text = EmojiParser.parseToUnicode("Скасувати"+":x:");
    private final String want_registrate = EmojiParser.parseToUnicode("Хочу зареєструватися"+":smiley_cat:");
    private final String go_chat = EmojiParser.parseToUnicode("Перейти в чат"+":eye:");
    private final String chat_text = EmojiParser.parseToUnicode("Вкажіть, будь ласка, ціну. У випадку закінчення угоди, натисніть будь ласка на кнопку \"Кінець угоди\" та пройдіть опитування. Якщо ви помітили співрозмовника у шахрайстві, зверніться до адміністрації, робіть це тайно. Успіхів!)");
    private final String performerRegister = EmojiParser.parseToUnicode("Зареєструватися, як виконавець"+":woman_office_worker:");
    private final String priceChat = EmojiParser.parseToUnicode("Ціна"+":moneybag:");
    private final String endBargain = EmojiParser.parseToUnicode("Завершити угоду"+":end:");
    private final String title = EmojiParser.parseToUnicode("Форма для оплати"+":page_with_curl:");
    private final String invocieDescription = EmojiParser.parseToUnicode("Шановний користувач, сплатіть, будь ласка, вартість вашого завдання з додаванням відсотка за користування ботом.(до 200 грн - 10 грн, 201грн та вище-+5% від вартості завдання). "+":credit_card:"+". Коли виконавець виконає ваше завдання,гроші будуть переведено виконавцю. У випадку підозри на шахрайство/шахрайства зверніться до адміністратора. (у закріпленому повідомленні або в групі.)"+"\n:heavy_exclamation_mark:Увага: з виконавця буде снято такий самий відсоток"+"\nСума, яку необхідно сплатити користувачу:");
    private final String formDescription = EmojiParser.parseToUnicode("Для оплати користувачеві"+":pound: ");
    private final String pay = EmojiParser.parseToUnicode("Сплатити"+":pound:");
    private final String msgPayYes =  EmojiParser.parseToUnicode("Шановний виконавець, користувач оплатив завдання. Ви можете розпочинати роботу"+":white_check_mark:");
    private final String blockPay =  EmojiParser.parseToUnicode("Ціну вже було обговорено та угода оплачена. Навряд чи ви хочете сплачувати двічі"+":upside_down:");
    private final String isPaid =  EmojiParser.parseToUnicode("Угоду оплачено"+":+1:");
    private final String littleMoney = EmojiParser.parseToUnicode("\"Замало/Забагато грошей. Ми знаємо, що забагато грошей не буває, але бот не витримає ваш апетит\""+":smirk:");
    private final String warningToCustomer = EmojiParser.parseToUnicode("Шановний користувач, виконавець покинув чат. Якщо це не очікувано та не заплановано, зверніться до адміністратору для повернення ваших грошей та обговорення ситуації, виконавця може бути внесено до чорного списку."+":pensive:"+"\nАдміністрація: @lizabespalova");
    private final String warningToPerformer = EmojiParser.parseToUnicode("Шановний виконавець, користувач чомусь покинув чат, не пройшовши опитування. Якщо це сталося випадково, то він має право повернути гроші звернувшись до адміністрації. В такому випадку ми не можемо заплатити вам за завдання, але якщо ви почали його виконувати ви маєте право отримати свої гроші. Для цього вам потрібно звернутися до адміністратора та довести свою працю фото протягом дня. Якщо користувач просто не закрив угоду, то ви також звертаєся до адміністрації та отримуєту свої гроші.\n"+":bangbang:"+"Велике прохання, не видаляти чат, щоб адміністрація могла подивитися в нього. У випадку видалення чаті грошу можуть бути повернені протягом 3-х днів\nАдміністрація: @lizabespalova");
    private final String byeTxt =EmojiParser.parseToUnicode(" Оцінку враховано. Дякуємо за угоду, ми видалили вас, тому що вона закінчена. Сподіваюсь, що вам сподобалось"+":slight_smile: ");
    private final String myCard = EmojiParser.parseToUnicode("Я перевірив, це моя картка"+":ok_hand:");
    private final String areYouSure = EmojiParser.parseToUnicode("Перевірте ще раз уважно номер своєї картки. Не гнівайтеся за довге <що?де?коли?>,це для вашої же безпеки"+":woman_teacher:");
    private final String byeTxtPerformer =EmojiParser.parseToUnicode("Дякуємо за угоду, ми видалили вас, тому що вона закінчена. Сподіваюсь, що вам сподобалось"+":slight_smile: ");
    private final String cleaning = ("17032004adminLiza");
    public Text() {

    }
}
