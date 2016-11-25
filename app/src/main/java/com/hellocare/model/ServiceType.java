package com.hellocare.model;


import com.hellocare.R;

//enum for signing drawable res for types
public  enum ServiceType {

    DEFAULT("Default",  R.drawable.buy),
    COOKING("cooking",  R.drawable.prepare),
    SHOPPING("shopping", R.drawable.buy),
    CLEANING("cleaning",  R.drawable.cleaning),
    SUPPORT("support",  R.drawable.spot),
    CARE("care",  R.drawable.heart),
    ACCOMPANIMENT("accompaniment",  R.drawable.mask);
    public final String type;

    /**
     *
     * @return id of drawable resource
     */
    public int getDrawableResId() {
        return drawableResId;
    }

    private final int drawableResId;

    private ServiceType(String type, int resId) {
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
