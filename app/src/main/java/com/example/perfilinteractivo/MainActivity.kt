
package com.example.perfilinteractivo

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
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

private val Morado = Color(0xFF6938D3)
private val Azul = Color(0xFF3159DF)
private val Fondo = Color(0xFFF7F7FD)
private val Oscuro = Color(0xFF172033)
private val Gris = Color(0xFF68738A)

private data class Tecnologia(
    val nombre: String,
    val imagen: String,
    val inicial: String
)

private val tecnologias = listOf(
    Tecnologia("Kotlin", "logo_kotlin", "K"),
    Tecnologia("Jetpack Compose", "logo_compose", "JC"),
    Tecnologia("Java", "logo_java", "J"),
    Tecnologia("C++", "logo_cpp", "C++"),
    Tecnologia("PHP / Laravel", "logo_php", "PHP"),
    Tecnologia("JavaScript", "logo_javascript", "JS"),
    Tecnologia("MongoDB Atlas", "logo_mongodb", "DB"),
    Tecnologia("PostgreSQL", "logo_postgresql", "SQL"),
    Tecnologia("Docker", "logo_docker", "D"),
    Tecnologia("Git / GitHub", "logo_git", "Git")
)

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Configuration.getInstance().load(
            applicationContext,
            getSharedPreferences("osmdroid", MODE_PRIVATE)
        )

        Configuration.getInstance().userAgentValue =
            "PerfilInteractivo/2.1"

        setContent {
            MaterialTheme(
                colorScheme = lightColorScheme(
                    primary = Morado,
                    background = Fondo,
                    surface = Color.White
                )
            ) {
                PantallaPerfil()
            }
        }
    }
}

@Composable
fun PantallaPerfil() {
    val snackbar = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    Scaffold(
        containerColor = Fondo,
        snackbarHost = {
            SnackbarHost(hostState = snackbar)
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    scope.launch {
                        snackbar.showSnackbar(
                            message = "¡Esta es mi práctica de Android! Si saco 10, Ya Paseme Profe \uD83E\uDD7A ",
                            withDismissAction = true,
                            duration = SnackbarDuration.Long
                        )
                    }
                },
                containerColor = Morado,
                contentColor = Color.White,
                shape = CircleShape,
                modifier = Modifier
                    .size(62.dp)
                    .shadow(10.dp, CircleShape)
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Mostrar mensaje",
                    modifier = Modifier.size(31.dp)
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

            CabeceraPerfil()

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
            ) {

                Spacer(Modifier.height(18.dp))

                InformacionPerfil()

                Spacer(Modifier.height(26.dp))

                EncabezadoSeccion(
                    titulo = "ACERCA DE MÍ",
                    subtitulo = "Mi formación, intereses y trayectoria"
                )

                Spacer(Modifier.height(12.dp))

                TarjetaDescripcion()

                Spacer(Modifier.height(27.dp))

                EncabezadoSeccion(
                    titulo = "LENGUAJES DE PROGRAMACIÓN",
                    subtitulo = "10 tecnologías y herramientas · Desliza →"
                )
            }

            Spacer(Modifier.height(12.dp))

            SeccionTecnologias()

            Spacer(Modifier.height(26.dp))

            Column(
                modifier = Modifier.padding(horizontal = 20.dp)
            ) {

                EncabezadoSeccion(
                    titulo = "UBICACIÓN",
                    subtitulo = "Explora el mapa interactivo"
                )

                Spacer(Modifier.height(13.dp))

                MapaInteractivo()

                Spacer(Modifier.height(23.dp))

                TarjetaRepositorio()

                Spacer(Modifier.height(110.dp))
            }
        }
    }
}

@Composable
fun LogoOpcional(
    nombre: String,
    inicial: String,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    val recurso = remember(context, nombre) {
        context.resources.getIdentifier(
            nombre,
            "drawable",
            context.packageName
        )
    }

    if (recurso != 0) {
        Image(
            painter = painterResource(recurso),
            contentDescription = nombre,
            modifier = modifier,
            contentScale = ContentScale.Fit
        )
    } else {
        Box(
            modifier = modifier
                .clip(RoundedCornerShape(12.dp))
                .background(Color(0xFFECE5FF)),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = inicial,
                color = Morado,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
fun CabeceraPerfil() {

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(280.dp)
    ) {

        Image(
            painter = painterResource(R.drawable.portada),
            contentDescription = "Portada",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        listOf(
                            Color.Black.copy(alpha = 0.08f),
                            Color(0xFF111B43).copy(alpha = 0.85f)
                        )
                    )
                )
        )

        Surface(
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(start = 18.dp, top = 18.dp),
            color = Color.White.copy(alpha = 0.92f),
            shape = RoundedCornerShape(22.dp),
            shadowElevation = 5.dp
        ) {
            Row(
                modifier = Modifier.padding(
                    horizontal = 12.dp,
                    vertical = 9.dp
                ),
                verticalAlignment = Alignment.CenterVertically
            ) {
                LogoOpcional(
                    nombre = "logo_upp",
                    inicial = "UPP",
                    modifier = Modifier.size(35.dp)
                )

                Spacer(Modifier.width(9.dp))

                Column {
                    Text(
                        text = "Universidad Politécnica",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = Oscuro
                    )

                    Text(
                        text = "de Pachuca",
                        fontSize = 11.sp,
                        color = Gris
                    )
                }
            }
        }

        Column(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(end = 18.dp, bottom = 40.dp),
            horizontalAlignment = Alignment.End
        ) {
            Text(
                text = "DIGITAL PORTFOLIO",
                color = Color(0xFFCBD5FF),
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.5.sp
            )

            Spacer(Modifier.height(5.dp))

            Text(
                text = "Innovación\nsin límites.",
                color = Color.White,
                fontSize = 24.sp,
                lineHeight = 28.sp,
                fontWeight = FontWeight.ExtraBold,
                textAlign = TextAlign.End
            )
        }

        Image(
            painter = painterResource(R.drawable.img2),
            contentDescription = "Foto de perfil",
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(start = 19.dp)
                .size(122.dp)
                .shadow(9.dp, CircleShape)
                .background(Color.White, CircleShape)
                .padding(5.dp)
                .clip(CircleShape),
            contentScale = ContentScale.Crop
        )
    }
}

@Composable
fun InformacionPerfil() {

    Column {

        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {

            Text(
                text = "David de Jesús\nHernández Hernández",
                fontSize = 25.sp,
                lineHeight = 30.sp,
                fontWeight = FontWeight.ExtraBold,
                color = Oscuro,
                modifier = Modifier.weight(1f)
            )

            Surface(
                shape = CircleShape,
                color = Morado
            ) {
                Text(
                    text = "✓",
                    color = Color.White,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(
                        horizontal = 10.dp,
                        vertical = 6.dp
                    )
                )
            }
        }

        Spacer(Modifier.height(9.dp))

        Text(
            text = "Ingeniería en Tecnologías de la Información e Innovación Digital",
            color = Morado,
            fontSize = 14.sp,
            lineHeight = 21.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(Modifier.height(9.dp))

        Text(
            text = "Estudiante UPP  |  Desarrollo Android y Web",
            color = Gris,
            fontSize = 12.sp
        )

        Spacer(Modifier.height(13.dp))

        Surface(
            color = Color(0xFFEDE8FC),
            shape = RoundedCornerShape(30.dp)
        ) {
            Text(
                text = "✉  daviddejesushh@gmail.com",
                color = Morado,
                fontSize = 12.sp,
                modifier = Modifier.padding(
                    horizontal = 14.dp,
                    vertical = 10.dp
                )
            )
        }
    }
}

@Composable
fun EncabezadoSeccion(
    titulo: String,
    subtitulo: String
) {
    Column {
        Text(
            text = titulo,
            fontSize = 17.sp,
            fontWeight = FontWeight.ExtraBold,
            color = Oscuro,
            letterSpacing = 0.4.sp
        )

        Spacer(Modifier.height(5.dp))

        Text(
            text = subtitulo,
            fontSize = 12.sp,
            color = Gris
        )
    }
}

@Composable
fun TarjetaDescripcion() {

    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(22.dp),
        color = Color.White,
        shadowElevation = 4.dp,
        border = BorderStroke(
            1.dp,
            Color(0xFFEAEAF3)
        )
    ) {

        Column(
            modifier = Modifier.padding(20.dp)
        ) {

            Text(
                text = "✦  Mi perfil profesional",
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = Morado
            )

            Spacer(Modifier.height(12.dp))

            Text(
                text = "Soy estudiante de Ingeniería en Tecnologías de la Información e Innovación Digital en la Universidad Politécnica de Pachuca. Me apasiona el desarrollo de aplicaciones móviles con Kotlin y Jetpack Compose, así como la creación de sistemas web y el trabajo con bases de datos. También utilizo Linux, Docker, Git y GitHub para desarrollar y administrar proyectos tecnológicos.",
                fontSize = 13.sp,
                lineHeight = 21.sp,
                color = Color(0xFF48546B)
            )
        }
    }
}

@Composable
fun SeccionTecnologias() {

    val context = LocalContext.current
    val scroll = rememberScrollState()

    Column {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(scroll)
                .padding(
                    horizontal = 20.dp,
                    vertical = 6.dp
                ),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {

            tecnologias.chunked(2).forEach { columna ->

                Column(
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {

                    columna.forEach { tecnologia ->

                        Surface(
                            onClick = {
                                Toast.makeText(
                                    context,
                                    "Tecnología: ${tecnologia.nombre}",
                                    Toast.LENGTH_SHORT
                                ).show()
                            },
                            modifier = Modifier
                                .width(175.dp)
                                .height(70.dp),
                            shape = RoundedCornerShape(18.dp),
                            color = Color.White,
                            shadowElevation = 3.dp,
                            border = BorderStroke(
                                1.dp,
                                Color(0xFFE9E6F3)
                            )
                        ) {

                            Row(
                                modifier = Modifier.padding(10.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {

                                LogoOpcional(
                                    nombre = tecnologia.imagen,
                                    inicial = tecnologia.inicial,
                                    modifier = Modifier.size(39.dp)
                                )

                                Spacer(Modifier.width(9.dp))

                                Text(
                                    text = tecnologia.nombre,
                                    fontSize = 12.sp,
                                    lineHeight = 16.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = Oscuro
                                )
                            }
                        }
                    }
                }
            }
        }

        Spacer(Modifier.height(10.dp))

        Text(
            text = "10 tecnologías disponibles  •  Desliza horizontalmente",
            fontSize = 11.sp,
            color = Morado,
            modifier = Modifier.padding(horizontal = 20.dp)
        )
    }
}

@Composable
fun MapaInteractivo() {

    val context = LocalContext.current

    val apiKey = context.getString(
        R.string.maptiler_api_key
    ).trim()

    val ubicacion = remember {
        GeoPoint(19.9806, -98.6847)
    }

    if (
        apiKey.isBlank() ||
        apiKey.startsWith("TU_CLAVE") ||
        apiKey == "TU_NUEVA_CLAVE"
    ) {

        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .height(260.dp),
            color = Color(0xFFEDE8FC),
            shape = RoundedCornerShape(22.dp)
        ) {

            Box(contentAlignment = Alignment.Center) {
                Text(
                    text = "Configura tu clave de MapTiler en res/values/maptiler_keys.xml",
                    color = Morado,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(20.dp)
                )
            }
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
                arrayOf("https://api.maptiler.com/")
            ) {

                override fun getTileURLString(
                    pMapTileIndex: Long
                ): String {

                    val z = MapTileIndex.getZoom(pMapTileIndex)
                    val x = MapTileIndex.getX(pMapTileIndex)
                    val y = MapTileIndex.getY(pMapTileIndex)

                    return "https://api.maptiler.com/maps/streets-v2/256/$z/$x/$y.png?key=$apiKey"
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
            marcador.snippet = "PerfilInteractivo"

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

    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(22.dp),
        color = Color.White,
        shadowElevation = 4.dp
    ) {

        Column {

            AndroidView(
                factory = { mapa },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp)
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
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
                        containerColor = Morado
                    )
                ) {
                    Text("⌖  Centrar")
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

            Text(
                text = "© OpenStreetMap contributors · © MapTiler",
                fontSize = 10.sp,
                color = Gris,
                modifier = Modifier.padding(
                    start = 15.dp,
                    bottom = 13.dp
                )
            )
        }
    }
}

@Composable
fun TarjetaRepositorio() {

    val context = LocalContext.current

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                val intent = Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse(
                        "https://github.com/DavidH104/PerfilInteractivo"
                    )
                )

                context.startActivity(intent)
            },
        shape = RoundedCornerShape(22.dp),
        color = Oscuro
    ) {

        Row(
            modifier = Modifier.padding(20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Box(
                modifier = Modifier
                    .size(50.dp)
                    .clip(RoundedCornerShape(15.dp))
                    .background(Morado),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "</>",
                    color = Color.White,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(Modifier.width(14.dp))

            Column(modifier = Modifier.weight(1f)) {

                Text(
                    text = "MI REPOSITORIO",
                    color = Color(0xFFBDB3FF),
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(Modifier.height(5.dp))

                Text(
                    text = "PerfilInteractivo en GitHub",
                    color = Color.White,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(Modifier.height(5.dp))

                Text(
                    text = "github.com/DavidH104/PerfilInteractivo",
                    color = Color(0xFFD0D5DD),
                    fontSize = 10.sp,
                    lineHeight = 15.sp
                )
            }

            Text(
                text = "↗",
                color = Color.White,
                fontSize = 27.sp
            )
        }
    }
}
