from flask import Flask, request, jsonify, make_response, redirect, url_for, session
from flask_redis import FlaskRedis
import hashlib

REDIS_URL = "redis://:password@localhost:6379/0"
app = Flask(__name__)
app.secret_key = 'secret' 
redis_store = FlaskRedis(app)


# root
@app.route("/")
def index():
    return "This is root"

@app.route('/api/signup', methods=['POST'])
def signup():
    json = request.get_json()
    if len(json['email']) == 0 or len(json['password']) == 0:
        return jsonify({'error': 'invalid input'})
    redis_store.set(hashlib.sha512(json['password'] + json['email']).hexdigest(), json['email'])
    return jsonify({'token': hashlib.sha512(json['password'] + json['email']).hexdigest()})

@app.route('/login')
def login():
    return "<form method='POST' action='api/login'><input type='text' name='email' placeholder='Email' /></br><input type='text' name='password' placeholder='Password' /></br><button type='submit' >Signin</button></form>"

@app.route('/api/login', methods=['POST'])
def signin():
    email = request.form.get("email")
    password = request.form.get("password")
    print(email, password, redis_store.get(hashlib.sha512(password+email).hexdigest()))
    if redis_store.get(hashlib.sha512(password+email).hexdigest()):
        if session["url"]:
            res = make_response(redirect(session["url"], code=302))
            res.set_cookie("token", value=hashlib.sha512(password+email).hexdigest())
            del(session["url"])
            return res
        else:
            return "Succesfull"
    else:
        return jsonify({'error': "Error"})

#?client_id={client_id}&redirect_uri=mysite.com/fblogin&response_type=code
@app.route('/api/oauth')
def oauth():
    client_id = request.args.get("client_id", default="")
    scope = request.args.get("scope", default="")
    redirect_url = request.args.get("redirect_url", default="")
    if request.cookies.get("token") is None:
        session["url"] = "/api/oauth?{}".format(request.query_string)
        return redirect("/login", code=302)
    if request.args.get("button", default="") is "":
        session["client_id"] = client_id
        session["scope"] = scope
        session["redirect_url"] = redirect_url
        return "<form method='GET' action='/api/oauth?{}'><h3>{} app is requesting access to your {}.</h3><button name='button' value='ok' vatype='submit'>Ok</button><button name='button' value='cancel' vatype='submit'>Cancel</button></form>".format(request.query_string, client_id, scope)
    elif request.args.get("button") == "ok":
        client_access_token = hashlib.sha512(session["scope"] + session["client_id"] + request.cookies.get("token")).hexdigest()
        del(session["scope"])
        del(session["client_id"])
        redis_store.set(client_access_token, request.cookies.get("token"))
        return redirect("http://{}#{}".format(session["redirect_url"],client_access_token), code=302)
    else:
        del(session["scope"])
        del(session["client_id"])
        return redirect("http://{}#{}".format(session["redirect_url"],"error"), code=302)


@app.route('/api/register_client', methods=['POST'])
def register_client():
    json = request.get_json()
    client_id = json["client_id"]
    client_secret = hashlib.sha512(client_id).hexdigest()

    redis_store.set(client_id, client_secret)
    return jsonify({'client_secret': client_secret})


@app.route('/api/get_user/<token>')
def get_user(token):
    email = redis_store.get(redis_store.get(token))
    if email is None:
        email = redis_store.get(token)
    return jsonify({"email": email})

if __name__ == '__main__':
    app.run(host='0.0.0.0', port=5000)