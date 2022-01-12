#!/bin/bash
#*****************************************************************************
#* Copyright (c) 2022 Chris J Daly (github user cjdaly)
#* All rights reserved. This program and the accompanying materials
#* are made available under the terms of the Eclipse Public License v1.0
#* which accompanies this distribution, and is available at
#* http://www.eclipse.org/legal/epl-v10.html
#*
#* Contributors:
#*   cjdaly - initial API and implementation
#****************************************************************************/


CP_SEP=':' # ';' on Windows

ECLIPSE_PLUGINS="/Applications/Eclipse.app/Contents/Eclipse/plugins"
ECLIPSE_CP=$(find $ECLIPSE_PLUGINS/org.apache.httpcomponents.httpcore_*.jar)$CP_SEP
ECLIPSE_CP+=$(find $ECLIPSE_PLUGINS/org.apache.httpcomponents.httpclient_*.jar)$CP_SEP
ECLIPSE_CP+=$(find $ECLIPSE_PLUGINS/org.apache.commons.logging_*.jar)$CP_SEP
ECLIPSE_CP+=$(find $ECLIPSE_PLUGINS/org.eclipse.json_*.jar)

JLINE_CP=lib/jline-3.21.0.jar

java -cp "${ECLIPSE_CP}${CP_SEP}${JLINE_CP}${CP_SEP}bin" client.RestClientRepl "$@"
