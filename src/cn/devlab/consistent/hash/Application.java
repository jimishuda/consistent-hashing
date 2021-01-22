package cn.devlab.consistent.hash;

import java.util.Arrays;

public class Application {
    public static void main(String[] args) {
        XNode node1 = new XNode("IDC1", "10.8.1.11", 8080);
        XNode node2 = new XNode("IDC1", "10.8.3.99", 8080);
        XNode node3 = new XNode("IDC1", "10.9.11.105", 8080);
        XNode node4 = new XNode("IDC1", "10.10.9.210", 8080);

        ConsistentHashing<XNode> consistentHashRouter = new ConsistentHashing<>(Arrays.asList(node1,node2,node3,node4),20);

        String requestIP1 = "192.168.0.1";
        String requestIP2 = "192.168.0.2";
        String requestIP3 = "192.168.0.3";
        String requestIP4 = "192.168.0.4";
        String requestIP5 = "192.168.0.5";

        goRoute(consistentHashRouter,requestIP1,requestIP2,requestIP3,requestIP4,requestIP5);
    }


    private static void goRoute(ConsistentHashing<XNode> consistentHashRouter ,String ... requestIps){
        for (String requestIp: requestIps) {
            System.out.println(requestIp + " is route to " + consistentHashRouter.rout(requestIp));
        }
    }
}
