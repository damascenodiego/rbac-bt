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
for counti in {1..30}; do
for rst in '2528'; do
for pol in 'user11roles2_v2' 'ExperiencePointsv2' 'Masood2009P2v2' ; do
#for pol in 'user11roles2_v2' ; do
#for pol in 'Masood2009P2v2' ; do
#for pol in 'ExperiencePointsv2' ; do
#for pol in 'SeniorTraineeDoctor'; do
#for pol in 'Masood2010Example1'; do
  for test in ./results/*$pol/test/*.spy.test ./results/*$pol/test/*.hsi.test ./results/*$pol/test/*.w.test; do
    sz=`cat $test.testCharacteristics.log |cut -d$'\t' -f6|tail -n 1`
    sz=`awk "BEGIN {printf \"%.0f\",$sz}"`

       shuf $test > $test.$runTimeStamp
       mv $test.$runTimeStamp $test.subset.$rst.test
       sed -n -e 1,"$rst"p $test.subset.$rst.test -i.bkp 
       $JAVA_RUN/java -Xmx8000m -jar ./rbacBt_cartaxPrtzSort.jar -ct ./results/*$pol/$pol.rbac -mutants ./results/*$pol/$mutantsFile -test $test.subset.$rst.test >> ./$runTimeStamp/conformanceTest.$runTimeStamp.out
       $JAVA_RUN/java -Xmx8000m -jar ./rbacBt_cartaxPrtzSort.jar -testCharact -rbac ./results/*$pol/$pol.rbac -test $test.subset.$rst.test >> ./$runTimeStamp/testCharacteristics.$runTimeStamp.out

       $JAVA_RUN/java -Xmx8000m -jar ./rbacBt_cartaxPrtzSort.jar -prtz $test.subset.$rst.test  -rbac ./results/*$pol/$pol.rbac -mode cartax
       $JAVA_RUN/java -Xmx8000m -jar ~/fragmentTestSuite.jar $test.subset.$rst.test.cartax.test

       $JAVA_RUN/java -Xmx8000m -jar ./rbacBt_cartaxPrtzSort.jar -prtz $test.subset.$rst.test  -rbac ./results/*$pol/$pol.rbac -mode damasc
       $JAVA_RUN/java -Xmx8000m -jar ~/fragmentTestSuite.jar $test.subset.$rst.test.damasc.test

       tname=`basename $test.subset.$rst.test`

       for testprtz in ./prioritization/$pol/$tname.cartax.test.[0-9][0-9][0-9].test ./prioritization/$pol/$tname.damasc.test.[0-9][0-9][0-9].test; do
          $JAVA_RUN/java -Xmx8000m -jar ./rbacBt_cartaxPrtzSort.jar -ct ./results/*$pol/$pol.rbac -mutants ./results/*$pol/$mutantsFile -test $testprtz >> ./$runTimeStamp/conformanceTest.$runTimeStamp.out
       done
       for countj in {1..10}; do # random prtz
          shuf $test.subset.$rst.test > $test.subset.$rst.test.random.test
          $JAVA_RUN/java -Xmx8000m -jar ~/fragmentTestSuite.jar $test.subset.$rst.test.random.test
          for testprtz in ./prioritization/$pol/$tname.random.test.[0-9][0-9][0-9].test; do
               $JAVA_RUN/java -Xmx8000m -jar ./rbacBt_cartaxPrtzSort.jar -ct ./results/*$pol/$pol.rbac -mutants ./results/*$pol/$mutantsFile -test $testprtz >> ./$runTimeStamp/conformanceTest.$runTimeStamp.out
          done
       done #for countj
       rm -rf $test.subset.*  ./prioritization/$pol/
  done #for test
  echo " " >> ./$runTimeStamp/conformanceTest.$runTimeStamp.out
  grep -v '^[a-zA-Z0-9_-_]*$' ./$runTimeStamp/conformanceTest.$runTimeStamp.out > /home/damasceno/Dropbox/$runTimeStamp/conformanceTest.$runTimeStamp.out
done; #for pol
done #for counti
done; #for rst
