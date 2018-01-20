package com.crypto.price;

import com.crypto.cloudinary.CloudinaryHelper;
import com.crypto.utils.ApiUtils;
import com.crypto.utils.DbUtils;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * Because I'm only retrieving the price of Ethereum, it is currently not necessary
 * to write an entire API to support all functions by Crytocompare.
 */
public class PriceChecker {

    private static final Logger logger = LoggerFactory.getLogger(PriceChecker.class);

    public PriceChecker() {}

    /**
     * Get the current price of ether from the CryptoCompare API
     * @return value
     */
    public static Double getCurrentEtherPrice() {
        String requestUrl = "https://min-api.cryptocompare.com/data/price?fsym=ETH&tsyms=USD";

        try {
            JSONObject json = ApiUtils.getResponseBody(requestUrl);
            Object value = json.get("USD");
            return (Double) value;
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        return null;
    }

    /**
     * Determines if the current ether price is valid for creating a meme
     */
    public static void processEtherPrice() {
        Double currentEtherPrice = getCurrentEtherPrice();

        logger.info("Current ether price: {}", currentEtherPrice.toString());

        DbUtils db = new DbUtils();
        Double historicalMaxEtherPrice = db.getHighestEtherValue();

        logger.info("Current highest ether value: {}", historicalMaxEtherPrice.toString());

        Integer lowestHundredIncrement = (currentEtherPrice.intValue() / 100) * 100;

        if (lowestHundredIncrement > historicalMaxEtherPrice) {
            CloudinaryHelper cloudinary = new CloudinaryHelper();
            cloudinary.sendMeme(lowestHundredIncrement);

            db.insertNewIncrement(lowestHundredIncrement);
            logger.info("Inserted new Ether value {} into database", lowestHundredIncrement);
        }
    }
}
