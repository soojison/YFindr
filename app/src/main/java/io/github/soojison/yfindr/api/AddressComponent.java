package io.github.soojison.yfindr.api;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class AddressComponent {

    @SerializedName("long_name")
    @Expose
    public String longName;
    @SerializedName("short_name")
    @Expose
    public String shortName;
    @SerializedName("types")
    @Expose
    public List<String> types = null;

}
