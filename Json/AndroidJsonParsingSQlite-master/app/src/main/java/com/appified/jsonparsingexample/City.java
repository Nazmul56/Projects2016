package com.appified.jsonparsingexample;

/**
 * Created by Priyabrat on 6/13/2015.
 */
public class City {

    private int id;
    private String name;
    private String state;
    private String description;

    public City() {
    }

    public City(String name, String state, String description) {
        this.name = name;
        this.state = state;
        this.description = description;
    }

    public City(int id, String name, String state, String description) {
        this.id = id;
        this.name = name;
        this.state = state;
        this.description = description;
    }

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

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
