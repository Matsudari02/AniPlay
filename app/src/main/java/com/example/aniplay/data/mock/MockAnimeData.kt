package com.example.aniplay.data.mock

import com.example.aniplay.model.Anime

/**
 * Dados FICTÍCIOS para desenvolvimento (notas e imagens são placeholders).
 * Quando houver uma API real (ex.: AniList/Jikan), basta trocar a origem no repositório.
 */
object MockAnimeData {

    private fun cover(id: Int) = "https://picsum.photos/seed/aniplay-cover-$id/400/600"
    private fun banner(id: Int) = "https://picsum.photos/seed/aniplay-banner-$id/1280/720"

    val animes: List<Anime> = listOf(
        Anime(
            id = 1,
            title = "Demon Slayer: Kimetsu no Yaiba",
            synopsis = "Depois que sua família é atacada por demônios e a irmã mais nova é transformada em um deles, Tanjiro entra para um grupo de caçadores em busca de uma cura.",
            imageUrl = cover(1), bannerUrl = banner(1),
            genres = listOf("Ação", "Fantasia"),
            rating = 4.9, episodes = 26, year = 2019, studio = "Ufotable",
        ),
        Anime(
            id = 2,
            title = "Jujutsu Kaisen",
            synopsis = "Yuji Itadori, um estudante de força fora do comum, engole um objeto amaldiçoado e passa a dividir o corpo com uma maldição poderosa.",
            imageUrl = cover(2), bannerUrl = banner(2),
            genres = listOf("Ação", "Sobrenatural"),
            rating = 4.8, episodes = 24, year = 2020, studio = "MAPPA",
        ),
        Anime(
            id = 3,
            title = "Attack on Titan",
            synopsis = "A humanidade vive protegida por muralhas gigantes contra titãs devoradores. Quando a muralha cai, Eren jura destruir todos eles.",
            imageUrl = cover(3), bannerUrl = banner(3),
            genres = listOf("Ação", "Drama"),
            rating = 4.9, episodes = 25, year = 2013, studio = "Wit Studio",
        ),
        Anime(
            id = 4,
            title = "Fullmetal Alchemist: Brotherhood",
            synopsis = "Dois irmãos alquimistas pagam um preço alto por um ritual proibido e partem em busca da Pedra Filosofal para recuperar o que perderam.",
            imageUrl = cover(4), bannerUrl = banner(4),
            genres = listOf("Aventura", "Fantasia"),
            rating = 4.9, episodes = 64, year = 2009, studio = "Bones",
        ),
        Anime(
            id = 5,
            title = "SPY x FAMILY",
            synopsis = "Para cumprir uma missão, um espião precisa montar uma família de mentira. O problema é que a filha é telepata e a esposa é uma assassina.",
            imageUrl = cover(5), bannerUrl = banner(5),
            genres = listOf("Comédia", "Ação"),
            rating = 4.7, episodes = 25, year = 2022, studio = "Wit Studio / CloverWorks",
        ),
        Anime(
            id = 6,
            title = "Frieren: Além do Fim da Jornada",
            synopsis = "Depois de derrotar o Rei Demônio, a maga elfa Frieren percebe que mal conheceu seus companheiros humanos e parte numa nova jornada.",
            imageUrl = cover(6), bannerUrl = banner(6),
            genres = listOf("Fantasia", "Aventura", "Drama"),
            rating = 4.9, episodes = 28, year = 2023, studio = "Madhouse",
        ),
        Anime(
            id = 7,
            title = "Chainsaw Man",
            synopsis = "Endividado, Denji caça demônios ao lado do cão-motosserra Pochita. Uma traição muda a vida dos dois para sempre.",
            imageUrl = cover(7), bannerUrl = banner(7),
            genres = listOf("Ação", "Terror"),
            rating = 4.6, episodes = 12, year = 2022, studio = "MAPPA",
        ),
        Anime(
            id = 8,
            title = "Kaguya-sama: Love Is War",
            synopsis = "Dois gênios da elite escolar estão apaixonados, mas nenhum quer ser o primeiro a se declarar. Começa uma guerra de estratégias românticas.",
            imageUrl = cover(8), bannerUrl = banner(8),
            genres = listOf("Comédia", "Romance"),
            rating = 4.8, episodes = 12, year = 2019, studio = "A-1 Pictures",
        ),
        Anime(
            id = 9,
            title = "Steins;Gate",
            synopsis = "Um grupo de amigos descobre que um micro-ondas modificado consegue enviar mensagens ao passado e passa a lidar com as consequências.",
            imageUrl = cover(9), bannerUrl = banner(9),
            genres = listOf("Ficção científica", "Suspense"),
            rating = 4.9, episodes = 24, year = 2011, studio = "White Fox",
        ),
        Anime(
            id = 10,
            title = "Haikyu!!",
            synopsis = "Baixinho, mas cheio de energia, Hinata sonha em brilhar no vôlei e forma uma dupla improvável com um rival do ensino médio.",
            imageUrl = cover(10), bannerUrl = banner(10),
            genres = listOf("Esporte", "Comédia"),
            rating = 4.8, episodes = 25, year = 2014, studio = "Production I.G",
        ),
    )
}
