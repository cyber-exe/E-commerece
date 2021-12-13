package service.user_service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import model.user.Card;
import service.BaseService;
import service.paths.Root;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class CardService implements BaseService<Card, String> {
    List<Card> cards;
    {
        try {
            Objects.requireNonNullElseGet(this.listFromJson(Root.cardsPath), ArrayList::new);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Override
    public Card add(Card card) throws IOException {
        if(!this.check(card)) {
            this.cards.add(card);
            this.updateJson(this.cards, Root.cardsPath);
            return card;
        }
        return null;
    }

    @Override
    public boolean delete(Card card) throws IOException {
        card.setActive(false);
        this.updateJson(cards, Root.cardsPath);
        return false;
    }

    @Override
    public Card edit(Card card) throws IOException {
        this.updateJson(cards, Root.cardsPath);
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
    public void updateJson(List<Card> list, String path) throws IOException {
        BaseService.super.updateJson(list, path);
    }

    @Override
    public List<Card> listFromJson(String path) throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.readValue(new File(path), new TypeReference<List<Card>>() {});
        } catch (Exception e) {
            return null;
        }
    }
}
