package com.eronor.k2_printer;

import androidx.annotation.NonNull;

import io.flutter.embedding.engine.plugins.FlutterPlugin;
import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.MethodChannel.MethodCallHandler;
import io.flutter.plugin.common.MethodChannel.Result;
import io.flutter.plugin.common.PluginRegistry.Registrar;

public class K2PrinterPlugin implements FlutterPlugin, MethodCallHandler {
    private MethodChannel channel;
    private static K2PrinterModule k2PrinterModule;

    private String RESET = "reset";
    private String START_PRINT = "startPrint";
    private String STOP_PRINT = "stopPrint";
    private String IS_PRINTING = "isPrinting";
    private String BOLD_ON = "boldOn";
    private String BOLD_OFF = "boldOff";
    private String UNDERLINE_ON = "underlineOn";
    private String UNDERLINE_OFF = "underlineOff";
    private String EMPTY_LINES = "emptyLines";
    private String PRINT_TEXT = "printText";
    private String PRINT_ROW = "printRow";
    private String PRINT_IMAGE = "printImage";
    private String CUT_PAPER = "cutPaper";
    private String PRINT_BARCODE = "printBarcode";

    @Override
    public void onAttachedToEngine(@NonNull FlutterPluginBinding flutterPluginBinding) {
        channel = new MethodChannel(flutterPluginBinding.getFlutterEngine().getDartExecutor(), "k2_printer");
        channel.setMethodCallHandler(this);
        k2PrinterModule = new K2PrinterModule();
        k2PrinterModule.initAidl(flutterPluginBinding.getApplicationContext());
    }

    // This static function is optional and equivalent to onAttachedToEngine. It
    // supports the old pre-Flutter-1.12 Android projects. You are encouraged to
    // continue supporting plugin registration via this function while apps migrate
    // to use the new Android APIs post-flutter-1.12 via
    // https://flutter.dev/go/android-project-migration.
    //
    // It is encouraged to share logic between onAttachedToEngine and registerWith
    // to keep them functionally equivalent. Only one of onAttachedToEngine or
    // registerWith will be called depending on the user's project.
    // onAttachedToEngine or registerWith must both be defined in the same class.
    public static void registerWith(Registrar registrar) {
        final MethodChannel channel = new MethodChannel(registrar.messenger(), "k2_printer");
        channel.setMethodCallHandler(new K2PrinterPlugin());
        k2PrinterModule = new K2PrinterModule();
        k2PrinterModule.initAidl(registrar.context());
    }

    @Override
    public void onMethodCall(@NonNull MethodCall call, @NonNull Result result) {
        if (call.method.equals(RESET)) {
            k2PrinterModule.reset();
            result.success(null);
        } else if (call.method.equals(START_PRINT)) {
            k2PrinterModule.startPrint();
            result.success(null);
        } else if (call.method.equals(STOP_PRINT)) {
            k2PrinterModule.stopPrint();
            result.success(null);
        } else if (call.method.equals(IS_PRINTING)) {
            result.success(k2PrinterModule.isPrinting());
        } else if (call.method.equals(BOLD_ON)) {
            k2PrinterModule.boldOn();
            result.success(null);
        } else if (call.method.equals(BOLD_OFF)) {
            k2PrinterModule.boldOff();
            result.success(null);
        } else if (call.method.equals(UNDERLINE_ON)) {
            k2PrinterModule.underlineOn();
            result.success(null);
        } else if (call.method.equals(UNDERLINE_OFF)) {
            k2PrinterModule.underlineOff();
            result.success(null);
        } else if (call.method.equals(PRINT_TEXT)) {
            String text = call.argument("text");
            int align = call.argument("align");
            boolean bold = call.argument("bold");
            boolean underline = call.argument("underline");
            int linesAfter = call.argument("linesAfter");
            int size = call.argument("size");
            k2PrinterModule.text(text, align, bold, underline, size, linesAfter);
            result.success(null);
        } else if (call.method.equals(EMPTY_LINES)) {
            int n = call.argument("n");
            k2PrinterModule.emptyLines(n);
            result.success(null);
        } else if (call.method.equals(PRINT_ROW)) {
            String cols = call.argument("cols");
            boolean bold = call.argument("bold");
            boolean underline = call.argument("underline");
            int textSize = call.argument("textSize");
            int linesAfter = call.argument("linesAfter");
            k2PrinterModule.row(cols, bold, underline, textSize, linesAfter);
            result.success(null);
        } else if (call.method.equals(PRINT_IMAGE)) {
            String base64 = call.argument("base64");
            int align = call.argument("align");
            k2PrinterModule.printImage(base64, align);
            result.success(null);
        } else if (call.method.equals(CUT_PAPER)) {
            k2PrinterModule.cutPaper();
            result.success(null);
        } else if (call.method.equals(PRINT_BARCODE)) {
            String data = call.argument("data");
            int barcodeType = call.argument("barCodeType");
            int width = call.argument("width");
            int height = call.argument("height");
            int textPosition = call.argument("textPosition");
            System.out.println(data + " " + barcodeType + " " + width+ " " + height+ " " + textPosition);
            k2PrinterModule.printBarCode(data, barcodeType,width ,height, textPosition);
            result.success(null);
        } else {
            result.notImplemented();
        }
    }

    @Override
    public void onDetachedFromEngine(@NonNull FlutterPluginBinding binding) {
        channel.setMethodCallHandler(null);
    }
}
