package com.testprojectkotlin.configuration

import com.amazonaws.auth.AWSCredentials
import com.amazonaws.auth.AWSCredentialsProvider
import com.amazonaws.auth.AWSStaticCredentialsProvider
import com.amazonaws.auth.BasicAWSCredentials
import com.amazonaws.client.builder.AwsClientBuilder
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper
import org.socialsignin.spring.data.dynamodb.repository.config.EnableDynamoDBRepositories
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary

@Configuration
@EnableDynamoDBRepositories(basePackages = ["com.testprojectkotlin.repository"])
class DynamoDBConfig {
    @Value("\${amazon.dynamodb.endpoint}")
    private var amazonDynamoDBEndpoint: String? = null

    @Value("\${amazon.aws.accesskey}")
    private var amazonAWSAccessKey: String? = null

    @Value("\${amazon.aws.secretkey}")
    private var amazonAWSSecretKey: String? = null

    @Bean
    @Primary
    fun dynamoDBMapper(): DynamoDBMapper = DynamoDBMapper(amazonDynamoDB())

    @Bean
    fun amazonDynamoDB(): AmazonDynamoDB {
        return AmazonDynamoDBClientBuilder.standard()
            .withEndpointConfiguration(AwsClientBuilder.EndpointConfiguration(amazonDynamoDBEndpoint, ""))
            .withCredentials(amazonAWSCredentialsProvider())
            .build()
    }

    @Bean
    fun amazonAWSCredentials(): AWSCredentials = BasicAWSCredentials(amazonAWSAccessKey, amazonAWSSecretKey)

    @Bean
    fun amazonAWSCredentialsProvider(): AWSCredentialsProvider = AWSStaticCredentialsProvider(amazonAWSCredentials())

}