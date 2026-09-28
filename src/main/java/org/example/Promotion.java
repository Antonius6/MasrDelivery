package org.example;

import java.time.LocalDateTime;

public class Promotion {


    private final String code;
    private final double minSubtotal;
    private final LocalDateTime expiryDate;
    private final District restrictedDistrict;
    private final boolean firstTimeCustomerOnly;

    private final PromotionStrategy strategy;

    public Promotion(Builder builder) {
        this.code = builder.code;
        this.minSubtotal = builder.minSubtotal;
        this.expiryDate = builder.expiryDate;
        this.restrictedDistrict = builder.restrictedDistrict;
        this.firstTimeCustomerOnly = builder.firstTimeCustomerOnly;
        this.strategy = builder.strategy;

    }




    static class Builder {
        private String code;
        private double minSubtotal;
        private LocalDateTime expiryDate;
        private District restrictedDistrict;
        private boolean firstTimeCustomerOnly;
        private PromotionStrategy strategy;

        public Builder code(String code) {
            if (code == null || code.isBlank()) {
                throw new IllegalArgumentException("Promotion code cannot be null or blank");
            }
            this.code = code.toUpperCase().trim();
            return this;
        }

        public Builder minSubtotal(double minSubtotal) {
            if (minSubtotal < 0) {
                throw new IllegalArgumentException("minSubtotal must be greater than 0");
            }
            this.minSubtotal = minSubtotal;
            return this;
        }


        public Builder expiryDate(LocalDateTime expiryDate) {
            if (expiryDate != null && expiryDate.isBefore(LocalDateTime.now())) {
                throw new IllegalArgumentException("expiryDate cannot be in the past");
            }
            this.expiryDate = expiryDate;
            return this;

        }

        public Builder restrictedDistrict(District restrictedDistrict) {
            this.restrictedDistrict = restrictedDistrict;
            return this;
        }

        public Builder firstTimeCustomerOnly(boolean firstTimeCustomerOnly) {
            this.firstTimeCustomerOnly = firstTimeCustomerOnly;
            return this;

        }

        public Builder strategy(PromotionStrategy strategy) {
            if (strategy == null) {
                throw new IllegalArgumentException("strategy cannot be null");
            }
            this.strategy = strategy;
            return this;
        }

        public Promotion build() {

            if(code==null || code.isBlank() || strategy == null) {
                throw new IllegalArgumentException("Promotion code or strategy cannot be null or blank");
            }

            return new Promotion(this);
        }


    }

    public void validatePromotion(double subtotal, District deliveryDistrict, boolean isFirstTimeCustomer) {
        if (expiryDate != null && LocalDateTime.now().isAfter(expiryDate)) {
            throw new PromotionExpiredException("Promotion code '" + code + "' has expired.");
        }
        if (subtotal < minSubtotal) {
            throw new InvalidPromotionException("Subtotal (" + subtotal +
                    " EGP) is below minimum required (" + minSubtotal + " EGP).");
        }
        if (restrictedDistrict != null && restrictedDistrict != deliveryDistrict) {
            throw new InvalidPromotionException("Promotion '" + code +
                    "' is only valid for district: " + restrictedDistrict);
        }
        if (firstTimeCustomerOnly && !isFirstTimeCustomer) {
            throw new InvalidPromotionException("Promotion '" + code +
                    "' is restricted to first-time customers only.");
        }
    }



    public double calculateDiscount(double subtotal) {
        return strategy.Discount(subtotal);

    }


    public String getCode() {
        return code;
    }

    public double getMinSubtotal() {
        return minSubtotal;
    }

    public LocalDateTime getExpiryDate() {
        return expiryDate;
    }

    public District getRestrictedDistrict() {
        return restrictedDistrict;
    }

    public boolean isFirstTimeCustomerOnly() {
        return firstTimeCustomerOnly;
    }
    public boolean waivesDeliveryFee() {
        return strategy.appliesToDeliveryFee();
    }

}


