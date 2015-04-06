package com.example.jephillips.ereader;

import retrofit.http.GET;

/**
 * Created by Josh on 4/4/2015.
 */
public interface BookUpdateInterface {
    @GET("/misc/empublite-update.json")
    BookUpdateInfo update();

}
