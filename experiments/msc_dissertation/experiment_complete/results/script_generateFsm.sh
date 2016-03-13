#!/bin/sh

RBACBT='/home/damasceno/git/rbac-bt/rbac-bt/dist/rbacBt.jar'


for p in ./0*/*.rbac; do 
   java -Xmx6500m -jar $RBACBT -r2f $p;
   java -Xmx6500m -jar $RBACBT -r2f $p -kk;
done;


for p in ./0*/*/*.rbac; do 
   java -Xmx6500m -jar $RBACBT -r2f $p -kk;
done;



