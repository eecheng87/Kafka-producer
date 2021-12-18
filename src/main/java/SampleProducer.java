import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.header.Header;


import java.util.Properties;
import java.util.List;
import java.util.ArrayList;

// docker run -i -t astraea/broker:2.8.1
// bin/kafka-console-consumer.sh --bootstrap-server 192.168.206.61:12976 --topic channel -- from-beginning

public class SampleProducer {
    String brokers;
    String topic;
    int records;
    int recordSize;

    public void parser(String[] args){
        int index = 0;
        while(index < 8){
            if(args[index].equals("--brokers")){
                brokers = args[++index];
            }else if(args[index].equals("--topic")){
                topic = args[++index];
            }else if(args[index].equals("--records")){
                records = Integer.parseInt(args[++index]);
            }else if(args[index].equals("--recordSize")){
                recordSize = Integer.parseInt(args[++index]);
            }else{
                // TODO: throw exception
                System.out.println("Invalid argument");
            }
            index++;
        }
    }

    public SampleProducer(String[] args){
        parser(args);
        Properties properties = new Properties();
        properties.put("bootstrap.servers", brokers);
        properties.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        properties.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        //ProducerRecord producerRecord = new ProducerRecord("channel", "name", "selftuts");
        KafkaProducer kafkaProducer = new KafkaProducer(properties);
        System.out.printf("-> %s %s %d %d\n", brokers, topic, records, recordSize);

        for(int i = 0; i < records; i++) {
            List<Header> headers = new ArrayList<Header>();
            headers.add(new Dummy(recordSize));

            kafkaProducer.send(new ProducerRecord<String, String>(
                    topic,
                    null,
                    String.format("key-%010d", i),
                    String.format("value-%010d", i),
                    headers
            ));
        }
        System.out.println("Message sent successfully");
        kafkaProducer.close();
    }
}
