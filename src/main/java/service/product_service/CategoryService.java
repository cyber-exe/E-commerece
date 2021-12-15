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
    List<Category> categoryList ;
    {
        try {
            this.categoryList = Objects.requireNonNullElseGet(listFromJson(Root.categoriesPath), ArrayList::new);
        }catch (Exception e){
            e.printStackTrace();
            categoryList = new ArrayList<>();
        }

    }

    @Override
    public Category add(Category category) throws Exception {
        this.categoryList.add(category);
        this.updateJson(this.categoryList, Root.categoriesPath);
        return category;
    }

    @Override
    public boolean delete(Category category) throws IOException {
        category.setActive(false);
        this.updateJson(this.categoryList, Root.categoriesPath);
        return false;
    }

    @Override
    public Category edit(Category category) throws IOException {
        return null;
    }

    @Override
    public Category get(UUID id) {
        for (Category s :
                categoryList) {
            if(s.getId().equals(id)) return s;
        }
        return null;
    }

    @Override
    public List<Category> getList() {
        return this.categoryList;
    }

    @Override
    public List<Category> getActives() {
        List<Category> list = new ArrayList<>();

        for (Category category : categoryList) {
            if(!category.isActive())
                list.add(category);
        }

        return list;
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
