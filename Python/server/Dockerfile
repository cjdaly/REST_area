# syntax=docker/dockerfile:1

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

# docker build -t restarea .
# docker run -d -p 5000:5000 restarea

FROM python:3.8-slim-buster

WORKDIR /app

COPY requirements.txt requirements.txt
RUN pip3 install -r requirements.txt

COPY app.py app.py

CMD ["python3", "-m", "flask", "run", "--host=0.0.0.0" ]
