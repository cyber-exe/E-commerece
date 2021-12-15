package service.user_service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import model.user.Seller;
import service.BaseService;
import service.paths.Root;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class SellerService implements BaseService<Seller, String> {

    List<Seller> sellerList;
    {
        try {
            this.sellerList  = Objects.requireNonNullElseGet(this.listFromJson(Root.sellersPath),ArrayList::new);
        }catch (Exception e){
            e.printStackTrace();
        }
    }


    @Override
    public Seller add(Seller seller) throws IOException {
        if(!this.check(seller)) {
            sellerList.add(seller);
            this.updateJson(sellerList, Root.buyersPath);

            return seller;
        }

        return null;
    }

    @Override
    public boolean delete(Seller seller) throws IOException {
        seller.setActive(false);
        this.updateJson(sellerList, Root.sellersPath);
        return true;
    }

    @Override
    public Seller edit(Seller seller) throws IOException {
        this.updateJson(this.sellerList, Root.sellersPath);
        return seller;
    }

    @Override
    public Seller get(UUID id) {
        for (Seller s :
                sellerList) {
            if(s.getId().equals(id)) return s;
        }
        return null;
    }

    @Override
    public List<Seller> getList() {
        return this.sellerList;
    }

    @Override
    public List<Seller> getActives() {
        return null;
    }

    @Override
    public boolean check(Seller seller) {
        for(Seller s: sellerList){
            if(s.getPhoneNumber().equals(seller.getPhoneNumber()) || s.getEmail().equals(seller.getEmail()))
                return true;
        }
        return false;
    }

    @Override
    public void updateJson(List<Seller> list, String path) throws IOException {
        BaseService.super.updateJson(list, path);
    }

    @Override
    public List<Seller> listFromJson(String sellersPath) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.readValue(new File(sellersPath), new TypeReference<List<Seller>>(){});
        } catch (IOException e) {
            return null;
        }

    }
}
