package com.geekerstar.gateway.controller;

import com.geekerstar.gateway.config.GatewayProperties;
import com.geekerstar.gateway.exception.AuthException;
import com.geekerstar.gateway.util.RequestResponseUtil;
import com.geekerstar.gateway.util.http.HttpClientUtil;
import com.geekerstar.gateway.util.http.builder.HCB;
import com.geekerstar.gateway.util.http.common.HttpConfig;
import com.geekerstar.gateway.util.http.common.HttpHeader;
import com.geekerstar.gateway.util.http.common.HttpMethods;
import com.geekerstar.gateway.util.http.common.Utils;
import com.google.common.collect.Maps;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Map;

import static com.geekerstar.gateway.constant.CommonConstant.*;

/**
 * @author geekerstar
 * @date 2020/4/15 09:17
 * @description
 */
@Slf4j
@RestController
@RequestMapping("/router")
@Api(tags = "路由转发")
@RequiredArgsConstructor
public class RouterController {

    private final GatewayProperties gatewayProperties;

    @GetMapping(value = "/**")
    public void routeGet(HttpServletRequest req, HttpServletResponse resp) {
        try {
            String uri = req.getRequestURI();
            String[] s = uri.split(DEFAULT_PATH_SEPARATOR);
            String serverPath;
            if (URL_PREFIX.equals(s[1])) {
                try {
                    serverPath = gatewayProperties.getServer().get(s[2]);
                } catch (Exception e) {
                    throw AuthException.ROUTER_ERROR;
                }
            } else {
                throw AuthException.ROUTER_ERROR;
            }
            String url = HTTP_PREFIX + serverPath + uri;
            URIBuilder ub = new URIBuilder(url);
            Map<String, String> map = RequestResponseUtil.getRequestParameters(req);
            for (String key : map.keySet()) {
                ub.setParameter(key, map.get(key));
            }
            url = ub.build().toString();
            //插件式配置生成HttpClient时所需参数（超时、连接池、ssl、重试）
            HCB hcb = HCB.custom()
                    //启用连接池，每个路由最大创建10个链接，总连接数限制为100个
                    .pool(100, 10)
                    .retry(5);
            CloseableHttpClient client = hcb.build();
            //转接Header，插件式配置Header（各种header信息、自定义header）
            Map<String, String> hdmp = RequestResponseUtil.getRequestHeaders(req);
            HttpHeader header = HttpHeader.custom();
            for (Map.Entry<String, String> e : hdmp.entrySet()) {
                String name = e.getKey();
                String paramValue = e.getValue();
                header.other(name, paramValue);
            }
            Header[] headers = header.build();
            //插件式配置请求参数（网址、请求参数、编码、client）
            HttpConfig config = HttpConfig.custom()
                    .headers(headers)
                    .timeout(180000)
                    .url(url)
                    .encoding(StandardCharsets.UTF_8.toString())
                    .client(client);
            //设置get请求
            config.method(HttpMethods.GET);
            //请求回内容
            HttpResponse rp = HttpClientUtil.execute(config);
            InputStream instream = rp.getEntity().getContent();
            String contentType = rp.getEntity().getContentType().getValue();
            if (contentType.contains("force-download")) {
                resp.setContentType(contentType);
                Header disposition = rp.getFirstHeader("Content-Disposition");
                resp.addHeader(disposition.getName(), disposition.getValue());
            } else {
                resp.setContentType("text/html;charset=UTF-8");
            }
            try {
                byte[] tmp = new byte[4096];
                OutputStream outstream = resp.getOutputStream();
                int l;
                while ((l = instream.read(tmp)) != -1) {
                    outstream.write(tmp, 0, l);
                }
            } finally {
                instream.close();
            }
            resp.flushBuffer();
        } catch (Exception e) {
            try {
                String data = e.getMessage();
                OutputStream outputStream = resp.getOutputStream();
                resp.setContentType("text/html;charset=UTF-8");
                byte[] dataByteArr = data.getBytes(StandardCharsets.UTF_8);
                outputStream.write(dataByteArr);
                resp.flushBuffer();
            } catch (Exception ignored) {
            }
        }
    }

    @RequestMapping(value = "/**", method = RequestMethod.POST)
    public void routePost(HttpServletRequest req, HttpServletResponse resp) {

        try {
            String uri = req.getRequestURI();
            String[] s = uri.split(DEFAULT_PATH_SEPARATOR);
            String serverPath;
            if (URL_PREFIX.equals(s[1])) {
                try {
                    serverPath = gatewayProperties.getServer().get(s[2]);
                } catch (Exception e) {
                    throw AuthException.ROUTER_ERROR;
                }
            } else {
                throw AuthException.ROUTER_ERROR;
            }
            String url = HTTP_PREFIX + serverPath + uri;
            //插件式配置生成HttpClient时所需参数（超时、连接池、ssl、重试）
            HCB hcb = HCB.custom()
                    //启用连接池，每个路由最大创建10个链接，总连接数限制为100个
                    .pool(100, 10)
                    .retry(5);
            CloseableHttpClient client = hcb.build();
            //转接Header，插件式配置Header（各种header信息、自定义header）
            Map<String, String> hdmp = RequestResponseUtil.getRequestHeaders(req);
            HttpHeader header = HttpHeader.custom();
            for (Map.Entry<String, String> e : hdmp.entrySet()) {
                String name = e.getKey();
                String paramValue = e.getValue();
                header.other(name, paramValue);
            }
            Header[] headers = header.build();
            HttpConfig config = HttpConfig.custom();
            //插件式配置请求参数（网址、请求参数、编码、client）
            config.headers(headers)
                    .timeout(180000)
                    .url(url)
                    .encoding(StandardCharsets.UTF_8.toString())
                    .client(client);
            String params = IOUtils.toString(req.getInputStream(), StandardCharsets.UTF_8);

            if (req.getContentType() != null && req.getContentType().contains("application/json")) {
                // 传递的参数，json格式
                config.json(params);
            } else {
                Map<String, Object> map = Maps.newHashMap();
                //传递的参数，一般文本
                map.put(Utils.ENTITY_STRING, params);
                config.map(map);
            }
            config.method(HttpMethods.POST);
            //请求回内容
            HttpResponse rp = HttpClientUtil.execute(config);
            resp.setContentType("text/html;charset=UTF-8");
            InputStream instream = rp.getEntity().getContent();
            try {
                byte[] tmp = new byte[4096];
                OutputStream outstream = resp.getOutputStream();
                int l;
                while ((l = instream.read(tmp)) != -1) {
                    outstream.write(tmp, 0, l);
                }
            } finally {
                instream.close();
            }
            resp.flushBuffer();
        } catch (Exception e) {
            e.printStackTrace();
            try {
                String data = e.getMessage();
                //获取OutputStream输出流
                OutputStream outputStream = resp.getOutputStream();
                //通过设置响应头控制浏览器以UTF-8的编码显示数据，如果不加这句话，那么浏览器显示的将是乱码
                resp.setHeader("content-type", "text/html;charset=UTF-8");
                //将字符转换成字节数组，指定以UTF-8编码进行转换
                byte[] dataByteArr = data.getBytes(StandardCharsets.UTF_8);
                //使用OutputStream流向客户端输出字节数组
                outputStream.write(dataByteArr);
                resp.flushBuffer();  //这个要测试？？？
            } catch (Exception ignored) {
            }
        }
    }

//    @GetMapping(value = "/**")
//    public Response routerGet(HttpServletRequest request, HttpServletResponse resp) {
//        Map<String, String> requestBodyMap = RequestResponseUtil.getRequestBodyMap(request);
//        String uri = request.getRequestURI();
//        String[] s = uri.split(DEFAULT_PATH_SEPARATOR);
//        String serverPath;
//        if (URL_PREFIX.equals(s[1])) {
//            try {
//                serverPath = gatewayProperties.getServer().get(s[2]);
//            } catch (Exception e) {
//                throw AuthException.ROUTER_ERROR;
//            }
//        } else {
//            throw AuthException.ROUTER_ERROR;
//        }
//        String requestUri = HTTP_PREFIX + serverPath + uri;
//        StringBuffer buffer = new StringBuffer(requestUri);
//        if (requestBodyMap != null) {
//            Iterator iterator = requestBodyMap.entrySet().iterator();
//            if (iterator.hasNext()) {
//                buffer.append(REQUEST_SPLIT);
//                Object element;
//                while (iterator.hasNext()) {
//                    element = iterator.next();
//                    Map.Entry entry = (Map.Entry) element;
//                    //过滤value为null，value为null时进行拼接字符串会变成 "null"字符串
//                    if (entry.getValue() != null) {
//                        buffer.append(element).append(REQUEST_CONTACT);
//                    }
//                    requestUri = buffer.substring(0, buffer.length() - 1);
//                }
//            }
//        }
//        log.debug("网关将请求路由转发至:{}", requestUri);
//        return new RestTemplate().getForObject(requestUri, Response.class);
//    }
//
//    @PostMapping(value = "/**")
//    public Response routerPost(HttpServletRequest request, HttpServletResponse resp) {
//        Map<String, String> requestBodyMap = RequestResponseUtil.getRequestBodyMap(request);
//        String uri = request.getRequestURI();
//        String[] s = uri.split(DEFAULT_PATH_SEPARATOR);
//        String serverPath;
//        if (URL_PREFIX.equals(s[1])) {
//            try {
//                serverPath = gatewayProperties.getServer().get(s[2]);
//            } catch (Exception e) {
//                throw AuthException.ROUTER_ERROR;
//            }
//        } else {
//            throw AuthException.ROUTER_ERROR;
//        }
//        String requestUri = HTTP_PREFIX + serverPath + uri;
//        log.debug("网关将请求路由转发至:{}", requestUri);
//        return new RestTemplate().postForObject(requestUri, requestBodyMap, Response.class);
//    }
}
