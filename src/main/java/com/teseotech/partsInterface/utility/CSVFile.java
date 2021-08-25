package com.teseotech.partsInterface.utility;

import com.teseotech.partsInterface.implementation.owlInterface.OWLFeature;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class CSVFile {
    private final Class<?>[] types;
    private final List<Set<OWLFeature<?>>> data = new ArrayList<>();

    public CSVFile(String filePath, Class<?>[] types) {
        this.types = types;
        this.setCSVdata(filePath, ";", null);
    }

    public CSVFile(String filePath, String columnDelimiter, Class<?>[] types) {
        this.types = types;
        this.setCSVdata(filePath, columnDelimiter, null);
    }

    public CSVFile(String filePath, String columnDelimiter, Class<?>[] types, String[] header) {
        this.types = types;
        this.setCSVdata(filePath, columnDelimiter, header);
    }

    private void setCSVdata(String filePath, String columnsDelimiter, String[] givenHeader) {
        String line = "";
        boolean headerFound = false; // Hypothesis: the first file should be the header (if the latter is not given as input parameter).
        String[] header = givenHeader;
        if (header != null)
            headerFound = true;
        try {
            BufferedReader br = new BufferedReader(new FileReader(filePath));
            while ((line = br.readLine()) != null) {
                String[] parsed = line.split(columnsDelimiter);
                //ArrayList<String> subOut = new ArrayList<>(Arrays.asList(parsed));
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
                        final OWLFeature<?> csvRow = castData(header[i], parsed[i], types[i]);
                        rowFeatures.add(csvRow);
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
        if (type == Boolean.class)
            return new OWLFeature<>(key, Boolean.valueOf(data));
        if (type == Integer.class)
            return new OWLFeature<>(key, Integer.valueOf(data));
        if (type == Float.class)
            return new OWLFeature<>(key, Float.valueOf(data));
        if (type == Double.class)
            return new OWLFeature<>(key, Double.valueOf(data));
        if (type == Long.class)
            return new OWLFeature<>(key, Long.valueOf(data));
        if (type == String.class)
            return new OWLFeature<>(key, data);
        StaticLogger.logError("Cannot parse data " + data + "(key: " + key + ") with type " + type);
        return new OWLFeature<>(key, data);
    }

    public static String removeUTF8(String s) {
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

    public static CSVFile readCsv(String filePath, Class<?>[] types){
        return new CSVFile(filePath, ";", types);
    }
    public static CSVFile readCsv(String filePath, String columnsDelimiter, Class<?>[] types){
        return new CSVFile(filePath, columnsDelimiter, types);
    }
    public static CSVFile readCsv(String filePath, String columnsDelimiter, Class<?>[] types, String[] header){
        return new CSVFile(filePath, columnsDelimiter, types, header);
    }
}
