#!/bin/sh

FNAME=$2
STATES=$1
FNAME=`echo $FNAME | cut -d'.' -f1 `
echo $0 $FNAME
date >> ./results/'spy.test.log'
echo "filename  | $FNAME" >> ./results/'spy.test.log'
date1=$(date +"%s%3N")
./app/spy 0 $STATES < $FNAME.kk > ./results/$FNAME'.spy.test'
date2=$(date +"%s%3N")
diff=$(($date2-$date1))
echo "time elapsed (miliseconds) | $diff" >> ./results/'spy.test.log'
echo "############################################################" >> ./results/'spy.test.log'
