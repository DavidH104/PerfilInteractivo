
package com.example.perfilinteractivo

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import kotlinx.coroutines.launch
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.OnlineTileSourceBase
import org.osmdroid.util.GeoPoint
import org.osmdroid.util.MapTileIndex
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker

private val Azul = Color(0xFF1565C0)
private val Fondo = Color(0xFFF5F6FA)

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Configuration.getInstance().load(
            applicationContext,
            getSharedPreferences("osmdroid", MODE_PRIVATE)
        )

        Configuration.getInstance().userAgentValue =
            "PerfilInteractivo/1.0"

        setContent {
            MaterialTheme {
                PantallaPerfil()
            }
        }
    }
}

@Composable
fun PantallaPerfil() {

    val snackbarHostState = remember {
        SnackbarHostState()
    }

    val scope = rememberCoroutineScope()

    Scaffold(
        containerColor = Fondo,

        snackbarHost = {
            SnackbarHost(
                hostState = snackbarHostState
            )
        },

        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    scope.launch {
                        snackbarHostState.showSnackbar(
                            message = "¡Esta es mi práctica de Android! Si saco 10, ya te la sabes. 😎🔥",
                            withDismissAction = true,
                            duration = SnackbarDuration.Long
                        )
                    }
                },
                containerColor = Azul,
                contentColor = Color.White,
                shape = CircleShape
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Mostrar mensaje"
                )
            }
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(Fondo)
                .verticalScroll(rememberScrollState())
        ) {

            // PORTADA CON IMAGEN REAL
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
            ) {

                Image(
                    painter = painterResource(
                        id = R.drawable.portada
                    ),
                    contentDescription = "Portada tecnológica",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            }

            Spacer(Modifier.height(18.dp))

            // FOTOGRAFÍA CIRCULAR
            Image(
                painter = painterResource(
                    id = R.drawable.img2
                ),
                contentDescription = "Fotografía de perfil",
                modifier = Modifier
                    .size(112.dp)
                    .align(Alignment.CenterHorizontally)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop
            )

            Spacer(Modifier.height(14.dp))

            // NOMBRE Y CORREO
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Text(
                    text = "David de Jesús Hernández",
                    fontSize = 23.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    color = Color(0xFF202124)
                )

                Spacer(Modifier.height(6.dp))

                Text(
                    text = "daviddejesushh@gmail.com",
                    fontSize = 14.sp,
                    color = Azul,
                    textAlign = TextAlign.Center
                )

                Spacer(Modifier.height(6.dp))

                Text(
                    text = "Estudiante de Ingeniería | Desarrollador Android",
                    fontSize = 12.sp,
                    color = Color.Gray,
                    textAlign = TextAlign.Center
                )
            }

            Spacer(Modifier.height(24.dp))

            // ACERCA DE MÍ
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
            ) {

                Text(
                    text = "About",
                    fontSize = 23.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(Modifier.height(10.dp))

                Text(
                    text = "Soy estudiante de Ingeniería en " +
                            "Tecnologías de la Información e " +
                            "Innovación Digital en la Universidad " +
                            "Politécnica de Pachuca. Desarrollo " +
                            "aplicaciones móviles con Kotlin y " +
                            "Jetpack Compose, sistemas web con " +
                            "PHP, Laravel y JavaScript, y trabajo " +
                            "con bases de datos MongoDB Atlas y " +
                            "PostgreSQL. También utilizo Linux, " +
                            "Docker, Git y GitHub para desarrollar " +
                            "y administrar mis proyectos.",
                    fontSize = 14.sp,
                    lineHeight = 22.sp,
                    color = Color(0xFF424242)
                )

                Spacer(Modifier.height(24.dp))

                Text(
                    text = "Mis habilidades e intereses",
                    fontSize = 21.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(Modifier.height(5.dp))

                Text(
                    text = "Desliza horizontalmente para ver las 10 etiquetas",
                    fontSize = 12.sp,
                    color = Color.Gray
                )
            }

            // DIEZ ETIQUETAS
            EtiquetasPerfil()

            Spacer(Modifier.height(16.dp))

            // UBICACIÓN
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
            ) {

                Text(
                    text = "Mi ubicación",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(Modifier.height(6.dp))

                Text(
                    text = "Mapa interactivo con OpenStreetMap",
                    fontSize = 13.sp,
                    color = Color.Gray
                )

                Spacer(Modifier.height(12.dp))

                MapaPerfil()

                Spacer(Modifier.height(10.dp))

                Text(
                    text = "© OpenStreetMap contributors · © MapTiler",
                    fontSize = 11.sp,
                    color = Color.Gray
                )

                Spacer(Modifier.height(100.dp))
            }
        }
    }
}

@Composable
fun EtiquetasPerfil() {

    val etiquetas = listOf(
        "Kotlin",
        "Jetpack Compose",
        "Java",
        "C++",
        "PHP / Laravel",
        "JavaScript",
        "MongoDB Atlas",
        "PostgreSQL",
        "Docker",
        "Git / GitHub"
    )

    val colores = listOf(
        Color(0xFF1565C0),
        Color(0xFF6A1B9A),
        Color(0xFF00897B),
        Color(0xFFEF6C00),
        Color(0xFF3949AB)
    )

    val context = LocalContext.current

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState())
            .padding(
                horizontal = 20.dp,
                vertical = 14.dp
            ),
        horizontalArrangement = Arrangement.spacedBy(9.dp)
    ) {

        etiquetas.forEachIndexed { index, etiqueta ->

            val color = colores[index % colores.size]

            Surface(
                onClick = {
                    Toast.makeText(
                        context,
                        "Tecnología: $etiqueta",
                        Toast.LENGTH_SHORT
                    ).show()
                },
                color = color.copy(alpha = 0.10f),
                shape = RoundedCornerShape(50.dp),
                border = BorderStroke(
                    1.dp,
                    color.copy(alpha = 0.35f)
                )
            ) {

                Text(
                    text = etiqueta,
                    modifier = Modifier.padding(
                        horizontal = 15.dp,
                        vertical = 10.dp
                    ),
                    color = color,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}

@Composable
fun MapaPerfil() {

    val context = LocalContext.current

    val apiKey = obtenerClaveMapTiler(context)

    val ubicacion = remember {
        GeoPoint(19.9806, -98.6847)
    }

    if (
        apiKey.isBlank() ||
        apiKey == "TU_NUEVA_CLAVE"
    ) {

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(260.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(Color(0xFFE3F2FD)),
            contentAlignment = Alignment.Center
        ) {

            Text(
                text = "Configura tu clave de MapTiler\n" +
                        "en res/values/maptiler_keys.xml",
                textAlign = TextAlign.Center,
                color = Azul,
                modifier = Modifier.padding(16.dp)
            )
        }

        return
    }

    val mapa = remember(context, apiKey) {

        MapView(context).apply {

            val fuente = object : OnlineTileSourceBase(
                "MapTilerStreets",
                0,
                19,
                256,
                ".png",
                arrayOf(
                    "https://api.maptiler.com/"
                )
            ) {

                override fun getTileURLString(
                    pMapTileIndex: Long
                ): String {

                    val z = MapTileIndex.getZoom(
                        pMapTileIndex
                    )

                    val x = MapTileIndex.getX(
                        pMapTileIndex
                    )

                    val y = MapTileIndex.getY(
                        pMapTileIndex
                    )

                    return "https://api.maptiler.com/" +
                            "maps/streets-v2/256/" +
                            "$z/$x/$y.png?key=$apiKey"
                }
            }

            setTileSource(fuente)

            setMultiTouchControls(true)

            minZoomLevel = 2.0
            maxZoomLevel = 19.0

            controller.setZoom(15.0)
            controller.setCenter(ubicacion)

            val marcador = Marker(this)

            marcador.position = ubicacion
            marcador.title = "Ubicación de ejemplo"
            marcador.snippet = "Proyecto Perfil Interactivo"

            marcador.setAnchor(
                Marker.ANCHOR_CENTER,
                Marker.ANCHOR_BOTTOM
            )

            overlays.add(marcador)
        }
    }

    DisposableEffect(mapa) {

        mapa.onResume()

        onDispose {
            mapa.onPause()
            mapa.onDetach()
        }
    }

    Column {

        AndroidView(
            factory = { mapa },
            modifier = Modifier
                .fillMaxWidth()
                .height(280.dp)
                .clip(RoundedCornerShape(16.dp))
        )

        Spacer(Modifier.height(12.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {

            OutlinedButton(
                onClick = {
                    mapa.controller.setZoom(
                        (mapa.zoomLevelDouble - 1.0)
                            .coerceAtLeast(2.0)
                    )
                }
            ) {
                Text("−", fontSize = 20.sp)
            }

            Button(
                onClick = {
                    mapa.controller.setCenter(ubicacion)
                    mapa.controller.setZoom(15.0)
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Azul
                )
            ) {
                Text("Centrar")
            }

            OutlinedButton(
                onClick = {
                    mapa.controller.setZoom(
                        (mapa.zoomLevelDouble + 1.0)
                            .coerceAtMost(19.0)
                    )
                }
            ) {
                Text("+", fontSize = 20.sp)
            }
        }
    }
}

fun obtenerClaveMapTiler(
    context: android.content.Context
): String {
    return context.getString(
        R.string.maptiler_api_key
    )
}
