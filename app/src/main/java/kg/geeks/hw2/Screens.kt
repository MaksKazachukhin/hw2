package kg.geeks.hw2

sealed class Screen {
    object BookList : Screen()
    data class BookDetail(val book: Book) : Screen()
}