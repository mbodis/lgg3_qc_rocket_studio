package sk.svb.lgg3.svb_circlecase_rocket.UsbDevice;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbManager;
import android.util.Log;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Iterator;

import sk.svb.lgg3.svb_circlecase_rocket.R;
import sk.svb.lgg3.svb_circlecase_rocket.logic.ServoLimits;
import sk.svb.lgg3.svb_circlecase_rocket.service.UsbDeviceService;

/**
 * Created by mbodis on 2/12/16.
 */
public class ServoController {

    public static final String TAG = ServoController.class.getName();

    private static final int MY_USB_VENDOR_ID = 4616;
    private static final int MY_USB_PRODUCT_ID3 = 2069;


    /**
     * send direct position to servo
     * @param ctx
     * @param servoLocation
     * @param servoSpeed
     */
    public static void sendMessage(Context ctx, int servoLocation, int servoSpeed){
        String msg = "#1P"+servoLocation+"T"+servoSpeed+"\r\n";

        Intent mIntent = new Intent(UsbDeviceService.SEND_DATA_INTENT);
        mIntent.putExtra(UsbDeviceService.DATA_EXTRA, msg.getBytes());
        ctx.sendBroadcast(mIntent);
    }

    /**
     * calcualte center coordiante and send message to servo
     * @param ctx
     * @param width
     * @param gateCenterX
     * @param locCenter
     * @param locLeftMax
     * @param locRightMax
     */
    public static void sendMessageCenter(Context ctx, int width, int gateCenterX,
                                         int locCenter, int locLeftMax, int locRightMax){
        // move to left
        if (width /2 > gateCenterX){
            int rangeRot = Math.abs(locCenter-locLeftMax);
            double ratio = (double)gateCenterX/((double)width/2);

            int res = locLeftMax + (int)((double)rangeRot*ratio);
            Log.d(TAG, "gateCenterX: " + gateCenterX);
            Log.d(TAG, "rangeRot: " + rangeRot + " ratio:" + ratio + " LEFT: " + res);
            sendMessage(ctx, res, ServoLimits.SPEED_NORMAL);

        // move to right
        }else{
            int rangeRot = Math.abs(locCenter - locRightMax);
            double ratio = ((double)gateCenterX-(width/2)) / ((double)width/2);
            int res = locCenter + (int)((double)rangeRot*ratio);

            Log.d(TAG, "gateCenterX: " + gateCenterX);
            Log.d(TAG, "rangeRot: " + rangeRot + " ratio:" + ratio + " RIGHT: " + res);
            sendMessage(ctx, res, ServoLimits.SPEED_NORMAL);
        }
    }

    public static void findDeviceAndStartService(Context ctx) {
        UsbManager usbManager = (UsbManager) ctx.getSystemService(Context.USB_SERVICE);
        UsbDevice usbDevice = null;
        HashMap<String, UsbDevice> usbDeviceList = usbManager.getDeviceList();
        Log.d(TAG, "length: " + usbDeviceList.size());
        Iterator<UsbDevice> deviceIterator = usbDeviceList.values().iterator();
        while (deviceIterator.hasNext()) {
            UsbDevice tempUsbDevice = deviceIterator.next();

            // Print device information. If you think your device should be able
            // to communicate with this app, add it to accepted products below.
            Log.d(TAG, "VendorId: " + tempUsbDevice.getVendorId());
            Log.d(TAG, "ProductId: " + tempUsbDevice.getProductId());
            Log.d(TAG, "DeviceName: " + tempUsbDevice.getDeviceName());
            Log.d(TAG, "DeviceId: " + tempUsbDevice.getDeviceId());
            Log.d(TAG, "DeviceClass: " + tempUsbDevice.getDeviceClass());
            Log.d(TAG, "DeviceSubclass: " + tempUsbDevice.getDeviceSubclass());
            Log.d(TAG, "InterfaceCount: " + tempUsbDevice.getInterfaceCount());
            Log.d(TAG, "DeviceProtocol: " + tempUsbDevice.getDeviceProtocol());

            if (tempUsbDevice.getVendorId() == MY_USB_VENDOR_ID){
                Log.i(TAG, "My device found!");

                if (tempUsbDevice.getProductId() == MY_USB_PRODUCT_ID3) {
                    Toast.makeText(ctx, "My usb device" + ctx.getString(R.string.found), Toast.LENGTH_SHORT).show();
                    usbDevice = tempUsbDevice;
                }
            }
        }

        if (usbDevice == null) {
            Log.i(TAG, "No device found!");
            Toast.makeText(ctx, ctx.getString(R.string.no_device_found), Toast.LENGTH_LONG).show();
        } else {
            Log.i(TAG, "Device found!");
            Intent startIntent = new Intent(ctx, UsbDeviceService.class);
            PendingIntent pendingIntent = PendingIntent.getService(ctx, 0, startIntent, 0);
            usbManager.requestPermission(usbDevice, pendingIntent);
        }
    }

}
