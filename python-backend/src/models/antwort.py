class Antwort:
    """Repräsentiert eine Antwort zu einer Frage"""
    
    def __init__(self, id, question, text, correct_answer):
        self.id = id
        self.question = question
        self.text = text
        self.correct_answer = correct_answer  # 0 = falsch, 1 = richtig
    
    def is_correct(self):
        """Prüft ob die Antwort korrekt ist"""
        return self.correct_answer == 1
    
    def to_dict(self):
        return {
            'id': self.id,
            'question': self.question,
            'text': self.text,
            'correct_answer': self.correct_answer
        }