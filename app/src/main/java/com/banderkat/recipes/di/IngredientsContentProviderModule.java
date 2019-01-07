package com.banderkat.recipes.di;

import com.banderkat.recipes.IngredientsContentProvider;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class IngredientsContentProviderModule {

    @ContributesAndroidInjector
    abstract IngredientsContentProvider contributeIngredientsContentProvider();
}
