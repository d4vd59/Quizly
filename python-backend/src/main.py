"""
Quizly Backend - Einfacher API Wrapper

Wird von C# aufgerufen und kommuniziert mit der externen API.
"""

import sys
import json
import base64
from services.api_client import APIClient


def main():
    """Hauptfunktion - wird von C# aufgerufen"""
    
    if len(sys.argv) < 2:
        print(json.dumps({"error": "Kein Command angegeben"}))
        return
    
    command = sys.argv[1]
    client = APIClient()
    
    try:
        # ===== USER MANAGEMENT =====
        
        if command == "signup":
            if len(sys.argv) < 6:
                print(json.dumps({"error": "signup benötigt: email nickname fullname password"}))
                return
            
            result = client.signup(
                email=sys.argv[2],
                nickname=sys.argv[3],
                fullname=sys.argv[4],
                password=sys.argv[5]
            )
            print(json.dumps(result))
        
        elif command == "signin":
            if len(sys.argv) < 4:
                print(json.dumps({"error": "signin benötigt: user password"}))
                return
            
            result = client.signin(
                user=sys.argv[2],
                password=sys.argv[3]
            )
            print(json.dumps(result))
        
        elif command == "signout":
            if len(sys.argv) < 3:
                print(json.dumps({"error": "signout benötigt: user"}))
                return
            
            result = client.signout(sys.argv[2])
            print(json.dumps(result))
        
        elif command == "update_user":
            # update_user <user> <current_password> [nickname=x] [fullname=y] [email=z] [new_password=w]
            if len(sys.argv) < 4:
                print(json.dumps({"error": "update_user benötigt: user current_password [updates...]"}))
                return
            
            user = sys.argv[2]
            current_password = sys.argv[3]
            updates = {}
            
            # Parse optionale Updates (format: key=value)
            for arg in sys.argv[4:]:
                if '=' in arg:
                    key, value = arg.split('=', 1)
                    updates[key] = value
            
            result = client.update_user(user, current_password, **updates)
            print(json.dumps(result))
        
        elif command == "get_users":
            result = client.get_users()
            print(json.dumps(result))
        
        elif command == "get_user":
            if len(sys.argv) < 3:
                print(json.dumps({"error": "get_user benötigt: user"}))
                return
            
            result = client.get_user(sys.argv[2])
            print(json.dumps(result))
        
        # ===== KATEGORIEN =====
        
        elif command == "get_categories":
            # Optional: get_categories <number>
            number = int(sys.argv[2]) if len(sys.argv) > 2 else None
            result = client.get_categories(number=number)
            print(json.dumps(result))
        
        elif command == "get_category":
            if len(sys.argv) < 3:
                print(json.dumps({"error": "get_category benötigt: category_id"}))
                return
            
            result = client.get_category(int(sys.argv[2]))
            print(json.dumps(result))
        
        # ===== SCHWIERIGKEITSGRADE =====
        
        elif command == "get_difficulties":
            result = client.get_difficulties()
            print(json.dumps(result))
        
        elif command == "get_difficulty":
            if len(sys.argv) < 3:
                print(json.dumps({"error": "get_difficulty benötigt: difficulty_id"}))
                return
            
            result = client.get_difficulty(int(sys.argv[2]))
            print(json.dumps(result))
        
        # ===== SETTINGS =====
        
        elif command == "get_settings":
            result = client.get_settings()
            print(json.dumps(result))
        
        # ===== GAME MODES =====
        
        elif command == "get_game_modes":
            result = client.get_game_modes()
            print(json.dumps(result))
        
        elif command == "get_game_mode":
            if len(sys.argv) < 3:
                print(json.dumps({"error": "get_game_mode benötigt: mode_id"}))
                return
            
            result = client.get_game_mode(int(sys.argv[2]))
            print(json.dumps(result))
        
        # ===== GAME STATES =====
        
        elif command == "get_game_states":
            result = client.get_game_states()
            print(json.dumps(result))
        
        elif command == "get_game_state":
            if len(sys.argv) < 3:
                print(json.dumps({"error": "get_game_state benötigt: state_id"}))
                return
            
            result = client.get_game_state(int(sys.argv[2]))
            print(json.dumps(result))
        
        # ===== GAMEPLAY =====
        
        elif command == "get_random_user":
            result = client.get_random_user()
            print(json.dumps(result))
        
        elif command == "get_user_matches":
            # get_user_matches <user> [match_type] [limit]
            if len(sys.argv) < 3:
                print(json.dumps({"error": "get_user_matches benötigt: user [match_type] [limit]"}))
                return
            
            user = sys.argv[2]
            match_type = sys.argv[3] if len(sys.argv) > 3 else 'all'
            limit = int(sys.argv[4]) if len(sys.argv) > 4 else None
            
            result = client.get_user_matches(user, match_type, limit)
            print(json.dumps(result))
        
        # ===== AVATAR MANAGEMENT =====
        
        elif command == "upload_avatar":
            # upload_avatar <user> <image_path>
            if len(sys.argv) < 4:
                print(json.dumps({"error": "upload_avatar benötigt: user image_path"}))
                return
            
            user = sys.argv[2]
            image_path = sys.argv[3]
            
            # Bild laden und Base64 kodieren
            try:
                with open(image_path, 'rb') as f:
                    image_data = f.read()
                    base64_data = base64.b64encode(image_data).decode('utf-8')
                
                # MIME-Type aus Dateiendung ermitteln
                if image_path.lower().endswith('.png'):
                    mime_type = 'image/png'
                elif image_path.lower().endswith('.jpg') or image_path.lower().endswith('.jpeg'):
                    mime_type = 'image/jpeg'
                elif image_path.lower().endswith('.gif'):
                    mime_type = 'image/gif'
                else:
                    mime_type = 'image/png'  # Default
                
                result = client.upload_avatar(user, mime_type, base64_data)
                print(json.dumps(result))
            
            except FileNotFoundError:
                print(json.dumps({"error": f"Datei nicht gefunden: {image_path}"}))
            except Exception as e:
                print(json.dumps({"error": f"Fehler beim Lesen der Datei: {str(e)}"}))
        
        elif command == "get_avatar":
            # get_avatar <user> [type]
            if len(sys.argv) < 3:
                print(json.dumps({"error": "get_avatar benötigt: user [type=json|raw]"}))
                return
            
            user = sys.argv[2]
            avatar_type = sys.argv[3] if len(sys.argv) > 3 else 'json'
            
            result = client.get_avatar(user, avatar_type)
            print(json.dumps(result))
        
        # ===== GAMEPLAY - UPDATED! =====
        
        elif command == "create_single_match":
            # create_single_match <user_id> <difficulty>
            if len(sys.argv) < 4:
                print(json.dumps({"error": "create_single_match benötigt: user_id difficulty"}))
                return
            
            user_id = int(sys.argv[2])
            difficulty = int(sys.argv[3])
            
            result = client.create_single_match(user_id, difficulty)
            print(json.dumps(result))
        
        elif command == "create_duel_match":
            # create_duel_match <user_id> <difficulty> <opponent_ids>
            # Beispiel: create_duel_match 16 3 "[1,2,3]"
            if len(sys.argv) < 5:
                print(json.dumps({"error": "create_duel_match benötigt: user_id difficulty opponent_ids"}))
                return
            
            user_id = int(sys.argv[2])
            difficulty = int(sys.argv[3])
            opponent_ids = json.loads(sys.argv[4])  # "[1,2,3]" -> [1,2,3]
            
            result = client.create_duel_match(user_id, difficulty, opponent_ids)
            print(json.dumps(result))
        
        elif command == "get_match_details":
            if len(sys.argv) < 3:
                print(json.dumps({"error": "get_match_details benötigt: match_id"}))
                return
            
            result = client.get_match_details(int(sys.argv[2]))
            print(json.dumps(result))
        
        elif command == "get_round_questions":
            if len(sys.argv) < 4:
                print(json.dumps({"error": "get_round_questions benötigt: match_id round_number"}))
                return
            
            result = client.get_round_questions(int(sys.argv[2]), int(sys.argv[3]))
            print(json.dumps(result))
        
        elif command == "submit_answer":
            # submit_answer <match_id> <question_id> <answer_id> [time_taken]
            if len(sys.argv) < 5:
                print(json.dumps({"error": "submit_answer benötigt: match_id question_id answer_id [time_taken]"}))
                return
            
            match_id = int(sys.argv[2])
            question_id = int(sys.argv[3])
            answer_id = int(sys.argv[4])
            time_taken = int(sys.argv[5]) if len(sys.argv) > 5 else None
            
            result = client.submit_answer(match_id, question_id, answer_id, time_taken)
            print(json.dumps(result))
        
        elif command == "end_turn":
            if len(sys.argv) < 3:
                print(json.dumps({"error": "end_turn benötigt: match_id"}))
                return
            
            result = client.end_turn(int(sys.argv[2]))
            print(json.dumps(result))
        
        elif command == "invite_friend":
            # invite_friend <friend_email_or_id> [message]
            if len(sys.argv) < 3:
                print(json.dumps({"error": "invite_friend benötigt: friend_email_or_id [message]"}))
                return
            
            friend = sys.argv[2]
            message = sys.argv[3] if len(sys.argv) > 3 else None
            
            result = client.invite_friend(friend, message)
            print(json.dumps(result))
        
        elif command == "accept_invite":
            if len(sys.argv) < 3:
                print(json.dumps({"error": "accept_invite benötigt: invite_id"}))
                return
            
            result = client.accept_invite(int(sys.argv[2]))
            print(json.dumps(result))
        
        elif command == "get_invites":
            result = client.get_invites()
            print(json.dumps(result))
        
        elif command == "get_leaderboard":
            # get_leaderboard [limit] [period]
            limit = int(sys.argv[2]) if len(sys.argv) > 2 else 10
            period = sys.argv[3] if len(sys.argv) > 3 else 'all'
            
            result = client.get_leaderboard(limit, period)
            print(json.dumps(result))
        
        # ===== UNBEKANNTER COMMAND =====
        
        else:
            print(json.dumps({
                "error": f"Unbekannter Command: {command}",
                "verfügbare_commands": [
                    "signup", "signin", "signout", "update_user",
                    "get_users", "get_user",
                    "upload_avatar", "get_avatar",
                    "get_categories", "get_category",
                    "get_difficulties", "get_difficulty",
                    "get_settings",
                    "get_game_modes", "get_game_mode",
                    "get_game_states", "get_game_state",
                    "get_random_user", "get_user_matches",
                    "create_match", "get_match_details",
                    "get_round_questions", "submit_answer", "end_turn",
                    "invite_friend", "accept_invite", "get_invites",
                    "get_leaderboard"
                ]
            }))
    
    except Exception as e:
        print(json.dumps({"error": str(e)}))


if __name__ == '__main__':
    main()