#!/bin/sh

FNAME=$2
STATES=$1
FNAME=`echo $FNAME | cut -d'.' -f1 `
echo $0 $FNAME
date >> ./results/'w.test.log'
echo "filename  | $FNAME" >> ./results/'w.test.log'
date1=$(date +"%s%3N")
java -Xmx7000m -jar ./app/w.jar $FNAME.kk > ./results/$FNAME'.w.test'
date2=$(date +"%s%3N")
diff=$(($date2-$date1))
echo "time elapsed (miliseconds) | $diff" >> ./results/'w.test.log'
echo "############################################################" >> ./results/'w.test.log'
