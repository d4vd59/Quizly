class Schwierigkeitsgrad:
    """Repräsentiert einen Schwierigkeitsgrad"""
    
    def __init__(self, level, description):
        self.level = level 
        self.description = description 
    
    def to_dict(self):
        return {
            'level': self.level,
            'description': self.description
        }