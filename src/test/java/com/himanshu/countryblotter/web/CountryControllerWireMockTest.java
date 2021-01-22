package com.himanshu.countryblotter.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import com.github.tomakehurst.wiremock.junit.WireMockRule;
import com.himanshu.countryblotter.domain.Country;
import com.himanshu.countryblotter.fetcher.RestCountryFetcher;
import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.util.ServerInfo;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.hamcrest.Matchers;
import org.junit.*;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.*;
import org.springframework.web.client.RestTemplate;
import wiremock.org.apache.http.conn.ssl.NoopHostnameVerifier;

import javax.net.ssl.*;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.util.List;

import static com.github.tomakehurst.wiremock.client.WireMock.*;

@Slf4j
public class CountryControllerWireMockTest {
  public boolean isRecording = false; //true if you want to capture stubs / false if you want to run tests
  @Rule
  public WireMockRule wireMockRule = new WireMockRule(WireMockConfiguration.options().port(2345)); //Instantiate wiremock with default

  private RestCountryFetcher restCountryFetcher;

  @Before
  public void setup() {
    String baseUrl;
    baseUrl = String.format("http://localhost:%s/api/countries", wireMockRule.port()); //wiremock acts as a proxy server, which forwards to actual server during recording mode, hence address never changes
    restCountryFetcher = new RestCountryFetcher(baseUrl, "/all", "/alpha/%(country-code)",
          new ObjectMapper(), nonSecureRestTemplatePreAuth());

    if (isRecording) {
      WireMock.startRecording("https://127.0.0.1:8443/"); //Host country application locally for recording.
    }

  }

  @After
  public void clear() {
    if (isRecording) {
      WireMock.stopRecording();
    }
  }

  /*@Test
  public void testAllOk() {
    stubFor(any(anyUrl()).willReturn(okJson("{name : \"India\"}")));
  }*/

  @Test
  public void testGetAllCountries() {
    //stubFor(get(urlPathEqualTo("/all")).willReturn(okJson("[{\"name\" : \"India\", \"alpha2Code\" : \"IN\", \"capital\" : \"Delhi\"}]")));
    List<Country> countries = restCountryFetcher.fetchAllCountries();
    Assert.assertNotNull(countries);
    //Assert.assertTrue(countries.size() == 1);
    //Assert.assertThat(countries.get(0).getName(), Matchers.equalTo("India"));
  }

  private RestTemplate nonSecureRestTemplatePreAuth() {
    RestTemplate restTemplate = new RestTemplate();
    final ClientHttpRequestFactory clientHttpRequestFactory =
          new MyCustomClientHttpRequestFactory(org.apache.http.conn.ssl.SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
    restTemplate.setRequestFactory(clientHttpRequestFactory);
    restTemplate.getInterceptors().add(new ClientHttpRequestInterceptor() {
      @Override
      public ClientHttpResponse intercept(HttpRequest httpRequest, byte[] bytes, ClientHttpRequestExecution clientHttpRequestExecution) throws IOException {
        httpRequest.getHeaders().add("Authorization", "Basic "+ Base64.encode("user:password".getBytes()));
        return clientHttpRequestExecution.execute(httpRequest, bytes);
      }
    });
    return restTemplate;
  }

  private static class MyCustomClientHttpRequestFactory  extends SimpleClientHttpRequestFactory {

    private final HostnameVerifier hostNameVerifier;

    public MyCustomClientHttpRequestFactory(final HostnameVerifier hostNameVerifier) {
      this.hostNameVerifier = hostNameVerifier;
    }

    @Override
    protected void prepareConnection(final HttpURLConnection connection, final String httpMethod)
          throws IOException {
      if (connection instanceof HttpsURLConnection) {
        try {
          ((HttpsURLConnection) connection).setHostnameVerifier(hostNameVerifier);
          ((HttpsURLConnection) connection).setSSLSocketFactory(sslSocketFactory());
        } catch (KeyStoreException e) {
          throw new RuntimeException(e);
        } catch (CertificateException e) {
          throw new RuntimeException(e);
        } catch (NoSuchAlgorithmException e) {
          throw new RuntimeException(e);
        } catch (KeyManagementException e) {
          throw new RuntimeException(e);
        }
      }
      super.prepareConnection(connection, httpMethod);
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
}
