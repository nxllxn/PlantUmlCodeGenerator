package com.nxllxn.plantuml.java;

import java.lang.reflect.Modifier;

/**
 * 用于定义Java类型的可见性
 * @author wenchao
 */
public enum Visibility {
    PRIVATE("-"),
    DEFAULT("~"),
    PROTECTED("#"),
    PUBLIC("+");

    private String visibility;

    Visibility(String visibility) {
        this.visibility = visibility;
    }

    @Override
    public String toString() {
        return this.visibility;
    }

    public static Visibility from(int modifier) {
        if (Modifier.isPublic(modifier)) {
            return Visibility.PUBLIC;
        }

        if (Modifier.isProtected(modifier)) {
            return Visibility.PROTECTED;
        }

        if (Modifier.isProtected(modifier)) {
            return Visibility.PRIVATE;
        }

        return Visibility.DEFAULT;
    }
}
