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

- Run it from the root path of the project with `java -classpath target/classes:lib/* io.hops.intrabody.IntrabodySPP`  
