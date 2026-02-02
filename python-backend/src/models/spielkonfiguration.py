class Spielkonfiguration:
    """Repräsentiert eine Spielkonfiguration"""
    
    def __init__(self, config_id, config_name, anzahl_runden, 
                 fragen_pro_runde, antworten_pro_frage, 
                 max_wiederholungen_frage, max_spiellaenge_sekunden, 
                 ist_aktiv=1):
        self.config_id = config_id
        self.config_name = config_name
        self.anzahl_runden = anzahl_runden
        self.fragen_pro_runde = fragen_pro_runde
        self.antworten_pro_frage = antworten_pro_frage
        self.max_wiederholungen_frage = max_wiederholungen_frage
        self.max_spiellaenge_sekunden = max_spiellaenge_sekunden
        self.ist_aktiv = ist_aktiv
    
    def to_dict(self):
        return {
            'config_id': self.config_id,
            'config_name': self.config_name,
            'anzahl_runden': self.anzahl_runden,
            'fragen_pro_runde': self.fragen_pro_runde,
            'antworten_pro_frage': self.antworten_pro_frage,
            'max_wiederholungen_frage': self.max_wiederholungen_frage,
            'max_spiellaenge_sekunden': self.max_spiellaenge_sekunden,
            'ist_aktiv': self.ist_aktiv
        }