package com.framework.encrytable.encrypt;

import com.ulisesbocchio.jasyptspringboot.EncryptablePropertyFilter;
import org.springframework.core.env.PropertySource;

public class ImplEncryptablePropertyFilter implements EncryptablePropertyFilter {

    @Override
    public boolean shouldInclude(PropertySource<?> propertySource, String name) {
        return true;
    }
}
