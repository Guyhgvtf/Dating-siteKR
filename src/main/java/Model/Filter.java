package Model;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Filter {
    private final String URL = "jdbc:mysql://localhost:3306/dating_site_db";
    private final String USERNAME = "root";
    private final String PASSWORD = "qwerty";

    public List<Profile> getFilteredProfiles(String userToken) {
        List<Profile> profiles = new ArrayList<>();
        int age = 0;
        String city = null;
        String lookingFor = null;
        String yourGender = null;

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);

            String query = "SELECT age, city, lookingFor, your_gender FROM questionnaire WHERE token = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, userToken);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                age = resultSet.getInt("age");
                city = resultSet.getString("city");
                lookingFor = resultSet.getString("lookingFor");
                yourGender = resultSet.getString("your_gender");
            }

            String matchQuery = "SELECT * FROM questionnaire WHERE your_gender = ? AND city = ? AND age BETWEEN ? AND ? AND token != ?";
            PreparedStatement matchPreparedStatement = connection.prepareStatement(matchQuery);
            matchPreparedStatement.setString(1, lookingFor);
            matchPreparedStatement.setString(2, city);
            matchPreparedStatement.setInt(3, age - 2);
            matchPreparedStatement.setInt(4, age + 2);
            matchPreparedStatement.setString(5, userToken);
            ResultSet matchResultSet = matchPreparedStatement.executeQuery();

            while (matchResultSet.next()) {
                Profile profile = new Profile();
                profile.setName(matchResultSet.getString("name"));
                profile.setAge(matchResultSet.getInt("age"));
                profile.setCity(matchResultSet.getString("city"));
                profile.setLookingFor(matchResultSet.getString("lookingFor"));
                profile.setDescription(matchResultSet.getString("description"));
                profile.setYourGender(matchResultSet.getString("your_gender"));
                profile.setMedia(matchResultSet.getBlob("media"));
                profile.setToken(matchResultSet.getString("token"));  // Set the token
                profiles.add(profile);
            }

            matchResultSet.close();
            matchPreparedStatement.close();
            resultSet.close();
            preparedStatement.close();
            connection.close();

        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        return profiles;
    }
}
