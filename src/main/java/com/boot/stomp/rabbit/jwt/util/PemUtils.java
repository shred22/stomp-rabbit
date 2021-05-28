package com.boot.stomp.rabbit.jwt.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.EncodedKeySpec;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

import org.bouncycastle.util.io.pem.PemObject;
import org.bouncycastle.util.io.pem.PemReader;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Slf4j
public class PemUtils {
  public static byte[] parsePEMFile(File pemFile) throws IOException {
    log.info("Parsing pem file.");
    if (pemFile == null) {
      throw new IllegalArgumentException("The file cannot be null.");
    }
    if (!pemFile.isFile() || !pemFile.exists()) {
      throw new FileNotFoundException(
          String.format("The file '%s' doesn't exist.", pemFile.getAbsolutePath()));
    }
    try (PemReader reader = new PemReader(new FileReader(pemFile))) {
      PemObject pemObject = reader.readPemObject();
      return pemObject.getContent();
    }
  }
  private static PublicKey getPublicKey(byte[] keyBytes, String algorithm) {
    log.info("Trying to form PublicKey of " + algorithm + " algorithm.");
    PublicKey publicKey = null;
    try {
      KeyFactory kf = KeyFactory.getInstance(algorithm);
      EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
      publicKey = kf.generatePublic(keySpec);
    } catch (NoSuchAlgorithmException nsae) {
      log.error("Could not reconstruct the public key, the given algorithm could not be found.",
          nsae);
    } catch (InvalidKeySpecException ikse) {
      log.error("Could not reconstruct the public key.", ikse);
    }
    return publicKey;
  }
  private static PrivateKey getPrivateKey(byte[] keyBytes, String algorithm) {
    log.info("Trying to form PrivateKey of " + algorithm + " algorithm.");
    PrivateKey privateKey = null;
    try {
      KeyFactory kf = KeyFactory.getInstance(algorithm);
      EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);
      privateKey = kf.generatePrivate(keySpec);
    } catch (NoSuchAlgorithmException nsae) {
      log.error("Could not reconstruct the private key, the given algorithm could not be found.",
          nsae);
    } catch (InvalidKeySpecException ikse) {
      log.error("Could not reconstruct the private key.", ikse);
    }
    return privateKey;
  }
  public static RSAPublicKey readPublicKeyFromFile(
      File publicKeyFile, String algorithm) throws IOException {
    byte[] bytes = parsePEMFile(publicKeyFile);
    return (RSAPublicKey) getPublicKey(bytes, algorithm);
  }
  public static RSAPrivateKey readPrivateKeyFromFile(
      File privateKeyFile, String algorithm) throws IOException {
    byte[] bytes = parsePEMFile(privateKeyFile);
    return (RSAPrivateKey) getPrivateKey(bytes, algorithm);
  }
}