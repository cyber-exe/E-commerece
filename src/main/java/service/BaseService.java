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
    I add(I i) throws IOException, Exception;
    boolean delete(I i) throws IOException;
    I edit(I i) throws IOException;
    I get(UUID id);
    List<I> getList();
    List<I> getActives();
    boolean check(I i);
    List<I> listFromJson(String path) throws Exception;
    default void updateJson(List<I> list, String path) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.writerWithDefaultPrettyPrinter().writeValue(new File(path), list);
    }
}
