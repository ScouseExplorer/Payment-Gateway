package com.gateway.core.domain.model;

/**
 * RiskScore Value Object
 * 
 * Represents fraud risk assessment for a payment (0-100).
 * Immutable value object ensuring valid risk score range.
 * 
 * Risk Levels:
 * - 0-20: Very Low Risk
 * - 21-40: Low Risk
 * - 41-60: Medium Risk
 * - 61-80: High Risk
 * - 81-100: Very High Risk (likely fraud)
 * 
 * @author Fraud Prevention Team
 */
public class RiskScore {
    private final int score;
    
    private RiskScore(int score) {
        if (score < 0 || score > 100) {
            throw new IllegalArgumentException("Risk score must be between 0 and 100");
        }
        this.score = score;
    }
    
    public static RiskScore of(int score) {
        return new RiskScore(score);
    }
    
    public int getScore() {
        return score;
    }
    
    public boolean isHighRisk() {
        return score > 80;
    }
    
    public boolean isMediumRisk() {
        return score >= 41 && score <= 80;
    }
    
    public boolean isLowRisk() {
        return score <= 40;
    }
    
    @Override
    public String toString() {
        return String.valueOf(score);
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RiskScore)) return false;
        RiskScore that = (RiskScore) o;
        return score == that.score;
    }
    
    @Override
    public int hashCode() {
        return Integer.hashCode(score);
    }
}
