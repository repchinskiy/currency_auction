package com.service;

import com.web.bean.CurrencyInfo;
import com.web.bean.CurrencyResponse;

/**
 * Created by Bizon on 24.03.2015.
 */
public class Utils {
    public static void prepareResponse(CurrencyResponse currencyResponse) {
        if (currencyResponse != null && currencyResponse.getCurrencyInfoList() != null) {
            for (CurrencyInfo currencyInfo : currencyResponse.getCurrencyInfoList()) {
                if (currencyInfo.getText() != null) {
                    currencyInfo.setText(currencyInfo.getText().replaceAll("\n", "").replaceAll("\\s+", " ").trim());
                }
            }
        }
    }
}
