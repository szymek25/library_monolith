docker remove library-system
mvn package
docker build -t library-system .