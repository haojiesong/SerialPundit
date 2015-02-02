package test20;

import com.embeddedunveiled.serial.SerialComManager;
import com.embeddedunveiled.serial.SerialComManager.BAUDRATE;
import com.embeddedunveiled.serial.SerialComManager.DATABITS;
import com.embeddedunveiled.serial.SerialComManager.FLOWCONTROL;
import com.embeddedunveiled.serial.SerialComManager.PARITY;
import com.embeddedunveiled.serial.SerialComManager.STOPBITS;
import com.embeddedunveiled.serial.ISerialComDataListener;
import com.embeddedunveiled.serial.SerialComDataEvent;

class Data0 implements ISerialComDataListener{
	@Override
	public void onNewSerialDataAvailable(SerialComDataEvent data) {
		System.out.println("0 Read from serial port : " + new String(data.getDataBytes()) + "\n");
	}
}

class Data1 implements ISerialComDataListener{
	@Override
	public void onNewSerialDataAvailable(SerialComDataEvent data) {
		System.out.println("1 Read from serial port : " + new String(data.getDataBytes()) + "\n");
	}
}

public class Test20 {
	public static void main(String[] args) {
		
		long handle = 0;
		SerialComManager scm = new SerialComManager();
		
		// instantiate class which is will implement ISerialComDataListener interface
		Data0 dataListener0 = new Data0();
		Data1 dataListener1 = new Data1();
		
		try {
			// open and configure port that will listen data
			handle = scm.openComPort("/dev/ttyUSB0", true, true, false);
			scm.configureComPortData(handle, DATABITS.DB_8, STOPBITS.SB_1, PARITY.P_NONE, BAUDRATE.B115200, 0);
			scm.configureComPortControl(handle, FLOWCONTROL.SOFTWARE, '$', '#', false, false);
			scm.registerDataListener(handle, dataListener0);
			
			// open and configure port which will send data
			long handle1 = scm.openComPort("/dev/ttyUSB1", true, true, false);
			scm.configureComPortData(handle1, DATABITS.DB_8, STOPBITS.SB_1, PARITY.P_NONE, BAUDRATE.B115200, 0);
			scm.configureComPortControl(handle1, FLOWCONTROL.SOFTWARE, '$', '#', false, false);
			scm.registerDataListener(handle, dataListener1);
			
			scm.writeString(handle, "#", 0);
			
			scm.writeString(handle1, "17", 0);


			Thread.sleep(2000);
			// close the port releasing handle
			scm.unregisterDataListener(dataListener0);
			scm.unregisterDataListener(dataListener1);
			scm.closeComPort(handle);
			scm.closeComPort(handle1);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}