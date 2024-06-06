package Model;



import java.sql.Blob;

public class Profile {


    private String token;
    private String name;
    private int age;
    private String city;
    private String lookingFor;
    private String description;
    private String yourGender;
    private Blob media;

    public void setName(String name) {
        this.name = name;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setLookingFor(String lookingFor) {
        this.lookingFor = lookingFor;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setYourGender(String yourGender) {
        this.yourGender = yourGender;
    }

    public void setMedia(Blob media) {
        this.media = media;
    }



    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }

    public String getCity() {
        return city;
    }

    public String getLookingFor() {
        return lookingFor;
    }

    public String getDescription() {
        return description;
    }

    public String getYourGender() {
        return yourGender;
    }

    public Blob getMedia() {
        return media;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }


}
