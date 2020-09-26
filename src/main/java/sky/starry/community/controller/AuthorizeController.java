package sky.starry.community.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import sky.starry.community.dto.AccessTokenDTO;
import sky.starry.community.dto.GithubUser;
import sky.starry.community.model.User;
import sky.starry.community.pojo.GithubProvider;
import sky.starry.community.service.UserService;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

@Controller
public class AuthorizeController {
    @Autowired
    private GithubProvider githubProvider;
    @Autowired
    private UserService userService;

    @Value("${github.client.id}")
    String clientId;
    @Value("${github.client.secret}")
    String clientSecret;
    @Value("${github.redirect.uri}")
    String recdirectUri;

    @GetMapping("/callback")
    public String callback(@RequestParam(name = "code") String code,
                           @RequestParam(name="state") String state,
                           HttpServletRequest request,
                           HttpServletResponse response
                           ) throws Exception {
        //请求git身份，访问特定网址获取code和state（https://github.com/login/oauth/authorize?client_id=4bc98ff7ae188a5f5470&redirect_uri=http://localhost:8080/callback&scope=user&state=1） GET

        AccessTokenDTO accessTokenDTO = new AccessTokenDTO();
        accessTokenDTO.setCode(code);
        accessTokenDTO.setClient_id(clientId);
        accessTokenDTO.setClient_secret(clientSecret);
        accessTokenDTO.setRedirect_uri(recdirectUri);
        accessTokenDTO.setState(state);

        //client_id	string	需要。您从GitHub收到的GitHub App的客户端ID。
        //client_secret	string	需要。您从GitHub收到的GitHub App的客户密码。
        //code	string	需要。您收到的作为对步骤1的响应的代码。
        //redirect_uri	string	授权后将用户发送到应用程序中的URL。
        //state	string	您在步骤1中提供的无法猜测的随机字符串。

        //以code为令牌，及各种参量获取匹配网址获取参量access_token POST
        String accessToken = githubProvider.getAccessToken(accessTokenDTO);
        //以token参量获取用户信息 GET
        GithubUser githubUser = githubProvider.getGithubUser(accessToken);
        

        if(githubUser != null && githubUser.getId()!=null){

            User user = new User();
            user.setToken(UUID.randomUUID().toString());
            user.setName(githubUser.getName());
            user.setAccountId(String.valueOf(githubUser.getId()));
            user.setGmtCreate(System.currentTimeMillis());
            user.setGmtModified(user.getGmtCreate());
            user.setAvatarUrl(githubUser.getAvatarUrl());
            userService.createOrUpdata(user);

            //登陆成功，写cookse和session
            //request.getSession().setAttribute("user",githubUser);

            response.addCookie(new Cookie("token",user.getToken()));

            return "redirect:/";
        }else {
            //登陆失败，重新登陆
            return "redirect:/";
        }
    }

    @GetMapping("/loginout")
    private String loginout(HttpServletRequest request,
                            HttpServletResponse response){

        request.getSession().removeAttribute("user");
        Cookie cookie = new Cookie("token",null);
        cookie.setMaxAge(0);
        response.addCookie(cookie);

        return "redirect:/";
    }
}
