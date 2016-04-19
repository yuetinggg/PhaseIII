package com.mycompany.phaseiii;

/**
 * Created by yuetinggg on 7/19/15.
 */
public class User {
    private boolean isInspector;
    private String id;

    public User(boolean isInspector, String id) {
        this.isInspector = isInspector;
        this.id = id;
    }

    public boolean isInspector() {
        return isInspector;
    }

    public String getId() {
        return id;
    }

    public void setIsInspector(boolean isInspector) {
        this.isInspector = isInspector;
    }

    public void setId(String id) {
        this.id = id;
    }
}
