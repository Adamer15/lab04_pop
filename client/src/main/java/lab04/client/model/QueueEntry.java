package lab04.client.model;

import javafx.beans.property.*;

public class QueueEntry {
    private final StringProperty provider = new SimpleStringProperty();
    private final StringProperty locality = new SimpleStringProperty();
    private final StringProperty benefit = new SimpleStringProperty();
    private final IntegerProperty queueLength = new SimpleIntegerProperty();
    private final IntegerProperty waitingTime = new SimpleIntegerProperty();

    public StringProperty providerProperty() {
        return provider;
    }

    public String getProvider() {
        return provider.get();
    }

    public void setProvider(String provider) {
        this.provider.set(provider);
    }

    public StringProperty localityProperty() {
        return locality;
    }

    public String getLocality() {
        return locality.get();
    }

    public void setLocality(String locality) {
        this.locality.set(locality);
    }

    public StringProperty benefitProperty() {
        return benefit;
    }

    public String getBenefit() {
        return benefit.get();
    }

    public void setBenefit(String benefit) {
        this.benefit.set(benefit);
    }

    public IntegerProperty queueLengthProperty() {
        return queueLength;
    }

    public int getQueueLength() {
        return queueLength.get();
    }

    public void setQueueLength(int queueLength) {
        this.queueLength.set(queueLength);
    }

    public IntegerProperty waitingTimeProperty() {
        return waitingTime;
    }

    public int getWaitingTime() {
        return waitingTime.get();
    }

    public void setWaitingTime(int waitingTime) {
        this.waitingTime.set(waitingTime);
    }
}
