package bot;


import lombok.Data;
import lombok.SneakyThrows;
import model.user.Buyer;
import model.user.Gender;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageReplyMarkup;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import service.user_service.BuyerService;

import java.util.*;

public class TgBot extends TelegramLongPollingBot implements TelegramBotUtils {
    private String chatId;
    private String message;
    private String lang;
    private State state;
    private BuyerService buyerService;
    private UserService userService;
    private Buyer buyer;

    Stack<ReplyKeyboard> menues = new Stack<>();
    Stack<String> headerOfMenu = new Stack<>();

    HashMap<String, ManageLang> manageLangList = new HashMap<String, ManageLang>();

    {
        buyerService = new BuyerService();
        userService = new UserService();
        this.state = State.SELLECT_LANG;
        manageLangList.put("UZBEK", new ContentUz());
        manageLangList.put("RUSSIAN", new ContentRu());
        manageLangList.put("ENGLISH", new ContentEng());
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

            buyer = userService.getUserByChatId(update.getMessage().getChatId());

            if(buyer == null)
                buyer = buyerService.add(new Buyer(update.getMessage().getChatId(), update.getMessage().getFrom().getFirstName()));

            String text = update.getMessage().getText();

            if(text.equals("/start")) {
                this.message = "Assalomu alaykum. Tilni kiriting!\nHello, select language!\nПривет, выберите язык!";
                this.execute(langMenu(), this.message);
            }
        } else if(update.hasCallbackQuery()) {
            this.chatId = update.getCallbackQuery().getMessage().getChatId().toString();
            String data = update.getCallbackQuery().getData();
//            update.getCallbackQuery().getMessage().getMessageId();
            if(data.equals("UZBEK") || data.equals("RUSSIAN") || data.equals("ENGLISH")) {
                buyer.setLan(data);
                buyer.setState(State.MAIN_MENU);
                buyerService.edit(buyer);
                execute(manageLangList.getOrDefault(data, new ContentEng()).selected_lang);
                execute(buyer.toString());
                this.state = State.MAIN_MENU;
                EditMessageText editMessageText = new EditMessageText();
                editMessageText.setText("Main menu");
                editMessageText.setChatId(this.chatId);
                editMessageText.setMessageId(update.getCallbackQuery().getMessage().getMessageId());
                editMessageText.setReplyMarkup(mainMenu());
                super.execute(editMessageText);
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

        inlineKeyboardButton.setText("produkti");
        inlineKeyboardButton.setCallbackData("PRODUCTS");
        List<InlineKeyboardButton> row = new ArrayList<>();
        row.add(inlineKeyboardButton);

        InlineKeyboardButton inlineKeyboardButton1 = new InlineKeyboardButton();
        inlineKeyboardButton1.setText("Moya korzinka");
        inlineKeyboardButton1.setCallbackData("MY_BASKET");
        List<InlineKeyboardButton> row1 = new ArrayList<>();
        row1.add(inlineKeyboardButton1);

        InlineKeyboardButton inlineKeyboardButton2 = new InlineKeyboardButton();
        inlineKeyboardButton2.setText("Nastroyka");
        inlineKeyboardButton2.setCallbackData("SETTINGS");
        List<InlineKeyboardButton> row2 = new ArrayList<>();
        row2.add(inlineKeyboardButton2);

        list.add(row);
        list.add(row1);
        list.add(row2);

        return inlineKeyboardMarkup;
    }

    public InlineKeyboardMarkup langMenu() {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> list = new ArrayList<>();

        inlineKeyboardMarkup.setKeyboard(list);

        InlineKeyboardButton inlineKeyboardButton = new InlineKeyboardButton();

        inlineKeyboardButton.setText("UZBEK");
        inlineKeyboardButton.setCallbackData("UZBEK");
        List<InlineKeyboardButton> row = new ArrayList<>();
        row.add(inlineKeyboardButton);

        InlineKeyboardButton inlineKeyboardButton1 = new InlineKeyboardButton();
        inlineKeyboardButton1.setText("RUSSIAN");
        inlineKeyboardButton1.setCallbackData("RUSSIAN");
        List<InlineKeyboardButton> row1 = new ArrayList<>();
        row1.add(inlineKeyboardButton1);

        InlineKeyboardButton inlineKeyboardButton2 = new InlineKeyboardButton();
        inlineKeyboardButton2.setText("ENGLISH");
        inlineKeyboardButton2.setCallbackData("ENGLISH");
        List<InlineKeyboardButton> row2 = new ArrayList<>();
        row2.add(inlineKeyboardButton2);

        list.add(row);
        list.add(row1);
        list.add(row2);

        return inlineKeyboardMarkup;
    }

    public ReplyKeyboardMarkup genderMenu() {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        List<KeyboardRow> keyboardRows = new ArrayList<>();
        replyKeyboardMarkup.setKeyboard(keyboardRows);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setOneTimeKeyboard(true);
        replyKeyboardMarkup.setInputFieldPlaceholder("The gender is...");

        KeyboardRow keyboardRow = new KeyboardRow();
        keyboardRow.add("MALE");

        KeyboardRow keyboardRow1 = new KeyboardRow();
        keyboardRow1.add("FEMALE");

        KeyboardRow keyboardRow2 = new KeyboardRow();
        keyboardRow2.add("OTHERS");

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
