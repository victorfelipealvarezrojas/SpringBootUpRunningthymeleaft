package com.bookspring.mvcjsptemplatethymeleaf;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.reactive.function.client.WebClient;

@RequiredArgsConstructor
@Controller
public class PositionController {

    @NonNull
    private final AircraftRepository repository;
    private WebClient client = WebClient.create("http://localhost:7634/aircraft");

    //Model bean que es aprovechado por el motor de plantilla para proporcionar acceso a la los componentes de la app
    @GetMapping("/aircraft")
    public String getCurrentAircraftPositions(Model model) {
        repository.deleteAll();
        client.get()
                .retrieve()
                .bodyToFlux(Aircraft.class)
                .filter(plane -> !plane.getReg().isEmpty())
                .toStream()
                .forEach(repository::save);
        model.addAttribute("currentPositions", repository.findAll());

        //retorno del método. tipo de cadena en lugar de un tipo de clase y el nombre de una plantilla (extensión .html)
        return "positions";
    }
}
