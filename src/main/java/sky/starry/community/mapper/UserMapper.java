package sky.starry.community.mapper;


import org.apache.ibatis.annotations.*;
import sky.starry.community.model.User;

@Mapper
public interface UserMapper{

    @Insert("insert into user(name,account_id,token,gmt_create,gmt_modified,avatar_url) values(#{name},#{accountId},#{token},#{gmtCreate},#{gmtModified},#{avatarUrl})")
    void insert(User user);

    @Select("select * from user where token = #{token}")
    User findByToken(@Param("token") String token);

    @Select("select * from user where id = #{id}")
    User findByID(Integer id);

    @Select("select * from user where account_id = #{ccountId}")
    User findByAccountID(String accountId);

    @Update("updata user set name=#{name},token=#{token},gmt_modified=#{gmtModified},avatar_url=#{avatarUrl} where account_id = #{accountId}")
    void updata(User user);
}