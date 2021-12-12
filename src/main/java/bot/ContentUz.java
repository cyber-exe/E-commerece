package bot;

import lombok.Data;


@Data
class ContentUz extends ManageLang {
    ContentUz(){
        selected_lang = "O'zbek tili tanlandi!";
        enter_to_system = "Tizimga kirish";
        sign_in = "Kirish";
        sign_up = "Ro'yhatdan o'tish";
        enter_email = "Elektron pochtangizni kiriting: ";
        enter_phone = "Tel. raqamingizni kiriting: ";
        enter_age = "Yoshingizni kiriting: ";
        about_me = "Men haqimda";
        my_basket = "Mening savatcham";
        products = "Mahsulotlar";
        prev = "Orqaga";
        main_header = "\"Savdozon\" telegram botiga muvaffaqiyatli kirdingiz!";
        input_error = "Kirilgan malumotlar xato, qayta urinib ko'ring!";
    }
}


