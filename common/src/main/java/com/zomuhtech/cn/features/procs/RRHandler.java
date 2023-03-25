/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.zomuhtech.cn.features.procs;

/**
 *
 * @author Zomuh Tech
 */
import ca.weblite.codename1.json.JSONArray;
import ca.weblite.codename1.json.JSONException;
import ca.weblite.codename1.json.JSONObject;
import com.codename1.components.ToastBar;
import com.codename1.io.ConnectionRequest;
import com.codename1.io.JSONParser;
import com.codename1.io.Log;
import com.codename1.io.NetworkManager;
import com.codename1.io.gzip.GZConnectionRequest;
import com.codename1.io.gzip.GZIPInputStream;
import com.codename1.io.rest.Response;
import com.codename1.ui.Form;
import com.codename1.ui.events.ActionEvent;
import com.codename1.ui.events.ActionListener;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Map;
import com.codename1.io.rest.Rest;

public class RRHandler {

    public boolean prod = true;
    UnplainT unPlainT = new UnplainT();
    Proc proc = new Proc();
    Form currForm, dash;
    int circleSize;

    private void showReqResp(String value) {
        if (!prod) {
            //System.out.println(value);
            Log.p(value);
        }
        //Log.p(value);
    }

    /* 
    Synchronous - updating UI on network error   
     */
    public void processReq(RespHandler respH, String reqData,
            final String taskTag) {

        if (!taskTag.equals("upload")) {
            showReqResp("REQ>>" + reqData);
        }
        String encT = unPlainT.unPlain(reqData);
        if (!taskTag.equals("upload")) {
            showReqResp("EncREQ>> " + encT);
        }

        String authCode = "eriq:" + "123";

        String url;
        if (prod) {
            //request.setUrl("https://cn.adv.ft.zomuh-tech.com/proc_img");
            url = "https://cn.adv.ft.zomuh-tech.com/networking_p";
            //192.168.43.182
            //url = "http://192.168.43.182/AdvanceFt/networking_p";
        } else {
            //request.setUrl("http://127.0.0.1/AdvanceFt/proc_img");
            //url = "http://127.0.0.1/AdvanceFt/networking_p";
            url = "http://127.0.0.1/AdvanceFt/networking_t";
        }

        //POST
        //ConnectionRequest request = new ConnectionRequest(url, true);
        ConnectionRequest request = new ConnectionRequest();
        request.setUrl(url);
        request.setHttpMethod("POST");

        //GET
        //ConnectionRequest request = new ConnectionRequest(url, false);
        request.addRequestHeader("Authorization", authCode);//add req header
        request.setTimeout(15000);
        request.setReadTimeout(20000);
        request.addArgument("reqData", encT);
        request.setFailSilently(true);

        NetworkManager.getInstance().addToQueueAndWait(request);

        showReqResp("\nRespCode1 " + request.getResponseCode());

        /*byte[] rs = request.getResponseData();
        String res = new String(rs);

        showReqResp("Resp<< " + res);*/
        switch (request.getResponseCode()) {

            case 0:
                //showReqResp("Connection failed");
                ToastBar.showErrorMessage("Connection failed");
                break;

            case 404:
                //showReqResp("Services Unavailable");
                ToastBar.showErrorMessage("Services Unavailable");
                break;

            case 200:

                byte[] result = request.getResponseData();
                String resp = new String(result).trim();
                showReqResp("EncResp<< " + resp);
                String decT = unPlainT.plainT(resp);
                showReqResp("Resp<< " + decT);
                respH.getResp(decT, taskTag);

                /*try {
                Map<String, Object> res = new JSONParser()
                        .parseJSON(new InputStreamReader(
                                new ByteArrayInputStream(
                                        request.getResponseData()),
                                "UTF-8"));

                showReqResp("Result<< " + res);

                Map<String, Object> response = (Map<String, Object>) res.get("response");
                showReqResp("Resp<< " + response);
                respH.getResp(response.toString(), taskTag);

            } catch (IOException e) {
            }*/
                //showReqResp("Resp<< " + resp);
                //showReqResp("Resp<< " + response);
                //respH.getResp(response.toString(), taskTag);
                break;

            default:
                ToastBar.showErrorMessage("Processing failed");
                byte[] rs = request.getResponseData();
                String res = new String(rs);
                showReqResp("Resp<< " + res);
                break;
        }

    }

    /* 
    Synchronous - updating UI on network error   
     */
    public void processGZReq(RespHandler respH, String reqData,
            final String taskTag) {

        showReqResp("REQ>>" + reqData);
        String encT = unPlainT.unPlain(reqData);
        showReqResp("EncREQ>> " + encT);

        String authCode = "eriq:" + "123";

        String url;
        if (prod) {
            //request.setUrl("https://cn.adv.ft.zomuh-tech.com/proc_img");
            url = "https://cn.adv.ft.zomuh-tech.com/networking_p";
            //192.168.43.182
            //url = "http://192.168.43.182/AdvanceFt/networking_p";
        } else {
            //request.setUrl("http://127.0.0.1/AdvanceFt/proc_img");
            //url = "http://127.0.0.1/AdvanceFt/networking_p";
            url = "http://127.0.0.1/AdvanceFt/networking_t";
        }

        //POST
        //ConnectionRequest request = new ConnectionRequest(url, true);
//        GZConnectionRequest request = new GZConnectionRequest();
//        request.setUrl(url);
//        request.setHttpMethod("POST");
        //GET
        //GZConnectionRequest request = new GZConnectionRequest(url, false);
        //request.addRequestHeader("Authorization", authCode);//add req header
        //ConnectionRequest request = new ConnectionRequest() {
        
        GZConnectionRequest request = new GZConnectionRequest();
//        GZConnectionRequest request = new GZConnectionRequest() {
//
//            private String resp;
//            Result result;
//
//            @Override
//            protected void readHeaders(Object connection) throws IOException {
//                proc.printLine("Object " + connection);
//                String[] headerNames = getHeaderFieldNames(connection);
//
//                for (String headerName : headerNames) {
//
//                    String headerValue = getHeader(connection, headerName);
//                    proc.printLine("HeaderName: " + headerName
//                            + " HeaderValue: " + headerValue);
//                }
//            }
//
//            //protected void readResponse(InputStream input) throws IOException {
//            @Override
//            protected void readUnzipedResponse(InputStream input) throws IOException {
//                result = Result.fromContent(input, Result.JSON);
//                resp = result.getAsString("root");
//
//                showReqResp("Input Result1 " + result.toString());
//                showReqResp("Input Resp1 " + resp);
//            }
//
//            @Override
//            protected void postResponse() {
//                showReqResp("Input Result2 " + result.toString());
//                showReqResp("Input Resp2 " + resp);
//            }
//        };
        request.setUrl(url);
        request.addRequestHeader("Authorization", authCode);//add req header
        request.setHttpMethod("POST");
        request.addRequestHeader("Accept-Encoding", "gzip, deflate");//add req header
        request.setTimeout(15000);
        request.setReadTimeout(20000);
        request.addArgument("reqData", encT);
        request.setFailSilently(true);

        NetworkManager.getInstance().addToQueueAndWait(request);
        NetworkManager.getInstance().addErrorListener((e) -> e.consume());

        showReqResp("\nRespCode1 " + request.getResponseCode());

        /*byte[] rs = request.getResponseData();
        String res = new String(rs);

        showReqResp("Resp<< " + res);*/
        switch (request.getResponseCode()) {

            case 0:
                //showReqResp("Connection failed");
                ToastBar.showErrorMessage("Connection failed");
                break;

            case 404:
                //showReqResp("Services Unavailable");
                ToastBar.showErrorMessage("Services Unavailable");
                break;

            case 200:

                String contentEncoding;
                //Hashtable parsedResp;
                Map parsedResp;

                try {
                    //contentEncoding = GZConnectionRequest.g

                    byte[] result = request.getResponseData();
                    showReqResp("Size " + result.length + " bytes");
                    showReqResp("RR " + new String(result));

                    InputStreamReader is = new InputStreamReader(new GZIPInputStream(new ByteArrayInputStream(result)));
                    //InputStreamReader is = new InputStreamReader(new ByteArrayInputStream(result));

                    parsedResp = new JSONParser().parseJSON(is);
                    //String resp = new String(result).trim();
                    String resp = parsedResp.toString();
                    showReqResp("EncResp<< " + resp);
                    String decT = unPlainT.plainT(resp);
                    showReqResp("Resp<< " + decT);
                    respH.getResp(decT, taskTag);

                } catch (Exception e) {
                    showReqResp("Exc " + e.getMessage());
                }

                /*try {
                Map<String, Object> res = new JSONParser()
                        .parseJSON(new InputStreamReader(
                                new ByteArrayInputStream(
                                        request.getResponseData()),
                                "UTF-8"));

                showReqResp("Result<< " + res);

                Map<String, Object> response = (Map<String, Object>) res.get("response");
                showReqResp("Resp<< " + response);
                respH.getResp(response.toString(), taskTag);

            } catch (IOException e) {
            }*/
                //showReqResp("Resp<< " + resp);
                //showReqResp("Resp<< " + response);
                //respH.getResp(response.toString(), taskTag);
                break;

            default:
                ToastBar.showErrorMessage("Processing failed");
                byte[] rs = request.getResponseData();
                String res = new String(rs);
                showReqResp("Resp<< " + res);
                break;
        }

    }

    //Asynchronous - not updating UI on network error
    public void processGZReq_(RespHandler respH, String reqData,
            final String taskTag) {

        reqData = reqData + ">CN1";
        showReqResp("REQ>>" + reqData);

        String encT = unPlainT.unPlain(reqData);
        showReqResp("EncREQ>> " + encT);

        NetworkManager.getInstance().addToQueue(new GZConnectionRequest() {

            private Object connection;
            private String contentEncoding;
            ///private Hashtable parsedResponse;
            private Map parsedResponse;

            {
                setPost(true);
                setUrl("http://127.0.0.1/AdvanceFt/networking_t");
                addArgument("reqData", encT);
                addRequestHeader("Accept-Encoding", "gzip");//add req header

                addResponseListener(new ActionListener() {

                    @Override
                    public void actionPerformed(ActionEvent evt) {
                        byte[] data = getResponseData();
                        if (data == null) {
                            showReqResp("Empty response");
                        }

                        InputStreamReader is = null;
//                        try {
//                            //user readHeader() instead                       
//                           contentEncoding = getHeader(connection, "Content-Encoding");
//                            showReqResp("Content Encoding " + contentEncoding);
//                        } catch (IOException e) {
//
//                        }

//                        if (contentEncoding != null
//                                && contentEncoding.toLowerCase().endsWith("gzip")) {
                        try {
                            is = new InputStreamReader(new GZIPInputStream(
                                    new ByteArrayInputStream(data)));
                        } catch (IOException e) {
                            showReqResp("Error decoding gzip stream " + e.getMessage());
                        }
//                        } else {
//                            is = new InputStreamReader(
//                                    new ByteArrayInputStream(data));
//                        }

                        try {
                            parsedResponse = new JSONParser().parse(is);
                            showReqResp("Resp " + parsedResponse.toString());
                        } catch (IOException e) {

                        }
                    }

                });
            }

        });

//        //try {
//        ConnectionRequest request = new ConnectionRequest() {
//            private String resp;
//            Result result;
//
//            @Override
//            protected void readResponse(InputStream input) throws IOException {
//
//                //result = Result.fromContent(input, Result.JSON);
//                //resp = result.getAsString("root");
//            }
//
//            @Override
//            protected void postResponse() {
//
//                showReqResp("RR " + result.toString());
//                showReqResp("RESP<<" + resp);
//
//                if ((result.toString()).equals("{}") && resp == null) {
//
//                    respH.getResp("", taskTag);
//
//                } else {
//
//                    switch (taskTag) {
//                        case "getCand":
//                            respH.getResp(result.toString(), taskTag);
//                            break;
//                        default:
//                            respH.getResp(resp, taskTag);
//                            break;
//                    }
//                }
//            }
//        };
//
//        request.setUrl("http://127.0.0.1/AdvanceFt/networking_t");
//
//        request.setHttpMethod("POST");
//        request.addRequestHeader("Accept-Encoding", "gzip");//add req header
//        request.setTimeout(15000);
//        request.setReadTimeout(20000);
//        request.addArgument("reqData", encT);
//
//        NetworkManager.getInstance().addToQueue(request);
//        NetworkManager.getInstance().addErrorListener((e) -> e.consume());/*{
//            showException("Conn Err ");
//            //e.consume();
//            if (!taskTag.equals("getCand")) {
//                d.dispose();
//                showException("Connection failed");
//            } else {
//                respH.getResp("Connection failed", taskTag);
//            }
//        });*/
    }

    /* 
    Synchronous - updating UI on network error   
     */
    public void processNetTkn(RespHandler respH, String userId, String reqData,
            final String taskTag) {

        userId = "USERID>" + userId;

        showReqResp("REQ>>" + userId);
        String encT = unPlainT.unPlain(userId);
        showReqResp("EncREQ>> " + encT);

        String authCode = "eriq:" + "123";

        String url;
        if (prod) {
            url = "https://cn.adv.ft.zomuh-tech.com/networking_p_tkn";
            //192.168.43.182
            //url = "http://192.168.43.182/AdvanceFt/networking_p_tkn";
        } else {
            //url = "http://127.0.0.1/AdvanceFt/networking_p_tkn";
            url = "http://127.0.0.1/AdvanceFt/networking_t_tkn";
        }

        //POST
        //ConnectionRequest request = new ConnectionRequest(url, true);
        ConnectionRequest request = new ConnectionRequest();
        request.setUrl(url);
        request.setHttpMethod("POST");

        //GET
        //ConnectionRequest request = new ConnectionRequest(url, false);
        request.addRequestHeader("Authorization", authCode);//add req header
        request.setTimeout(15000);
        request.setReadTimeout(20000);
        request.addArgument("reqData", encT);
        request.setFailSilently(true);

        NetworkManager.getInstance().addToQueueAndWait(request);

        showReqResp("\nRespCode1 " + request.getResponseCode());

        /*byte[] rs = request.getResponseData();
        String res = new String(rs);

        showReqResp("Resp<< " + res);*/
        switch (request.getResponseCode()) {

            case 0:
                //showReqResp("Connection failed");
                //ToastBar.showErrorMessage("Connection failed");
                respH.getResp("FAIL>Connection failed", taskTag);
                break;

            case 404:
                //showReqResp("Services Unavailable");
                //ToastBar.showErrorMessage("Services Unavailable");
                respH.getResp("FAIL>Services Unavailable", taskTag);
                break;

            case 200:

                byte[] result = request.getResponseData();
                String resp = new String(result).trim();
                showReqResp("EncResp<< " + resp);
                String decT = unPlainT.plainT(resp);
                showReqResp("Resp<< " + decT);

                String[] decTArr = proc.splitValue(decT, ">");

                switch (decTArr[0]) {
                    case "SUCCESS":
                        try {
                        JSONObject object = new JSONObject(decTArr[1]);
                        String token = object.getString("Token");
                        String reqUrl = object.getString("Url");
                        String skey = null, iv = null;
                        JSONArray array = new JSONArray(object.getString("Enc"));

                        for (int j = 0; j < array.length(); j++) {
                            JSONObject obj = array.getJSONObject(j);
                            skey = obj.getString("genSKey");
                            iv = obj.getString("genIV");
                        }

                        reqData = reqData + ">TOKEN>" + token;

                        switch (taskTag) {
                            case "rest":
                                processRestReq(respH, userId, reqData, taskTag,
                                        skey, iv, reqUrl);
                                break;
                            default:
                                processNetReq(respH, userId, reqData, taskTag,
                                        skey, iv, reqUrl);
                                break;
                        }

                    } catch (JSONException e) {
                        respH.getResp("invalid json", taskTag);
                    }
                    break;
                    case "FAIL":
                        respH.getResp(decTArr[1], taskTag);
                        break;
                }

                break;

            default:
                //ToastBar.showErrorMessage("Processing failed");
                byte[] rs = request.getResponseData();
                String res = new String(rs);
                showReqResp("Resp<< " + res);
                respH.getResp("FAIL>Processing failed", taskTag);
                break;
        }

    }

    /* 
    Synchronous - updating UI on network error   
     */
    public void processNetReq(RespHandler respH, String userId, String reqData,
            final String taskTag, String genSKey, String genIV,
            String reqUrl) {

        //temp store genSKey&IV
        proc.setGenSKey(genSKey);
        proc.setGenIV(genIV);

        showReqResp("UserID>>" + userId);
        String encUserId = unPlainT.unPlain(userId);
        showReqResp("EncUserID>> " + encUserId);

        showReqResp("REQ>>" + reqData);
        String encT = unPlainT.unPlainNet(reqData, genSKey, genIV);
        showReqResp("EncREQ>> " + encT);

        String authCode = "eriq:" + "123";

        /*String url;
        if (prod) {
            //request.setUrl("https://cn.adv.ft.zomuh-tech.com/proc_img");
            //url = "https://cn.adv.ft.zomuh-tech.com/networking_p";
            //192.168.43.182
            url = "http://192.168.43.182/AdvanceFt/networking_p";
        } else {
            //request.setUrl("http://127.0.0.1/AdvanceFt/proc_img");
            //url = "http://127.0.0.1/AdvanceFt/networking_p";
            url = "http://127.0.0.1/AdvanceFt/networking_t";
        }*/
        //POST
        //ConnectionRequest request = new ConnectionRequest(url, true);
        ConnectionRequest request = new ConnectionRequest();
        request.setUrl(reqUrl);
        request.setHttpMethod("POST");
        //GET
        //ConnectionRequest request = new ConnectionRequest(reqUrl, false);
        request.addRequestHeader("Authorization", authCode);//add req header
        request.setTimeout(15000);
        request.setReadTimeout(20000);
        request.addArgument("userId", encUserId);
        request.addArgument("reqData", encT);
        request.setFailSilently(true);

        NetworkManager.getInstance().addToQueueAndWait(request);

        showReqResp("\nRespCode1 " + request.getResponseCode());

        switch (request.getResponseCode()) {

            case 0:
                //showReqResp("Connection failed");
                //ToastBar.showErrorMessage("Connection failed");

                respH.getResp("FAIL>Connection failed", taskTag);
                break;

            case 404:
                //showReqResp("Services Unavailable");
                //ToastBar.showErrorMessage("Services Unavailable");
                respH.getResp("FAIL>Services Unavailable", taskTag);
                break;

            case 200:

                byte[] result = request.getResponseData();
                String resp = new String(result).trim();
                showReqResp("EncResp<< " + resp);

                if (resp.trim().equals("")) {
                    respH.getResp("FAIL>no response, please check input and "
                            + "retry", taskTag);
                } else {

                    String decT;
                    try {
                        //decrypt with generated skey&iv
                        decT = unPlainT.plainNetT(resp, proc.getGenSKey(),
                                proc.getGenIV());
                        //keys mismatch
                        if (decT.equals("decryp failed")) {
                            decryptWithStatic(respH, resp, taskTag);
                        } else {
                            showReqResp("Resp<< " + decT);
                            //clear gen skey&iv
                            proc.setGenSKey("");
                            proc.setGenIV("");
                            respH.getResp(decT, taskTag);

                        }

                    } catch (Exception e) {
                        decryptWithStatic(respH, resp, taskTag);
                    }
                }

                break;

            default:
                //ToastBar.showErrorMessage("Processing failed");
                byte[] rs = request.getResponseData();
                String res = new String(rs);
                showReqResp("Resp<< " + res);
                respH.getResp("FAIL>Processing failed", taskTag);
                break;
        }

    }

    /* 
    Synchronous - updating UI on network error   
     */
    public void processRestReq(RespHandler respH, String userId, String reqData,
            final String taskTag, String genSKey, String genIV,
            String reqUrl) {

        //temp store genSKey&IV
        proc.setGenSKey(genSKey);
        proc.setGenIV(genIV);

        showReqResp("UserID>>" + userId);
        String encUserId = unPlainT.unPlain(userId);
        showReqResp("EncUserID>> " + encUserId);

        showReqResp("REQ>>" + reqData);
        String encT = unPlainT.unPlainNet(reqData, genSKey, genIV);
        showReqResp("EncREQ>> " + encT);

        String authCode = "eriq:" + "123";

        //Map<String, Object> jsonData = Rest.post(reqUrl).body(reqData).getAsJsonMap();
        //POST REQUEST
        Response<Map> response = Rest.post(reqUrl)
                .queryParam("userId", encUserId)
                .queryParam("reqData", encT)
                .getAsJsonMap();

        //GET REQUEST     
        /*Response<Map> response = Rest.get(reqUrl)
                .queryParam("userId", encUserId)
                .queryParam("reqData", encT)
                .getAsJsonMap();*/
        showReqResp("\nRespCode1 " + response.getResponseCode());

        switch (response.getResponseCode()) {

            case 0:
                respH.getResp("FAIL>Connection failed", taskTag);
                break;

            case 404:
                respH.getResp("FAIL>Services Unavailable", taskTag);
                break;

            case 200:
                //Accepts non-encrypted json object data, otherwise response
                //is null

                Map result = response.getResponseData();
                String resp = "" + result;
                showReqResp("Map<< " + resp);
                //{Status=SUCCESS, Phone=078181811, Name=huj}

                if (resp.equals("{}")) {
                    respH.getResp("FAIL>no response, please check input and "
                            + "retry", taskTag);
                } else {
                    String status = "" + result.get("Status");
                    String phone = "" + result.get("Phone");
                    String name = "" + result.get("Name");
                    String res = status + ">Phone: " + phone + "\nName: " + name;
                    respH.getResp(res, taskTag);
                }

                break;

            default:
                respH.getResp("FAIL>Processing failed", taskTag);
                break;
        }

    }

    private void decryptWithStatic(RespHandler respH, String resp, String taskTag) {
        try {
            //attempt decrypt with static skey&iv incase gen skey, 
            //iv & tokens are not found in the server
            String decT = unPlainT.plainT(resp);
            showReqResp("Resp 2<< " + decT);
            respH.getResp(decT, taskTag);

        } catch (Exception err) {

            respH.getResp("secure keys compromised, please retry on "
                    + "secure network ", taskTag);
        }
    }

    public interface RespHandler {
        void getResp(String resp, String taskTag);
    }

}
