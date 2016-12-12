package com.hellocare.model;


import com.hellocare.R;

//enum for signing drawable res for types
public  enum ServiceType {

    DEFAULT("Default",  R.drawable.buy),
    COOKING("cooking",  R.drawable.cook),
    SHOPPING("shopping", R.drawable.shopping),
    CLEANING("cleaning",  R.drawable.clean),
    SUPPORT("support",  R.drawable.support),
    CARE("care",  R.drawable.care),
    ACCOMPANIMENT("accompaniment",  R.drawable.accomp);
    public final String type;

    /**
     *
     * @return id of drawable resource
     */
    public int getDrawableResId() {
        return drawableResId;
    }

    private final int drawableResId;

    ServiceType(String type, int resId) {
        this.type = type;

        this.drawableResId = resId;


    }


    public static ServiceType fromValue(String type) {
        for (ServiceType serviceType : ServiceType.values()) {
            if (serviceType.type.equals( type)) {
                return serviceType;
            }
        }
        return DEFAULT;
    }



}
