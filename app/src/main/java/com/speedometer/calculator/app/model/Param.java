package com.speedometer.calculator.app.model;

import java.io.Serializable;

public class Param implements Serializable {

    private String name;
    private boolean checked;
    private String value;
    private String category;

    public Param(String name, boolean checked, String value, String category) {
        this.name = name;
        this.checked = checked;
        this.value = value;
        this.category = category;
    }

    public Param(String name, boolean checked, String value) {
        this.name = name;
        this.checked = checked;
        this.value = value;
    }

    public Param(String name, String value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
