package com.friends.test.automation.controller.resource;

public class RunnableResource {

    private Integer position;
    private String selectedElementId;
    private String message;
    private DefaultResource result;
    private String type;
    private String navigateUrl;
    private String selectionType;
    private String selectionValue;

    public Integer getPosition() {
        return position;
    }

    public void setPosition(Integer position) {
        this.position = position;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getNavigateUrl() {
        return navigateUrl;
    }

    public void setNavigateUrl(String navigateUrl) {
        this.navigateUrl = navigateUrl;
    }

    public String getSelectedElementId() {
        return selectedElementId;
    }

    public void setSelectedElementId(String selectedElementId) {
        this.selectedElementId = selectedElementId;
    }

    public String getSelectionType() {
        return selectionType;
    }

    public void setSelectionType(String selectionType) {
        this.selectionType = selectionType;
    }

    public String getSelectionValue() {
        return selectionValue;
    }

    public void setSelectionValue(String selectionValue) {
        this.selectionValue = selectionValue;
    }

    public DefaultResource getResult() {
        return result;
    }

    public void setResult(DefaultResource result) {
        this.result = result;
    }
}
