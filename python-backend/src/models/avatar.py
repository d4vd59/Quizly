import base64

class Avatar:
    """Repräsentiert ein Benutzer-Avatar"""
    
    def __init__(self, mime_type, avatar_data):
        self.mime_type = mime_type 
        self.avatar_data = avatar_data  
    
    def to_dict(self):
        return {
            'mime_type': self.mime_type,
            'avatar': self.avatar_data
        }
    
    @staticmethod
    def from_file(file_path):
        """Lädt Avatar aus Datei und kodiert als Base64"""
        with open(file_path, 'rb') as f:
            image_data = f.read()
            encoded = base64.b64encode(image_data).decode('utf-8')
            
        
        if file_path.endswith('.png'):
            mime_type = 'image/png'
        elif file_path.endswith('.jpg') or file_path.endswith('.jpeg'):
            mime_type = 'image/jpeg'
        else:
            mime_type = 'image/png'
            
        return Avatar(mime_type, encoded)