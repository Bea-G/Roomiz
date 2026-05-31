package com.example.roomiz;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class ProfileRepository {

    public interface ProfilesCallback {
        void onProfilesLoaded(List<Profile> profiles);
        void onError(Exception e);
    }

    public static void getProfilesFromFirestore(ProfilesCallback callback) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("profiles")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    List<Profile> profiles = new ArrayList<>();

                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        String name = document.getString("name");
                        String city = document.getString("city");
                        String about = document.getString("about");
                        String imageName = document.getString("imageName");

                        Long ageLong = document.getLong("age");
                        Long matchLong = document.getLong("matchPercentage");

                        int age = ageLong != null ? ageLong.intValue() : 0;
                        int matchPercentage = matchLong != null ? matchLong.intValue() : 0;

                        List<String> interests = (List<String>) document.get("interests");
                        String tagOne = interests != null && interests.size() > 0 ? interests.get(0) : "";
                        String tagTwo = interests != null && interests.size() > 1 ? interests.get(1) : "";

                        int imageResId = getImageResourceId(imageName);

                        profiles.add(new Profile(
                                imageResId,
                                name,
                                age,
                                city,
                                matchPercentage,
                                about,
                                tagOne,
                                tagTwo
                        ));
                    }

                    callback.onProfilesLoaded(profiles);
                })
                .addOnFailureListener(callback::onError);
    }

    private static int getImageResourceId(String imageName) {
        if ("dana_levy".equals(imageName)) {
            return R.drawable.dana_levy;
        } else if ("mika_dan".equals(imageName)) {
            return R.drawable.mika_dan;
        } else if ("gaya_refael".equals(imageName)) {
            return R.drawable.gaya_refael;
        } else {
            return R.drawable.dana_levy;
        }
    }
}