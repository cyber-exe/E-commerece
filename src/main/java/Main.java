import bot.TgBot;
import bot.language_service_json.Content;
import bot.language_service_json.Language;
import bot.language_service_json.LanguageService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import model.user.Buyer;
import model.user.Card;
import model.user.Gender;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;
import service.user_service.BuyerService;
import service.user_service.CardService;

import javax.xml.crypto.Data;
import java.io.*;
import java.nio.file.Files;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class Main {

    public static void main(String[] args) throws Exception {
//        System.out.println("Hello ");
//        BuyerService buyerService = new BuyerService();
//        Buyer buyer = new Buyer();
//        buyer.setName("dsa");
//        buyer.setEmail("husan");
//        buyer.setPhone("123");
//        buyerService.add(buyer);
//
//        Buyer buyer1 = new Buyer();
//        buyer1.setName("hello");
//        buyerService.add(buyer1);

//          ObjectMapper objectMapper = new ObjectMapper();
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

//        BuyerService buyerService = new BuyerService();
//
//        List<Buyer> list = buyerService.getList();
//        for (Buyer buyer : list) {
//            System.out.println(buyer.toString());
//        }
//        Buyer buyer = new Buyer();
//        //buyer.setId(UUID.fromString("a555823b-c4b2-42ee-a19b-e870d4b4b98f"));
//        buyer.setName("ALISHER");
//        buyer.setEmail("BEK");
//        buyer.setPhone("000");
//        buyerService.add(buyer);

        /*
        CardService cardService = new CardService();
//        List<Card> list1 = cardService.getList();
//        for (Card card : list1) {
//            System.out.println(card.toString());
//        }

        Card card = new Card();
        card.setCardNum("123456789");
//        card.setExpireDate(LocalDate.of(2020, 1, 8));
        card.setOwnerId(UUID.fromString("a555823b-c4b2-42ee-a19b-e870d4b4b98f"));
        cardService.add(card);*/
        LanguageService languageService = new LanguageService();
        Language language = new Language();
        language.setLang("Uz");
        List<Content> contentList = new ArrayList<>();
        Content content = new Content();
        content.setKey("Start");
        content.setValue("Hello from other side");
        contentList.add(content);
        language.setContents(contentList);
        languageService.add(language);



        TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
        telegramBotsApi.registerBot(new TgBot());
    }
}
