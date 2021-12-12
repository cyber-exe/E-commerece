package bot;


import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import lombok.Data;
import lombok.SneakyThrows;
import model.user.Buyer;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import service.user_service.BuyerService;

import java.time.LocalDate;
import java.util.*;

public class TgBot extends TelegramLongPollingBot implements TelegramBotUtils {
    private String chatId;
    private String message;
    private String lang;
    private State state;
    private BuyerService buyerService;
    private Buyer buyer;
    private boolean checkFromData;

    Stack<ReplyKeyboard> menues = new Stack<>();
    Stack<String> headerOfMenu = new Stack<>();

    HashMap<String, ManageLang> manageLangList = new HashMap<String, ManageLang>();

    {
        buyerService = new BuyerService();
        buyer = new Buyer();
        this.state = State.SELLECT_LANG;
        manageLangList.put("Uzbek", new ContentUz());
        manageLangList.put("Russian", new ContentRu());
        manageLangList.put("English", new ContentEng());
    }

    @Override
    public String getBotUsername() {
        return BOT_USERNAME;
    }

    @Override
    public String getBotToken() {
        return BOT_TOKEN;
    }

    @SneakyThrows
    @Override
    public void onUpdateReceived(Update update) {
        if(update.hasMessage()) {
            this.chatId = update.getMessage().getChatId().toString();
            buyer.setName(update.getMessage().getFrom().getFirstName());
            buyer.setSecondName(update.getMessage().getFrom().getLastName());

            String text = update.getMessage().getText();

            if(text.equals("/start")) {
                this.message = "Assalomu alaykum. Tilni kiriting!\nHello, select language!\nПривет, выберите язык!";
                String main_header = manageLangList.getOrDefault(this.lang, new ContentEng()).enter_to_system;
                this.menues.push(mainMenu());
                this.headerOfMenu.push(main_header);
                this.state = State.REGISTER_MENU;

                this.execute(langMenu(), this.message);
                this.execute(mainMenu(), main_header);

            } else if(text.equals("Uzbek") || text.equals("Russian") || text.equals("English")) {
                this.lang = text;
                this.execute(manageLangList.getOrDefault(text, new ContentEng()).selected_lang);

            } else if(this.state == State.ENTER_EMAIL) {
                execute("sizning emailingiz: " + text);
                buyer.setEmail(text);

                this.state = State.ENTER_PHONE;
                execute(manageLangList.getOrDefault(this.lang, new ContentEng()).enter_phone);
            } else if(this.state == State.ENTER_PHONE) {
                execute("sizning tel raqamingiz: " + text);
                buyer.setPhone(text);

                if(this.checkFromData) {
                    if(buyerService.check(buyer)) {
                        String txt = manageLangList.getOrDefault(this.lang, new ContentEng()).main_header;
                        this.state = State.BUYER_MENU;
                        this.menues.push(buyerMenu());
                        this.headerOfMenu.push(txt);
                        execute(buyerMenu(), txt);
                    } else {
                        execute(mainMenu(), manageLangList.getOrDefault(this.lang, new ContentEng()).input_error);
                    }
                } else {
                    this.state = State.ENTER_AGE;
                    execute(manageLangList.getOrDefault(this.lang, new ContentEng()).enter_age);
                }
            } else if(this.state == State.ENTER_AGE) {
                execute("sizning yoshingiz: " + text);

                try {
                    buyer.setAge(Integer.parseInt(text));
                    buyer.setCreatedAt(LocalDate.now());
                    buyerService.add(buyer);
                    String txt = manageLangList.getOrDefault(this.lang, new ContentEng()).main_header;
                    this.state = State.BUYER_MENU;
                    this.menues.push(buyerMenu());
                    this.headerOfMenu.push(txt);
                    execute(buyerMenu(), txt);
                } catch (NumberFormatException e) {
                    execute("Yosh xato, qayta kiriting");
                    e.printStackTrace();
                }
            }
        } else if(update.hasCallbackQuery()) {
            this.chatId = update.getCallbackQuery().getMessage().getChatId().toString();
            String data = update.getCallbackQuery().getData();

            if(State.SIGN_UP.toString().equals(data)) {
                execute(manageLangList.getOrDefault(this.lang, new ContentEng()).enter_email);
                this.state = State.ENTER_EMAIL;
            } else if(State.SIGN_IN.toString().equals(data)) {
                execute(manageLangList.getOrDefault(this.lang, new ContentEng()).enter_email);
                this.state = State.ENTER_EMAIL;
                this.checkFromData = true;
            } else if(data.equals("ABOUT_ME")) {
//                List<List<InlineKeyboardButton>> l = new ArrayList<>();
//                List<InlineKeyboardButton> row = new ArrayList<>();
//                InlineKeyboardButton btn = new InlineKeyboardButton();
//                btn.setText(manageLangList.getOrDefault(this.lang, new ContentEng()).prev);
//                btn.setCallbackData("PREV");

                execute(buyer.toString());
            } else if(data.equals("PREV")) {
                this.menues.pop();
                this.headerOfMenu.pop();
                if(this.menues.peek() instanceof InlineKeyboardMarkup) {
                    this.execute((InlineKeyboardMarkup) this.menues.peek(), this.headerOfMenu.peek());
                } else {
                    this.execute((ReplyKeyboardMarkup) this.menues.peek(), this.headerOfMenu.peek());
                }
            }
        }
    }

    public InlineKeyboardMarkup buyerMenu() {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> list = new ArrayList<>();

        inlineKeyboardMarkup.setKeyboard(list);

        InlineKeyboardButton inlineKeyboardButton = new InlineKeyboardButton();

        inlineKeyboardButton.setText(manageLangList.getOrDefault(this.lang, new ContentEng()).products);
        inlineKeyboardButton.setCallbackData("PRODUCT_LIST");
        List<InlineKeyboardButton> row = new ArrayList<>();
        row.add(inlineKeyboardButton);

        InlineKeyboardButton inlineKeyboardButton1 = new InlineKeyboardButton();
        inlineKeyboardButton1.setText(manageLangList.getOrDefault(this.lang, new ContentEng()).my_basket);
        inlineKeyboardButton1.setCallbackData("MY_BASKET");
        List<InlineKeyboardButton> row1 = new ArrayList<>();
        row1.add(inlineKeyboardButton1);

        InlineKeyboardButton inlineKeyboardButton2 = new InlineKeyboardButton();
        inlineKeyboardButton2.setText(manageLangList.getOrDefault(this.lang, new ContentEng()).about_me);
        inlineKeyboardButton2.setCallbackData("ABOUT_ME");
        List<InlineKeyboardButton> row2 = new ArrayList<>();
        row2.add(inlineKeyboardButton2);

        InlineKeyboardButton inlineKeyboardButton3 = new InlineKeyboardButton();
        inlineKeyboardButton3.setText(manageLangList.getOrDefault(this.lang, new ContentEng()).prev);
        inlineKeyboardButton3.setCallbackData("PREV");
        List<InlineKeyboardButton> row3 = new ArrayList<>();
        row3.add(inlineKeyboardButton3);

        list.add(row);
        list.add(row1);
        list.add(row2);
        list.add(row3);

        return inlineKeyboardMarkup;
    }

    public InlineKeyboardMarkup mainMenu() {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> list = new ArrayList<>();

        inlineKeyboardMarkup.setKeyboard(list);

        InlineKeyboardButton inlineKeyboardButton = new InlineKeyboardButton();

        inlineKeyboardButton.setText(manageLangList.getOrDefault(this.lang, new ContentEng()).sign_in);
        inlineKeyboardButton.setCallbackData("SIGN_IN");
        List<InlineKeyboardButton> row = new ArrayList<>();
        row.add(inlineKeyboardButton);

        InlineKeyboardButton inlineKeyboardButton1 = new InlineKeyboardButton();
        inlineKeyboardButton1.setText(manageLangList.getOrDefault(this.lang, new ContentEng()).sign_up);
        inlineKeyboardButton1.setCallbackData("SIGN_UP");


        List<InlineKeyboardButton> row1 = new ArrayList<>();
        row1.add(inlineKeyboardButton1);

        list.add(row);
        list.add(row1);

        return inlineKeyboardMarkup;
    }

    public ReplyKeyboardMarkup langMenu() {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        List<KeyboardRow> keyboardRows = new ArrayList<>();
        replyKeyboardMarkup.setKeyboard(keyboardRows);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setOneTimeKeyboard(true);
        replyKeyboardMarkup.setInputFieldPlaceholder("Language...");

        KeyboardRow keyboardRow = new KeyboardRow();
        keyboardRow.add("Uzbek");

        KeyboardRow keyboardRow1 = new KeyboardRow();
        keyboardRow1.add("Russian");

        KeyboardRow keyboardRow2 = new KeyboardRow();
        keyboardRow2.add("English");

        keyboardRows.add(keyboardRow);
        keyboardRows.add(keyboardRow1);
        keyboardRows.add(keyboardRow2);

        return replyKeyboardMarkup;
    }

    private void execute(ReplyKeyboardMarkup menu, String text) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setText(text);
        sendMessage.setChatId(this.chatId);
        sendMessage.setReplyMarkup(menu);

        try {
            super.execute(sendMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private void execute(InlineKeyboardMarkup menu, String text) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setText(text);
        sendMessage.setChatId(this.chatId);
        sendMessage.setReplyMarkup(menu);

        try {
            super.execute(sendMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private void execute(String text) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(this.chatId);
        sendMessage.setText(text);

        try {
            super.execute(sendMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

}
