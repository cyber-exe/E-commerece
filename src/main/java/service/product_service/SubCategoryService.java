package service.product_service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import model.product.SubCategory;
import service.BaseService;
import service.paths.Root;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class SubCategoryService implements BaseService<SubCategory, String> {
    List<SubCategory> subCategoryList;
    {
        try {
            this.subCategoryList = Objects.requireNonNullElseGet(listFromJson(Root.subCategoriesPath), ArrayList::new);

        }catch (Exception e){
            e.printStackTrace();

        }
    }


    @Override
    public SubCategory add(SubCategory subCategory) throws IOException {
        this.subCategoryList.add(subCategory);
        this.updateJson(this.subCategoryList, Root.subCategoriesPath);
        return subCategory;
    }

    @Override
    public boolean delete(SubCategory subCategory) throws IOException {
        subCategory.setActive(false);
        this.updateJson(this.subCategoryList, Root.categoriesPath);
        return false;
    }

    @Override
    public SubCategory edit(SubCategory subCategory) throws IOException {
        return null;
    }

    @Override
    public SubCategory get(UUID id) {
        for (SubCategory s :
                subCategoryList) {
            if(s.getId().equals(id)) return s;
        }
        return null;
    }

    @Override
    public List<SubCategory> getList() {
        return  this.subCategoryList;
    }

    @Override
    public List<SubCategory> getActives() {
        List<SubCategory> list = new ArrayList<>();

        for (SubCategory sub : subCategoryList) {
            if(sub.isActive())
                list.add(sub);
        }

        return list;
    }

    @Override
    public boolean check(SubCategory subCategory) {
        for(SubCategory el : subCategoryList){
            if(el.getId().equals(subCategory.getId()) && el.getParentId().equals(subCategory.getParentId()))
                return true;
        }
        return false;
    }

    @Override
    public List<SubCategory> listFromJson(String path) throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.readValue(new File(path), new TypeReference<List<SubCategory>>() {
            });
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void updateJson(List<SubCategory> list, String path) throws IOException {
        BaseService.super.updateJson(list, path);
    }
}
