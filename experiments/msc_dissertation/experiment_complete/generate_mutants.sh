#!/bin/bash

export JAVA_RUN=/home/damasceno/opt/jdk1.8.0_45/bin/
cd `dirname $0`

rst=100
sz=26


runTimeStamp=run$(date +"%Y-%m-%d_%H-%M-%S")
mutantsFile=mutants.txt
counti=0

for pol in 'Masood2010Example1' 'SeniorTraineeDoctor' 'ExperiencePoints' 'ExperiencePointsv2' 'user11roles2' 'user11roles2_v2' 'Masood2009P2' 'Masood2009P2v2' 'Masood2009P1' 'Masood2009P1v2' 'ProcureToStock' 'ProcureToStockV2'; do
    $JAVA_RUN/java -Xmx8000m -jar ~/rbacBt.jar -rmut ./results/*$pol/$pol.rbac 
#    for pmut in ./results/*$pol/*/*.rbac; do $JAVA_RUN/java -Xmx8000m -jar ~/rbacBt.jar -rmut $pmut;done
    cd ./results/*$pol/
    ls -1 ./*/*.rbac  > ./$mutantsFile
#    ls -1 ./*/*/*.rbac >> ./$mutantsFile
    cd -
done

