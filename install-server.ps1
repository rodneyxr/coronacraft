New-Item -ItemType Directory -Force server
cd server
wget "https://repo.spongepowered.org/maven/org/spongepowered/spongevanilla/1.12.2-7.1.10/spongevanilla-1.12.2-7.1.10.jar" -OutFile "spongevanilla-1.12.2-7.1.10.jar"
java -jar "spongevanilla-1.12.2-7.1.10.jar"
(Get-Content ./eula.txt).replace('eula=false', 'eula=true') | Set-Content ./eula.txt
cp "../build/libs/coronacraft-1.0.0.jar" mods
cd ..