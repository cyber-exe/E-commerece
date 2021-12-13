package service.product_service;

import model.product.SubCategory;
import service.BaseService;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

public class SubCategoryService implements BaseService<SubCategory, String> {
    @Override
    public SubCategory add(SubCategory subCategory) throws IOException {
        return null;
    }

    @Override
    public boolean delete(SubCategory subCategory) throws IOException {
        return false;
    }

    @Override
    public SubCategory edit(SubCategory subCategory) throws IOException {
        return null;
    }

    @Override
    public SubCategory get(UUID id) {
        return null;
    }

    @Override
    public List<SubCategory> getList() {
        return null;
    }

    @Override
    public boolean check(SubCategory subCategory) {
        return false;
    }

    @Override
    public List<SubCategory> listFromJson(String path) throws Exception {
        return null;
    }

    @Override
    public void updateJson(List<SubCategory> list, String path) throws IOException {
        BaseService.super.updateJson(list, path);
    }
}
