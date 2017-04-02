# CrowdSpeaker
_ Imagine the following situation: You and your mates are in a protest and you forgot your loudspeaker. You smart kids grab your phone and start CrowdSpeaker. The app connects all your devices via WifiP2p and you are able to speak into your phone. All the other phones will start playing your voice in no time and it sounds bombastic. _

That was the idea....

But sadly the Android WifiP2P world is damn complicated and we did not manage to archive our goals.

## What workvvs
The devices connect over the WifiP2P interfaces and connect into their own network. Sometimes they are able to transmit messages and also the audio stream is ready to go.

## Challenges that we suffered
- This very case is not well documented and we had to dig through all sorts of documentation and stack overflow questions, but in the end only trial and error helped us.
- The IPv4 stack in Android is pretty poor implemented and depends totally on the vendor -> After hours of swearing, IPv6 did the trick
- Sometimes the connection is established, not via P2P - but over the internet...bye bye 1GB of data :/

## What we learned
We had a lot fun playing around with Android, because for most of us, it was our first Android project. It's kind of a cool to realise that you work on a topic only few people worked on before...that feeling helps us through the hard hours of documentation reading.

# <3 Happy Hacking
