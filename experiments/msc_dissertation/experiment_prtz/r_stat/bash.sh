grep --color -E  "^[a-zA-Z0-9]+\W+[0-9]+" ./test.out  | sed 's/\([^[:digit:]]\)\./\1\t/g' | sed "s/.test//g" > ./out_test.log
grep --color -E  "^[a-zA-Z0-9\._]+[[:space:]]+[0-9]+" ./subset.out | sed 's/\([^[:digit:]]\)\./\1\t/g' | sed "s/.test//g" > ./out_subset.log


grep --color -E "^[a-zA-Z0-9\._]+[[:space:]]+[0-9]+[[:space:]]+[a-zA-Z0-9_]+[[:space:]]+[a-zA-Z0-9]+[[:space:]]+[0-9\.]+$" 							 ./out_test.log   >  out_test_100.log 
grep --color -E "^[a-zA-Z0-9_]+[[:space:]]+[0-9]+[[:space:]]+[a-zA-Z0-9_]+[[:space:]]+[a-zA-Z0-9]+[[:space:]]+[a-z]+[[:space:]]+[0-9]+[[:space:]]+[0-9\.]+$" 			 ./out_test.log   > out_test_frag.log 
grep --color -E "^[a-zA-Z0-9\._]+[[:space:]]+[0-9]+[[:space:]]+[a-zA-Z0-9\._]+[[:space:]]+[a-zA-Z0-9]+[[:space:]]+[0-9]+[[:space:]]+[0-9\.]+$" 					 ./out_subset.log > out_subset_100.log 
grep --color -E "^[a-zA-Z0-9\._]+[[:space:]]+[0-9]+[[:space:]]+[a-zA-Z0-9\._]+[[:space:]]+[a-zA-Z0-9]+[[:space:]]+[0-9]+[[:space:]]+[a-z]+[[:space:]]+[0-9]+[[:space:]]+[0-9\.]+$" ./out_subset.log > out_subset_frag.log 
