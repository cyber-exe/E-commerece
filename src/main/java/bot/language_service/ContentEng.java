package bot.language_service;

import lombok.Data;

@Data
public
class ContentEng extends ManageLang{
    public ContentEng(){
        selected_lang = "Chosen English language!";
        enter_to_system = "Login to the system";
        sign_in = "Sign in";
        sign_up = "Sign up";
        enter_email = "Enter own email: ";
        enter_phone = "Share your phone: ";
        enter_age = "Enter own age: ";
        about_me = "About me";
        my_basket = "My basket";
        products = "Products";
        prev = "Back";
        main_header = "You have successfully entered the Savdozon's telegram bot!";
        input_error = "Input error, try again!";
    }
}
