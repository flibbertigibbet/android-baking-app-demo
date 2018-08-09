package com.banderkat.recipes.di;

import com.banderkat.recipes.RecipesMainActivity;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

/**
 * Based on:
 * https://github.com/googlesamples/android-architecture-components/blob/e33782ba54ebe87f7e21e03542230695bc893818/GithubBrowserSample/app/src/main/java/com/android/example/github/di/MainActivityModule.java
 */

@Module
public abstract class RecipesMainActivityModule {
    @ContributesAndroidInjector
    abstract RecipesMainActivity contributeRecipesMainActivity();
}
