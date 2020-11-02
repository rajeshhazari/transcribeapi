package com.rajesh.transcribe.utils;

import com.c3transcribe.transcribeapi.api.domian.AppUsers;
import com.c3transcribe.transcribeapi.api.models.dto.AuthUserProfileDto;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeansException;

import java.lang.reflect.InvocationTargetException;
import java.time.LocalDateTime;

public class SpringBeanUtilsTest {
    
        
        
        @Test
        public void testBeanUtils() throws BeansException, InvocationTargetException, IllegalAccessException {
            
            AppUsers appUsers = new AppUsers(8L,"rajeshh","rajesh","hazari","rajesh_hazari@yahoo.com",null,true,false,true,false,"27560",
                    "abcd", LocalDateTime.parse("2020-10-12T06:22:03.048331"),LocalDateTime.parse("2020-10-12T06:22:03.048331"),null);
            AuthUserProfileDto authUserProfileDto = new AuthUserProfileDto();
            
            BeanUtils.copyProperties(appUsers,authUserProfileDto);
            //org.apache.commons.beanutils.BeanUtils.copyProperties(authUserProfileDto, appUsers);
    
            Assert.assertEquals(appUsers,authUserProfileDto);
    
            Assert.assertTrue(appUsers.getEmail().equalsIgnoreCase(authUserProfileDto.getEmail()));
            
            
            
        }
        
       
        
}
