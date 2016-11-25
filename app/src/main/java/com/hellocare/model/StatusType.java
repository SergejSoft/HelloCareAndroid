package com.hellocare.model;


public enum StatusType {
    //types of communication object
    DEFAULT("new"),
    ACCEPTED("accepted"),
    ASSIGNED("assigned");
    private final String type;


    private StatusType(String type) {
        this.type = type;
    }


    public static StatusType fromValue(String type) {
        for (StatusType statusType : StatusType.values()) {
            if (statusType.type.equals(type)) {
                return statusType;
            }
        }
        return DEFAULT;
    }

    @Override
    public String toString() {
        return type;
    }

}
