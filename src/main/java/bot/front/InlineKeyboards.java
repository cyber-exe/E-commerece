package bot.front;

import bot.TgBot;
import bot.user_service.UserService;
import model.product.Category;
import model.user.Buyer;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import service.product_service.CategoryService;
import service.user_service.BuyerService;

import java.util.ArrayList;
import java.util.List;


public class InlineKeyboards {

    public static InlineKeyboardMarkup langMenu() {


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

    public  InlineKeyboardMarkup categoryMenu(Buyer buyer, CategoryService categoryService) {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> list = getItemList(categoryService, buyer);

        list.add(pageRow(buyer));
        list.add(addCategory());
        list.add(deleteCategoryBtn());
        list.add(back());

        inlineKeyboardMarkup.setKeyboard(list);

        return inlineKeyboardMarkup;
    }

    //! inline paginationMenu
    public List<InlineKeyboardButton> pageRow(Buyer buyer) {
        List<InlineKeyboardButton> row = new ArrayList<>();
        row.add(prev());
        row.add(currentPage(buyer));
        row.add(next());

        return row;
    }

    //! back inline button
    public List<InlineKeyboardButton> back() {
        InlineKeyboardButton inlineKeyboardButton = new InlineKeyboardButton();
        inlineKeyboardButton.setText("⬅️");
        inlineKeyboardButton.setCallbackData("BACK");

        List<InlineKeyboardButton> row = new ArrayList<>();
        row.add(inlineKeyboardButton);

        return row;
    }
    //! next inline button
    public InlineKeyboardButton next() {
        InlineKeyboardButton inlineKeyboardButton = new InlineKeyboardButton();
        inlineKeyboardButton.setText("➡️");
        inlineKeyboardButton.setCallbackData("NEXT");

        return inlineKeyboardButton;
    }

    //! previous inline button
    public  InlineKeyboardButton prev() {
        InlineKeyboardButton inlineKeyboardButton = new InlineKeyboardButton();
        inlineKeyboardButton.setText("⬅️");
        inlineKeyboardButton.setCallbackData("PREV");

        return inlineKeyboardButton;
    }

    //! current page inline button
    public InlineKeyboardButton currentPage(Buyer buyer ) {
        InlineKeyboardButton inlineKeyboardButton = new InlineKeyboardButton();
        inlineKeyboardButton.setText(" " + buyer.getCurrentPage() + " ");
        inlineKeyboardButton.setCallbackData("PREV");

        return inlineKeyboardButton;
    }

    //! addCategory inline button
    public List<InlineKeyboardButton> addCategory() {
        InlineKeyboardButton inlineKeyboardButton = new InlineKeyboardButton();
        inlineKeyboardButton.setText("Add new category");
        inlineKeyboardButton.setCallbackData("ENTER_CATEGORY_NAME");

        List<InlineKeyboardButton> row = new ArrayList<>();
        row.add(inlineKeyboardButton);

        return row;
    }

    //! deleteCategory inline button
    public List<InlineKeyboardButton> deleteCategoryBtn() {
        InlineKeyboardButton inlineKeyboardButton = new InlineKeyboardButton();
        inlineKeyboardButton.setText("Delete category");
        inlineKeyboardButton.setCallbackData("DELETE_CATEGORY");

        List<InlineKeyboardButton> row = new ArrayList<>();
        row.add(inlineKeyboardButton);

        return row;
    }


//!Universal qlish kereak
    public  List<List<InlineKeyboardButton>> getItemList(CategoryService categoryService, Buyer buyer) {
        int i = 0;
        boolean getItem = false;

        List<List<InlineKeyboardButton>> list = new ArrayList<>();

        for (Category category : categoryService.getActives()) {
            if(buyer.getCurrentPage() * 10 - 10 == i)
                getItem = true;

            if(buyer.getCurrentPage() * 10 == i)
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
}
