package io.github.soojison.yfindr.api;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface GeocodeAPI {
    String API_PATH = "maps/api/geocode/json";
    String QUERY_ADDRESS = "address";
    String QUERY_API_KEY = "key";

    @GET(API_PATH)
    Call<GeocodeResult> getCurrentWeather(@Query(QUERY_ADDRESS) String address,
                                          @Query(QUERY_API_KEY) String apiKey);

}
