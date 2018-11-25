package com.example.montana.hackathonproject;

import android.util.Log;

import com.google.api.gax.core.FixedCredentialsProvider;
import com.google.auth.Credentials;
import com.google.auth.oauth2.ServiceAccountCredentials;
import com.google.cloud.vision.v1.AnnotateImageRequest;
import com.google.cloud.vision.v1.AnnotateImageResponse;
import com.google.cloud.vision.v1.BatchAnnotateImagesResponse;
import com.google.cloud.vision.v1.EntityAnnotation;
import com.google.cloud.vision.v1.Feature;
import com.google.cloud.vision.v1.Feature.Type;
import com.google.cloud.vision.v1.Image;
import com.google.cloud.vision.v1.ImageAnnotatorClient;
import com.google.cloud.vision.v1.ImageAnnotatorSettings;
import com.google.protobuf.ByteString;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class CloudVision {

    ImageAnnotatorClient vision;

    public CloudVision() {
        try {
            Credentials cred = ServiceAccountCredentials.fromStream(new FileInputStream("/sdcard/Download/googleAPI.json"));
            ImageAnnotatorClient vision = ImageAnnotatorClient.create();
            ImageAnnotatorSettings imageAnnotatorSettings =
                    ImageAnnotatorSettings.newBuilder()
                            .setCredentialsProvider(FixedCredentialsProvider.create(cred))
                            .build();
            ImageAnnotatorClient imageAnnotatorClient =
                    ImageAnnotatorClient.create(imageAnnotatorSettings);
        } catch (IOException e) {
            Log.d("MYBUG", e.getMessage());
        }
    }

    public void readImage(String fileName) {
        if (vision == null)
            return;

        // Reads the image file into memory
        Path path = Paths.get(fileName);

        try {
            byte[] data = Files.readAllBytes(path);
            ByteString imgBytes = ByteString.copyFrom(data);
            // Builds the image annotation request
            List<AnnotateImageRequest> requests = new ArrayList<>();
            Image img = Image.newBuilder().setContent(imgBytes).build();
            Feature feat = Feature.newBuilder().setType(Type.LABEL_DETECTION).build();
            AnnotateImageRequest request = AnnotateImageRequest.newBuilder()
                    .addFeatures(feat)
                    .setImage(img)
                    .build();
            requests.add(request);
            // Performs label detection on the image file
            BatchAnnotateImagesResponse response = vision.batchAnnotateImages(requests);
            List<AnnotateImageResponse> responses = response.getResponsesList();

            for (AnnotateImageResponse res : responses) {
                if (res.hasError()) {
                    System.out.printf("Error: %s\n", res.getError().getMessage());
                    return;
                }

                for (EntityAnnotation annotation : res.getLabelAnnotationsList()) {
                    Log.d("MYBUG",annotation.getDescription());
                }
            }
        }catch (IOException e) {
            Log.d("MYBUG", "Image file not found");
        }
    }
}
