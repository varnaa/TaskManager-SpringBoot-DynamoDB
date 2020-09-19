package com.varnaa.taskManager.configuation;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.model.CreateTableRequest;
import com.amazonaws.services.dynamodbv2.model.ProvisionedThroughput;
import com.amazonaws.services.dynamodbv2.model.ResourceInUseException;
import com.amazonaws.util.StringUtils;
import com.varnaa.taskManager.model.Tasks;
import com.varnaa.taskManager.repository.TasksRepository;
import org.socialsignin.spring.data.dynamodb.repository.config.EnableDynamoDBRepositories;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

@Configuration
@EnableDynamoDBRepositories(basePackages = "com.varnaa.taskManager.repository")
public class DynamoDBConfig {
    @Value("${amazon.dynamodb.endpoint}")
    private String amazonDynamoDBEndpoint;

    @Value("${amazon.aws.accesskey}")
    private String amazonAWSAccessKey;

    @Value("${amazon.aws.secretkey}")
    private String amazonAWSSecretKey;

    @Bean
    public AmazonDynamoDB amazonDynamoDB() {
        AmazonDynamoDB amazonDynamoDB
                = new AmazonDynamoDBClient(amazonAWSCredentials());

        if (!StringUtils.isNullOrEmpty(amazonDynamoDBEndpoint)) {
            amazonDynamoDB.setEndpoint(amazonDynamoDBEndpoint);
        }

        return amazonDynamoDB;
    }

    @Bean
    public AWSCredentials amazonAWSCredentials() {
        return new BasicAWSCredentials(
                amazonAWSAccessKey, amazonAWSSecretKey);
    }


    @Autowired
    private AmazonDynamoDB amazonDynamoDB;
    private DynamoDBMapper dynamoDBMapper;
    @Autowired
    TasksRepository repository;

    @PostConstruct
    public void init() {
        createTaskTable();

    }

    public void createTaskTable() {
        try {
            dynamoDBMapper = new DynamoDBMapper(amazonDynamoDB);
            CreateTableRequest tableRequest = dynamoDBMapper.generateCreateTableRequest(Tasks.class);
            tableRequest.setProvisionedThroughput(new ProvisionedThroughput(1L, 1L));
            amazonDynamoDB.createTable(tableRequest);
        } catch (ResourceInUseException e) {
            System.out.println("Error found");
        }
    }

}
