import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import model.user.Buyer;
import model.user.Gender;
import service.user_service.BuyerService;

import java.io.*;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String[] args) throws IOException {
//        System.out.println("Hello ");
//        Buyer buyer = new Buyer();
//        buyer.setName("dsa");
//        buyer.setEmail("husan");
//        buyer.setPhone("123");
//        List<Buyer> l1 = new ArrayList<>();
//        l1.add(buyer);
//        ObjectMapper objectMapper = new ObjectMapper();
//        String str = objectMapper.writeValueAsString(l1);
//        System.out.println(str);
//
//        FileOutputStream writer = new FileOutputStream("C:\\Users\\User\\IdeaProjects\\E-commerece\\src\\main\\resources\\buyers.json");
//        writer.write(str.getBytes());
//        writer.close();

//        FileInputStream reader = new FileInputStream("C:\\Users\\User\\IdeaProjects\\E-commerece\\src\\main\\resources\\buyers.json");

//        byte[] bytes = Files.readAllBytes(new File("C:\\Users\\User\\IdeaProjects\\E-commerece\\src\\main\\resources\\buyers.json").toPath());
//        ObjectMapper objectMapper = new ObjectMapper();
//        String str = new String(bytes);
//        objectMapper.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);
//        List<Buyer> buyer = objectMapper.readValue(str, new TypeReference<List<Buyer>>() {});
//        System.out.println("node" + buyer.get(0));

        BuyerService buyerService = new BuyerService();

        List<Buyer> list = buyerService.getList();
        for (Buyer buyer : list) {
            System.out.println(buyer.toString());
        }
        Buyer buyer = new Buyer();
        buyer.setName("Jasur");
        buyer.setEmail("Jasur");
        buyer.setPhone("123");
        buyerService.add(buyer);


    }
}
