package io.dume.dume.firstTimeUser

enum class ForwardFlowStatTeacher(val flow: String) {
    ROLE("role"),
    PERMISSION("permission"),
    PRIVACY("privacy"),
    LOGIN("login"),
    VERIFICATION("verification"),
    NID("nid"),
    REGISTER("register"),//register to location //DP not needed
    QUALIFICATION("qualification"),
    ADDSKILL("addSkill"),
    PAYMENT("payment")
}

enum class ForwardFlowStatStudent(val flow: String) {
    ROLE("role"),
    PERMISSION("permission"),
    PRIVACY("privacy"),
    LOGIN("login"),
    VERIFICATION("verification"),
    REGISTER("register"),//location will be added here
    POSTJOB("PostJob")
}

enum class UserState(val userStat: String) {
    USERFOUND("userfound"),
    NEWUSERFOUND("newuserfound"),
    ERROR("error")
}

enum class Role(val flow: String) {
    TEACHER("teacher"),
    STUDENT("student")
}



