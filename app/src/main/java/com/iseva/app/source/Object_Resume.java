package com.iseva.app.source;

/**
 * Created by hp on 9/12/2016.
 */
public class Object_Resume {

    private int id;
    private String name;
    private String Fname;
    private String DOB;
    private String gender;
    private String Address;
    private String number;
    private String email;
    private String qualification;
    private String experience;
    private String currentJob;
    private String currentSalary;
    private String others;
    private String filename;
    private Object_City objectCity;
    private Object_Category objectCategory;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Object_Category getObjectCategory() {
        return objectCategory;
    }

    public void setObjectCategory(Object_Category objectCategory) {
        this.objectCategory = objectCategory;
    }

    public Object_City getObjectCity() {
        return objectCity;
    }

    public void setObjectCity(Object_City objectCity) {
        this.objectCity = objectCity;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public String getCurrentJob() {
        return currentJob;
    }

    public void setCurrentJob(String currentJob) {
        this.currentJob = currentJob;
    }

    public String getCurrentSalary() {
        return currentSalary;
    }

    public void setCurrentSalary(String currentSalary) {
        this.currentSalary = currentSalary;
    }

    public String getDOB() {
        return DOB;
    }

    public void setDOB(String DOB) {
        this.DOB = DOB;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getExperience() {
        return experience;
    }

    public void setExperience(String experience) {
        this.experience = experience;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getFname() {
        return Fname;
    }

    public void setFname(String fname) {
        Fname = fname;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getOthers() {
        return others;
    }

    public void setOthers(String others) {
        this.others = others;
    }

    public String getQualification() {
        return qualification;
    }

    public void setQualification(String qualification) {
        this.qualification = qualification;
    }
}
