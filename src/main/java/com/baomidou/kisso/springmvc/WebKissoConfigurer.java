package com.baomidou.kisso.springmvc;

import com.baomidou.kisso.SSOConfig;

public class WebKissoConfigurer {

    private SSOConfig ssoConfig;

    public void initKisso() {
        SSOConfig.init(this.getSsoConfig());
    }

    public SSOConfig getSsoConfig() {
        return ssoConfig;
    }

    public void setSsoConfig(SSOConfig ssoConfig) {
        this.ssoConfig = ssoConfig;
    }

    public void setRunMode(String runMode) {
    }
}
