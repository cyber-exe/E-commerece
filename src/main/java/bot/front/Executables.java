package bot.front;

import bot.TgBot;
import bot.language_service.ContentEng;
import model.product.Category;
import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardRemove;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.IOException;

public class Executables extends TgBot {

    public boolean deleteCategory(String data) {
        for (Category category : getCategoryService().getList()) {
            if(data.equals(category.getId().toString())) {
                try {
                    getCategoryService().delete(category);
                } catch (IOException e) {
                    send("Category with this name is not defined!");
                    e.printStackTrace();
                }
                break;
            }
        }

        return false;
    }

    public void popup(String callbackQueryId, String data) {
        AnswerCallbackQuery answerCallbackQuery = new AnswerCallbackQuery();
        answerCallbackQuery.setText(getManageLangList().getOrDefault(data, new ContentEng()).selected_lang);
        answerCallbackQuery.setShowAlert(true);
        answerCallbackQuery.setCallbackQueryId(callbackQueryId);

        try {
            super.execute(answerCallbackQuery);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public void send(ReplyKeyboardMarkup menu, String text) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setText(text);
        sendMessage.setChatId(this.getChatId());
        sendMessage.setReplyMarkup(menu);

        try {
            super.execute(sendMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public void send(InlineKeyboardMarkup menu, String text) {
        SendMessage sendMessage = new SendMessage();
        Integer replyToMessageId = sendMessage.getReplyToMessageId();
        sendMessage.setText(text);
        sendMessage.setChatId(this.getChatId());
        sendMessage.setReplyMarkup(menu);

        try {
            super.execute(sendMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public void send(String text) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(this.getChatId());
        sendMessage.setText(text);

        try {
            super.execute(sendMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public void edit(int editedId, InlineKeyboardMarkup menu, String text) {
        EditMessageText editMessageText = new EditMessageText();
        editMessageText.setChatId(getChatId());
        editMessageText.setText(text);
        editMessageText.setMessageId(editedId);
        editMessageText.setReplyMarkup(menu);

        try {
            super.execute(editMessageText);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public void edit(int editedId, String text) {
        EditMessageText editMessageText = new EditMessageText();
        editMessageText.setChatId(this.getChatId());
        editMessageText.setText(text);
        editMessageText.setMessageId(editedId);


        try {
            super.execute(editMessageText);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public void delete(int deletedId) {
        DeleteMessage deleteMessage = new DeleteMessage();
        deleteMessage.setChatId(this.getChatId());
        deleteMessage.setMessageId(deletedId);
        ReplyKeyboardRemove replyKeyboardRemove = new ReplyKeyboardRemove(true);
        try {
            super.execute(deleteMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}
