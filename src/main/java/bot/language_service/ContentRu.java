package bot.language_service;

import lombok.Data;

@Data
public
class ContentRu extends ManageLang{
    public ContentRu(){
        selected_lang = "Выбран русский язык! ";
        enter_to_system = "Вход в систему";
        sign_in = "Войти";
        sign_up = "Зарегистрироваться";
        enter_email = "Введите свою эл. почту: ";
        enter_phone = "Введите свой тел. номер: ";
        enter_age = "Введите свой возраст: ";
        select_gender = "Выберите свой пол: ";
        about_me = "Обо мне";
        my_basket = "Моя корзинка";
        products = "Товары";
        prev = "Назад";
        main_header = "Вы успешно вошли в Telegram-бот \"Savdozon\"!";
        input_error = "Ошибка входных данных, попробуйте еще раз!";
    }
}
