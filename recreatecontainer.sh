docker remove library-system
mvn clean package -DskipTests
docker build -t szymek25/library-system .