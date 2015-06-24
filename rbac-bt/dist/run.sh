#!/bin/sh
FNAME=`echo $1 | cut -d'.' -f1`
java -jar -Xmx2048m rbacBt.jar -r2f  ./$FNAME.rbac -o ./$FNAME
java -jar -Xmx2048m rbacBt.jar -rmut ./$FNAME.rbac -o ./$FNAME
#java -jar -Xmx2048m rbacBt.jar -qset ./$FNAME/$FNAME.fsm  -o ./$FNAME
#java -jar -Xmx2048m rbacBt.jar -pset ./$FNAME/$FNAME.fsm  -o ./$FNAME
#java -jar -Xmx2048m rbacBt.jar -tt   ./$FNAME/$FNAME.fsm  -o ./$FNAME
