package service.user_service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import model.user.Buyer;
import model.user.Seller;
import service.BaseService;
import service.paths.Root;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class SellerService implements BaseService<Seller, String> {

    List<Seller> sellerList = new ArrayList<>();
    {
        try {

            this.sellerList = listFromJson(Root.sellersPath);
        }catch (Exception e){
            e.printStackTrace();
        }
    }


    @Override
    public Seller add(Seller seller) throws IOException {
        if(!this.check(seller)) {
            sellerList.add(seller);
            this.toJson(sellerList, Root.buyersPath);

            return seller;
        }

        return null;
    }

    @Override
    public boolean delete(Seller seller) throws IOException {
        int idx = 0;

        for (Seller seller1 : sellerList) {
            if(seller1.getId().equals(seller.getId())) {
                seller.setActive(false);
                sellerList.set(idx, seller);
                this.toJson(sellerList, Root.buyersPath);

                return true;
            }

            idx++;
        }

        return false;
    }

    @Override
    public Seller edit(Seller seller) throws IOException {
        if(seller != null) {
            int idx = 0;

            for (Seller seller1: sellerList) {
                if(seller1.getId().equals(seller.getId())) {
                    Seller res = sellerList.set(idx, seller);
                    this.toJson(sellerList, Root.buyersPath);

                    return res;
                }

                idx++;
            }
        }
        return null;
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
    public boolean check(Seller seller) {
        for(Seller s: sellerList){
            if(s.getPhoneNumber().equals(seller.getPhoneNumber()) && s.getEmail().equals(seller.getEmail()))
                return true;

        }
        return false;
    }

    @Override
    public void toJson(List<Seller> list, String path) throws IOException {
        BaseService.super.toJson(list, path);
    }

    @Override
    public List<Seller> listFromJson(List<Seller> list, String path) throws Exception {
        return BaseService.super.listFromJson(list, path);
    }


    private List<Seller> listFromJson(String sellersPath) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.readValue(new File(sellersPath), new TypeReference<List<Seller>>(){});

        } catch (IOException e) {
            return null;
        }

    }
}
