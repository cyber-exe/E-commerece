package bot;


import lombok.SneakyThrows;
import model.product.Category;
import model.product.SubCategory;
import model.user.Buyer;
import model.user.Gender;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardRemove;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import service.product_service.CategoryService;
import service.product_service.SubCategoryService;
import service.user_service.BuyerService;

import java.io.IOException;
import java.util.*;

public class TgBot extends TelegramLongPollingBot implements TelegramBotUtils {
    private String chatId;
    private String message;
    private String lang;
    private State state;
    private BuyerService buyerService;
    private UserService userService;
    private CategoryService categoryService;
    private SubCategoryService subCategoryService;
    private Buyer buyer;
    private String categoryId;
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
        subCategoryService = new SubCategoryService();
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

            buyer = buyerService.getUserByChatId(update.getMessage().getChatId());

            if(buyer == null)
                buyer = buyerService.add(new Buyer(update.getMessage().getChatId(), update.getMessage().getFrom().getFirstName()));

            String text = update.getMessage().getText();
            delete(msgId);

            if(text.equals("/start")) {
                this.message = "Assalomu alaykum. Tilni kiriting!\nHello, select language!\nПривет, выберите язык!";
                this.send(langMenu(), this.message);

            } else if(text.equals("✅ SELECT_LANG ✅")) {
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

                    send(categoryMenu(), "Categories");
                }
            } else if(buyer.getState() == State.ENTER_CATEGORY_NAME) {
                Category category = new Category();
                category.setName(text);
                try {
                    categoryService.add(category);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                delete(buyer.getMassageId());
//                edit(buyer.getMassageId(), categoryMenu(), "Categories");

                this.state = State.CATEGORY_LIST;
                buyer.setMassageId(msgId);
                buyer.setState(State.CATEGORY_LIST);
                buyerService.edit(buyer);

                send(categoryMenu(), "Categories");
            } else if(buyer.getState() == State.ENTER_SUB_CATEGORY_NAME) {
                SubCategory subCategory = new SubCategory();
                subCategory.setName(text);
                UUID ctgId = UUID.fromString(this.categoryId);
                subCategory.setCategoryId(ctgId);
                try {
                    subCategoryService.add(subCategory);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                delete(buyer.getMassageId());
//                edit(buyer.getMassageId(), subCategoryMenu(categoryId), "Sub   ategories");

                this.state = State.SUB_CATEGORY_LIST;
                buyer.setMassageId(msgId);
                buyer.setState(State.SUB_CATEGORY_LIST);
                buyerService.edit(buyer);

                send(subCategoryMenu(this.categoryId), "subCategories");
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
                send("Select category");
                this.state = State.DELETE_CATEGORY;
                buyer.setState(State.DELETE_CATEGORY);
                buyer.setMassageId(msgId);
                buyerService.edit(buyer);
            }else if(data.equals("ENTER_SUB_CATEGORY_NAME")) {
                this.state = State.ENTER_SUB_CATEGORY_NAME;
                buyer.setState(State.ENTER_SUB_CATEGORY_NAME);
                buyer.setMassageId(msgId);
                buyerService.edit(buyer);

                send("Enter subcategory name: ");
            } else if(data.equals("DELETE_SUB_CATEGORY")) {
                send("Select category");
                this.state = State.DELETE_SUB_CATEGORY;
                buyer.setState(State.DELETE_SUB_CATEGORY);
                buyer.setMassageId(msgId);
                buyerService.edit(buyer);
            } else if(buyer.getState().equals(State.DELETE_CATEGORY)) {
                if(deleteCategory(data))
                    popup(callbackQueryId, "The category is deleted!", false);
                else
                    popup(callbackQueryId, "The category is not deleted!", false);

                delete(buyer.getMassageId());
                send(categoryMenu(), "Categories");

                this.state = State.CATEGORY_LIST;
                buyer.setState(State.CATEGORY_LIST);
                buyer.setMassageId(msgId);
                buyerService.edit(buyer);

            } else if(buyer.getState().equals(State.DELETE_SUB_CATEGORY)) {
                if(deleteSubCategory(data))
                    popup(callbackQueryId, "The subcategory is deleted!", false);
                else
                    popup(callbackQueryId, "The subcategory is not deleted!", false);

                delete(buyer.getMassageId());
                send(subCategoryMenu(this.categoryId), "subbCategories");

                this.state = State.SUB_CATEGORY_LIST;
                buyer.setState(State.SUB_CATEGORY_LIST);
                buyer.setMassageId(msgId);
                buyerService.edit(buyer);

            } else if(buyer.getState().equals(State.CATEGORY_LIST)) {
                this.state = State.SUB_CATEGORY_LIST;
                buyer.setMassageId(msgId);
                buyer.setState(State.SUB_CATEGORY_LIST);
                buyerService.edit(buyer);
                this.categoryId = data;
                edit(buyer.getMassageId(), subCategoryMenu(data), "SubCategories");

            } else if(data.equals("BACK")) {
                if(buyer.getState() == State.CATEGORY_LIST) {
                    this.state = State.MAIN_MENU;
                    buyer.setMassageId(0);
                    buyer.setState(State.MAIN_MENU);
                    buyerService.edit(buyer);

                    delete(update.getCallbackQuery().getMessage().getMessageId());
                    send(mainMenu(), "Main menu");
                } else if(buyer.getState() == State.SUB_CATEGORY_LIST) {
                    edit(buyer.getMassageId(), subCategoryMenu(this.categoryId), "Sub Categories");

                    this.state = State.CATEGORY_LIST;
                    buyer.setMassageId(msgId);
                    buyer.setState(State.CATEGORY_LIST);
                    buyerService.edit(buyer);

//                    delete(msgId);

                }
            } else if(data.equals("PREV")) {
                if(buyer.getCurrentPage() != 1) {
                    buyer.setCurrentPage(buyer.getCurrentPage() - 1);
                    this.messageId = msgId;
                    buyer.setMassageId(msgId);
                    buyerService.edit(buyer);

                    if(buyer.getState().equals(State.CATEGORY_LIST))
                        edit(msgId, categoryMenu(), "Categories");
                    else if(buyer.getState().equals(State.SUB_CATEGORY_LIST))
                        edit(msgId, subCategoryMenu(this.categoryId), "Sub-categories");
                }
            } else if(data.equals("NEXT")) {
                buyer.setCurrentPage(buyer.getCurrentPage() + 1);
                this.messageId = msgId;
                buyer.setMassageId(msgId);
                buyerService.edit(buyer);

                if(buyer.getState().equals(State.CATEGORY_LIST))
                    edit(msgId, categoryMenu(), "Categories");
                else if(buyer.getState().equals(State.SUB_CATEGORY_LIST))
                    edit(msgId, subCategoryMenu(categoryId), "Sub-categories");

            }
        }
    }


// TODO universal qilish kerak shu methodni
    public List<List<InlineKeyboardButton>> getItemList() {
        int i = 0;
        boolean getItem = false;

        List<List<InlineKeyboardButton>> list = new ArrayList<>();

        for (Category category : categoryService.getActives("")) {
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

    public List<List<InlineKeyboardButton>> getItemList(String data) {
        int i = 0;
        boolean getItem = false;

        List<List<InlineKeyboardButton>> list = new ArrayList<>();

        for (SubCategory sub : subCategoryService.getActives(data)) {
            if(this.buyer.getCurrentPage() * 10 - 10 == i)
                getItem = true;

            if(this.buyer.getCurrentPage() * 10 == i)
                break;

            if(getItem) {
                InlineKeyboardButton inlineKeyboardButton = new InlineKeyboardButton();
                inlineKeyboardButton.setText(sub.getName());
                inlineKeyboardButton.setCallbackData(sub.getId().toString());
                List<InlineKeyboardButton> row = new ArrayList<>();
                row.add(inlineKeyboardButton);
                list.add(row);
            }

            i++;
        }

        return list;
    }

    public InlineKeyboardMarkup subCategoryMenu(String categoryId) {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> list = getItemList(categoryId);

        list.add(pageRow());
        list.add(addBtn("Add new subcategory", "ENTER_SUB_CATEGORY_NAME"));
        list.add(deleteBtn("Delete subcategory","DELETE_SUB_CATEGORY"));
        list.add(back());

        inlineKeyboardMarkup.setKeyboard(list);

        return inlineKeyboardMarkup;
    }

    public InlineKeyboardMarkup categoryMenu() {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> list = getItemList();

        list.add(pageRow());
        list.add(addBtn("Add new category", "ENTER_CATEGORY_NAME"));
        list.add(deleteBtn("Delete category","DELETE_CATEGORY"));
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

    public List<InlineKeyboardButton> addBtn(String text, String callbackData) {
        InlineKeyboardButton inlineKeyboardButton = new InlineKeyboardButton();
        inlineKeyboardButton.setText(text);
        inlineKeyboardButton.setCallbackData(callbackData);

        List<InlineKeyboardButton> row = new ArrayList<>();
        row.add(inlineKeyboardButton);

        return row;
    }

    public List<InlineKeyboardButton> deleteBtn(String text, String callbackData) {
        InlineKeyboardButton inlineKeyboardButton = new InlineKeyboardButton();
        inlineKeyboardButton.setText(text);
        inlineKeyboardButton.setCallbackData(callbackData);

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

    public boolean deleteSubCategory(String data) {

        for (SubCategory sub : subCategoryService.getList()) {
            if(data.equals(sub.getId().toString())) {
                try {
                    this.subCategoryService.delete(sub);
                    return true;
                } catch (IOException e) {
                    send("Sub category with this name is not defined!");
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

        inlineKeyboardButton.setText("\uD83C\uDDFA\uD83C\uDDFF UZBEK \uD83C\uDDFA\uD83C\uDDFF");
        inlineKeyboardButton.setCallbackData("UZBEK");
        List<InlineKeyboardButton> row = new ArrayList<>();
        row.add(inlineKeyboardButton);

        InlineKeyboardButton inlineKeyboardButton1 = new InlineKeyboardButton();
        inlineKeyboardButton1.setText("\uD83C\uDDF7\uD83C\uDDFA RUSSIAN \uD83C\uDDF7\uD83C\uDDFA");
        inlineKeyboardButton1.setCallbackData("RUSSIAN");
        List<InlineKeyboardButton> row1 = new ArrayList<>();
        row1.add(inlineKeyboardButton1);

        InlineKeyboardButton inlineKeyboardButton2 = new InlineKeyboardButton();
        inlineKeyboardButton2.setText("\uD83C\uDDFA\uD83C\uDDF8 ENGLISH \uD83C\uDDFA\uD83C\uDDF8");
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
