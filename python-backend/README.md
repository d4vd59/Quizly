# Python Backend für Quizly

## Was macht dieses Backend?

Dieses Python-Backend ist ein **einfacher Wrapper** für die externe Quizly API (`https://quizzapp.game-creators.de`).

Es wird von deinem C# Frontend aufgerufen und kommuniziert mit dem externen Server.

```
C# GUI (WPF)
    ↓ ruft auf
Python Backend (main.py)
    ↓ macht HTTP Request
Externe API (quizzapp.game-creators.de)
```

## Installation

```bash
cd python-backend
pip install -r requirements.txt
```

## Verwendung

### Von der Command Line testen:

```bash
# Kategorien holen
python src/main.py get_categories

# Settings holen
python src/main.py get_settings

# User registrieren
python src/main.py signup "test@test.de" "nickname" "Max Mustermann" "Pass123"

# User einloggen
python src/main.py signin "test@test.de" "Pass123"
```

### Von C# aufrufen:

```csharp
using System.Diagnostics;
using System.Text.Json;

public class PythonBackend
{
    private string pythonPath = "python";
    private string scriptPath = @"python-backend\src\main.py";
    
    private string RunPython(params string[] args)
    {
        var startInfo = new ProcessStartInfo
        {
            FileName = pythonPath,
            Arguments = $"{scriptPath} {string.Join(" ", args)}",
            RedirectStandardOutput = true,
            UseShellExecute = false,
            CreateNoWindow = true
        };
        
        using var process = Process.Start(startInfo);
        string output = process.StandardOutput.ReadToEnd();
        process.WaitForExit();
        return output;
    }
    
    public string GetCategories()
    {
        return RunPython("get_categories");
    }
    
    public string Signup(string email, string nickname, string fullname, string password)
    {
        return RunPython("signup", email, nickname, fullname, password);
    }
    
    public string Signin(string user, string password)
    {
        return RunPython("signin", user, password);
    }
}

// Verwendung:
var backend = new PythonBackend();
string json = backend.GetCategories();
var categories = JsonDocument.Parse(json);
```

## Verfügbare Commands

| Command | Args | Beschreibung |
|---------|------|--------------|
| `signup` | email nickname fullname password | User registrieren |
| `signin` | user password | User einloggen |
| `get_categories` | - | Alle Kategorien holen |
| `get_category` | category_id | Eine Kategorie holen |
| `get_difficulties` | - | Alle Schwierigkeitsgrade |
| `get_difficulty` | difficulty_id | Einen Schwierigkeitsgrad |
| `get_settings` | - | Spieleinstellungen |
| `get_users` | - | Alle User |
| `get_user` | user | User-Details |

## Projektstruktur

```
python-backend/
├── src/
│   ├── main.py                  # Einstiegspunkt (von C# aufgerufen)
│   ├── models/                  # Datenmodelle (Spieler, Frage, etc.)
│   │   ├── spieler.py
│   │   ├── frage.py
│   │   └── ...
│   └── services/
│       └── api_client.py        # Kommuniziert mit externer API
├── requirements.txt             # Python-Abhängigkeiten
└── README.md                    # Diese Datei
```

## Wie funktioniert es?

1. **C# ruft Python auf**: `python main.py get_categories`
2. **main.py** empfängt den Command
3. **api_client.py** macht HTTP-Request zu `quizzapp.game-creators.de`
4. **Externe API** gibt JSON zurück
5. **Python** gibt JSON an stdout aus
6. **C#** liest JSON und verarbeitet es

## Wichtig

- Alle Daten kommen vom **externen Server** (quizzapp.game-creators.de)
- Python ist nur ein **Wrapper** - keine eigene Datenbank
- Alle Commands geben **JSON** zurück
- Fehler werden als JSON mit `{"error": "..."}` zurückgegeben

## Project Structure
```
python-backend
├── src
│   ├── __init__.py
│   ├── main.py
│   ├── api
│   │   ├── __init__.py
│   │   └── routes.py
│   ├── models
│   │   └── __init__.py
│   ├── services
│   │   └── __init__.py
│   └── utils
│       └── __init__.py
├── tests
│   ├── __init__.py
│   └── test_main.py
├── requirements.txt
├── .env.example
├── .gitignore
└── README.md
```

## Setup Instructions
1. Clone the repository:
   ```
   git clone https://github.com/d4vd59/Quizly.git
   cd Quizly/python-backend
   ```

2. Create a virtual environment:
   ```
   python -m venv venv
   ```

3. Activate the virtual environment:
   - On Windows:
     ```
     venv\Scripts\activate
     ```
   - On macOS/Linux:
     ```
     source venv/bin/activate
     ```

4. Install the required dependencies:
   ```
   pip install -r requirements.txt
   ```

5. Set up environment variables:
   Copy `.env.example` to `.env` and configure the necessary variables.

## Usage
To run the application, execute the following command:
```
python src/main.py
```

## Testing
To run the tests, use:
```
pytest tests/
```

## Contributing
Contributions are welcome! Please open an issue or submit a pull request for any enhancements or bug fixes.

## License
This project is licensed under the MIT License. See the LICENSE file for details.