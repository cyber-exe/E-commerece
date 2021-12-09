package service.user_service;

import model.user.Buyer;
import service.BaseService;
import service.paths.Root;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


public class BuyerService implements BaseService<Buyer, String> {
    List<Buyer> buyers = new ArrayList<>();
    {
        try {
            buyers = listFromJson(buyers, Root.buyersPath);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public Buyer add(Buyer buyer) throws IOException {
        if(!this.check(buyer)) {
            buyers.add(buyer);
            this.toJson(buyers, Root.buyersPath);
            return buyer;
        }

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
    public boolean check(Buyer buyer) {
        for(Buyer el : buyers){
            if(el.getPhone().equals(buyer.getPhone()) && el.getEmail().equals(buyer.getEmail()))
                return true;
        }
        return false;
    }

    @Override
    public void toJson(List<Buyer> list, String path) throws IOException {
        BaseService.super.toJson(list, path);
    }

    @Override
    public List<Buyer> listFromJson(List<Buyer> list, String path) throws Exception {
        return BaseService.super.listFromJson(list, path);
    }

}
