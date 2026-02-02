from datetime import datetime

class Spieler:
    """Repräsentiert einen Spieler/Benutzer"""
    
    def __init__(self, user_id, nickname, email, fullname=None, 
                 password=None, email_confirmed=False, 
                 email_validation_token=None, auth_token=None,
                 token_valid_until=None, last_login=None, avatar=None):
        self.user_id = user_id
        self.nickname = nickname
        self.fullname = fullname
        self.email = email
        self.password = password 
        self.email_confirmed = email_confirmed
        self.email_validation_token = email_validation_token
        self.auth_token = auth_token  
        self.token_valid_until = token_valid_until 
        self.last_login = last_login
        self.avatar = avatar 
    
    def to_dict(self, include_sensitive=False):
        """Konvertiert zu Dictionary (ohne Passwort)"""
        data = {
            'user_id': self.user_id,
            'nickname': self.nickname,
            'fullname': self.fullname,
            'email': self.email
        }
        
        if include_sensitive:
            data.update({
                'email_confirmed': self.email_confirmed,
                'last_login': self.last_login.isoformat() if self.last_login else None
            })
        
        return data
    
    def is_token_valid(self):
        """Prüft ob Auth-Token noch gültig ist"""
        if not self.token_valid_until:
            return False
        return datetime.now().timestamp() < self.token_valid_until

