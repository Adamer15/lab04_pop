module lab04.client {
    requires java.net.http;
    requires com.fasterxml.jackson.databind;

    exports lab04.client.model;
    exports lab04.client;
}
