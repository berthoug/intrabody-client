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
- Define the **record** to be sent, in `src/main/resources/record.txt`. It must be a *one-line* valid json format 
string. 
Default is:
```
{dvcId: 18367, channelId: 123, value: 287.23, unit: 5, time:${time}, longitude: 47.75230306919155, bootMillis: 178615447, bootNum: 0, deviceUUID: f6214976-ccae-48f0-9aa9-97d09b22ba6d, epochMillis: 1517235415807, recordUUID: fe7b0edf-105b-4d7e-95d2-d03ed85a39bb}
 ``` 
`time` is automatically set by the program in runtime.

- Run it with
```
java -Dfile.encoding=UTF-8 -classpath <PATH_TO_INTRABODY_CLIENT_ROOT_DIR>/target/classes:<PATH_TO_INTRABODY_CLIENT_ROOT_DIR>/lib/bluecove-2.1.1-SNAPSHOT.jar:<PATH_TO_INTRABODY_CLIENT_ROOT_DIR>/lib/bluecove-gpl-2.1.1-SNAPSHOT.jar:~hops/intrabody-client/lib/bluecove-bluez-2.1.1-SNAPSHOT.jar io.hops.intrabody.IntrabodySPP client
```


## Troubleshoot
On Ubuntu you may need to do the following 
- [BlueCove with Bluez chucks “Can not open SDP session. [2] No such file or directory”](https://stackoverflow.com/questions/30946821/bluecove-with-bluez-chucks-can-not-open-sdp-session-2-no-such-file-or-direct)
- If bluetooth adapter cannot discover devices, you need download and install [BRCM](https://github.com/winterheart/broadcom-bt-firmware/tree/master/brcm) under 
`/lib/firmware/brcm`. The file you download needs to match the bluetooth adapter of your laptop. Useful information 
is available [here](https://askubuntu.com/questions/1032417/ubuntu-18-04-lts-bluetooth-0cf33004-discovery-not-working). **Attention**: You may need to rename the driver because Ubuntu expects a slightly different name for it. 
To find out if that's the case, you will see an error in the output of this `lsusb; rfkill list; dmesg | egrep -i 'blue|firm'`