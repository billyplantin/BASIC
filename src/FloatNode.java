public class FloatNode extends Node{


    private float value;

    public FloatNode(float value) {
        this.value = value;
    }

    public float getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "FloatNode(" + value + ")";
    }
}
