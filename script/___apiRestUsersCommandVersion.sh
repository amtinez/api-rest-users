#!/usr/bin/env bash

printf "\nWhat do you want to do?\n"
echo "1. Remove docker containers"
echo "2. Start docker containers"
echo "3. Remove docker data directory"
echo "4. Compile"
echo "5. Start the server"
echo "6. Compile and start the server"
echo "7. Execute JaCoCo test report"
echo "0. Exit"

PROJECT_PATH=[YOUR PROJECT PATH]
DOCKER_DATA_DIRECTORY=docker-data

while :; do
  read -r OPTION
  printf "\nYou have selected: %s\n" "$OPTION"
  case $OPTION in
  0)
    echo "Disconnected"
    break
    ;;
  1)
    echo "Removing docker containers..."
    cd "$PROJECT_PATH"/docker && docker-compose stop && docker-compose rm -f
    break
    ;;
  2)
    echo "Starting docker containers..."
    cd "$PROJECT_PATH"/docker && docker-compose up -d
    break
    ;;
  3)
    echo "Removing docker data directory..."
    cd "$PROJECT_PATH" || exit
    if [ -d "$DOCKER_DATA_DIRECTORY" ]; then
      rm -r "$DOCKER_DATA_DIRECTORY"
      echo "The Docker data directory has been removed"
    else
      echo "The docker data directory does not exist"
    fi
    break
    ;;
  4)
    echo "Compiling..."
    cd "$PROJECT_PATH" && ./gradlew classes && ./gradlew testClasses
    break
    ;;
  5)
    echo "Starting the server..."
    cd "$PROJECT_PATH" && ./gradlew bootRun
    break
    ;;
  6)
    echo "Compiling and starting the server..."
    cd "$PROJECT_PATH" && ./gradlew classes && ./gradlew testClasses && ./gradlew bootRun
    break
    ;;
  7)
    echo "Executing JaCoCo test report..."
    cd "$PROJECT_PATH" && ./gradlew check && ./gradlew jacocoTestReport
    break
    ;;
  *)
    echo "Select a correct option"
    ;;
  esac
done
