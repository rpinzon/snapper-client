package org.rpinzon.snapper;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.rpinzon.snapper.model.Data;
import org.rpinzon.snapper.util.Configs;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.env.SimpleCommandLinePropertySource;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 * Created by Renan T. Pinzon on 02/08/18.
 */
@SpringBootApplication
public class StartApp {

    private static final Logger LOG = LoggerFactory.getLogger(StartApp.class);

    private static final String FILENAME = "filename";

    private static final String SERVER = "server";

    private static final String PORT = "port";

    public static void main(String[] args) {
        SimpleCommandLinePropertySource commandLine = new SimpleCommandLinePropertySource(args);
        String filename = filename(commandLine);
        String uri = uri(commandLine);
        SpringApplication.run(StartApp.class, args);
        ObjectMapper mapper = new ObjectMapper();
        RestTemplate restTemplate = new RestTemplate();

        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                try {
                    Data data = mapper.readValue(line, Data.class);
                    restTemplate.postForEntity(uri, data, Data.class);
                } catch (JsonParseException | JsonMappingException e) {
                    LOG.error("Invalid line content!! Skipping to the next line!", e);
                }
            }
        } catch (IOException e) {
            LOG.error("Error attempting to read file " + filename, e);
        } catch (RestClientException e) {
            LOG.error("Error attempting to call the service at " + uri, e);
        }

    }

    private static String filename(SimpleCommandLinePropertySource line) {
        if (!line.containsProperty(FILENAME)) {
            LOG.error("Invalid parameters!! Missing filename!");
            System.exit(1);
        }
        return line.getProperty(FILENAME);
    }

    private static String uri(SimpleCommandLinePropertySource line) {
        String server = line.containsProperty(SERVER) ? line.getProperty(SERVER) : Configs.REST_SERVER;
        String port = line.containsProperty(PORT) ? line.getProperty(PORT) : Configs.REST_SERVER_PORT;
        String service = "http://${server}:${port}/" + Configs.REST_CONTEXT + "/" + Configs.REST_POST_SERVICE;
        return service.replace("${server}", server).replace("${port}", port);
    }

}
