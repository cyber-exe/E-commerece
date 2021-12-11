package bot;

import lombok.Data;

@Data
class ContentEng extends ManageLang{
    ContentEng(){
        start_header = "Chosen English language!";
        sign_in = "Sign in";
        sign_up = "Sign up";
    }
}
