package com.eronor.k2_printer.utils;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.IBinder;
import android.os.RemoteException;
import android.widget.Toast;


import com.eronor.k2_printer.entities.TableItem;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import com.sunmi.extprinterservice.ExtPrinterService;

public class AidlUtil {
    private static final String SERVICE＿PACKAGE = "com.sunmi.extprinterservice";
    private static final String SERVICE＿ACTION = "com.sunmi.extprinterservice.PrinterService";

    private ExtPrinterService printerService;
    private static AidlUtil mAidlUtil = new AidlUtil();
    private static final int LINE_BYTE_SIZE = 32;
    private Context context;

    private AidlUtil() {
    }

    public static AidlUtil getInstance() {
        return mAidlUtil;
    }

    /**
     * 连接服务
     *
     * @param context context
     */
    public void connectPrinterService(Context context) {
        this.context = context.getApplicationContext();
        Intent intent = new Intent();
        intent.setPackage(SERVICE＿PACKAGE);
        intent.setAction(SERVICE＿ACTION);
        context.getApplicationContext().startService(intent);
        context.getApplicationContext().bindService(intent, connService, Context.BIND_AUTO_CREATE);
    }

    /**
     * 断开服务
     *
     * @param context context
     */
    public void disconnectPrinterService(Context context) {
        if (printerService != null) {
            context.getApplicationContext().unbindService(connService);
            printerService = null;
        }
    }

    public boolean isConnect() {
        return printerService != null;
    }

    private ServiceConnection connService = new ServiceConnection() {

        @Override
        public void onServiceDisconnected(ComponentName name) {
            printerService = null;
        }

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            printerService = ExtPrinterService.Stub.asInterface(service);
        }
    };

    /**
     * 设置打印浓度
     */
    private int[] darkness = new int[] { 0x0600, 0x0500, 0x0400, 0x0300, 0x0200, 0x0100, 0, 0xffff, 0xfeff, 0xfdff,
            0xfcff, 0xfbff, 0xfaff };

    public void setDarkness(int index) {
         if (printerService == null) {
             return;
         }

         int k = darkness[index];
         try {
             printerService.sendRawData(ESCUtil.setPrinterDarkness(k), null);
         } catch (RemoteException e) {
             e.printStackTrace();
         }
    }

    public int getPrinterStatus(){
        if(printerService == null){
            Toast.makeText(context,"Service disconnected",Toast.LENGTH_LONG).show();
            return -1;
        }

        int res;
        try {
            res = printerService.getPrinterStatus();
        } catch (RemoteException e) {
            e.printStackTrace();
            res = -1;
        }
        return res;
    }

    /**
     * 初始化打印机
     */
   public void initPrinter() {
        if (printerService == null) {
            return;
        }

        try {
            printerService.printerInit();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    // TODO Second parameter will check setFontZoom(first,second)
    public void setFontSize(int size) {
        try {
            printerService.setFontZoom(size,size);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    /**
     * 打印二维码
     */
    public void printQr(String data, int modulesize, int errorlevel) {
        if (printerService == null) {
            return;
        }

        try {
            printerService.setAlignMode(1);
            printerService.printQrCode(data, modulesize, errorlevel);
            printerService.lineWrap(2);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    /**
     * 打印条形码
     */
    public void printBarCode(String data, int symbology, int height, int width, int textposition) {
        if (printerService == null) {
            return;
        }
        try {
            printerService.printBarCode(data, symbology, height, width, textposition);
            printerService.lineWrap(2);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    /**
     * 打印文字
     */
    public void printText(String content, int size,int alignment,boolean isBold, boolean isUnderLine) {
        if (printerService == null) {
            return;
        }
        try {
            if (isBold) {
                printerService.sendRawData(ESCUtil.boldOn());
            } else {
                printerService.sendRawData(ESCUtil.boldOff());
            }

            if (isUnderLine) {
                printerService.sendRawData(ESCUtil.underlineWithOneDotWidthOn());
            } else {
                printerService.sendRawData(ESCUtil.underlineOff());
            }
            printerService.setAlignMode(alignment);
            printerService.setFontZoom(size,2);
            printerService.printText(content);
            printerService.lineWrap(3);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    /*
     * 打印图片
     */
    public void printBitmap(Bitmap bitmap, int align) {

        // 0: common  1：Double width 2：Double height 3：Double height & double width

        if (printerService == null) {
            return;
        }

        try {
            printerService.setAlignMode(align);
            printerService.printBitmap(bitmap, 0);
            printerService.lineWrap(1);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public void lineWrap(int line) {
        try {
            printerService.lineWrap(line);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    /**
     * 打印图片和文字按照指定排列顺序
     */
    public void printBitmap2(Bitmap bitmap, int orientation) {
        if (printerService == null) {
            return;
        }
        try {
            if (orientation == 0) {
                printerService.printBitmap(bitmap,0);
                printerService.printText("\nHorizantal\n");
                printerService.printBitmap(bitmap,0);
                printerService.printText("\nHorizantal\n");
            } else {
                printerService.printBitmap(bitmap,0);
                printerService.printText("\nVertical\n");
                printerService.printBitmap(bitmap,0);
                printerService.printText("\nVertical\n");
            }
            printerService.lineWrap(2);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 打印表格
     */
    public void printTable(LinkedList<TableItem> list) {
        if (printerService == null) {
            return;
        }
        try {
            for (TableItem tableItem : list) {
                printerService.printColumnsText(tableItem.getText(), tableItem.getWidth(), tableItem.getAlign());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 打印表格项
     */
    public void printTableItem(String[] text, int[] width, int[] align) {
        if (printerService == null) {
            return;
        }
        try {
            printerService.printColumnsText(text, width, align);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    /*
     * 空打三行！
     */
    public void print3Line() {
        if (printerService == null) {
            return;
        }

        try {
            printerService.lineWrap(3);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public void sendRawData(byte[] data) {
        if (printerService == null) {
            return;
        }

        try {
            printerService.sendRawData(data);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public void cutPaper() {
        if (printerService == null) {
            return;
        }

        try {
            printerService.cutPaper(0, 0);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    // public void sendRawDatabyBuffer(byte[] data, ICallback iCallback) {
    //     if (woyouService == null) {
    //         return;
    //     }

    //     try {
    //         woyouService.enterPrinterBuffer(true);
    //         woyouService.sendRAWData(data, iCallback);
    //         woyouService.exitPrinterBufferWithCallback(true, iCallback);
    //     } catch (RemoteException e) {
    //         e.printStackTrace();
    //     }
    // }
}
