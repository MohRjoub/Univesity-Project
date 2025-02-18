
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DataHandler {
    private static List<String[]> data, testData, trainingData;
    private static String[] columns;

    public static void readData(String fileName){
        data = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;

            columns = br.readLine().split(",");

            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                data.add(values);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static boolean splitData(double testPercentage){
        if(testPercentage<0 || testPercentage>1) return false;
        testData = new ArrayList<>();
        trainingData = new ArrayList<>();
        int n=data.size();
        int numOfTestRows = (int)(n*testPercentage);
        byte[] visited = new byte[(n+7/8)];
        while(numOfTestRows>0){
            int rand = (int)(Math.random()*n);
            int index = rand/8;
            if((visited[index]&(1<<7-rand%8))==0){
                testData.add(data.get(rand));
                visited[index] = (byte)(visited[index] | (1<<7-rand%8));
                numOfTestRows--;
            }
        }
        for(int i=0; i<(n+7)/8; i++){
            for(int j=7; j>=0; j--){
                if((visited[i]&(1<<7-j))==0){
                    if(i*8+7-j>=n) break;
                    trainingData.add(data.get(i*8+7-j));
                }
            }
        }
        return true;
    }

    public static List<String[]> getTestData(){
        return testData;
    }
    public static List<String[]> getTrainingData(){
        return trainingData;
    }
    public static String[] getColumns(){
        return columns;
    }
}
