import java.util.*;

public class InMemoryDatabase {
    private final Map<String, UserProfile> userProfiles;
    private final Map<String, String> emailIndex;
    private final TreeMap<Integer, String> ageIndex;

    public InMemoryDatabase() {
        this.userProfiles = new HashMap<>();
        this.emailIndex = new HashMap<>();
        this.ageIndex = new TreeMap<>();
    }

    //Create new user profiles
    public void createUserProfile(UserProfile userProfile) {
        if (!userProfiles.containsKey(userProfile.getUserId())){
            userProfiles.put(userProfile.getUserId(), userProfile);
            emailIndex.put(userProfile.getEmail(), userProfile.getUserId());
            ageIndex.put(userProfile.getAge(), userProfile.getUserId());
        } else {
            throw new IllegalArgumentException("User ID already exists");
        }
    }

    //Update an existing user profile
    public void updateUserProfile(String userId, UserProfile updatedProfile) {
        if (userProfiles.containsKey(userId)){
            String oldEmail = userProfiles.get(userId).getEmail();
            if (!oldEmail.equals(updatedProfile.getEmail())){
                emailIndex.remove(oldEmail);
            }
            userProfiles.put(userId, updatedProfile);
            emailIndex.put(updatedProfile.getEmail(), userId);
        } else {
            throw new IllegalArgumentException("User ID does not exist");
        }
    }

    //Delete a user profile by user
    public void deleteUserProfile(String userId) {
        if (userProfiles.containsKey(userId)){
            String email = userProfiles.get(userId).getEmail();
            userProfiles.remove(userId);
            emailIndex.remove(email);
        } else {
            throw new IllegalArgumentException("User ID does not exist");
        }
    }

    //List all user profiles
    public List<UserProfile> getAllUserProfiles() {
        return new ArrayList<>(userProfiles.values());
    }

}
