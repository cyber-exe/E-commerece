package service.user_service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import model.user.Buyer;
import org.apache.poi.util.StringUtil;
import service.BaseService;
import service.paths.Root;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;


public class BuyerService implements BaseService<Buyer, String> {
    List<Buyer> buyers;

    {
        try {
            this.buyers = Objects.requireNonNullElseGet(listFromJson(Root.buyersPath), ArrayList::new);
        } catch (Exception e) {
            e.printStackTrace();
            buyers = new ArrayList<>();
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

    @Override
    public List<Buyer> getList() {
        return this.buyers;
    }

    @Override
    public List<Buyer> getActives() {
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
    public List<Buyer> listFromJson(String path) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.readValue(new File(path), new TypeReference<List<Buyer>>() {
            });
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
