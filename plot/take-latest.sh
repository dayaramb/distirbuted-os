head -n 15 /tmp/test.txt|tail -n 12 |head -n 10 |cut -f2 -d '(' |cut -f1 -d ')' 
cp /dev/null /tmp/test.txt
