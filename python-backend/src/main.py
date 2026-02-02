"""
Quizly Backend - Einfacher API Wrapper

Wird von C# aufgerufen und kommuniziert mit der externen API.

Verwendung:
    python main.py signup email nickname fullname password
    python main.py signin user password
    python main.py get_categories
    python main.py get_settings
"""

import sys
import json
from services.api_client import APIClient


def main():
    """Hauptfunktion - wird von C# aufgerufen"""
    
    if len(sys.argv) < 2:
        print(json.dumps({"error": "Kein Command angegeben"}))
        return
    
    command = sys.argv[1]
    client = APIClient()
    
    try:
        # User registrieren
        if command == "signup":
            if len(sys.argv) < 6:
                print(json.dumps({"error": "signup benötigt: email nickname fullname password"}))
                return
            
            result = client.signup(
                email=sys.argv[2],
                nickname=sys.argv[3],
                fullname=sys.argv[4],
                password=sys.argv[5]
            )
            print(json.dumps(result))
        
        # User einloggen
        elif command == "signin":
            if len(sys.argv) < 4:
                print(json.dumps({"error": "signin benötigt: user password"}))
                return
            
            result = client.signin(
                user=sys.argv[2],
                password=sys.argv[3]
            )
            print(json.dumps(result))
        
        # Kategorien holen
        elif command == "get_categories":
            result = client.get_categories()
            print(json.dumps(result))
        
        # Kategorie nach ID
        elif command == "get_category":
            if len(sys.argv) < 3:
                print(json.dumps({"error": "get_category benötigt: category_id"}))
                return
            
            result = client.get_category(int(sys.argv[2]))
            print(json.dumps(result))
        
        # Schwierigkeitsgrade holen
        elif command == "get_difficulties":
            result = client.get_difficulties()
            print(json.dumps(result))
        
        # Schwierigkeitsgrad nach ID
        elif command == "get_difficulty":
            if len(sys.argv) < 3:
                print(json.dumps({"error": "get_difficulty benötigt: difficulty_id"}))
                return
            
            result = client.get_difficulty(int(sys.argv[2]))
            print(json.dumps(result))
        
        # Settings holen
        elif command == "get_settings":
            result = client.get_settings()
            print(json.dumps(result))
        
        # User-Liste
        elif command == "get_users":
            result = client.get_users()
            print(json.dumps(result))
        
        # User-Details
        elif command == "get_user":
            if len(sys.argv) < 3:
                print(json.dumps({"error": "get_user benötigt: user"}))
                return
            
            result = client.get_user(sys.argv[2])
            print(json.dumps(result))
        
        # Unbekannter Command
        else:
            print(json.dumps({
                "error": f"Unbekannter Command: {command}",
                "verfügbare_commands": [
                    "signup", "signin", "get_categories", "get_category",
                    "get_difficulties", "get_difficulty", "get_settings",
                    "get_users", "get_user"
                ]
            }))
    
    except Exception as e:
        print(json.dumps({"error": str(e)}))


if __name__ == '__main__':
    main()