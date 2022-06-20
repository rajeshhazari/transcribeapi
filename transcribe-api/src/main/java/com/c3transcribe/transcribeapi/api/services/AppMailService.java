package com.c3transcribe.transcribeapi.api.services;


import com.c3transcribe.transcribeapi.api.domian.RegUserVerifyLogDetails;
import com.c3transcribe.transcribeapi.api.services.exceptions.EmailNotFoundException;

import javax.mail.MessagingException;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

/**
 * E-mail sending service.
 */
public interface AppMailService {
    
    
    public boolean sendEmailForUserRegistration(String email) throws MessagingException;
    
    /**
     * This method sends an e-mail with an email change token.
     *  @param email E-mail address of the recipient
     * @param token E-mail change token
     * @return
     */
    RegUserVerifyLogDetails sendMailWhenEmailChangeReqestedWithToken(
            String orgEmail, String newEmail,
             final String token
    ) throws MessagingException, EmailNotFoundException;
    
    /**
     * This method sends an e-mail with an email change token.
     *  @param email E-mail address of the recipient
     * @return
     */
    RegUserVerifyLogDetails sendMailWithEmailChangeWithOutToken(
            String orgEmail, String newEmail) throws MessagingException, EmailNotFoundException;
    
    /**
     *
     * @param regUser
     * @param email
     * @param token
     * @return
     */
    RegUserVerifyLogDetails sendMailWithEmailChangeToken(
            RegUserVerifyLogDetails regUser,
            @NotBlank @Email String email,
            @NotBlank String token
    ) throws MessagingException;
    RegUserVerifyLogDetails sendMailWithEmailChangeToken(
            RegUserVerifyLogDetails regUser,
            @NotBlank @Email String email) throws EmailNotFoundException, MessagingException;
    /**
     * This method sends an e-mail with a new password.
     *
     * @param email E-mail address of the recipient
     * @param newPassword A new password
     */
    void sendMailWithNewPassword( @NotBlank @Email final String email, final String newPassword );
    
    /**
     * This method sends an e-mail with registered username aka email.
     *
     * @param email E-mail address of the recipient
     * @param username The user's name
     */
    void sendMailWithUsername(
            @NotBlank @Email final String email,
            @NotBlank final String username
    );
    
    public boolean  sendConfEmailForEmailChange(final String orgEmail, String newEmail) throws MessagingException, EmailNotFoundException ;
}
