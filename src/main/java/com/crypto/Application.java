package com.crypto;

import com.crypto.cloudinary.CloudinaryHelper;

public class Application {

    public static void main(String[] args) {
        CloudinaryHelper cloudinary = new CloudinaryHelper();
        cloudinary.sendMeme(1000);
    }
}
