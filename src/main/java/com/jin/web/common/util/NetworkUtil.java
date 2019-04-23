package com.jin.web.common.util;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.NetworkInterface;

/** 
 * 常用获取客户端信息的工具 
 *  
 */  
public final class NetworkUtil {  
    /** 
     * Logger for this class 
     */  
    private static Logger logger = LoggerFactory.getLogger(NetworkUtil.class);

    private static final String unknown="unknown";
    private static final String X_Forwarded_For="X-Forwarded-For";
    private static final String Proxy_Client_IP="Proxy-Client-IP";
    private static final String WL_Proxy_Client_IP="WL-Proxy-Client-IP";
    private static final String HTTP_CLIENT_IP="HTTP_CLIENT_IP";
    private static final String HTTP_X_FORWARDED_FOR="HTTP_X_FORWARDED_FOR";

    /** 
     * 获取请求主机IP地址,如果通过代理进来，则透过防火墙获取真实IP地址; 
     *  
     * @param request 
     * @return 
     * @throws IOException 
     */  
    public final static String getIpAddress(HttpServletRequest request) throws IOException {
        // 获取请求主机IP地址,如果通过代理进来，则透过防火墙获取真实IP地址  

        String type=X_Forwarded_For;
        String ip = request.getHeader(X_Forwarded_For);


        if (ip == null || ip.length() == 0 || unknown.equalsIgnoreCase(ip)) {
            if (ip == null || ip.length() == 0 || unknown.equalsIgnoreCase(ip)) {
                ip = request.getHeader(Proxy_Client_IP);
                type=Proxy_Client_IP;
            }
            if (ip == null || ip.length() == 0 || unknown.equalsIgnoreCase(ip)) {
                ip = request.getHeader(WL_Proxy_Client_IP);
                type=WL_Proxy_Client_IP;
            }  
            if (ip == null || ip.length() == 0 || unknown.equalsIgnoreCase(ip)) {
                ip = request.getHeader(HTTP_CLIENT_IP);
                type=HTTP_CLIENT_IP;
            }  
            if (ip == null || ip.length() == 0 || unknown.equalsIgnoreCase(ip)) {
                ip = request.getHeader(HTTP_X_FORWARDED_FOR);
                type=HTTP_X_FORWARDED_FOR;
            }  
            if (ip == null || ip.length() == 0 || unknown.equalsIgnoreCase(ip)) {
                ip = request.getRemoteAddr();
                type=unknown;
            }
        } else if (ip.length() > 15) {
            String[] ips = ip.split(",");
            for (int index = 0; index < ips.length; index++) {
                String strIp = ips[index];
                if (!( unknown.equalsIgnoreCase(strIp) )) {
                    ip = strIp;
                    break;
                }
            }
        }
        logger.info("getIpAddress(HttpServletRequest) {}={} ",type,ip);
        return ip;  
    }  
    
    /**
     * 通过IP地址获取MAC地址
     * @param ip String,127.0.0.1格式
     * @return mac String
     * @throws Exception
     */
    public static String getMACAddress(String ip) throws Exception {
        String line = "";
        String macAddress = "";
        final String MAC_ADDRESS_PREFIX = "MAC Address = ";
        final String LOOPBACK_ADDRESS = "127.0.0.1";
        //如果为127.0.0.1,则获取本地MAC地址。
        if (LOOPBACK_ADDRESS.equals(ip)) {
            InetAddress inetAddress = InetAddress.getLocalHost();
            //貌似此方法需要JDK1.6。
            byte[] mac = NetworkInterface.getByInetAddress(inetAddress).getHardwareAddress();
            //下面代码是把mac地址拼装成String
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < mac.length; i++) {
                if (i != 0) {
                    sb.append("-");
                }
                //mac[i] & 0xFF 是为了把byte转化为正整数
                String s = Integer.toHexString(mac[i] & 0xFF);
                sb.append(s.length() == 1 ? 0 + s : s);
            }
            //把字符串所有小写字母改为大写成为正规的mac地址并返回
            macAddress = sb.toString().trim().toUpperCase();
            return macAddress;
        }
        //获取非本地IP的MAC地址
        try {
            Process p = Runtime.getRuntime().exec("nbtstat -A " + ip);
            InputStreamReader isr = new InputStreamReader(p.getInputStream());
            BufferedReader br = new BufferedReader(isr);
            while ((line = br.readLine()) != null) {
                if (line != null) {
                    int index = line.indexOf(MAC_ADDRESS_PREFIX);
                    if (index != -1) {
                        macAddress = line.substring(index + MAC_ADDRESS_PREFIX.length()).trim().toUpperCase();
                    }
                }
            }
            br.close();
        } catch (IOException e) {
            e.printStackTrace(System.out);
        }
        return macAddress;
    }
}  