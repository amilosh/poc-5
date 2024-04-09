package pl.amilosh.managementservice.config;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailService;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailServiceClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AWSConfig {

    @Value("${aws.s3.access.key}")
    private String awsAccessKey;

    @Value("${aws.s3.secret.key}")
    private String awsSecretKey;

    @Value("${aws.s3.region}")
    private String awsRegion;

    public AWSCredentials credentials() {
        return new BasicAWSCredentials(awsAccessKey, awsSecretKey);
    }

    @Bean
    public AmazonS3 amazonS3() {
        return AmazonS3ClientBuilder
            .standard()
            .withCredentials(new AWSStaticCredentialsProvider(credentials()))
            .withRegion(Regions.fromName(awsRegion))
            .build();
    }

    @Bean
    public AmazonSimpleEmailService amazonSes() {
        return AmazonSimpleEmailServiceClientBuilder
            .standard()
            .withCredentials(new AWSStaticCredentialsProvider(credentials()))
            .withRegion(Regions.fromName(awsRegion)).build();
    }
}
