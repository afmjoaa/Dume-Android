package io.dume.dume.teacher.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class LocalDb {


    private List<String> categories, innerEducation, innerSchool, innerSchoolBanglaMedium, innerSchoolEnglishMedium,
            innerSchoolBanglaMediumClassOne, innerSchoolBanglaMediumClassTwo, innerSchoolBanglaMediumClassThree,
            innerSchoolBanglaMediumClassFour, innerSchoolBanglaMediumClassFive, innerSchoolBanglaMediumClassSix,
            innerSchoolBanglaMediumClassSeven, innerSchoolEnglishMediumClassOne, innerSchoolEnglishMediumClassTwo,
            innerSchoolEnglishMediumClassThree, innerSchoolEnglishMediumClassFour, innerSchoolEnglishMediumClassFive,
            innerSchoolEnglishMediumClassSix, innerSchoolEnglishMediumClassSeven = null;

    public List<String> getCategories() {
        return categories;
    }

    public List<String> getInnerEducation() {
        return innerEducation;
    }

    public List<String> getInnerSchool() {
        return innerSchool;
    }

    public List<String> getInnerSchoolBanglaMedium() {
        return innerSchoolBanglaMedium;
    }

    public List<String> getInnerSchoolEnglishMedium() {
        return innerSchoolEnglishMedium;
    }

    public List<String> getInnerSchoolBanglaMediumClassOne() {
        return innerSchoolBanglaMediumClassOne;
    }

    public List<String> getInnerSchoolBanglaMediumClassTwo() {
        return innerSchoolBanglaMediumClassTwo;
    }

    public List<String> getInnerSchoolBanglaMediumClassThree() {
        return innerSchoolBanglaMediumClassThree;
    }

    public List<String> getInnerSchoolBanglaMediumClassFour() {
        return innerSchoolBanglaMediumClassFour;
    }

    public List<String> getInnerSchoolBanglaMediumClassFive() {
        return innerSchoolBanglaMediumClassFive;
    }

    public List<String> getInnerSchoolBanglaMediumClassSix() {
        return innerSchoolBanglaMediumClassSix;
    }

    public List<String> getInnerSchoolBanglaMediumClassSeven() {
        return innerSchoolBanglaMediumClassSeven;
    }

    public List<String> getInnerSchoolEnglishMediumClassOne() {
        return innerSchoolEnglishMediumClassOne;
    }

    public List<String> getInnerSchoolEnglishMediumClassTwo() {
        return innerSchoolEnglishMediumClassTwo;
    }

    public List<String> getInnerSchoolEnglishMediumClassThree() {
        return innerSchoolEnglishMediumClassThree;
    }

    public List<String> getInnerSchoolEnglishMediumClassFour() {
        return innerSchoolEnglishMediumClassFour;
    }

    public List<String> getInnerSchoolEnglishMediumClassFive() {
        return innerSchoolEnglishMediumClassFive;
    }

    public List<String> getInnerSchoolEnglishMediumClassSix() {
        return innerSchoolEnglishMediumClassSix;
    }

    public List<String> getInnerSchoolEnglishMediumClassSeven() {
        return innerSchoolEnglishMediumClassSeven;
    }

    public LocalDb() {
        categories = new ArrayList<>(Arrays.asList("Education", "Software", "Programming", "Language", "Dance", "Art", "Cooking", "Music", "Sewing"));
        innerEducation = new ArrayList<>(Arrays.asList("School", "College", "Admission Test", "University"));
        innerSchool = new ArrayList<>(Arrays.asList("Bangla Medium", "English Medium"));
        innerSchoolBanglaMedium = new ArrayList<>(Arrays.asList("Class One", "Class Two", "Class Three", "Class Four", "Class Five", "Class Six", "Class Seven", "JSC", "SSC"));
        innerSchoolEnglishMedium = new ArrayList<>(Arrays.asList("Class One", "Class Two", "Class Three", "Class Four", "Class Five", "Class Six", "Class Seven", "O Level"));
        innerSchoolBanglaMediumClassOne = new ArrayList<>(Arrays.asList("Bangla", "English", "Math"));
        innerSchoolBanglaMediumClassTwo = new ArrayList<>(Arrays.asList("Bangla", "English", "Math"));
        innerSchoolBanglaMediumClassThree = new ArrayList<>(Arrays.asList("Bangla", "English", "Math", "Religion", "General Science", "Social Science"));
        innerSchoolBanglaMediumClassFour = new ArrayList<>(Arrays.asList("Bangla", "English", "Math", "Religion", "General Science", "Social Science"));
        innerSchoolBanglaMediumClassFive = new ArrayList<>(Arrays.asList("Bangla", "English", "Math", "Religion", "General Science", "Social Science"));
        innerSchoolBanglaMediumClassSix = new ArrayList<>(Arrays.asList("Bangla", "English", "Math", "Religion", "General Science", "Social Science", "Agriculture", "Domestic Science", "ICT"));
        innerSchoolBanglaMediumClassSeven = new ArrayList<>(Arrays.asList("Bangla", "English", "Math", "Religion", "General Science", "Social Science", "Agriculture", "Domestic Science", "ICT"));
        innerSchoolEnglishMediumClassOne = new ArrayList<>(Arrays.asList("English", "Bangla", "Math", "Science", "Religion"));
        innerSchoolEnglishMediumClassTwo = new ArrayList<>(Arrays.asList());
        innerSchoolEnglishMediumClassThree = new ArrayList<>(Arrays.asList());
        innerSchoolEnglishMediumClassFour = new ArrayList<>(Arrays.asList());
        innerSchoolEnglishMediumClassFive = new ArrayList<>(Arrays.asList());
        innerSchoolEnglishMediumClassSix = new ArrayList<>(Arrays.asList());
        innerSchoolEnglishMediumClassSeven = new ArrayList<>(Arrays.asList());
    }


    List<String> getInnerOne(String outer) {
        List<String> list = null;
        switch (outer) {
            case "Education":

                break;
            case "Software":
                break;
            case "Programming":
                break;
            case "Language":
                break;
            case "Dance":
                break;
            case "Art":
                break;
            case "Sewing":
                break;
            case "Music":
                break;
            case "Cooking":
                break;
            default:


        }
        return null;
    }

}
