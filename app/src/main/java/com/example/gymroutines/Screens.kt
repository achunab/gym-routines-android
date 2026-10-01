package com.example.gymroutines

//Link para página con emojis
//https://emojipedia.org/star
//Link para iconos de aplicaciones móviles
//https://romannurik.github.io/AndroidAssetStudio/icons-launcher.html#foreground.type=clipart&foreground.clipart=android&foreground.space.trim=1&foreground.space.pad=0.25&foreColor=rgba(96%2C%20125%2C%20139%2C%200)&backColor=rgb(68%2C%20138%2C%20255)&crop=0&backgroundShape=circle&effects=none&name=ic_launcher

sealed class Screens(val route: String, val label: String, val emoji: String) {
    //object Home : Screens("home", "Entreno", "\uD83C\uDFCB\uFE0F\u200D♀\uFE0F")
    object Home : Screens("home", "home", "\uD83C\uDFCB\uFE0F\u200D♀\uFE0F")
    object Historial : Screens("historial", "Historial", "\uD83D\uDD25")
    object Acerca : Screens("acerca", "Acerca","⭐")
}