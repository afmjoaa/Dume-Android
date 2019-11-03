package io.dume.dume.teacher.model;

import androidx.annotation.Keep;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Keep
public class LocalDb {
    private List<String> categories, innerLevel, innerSchool, innerSchoolBanglaMedium, innerSchoolEnglishMedium,
            innerSchoolBanglaMediumClassOne, innerSchoolBanglaMediumClassTwo, innerSchoolBanglaMediumClassThree,
            innerSchoolBanglaMediumClassFour, innerSchoolBanglaMediumClassFive, innerSchoolBanglaMediumClassSix,
            innerSchoolBanglaMediumClassSeven, innerSchoolEnglishMediumClassOne, innerSchoolEnglishMediumClassTwo,
            innerSchoolEnglishMediumClassThree, innerSchoolEnglishMediumClassFour, innerSchoolEnglishMediumClassFive,
            innerSchoolEnglishMediumClassSix, innerSchoolEnglishMediumClassSeven, innerSchoolEnglishMediumClassEight, innerSchoolEnglishMediumEdExcelOLevel,
            innerSchoolEnglishMediumCambridgeOLevel, innerCollege, innerCollegeEnglishMedium,
            innerCollegeEnglishMediumEdexelAs_A2,
            innerCollegeEnglishMediumCambridgeAs_A2, innerCollegeEnglishMediumIbAs_A2, innerCollegeBanglaMedium,
            innerCollegeBanglaMediumScience, innerCollegeBanglaMediumCommerce, innerCollegeBanglaMediumArts,
            innerCollegeEnglishVersion, innerCollegeEnglishVersionScience, innerCollegeEnglishVersionCommerce,
            innerCollegeEnglishVersionArts, innerSchoolBanglaMediumClassEight, innerSchoolBanglaMediumClassSSC,
            innerSchoolBanglaMediumClassSSCScience, innerSchoolBanglaMediumClassSSCCommerce, innerSchoolBanglaMediumClassSSCArts,
            innerProgramming, innerSoftWare, innerLanguage, innerDance, innerMusic, innerCooking, innerMusicInstrumental,
            innerMusicGuitar, innerUniversity, innerUniversityEngineering, innerUniversityMedical, innerUniversityOthers,
            innerAdmissionTest, innerOthers;
    private final Map<String, List<String>> levelOneMap;
    private final HashMap<String, List<String>> educaitonMap;
    private final Map<String, List<String>> musicMap;
    private final Map<String, List<String>> schoolMap;
    private final Map<String, List<String>> collegeMap;
    private final Map<String, List<String>> universityMap;
    private final Map<String, List<String>> instrumentalMap;
    private final Map<String, List<String>> schoolBanglaMediumMap;
    private final Map<String, List<String>> schoolEnglishMediumMap;
    private final Map<String, List<String>> schoolEnglishVersionMap;
    private final Map<String, List<String>> collegeBanglaMediumMap;
    private final Map<String, List<String>> collegeEnglishMediumMap;
    private final Map<String, List<String>> collegeEnglishVersionMap;
    //private final List<String> uniTestSub;
    private final Map<String, List<String>> universityEngineerMap = null;
    private final Map<String, List<String>> universityMedicalMap = null;
    private final Map<String, List<String>> universityHonsMap = null;
    private final List<String> genderPreferencesList;
    public final List<String> crossCheck;
    public final List<String> payment;
    public final List<String> capacity;
    private final ArrayList<String> innerArt;
    private final ArrayList<String> innerMusicSinging;


    public List<String> getInnerSchoolEnglishMediumEdExcelOLevel() {
        return innerSchoolEnglishMediumEdExcelOLevel;
    }

    public List<String> getInnerSchoolEnglishMediumCambridgeOLevel() {
        return innerSchoolEnglishMediumCambridgeOLevel;
    }

    public List<String> getInnerCollege() {
        return innerCollege;
    }

    public List<String> getInnerCollegeEnglishMedium() {
        return innerCollegeEnglishMedium;
    }


    public List<String> getInnerCollegeEnglishMediumEdexelAs_A2() {
        return innerCollegeEnglishMediumEdexelAs_A2;
    }

    public List<String> getInnerCollegeEnglishMediumCambridgeAs_A2() {
        return innerCollegeEnglishMediumCambridgeAs_A2;
    }

    public List<String> getInnerCollegeEnglishMediumIbAs_A2() {
        return innerCollegeEnglishMediumIbAs_A2;
    }

    public List<String> getInnerCollegeBanglaMedium() {
        return innerCollegeBanglaMedium;
    }

    public List<String> getInnerCollegeBanglaMediumScience() {
        return innerCollegeBanglaMediumScience;
    }

    public List<String> getInnerCollegeBanglaMediumCommerce() {
        return innerCollegeBanglaMediumCommerce;
    }

    public List<String> getInnerCollegeBanglaMediumArts() {
        return innerCollegeBanglaMediumArts;
    }

    public List<String> getInnerCollegeEnglishVersion() {
        return innerCollegeEnglishVersion;
    }

    public List<String> getInnerCollegeEnglishVersionScience() {
        return innerCollegeEnglishVersionScience;
    }

    public List<String> getInnerCollegeEnglishVersionCommerce() {
        return innerCollegeEnglishVersionCommerce;
    }

    public List<String> getInnerCollegeEnglishVersionArts() {
        return innerCollegeEnglishVersionArts;
    }

    public List<String> getInnerSchoolBanglaMediumClassEight() {
        return innerSchoolBanglaMediumClassEight;
    }

    public List<String> getInnerSchoolBanglaMediumClassSSC() {
        return innerSchoolBanglaMediumClassSSC;
    }

    public List<String> getInnerSchoolBanglaMediumClassSSCScience() {
        return innerSchoolBanglaMediumClassSSCScience;
    }

    public List<String> getInnerSchoolBanglaMediumClassSSCCommerce() {
        return innerSchoolBanglaMediumClassSSCCommerce;
    }

    public List<String> getInnerSchoolBanglaMediumClassSSCArts() {
        return innerSchoolBanglaMediumClassSSCArts;
    }

    public List<String> getInnerProgramming() {
        return innerProgramming;
    }

    public List<String> getInnerSoftWare() {
        return innerSoftWare;
    }

    public List<String> getInnerLanguage() {
        return innerLanguage;
    }

    public List<String> getInnerDance() {
        return innerDance;
    }

    public List<String> getInnerMusic() {
        return innerMusic;
    }

    public List<String> getInnerCooking() {
        return innerCooking;
    }

    public List<String> getInnerMusicInstrumental() {
        return innerMusicInstrumental;
    }

    public List<String> getInnerMusicGuitar() {
        return innerMusicGuitar;
    }

    public List<String> getInnerUniversity() {
        return innerUniversity;
    }

    public List<String> getInnerUniversityEngineering() {
        return innerUniversityEngineering;
    }

    public List<String> getInnerUniversityMedical() {
        return innerUniversityMedical;
    }

    public List<String> getInnerUniversityOthers() {
        return innerUniversityOthers;
    }

    public List<String> getInnerAdmissionTest() {
        return innerAdmissionTest;
    }

    public List<String> getInnerOthers() {
        return innerOthers;
    }

    public List<String> getCategories() {
        return categories;
    }

    public List<String> getInnerLevel() {
        return innerLevel;
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

    public List<String> getInnerSchoolEnglishMediumClassEight() {
        return innerSchoolEnglishMediumClassEight;
    }


    public List<String> getGenderPreferencesList() {
        return genderPreferencesList;
    }

    public LocalDb() {
        categories = new ArrayList<String>(Arrays.asList("Education", "Software", "Programming", "Language", "Dance", "Art", "Cooking", "Music", "Others")) {
            @Override
            public String toString() {
                return "Categories";
            }

        };

        innerLevel = new ArrayList<String>(Arrays.asList("School", "College", "University")) {
            @Override
            public String toString() {
                return "Level";
            }
        };
        innerSchool = new ArrayList<String>(Arrays.asList("Bangla Medium", "English Medium", "English Version")) {
            @Override
            public String toString() {
                return "Medium";
            }
        };
        innerSchoolBanglaMedium = new ArrayList<String>(Arrays.asList("Class One", "Class Two", "Class Three",
                "Class Four", "Class Five", "Class Six", "Class Seven", "JSC", "Science SSC", "Commerce SSC", "Arts SSC")) {
            @Override
            public String toString() {
                return "Class";
            }
        };
        innerSchoolEnglishMedium = new ArrayList<String>(Arrays.asList("Class One", "Class Two", "Class Three", "Class Four", "Class Five", "Class Six", "Class Seven", "Class Eight", "O Level Edexcel", "O Level Cambridge")) {
            @Override
            public String toString() {
                return "Class";
            }
        };
        innerSchoolBanglaMediumClassOne = new ArrayList<String>(Arrays.asList("Bangla", "English", "Math")) {
            @Override
            public String toString() {
                return "Subject";
            }
        };
        innerSchoolBanglaMediumClassTwo = new ArrayList<String>(Arrays.asList("Bangla", "English", "Math")) {
            @Override
            public String toString() {
                return "Subject";
            }
        };
        innerSchoolBanglaMediumClassThree = new ArrayList<String>(Arrays.asList("Bangla", "English", "Math", "Religion", "General Science", "Social Science")) {
            @Override
            public String toString() {
                return "Subject";
            }
        };
        innerSchoolBanglaMediumClassFour = new ArrayList<String>(Arrays.asList("Bangla", "English", "Math", "Religion", "General Science", "Social Science")) {
            @Override
            public String toString() {
                return "Subject";
            }
        };
        innerSchoolBanglaMediumClassFive = new ArrayList<String>(Arrays.asList("Bangla", "English", "Math", "Religion", "General Science", "Social Science")) {
            @Override
            public String toString() {
                return "Subject";
            }
        };
        innerSchoolBanglaMediumClassSix = new ArrayList<String>(Arrays.asList("Bangla", "English", "Math", "Religion", "General Science", "Social Science", "Agriculture", "Domestic Science", "ICT")) {
            @Override
            public String toString() {
                return "Subject";
            }
        };
        innerSchoolBanglaMediumClassSeven = new ArrayList<String>(Arrays.asList("Bangla", "English", "Math", "Religion", "General Science", "Social Science", "Agriculture", "Domestic Science", "ICT")) {
            @Override
            public String toString() {
                return "Subject";
            }
        };
        innerSchoolBanglaMediumClassEight = new ArrayList<String>(Arrays.asList("English", "Bangla", "Math", "Science", "ICT")) {
            @Override
            public String toString() {
                return "Subject";
            }
        };
        innerSchoolBanglaMediumClassSSC = new ArrayList<String>(Arrays.asList("Science", "Commerce", "Arts")) {
            @Override
            public String toString() {
                return "Field";
            }
        };
        innerSchoolBanglaMediumClassSSCScience = new ArrayList<String>(Arrays.asList("English", "Bangla", "Physics", "Chemistry", "Biology", "Math", "Higher Math", "Statistics", "ICT", "Bangladesh and Global Studies", "Religion")) {
            @Override
            public String toString() {
                return "Subject";
            }
        };
        innerSchoolBanglaMediumClassSSCCommerce = new ArrayList<String>(Arrays.asList("English", "Bangla", "ICT", "Economics", "Accounting", "Finance & Banking", "Business Entrepreneurship", "Science", "Religion")) {
            @Override
            public String toString() {
                return "Subject";
            }
        };
        innerSchoolBanglaMediumClassSSCArts = new ArrayList<String>(Arrays.asList("English", "Bangla", "ICT", "Religion", "Geography and Environment", "History of Bangladesh and World Civilization", "Art and Crafts", "Science")) {
            @Override
            public String toString() {
                return "Subject";
            }
        };
        innerSchoolEnglishMediumClassOne = new ArrayList<String>(Arrays.asList("English", "Bangla", "Math", "Geography", "History", "Science", "Religion")) {
            @Override
            public String toString() {
                return "Subject";
            }
        };
        innerSchoolEnglishMediumClassTwo = new ArrayList<String>(Arrays.asList("English", "Bangla", "Math", "Geography", "History", "Science", "Religion")) {
            @Override
            public String toString() {
                return "Subject";
            }
        };
        innerSchoolEnglishMediumClassThree = new ArrayList<String>(Arrays.asList("English", "Bangla", "Math", "Geography", "History", "Science", "Religion")) {
            @Override
            public String toString() {
                return "Subject";
            }
        };
        innerSchoolEnglishMediumClassFour = new ArrayList<String>(Arrays.asList("English", "Bangla", "Math", "Geography", "History", "Science", "Religion")) {
            @Override
            public String toString() {
                return "Subject";
            }
        };
        innerSchoolEnglishMediumClassFive = new ArrayList<String>(Arrays.asList("English", "Bangla", "Math", "Geography", "History", "Science", "Religion")) {
            @Override
            public String toString() {
                return "Subject";
            }
        };
        innerSchoolEnglishMediumClassSix = new ArrayList<String>(Arrays.asList("English", "Bangla", "Math", "Geography", "History", "Science", "Religion")) {
            @Override
            public String toString() {
                return "Subject";
            }
        };
        innerSchoolEnglishMediumClassSeven = new ArrayList<String>(Arrays.asList("English", "Bangla", "Math", "Geography", "History", "Science", "Religion")) {
            @Override
            public String toString() {
                return "Subject";
            }
        };
        innerSchoolEnglishMediumClassEight = new ArrayList<String>(Arrays.asList("English", "Bangla", "Math", "Geography", "History", "Science", "Religion")) {
            @Override
            public String toString() {
                return "Subject";
            }
        };

        innerSchoolEnglishMediumEdExcelOLevel = new ArrayList<String>(Arrays.asList("Physics", "Chemistry", "Biology", "Math-B", "Pure Math", "English", "Bangla", "Accounting", "Economics", "Business")) {
            @Override
            public String toString() {
                return "Subject";
            }
        };
        innerSchoolEnglishMediumCambridgeOLevel = new ArrayList<String>(Arrays.asList("Physics", "Chemistry", "Biology", "Math-B", "Pure Math", "English", "Bangla", "Accounting", "Economics", "Business")) {
            @Override
            public String toString() {
                return "Subject";
            }
        };
        innerCollege = new ArrayList<String>(Arrays.asList("English Medium", "Bangla Medium", "English Version")) {
            @Override
            public String toString() {
                return "Medium";
            }
        };
        innerCollegeEnglishMedium = new ArrayList<String>(Arrays.asList("Edexcel As-A2", "Cambridge As-A2", "Ib As-A2")) {
            @Override
            public String toString() {
                return "Division";
            }
        };

        innerCollegeEnglishMediumEdexelAs_A2 = new ArrayList<String>(Arrays.asList("Physics", "Chemistry", "Biology", "Math", "Further Math", "Psychology", "Accounting", "Economics", "Business")) {
            @Override
            public String toString() {
                return "Subject";
            }
        };
        innerCollegeEnglishMediumCambridgeAs_A2 = new ArrayList<String>(Arrays.asList("Physics", "Chemistry", "Biology", "Math", "Further Math", "Psychology", "Accounting", "Economics", "Business")) {
            @Override
            public String toString() {
                return "Subject";
            }
        };
        innerCollegeEnglishMediumIbAs_A2 = new ArrayList<String>(Arrays.asList("Physics", "Chemistry", "Biology", "Math", "Further Math", "Psychology", "Accounting", "Economics", "Business")) {
            @Override
            public String toString() {
                return "Subject";
            }
        };
        innerCollegeBanglaMedium = new ArrayList<String>(Arrays.asList("Science", "Commerce", "Arts")) {
            @Override
            public String toString() {
                return "Division";
            }
        };
        innerCollegeBanglaMediumScience = new ArrayList<String>(Arrays.asList("English", "Physics", "Chemistry", "Biology", "Math", "Higher Math", "Statistics", "ICT")) {
            @Override
            public String toString() {
                return "Subject";
            }
        };
        innerCollegeBanglaMediumCommerce = new ArrayList<String>(Arrays.asList("English", "Bangla", "Accounting", "Business Organization and Management", "ICT", "Finance, Banking & Insurance", "Production Management & Marketing", "Statistics", "Economics", "Geography")) {
            @Override
            public String toString() {
                return "Subject";
            }
        };
        innerCollegeBanglaMediumArts = new ArrayList<String>(Arrays.asList("English", "Bangla", "ICT", "Parent Policy", "Economics", "Psychology", "Logic", "Social Science", "Agriculture", "Geography")) {
            @Override
            public String toString() {
                return "Subject";
            }
        };
        innerCollegeEnglishVersion = new ArrayList<String>(Arrays.asList("Science", "Commerce", "Arts")) {
            @Override
            public String toString() {
                return "Division";
            }
        };
        innerCollegeEnglishVersionScience = new ArrayList<String>(Arrays.asList("English", "Physics", "Chemistry", "Biology", "Math", "Higher Math", "Statistics", "ICT")) {
            @Override
            public String toString() {
                return "Subject";
            }
        };
        innerCollegeEnglishVersionCommerce = new ArrayList<String>(Arrays.asList("English", "Bangla", "Accounting", "Business Organization and Management", "ICT", "Finance, Banking & Insurance", "Production Management & Marketing", "Statistics", "Economics", "Geography")) {
            @Override
            public String toString() {
                return "Subject";
            }
        };
        innerCollegeEnglishVersionArts = new ArrayList<String>(Arrays.asList("English", "Bangla", "ICT", "Parent Policy", "Economics", "Psychology", "Logic", "Social Science", "Agriculture", "Geography")) {
            @Override
            public String toString() {
                return "Subject";
            }
        };

        innerProgramming = new ArrayList<String>(Arrays.asList("C", "Java", "C++", "C#", "JavaScript", "Python", "Web Development", "NodeJS", "Angular", "ReactJS", "VueJS", "Django", "Wordpress Development", "iOS Development", "Xamarin Cross Platform", "DotNet Framework", "Data Science", "Machine Learning", "Android Development", "WPF App Development")) {
            @Override
            public String toString() {
                return " Language ";
            }
        };

        innerSoftWare = new ArrayList<String>(Arrays.asList("Word", "Excel", "Powerpoint", "Adobe Illustrator", "Adobe PhotoShop", "Matlab", "Solid work", "AutoCad", "Adobe After Effect", "Adobe Premiere Pro", "Adobe XD", "Jetbrains IDEs")) {
            @Override
            public String toString() {
                return "Software";
            }
        };

        innerLanguage = new ArrayList<String>(Arrays.asList("English", "Bangla", "French", "Spanish", "Japanese", "Korean", "German", "Hindi", "Chinese")) {
            @Override
            public String toString() {
                return "Language";
            }
        };

        innerCooking = new ArrayList<String>(Arrays.asList("Bangla Cuisine", "Chinese Cuisine", "Baking", "Fast-Foods")) {
            @Override
            public String toString() {
                return "Field";
            }
        };

        innerDance = new ArrayList<String>(Arrays.asList("Classical", "Modern Dance", "Bollywood", "Contemporary")) {
            @Override
            public String toString() {
                return "Field";
            }
        };

        innerMusic = new ArrayList<String>(Arrays.asList("Singing Cource", "Instrumental")) {
            @Override
            public String toString() {
                return "Music";
            }
        };

        innerMusicInstrumental = new ArrayList<String>(Arrays.asList("Electric Guitar", "Acoustic Guitar", "Spanish Guitar", "hawaiian Guitar", "Tabla", "Violin", "Keyboard", "Piano", "Drums")) {
            @Override
            public String toString() {
                return "Item";
            }
        };
        innerMusicGuitar = new ArrayList<String>(Arrays.asList("Electric", "Acoustic", "Spanish", "hawaiian")) {
            @Override
            public String toString() {
                return "Flavour";
            }
        };

        innerMusicSinging = new ArrayList<String>(Arrays.asList("Pop Singing", "Rock Singing", "Country/Classical singing", "Blues/Jazz", "Hip Hop/Rap", "Adult Contemporary")) {
            @Override
            public String toString() {
                return "Flavour";
            }
        };

        //university
        innerUniversity = new ArrayList<String>(Arrays.asList("Engineering", "Medical", "Hons")) {
            @Override
            public String toString() {
                return "Degree";
            }
        };
        innerUniversityEngineering = new ArrayList<String>(Arrays.asList("BME", "EEE", "AE", "CSE", "CE", "ME", "IPE", "NSE", "NAME", "PME", "EWCE", "Architecture")) {
            @Override
            public String toString() {
                return "Course";
            }
        };
        innerUniversityMedical = new ArrayList<String>(Arrays.asList("Anatomy", "Biochemistry", "Physiology", "Community Medicine", "Forensic", "Microbiology", "Pathology", "Pharmacology", "Gynecology", "Surgery", "Psychiatry")) {
            @Override
            public String toString() {
                return "Course";
            }
        };
        innerUniversityOthers = new ArrayList<String>(Arrays.asList("Biotechnology", "Pharmacy", "Psychology", "BBA", "Economics", "Environmental Science", "Law(LLB)")) {
            @Override
            public String toString() {
                return "Course";
            }
        };
        //Admission Test
        innerAdmissionTest = new ArrayList<String>(Arrays.asList("Engineering", "Medical", "IBA", "Private University")) {
            @Override
            public String toString() {
                return "Type";
            }
        };
        innerOthers = new ArrayList<String>(Arrays.asList("BCS", "IELTS", "SAT", "GRE", "GMAT", "Arabic (Quran)", "Baby Sitting")) {
            @Override
            public String toString() {
                return "Item";
            }
        };

        /*uniTestSub = new ArrayList<String>(Arrays.asList("Foo 1", "Foo 2", "Foo 2", "Foo 2", "Foo 2", "Foo 2",
                "Foo 2", "Foo 2", "Foo 2", "Foo 2", "Foo 2", "Foo 2", "Foo 2", "Foo 2", "Foo 2")) {
            @Override
            public String toString() {
                return "Subject";
            }
        };*/

        //Extra Data For Cross Check

        genderPreferencesList = new ArrayList<String>(Arrays.asList("Male", "Female", "Any")) {
            @Override
            public String toString() {
                return "Gender";
            }
        };
        payment = new ArrayList<String>(Arrays.asList("1k", "2k", "3k")) {
            @Override
            public String toString() {
                return "Salary";
            }
        };
        crossCheck = new ArrayList<String>(Arrays.asList("1k", "2k", "3k")) {
            @Override
            public String toString() {
                return "Cross Check";
            }
        };
        capacity = new ArrayList<String>(Arrays.asList("10", "20", "30")) {
            @Override
            public String toString() {
                return "Capacity";
            }
        };
        innerArt = new ArrayList<String>(Arrays.asList("Therapeutic Art", "Digital Art", "Visual art", "Applied Art", "Performing Art", "Character Art")) {
            @Override
            public String toString() {
                return "Field";
            }
        };


        levelOneMap = new HashMap<>(categories.size());
        levelOneMap.put(categories.get(0), innerLevel);
        levelOneMap.put(categories.get(1), innerSoftWare);
        levelOneMap.put(categories.get(2), innerProgramming);
        levelOneMap.put(categories.get(3), innerLanguage);
        levelOneMap.put(categories.get(4), innerDance);
        levelOneMap.put(categories.get(5), innerArt);
        levelOneMap.put(categories.get(6), innerCooking);
        levelOneMap.put(categories.get(7), innerMusic);
        levelOneMap.put(categories.get(8), innerOthers);
        educaitonMap = new HashMap<>();
        educaitonMap.put(innerLevel.get(0), innerSchool);
        educaitonMap.put(innerLevel.get(1), innerCollege);
        educaitonMap.put(innerLevel.get(2), innerUniversity);
        musicMap = new HashMap<>();
        musicMap.put(innerMusic.get(0), innerMusicSinging);
        musicMap.put(innerMusic.get(1), innerMusicInstrumental);
        schoolMap = new HashMap<>();
        schoolMap.put(innerSchool.get(0), innerSchoolBanglaMedium);
        schoolMap.put(innerSchool.get(1), innerSchoolEnglishMedium);
        schoolMap.put(innerSchool.get(2), innerSchoolBanglaMedium);
        collegeMap = new HashMap<>();
        collegeMap.put(innerCollege.get(0), innerCollegeEnglishMedium);
        collegeMap.put(innerCollege.get(1), innerCollegeBanglaMedium);
        collegeMap.put(innerCollege.get(2), innerCollegeEnglishVersion);

        universityMap = new HashMap<>();
        universityMap.put(innerUniversity.get(0), innerUniversityEngineering);
        universityMap.put(innerUniversity.get(1), innerUniversityMedical);
        universityMap.put(innerUniversity.get(2), innerUniversityOthers);
        instrumentalMap = new HashMap<>();
        instrumentalMap.put(innerMusicInstrumental.get(0), innerMusicGuitar);


        schoolBanglaMediumMap = new HashMap<>();
        schoolBanglaMediumMap.put(innerSchoolBanglaMedium.get(0), innerSchoolBanglaMediumClassOne);
        schoolBanglaMediumMap.put(innerSchoolBanglaMedium.get(1), innerSchoolBanglaMediumClassTwo);
        schoolBanglaMediumMap.put(innerSchoolBanglaMedium.get(2), innerSchoolBanglaMediumClassThree);
        schoolBanglaMediumMap.put(innerSchoolBanglaMedium.get(3), innerSchoolBanglaMediumClassFour);
        schoolBanglaMediumMap.put(innerSchoolBanglaMedium.get(4), innerSchoolBanglaMediumClassFive);
        schoolBanglaMediumMap.put(innerSchoolBanglaMedium.get(5), innerSchoolBanglaMediumClassSix);
        schoolBanglaMediumMap.put(innerSchoolBanglaMedium.get(6), innerSchoolBanglaMediumClassSeven);
        schoolBanglaMediumMap.put(innerSchoolBanglaMedium.get(7), innerSchoolBanglaMediumClassEight);
        schoolBanglaMediumMap.put(innerSchoolBanglaMedium.get(8), innerSchoolBanglaMediumClassSSCScience);
        schoolBanglaMediumMap.put(innerSchoolBanglaMedium.get(9), innerSchoolBanglaMediumClassSSCCommerce);
        schoolBanglaMediumMap.put(innerSchoolBanglaMedium.get(10), innerSchoolBanglaMediumClassSSCArts);


        schoolEnglishVersionMap = new HashMap<>();
        schoolEnglishVersionMap.put(innerSchoolBanglaMedium.get(0), innerSchoolBanglaMediumClassOne);
        schoolEnglishVersionMap.put(innerSchoolBanglaMedium.get(1), innerSchoolBanglaMediumClassTwo);
        schoolEnglishVersionMap.put(innerSchoolBanglaMedium.get(2), innerSchoolBanglaMediumClassThree);
        schoolEnglishVersionMap.put(innerSchoolBanglaMedium.get(3), innerSchoolBanglaMediumClassFour);
        schoolEnglishVersionMap.put(innerSchoolBanglaMedium.get(4), innerSchoolBanglaMediumClassFive);
        schoolEnglishVersionMap.put(innerSchoolBanglaMedium.get(5), innerSchoolBanglaMediumClassSix);
        schoolEnglishVersionMap.put(innerSchoolBanglaMedium.get(6), innerSchoolBanglaMediumClassSeven);
        schoolEnglishVersionMap.put(innerSchoolBanglaMedium.get(7), innerSchoolBanglaMediumClassEight);
        schoolEnglishVersionMap.put(innerSchoolBanglaMedium.get(8), innerSchoolBanglaMediumClassSSCScience);
        schoolEnglishVersionMap.put(innerSchoolBanglaMedium.get(9), innerSchoolBanglaMediumClassSSCCommerce);
        schoolEnglishVersionMap.put(innerSchoolBanglaMedium.get(10), innerSchoolBanglaMediumClassSSCArts);

        schoolEnglishMediumMap = new HashMap<>();
        schoolEnglishMediumMap.put(innerSchoolEnglishMedium.get(0), innerSchoolEnglishMediumClassOne);
        schoolEnglishMediumMap.put(innerSchoolEnglishMedium.get(1), innerSchoolEnglishMediumClassTwo);
        schoolEnglishMediumMap.put(innerSchoolEnglishMedium.get(2), innerSchoolEnglishMediumClassThree);
        schoolEnglishMediumMap.put(innerSchoolEnglishMedium.get(3), innerSchoolEnglishMediumClassFour);
        schoolEnglishMediumMap.put(innerSchoolEnglishMedium.get(4), innerSchoolEnglishMediumClassFive);
        schoolEnglishMediumMap.put(innerSchoolEnglishMedium.get(5), innerSchoolEnglishMediumClassSix);
        schoolEnglishMediumMap.put(innerSchoolEnglishMedium.get(6), innerSchoolEnglishMediumClassSeven);
        schoolEnglishMediumMap.put(innerSchoolEnglishMedium.get(7), innerSchoolEnglishMediumClassEight);
        schoolEnglishMediumMap.put(innerSchoolEnglishMedium.get(8), innerSchoolEnglishMediumEdExcelOLevel);
        schoolEnglishMediumMap.put(innerSchoolEnglishMedium.get(9), innerSchoolEnglishMediumCambridgeOLevel);

        collegeBanglaMediumMap = new HashMap<>();
        collegeBanglaMediumMap.put(innerCollegeBanglaMedium.get(0), innerCollegeBanglaMediumScience);
        collegeBanglaMediumMap.put(innerCollegeBanglaMedium.get(1), innerCollegeBanglaMediumCommerce);
        collegeBanglaMediumMap.put(innerCollegeBanglaMedium.get(2), innerCollegeBanglaMediumArts);

        collegeEnglishMediumMap = new HashMap<>();
        collegeEnglishMediumMap.put(innerCollegeEnglishMedium.get(0), innerCollegeEnglishMediumEdexelAs_A2);
        collegeEnglishMediumMap.put(innerCollegeEnglishMedium.get(1), innerCollegeEnglishMediumCambridgeAs_A2);
        collegeEnglishMediumMap.put(innerCollegeEnglishMedium.get(2), innerCollegeEnglishMediumIbAs_A2);

        collegeEnglishVersionMap = new HashMap<>();
        collegeEnglishVersionMap.put(innerCollegeEnglishVersion.get(0), innerCollegeEnglishVersionScience);
        collegeEnglishVersionMap.put(innerCollegeEnglishVersion.get(1), innerCollegeEnglishVersionCommerce);
        collegeEnglishVersionMap.put(innerCollegeEnglishVersion.get(2), innerCollegeEnglishVersionArts);

       /* universityEngineerMap = new HashMap<>();
        universityEngineerMap.put(innerUniversityEngineering.get(0), uniTestSub);
        universityEngineerMap.put(innerUniversityEngineering.get(0), uniTestSub);
        universityEngineerMap.put(innerUniversityEngineering.get(0), uniTestSub);
        universityEngineerMap.put(innerUniversityEngineering.get(0), uniTestSub);
        universityEngineerMap.put(innerUniversityEngineering.get(0), uniTestSub);
        universityEngineerMap.put(innerUniversityEngineering.get(0), uniTestSub);
        universityEngineerMap.put(innerUniversityEngineering.get(0), uniTestSub);


        universityMedicalMap = new HashMap<>();
        universityMedicalMap.put(innerUniversityMedical.get(0), uniTestSub);
        universityMedicalMap.put(innerUniversityMedical.get(1), uniTestSub);
        universityMedicalMap.put(innerUniversityMedical.get(2), uniTestSub);
        universityMedicalMap.put(innerUniversityMedical.get(3), uniTestSub);
        universityMedicalMap.put(innerUniversityMedical.get(4), uniTestSub);
        universityMedicalMap.put(innerUniversityMedical.get(5), uniTestSub);
        universityMedicalMap.put(innerUniversityMedical.get(6), uniTestSub);
        universityMedicalMap.put(innerUniversityMedical.get(7), uniTestSub);
        universityMedicalMap.put(innerUniversityMedical.get(8), uniTestSub);
        universityMedicalMap.put(innerUniversityMedical.get(9), uniTestSub);
        universityMedicalMap.put(innerUniversityMedical.get(10), uniTestSub);
        universityHonsMap = new HashMap<>();*/


    }


    public List<String> getLevelOne(int i) {
        if (levelOneMap == null) {
            return null;
        }
        return levelOneMap.get(categories.get(i));
    }

    public List<String> getLevelTwo(String selected, String root) {

        List<String> list = null;
        switch (root) {
            case "Level":
                list = educaitonMap.get(selected);
                break;
            case "Software":
                list = null;
                break;
            case "Programming":
                list = null;
                break;
            case "Language":
                list = null;
                break;
            case "Dance":
                list = null;
                break;
            case "Art":
                list = null;
                break;
            case "Cooking":
                list = null;
                break;
            case "Music":
                list = musicMap.get(selected);
                break;
            case "Others":
                list = null;
                break;
            default:

        }
        return list;
    }

    public List<String> getLevelFour(String selected, String root, String grandRoot) {

        List<String> list = null;
        switch (grandRoot) {
            case "School":
                switch (root) {
                    case "English Medium":
                        list = schoolEnglishMediumMap.get(selected);
                        break;
                    case "Bangla Medium":
                        list = schoolBanglaMediumMap.get(selected);
                        break;
                    case "English Version":
                        list = schoolEnglishVersionMap.get(selected);
                        break;
                }
                break;
            case "College":

                switch (root) {
                    case "English Medium":
                        list = collegeEnglishMediumMap.get(selected);
                        break;
                    case "Bangla Medium":
                        list = collegeBanglaMediumMap.get(selected);
                        break;
                    case "English Version":
                        list = collegeEnglishVersionMap.get(selected);
                        break;
                }


                break;
            case "University":
                switch (root) {
                    case "Engineering":
                        //    list = universityEngineerMap.get(selected);
                        list = null;
                        break;
                    case "Medical":
                        // list = universityMedicalMap.get(selected);
                        list = null;

                        break;
                    case "Hons":
                        list = null;
                        break;
                }

                break;
            case "Instrumental":
                list = instrumentalMap.get(selected);
                break;
            case "Dance":
                list = null;
                break;
            case "Art":
                list = null;
                break;
            case "Cooking":
                list = null;
                break;
            case "Music":
                list = musicMap.get(selected);
                break;
            case "Others":
                list = null;
                break;
            default:

        }
        return list;
    }

    public List<String> getLevelThree(String selected, String root) {

        List<String> list = null;
        switch (root) {
            case "School":
                list = schoolMap.get(selected);
                break;
            case "College":
                list = collegeMap.get(selected);
                break;
            case "University":
                list = universityMap.get(selected);
                break;
            case "Instrumental":
                list = instrumentalMap.get(selected);
                break;
            case "Dance":
                list = null;
                break;
            case "Art":
                list = null;
                break;
            case "Cooking":
                list = null;
                break;
            case "Music":
                list = musicMap.get(selected);
                break;
            case "Others":
                list = null;
                break;
            default:

        }
        return list;
    }
    //4 sewing changed
}
