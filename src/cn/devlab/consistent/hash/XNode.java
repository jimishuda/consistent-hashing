package cn.devlab.consistent.hash;

public class XNode implements Node {

    private String idc;
    private String ip;
    private Integer port;

    public XNode(String idc, String ip, Integer port) {
        this.idc = idc;
        this.ip = ip;
        this.port = port;
    }

    @Override
    public String getKey() {
        return  idc + "-" + ip + "-" + port;
    }

    @Override
    public String toString() {
        return "XNode{" +
                "idc='" + idc + '\'' +
                ", ip='" + ip + '\'' +
                ", port=" + port +
                '}';
    }
}
