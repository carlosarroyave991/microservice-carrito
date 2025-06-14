package com.arka.microservice.carrito.domain.service;

import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

import static com.arka.microservice.carrito.domain.config.Const.*;

@Component
public class RandomReferenceGenerator {

    public String Generate(){
        //Elegimos la longitud
        int length = ThreadLocalRandom.current().nextInt(MIN_LEN, MAX_LEN + 1);

        //Convierte la cadena de permitidos a lista, la baraja y toma los primeros 'length'
        List<Character> pool = ALLOWED.chars()
                .mapToObj(c -> (char)c)
                .collect(Collectors.toList());

        Collections.shuffle(pool);

        return pool.stream()
                .limit(length)
                .map(String::valueOf)
                .collect(Collectors.joining());
    }
}
