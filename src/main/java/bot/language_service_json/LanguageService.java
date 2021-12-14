package bot.language_service_json;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import model.user.Buyer;
import service.BaseService;
import service.paths.Root;

import java.awt.*;
import java.awt.font.TextHitInfo;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;


@Data
@AllArgsConstructor
@NoArgsConstructor

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class LanguageService implements BaseService<Language, String> {
    List<Language> languageList;

    {
        try {
            this.languageList = Objects.requireNonNullElse(listFromJson(Root.languagesPath), new ArrayList<>());

        }catch (Exception e){
            e.printStackTrace();
            languageList = new ArrayList<>();
        }
    }


    @Override
    public Language add(Language language) throws Exception {
        this.languageList.add(language);
        this.updateJson(this.languageList, Root.languagesPath);
        return language;
    }






    public Content addContentKey(String key, String value){
        for (Language lan :
                languageList) {
            for (Content content: lan.getContents()) {
                content.setKey(key);
                content.setValue(value);
                return content;
            }
        }
        return null;
    }



    @Override
    public boolean delete(Language language) throws IOException {
        return false;
    }

    @Override
    public Language edit(Language language) throws IOException {
        return null;
    }


    public Content editValue(String key, String value ) throws IOException {
        for (Language lan:
             languageList) {
            for (Content content: lan.getContents()) {
                if(content.getKey().equals(key)){
                    content.setValue(value);
                    return content;
                }
            }
        }
        return null;
    }


    public Language edit(Language language, String newLan) throws IOException {
        for (Language lan :
                languageList) {
            if(lan.getLang().equals(language.getLang())){
                lan.setLang(newLan);
                return lan;
            }
        }
        return null;
    }

    @Override
    public Language get(UUID id) {


        return null;
    }



    public Language getStrLan(String lan) {
        for (Language language : languageList){
            if(language.getLang().equals(lan))
                return language;
        }
        return null;
    }

    @Override
    public List<Language> getList() {
        return this.getLanguageList();
    }

    @Override
    public boolean check(Language language) {
        return false;
    }


    public boolean checkStrLan(String lan) {
        for (Language language : languageList) {
            if(language.getLang().equals(lan)) return true;
        }
        return false;
    }

    @Override
    public List<Language> listFromJson(String path) throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        try{
            return objectMapper.readValue(new File(path), new TypeReference<List<Language>>() {
            });

        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void updateJson(List<Language> list, String path) throws IOException {
        BaseService.super.updateJson(list, path);
    }
}
