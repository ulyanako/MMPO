import java.util.List;
import java.util.Map;

public interface ClassificationAlgorithm {
    int[] predictClass(University target, Map<Integer, List<University>> trainByClasses, int numClasses);
}
