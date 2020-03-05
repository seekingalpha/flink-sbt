package com.seekingalpha.dm_flink.events;

//import com.google.gson.Gson;
//import com.google.gson.JsonObject;
//import com.google.gson.JsonParser;

import org.json.JSONArray;
import org.json.JSONObject;

import jdk.nashorn.internal.parser.JSONParser;
import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.connectors.kinesis.FlinkKinesisConsumer;
import org.apache.flink.streaming.connectors.kinesis.config.ConsumerConfigConstants;

import org.apache.flink.api.common.functions.MapFunction;

import java.util.Properties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
//import scala.util.parsing.json.JSONObject;

public class PageEvents {
    public static Logger logger = LoggerFactory.getLogger(PageEvents.class);
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
        String path = "/home/maor/Documents/git/java/flink-sbt/src/test/resources/page_view/2020/03/03/15/example1.json";
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

    public static void main(String[] args) throws Exception {


        // set up the streaming execution environment
//        final StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        final StreamExecutionEnvironment env = StreamExecutionEnvironment.createLocalEnvironment().setParallelism(1);

        /* if you would like to use runtime configuration properties, uncomment the lines below
         * DataStream<String> input = createSourceFromApplicationProperties(env);
         */
        DataStream<String> input = createSourcePath(env);
        logger.warn("zzzz1");
        /* if you would like to use runtime configuration properties, uncomment the lines below
         * input.addSink(createSinkFromApplicationProperties())
         */

        DataStream<Tuple2<String, String>> input2 = input.map(new MapFunction<String, Tuple2<String, String>>()
        {
            @Override
            public Tuple2<String, String> map(String jsonString) {
//                JsonObject convertedObject = new Gson().fromJson(value, JsonObject.class);

                JSONObject obj = new JSONObject(jsonString);
                String reqTime = obj.getString("req_time");
                String machineIp = obj.getString("machine_ip");

                return new Tuple2<String, String>(reqTime, machineIp);
            }});



        input2.print();

//        input.addSink(createSinkFromStaticConfig());



        env.execute("Flink Streaming Java API Skeleton");

    }



}
