package com.web.bean;

import com.web.bean.logger.Logger;
import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;

/**
 * Created by bizon on 23.03.2015.
 */
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY, getterVisibility = JsonAutoDetect.Visibility.NONE, setterVisibility = JsonAutoDetect.Visibility.NONE)
@JsonIgnoreProperties(ignoreUnknown = true)
public class CurrencyInfo {
    private String time;
    private String price;
    private String sum;
    private String text;

    private transient float priceF;
    private transient float sumF;

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getSum() {
        return sum;
    }

    public void setSum(String sum) {
        this.sum = sum;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public float getPriceF() {
        return priceF;
    }

    public void setPriceF(float priceF) {
        this.priceF = priceF;
    }

    public float getSumF() {
        return sumF;
    }

    public void setSumF(float sumF) {
        this.sumF = sumF;
    }

    public void convertStrToFloat() {
        try {
            this.priceF = Float.parseFloat(price.replaceAll(",", "."));
        } catch (NumberFormatException e) {
            Logger.error("BIZON convertStrToFloat: " + price, e);
        }

        try {
            this.sumF = Float.parseFloat(sum.replaceAll("\\$", "").replaceAll("\\s+", "").trim());
        } catch (NumberFormatException e) {
            Logger.error("BIZON convertStrToFloat: " + sum, e);
        }
    }

    @Override
    public String toString() {
        return "\nBIZON CurrencyInfo{" +
                "time='" + time + '\'' +
                ", price='" + price + '\'' +
                ", sum='" + sum + '\'' +
                ", text='" + text + '\'' +
                '}';
    }
}
