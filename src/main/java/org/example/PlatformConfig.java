package org.example;

public class PlatformConfig {
    private final double baseDeliveryFee ;
    private final double baseDeliveryDistanceKm ;
    private final double extraFeePerKm ;
    private final double serviceFeeRate;

    private static final PlatformConfig INSTANCE = new PlatformConfig();

    private PlatformConfig() {
        this.baseDeliveryFee =15.0;
        this.baseDeliveryDistanceKm =3.0;
        this.extraFeePerKm =3.0;
        this.serviceFeeRate =0.10;

    }

    public static PlatformConfig INSTANCE() {
        return INSTANCE;
    }

    public double getBaseDeliveryFee() {
        return baseDeliveryFee;
    }

    public double getBaseDeliveryDistanceKm() {
        return baseDeliveryDistanceKm;
    }

    public double getExtraFeePerKm() {
        return extraFeePerKm;
    }

    public double getServiceFeeRate() {
        return serviceFeeRate;
    }
}
