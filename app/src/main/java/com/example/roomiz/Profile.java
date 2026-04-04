package com.example.roomiz;

public class Profile {
    // Profile class to represent a profile object with all its properties

    private final int imageResId;  // id of the profile image
    private final String name;  // name of the profile
    private final int age;  // age of the profile
    private final String city;  // city of the profile
    private final int matchPercentage;  // match percentage of the profile
    private final String about;  // about of the profile
    private final String tagOne;  // tag one of the profile
    private final String tagTwo;  // tag two of the profile


    // Constructor
    public Profile(int imageResId, String name, int age, String city,
                   int matchPercentage, String about, String tagOne, String tagTwo) {
        this.imageResId = imageResId;
        this.name = name;
        this.age = age;
        this.city = city;
        this.matchPercentage = matchPercentage;
        this.about = about;
        this.tagOne = tagOne;
        this.tagTwo = tagTwo;
    }

    // Getters for the profile properties
    public int getImageResId() {
        return imageResId;
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

    public int getMatchPercentage() {
        return matchPercentage;
    }

    public String getAbout() {
        return about;
    }

    public String getTagOne() {
        return tagOne;
    }

    public String getTagTwo() {
        return tagTwo;
    }
}
