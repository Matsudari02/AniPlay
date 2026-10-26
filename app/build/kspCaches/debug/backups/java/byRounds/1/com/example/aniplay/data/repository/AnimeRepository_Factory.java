package com.example.aniplay.data.repository;

import com.example.aniplay.data.local.FavoriteAnimeDao;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

@ScopeMetadata("javax.inject.Singleton")
@QualifierMetadata
@DaggerGenerated
@Generated(
    value = "dagger.internal.codegen.ComponentProcessor",
    comments = "https://dagger.dev"
)
@SuppressWarnings({
    "unchecked",
    "rawtypes",
    "KotlinInternal",
    "KotlinInternalInJava",
    "cast",
    "deprecation"
})
public final class AnimeRepository_Factory implements Factory<AnimeRepository> {
  private final Provider<FavoriteAnimeDao> favoriteDaoProvider;

  public AnimeRepository_Factory(Provider<FavoriteAnimeDao> favoriteDaoProvider) {
    this.favoriteDaoProvider = favoriteDaoProvider;
  }

  @Override
  public AnimeRepository get() {
    return newInstance(favoriteDaoProvider.get());
  }

  public static AnimeRepository_Factory create(Provider<FavoriteAnimeDao> favoriteDaoProvider) {
    return new AnimeRepository_Factory(favoriteDaoProvider);
  }

  public static AnimeRepository newInstance(FavoriteAnimeDao favoriteDao) {
    return new AnimeRepository(favoriteDao);
  }
}
