package service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import model.user.Buyer;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.SplittableRandom;
import java.util.UUID;

public interface BaseService<I,T> {
    I add(I i) throws IOException;
    boolean delete(I i) throws IOException;
    I edit(I i) throws IOException;
    I get(UUID id);
    List<I> getList();
    boolean check(I i);


    default void toJson(List<I> list, String path) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.writerWithDefaultPrettyPrinter().writeValue(new File(path), list);
    }

    default List<I> listFromJson(List<I> list, String path) throws Exception{
            ObjectMapper objectMapper = new ObjectMapper();
        try {
            List<I> helper = objectMapper.readValue(new File(path), new TypeReference<List<I>>() {});
            list = helper;
            return list;
        } catch (Exception e) {
            return null;
        }
    }
}
