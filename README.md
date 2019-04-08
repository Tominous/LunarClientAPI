# Lunar Client API #
> Tested Versions: **1.7.x - 1.8.x**
<p>This is the primary repository for my version of the Lunar Client API. This API is not a supported
software by their development team (lunar.gg); please do not bug them to fix any issues for you. You are free to submit
issues via the Issue tracker on GitHub, we will aim to fix or add any issues you come across.</p>

## Installation Instructions ##
To use this API, add this to your maven project's dependencies and a soft dependency to your plugin.yml
````yml
depend: [LunarClientAPI]
````

#### Maven
````xml
<dependency>
   <groupId>gg.manny</groupId>
   <artifactId>lunar-client-api</artifactId>
   <version>1.0</version>
</dependency>
````
       
#### Gradle
````gradle
compile "gg.manny:lunar-client-api:1.0-SNAPSHOT"
````

#### JAR Files
1. Download the [latest version](https://github.com/AgentManny/LunarClientAPI/releases).
2. Add the JAR to your project.
 For Eclipse users, see [here](http://stackoverflow.com/questions/11033603/how-to-create-a-jar-with-external-libraries-included-in-eclipse).
 For IntelliJ users, see [here](http://stackoverflow.com/questions/1051640/correct-way-to-add-external-jars-lib-jar-to-an-intellij-idea-project).

## Usage
Here is some example usage, the following code will tell you whether a player is using lunar client.ó

````java
LunarClientAPI.getInstance().onClient(player); //Returns true or false

@EventHandler
public void onAuthenticate(PlayerAuthenticateEvent event) {
    Player player = event.getPlayer();
    player.sendMessage("Lunar Client Verified...");
}
````