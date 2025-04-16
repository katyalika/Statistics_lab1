package com.statistics.model;

import org.apache.commons.math3.distribution.TDistribution;
import org.apache.commons.math3.stat.StatUtils;
import org.apache.commons.math3.stat.correlation.Covariance;
import org.apache.commons.math3.stat.descriptive.moment.StandardDeviation;
import java.util.*;
/**
 *
 * @author lihac
 */
public class StatsCalculator {
    
    public Map<String, Object> calculateAllStatistics(List<double[]> samples, List<String> columnNames) {
        Map<String, Object> results = new LinkedHashMap<>();
        
        for (int i = 0; i < samples.size(); i++) {
            double[] sample = samples.get(i);
            String colName = columnNames.get(i);
            String prefix = colName + " - ";
            
            results.put(prefix + "Count", (double) sample.length);
            results.put(prefix + "Min", StatUtils.min(sample));
            results.put(prefix + "Max", StatUtils.max(sample));
            results.put(prefix + "Range", calculateRange(sample));
            results.put(prefix + "Mean", StatUtils.mean(sample));
            results.put(prefix + "Geometric Mean", calculateGeometricMean(sample));
            results.put(prefix + "Variance", StatUtils.variance(sample));
            results.put(prefix + "Std Deviation", new StandardDeviation(false).evaluate(sample));
            results.put(prefix + "Coeff of Variation", calculateCoeffOfVariation(sample));
            
            double[] ci = calculateConfidenceInterval(sample);
            results.put(prefix + "CI Lower (95%)", ci[0]);
            results.put(prefix + "CI Upper (95%)", ci[1]);
        }
        
        if (samples.size() > 1) {
            Map<String, Double> covariances = calculateCovariances(samples, columnNames);
            results.putAll(covariances);
        }
        
        return results;
    }

    private Map<String, Double> calculateCovariances(List<double[]> samples, List<String> columnNames) {
        Map<String, Double> covariances = new LinkedHashMap<>();
        Covariance covCalculator = new Covariance();
        
        int refLength = samples.get(0).length;
        for (int i = 1; i < samples.size(); i++) {
            if (samples.get(i).length != refLength) {
                throw new IllegalArgumentException("All samples must have the same length");
            }
        }
        
        for (int i = 0; i < samples.size(); i++) {
            for (int j = i + 1; j < samples.size(); j++) {
                double cov = covCalculator.covariance(samples.get(i), samples.get(j), false);
                String pairKey = String.format("Covariance %s & %s", columnNames.get(i), columnNames.get(j));
                covariances.put(pairKey, cov);
            }
        }
        
        return covariances;
    }

    private double calculateRange(double[] data) {
        return StatUtils.max(data) - StatUtils.min(data);
    }

    private double calculateCoeffOfVariation(double[] data) {
        double mean = StatUtils.mean(data);
        if (mean != 0) {
            return new StandardDeviation(false).evaluate(data) / mean;
        } else {
            return Double.NaN;
        }
    }

    private double[] calculateConfidenceInterval(double[] data) {
        int n = data.length;
        double t = new TDistribution(n - 1).inverseCumulativeProbability(0.975);
        double stdDev = new StandardDeviation(false).evaluate(data);
        double mean = StatUtils.mean(data);
        double margin = t * stdDev / Math.sqrt(n);
        return new double[]{mean - margin, mean + margin};
    }
    
    private double calculateGeometricMean(double[] data) {
        if (data == null || data.length == 0) {
            return Double.NaN;
        }
        for (double value : data) {
            if (value <= 0) {
                return Double.NaN;
            }
        }
        return StatUtils.geometricMean(data);
    }
}