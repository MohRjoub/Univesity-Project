
import java.util.List;

public class TestData {
    private static int TP=0, TN=0, FP=0, FN=0;
    private static double recall, f_score, accuracy, precision, error;

    public static void testData(DecisionTree tree, List<String[]> testData) {
        int targetIndex = tree.getTargetIndex(tree.getTargetClass());
        for (String[] instance : testData) {
            String prediction = tree.predict(instance);
            if(prediction.equals(instance[targetIndex])){
                if(prediction.equals("EDIBLE")) TP++;
                else TN++;
            }
            else{
                if(prediction.equals("EDIBLE")) FP++;
                else FN++;
            }
        }
        accuracy = (double)(TP+TN)/(TP+TN+FP+FN);
        precision = (double)TP/(TP+FP);
        recall = (double)TP/(TP+FN);
        f_score = (2.0*TP)/(2.0*TP+FN+FP);
        error = 1-accuracy;
        System.out.println("Accuracy: "+accuracy);
        System.out.println("Precision: "+precision);
        System.out.println("Recall: "+recall);
        System.out.println("F-score: "+f_score);
        System.out.println("Error: "+error);
    }
    public static double getAccuracy() {
        return accuracy;
    }
    public static double getPrecision() {
        return precision;
    }
    public static double getRecall() {
        return recall;
    }
    public static double getF_score() {
        return f_score;
    }
    public static double getError(){
        return error;
    }
    public static int[][] getConfusionMatrix(){
        int[][] confusionMatrix = new int[2][2];
        confusionMatrix[0][0] = TP;
        confusionMatrix[0][1] = FN;
        confusionMatrix[1][0] = FP;
        confusionMatrix[1][1] = TN;
        return confusionMatrix;
    }
}
