# intrabody-client
This program uses the Bluetooth adapter of the machine to connect to an Android phone using Bluetooth sockets. Code 
has been adopted from [https://github.com/gth828r/sprime](https://github.com/gth828r/sprime)


## Requirements
*IntrabodySPPClient* has been tested on Ubuntu 16.04 LTS. To build and run it you need to do the following:

```
sudo apt-get update
sudo apt-get install bluetooth libbluetooth-dev
# If there are missing librarires at runtime, install these as well 
# libbluetooth-dev libboost-thread-dev libglib2.0-dev
```
- Bluetooth adapter active on your laptop
- Define the **record** to be sent, in `src/main/resources/record.txt`. It must be a *one-line* string and replace 
the placeholder with the values to be sent.

Template:
```
<temp>,<humidity>,<gRPS>,<gStrech0>,<gStrech1>,<gStrech2>,<gStrech3>
``` 

Example:
```
12.6,45.4,5,01,10,20,30
```


- Run it with
```
java -Dfile.encoding=UTF-8 -classpath /home/theo/hops/intrabody-client/target/classes:/home/theo/hops/intrabody-client/lib/bluecove-2.1.1-SNAPSHOT.jar:/home/theo/hops/intrabody-client/lib/bluecove-gpl-2.1.1-SNAPSHOT.jar:/home/theo/hops/intrabody-client/lib/bluecove-bluez-2.1.1-SNAPSHOT.jar io.hops.intrabody.IntrabodySPP client
```


## Troubleshoot
On Ubuntu you may need to do the following 
- [BlueCove with Bluez chucks “Can not open SDP session. [2] No such file or directory”](https://stackoverflow.com/questions/30946821/bluecove-with-bluez-chucks-can-not-open-sdp-session-2-no-such-file-or-direct)
- If bluetooth adapter cannot discover devices, you need download and install [BRCM](https://github.com/winterheart/broadcom-bt-firmware/tree/master/brcm) under 
`/lib/firmware/brcm`. The file you download needs to match the bluetooth adapter of your laptop. Useful information 
is available [here](https://askubuntu.com/questions/1032417/ubuntu-18-04-lts-bluetooth-0cf33004-discovery-not-working). **Attention**: You may need to rename the driver because Ubuntu expects a slightly different name for it. 
To find out if that's the case, you will see an error in the output of this `lsusb; rfkill list; dmesg | egrep -i 'blue|firm'`