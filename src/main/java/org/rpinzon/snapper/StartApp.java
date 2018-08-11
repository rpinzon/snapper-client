package org.rpinzon.snapper;

import org.rpinzon.snapper.model.Data;
import org.rpinzon.snapper.util.Configs;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.SimpleCommandLinePropertySource;
import org.springframework.web.client.RestTemplate;

/**
 * Created by Renan T. Pinzon on 02/08/18.
 */
@SpringBootApplication
public class StartApp {

    private static final String PATH = "path";

    private static final String SERVER = "server";

    private static final String PORT = "port";

    public static void main(String[] args) {
        SimpleCommandLinePropertySource line = new SimpleCommandLinePropertySource(args);
        String path = path(line);
        String uri = uri(line);
        SpringApplication.run(StartApp.class, args);
        System.out.println("uri = " + uri);
        System.out.println("path = " + path);
        RestTemplate restTemplate = new RestTemplate();
        Data data = new Data();
        data.setPk(1);
        data.setScore(200);
        restTemplate.postForEntity(uri, data, Data.class);
    }

    private static String path(SimpleCommandLinePropertySource line) {
        if (!line.containsProperty(PATH)) {
            System.out.println("Invalid parameters!! Missing path to watch for files!");
            System.exit(1);
        }
        return line.getProperty(PATH);
    }

    private static String uri(SimpleCommandLinePropertySource line) {
        String server = line.containsProperty(SERVER) ? line.getProperty(SERVER) : Configs.REST_SERVER;
        String port = line.containsProperty(PORT) ? line.getProperty(PORT) : Configs.REST_SERVER_PORT;
        String service = "http://${server}:${port}/" + Configs.REST_CONTEXT + "/" + Configs.REST_POST_SERVICE;
        return service.replace("${server}", server).replace("${port}", port);
    }

}
