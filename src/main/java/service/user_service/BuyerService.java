package service.user_service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import model.user.Buyer;
import service.BaseService;
import service.paths.Root;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


public class BuyerService implements BaseService<Buyer, String> {
    List<Buyer> buyers = new ArrayList<>();

    {
        try {
            this.buyers = listFromJson(Root.buyersPath);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public Buyer add(Buyer buyer) throws IOException {
        if(!this.check(buyer)) {
            buyers.add(buyer);
            this.toJson(buyers, Root.buyersPath);

            return buyer;
        }

        return null;
    }

    @Override
    public boolean delete(Buyer buyer) throws IOException {
        int idx = 0;

        for (Buyer buyer1 : buyers) {
            if(buyer1.getId().equals(buyer.getId())) {
                buyer.setActive(false);
                buyers.set(idx, buyer);
                this.toJson(buyers, Root.buyersPath);

                return true;
            }

            idx++;
        }

        return false;
    }

    @Override
    public Buyer edit(Buyer buyer) throws IOException {
        if(buyer != null) {
            int idx = 0;

            for (Buyer buyer1 : buyers) {
                if(buyer1.getId().equals(buyer.getId())) {
                    Buyer res = buyers.set(idx, buyer);
                    this.toJson(buyers, Root.buyersPath);

                    return res;
                }

                idx++;
            }
        }
        return null;
    }

    @Override
    public Buyer get(UUID id) {
        for (Buyer buyer : buyers) {
            if(buyer.getId().equals(id))
                return buyer;
        }
        return null;
    }

    @Override
    public List<Buyer> getList() {
        return this.buyers;
    }


    @Override
    public boolean check(Buyer buyer) {
        for(Buyer el : buyers){
            if(el.getPhone().equals(buyer.getPhone()) && el.getEmail().equals(buyer.getEmail()))
                return true;
        }
        return false;
    }

    @Override
    public void toJson(List<Buyer> list, String path) throws IOException {
        BaseService.super.toJson(list, path);
    }

    @Override
    public List<Buyer> listFromJson(List<Buyer> list, String path) throws Exception {
        return BaseService.super.listFromJson(list, path);
    }

    public List<Buyer> listFromJson(String path) {
        //return BaseService.super.listFromJson(list, path);

        ObjectMapper objectMapper = new ObjectMapper();
        try {
            byte[] bytes = Files.readAllBytes(new File(path).toPath());
            String str = new String(bytes);
            objectMapper.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);

            return objectMapper.readValue(str, new TypeReference<List<Buyer>>() {});
        } catch (Exception e) {
            return null;
        }
    }

}
