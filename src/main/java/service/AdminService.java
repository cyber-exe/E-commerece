package service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import model.Admin;
import service.paths.Root;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class AdminService implements BaseService<Admin, String>{
    List<Admin> admins;
    {
        try{
            Objects.requireNonNullElseGet(listFromJson(Root.adminsPath), ArrayList::new);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public Admin add(Admin admin) throws IOException {
        this.admins.add(admin);
        this.updateJson(admins, Root.adminsPath);
        return null;
    }

    @Override
    public boolean delete(Admin admin) throws IOException {
        admin.setActive(false);
        this.updateJson(admins, Root.adminsPath);
        return false;
    }

    @Override
    public Admin edit(Admin admin) throws IOException {
        this.updateJson(admins, Root.adminsPath);
        return null;
    }

    @Override
    public Admin get(UUID id) {
        for(Admin admin : this.admins){
            if(admin.getId().equals(id))
                return admin;
        }
        return null;
    }

    @Override
    public List<Admin> getList() {
        return this.admins;
    }

    @Override
    public boolean check(Admin admin) {
        for(Admin el : this.admins){
            if(el.getPhone().equals(admin.getPhone()) || el.getEmail().equals(admin.getEmail()))
                return true;
        }
        return false;
    }

    @Override
    public List<Admin> listFromJson(String path) throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        try{
            return objectMapper.readValue(new File(path), new TypeReference<List<Admin>>() {});
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void updateJson(List<Admin> list, String path) throws IOException {
        BaseService.super.updateJson(list, path);
    }
}
