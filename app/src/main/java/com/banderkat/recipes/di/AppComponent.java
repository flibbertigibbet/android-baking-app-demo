package com.banderkat.recipes.di;

import android.app.Application;

import com.banderkat.recipes.BakingApp;
import com.banderkat.recipes.IngredientsContentProvider;

import javax.inject.Singleton;

import dagger.BindsInstance;
import dagger.Component;
import dagger.android.AndroidInjectionModule;

/**
 * Based on:
 * https://github.com/googlesamples/android-architecture-components/blob/e33782ba54ebe87f7e21e03542230695bc893818/GithubBrowserSample/app/src/main/java/com/android/example/github/di/AppComponent.java
 */

@Singleton
@Component(modules = {
        AndroidInjectionModule.class,
        AppModule.class,

        // Activities
        RecipesMainActivityModule.class,

        IngredientsWidgetServiceModule.class,
        IngredientsContentProviderModule.class
})
public interface AppComponent {
    @Component.Builder
    interface Builder {
        @BindsInstance
        Builder application(Application application);
        AppComponent build();
    }
    void inject(BakingApp bakingApp);
}
