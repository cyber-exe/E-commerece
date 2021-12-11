package bot;

import lombok.SneakyThrows;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.inlinequery.result.InlineQueryResult;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.List;

public class TgBot extends TelegramLongPollingBot implements TelegramBotUtils {
    private String chatId;
    private String message;
    private String lang;

    @Override
    public String getBotUsername() {
        return BOT_USERNAME;
    }

    @Override
    public String getBotToken() {
        return BOT_TOKEN;
    }

    @Override
    public void onUpdateReceived(Update update) {

        if(update.hasMessage()) {
            this.chatId = update.getMessage().getChatId().toString();

            String text = update.getMessage().getText();

            if(text.equals("/start")) {
                this.message = "Assalomu alaykum. Tilni kiriting!\nHello, select language!\nПривет, выберите язык!";

                this.execute(langMenu(), this.message);
            } else if(text.equals("Uzbek")) {
                this.execute(mainMenu(), "Tizimga kirish");
            } else if(text.equals("English")) {

            }
        } else if(update.hasCallbackQuery()) {
            this.chatId = update.getCallbackQuery().getMessage().getChatId().toString();
            String data = update.getCallbackQuery().getData();

            this.execute(data);
        }
    }

    public InlineKeyboardMarkup mainMenu() {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> list = new ArrayList<>();

        inlineKeyboardMarkup.setKeyboard(list);

        InlineKeyboardButton inlineKeyboardButton = new InlineKeyboardButton();
        inlineKeyboardButton.setText("Kirish");
        inlineKeyboardButton.setCallbackData("SINGIN");
        inlineKeyboardButton.setCallbackData(LangUzb.KIRISH.toString());
        List<InlineKeyboardButton> row = new ArrayList<>();
        row.add(inlineKeyboardButton);

        InlineKeyboardButton inlineKeyboardButton1 = new InlineKeyboardButton();
        inlineKeyboardButton1.setText("Registratsiya");
        inlineKeyboardButton1.setCallbackData("SIGNUP");
        inlineKeyboardButton1.setCallbackData("Sign Up");
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

        KeyboardRow keyboardRow = new KeyboardRow();
        keyboardRow.add("Uzbek");

        KeyboardRow keyboardRow1 = new KeyboardRow();
        keyboardRow1.add("Русский");

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

//    private void execute(ReplyKeyboardMarkup menu) {
//        SendMessage sendMessage = new SendMessage();
//        sendMessage.setChatId(this.chatId);
//        sendMessage.setReplyMarkup(menu);
//
//        try {
//            super.execute(sendMessage);
//        } catch (TelegramApiException e) {
//            e.printStackTrace();
//        }
//    }

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

//    private void execute(InlineKeyboardMarkup menu) {
//        SendMessage sendMessage = new SendMessage();
//        sendMessage.setChatId(this.chatId);
//        sendMessage.setReplyMarkup(menu);
//
//        try {
//            super.execute(sendMessage);
//        } catch (TelegramApiException e) {
//            e.printStackTrace();
//        }
//    }

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
