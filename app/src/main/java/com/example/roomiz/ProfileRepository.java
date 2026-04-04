package com.example.roomiz;

import java.util.ArrayList;
import java.util.List;

public class ProfileRepository {
    // Repository class to provide a list of profiles (class Profile)
    // NOTE: will be replaced with an actual database in the future

    public static List<Profile> getProfiles() {  // Get a list of profiles
        List<Profile> profiles = new ArrayList<>();  // Initialize the list

        // Create Profile objects and add them to the profiles list
        profiles.add(new Profile(
                R.drawable.mika_dan,
                "Mika Dan",
                24,
                "Haifa",
                89,
                "I love photography, long walks by the beach, and trying new coffee places.",
                "\uD83D\uDCF8 Photography",
                "\u2615 Coffee"
        ));

        profiles.add(new Profile(
                R.drawable.gaya_refael,
                "Gaya Refael",
                26,
                "Jerusalem",
                91,
                "I enjoy art museums, weekend trips, and cooking for friends and family.",
                "\uD83C\uDFA8 Art",
                "\uD83D\uDE97 Travel"
        ));

        profiles.add(new Profile(
                R.drawable.dana_levy,
                "Dana Levy",
                25,
                "Tel Aviv",
                93,
                "I’m a student of communications, and in my free time, I enjoy cooking, traveling, and hanging out with friends.",
                "\uD83C\uDFB5 Pop, Hip-hop",
                "\uD83C\uDFE0 Apartment"
        ));

        return profiles;
    }
}
