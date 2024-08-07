#!/bin/bash
set -e

#Build the project using Maven
echo "Building the project with Maven..."
mvn clean install

#Start the Docker containers
echo "Starting Docker containers..."
docker-compose up --build --force-recreate

echo "All steps completed successfully."
