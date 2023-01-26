package web_protocols;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.FileWriter;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class WorkingAPI {

    public static void createNewObject() throws Exception {
        HttpClient httpClient = HttpClient.newHttpClient();
        HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(URI.create("https://jsonplaceholder.typicode.com/users"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofFile(Paths.get("D:\\Programming\\Java" +
                        "\\homework-module-13\\src\\main\\java\\res\\user.json")))
                .build();

        HttpResponse<String> httpResponse = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());

        System.out.println(httpResponse.statusCode() + httpResponse.body());
    }

    public static void updateObject() throws Exception {
        HttpClient httpClient = HttpClient.newHttpClient();
        HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(URI.create("https://jsonplaceholder.typicode.com/users/1"))
                .header("Content-Type", "application/json")
                .PUT(HttpRequest.BodyPublishers.ofFile(Paths.get("D:\\Programming\\Java" +
                        "\\homework-module-13\\src\\main\\java\\res\\user.json")))
                .build();

        HttpResponse<String> httpResponse = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());

        System.out.println(httpResponse.statusCode() + httpResponse.body());
    }

    public static void deleteObject() throws Exception {
        HttpClient httpClient = HttpClient.newHttpClient();
        HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(URI.create("https://jsonplaceholder.typicode.com/users/6"))
                .DELETE()
                .build();

        HttpResponse<String> httpResponse = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());

        System.out.println(httpResponse.statusCode() + httpResponse.body());
    }

    public static void getInformationAboutAllUsers() throws Exception {
        HttpClient httpClient = HttpClient.newHttpClient();
        HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(URI.create("https://jsonplaceholder.typicode.com/users"))
                .GET()
                .build();

        HttpResponse<String> httpResponse = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());

        System.out.println(httpResponse.statusCode() + httpResponse.body());
    }

    public static void getInformationOnId() throws Exception {
        HttpClient httpClient = HttpClient.newHttpClient();
        HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(URI.create("https://jsonplaceholder.typicode.com/users/1"))
                .GET()
                .build();

        HttpResponse<String> httpResponse = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());

        System.out.println(httpResponse.statusCode() + httpResponse.body());
    }

    public static void getInformationOnUsername() throws Exception {
        HttpClient httpClient = HttpClient.newHttpClient();
        HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(URI.create("https://jsonplaceholder.typicode.com/users?username=Kamren"))
                .GET()
                .build();

        HttpResponse<String> httpResponse = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());

        System.out.println(httpResponse.statusCode() + httpResponse.body());
    }

    public static void getAllComments() throws Exception {
        HttpClient httpClient = HttpClient.newHttpClient();
        HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(URI.create("https://jsonplaceholder.typicode.com/users/1/posts"))
                .GET()
                .build();

        HttpResponse<String> httpResponse = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());

        System.out.println(httpResponse.statusCode() + httpResponse.body());

        ObjectMapper objectMapper = new ObjectMapper();

        List<Post> posts = objectMapper.readValue(httpResponse.body(), new TypeReference<>() {});

        Optional<Integer> max = posts.stream()
        .map(Post::getId)
        .max(Integer::compareTo);

        String URL = "https://jsonplaceholder.typicode.com/posts/".concat(max.get().toString()).concat("/comments");

        HttpRequest secondRequest = HttpRequest.newBuilder()
        .uri(URI.create(URL))
        .GET()
        .build();

        HttpResponse<String> secondResponse = httpClient.send(secondRequest, HttpResponse.BodyHandlers.ofString());

        List<Comment> comments = objectMapper.readValue(secondResponse.body(), new TypeReference<>() {});

        List<String> strings = comments.stream()
        .map(Comment::getBody)
        .toList();

        System.out.println(strings);

        FileWriter fileWriter = new FileWriter("D:\\Programming\\Java" +
                "\\homework-module-13\\src\\main\\java\\res\\user-1-post-10-comments.json");
        fileWriter.write(strings.toString());
        fileWriter.flush();
        fileWriter.close();
    }

    public static void openTasksById() throws Exception {
        HttpClient httpClient = HttpClient.newHttpClient();
        HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(URI.create("https://jsonplaceholder.typicode.com/users/1/todos"))
                .GET()
                .build();

        HttpResponse<String> response = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());

        ObjectMapper objectMapper = new ObjectMapper();
        Todo[] todos = objectMapper.readValue(response.body(), Todo[].class);

        System.out.println("Total tasks: " + todos.length);

        long count = Arrays.stream(todos)
                .filter(Todo::isCompleted)
                .count();
        System.out.println("Total completed tasks: " + count);

        List<Integer> completedTaskIds = Arrays.stream(todos)
                .filter(Todo::isCompleted)
                .map(Todo::getId).toList();
        completedTaskIds.forEach(System.out::println);
    }
}
