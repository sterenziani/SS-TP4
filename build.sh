mvn clean package
if [[ ! -d bin ]]
then
	mkdir bin
fi
cp target/*.jar bin
