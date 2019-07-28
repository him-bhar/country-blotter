package com.himanshu.countryblotter.configurer;

import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.*;
import org.springframework.cache.jcache.JCacheCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.cache.Caching;
import javax.cache.configuration.MutableConfiguration;
import javax.cache.expiry.CreatedExpiryPolicy;
import javax.cache.expiry.Duration;
import javax.cache.spi.CachingProvider;

//For more details read this : https://www.ehcache.org/documentation/3.0/107.html
@Configuration
@EnableCaching
public class CacheConfiguration implements CachingConfigurer {
  @Bean
  @Override
  public CacheManager cacheManager() {
    CachingProvider provider = Caching.getCachingProvider("org.ehcache.jsr107.EhcacheCachingProvider");
    javax.cache.CacheManager cacheManager = provider.getCacheManager();
    MutableConfiguration/*<String, Country>*/ configuration =
          new MutableConfiguration<>()
                //.setTypes(String.class, Country.class)
                .setStoreByValue(false)
                .setExpiryPolicyFactory(CreatedExpiryPolicy.factoryOf(Duration.ONE_MINUTE));

    cacheManager.createCache("countryCache", configuration);
    cacheManager.enableManagement("countryCache", true);
    cacheManager.enableStatistics("countryCache", true);

    return new JCacheCacheManager(cacheManager);
  }

  @Bean
  @Override
  public CacheResolver cacheResolver() {
    return new SimpleCacheResolver(cacheManager());
  }

  @Bean
  @Override
  public KeyGenerator keyGenerator() {
    return new SimpleKeyGenerator();
  }

  @Bean
  @Override
  public CacheErrorHandler errorHandler() {
    return new SimpleCacheErrorHandler();
  }
}
