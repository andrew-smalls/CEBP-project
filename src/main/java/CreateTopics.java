

public class CreateTopics {

    public static void main(String[] args) throws Exception {
    /*
        BasicConfigurator.configure();  // fixes some slf4j bugs

        ZkClient zkClient = null;
        ZkUtils zkUtils = null;
        try {
            String zookeeperHosts = "localhost:2181"; // If multiple zookeeper then -> String zookeeperHosts = "192.168.20.1:2181,192.168.20.2:2181";
            int sessionTimeOutInMs = 15 * 1000; // 15 secs
            int connectionTimeOutInMs = 10 * 1000; // 10 secs

            zkClient = new ZkClient(zookeeperHosts, sessionTimeOutInMs, connectionTimeOutInMs); //, ZKStringSerializer$.MODULE$);
            zkClient.setZkSerializer(new ZkSerializer() {
                @Override
                public byte[] serialize(Object o) throws ZkMarshallingError {
                    return ZKStringSerializer.serialize(o);
                }

                @Override
                public Object deserialize(byte[] bytes) throws ZkMarshallingError {
                    return ZKStringSerializer.deserialize(bytes);
                }
            });
            zkUtils = new ZkUtils(zkClient, new ZkConnection(zookeeperHosts), false);

            String topicName = "testTopic";
            int noOfPartitions = 2;
            int noOfReplications = 1;
            Properties topicConfiguration = new Properties();

            AdminUtils.createTopic(zkUtils, topicName, noOfPartitions, noOfReplications, topicConfiguration, RackAwareMode.Enforced$.MODULE$);

        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            if (zkClient != null) {
                zkClient.close();
            }
        }

     */
    }
}