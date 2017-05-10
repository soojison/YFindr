package io.github.soojison.yfindr.api;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Bounds {

    @SerializedName("northeast")
    @Expose
    public Northeast northeast;
    @SerializedName("southwest")
    @Expose
    public Southwest southwest;

}