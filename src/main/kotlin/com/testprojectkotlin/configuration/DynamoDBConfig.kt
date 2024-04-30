//package com.testprojectkotlin.configuration
//
//import com.amazonaws.auth.AWSCredentials
//import com.amazonaws.auth.AWSStaticCredentialsProvider
//import com.amazonaws.auth.BasicAWSCredentials
//import com.amazonaws.client.builder.AwsClientBuilder
//import com.amazonaws.regions.Regions
//import com.amazonaws.services.dynamodbv2.AmazonDynamoDB
//import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder
//import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper
//import org.socialsignin.spring.data.dynamodb.repository.config.EnableDynamoDBRepositories
//import org.springframework.beans.factory.annotation.Value
//import org.springframework.context.annotation.Bean
//import org.springframework.context.annotation.Configuration
//
//@Configuration
//@EnableDynamoDBRepositories(basePackages = ["com.testprojectkotlin.repository"])
//class DynamoDBConfig {
//    @Value("\${amazon.dynamodb.endpoint}")
//    private val amazonDynamoDBEndpoint: String? = null
//
//    @Value("\${amazon.aws.accesskey}")
//    private val amazonAWSAccessKey: String? = null
//
//    @Value("\${amazon.aws.secretkey}")
//    private val amazonAWSSecretKey: String? = null
//
////    @Bean
////    fun dynamoDBMapper(): DynamoDBMapper = DynamoDBMapper(amazonDynamoDB())
//
//    @Bean
//    fun amazonDynamoDB(): AmazonDynamoDB {
//        return AmazonDynamoDBClientBuilder.standard()
//            .withRegion(Regions.AP_EAST_1)
//            .withCredentials(AWSStaticCredentialsProvider(amazonAWSCredentials()))
//            .build()
//    }
//
//    fun amazonAWSCredentials(): AWSCredentials = BasicAWSCredentials(amazonAWSAccessKey, amazonAWSSecretKey)
//
//}