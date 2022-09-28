package com.example.composetip

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.composetip.ui.theme.ComposeTipTheme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ComposeTipTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    TipScreen()
                }
            }
        }
    }
}

@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun TipScreen(){

    var bill:Int? by remember { mutableStateOf(null)}
    var scope = rememberCoroutineScope()
    var isShowingSnackbar by remember { mutableStateOf(true)}
    var scaffoldState = rememberScaffoldState(snackbarHostState = SnackbarHostState())

    Scaffold(
        scaffoldState = scaffoldState
    ){
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(25.dp),
            verticalArrangement = Arrangement.spacedBy(25.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            TipTitle()
            TextField(
                value = if(bill == null) {
                    ""
                }else {
                    "$bill"
                },
                label = {Text("Cost of Services")},
                onValueChange = {
                    bill = it.toInt()
                },
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(
                    onDone = {
                        if(bill == null){
                            isShowingSnackbar = false
                        } else {
                            isShowingSnackbar = true
                        }
                        Log.d("onDone end", "$isShowingSnackbar")
                    }
                )
            )
        }
    }
    LaunchedEffect(key1 = isShowingSnackbar){
        scope.launch {
            if (isShowingSnackbar) {
                scaffoldState.snackbarHostState.showSnackbar(
                    message = "${bill.tipIt()}",
                    actionLabel = "TIP"
                )
            }
            isShowingSnackbar = false
            Log.d("scope", "$isShowingSnackbar")
        }
    }
}

@Composable
fun TipTitle(){
    Text(
        text = "Calculate Tip",
        style = MaterialTheme.typography.h4
    )
}

fun <A:Int?> A.tipIt():Double{
    return this?.times(0.15) ?: 0.0
}



@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    ComposeTipTheme {
        TipScreen()
    }
}