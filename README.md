
# REST_area

A place for REST....

![REST AREA](images/REST_area.png)

## REST_area server

The REST_area server is implemented in Python with Flask.  Before running it, make sure to install Flask:

    pip3 install flask

After this the server can be launched directly from the `Python/server` directory with:

    flask run

Alternatively one of the provided Dockerfiles may be used to run the server with Docker.

## REST_area client

With the REST_area server running (see above), the client may be run from a terminal by running the `main.sh` script in the `Java/client` directory.  For example, to invoke an HTTP GET on the server root URL:

    ./main.sh get

To GET a specified endpoint, like `/msgs/0`:

    ./main.sh "get(msgs/0)"

__NOTE:__ Quotes are necessary, as shown above, to prevent the shell from interpreting parenthesis and some other characters that may be used in commands.

### Available commands:
- `get(`_endpoint_`)`
- `put(`_endpoint_, _text_`)`
- `putfile(`_endpoint_, _filepath_`)`
- `post(`_endpoint_, _text_`)`
- `postfile(`_endpoint_, _filepath_`)`
- `delete(`_endpoint_`)`
- `sleep(`_milliseconds_`)`

Options may be passed to the REST_area client using the syntax `-optionName=value`.  All options are processed, in order, before any commands are run.

### Available options
- `-client=apache` - use the Apache HTTP client (instead of default Java HttpURLConnection based client).
- `-server=`_URL_ - use the provided REST_area server URL (instead of the default: `http://localhost:5000/`).
- `-logger=out` or `-logger=noout` - enable/disable printing progress messages to standard output.
- `-logger=err` or `-logger=noerr` - enable/disable printing error messages to standard error.

## Reference info

### media types (MIME)

- https://developer.mozilla.org/en-US/docs/Web/HTTP/Basics_of_HTTP/MIME_types

### Java client

- 'classic' Java HttpURLConnection based client
  - https://mkyong.com/webservices/jax-rs/restfull-java-client-with-java-net-url/
- Apache HttpClient
  - https://crunchify.com/how-to-create-restful-java-client-using-apache-httpclient-example/
  - https://itnext.io/how-to-create-a-simple-rest-client-for-testing-your-api-a0554d8380f8
  - https://www.javaguides.net/2018/10/apache-httpclient-get-post-put-and-delete-methods-example.html

### Python (Flask) server

- https://flask.palletsprojects.com/en/2.0.x/quickstart/
- https://www.fullstackpython.com/flask.html
- https://blog.miguelgrinberg.com/post/the-flask-mega-tutorial-part-i-hello-world
- https://www.digitalocean.com/community/tutorials/how-to-make-a-web-application-using-flask-in-python-3
- https://code.visualstudio.com/docs/python/tutorial-flask
- about Flask use of Python decorators:
  - https://ains.co/blog/things-which-arent-magic-flask-part-1.html
  - https://ains.co/blog/things-which-arent-magic-flask-part-2.html
- Dockerizing Flask:
  - https://docs.docker.com/language/python/build-images/
  - about requirements.txt: https://pip.pypa.io/en/latest/user_guide/#requirements-files
  - Dockerfile ref: https://docs.docker.com/engine/reference/builder/
  - VS Code: https://code.visualstudio.com/docs/containers/quickstart-python
