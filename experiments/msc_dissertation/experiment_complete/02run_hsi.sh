#!/bin/sh

FNAME=$2
STATES=$1
FNAME=`echo $FNAME | cut -d'.' -f1 `
echo $0 $FNAME
date >> ./results/'hsi.test.log'
echo "filename  | $FNAME" >> ./results/'hsi.test.log'
date1=$(date +"%s%3N")
./app/fsm-hsi < $FNAME.kk > ./results/$FNAME'.hsi.test'
date2=$(date +"%s%3N")
diff=$(($date2-$date1))
echo "time elapsed (miliseconds) | $diff" >> ./results/'hsi.test.log'
echo "############################################################" >> ./results/'hsi.test.log'
