# Coronacraft
Coronavirus simulator in Minecraft to promote awareness and social distancing.

# Description
This Minecraft mod was created by using the Sponge Minecraft Modding API.  The Mod
works by infecting a player with COVID-19 which is tracked by extending a Minecraft
Entity object to add various additional fields to the class. The virus can only
infect Minecraft Villagers and Player entities. The first host infected with COVID-19
can infect any other entity that is prone to the virus within a configurable distance
to the carrier. The surrounding entities that come into contact with an infected entity
are tracked by the mod. We implement somewhat realistic transmission and fatality 
statistics to simulate the virus spread and damage in the real world. Our team
conducted experiments with the virus in a variety of populations and we were able to
simulate a bell curve when the entities were in close proximity. The curve was
flattened when the entities were practicing social distancing.

## Purpose
This project was created for RowdyHacks 2020. Our team of four had no 
prior experience to building a Minecraft Mod so it was learning process. Our goals 
were ambitious and we managed to get a lot done.