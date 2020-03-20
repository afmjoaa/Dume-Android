package io.dume.dume.firstTimeUser

enum class ForwardFlowStatTeacher(val flow: String) {
    ROLE("role"),
    PERMISSION("permission"),
    PRIVACY("privacy"),
    LOGIN("login"),
    NID("nid"),
    REGISTER("register"), //register to location //DP not needed
    QUALIFICATION("qualification"),
    ADDSKILL("addSkill"),
    PAYMENT("payment")
}

enum class ForwardFlowStatStudent(val flow: String) {
    ROLE("role"),
    PERMISSION("permission"),
    PRIVACY("privacy"),
    LOGIN("login"),
    REGISTER("register"), //location will be added here
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

enum class Flag(var flow: String) {
    FORWARD_TEACHER("forward_teacher"),
    FORWARD_STUDENT("forward_student"),
    STUDENT_SEARCH("student_search"),
    STUDENT_POST("student_post"),
    TEACHER_SKILL("teacher_skill"),

}



