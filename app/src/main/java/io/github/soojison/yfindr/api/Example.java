package io.github.soojison.yfindr.api;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Example {

    @SerializedName("results")
    @Expose
    public List<GeocodeResult> results = null;
    @SerializedName("status")
    @Expose
    public String status;

}