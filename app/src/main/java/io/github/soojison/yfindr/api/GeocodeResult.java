package io.github.soojison.yfindr.api;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by sonsooji on 5/10/17.
 */

public class GeocodeResult {

    @SerializedName("address_components")
    @Expose
    public List<AddressComponent> addressComponents = null;
    @SerializedName("formatted_address")
    @Expose
    public String formattedAddress;
    @SerializedName("geometry")
    @Expose
    public Geometry geometry;
    @SerializedName("place_id")
    @Expose
    public String placeId;
    @SerializedName("types")
    @Expose
    public List<String> types = null;

}
