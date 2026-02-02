class Settings:
    """Repräsentiert globale Spieleinstellungen"""
    
    def __init__(self, turns_per_game=6, questions_per_turn=3, 
                 answers_per_question=4, max_time_game=864000,
                 max_time_turn=432000, max_time_question=30,
                 points_per_question=1, question_repetition_per_game=1):
        self.turns_per_game = turns_per_game
        self.questions_per_turn = questions_per_turn
        self.answers_per_question = answers_per_question
        self.max_time_game = max_time_game  
        self.max_time_turn = max_time_turn
        self.max_time_question = max_time_question  
        self.points_per_question = points_per_question
        self.question_repetition_per_game = question_repetition_per_game
    
    def to_dict(self):
        return {
            'turns_per_game': self.turns_per_game,
            'questions_per_turn': self.questions_per_turn,
            'answers_per_question': self.answers_per_question,
            'max_time_game': self.max_time_game,
            'max_time_turn': self.max_time_turn,
            'max_time_question': self.max_time_question,
            'points_per_question': self.points_per_question,
            'question_repetition_per_game': self.question_repetition_per_game
        }