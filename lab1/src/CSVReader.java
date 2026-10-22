import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CSVReader {
    List<University> loadCSV(String filename) {
        List<University> dataset =  new ArrayList<>();
        String line = "";
        String spliter = ",";
        try {
            BufferedReader br = new BufferedReader(new FileReader(filename));
            while ((line = br.readLine()) != null) {
                String[] values = line.split(spliter);
                try{
                    double worldRank = Double.parseDouble(values[0]);
                    double national_rank =  Double.parseDouble(values[3]);
                    double quality_of_education = Double.parseDouble(values[4]);
                    double alumni_employment =  Double.parseDouble(values[5]);
                    double quality_of_faculty =  Double.parseDouble(values[6]);
                    double publications  = Double.parseDouble(values[7]);
                    double score =   Double.parseDouble(values[12]);

                    double [] features = {national_rank, quality_of_education, alumni_employment, quality_of_faculty,
                    publications, score};

                    int targetClass = 0;
                    if (worldRank > 0 && worldRank <= 100) {
                        targetClass = 1;
                    }
                    else if (worldRank > 100 && worldRank <= 300) {
                        targetClass = 2;
                    }
                    else if (worldRank > 300 && worldRank <= 500) {
                        targetClass = 3;
                    }
                    else if (worldRank > 500 && worldRank <= 1000) {
                        targetClass = 4;
                    }
                    if (targetClass != 0) {
                        dataset.add(new University(features, targetClass));
                    }
                }
                catch(Exception e){
                    continue;
                }

            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return dataset;
    }

    // Нормализация признаков к отрезку [0, 1]
    public void normalizeFeatures(List<University> dataset) {
        if (dataset.isEmpty()) return;
        int numFeatures = dataset.get(0).getFeatures().length;

        for (int j = 0; j < numFeatures; j++) {
            double min = Double.MAX_VALUE;
            double max = -Double.MAX_VALUE;

            for (University u : dataset) {
                double val = u.getFeatures()[j];
                if (val < min) min = val;
                if (val > max) max = val;
            }

            for (University u : dataset) {
                if (max != min) {
                    u.getFeatures()[j] = (u.getFeatures()[j] - min) / (max - min);
                }
            }
        }
    }
}
