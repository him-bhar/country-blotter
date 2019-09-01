package com.himanshu.countryblotter.web;

import jdk.nashorn.internal.ir.annotations.Ignore;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.*;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.util.Base64;

//Only to be tested locally
@Ignore
public class CountryControllerIT {
  private static final Logger log = LoggerFactory.getLogger(CountryControllerIT.class);

  /**
   * To see detailed logs run test with System Property: -Djavax.net.debug=ssl
   * @throws IOException
   */
  @Test
  public void testGetAllCountries() throws IOException, CertificateException, NoSuchAlgorithmException, KeyStoreException, KeyManagementException {
    URL obj = new URL("https://localhost:8443/api/countries/all");
    HttpsURLConnection con = (HttpsURLConnection) obj.openConnection();
    con.setSSLSocketFactory(sslSocketFactory());
    con.setHostnameVerifier(new HostnameVerifier() {
      @Override
      public boolean verify(String hostname, SSLSession sslSession) {
        if (hostname.equalsIgnoreCase("localhost")) {
          return true;
        }
        return false;
      }
    });
    con.setRequestMethod("GET");
    con.setRequestProperty("Authorization", "Basic "+generateAuthorizationValue());
    int responseCode = con.getResponseCode();
    log.info("GET Response Code :: " + responseCode);
    if (responseCode == HttpURLConnection.HTTP_OK) { // success
      BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
      String inputLine;
      StringBuffer response = new StringBuffer();
      while ((inputLine = in.readLine()) != null) {
        response.append(inputLine);
      }
      in.close();
      log.info(response.toString());
    } else {

      Assertions.fail("Non OK HTTP Response HTTP-"+responseCode);
    }
  }

  private String generateAuthorizationValue() {
    return Base64.getEncoder().encodeToString("user:password".getBytes());
  }

  private SSLSocketFactory sslSocketFactory() throws KeyStoreException, IOException, CertificateException, NoSuchAlgorithmException, KeyManagementException {
    KeyStore keyStore = null;
    TrustManagerFactory tmf = null;
    SSLContext sslContext = null;

    try {
      keyStore = KeyStore.getInstance("JKS");
      InputStream is = null;
      String certificateFilePath = CountryControllerIT.class.getResource("/").getFile().concat("../classes/keystore.p12");
      log.info("Certificate file path: {}", certificateFilePath);
      FileInputStream fileInputStream = new FileInputStream(certificateFilePath);
      keyStore.load(fileInputStream, "changeit".toCharArray());
      tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
      tmf.init(keyStore);
      sslContext = SSLContext.getInstance("TLSv1.2");
      sslContext.init(null, tmf.getTrustManagers(), null);
      SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();
      return sslSocketFactory;
    } catch (Exception e) {
      throw e;
    }
  }
}
