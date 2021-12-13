package service.product_service;

import model.product.Product;
import service.BaseService;
import service.paths.Root;

import java.io.IOException;
import java.util.*;
import java.util.UUID;

public class ProductService implements BaseService<Product, String> {
    List<Product> products;

    {
        try{
            this.products = Objects.requireNonNullElseGet(listFromJson(Root.productsPath), ArrayList::new);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public Product add(Product product) throws IOException {
        product.setCreatedAt(new Date());
        this.products.add(product);
        this.updateJson(this.products, Root.productsPath);
        return product;
    }

    @Override
    public boolean delete(Product product) throws IOException {
        product.setActive(false);
        this.updateJson(this.products, Root.productsPath);
        return true;
    }

    @Override
    public Product edit(Product product) throws IOException {
        product.setUpdatedAt(new Date());
        this.updateJson(this.products, Root.productsPath);
        return product;
    }

    @Override
    public Product get(UUID id) {
        for(Product product : this.products){
            if(product.getId().equals(id))
                return product;
        }
        return null;
    }

    @Override
    public List<Product> getList() {
        return this.products;
    }

    @Override
    public boolean check(Product product) {
        return false;
    }

    @Override
    public List<Product> listFromJson(String path) throws Exception {
        return null;
    }

    @Override
    public void updateJson(List<Product> list, String path) throws IOException {
        BaseService.super.updateJson(list, path);
    }
}
