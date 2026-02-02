from flask import Blueprint, jsonify

api = Blueprint('api', __name__)

@api.route('/health', methods=['GET'])
def health_check():
    return jsonify({"status": "healthy"}), 200

@api.route('/example', methods=['GET'])
def example_route():
    return jsonify({"message": "This is an example route."}), 200

def register_routes(app):
    app.register_blueprint(api, url_prefix='/api')