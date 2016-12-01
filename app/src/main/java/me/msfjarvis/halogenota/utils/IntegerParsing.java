package me.msfjarvis.halogenota.utils;

import java.text.DecimalFormat;

public class IntegerParsing {

        public static String sizeFormat(long size) {
            if(size <= 0)
                return null;
            float newSize = size;
            String unit = " B";
            if (newSize > 1024) {
                unit = " KiB";
                newSize = newSize / 1024;
            }
            if (newSize >= 1024) {
                unit = " MiB";
                newSize = newSize / 1024;
            }
            if (newSize >= 1024) {
                unit = " GiB";
                newSize = newSize / 1024;
            }
            if (newSize >= 1024) {
                unit = " TiB";
                newSize = newSize / 1024;
            }
            if (newSize >= 1024) {
                unit = ". Wrong size.";
                newSize = 0;
            }
            return (new DecimalFormat("#0.00").format(newSize) + unit);
        }
}
