package com.studentportal.helpbot.service.consts;

import com.vdurmont.emoji.EmojiParser;
import lombok.Getter;

@Getter
public class Text {
    public static final String back_text =  EmojiParser.parseToUnicode(":back:"+"Назад");
    public static final String ready_text =  EmojiParser.parseToUnicode("Готово"+":+1:");
    public static final String end_text =  EmojiParser.parseToUnicode("Відмінити"+":x:");
    public static final String agreement_text =  EmojiParser.parseToUnicode("Зробити оголошення"+":woman_student:");
    public static final String thiefText = EmojiParser.parseToUnicode("Шахраї"+":octopus:");
    public static final String matem_text =   EmojiParser.parseToUnicode("Математика" + ":1234:");
    public static final String programming_text=EmojiParser.parseToUnicode("Програмування" + ":computer:");
    public static final String medicine_text=EmojiParser.parseToUnicode("Медицина" + ":woman_health_worker:");
    public static final String phylosophy_text=EmojiParser.parseToUnicode("Філософія" + ":book:");
    public static final String languages_text= EmojiParser.parseToUnicode("Мови" + ":globe_with_meridians:");
    public static final String chemistry_text= EmojiParser.parseToUnicode("Хімія" + ":alembic: ");
    public static final String geographe_text =EmojiParser.parseToUnicode("Географія" + ":earth_americas:");
    public static final String another_text = "Інше...";
    public static final String fix_price_text = "Фіксована";
    public static final String agreement_price_text = "Домовлена";
    public static final String active_state = EmojiParser.parseToUnicode("Активно" + ":white_check_mark:");
    public static final String passive_state = EmojiParser.parseToUnicode("Зроблено" + ":x:");
    public static final String price_text = EmojiParser.parseToUnicode(":moneybag:" + "Ціна");
    public static final String change_text =  EmojiParser.parseToUnicode("Змінити"+":carousel_horse:");
    public static final String public_text =  EmojiParser.parseToUnicode("Публікувати"+":loudspeaker:");
    public static final String change_subject = EmojiParser.parseToUnicode("Змінити галузь" + ":mortar_board:");
    public static final String change_description = EmojiParser.parseToUnicode("Змінити опис" + ":shark:");
    public static final String change_price =  EmojiParser.parseToUnicode("Змінити вартість" + ":money_with_wings:");
    public static final String change_photo_or_file = EmojiParser.parseToUnicode("Змінити фото" + ":selfie:");
    public static final String take_button = EmojiParser.parseToUnicode("Беру" + ":handshake: ");
    public static final String warning = EmojiParser.parseToUnicode("Не можемо опублікувати цей пост, спробуйте ще"+":confused:" );
    public static final String first_performer = EmojiParser.parseToUnicode("Це перша угода ");
    public static final String agree_text = EmojiParser.parseToUnicode("Спілкуватися"+":handshake:");
    public static final String cancel_text = EmojiParser.parseToUnicode("Скасувати"+":x:");
    public static final String want_registrate = EmojiParser.parseToUnicode("Хочу зареєструватися"+":smiley_cat:");
    public static final String go_chat = EmojiParser.parseToUnicode("Перейти в чат"+":eye:");
    public static final String chat_text = EmojiParser.parseToUnicode("Вкажіть, будь ласка, ціну. У випадку закінчення угоди, натисніть будь ласка на кнопку \"Кінець угоди\" та пройдіть опитування. Якщо ви помітили співрозмовника у шахрайстві, зверніться до адміністрації, робіть це тайно. Успіхів!)");
    public static final String performerRegister = EmojiParser.parseToUnicode("Зареєструватися, як виконавець"+":woman_office_worker:");
    public static final String priceChat = EmojiParser.parseToUnicode("Ціна"+":moneybag:");
    public static final String endBargain = EmojiParser.parseToUnicode("Завершити угоду"+":end:");
    public static final String title = EmojiParser.parseToUnicode("Форма для оплати"+":page_with_curl:");
    public static final String invocieDescription = EmojiParser.parseToUnicode("Шановний користувач, сплатіть, будь ласка, вартість вашого завдання з додаванням відсотка за користування ботом.(до 200 грн - 10 грн, 201грн та вище-+5% від вартості завдання). "+":credit_card:"+". Коли виконавець виконає ваше завдання,гроші будуть переведено виконавцю. У випадку підозри на шахрайство/шахрайства зверніться до адміністратора. (у закріпленому повідомленні або в групі.)"+"\n:heavy_exclamation_mark:Увага: з виконавця буде снято такий самий відсоток"+"\nСума, яку необхідно сплатити користувачу:");
    public static final String formDescription = EmojiParser.parseToUnicode("Для оплати користувачеві"+":pound: ");
    public static final String pay = EmojiParser.parseToUnicode("Сплатити"+":pound:");
    public static final String msgPayYes =  EmojiParser.parseToUnicode("Шановний виконавець, користувач оплатив завдання. Ви можете розпочинати роботу"+":white_check_mark:");
    public static final String blockPay =  EmojiParser.parseToUnicode("Ціну вже було обговорено та угода оплачена. Навряд чи ви хочете сплачувати двічі"+":upside_down:");
    public static final String isPaid =  EmojiParser.parseToUnicode("Угоду оплачено"+":+1:");
    public static final String littleMoney = EmojiParser.parseToUnicode("\"Замало/Забагато грошей. Ми знаємо, що забагато грошей не буває, але бот не витримає ваш апетит\""+":smirk:");
    public static final String warningToCustomer = EmojiParser.parseToUnicode("Шановний користувач, виконавець покинув чат. Якщо це не очікувано та не заплановано, зверніться до адміністратору для повернення ваших грошей та обговорення ситуації, виконавця може бути внесено до чорного списку."+":pensive:"+"\nАдміністрація: @lizabespalova");
    public static final String warningToPerformer = EmojiParser.parseToUnicode("Шановний виконавець, користувач чомусь покинув чат, не пройшовши опитування. Якщо це сталося випадково, то він має право повернути гроші, звернувшись до адміністрації. В такому випадку ми не можемо заплатити вам за завдання, але якщо ви почали його виконувати ви маєте право отримати свої гроші. Для цього вам потрібно звернутися до адміністратора та довести свою працю фото протягом дня. Якщо користувач просто не закрив угоду, то ви також звертаєся до адміністрації та отримуєту свої гроші.\n"+":bangbang:"+"Велике прохання, не видаляти чат, щоб адміністрація могла подивитися в нього. У випадку видалення чату гроші можуть бути повернені протягом 3-х днів\nАдміністрація: @lizabespalova");
    public static final String byeTxt =EmojiParser.parseToUnicode(" Оцінку враховано. Дякуємо за угоду, ми видалили вас, тому що вона закінчена. Сподіваюсь, що вам сподобалось"+":slight_smile: ");
    public static final String myCard = EmojiParser.parseToUnicode("Я перевірив, це моя картка"+":ok_hand:");
    public static final String areYouSure = EmojiParser.parseToUnicode("Перевірте ще раз уважно номер своєї картки. Не гнівайтеся за довге <що?де?коли?>,це для вашої же безпеки"+":woman_teacher:");
    public static final String byeTxtPerformer = EmojiParser.parseToUnicode("Дякуємо за угоду, ми видалили вас, тому що вона закінчена. Сподіваюсь, що вам сподобалось"+":slight_smile: ");
    public static final String cleaning = ("17032004adminLiza");
    public static final String thiefMenu = ("Оберіть, будь ласка, дію:");
    public static final String thiefList = ("Список");
    public static final String thiefAdd = ("Скарга");
    public static final String thiefId = ("Спочатку введи id");
    public static final String deleteThiefText = ("Для видалення шахрая з бази, введи його ID");
    public static final String deleteThief = ("Видалити");
    public static final String addThiefSurname = ("Вкажіть фамілію шахрая");
    public static final String addThiefName = ("Вкажіть ім'я шахрая");
    public static final String addThiefNick = ("Вкажіть нік шахрая");
    public static final String readyThief = ("Шахрая добавлено");
    public static final String checkThief=("Перевірка");
    public static final String thiefListToAppand = "Список шахраїв: ";
    public static final String leftSide = EmojiParser.parseToUnicode(":arrow_left:");
    public static final String rightSide = EmojiParser.parseToUnicode(":arrow_right:");
    public static final String setMe = EmojiParser.parseToUnicode("Для того, щоб додати людину до шахрайника, вам потрібно спочатку це довести. Напишіть адміністрації, для доведення шахрайства (фото, скріни, голосові повідомлення та ін.)\nАдміністрація: @lizabespalova");
    public static final String thiefCheck = EmojiParser.parseToUnicode("Для того, щоб перевірити чи є ваш виконавець шахраєм, просто перешліть його будь-яке смс сюди.");
    public static final String thiefExists = EmojiParser.parseToUnicode("На жаль, ця людина є у списках шахраїв. Не рекомендовано з нею продовжувати працю.");
    public static final String thiefDoesntExists = EmojiParser.parseToUnicode("Людина раніше не була схоплена за шахрайство");
    public static final String star = EmojiParser.parseToUnicode(":star:");
    public static final String hellestar = EmojiParser.parseToUnicode(":star2:");
    public static final String dunklestar = EmojiParser.parseToUnicode(":sparkles:");
}