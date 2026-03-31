package com.example.roomiz;

public class Profile {

    private final int imageResId;
    private final String name;
    private final int age;
    private final String city;
    private final int matchPercentage;
    private final String about;
    private final String tagOne;
    private final String tagTwo;

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
