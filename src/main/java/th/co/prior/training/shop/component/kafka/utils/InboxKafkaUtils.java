package th.co.prior.training.shop.component.kafka.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.annotation.PostConstruct;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.PartitionInfo;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import th.co.prior.training.shop.component.kafka.component.KafkaProducerComponent;
import th.co.prior.training.shop.entity.InboxEntity;

import java.util.List;
import java.util.Random;

@Component
@RequiredArgsConstructor
@Slf4j
public class InboxKafkaUtils {

    @NonNull
    private KafkaProducerComponent kafkaTemplate;

    private ObjectMapper mapper;

    private Random random;

    private List<PartitionInfo> partitionInfos;

    @Value("${app.config.kafka.topic}")
    private String topic;

    @PostConstruct
    public void init() {
        this.mapper = new ObjectMapper();
        this.mapper.registerModule(new JavaTimeModule());
        this.random = new Random();
        this.partitionInfos = this.kafkaTemplate.getKafkaTemplate().partitionsFor(topic);
    }

    public void execute(InboxEntity inbox) {
        try {
            String req = this.mapper.writeValueAsString(inbox);
            int nextPartition = this.generateSizePartition();
            log.info("partition: {}", nextPartition);

            this.kafkaTemplate.send(topic, nextPartition, String.valueOf(inbox.getId()), req);
        } catch (Exception e){
            throw new RuntimeException(e.getMessage());
        }
    }

    public int generateSizePartition() {
        int num;
        int index = this.random.nextInt(this.partitionInfos.size());
        num = this.partitionInfos.get(index).partition();
        return num;
    }
}
