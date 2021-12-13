package service.user_service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import model.user.History;
import service.BaseService;
import service.paths.Root;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class HistoryService implements BaseService<History, String> {
    List<History> histories;

    {
        try{
            Objects.requireNonNullElseGet(this.listFromJson(Root.historiesPath), ArrayList::new);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    @Override
    public History add(History history) throws IOException {
        this.histories.add(history);
        this.updateJson(this.histories, Root.historiesPath);
        return history;
    }

    @Override
    public boolean delete(History history) throws IOException {
        history.setActive(false);
        this.updateJson(this.histories, Root.historiesPath);
        return true;
    }

    @Override
    public History edit(History history) throws IOException {
//        this.updateJson(this.histories, Root.historiesPath);
        return null;
    }

    @Override
    public History get(UUID id) {
        for(History history : this.histories){
            if(history.getId().equals(id))
                return history;
        }
        return null;
    }

    @Override
    public List<History> getList() {
        return this.histories;
    }

    @Override
    public boolean check(History history) {
        return false;
    }

    @Override
    public void updateJson(List<History> list, String path) throws IOException {
        BaseService.super.updateJson(list, path);
    }

    @Override
    public List<History> listFromJson(String path) throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.readValue(new File(path), new TypeReference<List<History>>() {});
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
}
