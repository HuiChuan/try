package com.jd.dao;

import com.jd.entity.Hello;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface HelloMapper {
    @Select("select * from hello where name= #{name} limit 1")
    Hello selectHelloByName(@Param("name") String name);

    Integer insertHello(Hello hello);
}
