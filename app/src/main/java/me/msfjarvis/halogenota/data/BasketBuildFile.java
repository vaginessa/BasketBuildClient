package me.msfjarvis.halogenota.data;

import me.msfjarvis.halogenota.utils.IntegerParsing;

public class BasketBuildFile {
    public String filename;
    public String url;
    public String file_size;
    public String hDate;

    public BasketBuildFile(String f, String u, String s, String hDate) {
        filename = f;
        url = u;
        this.hDate = hDate;
        file_size = IntegerParsing.sizeFormat(Integer.parseInt(s));
    }
}