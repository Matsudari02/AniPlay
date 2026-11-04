package com.example.aniplay.data.mock

import com.example.aniplay.model.Episode

/**
 * Dados fictícios de episódios para o Player de Vídeo (INTEGRANTE 3).
 */
object MockEpisodeData {

    // Vídeo de exemplo público para teste de reprodução
    private const val SAMPLE_VIDEO_URL = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4"

    private fun epThumb(animeId: Int, epNum: Int) =
        "https://picsum.photos/seed/aniplay-ep-$animeId-$epNum/640/360"

    private val TITLES = listOf(
        "O Início da Jornada",
        "O Desafio Crucial",
        "Revelações Sombrias",
        "Laços Indestrutíveis",
        "O Despertar do Poder",
        "Confronto nas Sombras",
        "A Determinação do Herói",
        "Estratégia e Coragem",
        "O Preço da Vitória",
        "Uma Nova Esperança",
        "Duelo Final",
        "Além do Horizonte",
    )

    private val DESCRIPTIONS = listOf(
        "Diante de um perigo iminente, uma escolha difícil precisa ser feita para proteger aqueles que mais importam.",
        "Treinamentos intensos e descobertas surpreendentes colocam a determinação dos protagonistas à prova.",
        "Um segredo do passado vem à tona, mudando o rumo da missão e testando a confiança do grupo.",
        "Aliados improváveis se unificam para enfrentar um inimigo que ameaça o equilíbrio do mundo.",
        "Em um momento de desespero, uma nova habilidade é despertada, surpreendendo a todos no campo de batalha.",
    )

    /** Gera uma lista de episódios fictícios para um anime. */
    fun getEpisodesForAnime(animeId: Int, totalEpisodes: Int): List<Episode> {
        val count = totalEpisodes.coerceAtMost(12) // Limita a 12 episódios no mock
        return List(count) { index ->
            val epNum = index + 1
            val titleIndex = index % TITLES.size
            val descIndex = index % DESCRIPTIONS.size
            Episode(
                id = (animeId * 100) + epNum,
                animeId = animeId,
                number = epNum,
                title = "Episódio $epNum: ${TITLES[titleIndex]}",
                description = DESCRIPTIONS[descIndex],
                durationSeconds = 1420, // 23min 40s
                thumbnailUrl = epThumb(animeId, epNum),
                videoUrl = SAMPLE_VIDEO_URL,
            )
        }
    }
}
