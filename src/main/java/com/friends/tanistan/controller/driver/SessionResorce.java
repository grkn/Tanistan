package com.friends.tanistan.controller.driver;

import java.io.Serializable;

public class SessionResorce {

    private Object desiredCapabilities = new Test();

    public Object getDesiredCapabilities() {
        return desiredCapabilities;
    }

    public void setDesiredCapabilities(Object desiredCapabilities) {
        this.desiredCapabilities = desiredCapabilities;
    }

    private class Test implements Serializable{

    }
}
