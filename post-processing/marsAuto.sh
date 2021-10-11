cd ..
./build.sh
echo -e "v\tt" > output/marsLanding.txt
for V in $(seq 7.9995 0.00002 8.0005 )
	do 
		echo -e "4\n100\n${V}" > input.txt
		java -jar bin/SS-TP4-1.0-SNAPSHOT-jar-with-dependencies.jar >> output/marsLanding.txt
	done
cd post-processing
py plotSpeed.py
