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
    
    # ===== KATEGORIEN =====
    
    def get_categories(self):
        """
        Holt alle verfügbaren Kategorien
        
        Returns:
            list: Array mit Kategorie-Objekten
            
        Example:
            >>> client.get_categories()
            [{'id': 1, 'name': 'Sport'}, {'id': 2, 'name': 'Geschichte'}, ...]
        """
        url = f"{self.BASE_URL}/category"
        response = self.session.get(url, headers=self._auth_headers())
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
