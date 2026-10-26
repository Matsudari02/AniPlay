package com.example.aniplay.di;

import com.example.aniplay.data.local.AniPlayDatabase;
import com.example.aniplay.data.local.FavoriteAnimeDao;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

@ScopeMetadata
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
public final class DatabaseModule_ProvideFavoriteAnimeDaoFactory implements Factory<FavoriteAnimeDao> {
  private final Provider<AniPlayDatabase> databaseProvider;

  public DatabaseModule_ProvideFavoriteAnimeDaoFactory(Provider<AniPlayDatabase> databaseProvider) {
    this.databaseProvider = databaseProvider;
  }

  @Override
  public FavoriteAnimeDao get() {
    return provideFavoriteAnimeDao(databaseProvider.get());
  }

  public static DatabaseModule_ProvideFavoriteAnimeDaoFactory create(
      Provider<AniPlayDatabase> databaseProvider) {
    return new DatabaseModule_ProvideFavoriteAnimeDaoFactory(databaseProvider);
  }

  public static FavoriteAnimeDao provideFavoriteAnimeDao(AniPlayDatabase database) {
    return Preconditions.checkNotNullFromProvides(DatabaseModule.INSTANCE.provideFavoriteAnimeDao(database));
  }
}
