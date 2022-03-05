git pull
./gradlew clean shadowJar
docker build .
tagName=$(docker images | awk '{print $3}' | awk 'NR==2')
docker tag $tagName toadlessss/monkebot:latest
docker login
docker push toadlessss/monkebot:latest
echo "Done!"
