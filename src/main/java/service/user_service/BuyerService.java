package service.user_service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import model.product.Category;
import model.user.Buyer;
import org.apache.poi.util.StringUtil;
import service.BaseService;
import service.paths.Root;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.*;


public class BuyerService implements BaseService<Buyer, String> {
    List<Buyer> buyers;

    {
        try {
            this.buyers = Objects.requireNonNullElseGet(listFromJson(Root.buyersPath), ArrayList::new);
        } catch (Exception e) {
//            buyers = new ArrayList<>();
            e.printStackTrace();

        }
    }


    @Override
    public Buyer add(Buyer buyer) throws IOException {

        this.buyers.add(buyer);
        this.updateJson(this.buyers, Root.buyersPath);
        return buyer;
    }



    @Override
    public boolean delete(Buyer buyer) throws IOException {
        buyer.setActive(false);
        this.updateJson(this.buyers, Root.buyersPath);
        return false;
    }

    @Override
    public Buyer edit(Buyer buyer) throws IOException {
        for(Buyer buyer1 : this.buyers){
            System.out.println(buyer1.toString());
        }
        this.updateJson(this.buyers, Root.buyersPath);
        return buyer;
    }

    @Override
    public Buyer get(UUID id) {
        for (Buyer buyer : buyers){
            if(buyer.getId().equals(id))
                return buyer;
        }
        return null;
    }

    public Buyer getUserByChatId(long chatId) {
        for (Buyer buyer : buyers) {
            if(buyer.getChatId() == chatId)
                return buyer;
        }
        return null;
    }

    @Override
    public List<Buyer> getList() {
        return this.buyers;
    }

    @Override
    public List<Buyer> getActives(String data) {

        List<Buyer> list = new ArrayList<>();

        for (Buyer buyer : buyers) {
            if(buyer.isActive())
                list.add(buyer);
        }

        return list;
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
    public List<Buyer> listFromJson(String path) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.readValue(new File(path), new TypeReference<List<Buyer>>() {});
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void updateJson(List<Buyer> list, String path) throws IOException {
        BaseService.super.updateJson(list, path);
    }
}
