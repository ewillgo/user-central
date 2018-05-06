package org.trianglex.usercentral.security;

import com.github.benmanes.caffeine.cache.CacheLoader;
import org.springframework.beans.factory.annotation.Autowired;
import org.trianglex.usercentral.service.ApplicationService;

public class AppCacheLoader implements CacheLoader<String, String> {

    @Autowired
    private ApplicationService applicationService;

    @Override
    public String load(String key) throws Exception {
        return applicationService.getApplicationByAppKey(key, "app_secret").getAppSecret();
    }

}
