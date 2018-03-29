package org.zerhusen.customexceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@SuppressWarnings("serial")
@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "User with that username already exists.")
public class UsernameAlreadyExistsException extends RuntimeException {

}
