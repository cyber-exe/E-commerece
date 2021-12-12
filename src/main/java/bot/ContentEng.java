package bot;

import lombok.Data;

@Data
class ContentEng extends ManageLang{
    ContentEng(){
        selected_lang = "Chosen English language!";
        main_header = "Login to the system";
        sign_in = "Sign in";
        sign_up = "Sign up";
        enter_email = "Enter own email";
    }
}
