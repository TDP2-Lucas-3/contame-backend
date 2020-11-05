package com.lucas3.contanos.service.firebase;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Base64;

@Service
public class FCMInitializer {

    @Value("${contame.firebase-configuration}")
    private String firebaseConfig;

    @PostConstruct
    public void initialize() {
        try {
            InputStream credentials = new ByteArrayInputStream(Base64.getDecoder().decode(firebaseConfig));
            FirebaseOptions options = new FirebaseOptions.Builder()
                    .setCredentials(GoogleCredentials.fromStream(credentials)).build();
            if (FirebaseApp.getApps().isEmpty()) {
                FirebaseApp.initializeApp(options);
                System.out.println("Firebase application has been initialized");
            }
        } catch (IOException e) {
           System.out.println("Error inicializando firebase");
        }
    }

}