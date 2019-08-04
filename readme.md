#Country Blotter

####Out of the box enhancements
1. ######Hierarchy of logback xml:
   logback provides a hierarchy of logback xml files. Have a look at below files:
   \
   Both the files are wrapped in <i>included</i> tags. So they can be used as an import in other logback xmls. But are incomplete by themselves.
   1. [logback-console.xml](src/main/resources/logback-console.xml)
   1. [logback-file.xml](src/main/resources/logback-file.xml)
      
   \
   Now look at the [logback.xml](src/main/resources/logback.xml).
   Look for the `<include resource="logback-file.xml" />` or `<include resource="logback-console.xml" />`. This is how we include those files in the actual logback.xml.

1.  ######Hosting site in https:
    Initial study from [enable https in spring-boot](https://drissamri.be/blog/java/enable-https-in-spring-boot/) or following [tutorial](https://www.baeldung.com/spring-boot-https-self-signed-certificate).
    \
    Quick high-level view:
    1. To host a site on ssl the first thing required is ssl certificate. Now these certificates can be either:
       1. Self signed (free but not considered valid)
       1. Procured from a actual CA (certification authority), but you have to pay for it.
       
    1. Generating a self signed certificate:
       \
       This can be done using the keytool utility shipped with JDK itself. Run the following command:
       ````
       keytool -genkey -alias tomcat -storetype PKCS12 -keyalg RSA -keysize 2048 -keystore keystore.p12 -validity 3650
       
       Enter keystore password:
        Re-enter new password:
        What is your first and last name?
        [Unknown]:
        What is the name of your organizational unit?
        [Unknown]:
        What is the name of your organization?
        [Unknown]:
        What is the name of your City or Locality?
        [Unknown]:
        What is the name of your State or Province?
        [Unknown]:
        What is the two-letter country code for this unit?
        [Unknown]:
        Is CN=Unknown, OU=Unknown, O=Unknown, L=Unknown, ST=Unknown, C=Unknown correct?
        [no]: yes
       ````
       This will generate a PKCS12 keystore called keystore.p12 with your newly generate certificate in it, with certificate alias tomcat. You will need to reference keystore in a minute when we start to configure Spring Boot.
       
     1. Enabling ssl in spring-boot:
         \
         [Refer official link](https://docs.spring.io/spring-boot/docs/current-SNAPSHOT/reference/htmlsingle/#howto-configure-ssl).
         \
         Declaratively it can be configured.
         Let’s configure `HTTPS` in the default `application.properties` file under `src/main/resources` of your Spring Boot application:
         ````
         server.port: 8443
         server.ssl.key-store: keystore.p12
         server.ssl.key-store-password: mypassword
         server.ssl.keyStoreType: PKCS12
         server.ssl.keyAlias: tomcat
         ````
         That’s all you need to do to make your application accessible over HTTPS on https://localhost:8443
     
     1. Redirect HTTP to HTTPS (optional)
     \
     In some cases it might be a good idea to make your application accessible over HTTP too, but redirect all traffic to HTTPS. To achieve this we’ll need to add a second Tomcat connector, but currently it is not possible to configure two connector in the application.properties like mentioned before. Because of this we’ll add the HTTP connector programmatically and make sure it redirects all traffic to our HTTPS connector.
     \
     For this we will need to add the `TomcatEmbeddedServletContainerFactory` bean to one of our `@Configuration` classes.
     ````
      @Bean
       public EmbeddedServletContainerFactory servletContainer() {
         TomcatEmbeddedServletContainerFactory tomcat = new TomcatEmbeddedServletContainerFactory() {
             @Override
             protected void postProcessContext(Context context) {
               SecurityConstraint securityConstraint = new SecurityConstraint();
               securityConstraint.setUserConstraint("CONFIDENTIAL");
               SecurityCollection collection = new SecurityCollection();
               collection.addPattern("/*");
               securityConstraint.addCollection(collection);
               context.addConstraint(securityConstraint);
             }
           };
         
         tomcat.addAdditionalTomcatConnectors(initiateHttpConnector());
         return tomcat;
       }
       
       private Connector initiateHttpConnector() {
         Connector connector = new Connector("org.apache.coyote.http11.Http11NioProtocol");
         connector.setScheme("http");
         connector.setPort(8080);
         connector.setSecure(false);
         connector.setRedirectPort(8443);
         
         return connector;
       }
     ````
    To run the application:
    \
    Run this file:
    \
    `com.himanshu.countryblotter.Main`
    \
    With the following JVM parameters:
    \
    `-Dspring.profiles.active=uat` 

1. ######How to package the project:
pom.xml has been enhanced to package all the dependencies into a zip file.
\
`mvn clean package` 