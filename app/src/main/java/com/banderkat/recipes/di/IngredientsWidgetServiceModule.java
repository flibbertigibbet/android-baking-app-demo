package com.banderkat.recipes.di;

import com.banderkat.recipes.services.IngredientsWidgetService;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class IngredientsWidgetServiceModule {

    @ContributesAndroidInjector
    abstract IngredientsWidgetService contributeIngredientsWidgetService();
}
