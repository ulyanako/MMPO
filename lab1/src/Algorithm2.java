import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class Algorithm2 implements ClassificationAlgorithm {
    private final int k;

    public Algorithm2(int k) {
        this.k = k;
    }
    @Override
    public int[] predictClass(University target, Map<Integer, List<University>> trainByClasses, int numClasses) {
        // находим максимальный диаметр для выборки
        double d = calculateMaxDiameter(trainByClasses);
        int bestClassId = -1;
        double max = -Double.MAX_VALUE;
        for (Map.Entry<Integer, List<University>> entry : trainByClasses.entrySet()) {
            int classId = entry.getKey();
            double score = neighborhoodSimilarity(target, entry.getValue(), this.k, d);

            if (score > max) {
                max = score;
                bestClassId = classId;
            }
        }
        int[] classificationVector = new int[numClasses];
        if (bestClassId >= 1 && bestClassId <= numClasses) {
            classificationVector[bestClassId - 1] = 1;
        }
        return classificationVector;
    }

    //вычисление диаметра одного класса
    private static double calculateDiameter(List<University> universities){
        double diameter = 0;
        //перебираем попарно элементы одного класса
        for(int i = 0; i < universities.size(); i++){
            for(int j = 0; j < universities.size(); j++){
                double dist = Algorithm1.euclideanMetric(universities.get(i).getFeatures(), universities.get(j).getFeatures());
                if(dist > diameter){
                    diameter = dist;
                }
            }
        }
        return diameter;
    }

    //вычисление максимального диаметра среди всех классов
    private static double calculateMaxDiameter(Map<Integer, List<University>> trainByClasses){
        double maxDiameter = 0;
        for(List<University> universities : trainByClasses.values()){
            double diameter = calculateDiameter(universities);
            if(diameter  > maxDiameter){
                maxDiameter = diameter;
            }
        }
        return maxDiameter;
    }

    //функция сходства для двух объектов
    public static double similarity(double[] x1, double[] x2, double d){
        if(d == 0) return 0;
        //нормированное расстояние между двумя объектами
        double normalizedDistance = Algorithm1.euclideanMetric(x1, x2);
        double score = 0.1 - normalizedDistance / d;
        return Math.max(0, score);
    }

    //оценка близости по k ближайшим соседям в одном классе
    private double neighborhoodSimilarity(University target, List<University> trainSet, int k, double d) {
        List<Double> similarities = new ArrayList<>();
        for (University u : trainSet) {
            similarities.add(similarity(target.getFeatures(), u.getFeatures(), d));
        }
        // Сортируем ПО УБЫВАНИЮ, так как ищем наибольшее сходство
        similarities.sort(Collections.reverseOrder());
        double sum = 0.0;
        int limit = Math.min(k, similarities.size());
        for (int i = 0; i < limit; i++) {
            sum += similarities.get(i);
        }
        return sum / limit;
    }
}
