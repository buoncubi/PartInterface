package com.teseotech.partsInterface.utility;

import com.teseotech.partsInterface.implementation.kernel.Range;
import com.teseotech.partsInterface.implementation.owlInterface.OWLFeature;
import com.teseotech.partsInterface.implementation.owlInterface.OWLRangeFeature;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

/*
 * A class to parse a CSV file into a `List<Set<OWLFeature<?>>>`, which is retrievable with `getData()'.
 * Check the README for more information of the structure that the CSV should have.
 * It implements a `pullFeature()` function to extract Features with a given `key` from `getData()`.
 */
public class CSVFile {
    public static final String DEFAULT_COLUMNS_DELIMETER = ",";
    private final Class<?>[] types;
    private final List<Set<OWLFeature<?>>> data = new ArrayList<>();

    private CSVFile(String filePath, String columnDelimiter, Class<?>[] types) {
        this.types = types;
        this.setCSVData(filePath, columnDelimiter, null);
    }
    private CSVFile(String filePath, Class<?>[] types) {
        this.types = types;
        this.setCSVData(filePath, DEFAULT_COLUMNS_DELIMETER, null);
    }
    public CSVFile(String filePath, String columnDelimiter, Class<?>[] types, String[] header) {
        this.types = types;
        this.setCSVData(filePath, columnDelimiter, header);
    }
    public CSVFile(String filePath, Class<?>[] types, String[] header) {
        this.types = types;
        this.setCSVData(filePath, DEFAULT_COLUMNS_DELIMETER, header);
    }

    // Read the file line by line and parse it as OWLFeatures.
    // Hypothesis: if the header is not specified, the first line of the file should be the header.
    private void setCSVData(String filePath, String columnsDelimiter, String[] givenHeader) {
        boolean headerFound = false;
        String[] header = givenHeader;
        if (header != null)
            headerFound = true;
        try {
            BufferedReader br = new BufferedReader(new FileReader(filePath));
            String line;
            while ((line = br.readLine()) != null) {
                String[] parsed = line.split(columnsDelimiter);
                if (!headerFound) {
                    header = parsed;
                    StaticLogger.logInfo("Read CSV from " + filePath + " with headers: " + Arrays.toString(header));
                    headerFound = true;
                } else {
                    if (header == null)
                        StaticLogger.logError("CSV must contain an header in the first line!");
                    if (header.length != parsed.length | header.length != types.length)
                        StaticLogger.logError("CSV must work with consistent sizes: "
                                + "header.length=" + header.length + " raw.length=" + parsed.length + " type.length=" + types.length);
                    final Set<OWLFeature<?>> rowFeatures = new HashSet<>();
                    for (int i = 0; i < parsed.length; i++) {
                        if(!parsed[i].isEmpty()) {
                            final OWLFeature<?> csvData = castData(header[i], parsed[i], types[i]);
                            rowFeatures.add(csvData);
                        }
                    }
                    data.add(rowFeatures);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            StaticLogger.logError("Cannot read CSV from path " + filePath + '.');
        }
    }

    private OWLFeature<?> castData(String key, String data, Class<?> type) {
        key = removeUTF8(key.trim());
        data = removeUTF8(data.trim());
        if(type == Boolean.class)
            return new OWLFeature<>(key, Boolean.valueOf(data));
        if(type == Integer.class)
            return new OWLFeature<>(key, Integer.valueOf(data));
        if(type == Float.class)
            return new OWLFeature<>(key, Float.valueOf(data));
        if(type == Double.class)
            return new OWLFeature<>(key, Double.valueOf(data));
        if(type == Long.class)
            return new OWLFeature<>(key, Long.valueOf(data));
        if(type == String.class)
            return new OWLFeature<>(key, data.replaceAll("\"","").replaceAll("'",""));
        if(type == Range.class)
            return new OWLRangeFeature(key, parseRange(data));
        if(type == Number.class)
            return new OWLFeature<>(key, Float.valueOf(data));
        StaticLogger.logError("Cannot parse data " + data + "(key: " + key + ") with type " + type);
        return new OWLFeature<>(key, data.replaceAll("\"","").replaceAll("'",""));
    }

    private Range parseRange(String rangeStr){
        String[] split = rangeStr.split("-", 2);
        if(split.length == 1)
            return new Range(Float.valueOf(split[0]));
        return new Range(Float.valueOf(split[0]), Float.valueOf(split[1]));
    }

    private static String removeUTF8(String s) {
        if (s.startsWith("\uFEFF"))
            return s.substring(1);
        return s;
    }

    public List<Set<OWLFeature<?>>> getData() {
        return data;
    }

    // It finds the `BaseFeature: X` with a given `key` for each items in `data`.
    // Than `X.value` is returned and removed from `this.data`.
    public List<String> pullFeature(String key) {
        List<String> out = new ArrayList<>();
        for (Set<OWLFeature<?>> features : getData()) {
            for (OWLFeature<?> f : features) {  // For each feature of a part.
                if (f.getKey().equals(key)) {  // Find the given feature.
                    out.add(f.getValue().toString());
                    features.remove(f);
                    break;
                }
            }
        }
        return out;
    }
}
