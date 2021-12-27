#!/bin/bash

ECLIPSE_PLUGINS="/Applications/Eclipse.app/Contents/Eclipse/plugins"
APACHE_CP="\
${ECLIPSE_PLUGINS}/org.apache.httpcomponents.httpcore_4.4.14.v20210128-2225.jar:\
${ECLIPSE_PLUGINS}/org.apache.httpcomponents.httpclient_4.5.13.v20210128-2225.jar:\
${ECLIPSE_PLUGINS}/org.apache.commons.logging_1.2.0.v20180409-1502.jar"


java -cp "bin:$APACHE_CP" Main
