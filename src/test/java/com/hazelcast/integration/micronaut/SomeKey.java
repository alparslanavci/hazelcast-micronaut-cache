package com.hazelcast.integration.micronaut;

import java.io.Serializable;
import java.util.Objects;

public class SomeKey implements Serializable{
    private String someString;
    private int someInteger;

    public SomeKey(String someString, int someInteger) {
        this.someString = someString;
        this.someInteger = someInteger;
    }

    public String getSomeString() {
        return someString;
    }

    public void setSomeString(String someString) {
        this.someString = someString;
    }

    public int getSomeInteger() {
        return someInteger;
    }

    public void setSomeInteger(int someInteger) {
        this.someInteger = someInteger;
    }

    @Override
    public String toString() {
        return "SomeValue{" +
                "someString='" + someString + '\'' +
                ", someInteger=" + someInteger +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SomeKey someKey = (SomeKey) o;
        return someInteger == someKey.someInteger &&
                Objects.equals(someString, someKey.someString);
    }

    @Override
    public int hashCode() {

        return Objects.hash(someString, someInteger);
    }
}
