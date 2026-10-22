import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class Algorithm1 implements ClassificationAlgorithm {
    private final int k;

    public Algorithm1(int k) {
        this.k = k;
    }

    //расстояние между университетами, расчитанное с помощью метрики Евклида
    public static double euclideanMetric(double[] x1, double[] x2){
        double sum = 0.0;
        for(int i = 0; i < x1.length; i++){
            sum += Math.pow(x1[i] - x2[i], 2);
        }
        return Math.sqrt(sum);
    }

    //оценка принадлежности к классу f_i(x) через k ближайших соседей
    private static double neighborhoodMetric(University target, List<University> trainSet, int k){
        List<Double> distances = new ArrayList<>();//набор расстояний
        for(University u : trainSet){
            distances.add(euclideanMetric(u.getFeatures(), target.getFeatures()));
        }
        Collections.sort(distances);//переупорядоченный набор расстояний
        //среднее арифметическое расстояний до k ближайших соседей одного класса
        double sum = 0.0;
        int limit = Math.min(k, distances.size());
        for(int i = 0; i < limit; i++){
            sum += distances.get(i);
        }
        return sum / limit;
    }

    //присваиваем элементу x класс, которому он ближе всего, определяем классификационный вектор
    //Map<Integer, List<University>> trainByClasses - обучающие объекты, сгруппированные по классам
    public int[] predictClass(University target, Map<Integer, List<University>> trainByClasses, int numClasses){
        int bestClassId = -1;
        double min = Double.MAX_VALUE;
        for(Map.Entry<Integer, List<University>> entry : trainByClasses.entrySet()){
            int classId = entry.getKey();
            double score = neighborhoodMetric(target, entry.getValue(), this.k);
            if(score < min){
                min = score;
                bestClassId = classId;
            }
        }
        int[] classificationVector = new int[numClasses];
        if (bestClassId >= 1 && bestClassId <= numClasses) {
            classificationVector[bestClassId - 1] = 1;
        }
        return classificationVector;
    }
}
