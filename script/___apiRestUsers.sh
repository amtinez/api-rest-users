#!/usr/bin/env bash

echo "\nWhat do you want to do?"
echo "1. Remove docker containers"
echo "2. Start docker containers"
echo "3. Remove docker data directory"
echo "4. Compile"
echo "5. Start the server"
echo "6. Compile and start the server"
echo "7. Execute Sonarqube report"
echo "0. Exit"

PROJECT_PATH=[YOUR PROJECT PATH]
PROJECT_ACRONYM=[YOUR PROJECT ACRONYM]

while :
do
	read OPTION
	echo "\nYou have selected:" $OPTION
	case $OPTION in
		0)
			echo "Disconnected"
			break
			;;
		1)
			echo "Removing docker containers..."
			docker rm $PROJECT_ACRONYM-mysql -f
			docker rm $PROJECT_ACRONYM-mysql-test -f
			docker rm $PROJECT_ACRONYM-sonarqube -f
			break
			;;
		2)
			echo "Starting docker containers..."
			cd $PROJECT_PATH/docker && docker-compose up -d
			break
			;;
		3)
			echo "Removing docker data directory..."
			cd $PROJECT_PATH && rm -r docker-data
			break
			;;
		4)
			echo "Compiling..."
			cd $PROJECT_PATH && ./gradlew classes && ./gradlew testClasses
			break
			;;
		5)
			echo "Starting the server..."
			cd $PROJECT_PATH && ./gradlew bootRun
			break
			;;
		6)
			echo "Compiling and starting the server..."
			cd $PROJECT_PATH && ./gradlew classes && ./gradlew testClasses && ./gradlew bootRun
			break
			;;
		7)
			echo "Executing Sonarqube report..."
			cd $PROJECT_PATH && ./gradlew check && ./gradlew jacocoTestReport && ./gradlew sonarqube
			break
			;;
		*)
			echo "Select a correct option"
			;;    
  esac
done