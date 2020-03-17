package com.seekingalpha.dm_flink.events;



import javafx.util.Pair;
import jdk.nashorn.internal.parser.JSONParser;
import org.apache.flink.annotation.Public;
import org.apache.flink.api.common.ExecutionConfig;
import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.api.java.tuple.Tuple14;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.api.java.tuple.Tuple3;
import org.apache.flink.api.java.tuple.Tuple5;
import org.apache.flink.shaded.jackson2.com.fasterxml.jackson.core.JsonParser;
import org.apache.flink.shaded.jackson2.com.fasterxml.jackson.core.JsonProcessingException;
import org.apache.flink.shaded.jackson2.com.fasterxml.jackson.databind.DeserializationFeature;
import org.apache.flink.shaded.jackson2.com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.connectors.kinesis.FlinkKinesisConsumer;
import org.apache.flink.streaming.connectors.kinesis.config.ConsumerConfigConstants;

import org.apache.flink.api.common.functions.MapFunction;

import java.io.UnsupportedEncodingException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.Properties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import scala.Option;
import scala.Tuple6;

import static com.seekingalpha.dm_flink.common.BaseApplication.mainTimestampFormat;
import static com.seekingalpha.dm_flink.common.sql.*;
//import scala.util.parsing.json.JSONObject;


public class PageViewProcess {
    public static Logger logger = LoggerFactory.getLogger(PageViewProcess.class);
    private static final String region = "us-west-2";
    private static final String inputStreamName = "ExampleInputStream";
//    private static final String inputStreamName = "production-posts";
//    private static final String outputStreamName = "ExampleOutputStream";



    private static DataStream<String> createSourceFromStaticConfig(StreamExecutionEnvironment env) {
        Properties inputProperties = new Properties();
        inputProperties.setProperty(ConsumerConfigConstants.AWS_REGION, region);
        inputProperties.setProperty(ConsumerConfigConstants.STREAM_INITIAL_POSITION, "LATEST");

        return env.addSource(new FlinkKinesisConsumer<>(inputStreamName, new SimpleStringSchema(), inputProperties));
    }

    private static DataStream<String> createSourcePath(StreamExecutionEnvironment env) {


//        String path = "/home/maor/Documents/git/java/flink-sbt/src/test/resources/page_view/2020/03/03/15/example1.json";
        String path = "/Users/Maor/Documents/git/java/flink-sbt/src/test/resources/page_view/2020/03/03/15/example1.json";
        return env.readTextFile(path);

    }

//    private static DataStream<String> createSourceFromApplicationProperties(StreamExecutionEnvironment env) throws IOException {
//        Map<String, Properties> applicationProperties = KinesisAnalyticsRuntime.getApplicationProperties();
//        return env.addSource(new FlinkKinesisConsumer<>(inputStreamName, new SimpleStringSchema(),
//                applicationProperties.get("ConsumerConfigProperties")));
//    }

//    private static FlinkKinesisProducer<String> createSinkFromStaticConfig() {
//        Properties outputProperties = new Properties();
//        outputProperties.setProperty(ConsumerConfigConstants.AWS_REGION, region);
//        outputProperties.setProperty("AggregationEnabled", "false");
//
//        FlinkKinesisProducer<String> sink = new FlinkKinesisProducer<>(new SimpleStringSchema(), outputProperties);
//        sink.setDefaultStream(outputStreamName);
//        sink.setDefaultPartition("0");
//        return sink;
//    }

//    private static FlinkKinesisProducer<String> createSinkFromApplicationProperties() throws IOException {
//        Map<String, Properties> applicationProperties = KinesisAnalyticsRuntime.getApplicationProperties();
//        FlinkKinesisProducer<String> sink = new FlinkKinesisProducer<>(new SimpleStringSchema(),
//                applicationProperties.get("ProducerConfigProperties"));
//
//        sink.setDefaultStream(outputStreamName);
//        sink.setDefaultPartition("0");
//        return sink;
//    }


public static class PageViewSplitter implements MapFunction<String, Tuple6<String, String, String, String, String, Integer>>
    {
        final ObjectMapper mapper = new ObjectMapper()
                .configure(DeserializationFeature.ACCEPT_EMPTY_ARRAY_AS_NULL_OBJECT, true)
                .configure(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT, true)
                .configure(DeserializationFeature.FAIL_ON_NULL_FOR_PRIMITIVES, false)
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
                .configure(DeserializationFeature.READ_DATE_TIMESTAMPS_AS_NANOSECONDS, false);

        @Override
        public Tuple6<String, String, String, String, String, Integer> map(String jsonString) throws JsonProcessingException, UnsupportedEncodingException {
            PageViewInputSchema pageViewInput = mapper.readValue(jsonString, PageViewInputSchema.class); // parse json though setters in PageViewInputSchema

            LocalDateTime ldtNyNoTz = offsetStringToLocalDateTime(pageViewInput.getReqTime(), "America/New_York");
            String ts = ldtNyNoTz.format(mainTimestampFormat);

            String referrer = textDecoding(pageViewInput.getReferrer());

            String clientType = createClientType(pageViewInput.getPageType());
            String url = textDecoding(pageViewInput.getUrl());
            String urlParams = textDecoding(pageViewInput.getUrlParams());
            String urlFirstLevel = createUrlFirstLevel(Optional.ofNullable(url));
            String symbol = createSymbol(Optional.ofNullable(urlFirstLevel), Optional.ofNullable(clientType), Optional.ofNullable(url));
            String eventName = createPageViewEventName(Optional.ofNullable(urlFirstLevel));

            String userIdCode = createUserIdCode(pageViewInput.getUserId()); // to other_calc
            Integer UserId = createUserId(pageViewInput.getUserId()); // Integer can hold null and should not be converted with toString()

            Integer pxScore = pageViewInput.getPxScore().orElse((Integer)null);


            logger.warn("ts:" + ts + "; UserId: " + pageViewInput.getUserId() + "; userIdCode: " + userIdCode);



            return new Tuple6<> (ts, clientType, referrer, url, urlFirstLevel, pxScore);
        }


    }


    public static void main(String[] args) throws Exception {


        // set up the streaming execution environment
//        final StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        final StreamExecutionEnvironment env = StreamExecutionEnvironment.createLocalEnvironment().setParallelism(1);



        /* if you would like to use runtime configuration properties, uncomment the lines below
         * DataStream<String> input = createSourceFromApplicationProperties(env);
         */
        DataStream<String> input = createSourcePath(env);

        /* if you would like to use runtime configuration properties, uncomment the lines below
         * input.addSink(createSinkFromApplicationProperties())
         */


        SingleOutputStreamOperator<Tuple6<String, String, String, String, String, Integer>> zz = input.map(new PageViewSplitter());

//        zz.print();



//        zz.writeAsCsv("/home/maor/Documents/git/java/flink-sbt/src/test/resources/page_view/2020/03/03/15/result/");






//        input.addSink(createSinkFromStaticConfig());



        env.execute("Flink Streaming Java API Skeleton");

    }



}
