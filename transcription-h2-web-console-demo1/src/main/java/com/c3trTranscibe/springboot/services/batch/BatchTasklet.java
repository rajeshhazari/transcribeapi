package com.c3trTranscibe.springboot.services.batch;

import com.c3trTranscibe.springboot.services.TranscribitionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;

public class BatchTasklet  implements Tasklet {

    private final Logger logger = LoggerFactory.getLogger(BatchTasklet.class);

    @Autowired
    TranscribitionService tService;

    @Override
    public RepeatStatus execute(StepContribution stepContribution, ChunkContext chunkContext) throws Exception {
        logger.debug(" JobName: %s   JobId: %s ",chunkContext.getStepContext().getJobName(), chunkContext.getStepContext().getId());
         if(chunkContext.isComplete()){
             return  RepeatStatus.FINISHED;
         }
        return null;
    }
}
