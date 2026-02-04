"""
API Client - Kommuniziert mit der externen Quizly API
"""

import os
import json
import requests
from pathlib import Path
from dotenv import load_dotenv

load_dotenv()

class APIClient:
    """
    Einfacher Client für die externe Quizly API
    """
    
    BASE_URL = os.getenv('API_BASE_URL')
    TOKEN_FILE = Path(__file__).parent.parent / '.auth_token'  # Token-Cache-Datei
    
    def __init__(self):
        """Initialisiert den API Client"""
        
        if not self.BASE_URL:
            raise ValueError("API_BASE_URL nicht in .env Datei gefunden!")
        
        self.session = requests.Session()
        self.auth_token = None
        self.token_expiry = None
        
        # 🔥 Token aus Cache laden
        self._load_token()
    
    def _load_token(self):
        """Lädt gespeichertes Token aus Datei"""
        try:
            if self.TOKEN_FILE.exists():
                with open(self.TOKEN_FILE, 'r') as f:
                    data = json.load(f)
                    self.auth_token = data.get('token')
                    self.token_expiry = data.get('expiry')
        except Exception:
            pass
    
    def _save_token(self):
        """Speichert Token in Datei"""
        try:
            with open(self.TOKEN_FILE, 'w') as f:
                json.dump({
                    'token': self.auth_token,
                    'expiry': self.token_expiry
                }, f)
        except Exception:
            pass
    
    def _auth_headers(self):
        """Erstellt Headers mit Auth-Token"""
        if self.auth_token:
            return {"X-Auth-Token": self.auth_token}
        return {}
    
    # ===== USER MANAGEMENT =====
    
    def signup(self, email, nickname, fullname, password):
        """
        Registriert einen neuen Benutzer
        
        Args:
            email (str): Email-Adresse
            nickname (str): Benutzername
            fullname (str): Vollständiger Name
            password (str): Passwort
            
        Returns:
            dict: Response mit User-Daten und EmailValidationToken
        """
        url = f"{self.BASE_URL}/user/{email}/signup"
        
        data = {
            "nickname": nickname,
            "fullname": fullname,
            "password": password
        }
        
        response = self.session.put(url, json=data)
        return response.json()
    
    def signin(self, user, password):
        """
        Loggt einen Benutzer ein
        
        Args:
            user (str): User-ID, Nickname oder Email
            password (str): Passwort
            
        Returns:
            dict: Response mit X-Auth-Token
        """
        url = f"{self.BASE_URL}/user/{user}/signin"
        
        response = self.session.get(url, json={"password": password})
        result = response.json()
        
        # 🔥 Token automatisch speichern
        if "X-Auth-Token" in result:
            self.auth_token = result["X-Auth-Token"]
            self.token_expiry = result.get("Valid_until")
            self._save_token()  # In Datei speichern!
        
        return result
    
    def signout(self, user):
        """
        Loggt einen Benutzer aus
        
        Args:
            user (str): User-ID, Nickname oder Email
            
        Returns:
            dict: Response
        """
        url = f"{self.BASE_URL}/user/{user}/signout"
        
        response = self.session.get(url, headers=self._auth_headers())
        result = response.json()
        
        # 🔥 Token löschen
        self.auth_token = None
        self.token_expiry = None
        if self.TOKEN_FILE.exists():
            self.TOKEN_FILE.unlink()
        
        return result
    
    def verify_email(self, email, token):
        """
        Bestätigt die Email-Adresse
        
        Args:
            email (str): Email-Adresse
            token (str): Validierungs-Token aus der Email
            
        Returns:
            dict: Response
        """
        url = f"{self.BASE_URL}/user/{email}/verify"
        
        response = self.session.get(url, params={"token": token})
        return response.json()
    
    def update_user(self, user, current_password, **updates):
        """
        Aktualisiert User-Profil
        
        Args:
            user (str): User-ID, Nickname oder Email
            current_password (str): Aktuelles Passwort
            **updates: nickname, fullname, email, new_password
            
        Returns:
            dict: Response
        """
        url = f"{self.BASE_URL}/user/{user}"
        
        data = {"current_password": current_password}
        data.update(updates)
        
        response = self.session.patch(url, json=data, headers=self._auth_headers())
        return response.json()
    
    def get_users(self):
        """
        Holt alle User
        
        Returns:
            list: Liste mit User-Objekten
        """
        url = f"{self.BASE_URL}/user"
        
        response = self.session.get(url, headers=self._auth_headers())
        return response.json()
    
    def get_user(self, user):
        """
        Holt Details eines Users
        
        Args:
            user (str): User-ID, Nickname oder Email
            
        Returns:
            dict: User-Objekt
        """
        url = f"{self.BASE_URL}/user/{user}"
        
        response = self.session.get(url, headers=self._auth_headers())
        return response.json()
    
    def request_password_reset(self, email):
        """
        Fordert Passwort-Reset an
        
        Args:
            email (str): Email-Adresse
            
        Returns:
            dict: Response
        """
        url = f"{self.BASE_URL}/user/{email}/password"
        
        response = self.session.get(url)
        return response.json()
    
    # ===== AVATAR MANAGEMENT =====
    
    def upload_avatar(self, user, mime_type, avatar_base64):
        """
        Lädt einen Avatar hoch (Base64-kodiert)
        
        Args:
            user (str): User-ID, Nickname oder Email
            mime_type (str): MIME-Type (z.B. "image/png")
            avatar_base64 (str): Base64-kodierte Bilddaten
            
        Returns:
            dict: Response
        """
        url = f"{self.BASE_URL}/user/{user}/avatar"
        
        data = {
            "mime_type": mime_type,
            "avatar": avatar_base64
        }
        
        response = self.session.put(url, json=data, headers=self._auth_headers())
        return response.json()
    
    def upload_avatar_file(self, user, file_path):
        """
        Lädt einen Avatar hoch (als Datei)
        
        Args:
            user (str): User-ID, Nickname oder Email
            file_path (str): Pfad zur Bilddatei
            
        Returns:
            dict: Response
        """
        url = f"{self.BASE_URL}/user/{user}/avatar"
        
        with open(file_path, 'rb') as f:
            files = {'avatar': f}
            response = self.session.post(url, files=files, headers=self._auth_headers())
        
        return response.json()
    
    def get_avatar(self, user, avatar_type='json'):
        """
        Holt den Avatar eines Users
        
        Args:
            user (str): User-ID, Nickname oder Email
            avatar_type (str): 'json' oder 'raw'
            
        Returns:
            dict: Avatar-Daten (bei json) oder Binärdaten (bei raw)
        """
        url = f"{self.BASE_URL}/user/{user}/avatar"
        
        params = {"type": avatar_type}
        response = self.session.get(url, params=params, headers=self._auth_headers())
        
        if avatar_type == 'json':
            return response.json()
        else:
            return response.content
    
    # ===== KATEGORIEN =====
    
    def get_categories(self, number=None):
        """
        Holt alle verfügbaren Kategorien
        
        Args:
            number (int, optional): Anzahl zufälliger Kategorien
            
        Returns:
            list: Liste mit Kategorie-Objekten
        """
        url = f"{self.BASE_URL}/category"
        
        params = {"number": number} if number else {}
        response = self.session.get(url, params=params, headers=self._auth_headers())
        return response.json()
    
    def get_category(self, category_id):
        """
        Holt Details einer Kategorie
        
        Args:
            category_id (int): Kategorie-ID
            
        Returns:
            dict: Kategorie-Objekt
        """
        url = f"{self.BASE_URL}/category/{category_id}"
        
        response = self.session.get(url, headers=self._auth_headers())
        return response.json()
    
    # ===== SCHWIERIGKEITSGRADE =====
    
    def get_difficulties(self):
        """
        Holt alle Schwierigkeitsgrade
        
        Returns:
            list: Liste mit Difficulty-Objekten
        """
        url = f"{self.BASE_URL}/difficulty"
        
        response = self.session.get(url, headers=self._auth_headers())
        return response.json()
    
    def get_difficulty(self, difficulty_id):
        """
        Holt Details eines Schwierigkeitsgrades
        
        Args:
            difficulty_id (int): Difficulty-ID
            
        Returns:
            dict: Difficulty-Objekt
        """
        url = f"{self.BASE_URL}/difficulty/{difficulty_id}"
        
        response = self.session.get(url, headers=self._auth_headers())
        return response.json()
    
    # ===== SETTINGS =====
    
    def get_settings(self):
        """
        Holt die Spieleinstellungen
        
        Returns:
            dict: Settings-Objekt
        """
        url = f"{self.BASE_URL}/setting"
        
        response = self.session.get(url, headers=self._auth_headers())
        return response.json()
    
    # ===== GAME MODES =====
    
    def get_game_modes(self):
        """
        Holt alle Game-Modes
        
        Returns:
            list: Liste mit Game-Mode-Objekten
        """
        url = f"{self.BASE_URL}/gamemode"
        
        response = self.session.get(url, headers=self._auth_headers())
        return response.json()
    
    def get_game_mode(self, mode_id):
        """
        Holt Details eines Game-Modes
        
        Args:
            mode_id (int): Game-Mode-ID
            
        Returns:
            dict: Game-Mode-Objekt
        """
        url = f"{self.BASE_URL}/gamemode/{mode_id}"
        
        response = self.session.get(url, headers=self._auth_headers())
        return response.json()
    
    # ===== GAME STATES =====
    
    def get_game_states(self):
        """
        Holt alle Game-States
        
        Returns:
            list: Liste mit Game-State-Objekten
        """
        url = f"{self.BASE_URL}/gamestate"
        
        response = self.session.get(url, headers=self._auth_headers())
        return response.json()
    
    def get_game_state(self, state_id):
        """
        Holt Details eines Game-States
        
        Args:
            state_id (int): Game-State-ID
            
        Returns:
            dict: Game-State-Objekt
        """
        url = f"{self.BASE_URL}/gamestate/{state_id}"
        
        response = self.session.get(url, headers=self._auth_headers())
        return response.json()
    
    # ===== GAMEPLAY - MATCH MANAGEMENT =====
    
    def get_random_user(self):
        """
        Holt einen zufälligen User (für Matchmaking)
        
        Returns:
            dict: User-Objekt
        """
        url = f"{self.BASE_URL}/user/random"
        
        response = self.session.get(url, headers=self._auth_headers())
        return response.json()
    
    def get_user_matches(self, user, match_type='all', limit=None):
        """
        Holt alle Matches eines Users
        
        Args:
            user (str): User-ID, Nickname oder Email
            match_type (str): 'all', 'running', 'ended', 'running/single', 'running/duel', 'ended/single', 'ended/duel'
            limit (int, optional): Maximale Anzahl Ergebnisse
            
        Returns:
            list: Liste mit Match-Objekten
        """
        if match_type == 'all':
            url = f"{self.BASE_URL}/user/{user}/match"
        else:
            url = f"{self.BASE_URL}/user/{user}/match/{match_type}"
        
        params = {"last": limit} if limit else {}
        response = self.session.get(url, params=params, headers=self._auth_headers())
        return response.json()
    
    # ===== GAMEPLAY - MATCH CREATION (UPDATED!) =====
    
    def create_single_match(self, user_id, difficulty):
        """
        Erstellt ein neues Single-Player-Spiel
        
        Args:
            user_id (int): ID des Spielers
            difficulty (int): Schwierigkeitsgrad (1-5)
            
        Returns:
            dict: Response mit match_id
        """
        url = f"{self.BASE_URL}/user/{user_id}/match/single"
        
        data = {"difficulty": difficulty}
        
        response = self.session.post(url, json=data, headers=self._auth_headers())
        return response.json()
    
    def create_duel_match(self, user_id, difficulty, opponent_ids):
        """
        Erstellt ein neues Multiplayer-Duell
        
        Args:
            user_id (int): ID des Spielers
            difficulty (int): Schwierigkeitsgrad (1-5)
            opponent_ids (list): Liste der Gegner-IDs
            
        Returns:
            dict: Response mit match_id
        """
        url = f"{self.BASE_URL}/user/{user_id}/match/duel"
        
        data = {
            "difficulty": difficulty,
            "opponents": opponent_ids
        }
        
        response = self.session.post(url, json=data, headers=self._auth_headers())
        return response.json()
    
    # ===== GAMEPLAY - SPEKULATIV (NOCH NICHT DOKUMENTIERT!) =====
    
    def get_match_details(self, match_id):
        """⚠️ SPEKULATIV - Nicht in API-Doku!"""
        url = f"{self.BASE_URL}/match/{match_id}"
        response = self.session.get(url, headers=self._auth_headers())
        return response.json()
    
    def get_round_questions(self, match_id, round_number):
        """⚠️ SPEKULATIV - Nicht in API-Doku!"""
        url = f"{self.BASE_URL}/match/{match_id}/round/{round_number}/questions"
        response = self.session.get(url, headers=self._auth_headers())
        return response.json()
    
    def submit_answer(self, match_id, question_id, answer_id, time_taken=None):
        """⚠️ SPEKULATIV - Nicht in API-Doku!"""
        url = f"{self.BASE_URL}/match/{match_id}/answer"
        data = {
            "question_id": question_id,
            "answer_id": answer_id
        }
        if time_taken:
            data["time_taken"] = time_taken
        response = self.session.post(url, json=data, headers=self._auth_headers())
        return response.json()
    
    def end_turn(self, match_id):
        """⚠️ SPEKULATIV - Nicht in API-Doku!"""
        url = f"{self.BASE_URL}/match/{match_id}/end_turn"
        response = self.session.post(url, headers=self._auth_headers())
        return response.json()
    
    def invite_friend(self, friend, message=None):
        """⚠️ SPEKULATIV - Nicht in API-Doku!"""
        url = f"{self.BASE_URL}/invite"
        data = {"friend": friend}
        if message:
            data["message"] = message
        response = self.session.post(url, json=data, headers=self._auth_headers())
        return response.json()
    
    def accept_invite(self, invite_id):
        """⚠️ SPEKULATIV - Nicht in API-Doku!"""
        url = f"{self.BASE_URL}/invite/{invite_id}/accept"
        response = self.session.post(url, headers=self._auth_headers())
        return response.json()
    
    def get_invites(self):
        """⚠️ SPEKULATIV - Nicht in API-Doku!"""
        url = f"{self.BASE_URL}/invite"
        response = self.session.get(url, headers=self._auth_headers())
        return response.json()
    
    def get_leaderboard(self, limit=10, period='all'):
        """⚠️ SPEKULATIV - Nicht in API-Doku!"""
        url = f"{self.BASE_URL}/leaderboard"
        params = {"limit": limit, "period": period}
        response = self.session.get(url, params=params, headers=self._auth_headers())
        return response.json()
