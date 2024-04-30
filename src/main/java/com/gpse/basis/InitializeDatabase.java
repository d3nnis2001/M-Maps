package com.gpse.basis;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;

@Service
public class InitializeDatabase implements InitializingBean {

    public InitializeDatabase() {
    }
    @Override
    public void afterPropertiesSet() throws Exception {

    }
}
