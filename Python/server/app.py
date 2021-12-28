#*****************************************************************************
#* Copyright (c) 2021 Chris J Daly (github user cjdaly)
#* All rights reserved. This program and the accompanying materials
#* are made available under the terms of the Eclipse Public License v1.0
#* which accompanies this distribution, and is available at
#* http://www.eclipse.org/legal/epl-v10.html
#*
#* Contributors:
#*   cjdaly - initial API and implementation
#****************************************************************************/

from flask import Flask, request, Response

app = Flask(__name__)

@app.route("/")
def root():
    return "<p>Hello from RESTstop server!</p>"

########
# /props
_props = {}
_props['test'] = "Hello World!"
#
@app.route("/props")
def props():
    return "<p>{} properties buffered</p>".format(len(_props))
#
@app.route("/props/<name>", methods=["GET", "PUT", "DELETE"])
def prop(name):
    if request.method == "GET":
        if name in _props:
            return _props[name]
        else:
            return Response("Property '{}' not found!".format(name), 404)
    elif request.method == "PUT":
        if name in _props:
            _props[name] = request.get_data()
            return "/props: replacing value for '{}'".format(name)
        else:
            _props[name] = request.get_data()
            return "/props: set value for '{}'".format(name)
    elif request.method == "DELETE":
        if name in _props:
            del(_props[name])
            return "/props: deleted value for '{}'".format(name)
        else:
            return Response("Property '{}' not found!".format(name), 404)


#######
# /msgs
_messages = ["Hello World!"]
#
@app.route("/msgs", methods=["GET", "POST"])
def msgs():
    if request.method == "GET":
        return "<p>{} messages buffered</p>".format(len(_messages))
    elif request.method == "POST":
        msgNum = len(_messages)
        _messages.append(request.get_data())
        return "<p>New message #{} posted!</p>".format(msgNum)
#
@app.route("/msgs/<int:id>", methods=["GET", "DELETE"])
def msg(id):
    if request.method == "GET":
        if id >= len(_messages):
            return Response("Message #{} out of bounds!".format(id), 404)
        msg = _messages[id]
        if msg:
            return msg
        else:
            return Response("Message #{} not found!".format(id), 404)
    elif request.method == "DELETE":
        if id >= len(_messages):
            return Response("Message #{} out of bounds!".format(id), 404)
        msg = _messages[id]
        if msg:
            _messages[id] = None
            return "<p>Message #{} deleted!</p>".format(id)
        else:
            return Response("Message #{} not found!".format(id), 404)
