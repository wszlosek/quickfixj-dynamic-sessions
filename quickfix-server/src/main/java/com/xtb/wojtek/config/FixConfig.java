package com.xtb.wojtek.config;

import com.xtb.wojtek.core.DefaultFixServer;
import com.xtb.wojtek.core.FixAcceptor;
import com.xtb.wojtek.core.FixServer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;

/**
 * The type Fix config.
 *
 * @author yanghuadong
 * @date 2019 -03-18
 */
@Configuration
public class FixConfig {
    /**
     * The Fix acceptor.
     */
    @Resource
    private FixAcceptor fixAcceptor;

    /**
     * Fix server fix server.
     *
     * @return the fix server
     */
    @Bean
    public FixServer fixServer() {
        FixServer server = new DefaultFixServer(fixAcceptor);
        server.start();
        return server;
    }
}