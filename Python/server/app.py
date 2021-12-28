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
def hello():
    return "<p>Hello from RESTstop server!</p>"

@app.route("/msg")
def msg():
    return "<p>messages...</p>"

_props = {}
_props['hello'] = "world"

@app.route("/props")
def props():
    return "<p>properties...</p>"

@app.route("/props/<name>", methods=["GET", "PUT"])
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
