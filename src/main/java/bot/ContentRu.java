package bot;

import lombok.Data;

@Data
class ContentRu extends ManageLang{
    ContentRu(){
        start_header = "Выбран русский язык! ";
        sign_in = "Войти";
        sign_up = "Зарегистрироваться";
    }
}
