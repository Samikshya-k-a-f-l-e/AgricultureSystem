package org.agro.agriculturesystem.decision;

public class Node {
    private String feature;
    private Object splitValue;
    private boolean isLeaf;
    private double predictedValue;
    private Node left;
    private Node right;

    public String getFeature() { return feature; }
    public void setFeature(String feature) { this.feature = feature; }

    public Object getSplitValue() { return splitValue; }
    public void setSplitValue(Object splitValue) { this.splitValue = splitValue; }

    public boolean isLeaf() { return isLeaf; }
    public void setLeaf(boolean leaf) { isLeaf = leaf; }

    public double getPredictedValue() { return predictedValue; }
    public void setPredictedValue(double predictedValue) { this.predictedValue = predictedValue; }

    public Node getLeft() { return left; }
    public void setLeft(Node left) { this.left = left; }

    public Node getRight() { return right; }
    public void setRight(Node right) { this.right = right; }
}
