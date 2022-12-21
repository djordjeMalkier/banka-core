package common.bankarskiSistem.exceptions;

public class EntityAlreadyExistsException extends Exception{
    public EntityAlreadyExistsException(String errMessage) {
        super(errMessage);
    }
}
