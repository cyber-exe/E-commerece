package service.user_service;

import model.user.Buyer;
import service.BaseService;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class BuyerService implements BaseService<Buyer, String> {
    final static List<Buyer>  buyers = new ArrayList<>();
    @Override
    public Buyer add(Buyer buyer) {
        return null;
    }

    @Override
    public boolean delete(Buyer buyer) {
        return false;
    }

    @Override
    public Buyer edit(Buyer buyer) {
        return null;
    }

    @Override
    public Buyer get(UUID id) {
        return null;
    }

    @Override
    private void toJson(List<Buyer> list, String path) {
        String s = "C:\\Users\\delta\\Documents\\E-commerce\\src\\main\\resources\\buyers.json";
    }

    @Override
    public List<Buyer> list(String path) {
        return null;
    }
}
