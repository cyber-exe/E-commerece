package bot;

import model.user.Buyer;
import service.user_service.BuyerService;

import java.util.List;

public class UserService {
    BuyerService buyerService = new BuyerService();

    public Buyer getUserByChatId(long chatId) {
        List<Buyer> list = buyerService.getList();

        for (Buyer buyer : list) {
            if(buyer.getChatId() == chatId)
                return buyer;
        }

        return null;
    }
}
