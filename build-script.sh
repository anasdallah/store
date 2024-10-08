#!/bin/bash
set -e

#Build the project using Maven
echo "Building the project with Maven..."
mvn clean install

#Start the Docker containers
echo "Starting Docker containers..."
docker-compose up --build --force-recreate

echo "Generate Jacoco Report ..."
mvn jacoco:report

echo "All steps completed successfully."
