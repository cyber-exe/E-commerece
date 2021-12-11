package bot;

abstract class ManageLang {

     static ManageLang getContent(String lang){
        if(lang.equals("Uzbek")){
            ContentTemp temp = (ContentTemp) new ContentUz();
        }else if (lang.equals("Russian")){
            return new ContentRu();

        }else return new ContentEng();


    }
}
