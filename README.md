# YFindr
Final Project for AIT-Budapest Spring 2017 Mobile Development course

## Project Description
A crowdsourced Wi-Fi database that shows you the free Wi-Fi networks you can
use. Please only add networks that are not personal (someone's house, etc.) and
make sure the information is correct so that you don't lead people into a trap!

### Map Screen
You can see the map of the entire globe, and you can search for a specific area
in the world to see their wifi availability. Each of the locations that offer
free wifi are marked with pins: green marker means that the network is unlocked
and free to access, red means that you to might have to buy food/drinks from
the establishment in order to acquire the wifi password. Clicking on the marker
will show you the name of the network, and clicking on the bubble will show you
further details about the network.

### Nearby List
You can see a list of nearby networks based on your current location. The list
should contain wifi networks in the database that is within a walkable
distance (2.5km). Clicking on a list item will show you the same details screen
as the map screen.

### Emergency Navigation
The emergency navigation button will navigate you to the closest network in the
database based on your current location. It might not be a walkable distance,
since you are not within 2.5km away from the closest pin. However, it gives you
the closest one out of all the possibilities around the globe.

### Details Screen
Here you can see further details about the location, and you can leave a review
of the pin. You can only leave one review though! In the details screen, you should be able to see a "report" button in case the

In the navigation bar menu, you should be able to see a "report" button in case the
information is faulty - i.e. the wifi does not exist anymore or it is a personal
network in someone's home. Then, you can report it, and if there are more than 
100 reports, the pin will get deleted from the database.


## Functions
The database will cache the last fetched data, so even if you lose network
connection (when you're logged in), you can still see the networks. Unless you
log out on your own by the menu in the navigation drawer, you should be logged
in and be able to use the app as usual.

## External Libraries Used
Here are the numerous external libraries used in the project

### Design
* Intro screen: https://github.com/TangoAgency/material-intro-screen
* SmallBang: https://github.com/hanks-zyh/SmallBang
* Recycler ripple: https://github.com/balysv/material-ripple
* Fancy toasts: https://github.com/JohnPersano/SuperToasts

### Easier coding
* Boilerplate stuff: https://github.com/JakeWharton/butterknife
* Fragment management: https://github.com/ncapdevi/FragNav
* Easy object parceling: https://github.com/johncarl81/parceler

