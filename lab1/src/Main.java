import java.util.*;

public class Main {
    public static void main(String[] args) {
        int numClasses = 4;
        // Инициализируем список алгоритмов
        List<ClassificationAlgorithm> algorithms = new ArrayList<>();
        algorithms.add(new Algorithm1(7));
        algorithms.add(new Algorithm2(7));
        //Загружаем датасет
        CSVReader csvReader = new CSVReader();
        List<University> dataset = csvReader.loadCSV("C:\\1proga\\sem5\\MMPO\\lab1\\src\\cwurData.csv");
        csvReader.normalizeFeatures(dataset); // Обязательно нормализуем данные

        int numAlgorithms = algorithms.size();

        // Матрица для хранения результатов [Индекс алгоритма][Индекс разбиения]
        double[][] resultsTable = new double[numAlgorithms][numClasses];

        // 3. Выполняем расчёты для каждого алгоритма по всем разбиениям
        for (int i = 0; i < numAlgorithms; i++) {
            ClassificationAlgorithm algo = algorithms.get(i);

            for (int split = 0; split < numClasses; split++) {
                // Переупорядочиваем выборку
                Collections.shuffle(dataset);
                int trainSize = (int) (dataset.size() * 0.7);

                List<University> trainList = dataset.subList(0, trainSize);
                List<University> testList  = dataset.subList(trainSize, dataset.size());

                // Группируем обучающую выборку по классам
                Map<Integer, List<University>> trainByClasses = groupByClasses(trainList);

                // Считаем точность на контрольной выборке и сохраняем в матрицу
                double accuracy = calculateAccuracy(algo, testList, trainByClasses, numClasses);
                resultsTable[i][split] = accuracy;
            }
        }

        //Выводим итоговую таблицу результатов
        printResultsTable(algorithms, resultsTable, numClasses);
    }

    // Функция для оценки точности алгоритма
    private static double calculateAccuracy(ClassificationAlgorithm algo,
                                            List<University> testList,
                                            Map<Integer, List<University>> trainByClasses,
                                            int numClasses) {
        int correctCount = 0;

        for (University testUni : testList) {
            int[] predictClass = algo.predictClass(testUni, trainByClasses, numClasses);
            int actualClass = testUni.getTargetClass();

            if (actualClass >= 1 && actualClass <= numClasses && predictClass[actualClass - 1] == 1) {
                correctCount++;
            }
        }

        return (double) correctCount / testList.size();
    }

    // Функция для группировки обучающей выборки на классы
    private static Map<Integer, List<University>> groupByClasses(List<University> trainList) {
        Map<Integer, List<University>> trainByClasses = new HashMap<>();
        for (University uni : trainList) {
            trainByClasses.computeIfAbsent(uni.getTargetClass(), k -> new ArrayList<>()).add(uni);
        }
        return trainByClasses;
    }

    // Метод для форматированного вывода таблицы
    private static void printResultsTable(List<ClassificationAlgorithm> algorithms, double[][] results, int numClasses) {
        System.out.printf("%-15s", "Алгоритм");
        for (int s = 1; s <= numClasses; s++) {
            System.out.printf(" | %-12s", "Разбиение " + s);
        }
        System.out.println();
        // Строки с алгоритмами
        for (int i = 0; i < algorithms.size(); i++) {
            // Вывод названия алгоритма
            String algoName = "A" + (i + 1);
            System.out.printf("%-15s", algoName);

            double sumAccuracy = 0.0;

            // Вывод точности по каждому разбиению
            for (int s = 0; s < numClasses; s++) {
                double acc = results[i][s] * 100;
                sumAccuracy += results[i][s];
                System.out.printf(" | %-11.2f%%", acc);
            }
            System.out.println();
        }
    }
}