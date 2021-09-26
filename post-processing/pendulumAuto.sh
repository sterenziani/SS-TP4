cd ..
./build.sh
for N in {1..6}
do
	for Mode in G B V O
	do
		echo -e "1\n"$(echo "10^-${N}" | bc -l)"\n${Mode}" > input.txt
		java -jar bin/SS-TP4-1.0-SNAPSHOT-jar-with-dependencies.jar
	done
done
cd post-processing
