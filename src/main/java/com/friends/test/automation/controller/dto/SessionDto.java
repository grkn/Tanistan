package com.friends.test.automation.controller.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.List;

public class SessionDto implements Serializable {

    private DesiredCapabilities desiredCapabilities;

    public DesiredCapabilities getDesiredCapabilities() {
        return desiredCapabilities;
    }

    public void setDesiredCapabilities(DesiredCapabilities desiredCapabilities) {
        this.desiredCapabilities = desiredCapabilities;
    }

    public static class DesiredCapabilities {

        private String browserName;
        @JsonProperty("goog:chromeOptions")
        private ChromeOptions chromeOptions;
        private Capabilities capabilities;

        public String getBrowserName() {
            return browserName;
        }

        public void setBrowserName(String browserName) {
            this.browserName = browserName;
        }

        public ChromeOptions getChromeOptions() {
            return chromeOptions;
        }

        public void setChromeOptions(ChromeOptions chromeOptions) {
            this.chromeOptions = chromeOptions;
        }

        public Capabilities getCapabilities() {
            return capabilities;
        }

        public void setCapabilities(Capabilities capabilities) {
            this.capabilities = capabilities;
        }
    }

    public static class ChromeOptions {

        private String opt;

        public String getOpt() {
            return opt;
        }

        public void setOpt(String opt) {
            this.opt = opt;
        }
    }

    public static class Capabilities {

        private List<DesiredCapabilities> firstMatch;

        public List<DesiredCapabilities> getFirstMatch() {
            return firstMatch;
        }

        public void setFirstMatch(List<DesiredCapabilities> firstMatch) {
            this.firstMatch = firstMatch;
        }
    }
}
