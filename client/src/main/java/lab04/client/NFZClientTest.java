//package lab04.client;
//
//import lab04.client.model.QueueEntry;
//
//import java.io.IOException;
//import java.util.List;
//
//public class NFZClientTest {
//    public static void main(String[] args) {
//        NFZClient client = new NFZClient();
//
//        try {
//            // Pobierz dane dla usługi "PORADNIA STOMATOLOGICZNA" w województwie mazowieckim, w Warszawie
//            List<QueueEntry> entries = client.getQueueEntriesByBenefit(
//                    "PORADNIA STOMATOLOGICZNA", 25, "03", "Lublin");
//
//            // Wyświetl wyniki w konsoli
//            for (QueueEntry entry : entries) {
//                System.out.println(entry);
//            }
//        } catch (IOException | InterruptedException e) {
//            e.printStackTrace();
//        }
//    }
//}
