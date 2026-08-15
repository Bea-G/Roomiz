package com.example.roomiz;

import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class ProfileRepository {
    public interface ProfilesCallback {
        void onProfilesLoaded(List<Profile> profiles);
        void onError(Exception exception);
    }

    // Read the profiles.
    public static void getProfilesFromFirestore(ProfilesCallback callback) {
        FirebaseFirestore.getInstance().collection("profiles").get()
                .addOnSuccessListener(snapshots -> {  // Return the loaded profiles.
                    List<Profile> profiles = new ArrayList<>();
                    for (QueryDocumentSnapshot document : snapshots) {
                        String name = document.getString("name");
                        String city = document.getString("city");
                        String about = document.getString("about");
                        String imageName = document.getString("imageName");
                        Long ageValue = document.getLong("age");
                        Long matchValue = document.getLong("matchPercentage");
                        List<String> interests = (List<String>) document.get("interests");
                        String tagOne = interests != null && interests.size() > 0 ? interests.get(0) : "";
                        String tagTwo = interests != null && interests.size() > 1 ? interests.get(1) : "";
                        profiles.add(new Profile(document.getId(), imageName, name == null ? "" : name,
                                ageValue == null ? 0 : ageValue.intValue(), city == null ? "" : city,
                                matchValue == null ? 0 : matchValue.intValue(), about == null ? "" : about,
                                tagOne, tagTwo));
                    }
                    callback.onProfilesLoaded(profiles);
                })
                .addOnFailureListener(callback::onError);
    }
}