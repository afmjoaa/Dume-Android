package io.dume.dume.teacher.pojo;

public class Academic {

    String institute;
    String degree;
    String from, to;
    String result;
    String resultType;
    String description;

    public Academic() {

    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public Academic(String institute, String degree, String from, String to, String result, String resultType, String description) {
        this.institute = institute;
        this.degree = degree;
        this.from = from;
        this.to = to;
        this.result = result;

        this.resultType = resultType;
        this.description = description;
    }

    public String getInstitute() {
        return institute;
    }

    public void setInstitute(String institute) {
        this.institute = institute;
    }

    public String getDegree() {
        return degree;
    }

    public void setDegree(String degree) {
        this.degree = degree;
    }



    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getResultType() {
        return resultType;
    }

    public void setResultType(String resultType) {
        this.resultType = resultType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


}
