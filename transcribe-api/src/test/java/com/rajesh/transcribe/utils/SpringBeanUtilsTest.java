package com.rajesh.transcribe.utils;

import com.rajesh.transcribe.transribeapi.api.domian.AppUsers;
import com.rajesh.transcribe.transribeapi.api.domian.AppUsersAuth;
import com.rajesh.transcribe.transribeapi.api.models.dto.AuthUserProfileDto;
import org.apache.log4j.Logger;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.FatalBeanException;

import java.lang.reflect.InvocationTargetException;
import java.time.LocalDateTime;
import java.util.HashSet;

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
