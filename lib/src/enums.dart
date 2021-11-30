/*
 * flutter_sunmi_printer
 * Created by Andrey U.
 * 
 * Copyright (c) 2020. All rights reserved.
 * See LICENSE for distribution and usage details.
 */

class SunmiAlign {
  const SunmiAlign._internal(this.value);
  final int value;
  static const left = SunmiAlign._internal(0);
  static const center = SunmiAlign._internal(1);
  static const right = SunmiAlign._internal(2);
}

class SunmiSize {
  const SunmiSize._internal(this.value);
  final int value;
  static const xs = SunmiSize._internal(1);
  static const sm = SunmiSize._internal(2);
  static const md = SunmiSize._internal(3);
  static const lg = SunmiSize._internal(4);
  static const xl = SunmiSize._internal(5);
}

class SunmiBarCodeType {
  const SunmiBarCodeType._internal(this.value);
  final int value;
  static const upca = SunmiBarCodeType._internal(0);
  static const upce = SunmiBarCodeType._internal(1);
  static const ean13 = SunmiBarCodeType._internal(2);
  static const ean8 = SunmiBarCodeType._internal(3);
  static const code39 = SunmiBarCodeType._internal(4);
  static const itf = SunmiBarCodeType._internal(5);
  static const codebar = SunmiBarCodeType._internal(6);
  static const code93 = SunmiBarCodeType._internal(7);
  static const code128 = SunmiBarCodeType._internal(8);
}

class SunmiBarCodeTextPosition {
  const SunmiBarCodeTextPosition._internal(this.value);
  final int value;
  static const notToPrint = SunmiBarCodeTextPosition._internal(0);
  static const upward = SunmiBarCodeTextPosition._internal(1);
  static const downward = SunmiBarCodeTextPosition._internal(2);
  static const upwardDownward = SunmiBarCodeTextPosition._internal(3);
}
