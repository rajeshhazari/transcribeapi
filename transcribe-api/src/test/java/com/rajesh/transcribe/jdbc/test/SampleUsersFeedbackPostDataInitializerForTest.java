package com.rajesh.transcribe.jdbc.test;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.data.jdbc.core.JdbcAggregateTemplate;
import org.springframework.data.jdbc.core.mapping.AggregateReference;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;
import org.springframework.transaction.support.TransactionTemplate;

@Component
@Slf4j
@RequiredArgsConstructor
@Profile("test")
public class SampleUsersFeedbackPostDataInitializerForTest {
    
    
 
        private final JdbcAggregateTemplate template;
        private final TransactionTemplate tx;
        
        @EventListener(value = ContextRefreshedEvent.class)
        public void init() throws Exception {
            log.info("start sample UsersFeedbackPostData data initialization...");
            tx.executeWithoutResult(status -> {
                //this.template.deleteAll(Label.class);
                //this.template.deleteAll(Post.class);
                this.template.deleteAll(User.class);
                
                var user = User.builder().username("sampleTest@example.com")
                        .password("samplePassword");
                var savedUser = this.template.save(user);
                
                var post = Post.builder()
                        .title("Getting Started with Spring Data Jdbc")
                        .content("The content of Getting Started with Spring Data Jdbc")
                        .moderator(AggregateReference.to(savedUser))
                        .build();
                post.addLabel("Spring");
                post.addLabel("Spring Data Jdbc");
                
                var savedPost = this.template.save(post);
                
                var foundPost = this.template.findById(savedPost.getId(), Post.class);
                log.debug("found post by id: {}", foundPost);
            });
        }
    
}
