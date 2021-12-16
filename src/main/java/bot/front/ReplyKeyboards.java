package bot.front;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.List;

public class ReplyKeyboards {
    public ReplyKeyboardMarkup mainMenu() {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        List<KeyboardRow> keyboardRows = new ArrayList<>();
        replyKeyboardMarkup.setKeyboard(keyboardRows);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setOneTimeKeyboard(true);

        KeyboardRow keyboardRow = new KeyboardRow();
        keyboardRow.add("PRODUCTS");

        KeyboardRow keyboardRow1 = new KeyboardRow();
        keyboardRow1.add("MOYA KORZINKA");
        keyboardRow1.add("MOYI ZAKAZI");

        KeyboardRow keyboardRow2 = new KeyboardRow();
        keyboardRow2.add("SETTINGS");

        keyboardRows.add(keyboardRow);
        keyboardRows.add(keyboardRow1);
        keyboardRows.add(keyboardRow2);

        return replyKeyboardMarkup;
//        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
//        List<List<InlineKeyboardButton>> list = new ArrayList<>();
//
//        inlineKeyboardMarkup.setKeyboard(list);
//
//        InlineKeyboardButton inlineKeyboardButton = new InlineKeyboardButton();
//
//        inlineKeyboardButton.setText("produkti");
//        inlineKeyboardButton.setCallbackData("PRODUCTS");
//        List<InlineKeyboardButton> row = new ArrayList<>();
//        row.add(inlineKeyboardButton);
//
//        InlineKeyboardButton inlineKeyboardButton1 = new InlineKeyboardButton();
//        inlineKeyboardButton1.setText("Moya korzinka");
//        inlineKeyboardButton1.setCallbackData("MY_BASKET");
//        List<InlineKeyboardButton> row1 = new ArrayList<>();
//        row1.add(inlineKeyboardButton1);
//
//        InlineKeyboardButton inlineKeyboardButton2 = new InlineKeyboardButton();
//        inlineKeyboardButton2.setText("Nastroyka");
//        inlineKeyboardButton2.setCallbackData("SETTINGS");
//        List<InlineKeyboardButton> row2 = new ArrayList<>();
//        row2.add(inlineKeyboardButton2);
//
//        list.add(row);
//        list.add(row1);
//        list.add(row2);
//
//        return inlineKeyboardMarkup;
    }


    public ReplyKeyboardMarkup settingsMenu() {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        List<KeyboardRow> keyboardRows = new ArrayList<>();
        replyKeyboardMarkup.setKeyboard(keyboardRows);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setOneTimeKeyboard(true);

        KeyboardRow keyboardRow = new KeyboardRow();
        keyboardRow.add("\uD83D\uDC64 MY_PROFILE \uD83D\uDC64");
        keyboardRow.add("✅ SELECT_LANG ✅");

        KeyboardRow keyboardRow1 = new KeyboardRow();
        keyboardRow1.add("\uD83D\uDD19 PREV");

        keyboardRows.add(keyboardRow);
        keyboardRows.add(keyboardRow1);

        return replyKeyboardMarkup;
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
        keyboardRow.add("\uD83D\uDD7A MALE \uD83D\uDD7A");

        KeyboardRow keyboardRow1 = new KeyboardRow();
        keyboardRow1.add("\uD83D\uDC83 FEMALE \uD83D\uDC83");

        KeyboardRow keyboardRow2 = new KeyboardRow();
        keyboardRow2.add("\uD83E\uDD37\u200D♂️ OTHERS \uD83E\uDD37\u200D♂️");

        keyboardRows.add(keyboardRow);
        keyboardRows.add(keyboardRow1);
        keyboardRows.add(keyboardRow2);

        return replyKeyboardMarkup;
    }
}
