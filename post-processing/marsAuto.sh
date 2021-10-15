cd ..
./build.sh
echo -e "v\tt\td" > output/marsLanding.txt
for V in $(seq 7.999 0.00001 8.002 )
	do 
		echo -e "5\n100\n${V}" > input.txt
		java -jar bin/SS-TP4-1.0-SNAPSHOT-jar-with-dependencies.jar >> output/marsLanding.txt
	done
cd post-processing
py plotSpeed.py
