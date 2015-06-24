#!/bin/sh
java -jar -Xmx2048m rbacBt.jar -r2f  ./$1.rbac -o ./$1
java -jar -Xmx2048m rbacBt.jar -rmut ./$1.rbac -o ./$1
java -jar -Xmx2048m rbacBt.jar -qset ./$1/$1.fsm  -o ./$1
java -jar -Xmx2048m rbacBt.jar -pset ./$1/$1.fsm  -o ./$1
java -jar -Xmx2048m rbacBt.jar -tt   ./$1/$1.fsm  -o ./$1
