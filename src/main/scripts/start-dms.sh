#!/bin/bash
#
# start-dms.sh
#

(java -jar -DschemaFile="../config/deviceSchema.graphqls" ../lib/dms-0.0.1-SNAPSHOT.jar) &
