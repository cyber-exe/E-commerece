package bot;


import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import lombok.Data;
import lombok.SneakyThrows;
import model.Message;
import model.product.Category;
import model.user.Buyer;
import model.user.Gender;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageReplyMarkup;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardRemove;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import service.product_service.CategoryService;
import service.user_service.BuyerService;

import java.io.IOException;
import java.time.LocalDate;
import java.util.*;

public class TgBot extends TelegramLongPollingBot implements TelegramBotUtils {
    private String chatId;
    private String message;
    private String lang;
    private State state;
    private BuyerService buyerService;
    private UserService userService;
    private CategoryService categoryService;
    private Buyer buyer;
    private int currentPage;
    private int prevMenuId;
    private int messageId;

    Stack<ReplyKeyboard> menues = new Stack<>();
    Stack<String> headerOfMenu = new Stack<>();

    HashMap<String, ManageLang> manageLangList = new HashMap<String, ManageLang>();

    {
        buyerService = new BuyerService();
        userService = new UserService();
        categoryService = new CategoryService();
        this.state = State.SELECT_LANG;
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

    public TgBot() {
        super();
    }

    @SneakyThrows
    @Override
    public void onUpdateReceived(Update update) {
        if(update.hasMessage()) {
            this.chatId = update.getMessage().getChatId().toString();
            int msgId = update.getMessage().getMessageId();

            buyer = userService.getUserByChatId(update.getMessage().getChatId());
            buyer.setCurrentPage(1);
            buyerService.edit(buyer);

            if(buyer == null)
                buyer = buyerService.add(new Buyer(update.getMessage().getChatId(), update.getMessage().getFrom().getFirstName()));

            String text = update.getMessage().getText();
            delete(msgId);

            if(text.equals("/start")) {
                this.message = "Assalomu alaykum. Tilni kiriting!\nHello, select language!\nПривет, выберите язык!";
                this.send(langMenu(), this.message);
            } else if(text.equals("SELECT_LANG")) {
                this.state = State.SELECT_LANG;

                buyer.setState(State.SELECT_LANG);
                buyer.setCurrentPage(1);
                buyerService.edit(buyer);
                if(buyer.getMassageId() == 0)
                    send(langMenu(), manageLangList.getOrDefault(buyer.getLan(), new ContentEng()).selected_lang);
                else
                    edit(msgId, langMenu(), manageLangList.getOrDefault(buyer.getLan(), new ContentEng()).selected_lang);

            } else if(text.equals("SETTINGS")) {
                this.state = State.SETTINGS;

                buyer.setState(State.SETTINGS);
                buyerService.edit(buyer);

                send(settingsMenu(), "SETTINGS");
            } else if(text.equals("\uD83D\uDD19 PREV")) {
                if(buyer.getState() == State.SETTINGS) {
                    this.state = State.MAIN_MENU;

                    buyer.setState(State.MAIN_MENU);
                    buyerService.edit(buyer);

                    send(mainMenu(), manageLangList.getOrDefault(buyer.getLan(), new ContentEng()).main_header);
                }
            } else if(text.equals("PRODUCTS")) {
                if(buyer.getState() == State.MAIN_MENU) {
                    this.state = State.CATEGORY_LIST;
                    buyer.setMassageId(msgId);
                    buyer.setState(State.CATEGORY_LIST);
                    buyerService.edit(buyer);

                    send(categoryMenu(), manageLangList.getOrDefault(buyer.getLan(), new ContentEng()).main_header);
                }
            } else if(buyer.getState() == State.ENTER_CATEGORY_NAME) {
                Category category = new Category();
                category.setName(text);
                try {
                    categoryService.add(category);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                edit(buyer.getMassageId(), categoryMenu(), "Category menu");
            }
        } else if(update.hasCallbackQuery()) {
            this.chatId = update.getCallbackQuery().getMessage().getChatId().toString();
            String data = update.getCallbackQuery().getData();
            int msgId = update.getCallbackQuery().getMessage().getMessageId();
            String callbackQueryId = update.getCallbackQuery().getId();

            if(data.equals("UZBEK") || data.equals("RUSSIAN") || data.equals("ENGLISH")) {
                buyer.setLan(data);
                buyer.setState(State.MAIN_MENU);
                buyer.setMassageId(0);
                buyerService.edit(buyer);

                popup(callbackQueryId, manageLangList.getOrDefault(buyer.getLan(), new ContentEng()).selected_lang, true);

                delete(msgId);

               // send(manageLangList.getOrDefault(data, new ContentEng()).selected_lang);

                this.state = State.MAIN_MENU;

                send(mainMenu(), manageLangList.getOrDefault(buyer.getLan(), new ContentEng()).main_header);
            }
//            else if(data.equals("SELECT_LANG")) {
//                this.state = State.SELECT_LANG;
//
//                buyer.setState(State.SELECT_LANG);
//                buyerService.edit(buyer);
//                edit(msgId, langMenu(), manageLangList.getOrDefault(buyer.getLan(), new ContentEng()).selected_lang);
//            }
            else if(data.equals("ENTER_CATEGORY_NAME")) {
                this.state = State.ENTER_CATEGORY_NAME;
                buyer.setState(State.ENTER_CATEGORY_NAME);
                buyer.setMassageId(msgId);
                buyerService.edit(buyer);

                send("Enter category name: ");
            } else if(data.equals("DELETE_CATEGORY")) {
                this.state = State.DELETE_CATEGORY;
                buyer.setState(State.DELETE_CATEGORY);
                buyer.setMassageId(msgId);
                buyerService.edit(buyer);
            } else if(buyer.getState().equals(State.DELETE_CATEGORY)) {
                if(deleteCategory(data))
                    popup(callbackQueryId, "The category is deleted!", false);
                else
                    popup(callbackQueryId, "The category is not deleted!", false);

                delete(buyer.getMassageId());
                send(categoryMenu(), manageLangList.getOrDefault(buyer.getLan(), new ContentEng()).main_header);

                this.state = State.CATEGORY_LIST;
                buyer.setState(State.CATEGORY_LIST);
                buyer.setMassageId(msgId);
                buyerService.edit(buyer);

            } else if(data.equals("BACK")) {
                if(buyer.getState() == State.CATEGORY_LIST) {
                    this.state = State.MAIN_MENU;
                    buyer.setMassageId(0);
                    buyer.setState(State.MAIN_MENU);
                    buyerService.edit(buyer);

                    delete(update.getCallbackQuery().getMessage().getMessageId());
                    send(mainMenu(), "Main menu");
                }
            } else if(data.equals("PREV")) {
                if(buyer.getCurrentPage() != 1) {
                    buyer.setCurrentPage(buyer.getCurrentPage() - 1);
                    this.messageId = msgId;
                    buyer.setMassageId(msgId);
                    buyerService.edit(buyer);
                    edit(msgId, categoryMenu(), "dasdas");
                }
            } else if(data.equals("NEXT")) {
                buyer.setCurrentPage(buyer.getCurrentPage() + 1);
                this.messageId = msgId;
                buyer.setMassageId(msgId);
                buyerService.edit(buyer);
                edit(msgId, categoryMenu(), "dasdas");
            }
        }
    }
// TODO universal qilish kerak shu methodni
    public List<List<InlineKeyboardButton>> getItemList() {
        int i = 0;
        boolean getItem = false;

        List<List<InlineKeyboardButton>> list = new ArrayList<>();

        for (Category category : categoryService.getActives()) {
            if(this.buyer.getCurrentPage() * 10 - 10 == i)
                getItem = true;

            if(this.buyer.getCurrentPage() * 10 == i)
                break;

            if(getItem) {
                InlineKeyboardButton inlineKeyboardButton = new InlineKeyboardButton();
                inlineKeyboardButton.setText(category.getName());
                inlineKeyboardButton.setCallbackData(category.getId().toString());
                List<InlineKeyboardButton> row = new ArrayList<>();
                row.add(inlineKeyboardButton);
                list.add(row);
            }

            i++;
        }

        return list;
    }

    public InlineKeyboardMarkup categoryMenu() {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> list = getItemList();

        list.add(pageRow());
        list.add(addCategory());
        list.add(deleteCategoryBtn());
        list.add(back());

        inlineKeyboardMarkup.setKeyboard(list);

        return inlineKeyboardMarkup;
    }

    public List<InlineKeyboardButton> pageRow() {
        List<InlineKeyboardButton> row = new ArrayList<>();
        row.add(prev());
        row.add(currentPage());
        row.add(next());

        return row;
    }



    public List<InlineKeyboardButton> back() {
        InlineKeyboardButton inlineKeyboardButton = new InlineKeyboardButton();
        inlineKeyboardButton.setText("⬅️");
        inlineKeyboardButton.setCallbackData("BACK");

        List<InlineKeyboardButton> row = new ArrayList<>();
        row.add(inlineKeyboardButton);

        return row;
    }

    public InlineKeyboardButton next() {
        InlineKeyboardButton inlineKeyboardButton = new InlineKeyboardButton();
        inlineKeyboardButton.setText("➡️");
        inlineKeyboardButton.setCallbackData("NEXT");

        return inlineKeyboardButton;
    }

    public InlineKeyboardButton prev() {
        InlineKeyboardButton inlineKeyboardButton = new InlineKeyboardButton();
        inlineKeyboardButton.setText("⬅️");
        inlineKeyboardButton.setCallbackData("PREV");

        return inlineKeyboardButton;
    }

    public InlineKeyboardButton currentPage() {
        InlineKeyboardButton inlineKeyboardButton = new InlineKeyboardButton();
        inlineKeyboardButton.setText(" " + buyer.getCurrentPage() + " ");
        inlineKeyboardButton.setCallbackData("PREV");

        return inlineKeyboardButton;
    }

    public List<InlineKeyboardButton> addCategory() {
        InlineKeyboardButton inlineKeyboardButton = new InlineKeyboardButton();
        inlineKeyboardButton.setText("Add new category");
        inlineKeyboardButton.setCallbackData("ENTER_CATEGORY_NAME");

        List<InlineKeyboardButton> row = new ArrayList<>();
        row.add(inlineKeyboardButton);

        return row;
    }

    public List<InlineKeyboardButton> deleteCategoryBtn() {
        InlineKeyboardButton inlineKeyboardButton = new InlineKeyboardButton();
        inlineKeyboardButton.setText("Delete category");
        inlineKeyboardButton.setCallbackData("DELETE_CATEGORY");

        List<InlineKeyboardButton> row = new ArrayList<>();
        row.add(inlineKeyboardButton);

        return row;
    }

    public boolean deleteCategory(String data) {

        for (Category category : categoryService.getList()) {
            if(data.equals(category.getId().toString())) {
                try {
                    this.categoryService.delete(category);
                    return true;
                } catch (IOException e) {
                    send("Category with this name is not defined!");
                    e.printStackTrace();
                }
                break;
            }
        }

        return false;
    }

    public void popup(String callbackQueryId, String data, Boolean isAlert) {
        AnswerCallbackQuery answerCallbackQuery = new AnswerCallbackQuery();
        answerCallbackQuery.setText(data);
        answerCallbackQuery.setShowAlert(isAlert);
        answerCallbackQuery.setCallbackQueryId(callbackQueryId);

        try {
            super.execute(answerCallbackQuery);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

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

    public ReplyKeyboardMarkup settingsMenu() {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        List<KeyboardRow> keyboardRows = new ArrayList<>();
        replyKeyboardMarkup.setKeyboard(keyboardRows);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setOneTimeKeyboard(true);

        KeyboardRow keyboardRow = new KeyboardRow();
        keyboardRow.add("MY_PROFILE");
        keyboardRow.add("SELECT_LANG");

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

    private void send(ReplyKeyboardMarkup menu, String text) {
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

    private void send(InlineKeyboardMarkup menu, String text) {
        SendMessage sendMessage = new SendMessage();
        Integer replyToMessageId = sendMessage.getReplyToMessageId();
        sendMessage.setText(text);
        sendMessage.setChatId(this.chatId);
        sendMessage.setReplyMarkup(menu);

        try {
            super.execute(sendMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private void send(String text) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(this.chatId);
        sendMessage.setText(text);

        try {
            super.execute(sendMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private void edit(int editedId, InlineKeyboardMarkup menu, String text) {
        EditMessageText editMessageText = new EditMessageText();
        editMessageText.setChatId(this.chatId);
        editMessageText.setText(text);
        editMessageText.setMessageId(editedId);
        editMessageText.setReplyMarkup(menu);

        try {
            super.execute(editMessageText);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private void edit(int editedId, String text) {
        EditMessageText editMessageText = new EditMessageText();
        editMessageText.setChatId(this.chatId);
        editMessageText.setText(text);
        editMessageText.setMessageId(editedId);


        try {
            super.execute(editMessageText);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private void delete(int deletedId) {
        DeleteMessage deleteMessage = new DeleteMessage();
        deleteMessage.setChatId(this.chatId);
        deleteMessage.setMessageId(deletedId);
        ReplyKeyboardRemove replyKeyboardRemove = new ReplyKeyboardRemove(true);
        try {
            super.execute(deleteMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

//    private void deleteReplyMenu(int deletedId) {
//        ReplyKeyboardRemove replyKeyboardRemove = new ReplyKeyboardRemove();
//
//    }
}
