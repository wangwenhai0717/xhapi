package com.xunhao.project;

import com.xunhao.xhapiclientsdk.utils.SignUtil;
import com.xunhao.xhapicommon.model.entity.InterfaceInfo;
import com.xunhao.xhapicommon.model.entity.User;
import com.xunhao.xhapicommon.service.InnerInterfaceInfoService;
import com.xunhao.xhapicommon.service.InnerUserInterfaceInfoService;
import com.xunhao.xhapicommon.service.InnerUserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.reactivestreams.Publisher;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.core.io.buffer.DefaultDataBufferFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.http.server.reactive.ServerHttpResponseDecorator;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 全局过滤器
 */
@Slf4j
@Component
public class CustomGlobalFilter implements GlobalFilter, Ordered {

    @DubboReference
    private InnerUserInterfaceInfoService interfaceInfoService;

    @DubboReference
    private InnerInterfaceInfoService infoService;

    @DubboReference
    private InnerUserService userService;

    private static final List<String> IP_WHITE_LIST = Arrays.asList("127.0.0.1");
    private static final Long FIVE_MINUTES = 60 * 5L;
    private static final String INTERFACE_HOST = "http://106.54.193.109:8111";

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

        // 1.请求日志
        ServerHttpRequest request = exchange.getRequest();
        String path = INTERFACE_HOST + request.getPath().value();
        String method = request.getMethod().toString();
        log.info("请求唯一标识：" + request.getId());
        log.info("请求路径：" + path);
        log.info("请求方法：" + request.getMethod());
        log.info("请求参数：" + request.getQueryParams());
        String sourceAddress = request.getLocalAddress().getHostString();
        log.info("请求远程地址：" + request.getRemoteAddress());
        log.info("网关本地地址：" + sourceAddress);
        //log.info("接口请求ip：" + getIp());

        // 拿到响应对象
        ServerHttpResponse response = exchange.getResponse();
        // 2.(黑白名单)
        if (!IP_WHITE_LIST.contains(sourceAddress)) {
            return handleNoAuth(response);
        }
        // 3.用户鉴权（判断ak、sk是否合法）
        // 从请求头中获取参数
        HttpHeaders headers = request.getHeaders();
        String accessKey = headers.getFirst("accessKey");
        String nonce = headers.getFirst("nonce");
        String timestamp = headers.getFirst("timestamp");
        String sign = headers.getFirst("sign");
        String body = headers.getFirst("body");

        // 先到数据库查是否已分配给用户
        User invokeUser = null;
        try {
            // 调用内部服务，根据访问密钥获取用户信息
            invokeUser = userService.getInvokeUser(accessKey);
        } catch (Exception e) {
            // 捕获异常，打印日志
            log.error("getInvokeUser error");
        }
        if (invokeUser == null) {
            return handleNoAuth(response);
        }
        String secretKey = invokeUser.getSecretKey();
        String serverSign = SignUtil.getSign(body, secretKey);
        if (sign == null || !serverSign.equals(serverSign)) {
            return handleNoAuth(response);
        }
        if (Long.parseLong(nonce) > 10000L) {
            return handleNoAuth(response);
        }
        // 当前时间不超过5分钟
        // 获取当前时间戳，以秒为单位（System.currentTimeMillis()）获取的是毫秒数，/1000获取秒数
        Long currentTime = System.currentTimeMillis() / 1000;
        if ((currentTime - Long.parseLong(timestamp) >= FIVE_MINUTES)) {
            return handleNoAuth(response);
        }
//        if (Long.parseLong(nonce) > 10000) {
//            throw new RuntimeException("无权限");
//        }
        //要从数据库去查secretKey
        if (!sign.equals(serverSign)) {
            return handleNoAuth(response);
        }
        // 4.请求的模拟接口是否存在
        InterfaceInfo interfaceInfo = null;
        try {
            interfaceInfo = infoService.getInterfaceInfo(path, method);
        } catch (Exception e) {
            log.error("getInterfaceInfo error", e);
        }
        if (interfaceInfo == null) {
            return handleNoAuth(response);
        }
        //判断是否还有调用次数
        boolean leftCount = userService.getLeftNum(invokeUser.getId());
        if (!leftCount) {
            return handleNoAuth(response);
        }
        // 响应日志
        log.info("响应" + response.getStatusCode());
        // 5.请求转发，调用模拟接口
        return handleResponse(exchange, chain, interfaceInfo.getId(), invokeUser.getId());
    }

    @Override
    public int getOrder() {
        return -1;
    }

    public Mono<Void> handleNoAuth(ServerHttpResponse response) {
        // 设置响应状态码403(FORBIDDEN-禁止访问)
        response.setStatusCode(HttpStatus.FORBIDDEN);
        // 返回处理完成的响应
        return response.setComplete();
    }

    public Mono<Void> handleResponse(ServerWebExchange exchange, GatewayFilterChain chain, long interfaceInfoId, long userId) {
        try {
            // 获取原始响应对象
            ServerHttpResponse originalResponse = exchange.getResponse();
            // 获取数据缓冲工厂
            DataBufferFactory bufferFactory = originalResponse.bufferFactory();
            // 获取响应的状态码
            HttpStatus statusCode = originalResponse.getStatusCode();
            // 判断响应状态码
            if (statusCode != HttpStatus.OK) {
                return chain.filter(exchange);//降级处理返回数据
            }
            // 创建一个装饰后的响应对象（穿装备，增强能力）
            ServerHttpResponseDecorator decoratedResponse = new ServerHttpResponseDecorator(originalResponse) {
                // 重写writeWith方法，用于处理响应体的数据
                // 这段方法就是只要当我们的模拟接口调用完成之后，等它返回结果
                // 就会调用这个writeWith方法，我们就能根据响应结果做一些自己的处理
                @Override
                public Mono<Void> writeWith(Publisher<? extends DataBuffer> body) {
                    if (body instanceof Flux) {
                        Flux<? extends DataBuffer> fluxBody = Flux.from(body);
                        // 返回一个处理后的响应体
                        // （这里就理解为它在拼接字符串，它把缓冲区的数据取出来，一点一点拼接好）
                        return super.writeWith(
                                fluxBody.buffer().map(dataBuffers -> {
                                    // 7.调用成功次数+1
                                    try {
                                        interfaceInfoService.invokeCount(interfaceInfoId, userId);
                                    } catch (Exception e) {
                                        log.error("invokeCount error", e);
                                    }
                                    // 合并多个流集合，解决返回体分段传输
                                    DataBufferFactory dataBufferFactory = new DefaultDataBufferFactory();
                                    DataBuffer buff = dataBufferFactory.join(dataBuffers);
                                    byte[] content = new byte[buff.readableByteCount()];
                                    buff.read(content);
                                    DataBufferUtils.release(buff);//释放掉内存

                                    // 构建返回日志
                                    StringBuilder sb2 = new StringBuilder(200);
                                    List<Object> rspArgs = new ArrayList<>();
                                    rspArgs.add(originalResponse.getStatusCode());
                                    String data = new String(content, StandardCharsets.UTF_8);
                                    sb2.append(data);
                                    log.info("响应结果:" + data);
                                    return bufferFactory.wrap(content);
                                }));
                    } else {
                        // 8.调用失败，返回规范错误码
                        log.error("<-- {} 响应异常", getStatusCode());
                    }
                    return super.writeWith(body);
                }
            };
            return chain.filter(exchange.mutate().response(decoratedResponse).build());

        } catch (Exception e) {
            log.error("网关处理异常" + e);
            return chain.filter(exchange);
        }
    }
}
