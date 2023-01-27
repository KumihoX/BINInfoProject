package com.example.bininfo

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color.Companion.DarkGray
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.bininfo.ui.theme.*

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BINInfoTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    MainScreen()
                }
            }
        }
    }
}

@Composable
fun MainScreen() {
    val viewModel: MainViewModel = viewModel()

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp, 16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        BINField(viewModel = viewModel) { viewModel.onBinNumberChange(it) }
        AddToHistory(viewModel = viewModel)
        Scheme(viewModel = viewModel)
        Brand(viewModel = viewModel)
        CardNumber(viewModel = viewModel)
        Type(viewModel = viewModel)
        Prepaid(viewModel = viewModel)
        Country(viewModel = viewModel)
        Bank(viewModel = viewModel)

        if (viewModel.historySize.value != 0) {
            History()
            HistoryList(viewModel = viewModel)
        }
    }
}

@Composable
fun BINField(viewModel: MainViewModel, onBINChange: (String) -> Unit) {

    val number = remember { viewModel.binNumber }

    OutlinedTextField(
        value = number.value,
        onValueChange = onBINChange,
        modifier = Modifier
            .fillMaxWidth(),
        enabled = true,
        placeholder = {
            Text(
                text = stringResource(R.string.description),
                color = Gray,
                maxLines = 1
            )
        },
        shape = RoundedCornerShape(8.dp),
        singleLine = true,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        colors = TextFieldDefaults.outlinedTextFieldColors(
            unfocusedBorderColor = DarkGray,
            cursorColor = DarkGray,
            textColor = DarkGray,
            focusedBorderColor = Purple200
        )
    )
}

@Composable
fun AddToHistory(viewModel: MainViewModel) {
    Button(
        onClick = { viewModel.addToHistory() },
        enabled = viewModel.binNumber.value.isNotEmpty(),
        modifier = Modifier
            .fillMaxWidth()
            .padding(0.dp, 8.dp, 0.dp, 0.dp),
        shape = RoundedCornerShape(8.dp),
        colors = ButtonDefaults.buttonColors(
            backgroundColor = Purple200,
            contentColor = White,
            disabledBackgroundColor = Gray,
            disabledContentColor = White
        )
    )
    {
        Text(
            text = "Add to history",
            textAlign = TextAlign.Center,
            fontSize = 19.sp
        )
    }
}

@Composable
fun Scheme(viewModel: MainViewModel) {
    Headline(name = stringResource(R.string.scheme))
    StringInfo(data = viewModel.binInfo.value.scheme)
}

@Composable
fun Brand(viewModel: MainViewModel) {
    Headline(name = stringResource(R.string.brand))
    StringInfo(data = viewModel.binInfo.value.brand)
}

@Composable
fun CardNumber(viewModel: MainViewModel) {
    Headline(name = stringResource(R.string.card_number))

    Row(Modifier.fillMaxWidth())
    {
        Column()
        {
            SecondHeadline(name = stringResource(R.string.length))
            Text(
                text = if (viewModel.binInfo.value.number.length != null) {
                    viewModel.binInfo.value.number.length.toString()
                } else {
                    stringResource(R.string.nothing)
                },
                modifier = Modifier
                    .wrapContentSize(),
                textAlign = TextAlign.Start,
                fontSize = 16.sp,
                color = DarkGray
            )
        }
        Spacer(modifier = Modifier.width(30.dp))

        Column()
        {
            SecondHeadline(name = stringResource(R.string.luhn))
            BooleanInfo(viewModel.binInfo.value.number.luhn)
        }
    }
}

@Composable
fun Type(viewModel: MainViewModel) {
    Headline(name = stringResource(R.string.type))
    StringInfo(data = viewModel.binInfo.value.type)
}

@Composable
fun Prepaid(viewModel: MainViewModel) {
    Headline(name = stringResource(R.string.prepaid))
    BooleanInfo(viewModel.binInfo.value.prepaid)
}

@Composable
fun Country(viewModel: MainViewModel) {
    Headline(name = stringResource(R.string.country))

    Row(Modifier.fillMaxWidth())
    {
        Text(
            text = viewModel.binInfo.value.country.emoji,
            modifier = Modifier
                .wrapContentSize()
                .padding(0.dp, 0.dp, 0.dp, 2.dp),
            textAlign = TextAlign.Start,
            fontSize = 16.sp,
            color = DarkGray
        )
        if (viewModel.binInfo.value.country.emoji != stringResource(R.string.nothing)) {
            Spacer(modifier = Modifier.width(10.dp))
            Text(
                text = viewModel.binInfo.value.country.name,
                modifier = Modifier
                    .wrapContentSize()
                    .padding(0.dp, 0.dp, 0.dp, 2.dp),
                textAlign = TextAlign.Start,
                fontSize = 16.sp,
                color = DarkGray
            )
        }
    }
    val context = LocalContext.current

    ClickableText(
        text = AnnotatedString(
            "(" +
                    stringResource(R.string.latitude) + ": "
                    + "${
                when (viewModel.binInfo.value.country.latitude) {
                    null -> stringResource(R.string.nothing)
                    else -> viewModel.binInfo.value.country.latitude.toString()
                }
            }, "
                    + stringResource(R.string.longitude) + ": " +
                    "${
                        when (viewModel.binInfo.value.country.longitude) {
                            null -> stringResource(R.string.nothing)
                            else -> viewModel.binInfo.value.country.longitude.toString()
                        }
                    })"
        ),
        onClick = {
            if (viewModel.binInfo.value.country.latitude != null) {
                val latitude = viewModel.binInfo.value.country.latitude
                val longitude = viewModel.binInfo.value.country.longitude
                val intent = Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("google.streetview:cbll=$latitude,$longitude")
                )
                intent.setPackage("com.google.android.apps.maps")

                context.startActivity(intent)
            }
        },
        style = TextStyle(
            color = DarkGray,
            fontSize = 16.sp
        )
    )
}

@Composable
fun Bank(viewModel: MainViewModel) {
    Headline(name = stringResource(R.string.bank))
    StringInfo(data = viewModel.binInfo.value.bank.name)

    val context = LocalContext.current

    ClickableText(
        text = AnnotatedString(viewModel.binInfo.value.bank.url),
        style = TextStyle(
            color = if (viewModel.binInfo.value.bank.url != "?") {
                Blue
            } else {
                Black
            },
            fontSize = 16.sp
        ),
        onClick = {
            if (viewModel.binInfo.value.bank.url != "?") {
                val intent = Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("https://" + viewModel.binInfo.value.bank.url)
                )
                context.startActivity(intent)
            }
        },
        modifier = Modifier
            .fillMaxWidth(),
    )

    ClickableText(
        text = AnnotatedString(viewModel.binInfo.value.bank.phone),
        style = TextStyle(
            color = DarkGray,
            fontSize = 16.sp
        ),
        onClick = {
            if (viewModel.binInfo.value.bank.phone != "?") {
                val intent = Intent(
                    Intent.ACTION_DIAL,
                    Uri.parse("tel:" + viewModel.binInfo.value.bank.phone)
                )
                context.startActivity(intent)
            }
        },
        modifier = Modifier
            .fillMaxWidth(),
    )
}

@Composable
fun History() {
    Text(
        text = stringResource(R.string.history),
        modifier = Modifier
            .fillMaxWidth()
            .padding(0.dp, 16.dp, 0.dp, 2.dp),
        textAlign = TextAlign.Center,
        fontSize = 21.sp,
        color = Black
    )

    Text(
        text = stringResource(R.string.history_description),
        modifier = Modifier
            .fillMaxWidth()
            .padding(0.dp, 0.dp, 0.dp, 2.dp),
        textAlign = TextAlign.Center,
        fontSize = 14.sp,
        color = Gray
    )

}

@Composable
fun HistoryList(viewModel: MainViewModel) {
    val countCardNumbers = remember { viewModel.historySize }
    var curCardNumber = 0

    while (curCardNumber != countCardNumbers.value) {
        HistoryElement(number = viewModel.history[curCardNumber], viewModel = viewModel)

        curCardNumber++
    }
}

@Composable
fun HistoryElement(number: String, viewModel: MainViewModel) {
    Button(
        onClick = { viewModel.onBinNumberChange(number) },
        modifier = Modifier
            .fillMaxWidth()
            .padding(0.dp, 8.dp, 0.dp, 0.dp),
        shape = RoundedCornerShape(8.dp),
        colors = ButtonDefaults.buttonColors(
            backgroundColor = Purple200,
            contentColor = White
        )
    )
    {
        Text(
            text = number,
            textAlign = TextAlign.Start,
            fontSize = 16.sp
        )
    }
}


@Composable
fun Headline(name: String) {
    Text(
        text = name,
        modifier = Modifier
            .fillMaxWidth()
            .padding(0.dp, 16.dp, 0.dp, 2.dp),
        textAlign = TextAlign.Start,
        fontSize = 19.sp,
        color = Black
    )
}

@Composable
fun SecondHeadline(name: String) {
    Text(
        text = name,
        modifier = Modifier
            .wrapContentSize()
            .padding(0.dp, 0.dp, 0.dp, 2.dp),
        textAlign = TextAlign.Start,
        fontSize = 16.sp,
        color = DarkGray
    )
}

@Composable
fun StringInfo(data: String) {
    Text(
        text = data,
        modifier = Modifier
            .fillMaxWidth(),
        textAlign = TextAlign.Start,
        fontSize = 19.sp,
        color = DarkGray
    )
}

@Composable
fun BooleanInfo(data: Boolean? = null) {
    Text(
        text = when (data) {
            true -> "YES"
            false -> "NO"
            else -> "?"
        },
        modifier = Modifier
            .wrapContentSize(),
        textAlign = TextAlign.Start,
        fontSize = 16.sp,
        color = DarkGray
    )
}