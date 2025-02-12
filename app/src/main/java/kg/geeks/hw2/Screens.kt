package kg.geeks.hw2

sealed class Screen {
    data object BookList : Screen()
    data class BookDetail(val book: Book) : Screen()
}