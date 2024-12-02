package lab04.client.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class QueueEntry {
    @JsonProperty("provider") // Nazwa świadczeniodawcy
    private String provider;

    @JsonProperty("benefit") // Nazwa usługi/świadczenia
    private String service;

    @JsonProperty("waitingTime") // Czas oczekiwania w dniach
    private int waitingTime;

    @JsonProperty("queueLength") // Długość kolejki w osobach
    private int queueLength;

    public QueueEntry() {}

    public String getProvider() {
        return provider;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public int getWaitingTime() {
        return waitingTime;
    }

    public void setWaitingTime(int waitingTime) {
        this.waitingTime = waitingTime;
    }

    public int getQueueLength() {
        return queueLength;
    }

    public void setQueueLength(int queueLength) {
        this.queueLength = queueLength;
    }

    @Override
    public String toString() {
        return String.format("Placówka: %s, Usługa: %s, Czas oczekiwania: %d dni, Długość kolejki: %d osób",
                provider, service, waitingTime, queueLength);
    }
}
