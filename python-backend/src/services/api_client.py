"""
API Client - Kommuniziert mit der externen Quizly API
"""

import requests
import os
from dotenv import load_dotenv

load_dotenv()

class APIClient:
    """
    Einfacher Client für die externe Quizly API
    """
    
    # BASE_URL NUR aus .env holen (kein Fallback mehr!)
    BASE_URL = os.getenv('API_BASE_URL')
    
    def __init__(self):
        """Initialisiert den API Client"""
        # Prüfen ob BASE_URL gesetzt ist
        if not self.BASE_URL:
            raise ValueError("API_BASE_URL nicht in .env Datei gefunden!")
        
        self.session = requests.Session()
        self.auth_token = None
    
    # ===== USER MANAGEMENT =====
    
    def signup(self, email, nickname, fullname, password):
        """
        Registriert einen neuen Benutzer
        
        Args:
            email (str): Email-Adresse des Users
            nickname (str): Benutzername
            fullname (str): Vollständiger Name
            password (str): Passwort
            
        Returns:
            dict: Response mit Message, Email, Nickname, etc.
            
        Example:
            >>> client.signup("test@test.de", "max", "Max Mustermann", "Pass123")
            {'Message': 'User created successfully', 'Email': 'test@test.de', ...}
        """
        url = f"{self.BASE_URL}/user/{email}/signup"
        
        response = self.session.put(url, json={
            "nickname": nickname,
            "fullname": fullname,
            "password": password
        })
        
        return response.json()
    
    def signin(self, user, password):
        """
        Loggt einen Benutzer ein
        
        Args:
            user (str): User-ID, Nickname oder Email
            password (str): Passwort
            
        Returns:
            dict: Response mit Auth-Token und Gültigkeit
            
        Example:
            >>> client.signin("test@test.de", "Pass123")
            {'Message': 'Sign in successful', 'X-Auth-Token': '...', 'Valid_until': '...'}
        """
        url = f"{self.BASE_URL}/user/{user}/signin"
        
        response = self.session.get(url, json={"password": password})
        data = response.json()
        
        # Auth-Token für weitere Requests speichern
        if "X-Auth-Token" in data:
            self.auth_token = data["X-Auth-Token"]
            # Token auch in Session-Headers setzen
            self.session.headers.update({"X-Auth-Token": self.auth_token})
        
        return data
    
    def signout(self, user):
        """
        Loggt einen Benutzer aus
        
        Args:
            user (str): User-ID, Nickname oder Email
            
        Returns:
            dict: Response mit Message
        """
        url = f"{self.BASE_URL}/user/{user}/signout"
        response = self.session.get(url, headers=self._auth_headers())
        
        # Token löschen
        self.auth_token = None
        if "X-Auth-Token" in self.session.headers:
            del self.session.headers["X-Auth-Token"]
        
        return response.json()
    
    def get_users(self):
        """
        Holt Liste aller Benutzer
        
        Returns:
            list: Array mit User-Objekten
            
        Example:
            >>> client.get_users()
            [{'user_id': 1, 'nickname': 'max', 'email': '...'}, ...]
        """
        url = f"{self.BASE_URL}/user"
        response = self.session.get(url, headers=self._auth_headers())
        return response.json()
    
    def get_user(self, user):
        """
        Holt Details für einen spezifischen User
        
        Args:
            user (str): User-ID, Nickname oder Email
            
        Returns:
            dict: User-Details
        """
        url = f"{self.BASE_URL}/user/{user}"
        response = self.session.get(url, headers=self._auth_headers())
        return response.json()
    
    def update_user(self, user, current_password, **updates):
        """
        Aktualisiert User-Profil
        
        Args:
            user (str): User-ID, Nickname oder Email
            current_password (str): Aktuelles Passwort (Pflicht!)
            **updates: nickname, fullname, email, new_password
            
        Returns:
            dict: Response mit Message
        """
        url = f"{self.BASE_URL}/user/{user}"
        
        data = {"current_password": current_password}
        data.update(updates)
        
        response = self.session.patch(url, json=data, headers=self._auth_headers())
        return response.json()
    
    def verify_email(self, email, token):
        """
        Bestätigt die Email-Adresse eines Benutzers
        
        Args:
            email (str): Email-Adresse
            token (str): Bestätigungstoken aus der Email
            
        Returns:
            Response: HTML-Seite (Erfolg/Misserfolg)
        """
        url = f"{self.BASE_URL}/user/{email}/verify"
        response = self.session.get(url, params={"token": token})
        return response
    
    def upload_avatar(self, user, mime_type, avatar_base64):
        """
        Speichert Avatar als JSON mit Base64-kodierten Bilddaten
        
        Args:
            user (str): User-ID, Nickname oder Email
            mime_type (str): MIME-Type des Bildes (z.B. 'image/png')
            avatar_base64 (str): Base64-kodierte Bilddaten
            
        Returns:
            dict: Response mit Message
        """
        url = f"{self.BASE_URL}/user/{user}/avatar"
        
        response = self.session.put(url, json={
            "mime_type": mime_type,
            "avatar": avatar_base64
        }, headers=self._auth_headers())
        
        return response.json()
    
    def upload_avatar_file(self, user, image_file):
        """
        Speichert Avatar als Multipart-Upload
        
        Args:
            user (str): User-ID, Nickname oder Email
            image_file: Datei-Objekt oder Pfad zur Bilddatei
            
        Returns:
            dict: Response mit Message
        """
        url = f"{self.BASE_URL}/user/{user}/avatar"
        
        # Wenn Pfad übergeben wird, Datei öffnen
        if isinstance(image_file, str):
            with open(image_file, 'rb') as f:
                files = {'avatar': f}
                response = self.session.post(url, files=files, headers=self._auth_headers())
        else:
            files = {'avatar': image_file}
            response = self.session.post(url, files=files, headers=self._auth_headers())
        
        return response.json()
    
    def get_avatar(self, user, avatar_type='json'):
        """
        Holt Avatar eines Benutzers
        
        Args:
            user (str): User-ID, Nickname oder Email
            avatar_type (str): 'json' für Base64-JSON oder 'raw' für Bilddaten
            
        Returns:
            dict oder Response: JSON-Objekt mit Base64-Daten oder Bild-Response
        """
        url = f"{self.BASE_URL}/user/{user}/avatar"
        response = self.session.get(url, params={"type": avatar_type}, headers=self._auth_headers())
        
        if avatar_type == 'json':
            return response.json()
        else:
            return response  # Raw image Response
    
    def request_password_reset(self, email):
        """
        Fordert Passwort-Reset an (sendet Email)
        
        Args:
            email (str): Email-Adresse des Users
            
        Returns:
            dict: Response mit Message
        """
        url = f"{self.BASE_URL}/user/{email}/password"
        response = self.session.get(url)
        return response.json()
    
    # ===== KATEGORIEN =====
    
    def get_categories(self, number=None):
        """
        Holt alle verfügbaren Kategorien
        
        Args:
            number (int, optional): Anzahl der zufällig ausgewählten Kategorien
        
        Returns:
            list: Array mit Kategorie-Objekten
            
        Example:
            >>> client.get_categories()
            [{'id': 1, 'name': 'Sport'}, {'id': 2, 'name': 'Geschichte'}, ...]
            >>> client.get_categories(number=3)
            [{'id': 4, 'name': 'Comics'}, {'id': 7, 'name': 'Musik'}, ...]
        """
        url = f"{self.BASE_URL}/category"
        params = {"number": number} if number else {}
        response = self.session.get(url, params=params, headers=self._auth_headers())
        return response.json()
    
    def get_category(self, category_id):
        """
        Holt eine spezifische Kategorie
        
        Args:
            category_id (int): ID der Kategorie
            
        Returns:
            dict: Kategorie-Details
            
        Example:
            >>> client.get_category(4)
            {'id': 4, 'name': 'Comics und Märchen'}
        """
        url = f"{self.BASE_URL}/category/{category_id}"
        response = self.session.get(url, headers=self._auth_headers())
        return response.json()
    
    # ===== SCHWIERIGKEITSGRADE =====
    
    def get_difficulties(self):
        """
        Holt alle Schwierigkeitsgrade
        
        Returns:
            list: Array mit Schwierigkeitsgrad-Objekten
            
        Example:
            >>> client.get_difficulties()
            [{'level': 1, 'description': 'Sehr leicht'}, ...]
        """
        url = f"{self.BASE_URL}/difficulty"
        response = self.session.get(url, headers=self._auth_headers())
        return response.json()
    
    def get_difficulty(self, difficulty_id):
        """
        Holt einen spezifischen Schwierigkeitsgrad
        
        Args:
            difficulty_id (int): ID des Schwierigkeitsgrads
            
        Returns:
            dict: Schwierigkeitsgrad-Details
            
        Example:
            >>> client.get_difficulty(3)
            {'level': 3, 'description': 'Sehr leicht'}
        """
        url = f"{self.BASE_URL}/difficulty/{difficulty_id}"
        response = self.session.get(url, headers=self._auth_headers())
        return response.json()
    
    # ===== SETTINGS =====
    
    def get_settings(self):
        """
        Holt die Spieleinstellungen
        
        Returns:
            dict: Settings-Objekt mit allen Spielparametern
            
        Example:
            >>> client.get_settings()
            {
                'turns_per_game': 6,
                'questions_per_turn': 3,
                'answers_per_question': 4,
                ...
            }
        """
        url = f"{self.BASE_URL}/setting"
        response = self.session.get(url, headers=self._auth_headers())
        return response.json()
    
    # ===== GAME MODES =====
    
    def get_game_modes(self):
        """
        Holt alle verfügbaren Spiel-Modi
        
        Returns:
            list: Array mit Game-Mode-Objekten
            
        Example:
            >>> client.get_game_modes()
            [
                {'game_mode_id': 1, 'game_mode': 'Einzelspiel'},
                {'game_mode_id': 2, 'game_mode': 'Duell'}
            ]
        """
        url = f"{self.BASE_URL}/gamemode"
        response = self.session.get(url, headers=self._auth_headers())
        return response.json()
    
    def get_game_mode(self, mode_id):
        """
        Holt einen spezifischen Game-Mode
        
        Args:
            mode_id (int): ID des Game-Modes
            
        Returns:
            dict: Game-Mode-Details
            
        Example:
            >>> client.get_game_mode(2)
            {'game_mode_id': 2, 'game_mode': 'Duell'}
        """
        url = f"{self.BASE_URL}/gamemode/{mode_id}"
        response = self.session.get(url, headers=self._auth_headers())
        return response.json()
    
    # ===== GAME STATES =====
    
    def get_game_states(self):
        """
        Holt alle verfügbaren Spiel-Zustände
        
        Returns:
            list: Array mit Game-State-Objekten
        """
        url = f"{self.BASE_URL}/gamestate"
        response = self.session.get(url, headers=self._auth_headers())
        return response.json()
    
    def get_game_state(self, state_id):
        """
        Holt einen spezifischen Game-State
        
        Args:
            state_id (int): ID des Game-States
            
        Returns:
            dict: Game-State-Details
            
        Example:
            >>> client.get_game_state(3)
            {'game_state_id': 3, 'game_state': 'waiting'}
        """
        url = f"{self.BASE_URL}/gamestate/{state_id}"
        response = self.session.get(url, headers=self._auth_headers())
        return response.json()
    
    # ===== GAMEPLAY =====
    
    def get_random_user(self):
        """
        Holt einen zufällig ausgewählten Benutzer
        Nützlich für die zufällige Auswahl eines Gegners im Duell-Modus
        
        Returns:
            dict: Zufällig ausgewählter User
        """
        url = f"{self.BASE_URL}/user/random"
        response = self.session.get(url, headers=self._auth_headers())
        return response.json()
    
    def get_user_matches(self, user, match_type='all', limit=None):
        """
        Holt Spiele eines Benutzers
        
        Args:
            user (str): User-ID, Nickname oder Email
            match_type (str): 'all', 'running', 'running/single', 'running/duel', 
                             'ended', 'ended/single', 'ended/duel'
            limit (int, optional): Begrenzt Anzahl der Ergebnisse
            
        Returns:
            list: Array mit Match-Objekten
            
        Example:
            >>> client.get_user_matches(1, 'running/duel', limit=5)
            [...]
        """
        if match_type == 'all':
            url = f"{self.BASE_URL}/user/{user}/match"
        else:
            url = f"{self.BASE_URL}/user/{user}/match/{match_type}"
        
        params = {"last": limit} if limit else {}
        response = self.session.get(url, params=params, headers=self._auth_headers())
        return response.json()
    
    def create_match(self, match_data):
        """
        Erstellt ein neues Spiel
        
        Args:
            match_data (dict): Spiel-Konfigurationsdaten
            
        Returns:
            dict: Neu erstelltes Match-Objekt
            
        Note:
            Die genaue Struktur von match_data muss noch spezifiziert werden
        """
        url = f"{self.BASE_URL}/match"
        response = self.session.post(url, json=match_data, headers=self._auth_headers())
        return response.json()
    
    # ===== HELPER METHODS =====
    
    def _auth_headers(self):
        """
        Erstellt Headers mit Auth-Token
        
        Returns:
            dict: Headers-Dictionary mit X-Auth-Token
        """
        if self.auth_token:
            return {"X-Auth-Token": self.auth_token}
        return {}
