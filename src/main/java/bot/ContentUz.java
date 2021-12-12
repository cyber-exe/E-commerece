package bot;

import lombok.Data;


@Data
class ContentUz extends ManageLang {
    ContentUz(){
        selected_lang = "O'zbek tili tanlandi!";
        main_header = "Tizimga kirish";
        sign_in = "Kirish";
        sign_up = "Ro'yhatdan o'tish";
        enter_email = "Elektron pochtangizni kiriting";
    }
}


