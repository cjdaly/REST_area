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
    return "Hello from RESTstop server!"

########
# /sys - system interface
@app.route("/sys")
def sys():
    return "RESTstop system interface!"
#
@app.route("/sys/<name>", methods=["PUT"])
def sys_name(name):
    if request.method == "PUT":
        val = request.get_data().decode("utf-8")
        if name == "RESET" and val == "!RESET!":
            reset()
            return "RESTstop server reset!"
        return "Unsupported system command: {} / {}".format(name, val)

##########
# /props - properties with simple PUT / GET api
_props = {}
#
@app.route("/props")
def props():
    return "{} properties buffered".format(len(_props))
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


#########
# /msgs - messages with simple POST / GET api
_messages = []
#
@app.route("/msgs", methods=["GET", "POST"])
def msgs():
    if request.method == "GET":
        return "{} messages buffered".format(len(_messages))
    elif request.method == "POST":
        msgNum = len(_messages)
        _messages.append(request.get_data())
        return "New message #{} posted!".format(msgNum)
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
            return "Message #{} deleted!".format(id)
        else:
            return Response("Message #{} not found!".format(id), 404)

# reset server data to initial state
def reset():
    print("RESTstop server reset!")
    # reset properties
    _props.clear()
    _props['test'] = "Hello World!"
    # reset messages
    _messages.clear()
    _messages.append("Hello World!")
#
reset()
