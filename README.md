## PlayerHousing

PlayerHousing is a world based hosing plugin that allows a user to have full control over their own world.
This is made to be a more modern replacement of PlayerServers as this does not require a decent server
or require the player to understand how to manage a server, this method is alot more user friendly.

This plugin is built for folia as it takes advantage of how each world is on its own thread,
this means you can dump those anti-lag/anti-crash plugins and watch what a player can make
without having to break game machenics which disrupts the gameplay.

# Requirements
- Folia (latest)

# API
To access the PlayerHousing api, call `PlayerHousing.getAPI()`
The api contains useful tools such as world configs, world utils and more.

# Notes
PlayerHousing uses NMS to manually load worlds. why? because folia does not support loading worlds via bukkit api.
