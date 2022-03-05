git pull
./gradlew clean shadowJar
docker build .
tagName=$(docker images | awk '{print $3}' | awk 'NR==2')
docker tag $tagName frogg1t/monkebot:latest
docker login
docker push frogg1t/monkebot:latest
echo "Done!"
