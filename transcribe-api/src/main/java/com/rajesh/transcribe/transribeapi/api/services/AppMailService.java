package com.rajesh.transcribe.transribeapi.api.services;


import com.rajesh.transcribe.transribeapi.api.domian.RegisteredUserVerifyLogDetials;

import javax.mail.MessagingException;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

/**
 * E-mail sending service.
 */
public interface AppMailService {
    
    
    /**
     * This method sends an e-mail with an email change token.
     *  @param email E-mail address of the recipient
     * @param token E-mail change token
     * @return
     */
    RegisteredUserVerifyLogDetials sendMailWithEmailChangeToken(
            @NotBlank @Email final String email,
            @NotBlank final String token
    ) throws MessagingException;
    
    /**
     * This method sends an e-mail with an email change token.
     *  @param email E-mail address of the recipient
     * @return
     */
    RegisteredUserVerifyLogDetials sendMailWithEmailChangeToken(
            @NotBlank @Email final String email) throws MessagingException;
    
    /**
     *
     * @param regUser
     * @param email
     * @param token
     * @return
     */
    RegisteredUserVerifyLogDetials sendMailWithEmailChangeToken(RegisteredUserVerifyLogDetials regUser,
                                                                @NotBlank @Email String email,
                                                                @NotBlank String token
    );
    
    RegisteredUserVerifyLogDetials sendMailWithEmailChangeToken(RegisteredUserVerifyLogDetials regUser,
                                                                @NotBlank @Email String email);
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
    
    public boolean  sendConfEmail(String email) throws MessagingException;
}
