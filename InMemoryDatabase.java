import java.io.*;
import java.util.*;

public class InMemoryDatabase {
    private Map<String, UserProfile> userProfiles;
    private static final String DATA_FILE = "data.ser";

    public InMemoryDatabase() {
        this.userProfiles = new HashMap<>();
        loadFromFile();
    }

    //Create new user profiles
    public void createUserProfile(UserProfile userProfile) {
        if (!userProfiles.containsKey(userProfile.getUserId())){
            userProfiles.put(userProfile.getUserId(), userProfile);
        } else {
            throw new IllegalArgumentException("User ID already exists");
        }
    }

    //Update an existing user profile
    public void updateUserProfile(String userId, UserProfile updatedProfile) {
        if (userProfiles.containsKey(userId)){
            userProfiles.put(userId, updatedProfile);
        } else {
            throw new IllegalArgumentException("User ID does not exist");
        }
    }

    //Delete a user profile by user
    public void deleteUserProfile(String userId) {
        if (userProfiles.containsKey(userId)){
            userProfiles.remove(userId);
        } else {
            throw new IllegalArgumentException("User ID does not exist");
        }
    }

    //List all user profiles
    public List<UserProfile> getAllUserProfiles() {
        return new ArrayList<>(userProfiles.values());
    }

    public void saveToFile(){
        try(ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(DATA_FILE))){
            oos.writeObject(userProfiles);
            System.out.println("Data saved to " + DATA_FILE);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("unchecked")
    public void loadFromFile(){
        try(ObjectInputStream ois = new ObjectInputStream(new FileInputStream(DATA_FILE))){
            userProfiles = (Map<String, UserProfile>) ois.readObject();
            System.out.println("Data file loaded from " + DATA_FILE);
        } catch (FileNotFoundException e){
            System.out.println("No data file found");
        } catch (IOException | ClassNotFoundException e){
            e.printStackTrace();
        }
    }
}
