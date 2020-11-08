package com.example.hackandroidians;

public class SourceString
{
    public String Name;
    public int intermediate;
    public int source;
    public int destination;


    public SourceString(String name, int intermediate, int source, int destination) {
        Name = name;
        this.intermediate = intermediate;
        this.source = source;
        this.destination = destination;
    }


    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public int getIntermediate() {
        return intermediate;
    }

    public void setIntermediate(int intermediate) {
        this.intermediate = intermediate;
    }

    public int getSource() {
        return source;
    }

    public void setSource(int source) {
        this.source = source;
    }

    public int getDestination() {
        return destination;
    }

    public void setDestination(int destination) {
        this.destination = destination;
    }
}
