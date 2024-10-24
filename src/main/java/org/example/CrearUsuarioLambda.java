package org.example;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.github.javafaker.Faker;

/**
 * Hello world!
 *
 */
public class CrearUsuarioLambda implements RequestHandler<Input, Output>
{
    private final AmazonDynamoDB client = AmazonDynamoDBClientBuilder.standard().build();
    private final DynamoDB dynamoDB = new DynamoDB(client);
    private final String tableName = "usuarios_tabla";

    @Override
    public Output handleRequest(Input input, Context context) {



        Faker faker = new Faker();
        String nombre = input.getNombre();
        String correo = input.getEmail();

        // Validaciones sencillas
        if (nombre == null || nombre.trim().isEmpty()) {
            return new Output("Error: El nombre no puede estar vacío", null);
        }

        if (correo == null || correo.trim().isEmpty()) {
            return new Output("Error: El correo no puede estar vacío", null);
        }

        if (!correo.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) { // Validar formato del correo
            return new Output("Error: El formato del correo no es válido", null);
        }

        String id = faker.number().randomNumber() + "";
        Usuario usuario = new Usuario(id, nombre, correo);
        try {
            Table table = dynamoDB.getTable(tableName);
            Item item = new Item()
                    .withPrimaryKey("id", id)
                    .withString("telefono", "3026468094")
                    .withString("nombre", nombre)
                    .withString("correo", correo);
            table.putItem(item);
        } catch (Exception e) {
            context.getLogger().log("Error al guardar el usuario en DynamoDB: " + e.getMessage());
            return new Output("Error al guardar el usuario en la base de datos", null);
        }
        String message = "Usuario creado exitosamente";
        Output output = new Output(message, usuario);

        QueueService.sendMessage(usuario);

        return output;
    }


    public static void main(String[] args) {

    }
}
