package com.alura.screenmatch.principal;

import com.alura.screenmatch.Excepciones.ErrorEnConversionDeDuracionException;
import com.alura.screenmatch.modelos.Titulo;
import com.alura.screenmatch.modelos.TituloOmdb;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.FileWriter;
import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import static java.net.http.HttpResponse.*;

public class PrincipalConBusqueda {
    public static void main(String[] args) throws IOException, InterruptedException {

        Scanner lectura = new Scanner(System.in);
        List<Titulo> titulosList = new ArrayList<>();
        Gson gson = new GsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.UPPER_CAMEL_CASE)
                .setPrettyPrinting()
                .create();

        String apiK = "Colocar aqui la api Key";

        while (true){
            System.out.println("Escriba el nombre de la pelicula");
            var busqueda = lectura.nextLine();

            if (busqueda.equalsIgnoreCase("salir")){
                break;
            }

            busqueda = URLEncoder.encode(busqueda,"UTF-8");
            String direccion = "https://www.omdbapi.com/?t="+ busqueda +"&apikey=" + apiK;
            try {
                HttpClient client = HttpClient.newHttpClient();

                HttpRequest request = HttpRequest.newBuilder()
                        .uri(URI.create(direccion))
                        .build();


                //respuesta
                HttpResponse<String> response = client
                        //InteliJ recomienda manejar
                        .send(request, BodyHandlers.ofString());

                System.out.println(response.body());
                String json = response.body();


                //    Titulo miTitulo = gson.fromJson(json, Titulo.class);
                Titulo miTitulo = gson.fromJson(json, Titulo.class);
                TituloOmdb miTituloomdb = gson.fromJson(json, TituloOmdb.class);
                System.out.println("Titulo: " + miTitulo.getNombre());
                System.out.println(miTitulo);
                System.out.println(miTituloomdb);
                System.out.println("**************** try catch ***************");


                Titulo miTituloConOmdb = new Titulo(miTituloomdb);
                System.out.println("Titulo convertido: " + miTituloConOmdb);

             //   FileWriter escritura = new FileWriter("peliculas.txt");

              //  escritura.write(miTitulo.toString());
                //siempre cerrar la comunicacion para evitar que se haga lento el programa
              //  escritura.close();

                titulosList.add(miTitulo);


            } catch (NumberFormatException e) {
                System.out.println("Ocurrio un error:");
                System.out.println(e.getMessage());
            }catch (IllegalArgumentException e){
                System.out.println("Error URI verifique la direcion:");

            }catch (ErrorEnConversionDeDuracionException e){
                // Exception es la clase madre de todas las excepciones colocarla puede ser guenerica y hacer que estemos mucho tiempo buscando error.
                //
                System.out.println(e.getMessage());
            }
        }


        FileWriter escritura = new FileWriter("titulos.json");
        escritura.write(gson.toJson(titulosList));
        escritura.close();
        System.out.println("Finalizo la ejecucion del programa");


    }
}
