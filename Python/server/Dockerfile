
# syntax=docker/dockerfile:1

# docker build -t flask -f Dockerfile .
# docker run -d -p 5000:5000 flask

FROM python:3.8-slim-buster

WORKDIR /app

COPY requirements.txt requirements.txt
RUN pip3 install -r requirements.txt

COPY app.py app.py

CMD ["python3", "-m", "flask", "run", "--host=0.0.0.0" ]
