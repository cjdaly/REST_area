#!/bin/bash
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


ECLIPSE_PLUGINS="/Applications/Eclipse.app/Contents/Eclipse/plugins"

CP_SEP=':' # ';' on Windows
APACHE_CP=$(find $ECLIPSE_PLUGINS/org.apache.httpcomponents.httpcore_*.jar)$CP_SEP
APACHE_CP+=$(find $ECLIPSE_PLUGINS/org.apache.httpcomponents.httpclient_*.jar)$CP_SEP
APACHE_CP+=$(find $ECLIPSE_PLUGINS/org.apache.commons.logging_*.jar)$CP_SEP
APACHE_CP+=$(find $ECLIPSE_PLUGINS/org.eclipse.json_*.jar)

java -cp "bin${CP_SEP}${APACHE_CP}" Main "$@"
