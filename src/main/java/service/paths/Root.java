package service.paths;

public  interface Root {
    //Behzod
    //String basePath = "C:\\Users\\delta\\Documents\\E-commerce\\src\\main\\resources\\";
    //Khusan
    String basePath = "C:\\Users\\User\\IdeaProjects\\E-commerece\\src\\main\\resources\\";
    //Alisher
    //String basePath = "D:\\PDP-Unicorn\\JavaOOP\\ProjectsFromOthers\\E-commerece";


    String buyersPath = basePath + "buyers.json";
    String cardsPath = basePath + "cards.json";
    String sellersPath = basePath + "sellers.json";
    String basketsPath = basePath + "baskets.json";
    String historiesPath = basePath + "histories.json";
    String adminsPath = basePath + "admin.json";
}
