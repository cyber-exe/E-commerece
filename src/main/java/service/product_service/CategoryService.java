package service.product_service;

import model.product.Category;
import service.BaseService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class CategoryService implements BaseService<Category, String> {
    List<CategoryService> categoryServices = new ArrayList<>();


    @Override
    public Category add(Category category) throws IOException {
        return null;
    }

    @Override
    public boolean delete(Category category) throws IOException {
        return false;
    }

    @Override
    public Category edit(Category category) throws IOException {
        return null;
    }

    @Override
    public Category get(UUID id) {
        return null;
    }

    @Override
    public List<Category> getList() {
        return null;
    }

    @Override
    public boolean check(Category category) {
        return false;
    }

    @Override
    public List<Category> listFromJson(String path) throws Exception {
        return null;
    }

    @Override
    public void updateJson(List<Category> list, String path) throws IOException {
        BaseService.super.updateJson(list, path);
    }
}
