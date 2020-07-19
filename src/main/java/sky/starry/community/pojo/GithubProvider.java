package sky.starry.community.pojo;

import okhttp3.*;
import org.springframework.stereotype.Component;
import sky.starry.community.dto.AccessTokenDTO;
import com.alibaba.fastjson.*;
import sky.starry.community.dto.GithubUser;

import java.io.IOException;
import java.sql.SQLOutput;

@Component
public class GithubProvider {
    public String getAccessToken(AccessTokenDTO accessTokenDTO){                   //获取
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
    public GithubUser getGithubUser(String accessToken){
        OkHttpClient client = new OkHttpClient();
        System.out.println(accessToken);
        Request request = new Request.Builder()
                .url("https://api.github.com/user?access_token=" + accessToken)
                .build();
        try {
            Response response = client.newCall(request).execute();
            String string = response.body().string();
            GithubUser githubUser = JSON.parseObject(string,GithubUser.class);
            return githubUser;
        }catch (IOException e){
            e.printStackTrace();
        }
        return null;
    }
}
