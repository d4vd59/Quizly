class Frage:
    """Repräsentiert eine Quiz-Frage"""
    
    def __init__(self, id, text, category, difficulty, 
                 answered_correctly=0, answered_incorrectly=0):
        self.id = id
        self.text = text
        self.category = category
        self.difficulty = difficulty
        self.answered_correctly = answered_correctly
        self.answered_incorrectly = answered_incorrectly
        self.answers = []
    
    def add_answer(self, answer):
        """Fügt eine Antwort zur Frage hinzu"""
        self.answers.append(answer)
    
    def to_dict(self):
        return {
            'id': self.id,
            'text': self.text,
            'category': self.category,
            'difficulty': self.difficulty,
            'answered_correctly': self.answered_correctly,
            'answered_incorrectly': self.answered_incorrectly,
            'answers': [a.to_dict() for a in self.answers]
        }