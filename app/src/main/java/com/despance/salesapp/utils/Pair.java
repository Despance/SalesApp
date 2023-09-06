package com.despance.salesapp.utils;

class Pair {
    private int tag;
    private Object value;

    protected Pair(int tag, Object value) {
        this.tag = tag;
        this.value = value;
    }

    protected int getTag() {
        return tag;
    }

    protected Object getValue() {
        return value;

    }
}
