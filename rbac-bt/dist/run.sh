#!/bin/sh
#FNAME=`echo $1 | cut -d'.' -f1`
#java -jar -Xmx2048m rbacBt.jar -r2f  ./$FNAME.rbac -o ./$FNAME | tee -a -i out.log
java -jar -Xmx2048m rbacBt.jar -r2f $FNAME  | tee -a -i out.log
#java -jar -Xmx2048m rbacBt.jar -rmut ./$FNAME.rbac -o ./$FNAME | tee -a -i out.log
#java -jar -Xmx7000m rbacBt.jar -qset ./$FNAME/$FNAME.fsm  -o ./$FNAME | tee -a -i out.log
#java -jar -Xmx7000m rbacBt.jar -pset ./$FNAME/$FNAME.fsm  -o ./$FNAME | tee -a -i out.log
#java -jar -Xmx7000m rbacBt.jar -tt   ./$FNAME/$FNAME.fsm  -o ./$FNAME | tee -a -i out.log
