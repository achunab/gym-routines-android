# GymRoutines Android 💪

![Kotlin](https://img.shields.io/badge/Kotlin-7F52FF?style=for-the-badge&logo=kotlin&logoColor=white)
![Android](https://img.shields.io/badge/Android-3DDC84?style=for-the-badge&logo=android&logoColor=white)
![Android Studio](https://img.shields.io/badge/Android_Studio-3DDC84?style=for-the-badge&logo=android-studio&logoColor=white)
![Graduation](https://img.shields.io/badge/Graduated-Oct_1_2026-FF69B4?style=for-the-badge)

> App Android disponible a partir del - 1 de Octubre 2026 (Jueves)

### 🚀 Sobre el proyecto
App para rutinas de gym con notificaciones diarias que **sí coinciden con el día real**. 
Corregido el bug clásico de `Calendar.DAY_OF_WEEK`.

**Rutina estrella:** 4x15 Piernas, Abs y Glúteo - By Coach ZaziL

### 🛠️ Tecnologías
- Kotlin
- Android SDK
- Calendar API / java.time

### 🐛 Bug corregido en esta versión
```kotlin
// ANTES (error): siempre daba 7
val dia = Calendar.DAY_OF_WEEK

// AHORA (correcto): hoy da 5 = Jueves
val dia = Calendar.getInstance().get(Calendar.DAY_OF_WEEK)
👩‍💻 Autor - Andy Chunab | Coach ZaziL | Yucatán, México
Comentarios - 01/10/2026⭐ Si te gusta la rutina, ¡dale Star al repo!
