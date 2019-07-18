package com.fitbit.blesession;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Metadata {

    @SerializedName("value")
    @Expose
    private String value;
    @SerializedName("scale")
    @Expose
    private String scale;

    public Metadata(String value, String scale) {
        this.value = value;
        this.scale = scale;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getScale() {
        return scale;
    }

    public void setScale(String scale) {
        this.scale = scale;
    }

}
