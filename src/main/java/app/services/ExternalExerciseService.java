package app.services;

import app.dtos.ExternalExerciseDTO;
import app.utils.Utils;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.List;

    public class ExternalExerciseService {
        private static final String BASE_URL = "https://api.api-ninjas.com/v1/exercises";
        private final HttpClient client = HttpClient.newHttpClient();
        private final ObjectMapper mapper;
        private final String apiKey = Utils.getPropertyValue("API_NINJAS_KEY", "config.properties");

        public ExternalExerciseService(ObjectMapper mapper) {
            this.mapper = mapper;
            if (apiKey == null || apiKey.isBlank()) {
                throw new IllegalStateException("Missing env var API_NINJAS_KEY");
            }
        }

        public List<ExternalExerciseDTO> searchByName(String name) {
            return call("name", name);
        }
        public List<ExternalExerciseDTO> searchByMuscle(String muscle) {
            return call("muscle", muscle);
        }

        private List<ExternalExerciseDTO> call(String key, String value) {
            try {
                String q = URLEncoder.encode(value == null ? "" : value, StandardCharsets.UTF_8);
                String url = BASE_URL + "?" + key + "=" + q;

                HttpRequest req = HttpRequest.newBuilder(URI.create(url))
                        .timeout(Duration.ofSeconds(15))
                        .header("X-Api-Key", apiKey)
                        .GET()
                        .build();

                HttpResponse<String> res = client.send(req, HttpResponse.BodyHandlers.ofString());
                if (res.statusCode() != 200) {
                    throw new RuntimeException("API Ninjas error: " + res.statusCode() + " -> " + res.body());
                }
                return mapper.readValue(res.body(), new TypeReference<List<ExternalExerciseDTO>>() {});
            } catch (Exception e) {
                throw new RuntimeException("Failed to query API Ninjas: " + e.getMessage(), e);
            }
        }
    }

