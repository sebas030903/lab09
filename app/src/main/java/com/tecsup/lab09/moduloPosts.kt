package com.tecsup.lab09

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController

@Composable
fun ScreenPosts(navController: NavHostController, servicio: PostApiService) {
    // Lista reactiva para almacenar posts
    val listaPosts = remember { mutableStateListOf<PostModel>() }

    // Carga los posts solo una vez al iniciar esta composición
    LaunchedEffect(Unit) {
        val listado = servicio.getUserPosts()
        listaPosts.clear()
        listaPosts.addAll(listado)
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize()
    ) {
        items(listaPosts) { item ->
            Row(modifier = Modifier.padding(8.dp)) {
                Text(
                    text = item.id.toString(),
                    modifier = Modifier.weight(0.05f),
                    textAlign = TextAlign.End
                )
                Spacer(Modifier.width(8.dp))
                Text(
                    text = item.title,
                    modifier = Modifier.weight(0.7f)
                )
                IconButton(
                    onClick = {
                        navController.navigate("postsVer/${item.id}")
                        Log.e("POSTS", "ID = ${item.id}")
                    },
                    modifier = Modifier.weight(0.1f)
                ) {
                    Icon(imageVector = Icons.Outlined.Search, contentDescription = "Ver")
                }
            }
        }
    }
}

@Composable
fun ScreenPost(navController: NavHostController, servicio: PostApiService, id: Int) {
    var post by remember { mutableStateOf<PostModel?>(null) }

    LaunchedEffect(id) { // LaunchedEffect depende del id para recargar si cambia
        val xpost = servicio.getUserPostById(id)
        post = xpost // si no necesitas copia, puedes asignar directamente
    }

    Column(
        Modifier
            .padding(8.dp)
            .fillMaxSize()
    ) {
        if (post != null) {
            OutlinedTextField(
                value = post!!.id.toString(),
                onValueChange = {},
                label = { Text("id") },
                readOnly = true
            )
            OutlinedTextField(
                value = post!!.userId.toString(),
                onValueChange = {},
                label = { Text("userId") },
                readOnly = true
            )
            OutlinedTextField(
                value = post!!.title,
                onValueChange = {},
                label = { Text("title") },
                readOnly = true
            )
            OutlinedTextField(
                value = post!!.body ?: "",
                onValueChange = {},
                label = { Text("body") },
                readOnly = true
            )
        } else {
            Text("Cargando post...", modifier = Modifier.padding(8.dp))
        }
    }
}
