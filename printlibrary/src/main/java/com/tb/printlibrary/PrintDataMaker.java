package com.tb.printlibrary;

import java.util.List;

public interface PrintDataMaker {
    List<byte[]> getPrintData(int type);
}
