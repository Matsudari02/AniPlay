package com.example.aniplay

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

/** Ponto de entrada do Hilt: gera o grafo de dependências do app. */
@HiltAndroidApp
class AniPlayApplication : Application()
