package me.jisung.springplayground.common.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;

/**
 * 양방향 비대칭키 암호화
 * */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class RSAUtil {



    public static void main(String[] args) {
        final String algorithm = "RSA";
        final int keySize = 2048;

        try {
            KeyPairGenerator generator = KeyPairGenerator.getInstance(algorithm);
            generator.initialize(keySize);
            KeyPair pair = generator.generateKeyPair();

            // private key
            PrivateKey privateKey = pair.getPrivate();
            PublicKey publicKey = pair.getPublic();

            System.out.println("privateKey.getEncoded() = " + privateKey.getEncoded());
            System.out.println("publicKey.getEncoded() = " + publicKey.getEncoded());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
