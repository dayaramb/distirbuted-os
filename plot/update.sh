
sh take-latest.sh >/tmp/latest.txt

cat /tmp/latest.txt  |cut -f2 -d , >value.txt
cat /tmp/latest.txt  |cut -f1 -d , >key.txt
num=24
num2=25
pie1=25
pie2=26
#update java scripts file for key and value of the tweets
while read line
do 
#Here we replace a label and value of a java script file. Instead of 
 
# sed 's/^.*\bpattern\b.*$/Substitution/' file
#Here \b is for word boundaries. 

#^.*\bpattern\b.*$ is used to make sure whole line is matched containing pattern.  ^ is line start and $ is line end. .* matches 0 or more length text. So ^.* matches all the text before pattern and .*$ matches all the text after pattern
#


sed -i "$num,$num s/^.*\blabel\b.*$/\"label\":\"$line\",/" bar-chart.html
sed -i "$pie1,$pie1 s/^.*\blabel\b.*$/\"label\":\"$line\",/" pie-chart.html
num=$((num+4)) 
pie1=$((pie1+4)) 
done <key.txt


while read line2
do 
sed -i "$num2,$num2 s/^.*\bvalue\b.*$/\"value\":\"$line2\",/" bar-chart.html
sed -i "$pie2,$pie2 s/^.*\bvalue\b.*$/\"value\":\"$line2\",/" pie-chart.html
num2=$((num2+4)) 
pie2=$((pie2+4)) 
done <value.txt


