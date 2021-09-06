package com.example.android.moviefinder.model

import com.example.android.moviefinder.R

class RepositoryImpl : Repository {
    private val movies = listOf(
        Movie(
            id = 1,
            imageId = R.drawable.terminator,
            title = "Терминатор",
            originalTitle = "The Terminator",
            genres = arrayOf("фантастика", "боевик", "триллер"),
            duration = 108,
            rating = 8.0f,
            voteCount = 251_044,
            budget = 6_400_000,
            revenue = 78_371_200,
            released = "29.10.1984",
            overview = "История противостояния солдата Кайла Риза и киборга-терминатора, прибывших в 1984-й год из пост-апокалиптического будущего, где миром правят машины-убийцы, а человечество находится на грани вымирания. Цель киборга: убить девушку по имени Сара Коннор, чей ещё нерождённый сын к 2029 году выиграет войну человечества с машинами. Цель Риза: спасти Сару и остановить Терминатора любой ценой."
        ),
        Movie(
            id = 2,
            imageId = R.drawable.titanic,
            title = "Титаник",
            originalTitle = "Titanic",
            genres = arrayOf("мелодрама", "история", "триллер", "драма"),
            duration = 194,
            rating = 8.4f,
            voteCount = 577_192,
            budget = 200_000_000,
            revenue = 1_843_478_449,
            released = "01.11.1997",
            overview = "В первом и последнем плавании шикарного «Титаника» встречаются двое. Пассажир нижней палубы Джек выиграл билет в карты, а богатая наследница Роза отправляется в Америку, чтобы выйти замуж по расчёту. Чувства молодых людей только успевают расцвести, и даже не классовые различия создадут испытания влюблённым, а айсберг, вставший на пути считавшегося непотопляемым лайнера."
        ),
        Movie(
            id = 3,
            imageId = R.drawable.godfather,
            title = "Крестный отец",
            originalTitle = "The Godfather",
            genres = arrayOf("драма", "криминал"),
            duration = 175,
            rating = 8.7f,
            voteCount = 306_244,
            budget = 6_000_000,
            revenue = 243_862_778,
            released = "14.03.1972",
            overview = "Криминальная сага, повествующая о нью-йоркской сицилийской мафиозной семье Корлеоне. Фильм охватывает период 1945-1955 годов.\n" +
                    "\n" +
                    "Глава семьи, Дон Вито Корлеоне, выдаёт замуж свою дочь. В это время со Второй мировой войны возвращается его любимый сын Майкл. Майкл, герой войны, гордость семьи, не выражает желания заняться жестоким семейным бизнесом. Дон Корлеоне ведёт дела по старым правилам, но наступают иные времена, и появляются люди, желающие изменить сложившиеся порядки. На Дона Корлеоне совершается покушение."
        ),
        Movie(
            id = 4,
            imageId = R.drawable.greenmile,
            title = "Зеленая миля",
            originalTitle = "The Green Mile",
            genres = arrayOf("фэнтези", "драма", "криминал", "детектив"),
            duration = 189,
            rating = 9.1f,
            voteCount = 688_417,
            budget = 60_000_000,
            revenue = 286_801_374,
            released = "06.12.1999",
            overview = "История противостояния солдата Кайла Риза и киборга-терминатора, прибывших в 1984-й год из пост-апокалиптического будущего, где миром правят машины-убийцы, а человечество находится на грани вымирания. Цель киборга: убить девушку по имени Сара Коннор, чей ещё нерождённый сын к 2029 году выиграет войну человечества с машинами. Цель Риза: спасти Сару и остановить Терминатора любой ценой."
        ),
        Movie(
            id = 5,
            imageId = R.drawable.shawshank,
            title = "Побег из Шоушенка",
            originalTitle = "The Shawshank Redemption",
            genres = arrayOf("драма"),
            duration = 142,
            rating = 9.1f,
            voteCount = 780_525,
            budget = 25_000_000,
            revenue = 28_418_687,
            released = "10.09.1994",
            overview = "Бухгалтер Энди Дюфрейн обвинён в убийстве собственной жены и её любовника. Оказавшись в тюрьме под названием Шоушенк, он сталкивается с жестокостью и беззаконием, царящими по обе стороны решётки. Каждый, кто попадает в эти стены, становится их рабом до конца жизни. Но Энди, обладающий живым умом и доброй душой, находит подход как к заключённым, так и к охранникам, добиваясь их особого к себе расположения."
        )
    )

    override fun getMoviesFromLocalStorage(): List<Movie> {
        return movies
    }

    override fun getMoviesFromServer(): List<Movie> {
        return movies
    }

    override fun getMovieByIdFromLocalStorage(id: Int): Movie? {
        for (movie in movies) {
            if (id == movie.id) return movie
        }
        return null
    }

    override fun getMovieByIdFromServer(id: Int): Movie? {
        return getMovieByIdFromLocalStorage(id)
    }
}