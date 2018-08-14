package com.banderkat.recipes.data;

import android.arch.lifecycle.LiveData;

import com.banderkat.recipes.data.models.Recipe;
import com.banderkat.recipes.data.networkresource.ApiResponse;

import retrofit2.http.GET;

public interface RecipeWebservice {

    String WEBSERVICE_URL = "https://d17h27t6h515a5.cloudfront.net/topher/2017/May/59121517_baking/";

    @GET("baking.json")
    LiveData<ApiResponse<Recipe[]>> getRecipes();
}
