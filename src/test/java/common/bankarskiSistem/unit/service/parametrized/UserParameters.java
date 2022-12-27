package common.bankarskiSistem.unit.service.parametrized;

import common.bankarskiSistem.model.User;
import org.junit.jupiter.params.provider.Arguments;

import java.util.stream.Stream;

public class UserParameters {

    public static Stream<Arguments> generateUpdateUser(){
        User user1 = new User(
                "1111111111111",
                "Prvi",
                " korisnik",
                "Adressa1"

        );

        User user2 = new User(
                "1111111111111",
                "Drugi",
                " korisnik",
                "Adressa2"

        );

        return Stream.of(Arguments.of(user1, user2));
    }
}
