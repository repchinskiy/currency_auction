package com.web.bean;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;

import java.util.List;

/**
 * Created by bizon on 23.03.2015.
 */
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY, getterVisibility = JsonAutoDetect.Visibility.NONE, setterVisibility = JsonAutoDetect.Visibility.NONE)
@JsonIgnoreProperties(ignoreUnknown = true)
public class CurrencyResponse implements IResponse {
    @JsonProperty("result")
    List<CurrencyInfo> currencyInfoList;

    public List<CurrencyInfo> getCurrencyInfoList() {
        return currencyInfoList;
    }

    public void setCurrencyInfoList(List<CurrencyInfo> currencyInfoList) {
        this.currencyInfoList = currencyInfoList;
    }

    @Override
    public String toString() {
        return "BIZON CurrencyResponse{" +
                "currencyInfoList=" + currencyInfoList +
                '}';
    }
}
