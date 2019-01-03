package com.woloszyn.studyApp.dataModel;

import javafx.collections.ObservableList;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ImportantEvent {
    private String name;
    private String description;
    private Category category;
    private List<Query> queries = new ArrayList<>();


    public ImportantEvent(String name, String description, Category category) {
        this.name = name;
        this.description = description;
        this.category = category; // we need to specify a category to every event
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public List<Query> getQueries() {
        return queries;
    }

    public Category getCategory() {
        return category;
    }

    public void addQuery(String question, String answer){
        Query newQuery = new Query(question, answer);
        queries.add(newQuery);
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public int hashCode() {
        return this.name.hashCode() + this.category.hashCode() + 21;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == this) {
            return true;
        }
        if((obj == null) || obj.getClass()!= this.getClass())
            return false;
        String objName = ((ImportantEvent) obj).getName();
        Category objCategory = ((ImportantEvent) obj).getCategory();

        return (this.name.equals(objName) && this.category.equals(objCategory));
    }
}
