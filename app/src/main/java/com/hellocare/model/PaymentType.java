package com.hellocare.model;


import com.hellocare.R;

//enum for signing drawable res for types
public  enum PaymentType {

    DEFAULT("Default",  R.drawable.card),
    ONLINE("online",  R.drawable.card),
    CASH("cash", R.drawable.cash);

    public final String type;

    /**
     *
     * @return id of drawable resource
     */
    public int getDrawableResId() {
        return drawableResId;
    }

    private final int drawableResId;

    private PaymentType(String type, int resId) {
        this.type = type;

        this.drawableResId = resId;


    }


    public static PaymentType fromValue(String type) {
        for (PaymentType serviceType : PaymentType.values()) {
            if (serviceType.type.equals( type)) {
                return serviceType;
            }
        }
        return DEFAULT;
    }



}
