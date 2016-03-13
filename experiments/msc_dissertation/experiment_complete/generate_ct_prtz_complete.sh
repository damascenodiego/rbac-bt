#!/bin/bash

export JAVA_RUN=/home/damasceno/opt/jdk1.8.0_73/bin/
cd `dirname $0`

rst=100
sz=26

runTimeStamp=run$(date +"%Y-%m-%d_%H-%M-%S")
mutantsFile=mutantsNeq.txt
counti=0

mkdir ./$runTimeStamp
mkdir -p /home/damasceno/Dropbox/$runTimeStamp/
for pol in 'Masood2010Example1' 'SeniorTraineeDoctor'  ; do
  for test in ./results/*$pol/test/*.spy.test ./results/*$pol/test/*.hsi.test ./results/*$pol/test/*.w.test; do

       $JAVA_RUN/java -Xmx8000m -jar ./rbacBt_cartaxPrtzSort.jar -ct ./results/*$pol/$pol.rbac -mutants ./results/*$pol/$mutantsFile -test $test >> ./$runTimeStamp/conformanceTest.$runTimeStamp.out

       $JAVA_RUN/java -Xmx8000m -jar ./rbacBt_cartaxPrtzSort.jar -prtz $test  -rbac ./results/*$pol/$pol.rbac -mode cartax
       $JAVA_RUN/java -Xmx8000m -jar ~/fragmentTestSuite.jar $test.cartax.test

       $JAVA_RUN/java -Xmx8000m -jar ./rbacBt_cartaxPrtzSort.jar -prtz $test  -rbac ./results/*$pol/$pol.rbac -mode damasc
       $JAVA_RUN/java -Xmx8000m -jar ~/fragmentTestSuite.jar $test.damasc.test

       tname=`basename $test`
       for testprtz in ./prioritization/$pol/$tname.cartax.test.[0-9][0-9][0-9].test ./prioritization/$pol/$tname.damasc.test.[0-9][0-9][0-9].test; do
          $JAVA_RUN/java -Xmx8000m -jar ./rbacBt_cartaxPrtzSort.jar -ct ./results/*$pol/$pol.rbac -mutants ./results/*$pol/$mutantsFile -test $testprtz >> ./$runTimeStamp/conformanceTest.$runTimeStamp.out
       done
       for counti in {1..30}; do
             shuf $test > $test.random.test 
             $JAVA_RUN/java -Xmx8000m -jar ./rbacBt_cartaxPrtzSort.jar -ct ./results/*$pol/$pol.rbac -mutants ./results/*$pol/$mutantsFile -test $test.random.test >> ./$runTimeStamp/conformanceTest.$runTimeStamp.out
             $JAVA_RUN/java -Xmx8000m -jar ~/fragmentTestSuite.jar $test.random.test
             tname=`basename $test.random.test`
             for testprtz in ./prioritization/$pol/$tname.[0-9][0-9][0-9].test; do
                $JAVA_RUN/java -Xmx8000m -jar ./rbacBt_cartaxPrtzSort.jar -ct ./results/*$pol/$pol.rbac -mutants ./results/*$pol/$mutantsFile -test $testprtz >> ./$runTimeStamp/conformanceTest.$runTimeStamp.out
             done
        done #for counti
  done #for test      
  rm -rf ./prioritization/$pol/
  echo " " >> ./$runTimeStamp/conformanceTest.$runTimeStamp.out
  grep -v '^[a-zA-Z0-9_-_]*$' ./$runTimeStamp/conformanceTest.$runTimeStamp.out > /home/damasceno/Dropbox/$runTimeStamp/conformanceTest.$runTimeStamp.out
done; #for pol


