package service.user_service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import model.user.Basket;
import model.user.Buyer;
import model.user.History;
import service.BaseService;
import service.paths.Root;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class BasketService implements BaseService<Basket, String> {
    List<Basket> basketList = new ArrayList<>();

    {
        try {
            this.basketList = listFromJson(Root.basketsPath);


        }catch (Exception e){
            e.printStackTrace();
        }

    }


    @Override
    public Basket add(Basket basket) throws IOException {
        if(!this.check(basket)) {
            basketList.add(basket);
            this.toJson(basketList, Root.buyersPath);

            return basket;
        }

        return null;
    }

    @Override
    public boolean delete(Basket basket) throws IOException {
        int idx = 0;

        for (Basket basket1 : basketList) {
            if(basket1.getId().equals(basket.getId())) {
                basket1.setActive(false);
                basketList.set(idx, basket);
                this.toJson(basketList, Root.buyersPath);

                return true;
            }

            idx++;
        }

        return false;
    }

    @Override
    public Basket edit(Basket basket) throws IOException {

        if(basket != null) {
            int idx = 0;

            for (Basket basket1 : basketList) {
                if(basket1.getId().equals(basket.getId())) {
                    Basket res = basketList.set(idx, basket);
                    this.toJson(basketList, Root.buyersPath);

                    return res;
                }

                idx++;
            }
        }
        return null;
    }

    @Override
    public Basket get(UUID id) {
        for (Basket basket : basketList) {
            if(basket.getId().equals(id))
                return basket;
        }
        return null;
    }

    @Override
    public List<Basket> getList() {
        return this.basketList;
    }

    @Override
    public boolean check(Basket basket) {
        for(Basket b : basketList){
            if(b.getOwnerId().equals(basket.getOwnerId()) && b.getProductId().equals(basket.
            getProductId()))
                return true;
        }
        return false;
    }

    @Override
    public void toJson(List<Basket> list, String path) throws IOException {
        BaseService.super.toJson(list, path);
    }

    @Override
    public List<Basket> listFromJson(List<Basket> list, String path) throws Exception {
        return BaseService.super.listFromJson(list, path);
    }


    private List<Basket> listFromJson(String basketsPath) {
        ObjectMapper objectMapper = new ObjectMapper();
        try{
            byte[] bytes = Files.readAllBytes(new File(basketsPath).toPath());
            String str = new String(bytes);

            return objectMapper.readValue(str, new TypeReference<List<Basket>>() {});
        }catch (Exception e){
            return null;
        }

    }
}
