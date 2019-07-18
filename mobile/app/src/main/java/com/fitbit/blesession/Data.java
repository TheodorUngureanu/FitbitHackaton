package com.fitbit.blesession;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Data {

    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("timestampMs")
    @Expose
    private String timestampMs;
    @SerializedName("metadata")
    @Expose
    private Metadata metadata;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTimestampMs() {
        return timestampMs;
    }

    public void setTimestampMs(String timestampMs) {
        this.timestampMs = timestampMs;
    }

    public Metadata getMetadata() {
        return metadata;
    }

    public void setMetadata(Metadata metadata) {
        this.metadata = metadata;
    }

}
