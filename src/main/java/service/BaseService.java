package service;

import java.util.List;
import java.util.SplittableRandom;
import java.util.UUID;

public interface BaseService<I,T> {
    I add(I i);
    boolean delete(I i);
    I edit(I i);
    I get(UUID id);

    private void toJson(List<I> list, String path) {

    }

    List<I> list(String path);
}
