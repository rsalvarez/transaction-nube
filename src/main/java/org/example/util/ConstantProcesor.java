package org.example.util;

import org.springframework.beans.factory.annotation.Value;

public class ConstantProcesor {
    @Value("${variable.value.host.numerator}")
    public static String urlNumerator;
    @Value("${variable.value.path.get.numerator}")
    public static String pathgetNumerator;

    public static final String OK = "OK";
    public static final String FAILED = "FAILED";

}
