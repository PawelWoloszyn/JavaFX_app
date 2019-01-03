package com.woloszyn.studyApp.dataModel;

public class Category implements Comparable<Category> {
    private String name;


    public Category(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public int hashCode() {
        return this.name.hashCode() + 54;
    }

    @Override
    public boolean equals(Object obj) {
        if(this == obj){
            return true;
        }
        if((obj == null) || (obj.getClass() != this.getClass())){
            return false;
        }

        String objName = ((Category) obj).toString();
        return this.name.equals(objName);
    }

    @Override
    public int compareTo(Category o) {
        return this.name.compareToIgnoreCase(o.toString());
    }
}
