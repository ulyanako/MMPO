public class University {
    private double[] features;
    private int targetClass; //1, если top 100; 2, если top 101-300;3, если top 301-500

    public University(double[] features, int targetClass) {
        this.features = features;
        this.targetClass = targetClass;
    }

    public double[] getFeatures() {
        return features;
    }

    public int getTargetClass() {
        return targetClass;
    }
}
