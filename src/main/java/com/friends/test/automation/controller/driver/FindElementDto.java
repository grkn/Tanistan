package com.friends.test.automation.controller.driver;

/*
{"using":"<find strategy>","value":"<element-value>"}
{"using":"id","value":"email"}
{"using":"css selector","value":"#pass"}
xpath, className, name, link text, partial link text, tag name
 */
public class FindElementDto {

    private String using;
    private String value;

    public String getUsing() {
        return using;
    }

    public void setUsing(String using) {
        this.using = using;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
