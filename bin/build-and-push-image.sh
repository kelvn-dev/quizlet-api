mvn clean install -DskipTests
docker build -t kelvn/quizlet-api:latest -f Dockerfile .
docker push kelvn/quizlet-api:latest