package bot;


import bot.front.Executables;
import bot.front.InlineKeyboards;
import bot.front.ReplyKeyboards;
import bot.language_service.ContentEng;
import bot.language_service.ContentRu;
import bot.language_service.ContentUz;
import bot.language_service.ManageLang;
import bot.user_service.UserService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.SneakyThrows;
import model.product.Category;
import model.user.Buyer;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardRemove;

import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import service.product_service.CategoryService;
import service.user_service.BuyerService;

import java.io.IOException;

import java.util.HashMap;

@Data
@AllArgsConstructor

public class TgBot extends TelegramLongPollingBot implements TelegramBotUtils {
    private String chatId;
    private String message;
    private String lang;
    private State state;
    private BuyerService buyerService;
    private UserService userService;
    private CategoryService categoryService;
//    private Executables executables;
    private InlineKeyboards inlineKeyboards;
//    private ReplyKeyboards replyKeyboards;
    private Buyer buyer;
    private int currentPage;
    private int prevMenuId;
    private int messageId;


    HashMap<String, ManageLang> manageLangList = new HashMap<String, ManageLang>();

    {
        buyerService = new BuyerService();
        userService = new UserService();
        categoryService = new CategoryService();
//        executables = new Executables();
//        inlineKeyboards = new InlineKeyboards();
//        replyKeyboards = new ReplyKeyboards();

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
            inlineKeyboards = new InlineKeyboards();


            buyer = userService.getUserByChatId(update.getMessage().getChatId());
            buyer.setCurrentPage(1);
            buyerService.edit(buyer);

            if(buyer == null)
                buyer = buyerService.add(new Buyer(update.getMessage().getChatId(), update.getMessage().getFrom().getFirstName()));

            String text = update.getMessage().getText();
            delete(msgId);
            if(text.equals("/start")) {
                this.message = "Assalomu alaykum. Tilni kiriting!\nHello, select language!\nПривет, выберите язык!";
                send(new InlineKeyboards().langMenu(), this.message);

            } else if(text.equals("SELECT_LANG")) {
                this.state = State.SELECT_LANG;

                buyer.setState(State.SELECT_LANG);
                buyer.setCurrentPage(1);
                buyerService.edit(buyer);
                if(buyer.getMassageId() == 0)
                    send(new InlineKeyboards().langMenu(), manageLangList.getOrDefault(buyer.getLan(), new ContentEng()).selected_lang);
                else
                    edit(msgId, new InlineKeyboards().langMenu(), manageLangList.getOrDefault(buyer.getLan(), new ContentEng()).selected_lang);

            } else if(text.equals("SETTINGS")) {
                this.state = State.SETTINGS;

                buyer.setState(State.SETTINGS);
                buyerService.edit(buyer);

                send(new ReplyKeyboards().settingsMenu(), "SETTINGS");
            } else if(text.equals("\uD83D\uDD19 PREV")) {
                if(buyer.getState() == State.SETTINGS) {
                    this.state = State.MAIN_MENU;

                    buyer.setState(State.MAIN_MENU);
                    buyerService.edit(buyer);

                    send(new ReplyKeyboards().mainMenu(), manageLangList.getOrDefault(buyer.getLan(), new ContentEng()).main_header);
                }
            } else if(text.equals("PRODUCTS")) {
                if(buyer.getState() == State.MAIN_MENU) {
                    this.state = State.CATEGORY_LIST;
                    buyer.setMassageId(msgId);
                    buyer.setState(State.CATEGORY_LIST);
                    buyerService.edit(buyer);


                    send(inlineKeyboards.categoryMenu(buyer, categoryService),manageLangList.getOrDefault(buyer.getLan(), new ContentEng()).main_header);
                }
            } else if(buyer.getState() == State.ENTER_CATEGORY_NAME) {
                Category category = new Category();
                category.setName(text);
                try {
                    categoryService.add(category);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                edit(buyer.getMassageId(), new InlineKeyboards().categoryMenu(buyer, categoryService), "Category menu");
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
                popup(callbackQueryId, data);

                delete(msgId);

               // send(manageLangList.getOrDefault(data, new ContentEng()).selected_lang);

                this.state = State.MAIN_MENU;

                send(new ReplyKeyboards().mainMenu(), manageLangList.getOrDefault(buyer.getLan(), new ContentEng()).main_header);
            }

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
                this.state = State.CATEGORY_MENU;
                buyer.setState(State.CATEGORY_MENU);
                buyer.setMassageId(msgId);
                buyerService.edit(buyer);

                if(new Executables().deleteCategory(data))
                    popup(callbackQueryId, "The category is deleted!");
                else
                    popup(callbackQueryId, "The category is not deleted!");
                edit(msgId, manageLangList.getOrDefault(buyer.getLan(), new ContentEng()).main_header);

            } else if(data.equals("BACK")) {
                if(buyer.getState() == State.CATEGORY_LIST) {
                    this.state = State.MAIN_MENU;
                    buyer.setMassageId(0);
                    buyer.setState(State.MAIN_MENU);
                    buyerService.edit(buyer);

                    delete(update.getCallbackQuery().getMessage().getMessageId());
                    send(new ReplyKeyboards().mainMenu(), "Main menu");
                }
            } else if(data.equals("PREV")) {
                if(buyer.getCurrentPage() != 1) {
                    buyer.setCurrentPage(buyer.getCurrentPage() - 1);
                    this.messageId = msgId;
                    buyer.setMassageId(msgId);
                    buyerService.edit(buyer);
                    edit(msgId, new InlineKeyboards().categoryMenu(buyer, categoryService), "dasdas");
                }
            } else if(data.equals("NEXT")) {
                buyer.setCurrentPage(buyer.getCurrentPage() + 1);
                this.messageId = msgId;
                buyer.setMassageId(msgId);
                buyerService.edit(buyer);
                edit(msgId, new InlineKeyboards().categoryMenu(buyer, categoryService), "dasdas");
            }
        }
    }


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
