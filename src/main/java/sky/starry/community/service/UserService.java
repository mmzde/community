package sky.starry.community.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sky.starry.community.mapper.UserMapper;
import sky.starry.community.model.User;

@Service
public class UserService {

    @Autowired
    private UserMapper userMapper;

    public void createOrUpdata(User user) {

        User dbUser = userMapper.findByAccountID(user.getAccountId());

        if(dbUser == null){
            user.setGmtCreate(System.currentTimeMillis());
            user.setGmtModified(user.getGmtCreate());
            userMapper.insert(user);
        }else {
            dbUser.setGmtModified(System.currentTimeMillis());
            dbUser.setToken(user.getToken());
            dbUser.setName(user.getName());
            dbUser.setAvatarUrl(user.getAvatarUrl());
            userMapper.update(dbUser);
        }
    }
}
