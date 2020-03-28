./gradlew build
cp build/libs/coronacraft-1.0.0.jar server/mods
cd server
java -jar "spongevanilla-1.12.2-7.1.10.jar"
cd ..