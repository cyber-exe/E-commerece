package bot;

import lombok.Data;

@Data
class ContentEng extends ManageLang{

    ContentEng(){
        start_header = "Hello user you have chosen English language!";
        sign_in = "Start register";
    }
}
