package service.product_service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import model.product.SubCategory;
import service.BaseService;
import service.paths.Root;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.UUID;

public class SubCategoryService implements BaseService<SubCategory, String> {
    List<SubCategory> subCategories;
    {
        try{
            this.subCategories = Objects.requireNonNullElseGet(this.listFromJson(Root.subCategoriesPath),ArrayList::new);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    @Override
    public SubCategory add(SubCategory subCategory) throws IOException {
        this.subCategories.add(subCategory);
        this.updateJson(this.subCategories, Root.subCategoriesPath);
        return subCategory;
    }

    @Override
    public boolean delete(SubCategory subCategory) throws IOException {
        subCategory.setActive(false);
        this.updateJson(this.subCategories, Root.subCategoriesPath);
        return true;
    }

    @Override
    public SubCategory edit(SubCategory subCategory) throws IOException {
        this.updateJson(this.subCategories, Root.subCategoriesPath);
        return subCategory;
    }

    @Override
    public SubCategory get(UUID id) {
        for(SubCategory subCategory : this.subCategories){
            if(subCategory.getId().equals(id))
                return subCategory;
        }
        return null;
    }

    @Override
    public List<SubCategory> getList() {
        return this.subCategories;
    }

    @Override
    public boolean check(SubCategory subCategory) {
        for(SubCategory sub : this.subCategories){
            if(sub.getName().equals(subCategory.getName()))
                return false;
        }
        return true;
    }

    @Override
    public List<SubCategory> listFromJson(String path) throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.readValue(new File(path), new TypeReference<List<SubCategory>>() {});
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void updateJson(List<SubCategory> list, String path) throws IOException {
        BaseService.super.updateJson(list, path);
    }
}
