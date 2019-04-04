package io.dume.dume.teacher.pojo;

import java.io.Serializable;

public class Academic implements Serializable {

    String institute;
    String degree;
    String from, to;
    String result;
    int resultType;
    String level;

    public Academic() {
    }

    public Academic(String level, String institute, String degree, String from, String to, String result) {
        this.institute = institute;
        this.degree = degree;
        this.from = from;
        this.to = to;
        this.result = result;
        this.level = level;
        if(result.equals("Studying")){
            setResultType(1);
        }else if(result.endsWith("(CGPA)")){
            setResultType(3);
        }else{
            setResultType(2);
        }
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
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

    public int getResultType() {
        return resultType;
    }

    public void setResultType(int resultType) {
        this.resultType = resultType;
    }


}
