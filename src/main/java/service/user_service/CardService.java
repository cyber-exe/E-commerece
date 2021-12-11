package service.user_service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import model.user.Buyer;
import model.user.Card;
import service.BaseService;
import service.paths.Root;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class CardService implements BaseService<Card, String> {
    List<Card> cards = new ArrayList<>();
    {
        try {
            List<Card> l;
            if((l = listFromJson(Root.cardsPath)) != null)
                this.cards = l;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Override
    public Card add(Card card) throws IOException {
        if(!this.check(card)) {
            this.cards.add(card);
            this.toJson(this.cards, Root.cardsPath);

            return card;
        }

        return null;
    }

    @Override
    public boolean delete(Card card) throws IOException {
        int idx = 0;

        for (Card card1 : cards) {
            if(card1.getId().equals(card.getId())) {
                card.setActive(false);
                cards.set(idx, card);
                this.toJson(cards, Root.cardsPath);

                return true;
            }

            idx++;
        }

        return false;
    }

    @Override
    public Card edit(Card card) throws IOException {
        if(card != null) {
            int idx = 0;

            for (Card card1 : cards) {
                if(card1.getId().equals(card.getId())) {
                    Card res = cards.set(idx, card);
                    this.toJson(cards, Root.cardsPath);

                    return res;
                }

                idx++;
            }
        }
        return null;
    }

    @Override
    public Card get(UUID id) {
        for (Card card : cards) {
            if(card.getId().equals(id))
                return card;
        }
        return null;
    }

    @Override
    public List<Card> getList() {
        return cards;
    }

    @Override
    public boolean check(Card card) {
        if(this.cards == null) {
            return false;
        }
        for(Card el : this.cards){
            if(el.getCardNum().equals(card.getCardNum()) && el.getExpireDate().equals(card.getExpireDate()))
                return true;
        }
        return false;
    }

    @Override
    public void toJson(List<Card> list, String path) throws IOException {
        BaseService.super.toJson(list, path);
    }


    public List<Card> listFromJson(String path) throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            byte[] bytes = Files.readAllBytes(new File(path).toPath());
            String str = new String(bytes);
            objectMapper.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);

            return objectMapper.readValue(str, new TypeReference<List<Card>>() {});
        } catch (Exception e) {
            return null;
        }
    }
}
