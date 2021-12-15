package service.product_service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import model.product.Category;
import service.BaseService;
import service.paths.Root;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class CategoryService implements BaseService<Category, String> {
    List<Category> categoryServices ;
    {
        try {
            this.categoryServices = Objects.requireNonNullElseGet(listFromJson(Root.categoriesPath), ArrayList::new);
        }catch (Exception e){
            e.printStackTrace();
            categoryServices = new ArrayList<>();
        }

    }

    @Override
    public Category add(Category category) throws Exception {
        this.categoryServices.add(category);
        this.updateJson(this.categoryServices, Root.categoriesPath);
        return category;
    }

    @Override
    public boolean delete(Category category) throws IOException {
        category.setActive(false);
        this.updateJson(this.categoryServices, Root.categoriesPath);
        return false;
    }

    @Override
    public Category edit(Category category) throws IOException {
        return null;
    }

    @Override
    public Category get(UUID id) {

        for (Category s :
                categoryServices) {
            if(s.getId().equals(id)) return s;
        }
        return null;
    }

    @Override
    public List<Category> getList() {
        return this.categoryServices;
    }

    @Override
    public boolean check(Category category) {
        return false;
    }

    @Override
    public List<Category> listFromJson(String path) throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.readValue(new File(path), new  TypeReference<List<Category>>() {
            });
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void updateJson(List<Category> list, String path) throws IOException {
        BaseService.super.updateJson(list, path);
    }
}
