import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.function.Consumer;

public class MonoFluxDemo {

    @Test
    public  void testMono() {
        Mono<String> stringMono = Mono.just("java guides")
                        .log();
        stringMono.subscribe(System.out::println);
    }

    @Test
    public void testMonoError() {
        Mono<?> stringMono = Mono.just("java guides")
                .then(Mono.error(new RuntimeException("Exception occurred while emitting the data")))
                .log();
        stringMono.subscribe(System.out::println);
    }

    @Test
    public void testFlux() {
        Flux<String> stringFlux = Flux.just("Apple", "Banana", "Orange", "Mango")
                        .log();
        stringFlux.subscribe((element) -> {
            System.out.println(element);
        });
    }
}
