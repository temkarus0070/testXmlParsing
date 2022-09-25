package org.temkarus0070;

public class TestData {
    public double timeToParse;
    public long maxMemoryUsed;
    public String className;

    public TestData() {
    }

    public TestData(double timeToParse) {
        this.timeToParse = timeToParse;
    }

    public TestData(double timeToParse, String className) {
        this.timeToParse = timeToParse;
        this.className = className;
    }

    public TestData(double timeToParse, long maxMemoryUsed) {
        this.timeToParse = timeToParse;
        this.maxMemoryUsed = maxMemoryUsed;
    }

    public TestData(double timeToParse, long maxMemoryUsed, String className) {
        this.timeToParse = timeToParse;
        this.maxMemoryUsed = maxMemoryUsed;
        this.className = className;
    }
}
