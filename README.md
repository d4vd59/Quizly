# 🧩 Quizly Desktop

Das ist **Quizly**. Die native Desktop-Version unserer Quiz-App, entwickelt in **C#** und **Python**. Die App bietet dieselben Kernfunktionen wie die mobilen und Web-Versionen, optimiert für eine flüssige PC-Experience.

---

## ✨ Features

* **Quiz spielen:** Volles Quiz-Erlebnis direkt auf dem Desktop.
* **Echtzeit-Duelle:** Spannende Wettkämpfe über das integrierte Backend.
* **Profile & Ranglisten:** Verfolge deinen Fortschritt und miss dich mit anderen.
* **Interaktives User Interface:** Optimiert für Windows (WPF).

---

## 🚀 Setup

Befolge diese Schritte, um das Projekt lokal auszuführen:

1.  **Voraussetzungen:**
    * Visual Studio installieren (inklusive der Workload **.NET Desktop Development**).
      
2.  **Repository klonen:**
    ```bash
    git clone https://github.com/d4vd59/Quizly
    ```
    
3.  **Umgebungsvariabeln konfigurieren:**
    * Im Projektverzeichnis befindet sich eine Datei env.example.
    * Nenne diese Datei um in ".env".
    * ⚠️ Wichtig: Ohne korrekt konfigurierte .env-Datei kann die Anwendung nicht gestartet werden.
      
4.  **Projekt öffnen:**
    * Öffne die `.sln`-Datei in Visual Studio.
    * Warte, bis alle NuGet-Pakete automatisch wiederhergestellt wurden.
      
5.  **Starten:**
    * Drücke `F5` oder klicke auf **Starten**, um die Anwendung auszuführen.

---

## 📱 Android-Client

Im Verzeichnis `Android/` befindet sich ein natives Android-Frontend (Kotlin + Jetpack Compose) mit denselben Kernfunktionen wie der Desktop-Client. Es spricht direkt dieselbe externe API an (`https://quizzapp.game-creators.de`), die auch das Python-Backend verwendet — es wird kein eigenes Backend benötigt.

**Voraussetzungen:**
* Android Studio (oder JDK 17+ und die Android SDK Command-Line-Tools).

**Setup:**
1.  Öffne den Ordner `Android/` in Android Studio, oder baue per Kommandozeile:
    ```bash
    cd Android
    ./gradlew assembleDebug
    ```
2.  Zum Installieren auf einem verbundenen Gerät/Emulator:
    ```bash
    ./gradlew installDebug
    ```

*Hinweis: Einige Gameplay-Endpunkte (Matchdetails, Rundenfragen, Antwort-Übermittlung, Einladungen, Bestenliste) sind in der externen API nicht offiziell dokumentiert ("speculative" im Code markiert) und können bei Abweichungen vom angenommenen Format clientseitig elegant abgefangen werden.*

---

*Entwickelt mit ❤️ in C#, Python und Kotlin.*
