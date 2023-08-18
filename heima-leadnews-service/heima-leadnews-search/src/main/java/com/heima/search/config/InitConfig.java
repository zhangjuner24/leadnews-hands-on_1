package com.heima.search.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(value = {"com.heima.common.exception","com.heima.common.knife4j"})
public class InitConfig {
}
