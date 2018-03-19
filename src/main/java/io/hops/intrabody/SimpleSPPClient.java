package io.hops.intrabody;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.Vector;

import javax.bluetooth.DeviceClass;
import javax.bluetooth.DiscoveryAgent;
import javax.bluetooth.DiscoveryListener;
import javax.bluetooth.LocalDevice;
import javax.bluetooth.RemoteDevice;
import javax.bluetooth.ServiceRecord;
import javax.bluetooth.UUID;
import javax.microedition.io.Connector;
import javax.microedition.io.StreamConnection;

/**
 * A simple SPP client that connects with an SPP server
 */
public class SimpleSPPClient implements DiscoveryListener {
  
  
  public static void main(String[] args) throws IOException {
    System.out.println("\n" + "Testing BT Client" + "\n");
    SimpleSPPClient sppClient = new SimpleSPPClient();
    sppClient.runClient();
  }
  
  // object used for waiting
  private static Object lock = new Object();
  
  // vector containing the devices discovered
  private static Vector<RemoteDevice> vecDevices = new Vector<RemoteDevice>();
  
  // device connection address
  private static String connectionURL = null;
  
  /**
   * runs a bluetooth client that sends a string to a server and prints the response
   */
  public void runClient() throws IOException {
    
    // display local device address and name
    LocalDevice localDevice = LocalDevice.getLocalDevice();
    System.out.println("Address: " + localDevice.getBluetoothAddress());
    System.out.println("Name: " + localDevice.getFriendlyName());
    
    // find devices
    DiscoveryAgent agent = localDevice.getDiscoveryAgent();
    System.out.println("Starting device inquiry...");
    agent.startInquiry(DiscoveryAgent.GIAC, this);
    
    // avoid callback conflicts
    try {
      synchronized (lock) {
        lock.wait();
      }
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    
    System.out.println("Device Inquiry Completed. ");
    
    // print all devices in vecDevices
    int deviceCount = vecDevices.size();
    if (deviceCount <= 0) {
      System.out.println("No Devices Found .");
    } else {
      // print bluetooth device addresses and names in the format [ No. address (name) ]
      System.out.println("Bluetooth Devices: ");
      for (int i = 0; i < deviceCount; i++) {
        RemoteDevice remoteDevice = (RemoteDevice) vecDevices.elementAt(i);
        try {
          System.out.println((i + 1) + ". " + remoteDevice.getBluetoothAddress() + " (" + remoteDevice.getFriendlyName(true) + ")");
        } catch (IOException ioe) {
          ioe.printStackTrace();
        }
      }
    }
    
    // prompt user
    System.out.print("Choose the device to search for service : ");
    BufferedReader bReader = new BufferedReader(new InputStreamReader(System.in));
    String chosenIndex = bReader.readLine();
    int index = Integer.parseInt(chosenIndex.trim());
    
    // check for services
    RemoteDevice remoteDevice = (RemoteDevice) vecDevices.elementAt(index - 1);
    UUID[] uuidSet = new UUID[1];
    
    uuidSet[0] = new UUID("1101", true); // serial, SPP
    // uuidSet[0] = new UUID("0003", true); // rfcomm
    // uuidSet[0] = new UUID("1106", true); // obex file transfer
    // uuidSet[0] = new UUID("1105", true); // obex obj push
    
    System.out.println("\nSearching for services...");
    agent.searchServices(null, uuidSet, remoteDevice, this);

    // avoid callback conflicts
    try {
      synchronized (lock) {
        lock.wait();
      }
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    //connectionURL = "00001101-0000-1000-8000-00805f9b34fa";// "54:40:AD:1B:FC:00";
    // check
    if (connectionURL == null) {
      System.out.println("Device does not support Service.");
      System.exit(0);
    }
    
    // connect to the server
    StreamConnection connection = null;
    try {
      connection = (StreamConnection) Connector.open(connectionURL);
    } catch (Exception e) {
      e.printStackTrace();
      System.exit(0);
    }
    System.out.println("Connected to server.");
    
    // send string
    // OutputStream outStream = streamConnection.openOutputStream();
    // PrintWriter pWriter = new PrintWriter(new OutputStreamWriter(outStream));
    // pWriter.write("Test String from SPP Client\r\n");
    // pWriter.flush();
    
    // send string
    Thread sendT = new Thread(new sendLoop(connection));
    sendT.start();
    
    // read response
    // InputStream inStream = streamConnection.openInputStream();
    // BufferedReader bReader2 = new BufferedReader(new InputStreamReader(inStream));
    // String lineRead = bReader2.readLine();
    // System.out.println(lineRead);
    
    // read response
    Thread recvT = new Thread(new recvLoop(connection));
    recvT.start();
    
    System.out.println("\nClient threads started");
    
    // stay alive
    while (true) {
      try {
        Thread.sleep(2000);
        // System.out.println("\nClient looping.");
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }
    
  } // runClient
  
  // methods of DiscoveryListener
  public void deviceDiscovered(RemoteDevice btDevice, DeviceClass cod) {
    // add the device to the vector
    if (!vecDevices.contains(btDevice)) {
      vecDevices.addElement(btDevice);
    }
  }
  
  // implement this method since services are not being discovered
  public void servicesDiscovered(int transID, ServiceRecord[] servRecord) {
    if (servRecord != null && servRecord.length > 0) {
      connectionURL = servRecord[0].getConnectionURL(0, false);
    }
    synchronized (lock) {
      lock.notify();
    }
  }
  
  // implement this method since services are not being discovered
  public void serviceSearchCompleted(int transID, int respCode) {
    synchronized (lock) {
      lock.notify();
    }
  }
  
  public void inquiryCompleted(int discType) {
    synchronized (lock) {
      lock.notify();
    }
    
  }
  
  private static class recvLoop implements Runnable {
    private StreamConnection connection = null;
    private InputStream inStream = null;
    
    public recvLoop(StreamConnection c) {
      this.connection = c;
      try {
        this.inStream = this.connection.openInputStream();
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
    
    public void run() {
      while (true) {
        try {
          
          BufferedReader bReader = new BufferedReader(new InputStreamReader(inStream));
          String lineRead = bReader.readLine();
          if(lineRead != null) {
            System.out.println("Client recv: " + lineRead);
          }
          Thread.sleep(200);
        } catch (Exception e) {
          e.printStackTrace();
        }
      }
    }
  } // recvLoop
  
  private static class sendLoop implements Runnable {
    private StreamConnection connection = null;
    PrintWriter pWriter = null;
    
    public sendLoop(StreamConnection c) {
      this.connection = c;
      OutputStream outStream;
      try {
        outStream = this.connection.openOutputStream();
        this.pWriter = new PrintWriter(new OutputStreamWriter(outStream));
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
    
    public void run() {
      while (true) {
        try {
          // prompt
          System.out.println("Sending record message: ");
          //BufferedReader bReader = new BufferedReader(new InputStreamReader(System.in));
          String inputline = "{latitude: 999.55515634145839, longitude: 47.75230306919155, bootMillis: 178615447, " +
              "bootNum: 0, deviceUUID: f6214976-ccae-48f0-9aa9-97d09b22ba6d, epochMillis: 1517235415807, recordUUID: " +
              "fe7b0edf-105b-4d7e-95d2-d03ed85a39bb}";//bReader.readLine();
          pWriter.println(inputline);
          pWriter.flush();
          System.out.println("Client send: " + inputline+"\r\n");
          Thread.sleep(2000);
        } catch (Exception e) {
          e.printStackTrace();
        }
      }
    }
  } // sendLoop
  
}
