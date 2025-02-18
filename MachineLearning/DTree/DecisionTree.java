
import javafx.util.Pair;

import java.util.*;

public class DecisionTree {
    private TNode root;
    private HashMap<String, Set<String>> columnValues;
    private String targetClass;

    public DecisionTree(List<String[]> trainingData, String targetClass) {
        columnValues = new HashMap<>();
        this.targetClass = targetClass;
        int targetIndex = getTargetIndex(targetClass);
        getMaxGRColumn(trainingData);
        root = buildTree(trainingData, targetIndex);
    }
    public TNode buildTree(List<String[]> trainingData, int targetIndex) {
        if(getTargetEntropy(trainingData) == 0) {
            if(trainingData.size() == 0) return null;
            return new TNode(trainingData.get(0)[targetIndex], true);
        }
        String maxGRColumn = getMaxGRColumn(trainingData);
        TNode node = new TNode(maxGRColumn, false);
        for(String value : columnValues.get(maxGRColumn)) {
            node.getChildren().put(value, buildTree(getSplittedData(trainingData, maxGRColumn, value), targetIndex));
        }
        return node;
    }
    private String getMaxGRColumn(List<String[]> data) {
        double targetEntropy = getTargetEntropy(data);
        String maxGRAttribute = "";
        double maxGRValue = 0;
        String[] columns = DataHandler.getColumns();
        int m = columns.length;
        for (int i = 0; i < m; i++) {
            if(columns[i].equals(targetClass))
                continue;
            double[] result = getEntropy(data, columns[i]);
            int uniqueValues = (int)result[1];
            double IG = targetEntropy - result[0];
            double GR = IG/uniqueValues;
            if(GR>maxGRValue){
                maxGRValue = GR;
                maxGRAttribute = columns[i];
            }
        }
        return maxGRAttribute;
    }
    private double[] getEntropy(List<String[]> data, String attribute) {
        HashMap<Pair<String, String>, Integer> freq = new HashMap<>();

        String[] columns = DataHandler.getColumns();
        int attIndex=0, targetIndex=0, m=columns.length;
        for(int i=0; i<m; i++) {
            if(columns[i].equals(attribute))
                attIndex=i;
            if(columns[i].equals(targetClass))
                targetIndex=i;
        }
        int n = data.size();
        for (int i = 0; i<n; i++) {
            Pair<String, String> key = new Pair<>(data.get(i)[attIndex], data.get(i)[targetIndex]);
            if(columnValues.get(attribute)==null){
                HashSet<String> set = new HashSet<>();
                set.add(data.get(i)[attIndex]);
                columnValues.put(attribute, set);
            }
            else{
                columnValues.get(attribute).add(data.get(i)[attIndex]);
            }
            freq.merge(key, 1, (oldValue, one) -> oldValue + 1);
        }
        double uniqueValues=0;
        HashMap<String, Integer> totalFreq = new HashMap<>();
        for (Pair<String, String> key : freq.keySet()) {
            String totalKey = key.getKey();
            int f = freq.get(key);
            if(totalFreq.get(totalKey)==null) uniqueValues++;
            totalFreq.merge(totalKey, f, (oldValue, one) -> oldValue + f);
        }
        double entropy = 0;
        for (Pair<String, String> key : freq.keySet()) {
            String totalKey = key.getKey();
            double p = (double) freq.get(key)/totalFreq.get(totalKey);
            entropy += -1*p*(Math.log10(p)/Math.log10(2))*totalFreq.get(totalKey)/n;
        }
        return new double[]{entropy, uniqueValues};
    }
    private double getTargetEntropy(List<String[]> data) {
        HashMap<String, Integer> freq = new HashMap<>();

        String[] columns = DataHandler.getColumns();
        int targetIndex=0, m=columns.length;
        for(int i=0; i<m; i++) {
            if(columns[i].equals(targetClass)) {
                targetIndex = i;
                break;
            }
        }
        int n = data.size();
        for (int i = 0; i<n; i++) {
            String key = data.get(i)[targetIndex];
            freq.merge(key, 1, (oldValue, one) -> oldValue + 1);
        }
        double entropy = 0;
        for (String key : freq.keySet()) {
            double p = (double) freq.get(key)/n;
            entropy += -1*p*(Math.log10(p)/Math.log10(2));
        }
        return entropy;
    }
    public int getTargetIndex(String attribute) {
        String[] columns = DataHandler.getColumns();
        int m=columns.length;
        for(int i=0; i<m; i++) {
            if(columns[i].equals(attribute))
                return i;
        }
        return -1;
    }
    private List<String[]> getSplittedData(List<String[]> data, String column, String columnValue) {
        int columnIndex = getTargetIndex(column), n=data.size();
        List<String[]> splittedData = new ArrayList<>();
        for(int i=0; i<n; i++) {
            if(data.get(i)[columnIndex].equals(columnValue)){
                splittedData.add(data.get(i).clone());
            }
        }
        return splittedData;
    }

    public String predict(String[] instance){
        TNode node = root;
        String[] columns = DataHandler.getColumns();
        while(!node.isLeaf()){
            String col = node.getAttribute();
            for(int i=0; i<columns.length; i++) {
                if(columns[i].equals(col)) {
                    node = node.getChildren().get(instance[i]);
                    break;
                }
            }
        }
        return node.getAttribute();
    }
    public String getTargetClass(){
        return targetClass;
    }
    public void setTargetClass(String targetClass){
        this.targetClass = targetClass;
    }
    public TNode getRoot() {
        return root;
    }
}
