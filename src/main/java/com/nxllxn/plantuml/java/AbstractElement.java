package com.nxllxn.plantuml.java;

public abstract class AbstractElement implements Element {
    protected Visibility visibility;
    protected String name;

    public Visibility getVisibility() {
        return visibility;
    }

    public void setVisibility(Visibility visibility) {
        this.visibility = visibility;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "AbstractElement{" +
                "visibility=" + visibility +
                ", name='" + name + '\'' +
                '}';
    }
}
