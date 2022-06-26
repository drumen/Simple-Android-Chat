package email.rumen.simpleandroidchat.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Send
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import email.rumen.simpleandroidchat.R
import email.rumen.simpleandroidchat.model.MessageRealm
import email.rumen.simpleandroidchat.theme.*
import email.rumen.simpleandroidchat.utils.DateFormatter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import org.bson.types.ObjectId
import javax.inject.Inject
import kotlin.random.Random

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var dateFormatter: DateFormatter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            SimpleAndroidChatTheme {
               Scaffold (
                   topBar = { MuzTopAppBar() },
                   bottomBar = { MuzBottomAppBar() }
               ) { padding ->
                   Messages(dateFormatter, padding)
               }
            }
        }
    }
}

@Composable
fun Messages(dateFormatter: DateFormatter, padding: PaddingValues, viewModel: MainActivityViewModel = hiltViewModel()) {
    val messages = viewModel.messages.collectAsState(listOf<MessageRealm>())

    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()

    LazyColumn (
        state = listState,
        modifier = Modifier
            .fillMaxSize()
            .padding(
                top = padding.calculateTopPadding(),
                bottom = padding.calculateBottomPadding()
            )
    ) {
        items(messages.value) {
            if (it.timestampIt) {
                Timestamp(dateFormatter.formatDate(it.timestamp))
            }

            MessageBubble(it.message, it.isMuzMatch)
        }

        coroutineScope.launch {
            listState.animateScrollToItem(messages.value.size)
        }
    }
}

@Composable
fun MessageBubble(message: String, isMuzMatch: Boolean) {
    Box(modifier = Modifier
        .fillMaxWidth()
        .wrapContentHeight()
        .background(MainBackground)
    ) {
        Card(
            modifier = Modifier
                .align(if (isMuzMatch) Alignment.CenterStart else Alignment.CenterEnd)
                .widthIn(0.dp, 250.dp)
                .wrapContentSize()
                .padding(15.dp),
            shape = RoundedCornerShape(
                topStart = 15.dp,
                topEnd = 15.dp,
                bottomStart = if (isMuzMatch) 0.dp else 15.dp,
                bottomEnd = if (isMuzMatch) 15.dp else 0.dp,
            ),
            backgroundColor = if (isMuzMatch) Secondary300 else Primary500,
            elevation = 10.dp
        ) {
            Column(
                modifier = Modifier.padding(15.dp)
            ) {
                Text(
                    text = message,
                    modifier = Modifier.wrapContentSize(),
                    color = if (isMuzMatch) Secondary500 else MuzWhite,
                    fontSize = 15.sp,

                )
            }
        }
    }
}

@Composable
fun Timestamp(timestamp: String) {
    val timestampSplit: List<String> = timestamp.split(" ")

    Box(modifier = Modifier
        .fillMaxWidth()
        .wrapContentHeight()
        .background(MainBackground)
    ) {
        Box(
            modifier = Modifier
                .align(Alignment.Center)
                .widthIn(0.dp, 250.dp)
                .wrapContentSize()
                .padding(15.dp),
        ) {
            Column(
                modifier = Modifier.padding(15.dp)
            ) {
                Text(
                    text = AnnotatedString(
                        text = timestampSplit[0] + " ",
                        spanStyle = SpanStyle(
                            color = Secondary500,
                            fontWeight = FontWeight.W700
                        )
                    ).plus(
                        AnnotatedString(
                            text = timestampSplit[1],
                            spanStyle = SpanStyle(color = Secondary500)
                        )
                    ),
                    modifier = Modifier.wrapContentSize(),
                    fontSize = 15.sp
                )
            }
        }
    }
}

@Composable
fun MuzTopAppBar() {
    TopAppBar(
        modifier = Modifier.height(80.dp),
        backgroundColor = MuzWhite,
        elevation = 24.dp
    ) {
        Image(
            painterResource(id = R.drawable.profile_picture),
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .padding(start = 10.dp)
                .size(45.dp)
                .clip(CircleShape),
            alignment = Alignment.CenterStart,
            contentDescription = "Profile picture"
        )

        Text(
            text = "Sarah",
            modifier = Modifier.padding(start = 10.dp),
            color = Secondary700,
            fontSize = 20.sp,
            fontWeight = FontWeight.W700
        )

        Spacer(Modifier.weight(1f))

        Icon(
            Icons.Default.MoreVert,
            modifier = Modifier.size(35.dp),
            contentDescription = "Options",
            tint = Secondary500
        )
    }
}

@Composable
fun MuzBottomAppBar(viewModel: MainActivityViewModel = hiltViewModel()) {
    var text by remember { mutableStateOf("") }

    BottomAppBar(
        modifier = Modifier.height(80.dp),
        backgroundColor = MuzWhite,
        elevation = 24.dp
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                modifier = Modifier
                    .height(75.dp)
                    .padding(horizontal = 10.dp, vertical = 10.dp)
                    .weight(1f),
                value = text,
                shape = RoundedCornerShape(50),
                onValueChange = { text = it },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    capitalization = KeyboardCapitalization.Sentences
                ),
                textStyle = TextStyle(color = Secondary500, fontWeight = FontWeight.Bold),
            )

            Button(
                modifier = Modifier.size(55.dp),
                shape = RoundedCornerShape(50),
                onClick = {
                    viewModel.setPost(MessageRealm(ObjectId(), System.currentTimeMillis(), false, text, false))
                    text = ""
                    viewModel.getPost(Random.nextInt(1, 100))
                }
            ) {
                Icon(
                    Icons.Default.Send,
                    modifier = Modifier.fillMaxSize(),
                    contentDescription = "Send message",
                    tint = MuzWhite
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun show() {
    MessageBubble("Lorem Ipsum is simply dummy text of the printing and typesetting industry.", true)
}
