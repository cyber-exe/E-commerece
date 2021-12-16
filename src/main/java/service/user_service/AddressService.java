package service.user_service;

import model.user.Address;
import service.BaseService;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

public class AddressService implements BaseService<Address,String> {
    @Override
    public Address add(Address address) throws IOException {
        return null;
    }

    @Override
    public boolean delete(Address address) throws IOException {
        return false;
    }

    @Override
    public Address edit(Address address) throws IOException {
        return null;
    }

    @Override
    public Address get(UUID id) {
        return null;
    }

    @Override
    public List<Address> getList() {
        return null;
    }

    @Override
    public List<Address> getActives(String data) {
        return null;
    }

    @Override
    public boolean check(Address address) {
        return false;
    }

    @Override
    public List<Address> listFromJson(String path) throws Exception {
        return null;
    }

    @Override
    public void updateJson(List<Address> list, String path) throws IOException {
        BaseService.super.updateJson(list, path);
    }
}
