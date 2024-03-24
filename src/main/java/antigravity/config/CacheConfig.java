package antigravity.config;

import org.ehcache.config.CacheConfiguration;
import org.ehcache.config.builders.CacheConfigurationBuilder;
import org.ehcache.config.builders.ExpiryPolicyBuilder;
import org.ehcache.config.builders.ResourcePoolsBuilder;
import org.ehcache.config.units.EntryUnit;
import org.ehcache.config.units.MemoryUnit;
import org.ehcache.jsr107.Eh107Configuration;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.cache.CacheManager;
import javax.cache.Caching;
import javax.cache.spi.CachingProvider;
import java.time.Duration;

@Configuration
@EnableCaching
public class CacheConfig {

    private static final String PRODUCT_AMOUNT_CACHE = "getProductAmountCache";

    @Bean
    public CacheManager getCacheManager() {
        CachingProvider cachingProvider = Caching.getCachingProvider();
        CacheManager cacheManager = cachingProvider.getCacheManager();

        CacheConfiguration<Object, Object> configuration = CacheConfigurationBuilder.newCacheConfigurationBuilder(Object.class, Object.class,
                        ResourcePoolsBuilder.newResourcePoolsBuilder()
                                .heap(10, EntryUnit.ENTRIES))
                .withSizeOfMaxObjectSize(100, MemoryUnit.B)
                .withExpiry(ExpiryPolicyBuilder.timeToIdleExpiration(Duration.ofSeconds(30)))
                .withExpiry(ExpiryPolicyBuilder.timeToLiveExpiration(Duration.ofSeconds(60)))
                .build();

        javax.cache.configuration.Configuration<Object, Object> cacheConfiguration = Eh107Configuration.fromEhcacheCacheConfiguration(configuration);

        cacheManager.createCache(PRODUCT_AMOUNT_CACHE, cacheConfiguration);

        return cacheManager;
    }
}

