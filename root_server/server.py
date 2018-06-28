from flask import Flask, request, jsonify
from flask_redis import FlaskRedis
import hashlib

REDIS_URL = "redis://:password@localhost:6379/0"
app = Flask(__name__)
redis_store = FlaskRedis(app)


# root
@app.route("/")
def index():
    return "This is root"

@app.route('/api/signup', methods=['POST'])
def signup():
    json = request.get_json()
    print(json)
    if len(json['email']) == 0 or len(json['password']) == 0:
        return jsonify({'error': 'invalid input'})

    redis_store.set(hashlib.sha512(json['password'] + json['email']).hexdigest(), json['email'])
    return jsonify({'token': hashlib.sha512(json['password'] + json['email']).hexdigest()})

@app.route('/api/register_client', methods=['POST'])
def register_client():
    json = request.get_json()
    client_id = json["client_id"]
    client_secret = hashlib.sha512(client_id).hexdigest()

    redis_store.set(client_id, client_secret)
    return jsonify({'client_secret': client_secret})

@app.route('/api/client_access_request', methods=['POST'])
def request_access():
    json = request.get_json()
    client_id = json["client_id"]
    client_secret = json["client_secret"]
    root_token = json["token"]
    scope = json["scope"]

    if redis_store.get(client_id) != client_secret:
        return  jsonify({'error': "client_error"})
    client_access_token = hashlib.sha512(scope + client_id).hexdigest()
 
    redis_store.set(client_access_token, root_token)
    return jsonify({'token': client_access_token})


@app.route('/api/get_user/<token>')
def get_user(token):
    email = redis_store.get(redis_store.get(token))
    if email is None:
        email = redis_store.get(token)
    return jsonify({"email": email})

if __name__ == '__main__':
    app.run(host='0.0.0.0', port=5000)