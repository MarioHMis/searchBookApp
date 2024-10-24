package com.aluraproject.primerprojectospring.principal;


import com.aluraproject.primerprojectospring.Service.ConsumoApi;
import com.aluraproject.primerprojectospring.Service.ConvierteDatos;
import com.aluraproject.primerprojectospring.Service.Episodio;
import com.aluraproject.primerprojectospring.model.DatosEpisodio;
import com.aluraproject.primerprojectospring.model.DatosSerie;
import com.aluraproject.primerprojectospring.model.DatosTemporada;

import java.sql.SQLOutput;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Principal {
    private Scanner teclado = new Scanner(System.in);
    private ConsumoApi consumoApi = new ConsumoApi();
    private final String URL_BASE = "https://www.omdbapi.com/?t=";
    private final String API_KEY = "&apikey=ca6b437e";
    private ConvierteDatos conversor = new ConvierteDatos();

    public void muestraElMenu() {
        System.out.println("Escribe el nombre de la série que deseas buscar");
        //Busca los datos generales de las series
        var nombreSerie = teclado.nextLine();
        var json = consumoApi.obtenerDatos(URL_BASE + nombreSerie.replace(" ", "+") + API_KEY);
        //https://www.omdbapi.com/?t=game+of+thrones&apikey=4fc7c187
        DatosSerie datos = conversor.obetenerDatos(json, DatosSerie.class);
        System.out.println(datos);

        //Busca los datos de todas las temporadas
        List<DatosTemporada> temporadas = new ArrayList<>();

        for (int i = 1; i <= datos.totalTemporadas(); i++) {
            json = consumoApi.obtenerDatos(URL_BASE + nombreSerie.replace(" ", "+") + "&Season=" + i + API_KEY);
            DatosTemporada datosTemporada = conversor.obetenerDatos(json, DatosTemporada.class);
            temporadas.add(datosTemporada);
        }
        temporadas.forEach(System.out::println);

        //Mostrar solo el titulo de los episodios para las temporadas
        for (int i = 0; i < datos.totalTemporadas(); i++) {
            List<DatosEpisodio> episodiosTemporadas = temporadas.get(i).episodios();
            for (int j = 0; j < episodiosTemporadas.size(); j++) {
                System.out.println(episodiosTemporadas.get(j).titulo());
            }
        }
        // Mejoría usando funciones Lambda
//        temporadas.forEach(t -> t.episodios().forEach(e -> System.out.println(e.titulo())));

        List<DatosEpisodio> datosEpisodios = temporadas.stream()
                .flatMap(t -> t.episodios().stream())
                .collect(Collectors.toList());

        //Top 5 episodios
//        System.out.println("Top 5 episodios: ");
//        datosEpisodios.stream()
//                .filter(e -> !e.evaluacion().equalsIgnoreCase("N/A"))
//                .sorted(Comparator.comparing(DatosEpisodio::evaluacion).reversed())
//                .limit(5)
//                .forEach(System.out::println);

        //Convirtiendo los datos a una lista del tipo episodio
        List<Episodio> episodios = temporadas.stream()
                .flatMap(t -> t.episodios().stream()
                        .map(d -> new Episodio(t.numero(), d)))
                .collect(Collectors.toList());

        episodios.forEach(System.out::println);

        //Busqueda de episodios apartir de x año

//        System.out.println("Indica el año de los episodios: ");
//        var fecha = teclado.nextInt();
//        teclado.nextLine();
//
//        LocalDate fechaBusqueda = LocalDate.of(fecha, 1, 1);
//
//        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");
//
//        episodios.stream()
//                .filter(e -> e.getFechaDeLanzamiento() != null && e.getFechaDeLanzamiento().isAfter(fechaBusqueda))
//                .forEach(e -> System.out.println(
//                        "Temporada " + e.getTemporada() +
//                                "Episodio " + e.getTitulo() +
//                                "Fecha de lanzamiento " + e.getFechaDeLanzamiento().format(dtf)
//                ));

        //Usca episodios por pedazo de titulo
//        System.out.println("Escribe el titulo del episodio que deseas ver: ");
//        var pedazoTitulo = teclado.nextLine();
//        Optional<Episodio> episodioBuscar = episodios.stream()
//                .filter(e -> e.getTitulo().toUpperCase().contains(pedazoTitulo.toUpperCase()))
//                .findFirst();
//        if (episodioBuscar.isPresent()) {
//            System.out.println(" Episodio encontrado");
//            System.out.println(" Los datos son: " + episodioBuscar.get());
//        } else {
//            System.out.println(" No existe el episodio");
//        }

        Map<Integer, Double> evaluacionesPorTemporada = episodios.stream()
                .filter( e-> e.getEvaluacion() > 0.0)
                .collect(Collectors.groupingBy(Episodio::getTemporada,
                        Collectors.averagingDouble(Episodio::getEvaluacion)));
        System.out.println(evaluacionesPorTemporada);

        DoubleSummaryStatistics est = episodios.stream()
                .filter(e -> e.getEvaluacion() > 0.0)
                .collect(Collectors.summarizingDouble(Episodio::getEvaluacion));
        System.out.println(" La media de evlauacion de los episodios es: " + est.getAverage());
        System.out.println(" El episodio mejora valorado tiene una calificacion de: " + est.getMax());
        System.out.println(" El episodio peor valorado tiene una calificacion de: " + est.getMin());
    }

}