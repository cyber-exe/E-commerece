package bot;

import lombok.SneakyThrows;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.inlinequery.result.InlineQueryResult;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.List;

public class TgBot extends TelegramLongPollingBot implements TelegramBotUtils {
    private String chatId;
    private String message;

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
                this.message = "Assalomu alaykum. Siz Savdozon telegram botiga kirdingiz!";

                this.execute();
            }
        } else if(update.hasCallbackQuery()) {
            this.chatId = update.getCallbackQuery().getMessage().getChatId().toString();
//            this.message = update.getCallbackQuery().getData();
//            this.execute();
            String data = update.getCallbackQuery().getData();
            System.out.println(data);

//            try {
//                SendMessage send = new SendMessage();
//                send.setText(data);
//                send.setChatId(update.getCallbackQuery().);
//                super.execute(send);
//            } catch (TelegramApiException e) {
//                e.printStackTrace();
//            }
        }
    }

    public void execute() {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setText(this.message);
        sendMessage.setChatId(this.chatId);
        sendMessage.setReplyMarkup(mainMenu());

        try {
            super.execute(sendMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public InlineKeyboardMarkup mainMenu() {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> list = new ArrayList<>();

        inlineKeyboardMarkup.setKeyboard(list);

        InlineKeyboardButton inlineKeyboardButton = new InlineKeyboardButton();
        inlineKeyboardButton.setText("Kirish");
        inlineKeyboardButton.setCallbackData("kirish");
        List<InlineKeyboardButton> row = new ArrayList<>();
        row.add(inlineKeyboardButton);

        InlineKeyboardButton inlineKeyboardButton1 = new InlineKeyboardButton();
        inlineKeyboardButton1.setText("Registratsiya");
        inlineKeyboardButton1.setCallbackData("register");
        List<InlineKeyboardButton> row1 = new ArrayList<>();
        row1.add(inlineKeyboardButton1);

        list.add(row);
        list.add(row1);

        return inlineKeyboardMarkup;
    }
}
