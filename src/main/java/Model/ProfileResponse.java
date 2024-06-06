package Model;

 public class ProfileResponse {
    private String name;
    private int age;
    private String city;
    private String description;
    private String base64Image;
    private String token;

    public ProfileResponse(String name, int age, String city, String description, String base64Image, String token) {
        this.name = name;
        this.age = age;
        this.city = city;
        this.description = description;
        this.base64Image = base64Image;
        this.token = token;
    }
}