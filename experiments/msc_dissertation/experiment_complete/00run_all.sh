#!/bin/sh

for FNAME in '8 Masood2010Example1.kk' '21 SeniorTraineeDoctor.kk' '203 ExperiencePointsv2.kk' '485 user11roles2_v2.kk' '857 Masood2009P2v2.kk' '1880 Masood2009P1v2.kk' '5859 ProcureToStockV2.kk';
do
   date
   ./01run_w.sh $FNAME
   ./02run_hsi.sh $FNAME
   ./04run_spy.sh $FNAME
done
