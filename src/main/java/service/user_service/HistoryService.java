package service.user_service;

import model.user.History;
import service.BaseService;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

public class HistoryService implements BaseService<History, String> {
    @Override
    public History add(History history) throws IOException {
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
}
