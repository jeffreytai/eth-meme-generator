package com.crypto.cloudinary;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.crypto.slack.SlackClient;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.Map;
import java.util.Properties;

public class CloudinaryHelper {

    // Constants
    private static final String CLOUD_NAME = "ethereum-meme";
    private static final String BASE_DELIVERY_URL = "http://res.cloudinary.com/ethereum-meme";
    private static final String SECURE_DELIVER_URL = "https://res.cloudinary.com/ethereum-meme";
    private static final String API_BASE_URL = "https://api.cloudinary.com/v1_1/ethereum-meme";

    private final String PROPERTIES_FILE = "cloudinary.properties";
    private final String SLACK_ALERT_USERNAME = "ether-price-troll";

    // Credentials
    private String apiKey;
    private String apiSecret;

    // Member variables
    private Cloudinary cloudinary;

    public CloudinaryHelper() {
        Properties props = new Properties();

        try {
            InputStream stream = CloudinaryHelper.class.getClassLoader().getResourceAsStream(this.PROPERTIES_FILE);
            props.load(stream);
            stream.close();

            this.apiKey = props.getProperty("api-key");
            this.apiSecret = props.getProperty("api-secret");
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        Map config = ObjectUtils.asMap(
                "cloud_name", this.CLOUD_NAME,
                "api_key", this.apiKey,
                "api_secret", this.apiSecret
        );

        cloudinary = new Cloudinary(config);
    }

    /**
     * Apply proper transformations and captions to the meme and send it to appropriate channel in Slack
     * @param etherValue
     */
    public void sendMeme(Integer etherValue) {
        String imagePath = applyImageTransformations(etherValue);
        File captionedMeme = new File("captioned-meme.jpg");

        try {
            URL url = new URL(imagePath);

            // Create file object from contents of URL
            FileUtils.copyURLToFile(url, captionedMeme);
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        SlackClient slack = new SlackClient();
        slack.uploadFile(captionedMeme);
        slack.shutdown();
    }

    /**
     * Upload the picture stored locally to Cloudinary
     * @return the generated url for the picture
     */
    private String uploadPicture() {
        String memeFilename = "meme.jpg";

        // Retrieve the absolute file path
        String filePath = CloudinaryHelper.class.getClassLoader().getResource(memeFilename).getFile();

        try {
            Map options = ObjectUtils.asMap(
                    "public_id", "meme"
            );

            // Upload picture to Cloudinary
            Map uploadMap = cloudinary.uploader().upload(filePath, options);

            // Retrieve the url generated
            String generatedUrl = cloudinary.url().generate(memeFilename);

            return generatedUrl;
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        return null;
    }

    /**
     *
     * @param etherValue
     */
    public String applyImageTransformations(Integer etherValue) {
        String generatedUrl = uploadPicture();

        int lastSlashIndex = generatedUrl.lastIndexOf("/");
        String baseUrl = generatedUrl.substring(0, lastSlashIndex);
        String suffixUrl = generatedUrl.substring(lastSlashIndex);

        // Set picture to 500 pixels
        String widthTransformation = "w_500";

        // Format shared by both upper and lower caption
        String fontAndSize = "Impact_35";
        String padding = "y_20";
        String color = "co_rgb:ffffff";

        // Format the upper caption
        String upperCaption = "Y'ALL GOT ANYMORE OF THEM";
        String upperUrlFormattedCaption = upperCaption.replaceAll(" ", "%20");
        String upperPosition = "g_north";

        // Format the lower caption
        String lowerCaption = "CHEAP SUB " + etherValue.toString() + " ETH";
        String lowerUrlFormattedCaption = lowerCaption.replaceAll(" ", "%20");
        String lowerPosition = "g_south";

        StringBuilder upperTransformation = new StringBuilder();
        String upperUrl = upperTransformation
                .append("l_text:")
                .append(fontAndSize)
                .append(":")
                .append(upperUrlFormattedCaption)
                .append(",")
                .append(upperPosition)
                .append(",")
                .append(padding)
                .append(",")
                .append(color)
                .toString();

        StringBuilder lowerTransformation = new StringBuilder();
        String lowerUrl = lowerTransformation
                .append("l_text:")
                .append(fontAndSize)
                .append(":")
                .append(lowerUrlFormattedCaption)
                .append(",")
                .append(lowerPosition)
                .append(",")
                .append(padding)
                .append(",")
                .append(color)
                .toString();

        String transformedUrl = baseUrl + "/" + widthTransformation + "/" + upperTransformation + "/" + lowerTransformation + suffixUrl;

        return transformedUrl;
    }
}
