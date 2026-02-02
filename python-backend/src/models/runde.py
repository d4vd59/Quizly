from datetime import datetime

class Runde:
    """Repräsentiert eine Spielrunde"""
    
    def __init__(self, runde_id, spiel_id, runden_nummer, 
                 kategorie_id, kategorie_name=None, 
                 start_zeit=None, end_zeit=None):
        self.runde_id = runde_id
        self.spiel_id = spiel_id
        self.runden_nummer = runden_nummer
        self.kategorie_id = kategorie_id
        self.kategorie_name = kategorie_name
        self.start_zeit = start_zeit or datetime.now()
        self.end_zeit = end_zeit
    
    def to_dict(self):
        return {
            'runde_id': self.runde_id,
            'spiel_id': self.spiel_id,
            'runden_nummer': self.runden_nummer,
            'kategorie_id': self.kategorie_id,
            'kategorie_name': self.kategorie_name,
            'start_zeit': self.start_zeit.isoformat() if self.start_zeit else None,
            'end_zeit': self.end_zeit.isoformat() if self.end_zeit else None
        }