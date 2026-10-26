# AniPlay — base do projeto (Jetpack Compose + Hilt + Room + MVVM)

Projeto acadêmico de aplicativo Android inspirado no Crunchyroll com Compose e MVVM.

## Como abrir
1. Android Studio (Koala ou mais novo), JDK 17.
2. File > Open > pasta `AniPlay`. Aguarde o Gradle Sync.
3. Run no emulador (API 24+). Precisa de internet para as imagens de exemplo.

## Divisão das telas
| Tela | Arquivo | Responsável |
|------|---------|-------------|
| 1. Home | `ui/home/*`, `viewmodel/HomeViewModel.kt` | Integrante 1 (pronta) |
| 2. Detalhes | `ui/detail/DetailScreen.kt` | Integrante 2 |
| 3. Player | `ui/player/PlayerScreen.kt` | Integrante 3 |
| 4. Minha Lista | `ui/favorites/FavoritesScreen.kt` | Integrante 4 |
| 5. Perfil | `ui/profile/ProfileScreen.kt` | Integrante 5 |

## Regras para evitar conflito de merge
- Cada pessoa cria seu próprio `viewmodel/<Tela>ViewModel.kt` com `@HiltViewModel`.
- Não altere a assinatura dos `@Composable` das telas: o `AniPlayNavHost` já depende delas.
- `AnimeRepository`: só adicione métodos novos, não mude os existentes.
- Novos modelos vão em arquivos novos dentro de `model/`.
- Telas das abas (Início, Minha Lista, Perfil) devem usar `contentWindowInsets = WindowInsets(0, 0, 0, 0)` no Scaffold (veja `HomeScreen`).
- Se criar novas entidades Room, registre-as em `AniPlayDatabase` e suba o `version`.
