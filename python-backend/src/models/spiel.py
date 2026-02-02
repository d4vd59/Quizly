from datetime import datetime

class Spiel:
    """Repräsentiert ein Quiz-Spiel"""
    
    def __init__(self, spiel_id, spielmodus, schwierigkeitsgrad_level, 
                 config_id, status='laufend', start_zeit=None, 
                 end_zeit=None, spieldauer_sekunden=None):
        self.spiel_id = spiel_id
        self.spielmodus = spielmodus  # 'einzelmodus' oder 'duellmodus'
        self.schwierigkeitsgrad_level = schwierigkeitsgrad_level
        self.config_id = config_id
        self.start_zeit = start_zeit or datetime.now()
        self.end_zeit = end_zeit
        self.spieldauer_sekunden = spieldauer_sekunden
        self.status = status  # 'laufend', 'beendet', 'abgebrochen'
        self.spieler = []
        self.runden = []
    
    def to_dict(self):
        return {
            'spiel_id': self.spiel_id,
            'spielmodus': self.spielmodus,
            'schwierigkeitsgrad_level': self.schwierigkeitsgrad_level,
            'config_id': self.config_id,
            'start_zeit': self.start_zeit.isoformat() if self.start_zeit else None,
            'end_zeit': self.end_zeit.isoformat() if self.end_zeit else None,
            'spieldauer_sekunden': self.spieldauer_sekunden,
            'status': self.status
        }