package com.frontline.mainservice.utils;

public class State {
    private int sequenceID;
    private String stateDescription;
    private String stateMessage;

    public State(int sequenceID, String stateDescription, String stateMessage) {
        this.stateDescription = stateDescription;
        this.stateMessage = stateMessage;
        this.sequenceID = sequenceID;
    }

    public int getSequenceID() {
        return sequenceID;
    }

    public void setSequenceID(int sequenceID) {
        this.sequenceID = sequenceID;
    }

    public String getStateDescription() {
        return stateDescription;
    }

    public void setStateDescription(String stateDescription) {
        this.stateDescription = stateDescription;
    }

    public String getStateMessage() {
        return stateMessage;
    }

    public void setStateMessage(String stateMessage) {
        this.stateMessage = stateMessage;
    }
}
