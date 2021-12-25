
from flask import Flask

app = Flask(__name__)

@app.route("/")
def hello():
    return "<p>Hello World!</p>"

@app.route("/msg")
def msg():
    return "<p>messages...</p>"