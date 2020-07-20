package sky.starry.community.pojo;

import okhttp3.*;
import org.apache.http.HttpEntity;
import org.apache.http.HttpVersion;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Component;
import sky.starry.community.dto.AccessTokenDTO;
import com.alibaba.fastjson.*;
import sky.starry.community.dto.GithubUser;

import java.io.IOException;
import java.sql.SQLOutput;

@Component
public class GithubProvider {
    public String getAccessToken(AccessTokenDTO accessTokenDTO) {                   //获取
        MediaType mediaType = MediaType.get("application/json; charset=utf-8");

        OkHttpClient client = new OkHttpClient();

        RequestBody body = RequestBody.create(mediaType, JSON.toJSONString(accessTokenDTO));
        Request request = new Request.Builder()
                .url("https://github.com/login/oauth/access_token")
                .post(body)
                .build();
        try (Response response = client.newCall(request).execute()) {
            String string = response.body().string();
            String token = string.split("&")[0].split("=")[1];
            return token;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public GithubUser getGithubUser(String accessToken) throws IOException {
    /*    int timeout = 60;
        RequestConfig defaultRequestConfig = RequestConfig.custom()
                .setSocketTimeout(timeout * 1000)
                .setConnectTimeout(timeout * 1000)
                .setConnectionRequestTimeout(timeout * 1000)
                .build();
        CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpGet httpget = new HttpGet("https://api.github.com/user");
        httpget.addHeader(new BasicHeader("Authorization", "token " + accessToken));
        httpget.setProtocolVersion(HttpVersion.HTTP_1_0);
        httpget.addHeader(HTTP.CONN_DIRECTIVE, HTTP.CONN_CLOSE);
        httpget.setConfig(defaultRequestConfig);
        CloseableHttpResponse response = null;
        try {
            response = httpclient.execute(httpget);
            System.out.println(response.getStatusLine());
            HttpEntity entity = response.getEntity();
            GithubUser user = JSON.parseObject(entity.getContent(), GithubUser.class);
            EntityUtils.consume(entity);
            response.close();
            return user;
        } catch (Exception e) {
            throw e;
        } finally {
            if (response != null) {
                response.close();
            }
            if (httpclient != null) {
                httpclient.close();
            }
        } */
        OkHttpClient okHttpClient = new OkHttpClient();

        Request request = new Request.Builder()
                .url("https://api.github.com/user?access_token="+accessToken)
                .build();

        try {
            Response response = okHttpClient.newCall(request).execute();

            String string = response.body().string();
            System.out.println(string);
            GithubUser githubUser = JSON.parseObject(string, GithubUser.class);

            return githubUser;
        }catch (Exception e){
            System.out.println(e);
        }
        return null;
    }
}
