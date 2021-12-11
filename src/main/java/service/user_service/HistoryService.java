package service.user_service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import model.product.Product;
import model.user.History;
import model.user.Seller;
import service.BaseService;
import service.paths.Root;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class HistoryService implements BaseService<History, String> {

    List<History> histories= new ArrayList<>();
    List<Product> selectedProducts = new ArrayList<>();
    {
        try {
            this.histories = listFromJson(Root.historiesPath);
        }catch (Exception e){
            e.printStackTrace();
        }

    }


    @Override
    public History add(History history) throws IOException {
        if(!this.check(history)){
            histories.add(history);
            this.toJson(histories, Root.historiesPath);

            return history;
        }
        return null;
    }

    @Override
    public boolean delete(History history) throws IOException {
        return false;
    }

    @Override
    public History edit(History history) throws IOException {
        return null;
    }

    @Override
    public History get(UUID id) {
        return null;
    }

    @Override
    public List<History> getList() {
        return null;
    }

    @Override
    public boolean check(History history) {
        for(History h: histories){
            if(h.getId().equals(history.getId()) )
                return true;
        }
        return false;
    }

    @Override
    public void toJson(List<History> list, String path) throws IOException {
        BaseService.super.toJson(list, path);
    }

    @Override
    public List<History> listFromJson(List<History> list, String path) throws Exception {
        return BaseService.super.listFromJson(list, path);
    }
    private List<History> listFromJson(String historiesPath) {
        ObjectMapper objectMapper = new ObjectMapper();
        try{
            return objectMapper.readValue(new File(historiesPath), new TypeReference<List<History>>(){});

        }catch (Exception e){
            return null;
        }


    }
}
