package service.user_service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import model.user.Basket;
import service.BaseService;
import service.paths.Root;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class BasketService implements BaseService<Basket, String> {
    List<Basket> baskets;

    {
        try {
            this.baskets = Objects.requireNonNullElseGet(listFromJson(Root.basketsPath), ArrayList::new);
        }catch (Exception e){
            e.printStackTrace();
        }
    }


    @Override
    public Basket add(Basket basket) throws IOException {
        if(!this.check(basket)) {
            baskets.add(basket);
            this.updateJson(baskets, Root.basketsPath);
            return basket;
        }
        return null;
    }

    @Override
    public boolean delete(Basket basket) throws IOException {
        return false;
    }

    @Override
    public Basket edit(Basket basket) throws IOException {
        this.updateJson(this.baskets, Root.basketsPath);
        return basket;
    }

    @Override
    public Basket get(UUID id) {
        for (Basket basket : this.baskets) {
            if(basket.getId().equals(id))
                return basket;
        }
        return null;
    }

    @Override
    public List<Basket> getList() {
        return this.baskets;
    }

    @Override
    public List<Basket> getActives() {
        return null;
    }

    @Override
    public boolean check(Basket basket) {
        for(Basket b : this.baskets){
            if(b.getOwnerId().equals(basket.getOwnerId()) && b.getProductId().equals(basket.
            getProductId()))
                return true;
        }
        return false;
    }

    @Override
    public void updateJson(List<Basket> list, String path) throws IOException {
        BaseService.super.updateJson(list, path);
    }

    @Override
    public List<Basket> listFromJson(String basketsPath) {
        ObjectMapper objectMapper = new ObjectMapper();
        try{
            return objectMapper.readValue(new File(basketsPath), new TypeReference<List<Basket>>() {});
        }catch (Exception e){
            return null;
        }
    }
}
