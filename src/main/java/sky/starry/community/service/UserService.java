package sky.starry.community.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sky.starry.community.mapper.UserExtMapper;
import sky.starry.community.model.User;

@Service
public class UserService {

    @Autowired
    private UserExtMapper userExtMapper;

    public void createOrUpdata(User user) {

        User dbUser = userExtMapper.findByAccountID(user.getAccountId());

        if(dbUser == null){
            user.setGmtCreate(System.currentTimeMillis());
            user.setGmtModified(user.getGmtCreate());
            userExtMapper.insert(user);
        }else {
            dbUser.setGmtModified(System.currentTimeMillis());
            dbUser.setToken(user.getToken());
            dbUser.setName(user.getName());
            dbUser.setAvatarUrl(user.getAvatarUrl());
            userExtMapper.update(dbUser);
        }
    }
}
