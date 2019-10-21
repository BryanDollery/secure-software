package com.dollery.services.catalog;

import static java.lang.String.format;

public class Relationship implements Comparable<Relationship> {
    private Service consumer;
    private Service provider;

    public Relationship(Service consumer, Service provider) {
        this.consumer = consumer;
        this.provider = provider;
    }

    public String getConsumerName() {
        return consumer.getName();
    }

    public String getProviderName() {
        return provider.getName();
    }

    public Service getConsumer() {
        return consumer;
    }

    public Service getProvider() {
        return provider;
    }

    @Override
    public String toString() {
        return format("{\"consumer\":\"%s\",\"provider\":\"%s\"}", this.consumer.getName(), this.provider.getName());
    }

    @Override
    public int compareTo(Relationship relationship) {
        int compareConsumer = consumer.getName().compareTo(relationship.consumer.getName());
        int compareProvider = provider.getName().compareTo(relationship.provider.getName());
        return compareConsumer != 0 ? compareConsumer : compareProvider;
    }
}
