package lab04.client.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class QueueEntry {

    @JsonProperty("type")
    private String type;

    @JsonProperty("id")
    private String id;

    @JsonProperty("attributes")
    private Attributes attributes;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Attributes getAttributes() {
        return attributes;
    }

    public void setAttributes(Attributes attributes) {
        this.attributes = attributes;
    }

    @Override
    public String toString() {
        return String.format("Placówka: %s, Usługa: %s, Czas oczekiwania: %d dni, Długość kolejki: %d osób",
                attributes.getProvider(),
                attributes.getBenefit(),
                attributes.getStatistics() != null ? attributes.getStatistics().getProviderData().getAveragePeriod() : 0,
                attributes.getStatistics() != null ? attributes.getStatistics().getProviderData().getAwaiting() : 0);
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Attributes {
        @JsonProperty("provider")
        private String provider;

        @JsonProperty("benefit")
        private String benefit;

        @JsonProperty("statistics")
        private Statistics statistics;

        public String getProvider() {
            return provider;
        }

        public void setProvider(String provider) {
            this.provider = provider;
        }

        public String getBenefit() {
            return benefit;
        }

        public void setBenefit(String benefit) {
            this.benefit = benefit;
        }

        public Statistics getStatistics() {
            return statistics;
        }

        public void setStatistics(Statistics statistics) {
            this.statistics = statistics;
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Statistics {
        @JsonProperty("provider-data")
        private ProviderData providerData;

        public ProviderData getProviderData() {
            return providerData;
        }

        public void setProviderData(ProviderData providerData) {
            this.providerData = providerData;
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class ProviderData {
        @JsonProperty("awaiting")
        private int awaiting;

        @JsonProperty("average-period")
        private int averagePeriod;

        public int getAwaiting() {
            return awaiting;
        }

        public void setAwaiting(int awaiting) {
            this.awaiting = awaiting;
        }

        public int getAveragePeriod() {
            return averagePeriod;
        }

        public void setAveragePeriod(int averagePeriod) {
            this.averagePeriod = averagePeriod;
        }
    }
}
