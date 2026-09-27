package com.gateway.core.api.rest.response;

import com.gateway.core.domain.model.Merchant;

public class MerchantResponse {

    private final String merchantId;
    private final String name;
    private final String mcc;
    private final boolean active;

    private MerchantResponse(String merchantId, String name, String mcc, boolean active) {
        this.merchantId = merchantId;
        this.name = name;
        this.mcc = mcc;
        this.active = active;
    }

    public static MerchantResponse from(Merchant merchant) {
        return new MerchantResponse(merchant.getMerchantId(), merchant.getName(), merchant.getMcc(),
                merchant.isActive());
    }

    public String getMerchantId() {
        return merchantId;
    }

    public String getName() {
        return name;
    }

    public String getMcc() {
        return mcc;
    }

    public boolean isActive() {
        return active;
    }
}
