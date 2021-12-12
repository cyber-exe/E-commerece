package bot;

import lombok.Data;

@Data
class ContentRu extends ManageLang{
    ContentRu(){
        selected_lang = "Выбран русский язык! ";
        main_header = "Вход в систему";
        sign_in = "Войти";
        sign_up = "Зарегистрироваться";
        enter_email = "Введите свою эл. почту";
    }
}
