import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material.*
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp

data class Book(val id: Int, val title: String, val author: String, val description: String, val category: String)

val books = listOf(
    Book(1, "1984", "George Orwell", "Dystopian novel", "Fiction"),
    Book(2, "Brave New World", "Aldous Huxley", "Dystopian sci-fi", "Fiction"),
    Book(3, "Sapiens", "Yuval Noah Harari", "A history of humankind", "Non-Fiction"),
    Book(4, "Clean Code", "Robert C. Martin", "Guide to writing clean code", "Programming")
)
val categories = listOf("All", "Fiction", "Non-Fiction", "Programming")

sealed class Screen {
    object BookList : Screen()
    data class BookDetail(val book: Book) : Screen()
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { App() }
    }
}

@Composable
fun App() {
    var currentScreen by remember { mutableStateOf<Screen>(Screen.BookList) }
    when (currentScreen) {
        is Screen.BookList -> BookListScreen { book -> currentScreen = Screen.BookDetail(book) }
        is Screen.BookDetail -> BookDetailScreen((currentScreen as Screen.BookDetail).book) { currentScreen = Screen.BookList }
    }
}

@Composable
fun BookListScreen(onBookClick: (Book) -> Unit) {
    var searchQuery by remember { mutableStateOf(TextFieldValue()) }
    var selectedCategory by remember { mutableStateOf("All") }

    val filteredBooks = books.filter {
        (selectedCategory == "All" || it.category == selectedCategory) &&
                (it.title.contains(searchQuery.text, true) || it.author.contains(searchQuery.text, true))
    }

    Column {
        TextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            label = { Text("Search books") },
            modifier = Modifier.fillMaxWidth().padding(8.dp)
        )

        LazyRow(modifier = Modifier.padding(8.dp)) {
            items(categories) { category ->
                Button(
                    onClick = { selectedCategory = category },
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = if (selectedCategory == category) MaterialTheme.colors.primary else MaterialTheme.colors.surface
                    ),
                    modifier = Modifier.padding(horizontal = 4.dp)
                ) {
                    Text(category)
                }
            }
        }

        LazyColumn(modifier = Modifier.fillMaxSize().padding(8.dp)) {
            items(filteredBooks) { book ->
                Card(
                    modifier = Modifier.fillMaxWidth().padding(4.dp).clickable { onBookClick(book) },
                    elevation = 4.dp
                ) {
                    Column(modifier = Modifier.padding(8.dp)) {
                        Text(book.title, style = MaterialTheme.typography.)
                        Text("by ${book.author}", style = MaterialTheme.typography.)
                    }
                }
            }
        }
    }
}

@Composable
fun BookDetailScreen(book: Book, onBack: () -> Unit) {
    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Button(onClick = onBack, modifier = Modifier.padding(bottom = 8.dp)) {
            Text("Back")
        }
        Text(book.title, style = MaterialTheme.typography)
        Text("by ${book.author}", style = MaterialTheme.typography)
        Spacer(modifier = Modifier.height(8.dp))
        Text(book.description)
    }
}
