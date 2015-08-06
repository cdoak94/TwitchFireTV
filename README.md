# TwitchFireTV
A remake of the twitch app for the FireTV, cause the existing one is not great.
Don't have much right now. I still have to figure out the whole Android UI thing.
I wouldn't know at all how to integrate Amazon's SDK.
There are vague instructions on the Amazon developer's site, but documentation is spotty at best.
The bonus is that Android API 17 runs natively on fire tv stick, so I was gonna skip the whole Amazon integration thing.

## API's and Libs
Here is a run-down of the API's and libs to be used.

### Twitch API
We have to use the Twitch API, Right? https://github.com/justintv/Twitch-API<br>
More was figured out about the routes for the actual streams by decompiling the existing mobile app,
however there is a basic outline already existing here: <br>
http://www.johannesbader.ch/2014/01/find-video-url-of-twitch-tv-live-streams-or-past-broadcasts/ <br>
It's not exactly the same, but its the same idea.

### Exoplayer
According to my decompilation, this is the player used by the twitch mobile app, seems like the best way to go for playback.<br>
https://github.com/google/ExoPlayer

### GSON
A library for json to java class conversion, once again used in the existing twitch mobile app.<br>
It makes sense to use because most twitch api is all about the json.<br>
https://github.com/google/gson
